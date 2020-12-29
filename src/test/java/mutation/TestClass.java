package mutation;


import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import ast.Duplicate;
import ast.Insert;
import ast.Program;
import ast.ProgramImpl;
import ast.Remove;
import ast.Replace;
import ast.Swap;
import ast.Transform;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import parse.Parser;
import parse.ParserFactory;
import parse.PrettyPrinter;
public class TestClass {

	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue {
		Insert i = new Insert();
		Remove rem = new Remove();
		Replace rep = new Replace();
		Transform t = new Transform();
		Swap s = new Swap();
		Duplicate d = new Duplicate();
		PrettyPrinter pp = new PrettyPrinter();
		StringBuilder sb = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		
		
		Reader r = new FileReader("unmutated_critter.txt");
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		ProgramImpl p = (ProgramImpl) prog.get();

		p.establishParentLinks22();
		
		pp.PrettyPrint(p, sb);
		System.out.println(sb);
		for (int j = 0; j < 500; j++) {
			Program f = p.mutate();
			p = (ProgramImpl) f;
		}
		p.establishParentLinks22();

	
		p.establishParentLinks22();
		pp.PrettyPrint(p, sb);
		System.out.println(sb);
		for(int j = 0; j< 500; j++) {
			Program f = p.mutate();
			p = (ProgramImpl)f;
			p.establishParentLinks22();
		}
		

		pp.PrettyPrint(p, sb2);
		System.out.println(sb2);

	}
}