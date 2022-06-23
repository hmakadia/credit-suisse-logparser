package com.creditsuisse.test.logparser;

import org.junit.Assert;
import org.junit.Test;

import com.creditsuisse.test.logparser.validator.LogFileValidator;

public class LogParserMainTest {
	private static final String FILE = "../logparser/src/main/resources/logs.txt";

	@Test
	public void test_isValidFile_correct() {
		Assert.assertTrue(new LogFileValidator().isValidFile(FILE));
	}

	@Test
	public void test_isValidFile_not_correct() {
		Assert.assertFalse(new LogFileValidator()
				.isValidFile("../invalid.txt"));
	}

	@Test
	public void test_readLogEventsFromFile() {
		LogParserMain.readLogEventsFromFile(FILE);
	}

}
