package com.creditsuisse.test.logparser;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.creditsuisse.test.logparser.model.AlertEvent;
import com.creditsuisse.test.logparser.model.LogEvent;
import com.creditsuisse.test.logparser.validator.LogFileValidator;
import com.google.gson.Gson;

public class LogParserMain {

	private static final Logger logger = LoggerFactory
			.getLogger(LogParserMain.class);

	private static final SessionFactory sessionFactory = new Configuration()
			.configure().buildSessionFactory();

	/**
	 * This method will validate log file and read each line and process it one
	 * at a time
	 * 
	 * @param path
	 *            : Input log file path
	 */
	static void readLogEventsFromFile(String path) {

		final String method = "readLogEventsFromFile";
		logger.debug("Entering " + method);

		// calling a validator class to check if input file is readable and file
		// path is correct.
		LogFileValidator validator = new LogFileValidator();
		if (!validator.isValidFile(path)) {
			return;
		}

		// map to store log id -> LogEvent mapping
		Map<String, LogEvent> map = new HashMap<>();

		// create a hibernate session
		Session session = sessionFactory.openSession();
		session.beginTransaction();

		try (Stream<String> lines = Files.lines(Paths.get(path))) {
			lines.forEach(line -> processLogEventLines(map, line, session));
		} catch (IOException e) {
			logger.error("File not found exception: {}", e.getMessage());
		}

		session.getTransaction().commit();
		session.close();
		sessionFactory.close();

		logger.debug("Exiting " + method);
	}

	/**
	 * This function will process each log line by comparing it with previous
	 * line an will add alert event when time difference between 2 log
	 * statements is > 4 ms
	 * 
	 * @param map
	 *            - Map of id -> LogEvent to maintain mapping
	 * @param line
	 *            - current line being processed
	 * @param session
	 *            - hibernate session
	 */
	private static void processLogEventLines(Map<String, LogEvent> map,
			String line, Session session) {

		final String method = "processLogEventLines";
		logger.debug("Entering " + method);

		LogEvent logEvent = new Gson().fromJson(line, LogEvent.class);
		LogEvent previous = map.putIfAbsent(logEvent.getId(), logEvent);

		if (previous != null) {
			AlertEvent alertEvent = createAlertLog(previous, logEvent);
			session.saveOrUpdate(alertEvent);
			map.remove(previous.getId());
		}

		logger.debug("Exiting " + method);
	}

	/**
	 * Create POJO for AlertEvent
	 * 
	 * @param previousEvent
	 *            - START event
	 * @param currentEvent
	 *            - FINISH event
	 * @return
	 */
	private static AlertEvent createAlertLog(LogEvent previousEvent,
			LogEvent currentEvent) {

		final String method = "generateAlertLogs";
		logger.debug("Entering " + method);

		AlertEvent alertEvent = new AlertEvent();
		alertEvent.setId(previousEvent.getId());
		alertEvent.setEventDuration(calculateTime(previousEvent.getTimestamp(),
				currentEvent.getTimestamp()));
		alertEvent.setHost(previousEvent.getHost());
		alertEvent.setType(previousEvent.getType());
		alertEvent.setAlert(alertEvent.getEventDuration() > 4);

		logger.debug("Exiting " + method);

		return alertEvent;
	}

	/**
	 * Calculate difference between start and end time
	 * 
	 * @param start
	 *            - start time
	 * @param end
	 *            - end time
	 * @return
	 */
	private static long calculateTime(long start, long end) {
		return start > end ? start - end : end - start;
	}
}
