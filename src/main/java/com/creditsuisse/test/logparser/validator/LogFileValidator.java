package com.creditsuisse.test.logparser.validator;

import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LogFileValidator {

	private static final Logger logger = LoggerFactory
			.getLogger(LogFileValidator.class);

	/**
	 * Checks if file path is valid and file is valid
	 * 
	 * @param filePath
	 *            - input log file path
	 * @return
	 */
	public boolean isValidFile(String filePath) {
		final String method = "isValidFile";
		logger.debug("Entering " + method);

		try {
			Path path = Paths.get(filePath);
			if (!Files.isReadable(path)) {
				logger.error("Cannot read file!");
				return false;
			}
		} catch (InvalidPathException | NullPointerException ex) {
			logger.error("File Path is not correct!");
			return false;
		}

		logger.debug("Exiting " + method);
		return true;
	}
}
