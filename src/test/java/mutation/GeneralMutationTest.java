package mutation;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import ast.Mutation;
import ast.Program;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import parse.Parser;
import parse.ParserFactory;
import parse.PrettyPrinter;

public class GeneralMutationTest {
	public static void test(int i, Mutation m) throws FileNotFoundException {
		PrettyPrinter pp = new PrettyPrinter();
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		Reader r = new FileReader("/Users/ishaanchansarkar/Documents/GitA5/ajx8-ezj4-ic254-critterworld/src/test/java/mutation/Tester");
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		Program p;
		try {
			p = prog.get();
			p.establishParentLinks22();
			pp.PrettyPrint(p, sb);
			System.out.println(sb);
		} catch (NoMaybeValue e) {
			System.out.println("Couldn't parse program.");
		return;
		}
		Maybe<Program> mutated = p.mutate(i, m);
		try {
			Program mutatedProgram = mutated.get();
			mutatedProgram.establishParentLinks22();
			pp.PrettyPrint(mutatedProgram, sb2);
			System.out.println(sb2);
		} catch (NoMaybeValue e) {
			System.out.println("Failed mutation.");
			System.out.println();
		}
	}

}
