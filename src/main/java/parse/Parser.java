package parse;

import java.io.Reader;

import ast.Program;
import cms.util.maybe.Maybe;

/** An interface for parsing a critter program. */
public interface Parser {

	/**
	 * Parses a program in the given file.
	 *
	 * @param r A reader to read the program
	 * @return The parsed program, or Maybe.none if the program contains a syntax
	 *         error.
	 */
	Maybe<Program> parse(Reader r);
}
