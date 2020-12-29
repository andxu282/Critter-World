package parser;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import org.junit.jupiter.api.Test;

import ast.Program;
import cms.util.maybe.Maybe;
import parse.Parser;
import parse.ParserFactory;

/** This class contains tests for the Critter parser. */
public class ParserTest {

    /** Checks that a valid critter program is not {@code null} when parsed. */
    @Test
    public void testProgramIsNotNull() {
        InputStream in = ClassLoader.getSystemResourceAsStream("files/draw_critter.txt");
        Reader r = new BufferedReader(new InputStreamReader(in));
        Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
    }

	/** Checks that a valid critter program is not {@code null} when parsed. */
	@Test
	public void testExampleRules() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/example-rules.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/** Checks that a valid critter program is not {@code null} when parsed. */
	@Test
	public void testUnmutatedCritter() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/unmutated_critter.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed. More
	 * complex exprs.
	 */
	@Test
	public void testCritter1() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/critter_1.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed. No
	 * spaces and lots of parentheses.
	 */
	@Test
	public void testCritter2() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/critter_2.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed. List of
	 * updates.
	 */
	@Test
	public void testCritter3() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/critter_3.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is {@code null} when parsed. Missing
	 * semicolon.
	 */
	@Test
	public void testCritter4() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/critter_4.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertFalse(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed.
	 */
	@Test
	public void testMutatedCritter1() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/mutated_critter_1.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed.
	 */
	@Test
	public void testMutatedCritter2() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/mutated_critter_2.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed.
	 */
	@Test
	public void testMutatedCritter3() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/mutated_critter_3.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed.
	 */
	@Test
	public void testMutatedCritter4() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/mutated_critter_4.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed.
	 */
	@Test
	public void testMutatedCritter5() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/mutated_critter_5.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed.
	 */
	@Test
	public void testMutatedCritter6() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/mutated_critter_6.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is {@code null} when parsed. Typos in the
	 * file.
	 */
	@Test
	public void testMeantToBeakCritter() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/meant_to_break.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertFalse(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is {@code null} when parsed. Empty file
	 * has no rules
	 */
	@Test
	public void testEmpty() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/empty.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertFalse(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed. Comment
	 * takes up entire line.
	 */
	@Test
	public void testComments1() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/comments_1.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed. Same
	 * line and new line comments
	 */
	@Test
	public void testComments2() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/comments_2.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed. Starts
	 * off with full line comments
	 */
	@Test
	public void testComments3() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/comments_3.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is not {@code null} when parsed. Ends
	 * with full line comments
	 */
	@Test
	public void testComments4() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/comments_4.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertTrue(prog.isPresent(), "A valid critter program should not be null.");
	}

	/**
	 * Checks that a valid critter program is {@code null} when parsed. All
	 * comments, so no rules and should not be valid
	 */
	@Test
	public void testAllComments() {
		InputStream in = ClassLoader.getSystemResourceAsStream("files/all_comments.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		assertFalse(prog.isPresent(), "A valid critter program should not be null.");
	}
}
