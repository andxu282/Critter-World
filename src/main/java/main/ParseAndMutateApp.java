package main;


import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;

import ast.Program;
import ast.ProgramImpl;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import parse.Parser;
import parse.ParserFactory;
import parse.PrettyPrinter;


public class ParseAndMutateApp {
	public static void main(String[] args) throws SyntaxError {
        int n = 0;
        String file = null;
		PrettyPrinter printer = new PrettyPrinter();
        try {
            if (args.length == 1) {
                file = args[0];
				Reader r = new BufferedReader(new FileReader(file));
				Parser parser = ParserFactory.getParser();
				Maybe<Program> prog = parser.parse(r);
				if (!prog.isPresent())
					throw new SyntaxError("Invalid Program");
				ProgramImpl p = (ProgramImpl) prog.get();
				StringBuilder sb1 = new StringBuilder();
				printer.PrettyPrint(p, sb1);
				System.out.println(sb1);
            } else if (args.length == 3 && args[0].equals("--mutate")) {
                n = Integer.parseInt(args[1]);
                if (n < 0) throw new IllegalArgumentException();
                file = args[2];
				Reader r = new BufferedReader(new FileReader(file));
				Parser parser = ParserFactory.getParser();
				Maybe<Program> prog = parser.parse(r);
				ProgramImpl p = (ProgramImpl) prog.get();
				StringBuilder sb2 = new StringBuilder();
				if (!prog.isPresent())
					throw new SyntaxError("Invalid Program");
				for (int i = 0; i < n; i++) {
					Program f = p.mutate();
					p = (ProgramImpl) f;
					p.establishParentLinks22();
				}
				printer.PrettyPrint(p, sb2);
				System.out.println(sb2);
            } else {
                throw new IllegalArgumentException();
            }
        } catch (IllegalArgumentException e) {
            System.out.println("Usage:\n  <input_file>\n  --mutate <n> <input_file>");
		} catch (NoMaybeValue e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			System.out.println("FILE NOT FOUND");
			e.printStackTrace();
		}
    }
}