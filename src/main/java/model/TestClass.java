package model;

import java.io.FileNotFoundException;
import java.io.FileReader;

import ast.Program;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import parse.Parser;
import parse.ParserFactory;
import parse.PrettyPrinter;

public class TestClass  {
	
	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue{
	World w = new World();
	
	FileReader r = new FileReader("/Users/ishaanchansarkar/Documents/GitA5/ajx8-ezj4-ic254-critterworld/src/test/java/mutation/Tester");
	Parser parser = ParserFactory.getParser();
	PrettyPrinter pp = new PrettyPrinter();
	Maybe<Program> p = parser.parse(r);
	Program ok = p.get();
	//ok.establishParentLinks22();
	
	Executor e = new Executor();
	
	
	
	
	//StringBuilder sb = new StringBuilder();
	//pp.PrettyPrint(ok, sb);
	//System.out.println(sb);

	Critter c = new Critter("test", 7,10,8, 9, 20000, 8000, 0, ok);
	
	Interpreter i = new Interpreter(w);
	
	w.setTile(100, 100, c);
	
	//System.out.println(c.getMemory()[6]);
	

	

	//System.out.println(c.getMemory()[1]);
	System.out.println(i.interpret(c).getTokenType());
	//System.out.println(c.getMemory()[6]);
	//System.out.println(c.getC());
	//System.out.println(c.getR());
	//Action a = i.interpret(c);
	//System.out.println(a.act);
	e.forward(w, c);
	//System.out.println(c.getC());
	//System.out.println(c.getR());
	
	//System.out.println(c.getMemory()[6]);
	//System.out.println(c.getMemory()[1]);
	}	
	
	
}

