/*
 * Licensed Materials - Property of IBM
 * 
 * (c) Copyright IBM Corp. 2021.
 */
package dev.galasa.textscan.internal;

import static org.junit.Assert.assertEquals;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

import dev.galasa.textscan.FailTextFoundException;
import dev.galasa.textscan.ITextScanner;
import dev.galasa.textscan.IncorrectOccurrencesException;
import dev.galasa.textscan.MissingTextException;
import dev.galasa.textscan.TextScanException;
import dev.galasa.textscan.TextScanManagerException;

public class TestTextScannerImplStringScanForMach {
	
	private static final String TEXT_STRING = "This is a textscanner test";
	private ITextScanner scanner;

	@Before
	public void beforeClass() {
		scanner = new TextScannerImpl();
	}

	// scanForMatch() with text String and search Pattern
	@Test
	public void testScanForMachForStringInputWithSearchPatternFound() throws TextScanManagerException {
		Pattern searchPattern = Pattern.compile("h");
		int count = 1;
		assertEquals("h", scanner.scanForMatch(TEXT_STRING, searchPattern, count));
	
	}
	
	@Test(expected = MissingTextException.class)
	public void testScanForMachForStringInputWithSearchPatternNotFound() throws TextScanManagerException {
		Pattern searchPattern = Pattern.compile("[5]");
		int count = 1;
		scanner.scanForMatch(TEXT_STRING, searchPattern, count);
	}
	
	@Test(expected = IncorrectOccurrencesException.class)
	public void testScanForMachForStringInputWithIncorrectOccurancesPattern() throws TextScanManagerException {
		Pattern searchPattern = Pattern.compile("[abc]");
		int count = 5;
		scanner.scanForMatch(TEXT_STRING, searchPattern, count) ;
	}
	
	@Test (expected = TextScanException.class)
	public void testScanForStringInputWithIncorrectOccurancesNumber() throws TextScanManagerException {
		Pattern searchPattern = Pattern.compile("[abc]");
		int count = 0;
		scanner.scanForMatch(TEXT_STRING, searchPattern, count) ;
	}
	
	// scanForMatch() with text String and search String
	@Test
	public void testScanForMachForStringInputWithSearchStringFound() throws IncorrectOccurrencesException, MissingTextException, TextScanException {
		String searchString = "This";
		int count = 1;
		assertEquals(searchString, scanner.scanForMatch(TEXT_STRING, searchString, count));
	}
	
	@Test(expected = MissingTextException.class)
	public void testScanForMachForStringInputWithSearchStringNotFound() throws IncorrectOccurrencesException, MissingTextException, TextScanException {
		String searchString = "Dragon";
		int count = 1;
		scanner.scanForMatch(TEXT_STRING, searchString, count);
	}
	
	@Test(expected = IncorrectOccurrencesException.class)
	public void testScanForMachForStringInputWithIncorrectOccurancesString() throws IncorrectOccurrencesException, MissingTextException, TextScanException {
		String searchString = "test";
		int count = 3;
		scanner.scanForMatch(TEXT_STRING, searchString, count);
	}
	
	@Test (expected = TextScanException.class)
	public void testScanForStringInputWithIncorrectOccurancesNumberString() throws IncorrectOccurrencesException, FailTextFoundException, MissingTextException, TextScanException {
		String searchString = "test";
		scanner = new TextScannerImpl();
		int count = 0;
		scanner.scanForMatch(TEXT_STRING, searchString, count) ;
	}
}
