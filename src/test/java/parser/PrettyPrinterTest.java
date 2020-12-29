package parser;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ast.Action;
import ast.BinaryCondition;
import ast.BinaryExpr;
import ast.BinaryExpr.ExprOperator;
import ast.Node;
import ast.Number;
import ast.Number.NumberType;
import ast.Program;
import ast.ProgramImpl;
import ast.Relation;
import ast.Rule;
import ast.Sensor;
import ast.Update;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import exceptions.SyntaxError;
import parse.Parser;
import parse.ParserFactory;
import parse.TokenType;

class PrettyPrinterTest {
	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue, SyntaxError {
//		binaryConditionWithBraces();
//		binaryConditionWithManyBraces();
//		negativeNumbers();
//		binaryExpr();
//		binaryExprWithSensors();
//		binaryExprWithNegatives();
//		allBinaryExprs();
		binaryExprsWithManyParentheses();
//		binaryCondition();
//		action();
//		rule();

	}

	/*
	 * Tests negative numbers
	 */
	public static void negativeNumbers() { // tested
		Number b1 = new Number(-3, Number.NumberType.NUM);
		Number b2 = new Number(-45, Number.NumberType.NUM);
		Number b3 = new Number(0, Number.NumberType.NUM);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		b1.prettyPrint(sb1);
		b2.prettyPrint(sb2);
		b3.prettyPrint(sb3);
		System.out.println(sb1);
		System.out.println(b1.toString());
		System.out.println(sb2);
		System.out.println(b2.toString());
		System.out.println(sb3);
		System.out.println(b3.toString());
	}

	/*
	 * Tests simple binary expressions are printed out correctly
	 */
	public static void binaryExpr() { // tested
		BinaryExpr b1 = new BinaryExpr(new Number(3, Number.NumberType.NUM), ExprOperator.ADDOP,
				new Number(3, Number.NumberType.NUM), TokenType.PLUS);
		BinaryExpr b2 = new BinaryExpr(b1, ExprOperator.ADDOP, b1, TokenType.PLUS);
		BinaryExpr b3 = new BinaryExpr(b2, ExprOperator.ADDOP, b2, TokenType.PLUS);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		b1.prettyPrint(sb1);
		b2.prettyPrint(sb2);
		b3.prettyPrint(sb3);
		System.out.println(sb1);
		System.out.println(b1.toString());
		System.out.println(sb2);
		System.out.println(b2.toString());
		System.out.println(sb3);
		System.out.println(b3.toString());
	}

	/*
	 * Tests binary expressions with sensors
	 */
	public static void binaryExprWithSensors() { // tested
		BinaryExpr b1 = new BinaryExpr(new Sensor(new Number(3, NumberType.NUM), TokenType.NEARBY), ExprOperator.ADDOP,
				new Number(3, Number.NumberType.NUM), TokenType.PLUS);
		BinaryExpr b2 = new BinaryExpr(b1, ExprOperator.ADDOP,
				new Sensor(new Number(3, NumberType.NUM), TokenType.AHEAD), TokenType.PLUS);
		BinaryExpr b3 = new BinaryExpr(b2, ExprOperator.ADDOP, new Sensor(TokenType.SMELL), TokenType.PLUS);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		b1.prettyPrint(sb1);
		b2.prettyPrint(sb2);
		b3.prettyPrint(sb3);
		System.out.println(sb1);
		System.out.println(b1.toString());
		System.out.println(sb2);
		System.out.println(b2.toString());
		System.out.println(sb3);
		System.out.println(b3.toString());
	}

	/*
	 * Tests binary expressions with negative numbers
	 */
	public static void binaryExprWithNegatives() {
		BinaryExpr b1 = new BinaryExpr(new Number(-3, Number.NumberType.NUM), ExprOperator.ADDOP,
				new Number(-3, Number.NumberType.NUM), TokenType.PLUS);
		BinaryExpr b2 = new BinaryExpr(b1, ExprOperator.ADDOP, b1, TokenType.PLUS);
		BinaryExpr b3 = new BinaryExpr(b2, ExprOperator.ADDOP, b2, TokenType.PLUS);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		b1.prettyPrint(sb1);
		b2.prettyPrint(sb2);
		b3.prettyPrint(sb3);
		System.out.println(sb1);
		System.out.println(b1.toString());
		System.out.println(sb2);
		System.out.println(b2.toString());
		System.out.println(sb3);
		System.out.println(b3.toString());
	}

	/*
	 * Tests all binary expressions
	 */
	public static void allBinaryExprs() throws FileNotFoundException, SyntaxError, NoMaybeValue {
		Reader r = new BufferedReader(new FileReader(
				"/Users/andrew/eclipse-workspace/ajx8-ezj4-ic254-critterworld/src/test/resources/files/all_binary_exprs.txt"));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		if (!prog.isPresent())
			throw new SyntaxError("Invalid Program");
		ProgramImpl p = (ProgramImpl) prog.get();
		StringBuilder sb1 = new StringBuilder();
		p.prettyPrint(sb1);
		System.out.println(sb1);
	}

	/*
	 * Tests binary expressions with many parentheses
	 */
	public static void binaryExprsWithManyParentheses() throws FileNotFoundException, SyntaxError, NoMaybeValue {
		Reader r = new BufferedReader(new FileReader(
				"/Users/andrew/eclipse-workspace/ajx8-ezj4-ic254-critterworld/src/test/resources/files/binary_exprs_with_many_parentheses.txt"));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		if (!prog.isPresent())
			throw new SyntaxError("Invalid Program");
		ProgramImpl p = (ProgramImpl) prog.get();
		StringBuilder sb1 = new StringBuilder();
		p.prettyPrint(sb1);
		System.out.println(sb1);
	}

	/*
	 * Tests simple binary conditions are printed out correctly. Tests relations.
	 */
	public static void binaryCondition() {
		BinaryCondition b1 = new BinaryCondition(
				new Relation(new Number(3, NumberType.NUM), new Number(3, NumberType.NUM), TokenType.LE),
				BinaryCondition.Operator.and,
				new Relation(new Number(3, NumberType.NUM), new Number(3, NumberType.NUM), TokenType.LE));
		BinaryCondition b2 = new BinaryCondition(b1, BinaryCondition.Operator.and, b1);
		BinaryCondition b3 = new BinaryCondition(b2, BinaryCondition.Operator.and, b2);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		b1.prettyPrint(sb1);
		b2.prettyPrint(sb2);
		b3.prettyPrint(sb3);
		System.out.println(sb1);
		System.out.println(b1.toString());
		System.out.println(sb2);
		System.out.println(b2.toString());
		System.out.println(sb3);
		System.out.println(b3.toString());
	}

	/*
	 * Tests binary conditions with a single brace to ensure unambiguous grammar.
	 */
	public static void binaryConditionWithBraces() throws FileNotFoundException, SyntaxError, NoMaybeValue {
		Reader r = new BufferedReader(new FileReader(
				"/Users/andrew/eclipse-workspace/ajx8-ezj4-ic254-critterworld/src/test/resources/files/binary_condition_with_braces.txt"));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		if (!prog.isPresent())
			throw new SyntaxError("Invalid Program");
		ProgramImpl p = (ProgramImpl) prog.get();
		StringBuilder sb1 = new StringBuilder();
		p.prettyPrint(sb1);
		System.out.println(sb1);
	}

	/*
	 * Tests complex binary conditions with many braces.
	 */
	public static void binaryConditionWithManyBraces() throws FileNotFoundException, SyntaxError, NoMaybeValue {
		Reader r = new BufferedReader(new FileReader(
				"/Users/andrew/eclipse-workspace/ajx8-ezj4-ic254-critterworld/src/test/resources/files/binary_condition_with_many_braces.txt"));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		if (!prog.isPresent())
			throw new SyntaxError("Invalid Program");
		ProgramImpl p = (ProgramImpl) prog.get();
		StringBuilder sb1 = new StringBuilder();
		p.prettyPrint(sb1);
		System.out.println(sb1);
	}

	/**
	 * Tests actions are printed out correctly (including serve)
	 */
	public static void action() {
		Action b1 = new Action(TokenType.ATTACK);
		Action b2 = new Action(TokenType.WAIT);
		Action b3 = new Action(TokenType.SERVE, new Number(3, NumberType.NUM));
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		b1.prettyPrint(sb1);
		b2.prettyPrint(sb2);
		b3.prettyPrint(sb3);
		System.out.println(sb1);
		System.out.println(b1.toString());
		System.out.println(sb2);
		System.out.println(b2.toString());
		System.out.println(sb3);
		System.out.println(b3.toString());
	}

	/*
	 * Tests the syntactic sugar is printed out correctly. Tests the --> is printed
	 * out correctly. Tests updates are correctly formatted
	 */
	public static void rule() {
		Action a1 = new Action(TokenType.ATTACK);
		Action a2 = new Action(TokenType.WAIT);
		Action a3 = new Action(TokenType.SERVE, new Number(3, NumberType.NUM));

		Update u1 = new Update(new Number(1, NumberType.MEM), new Number(1, NumberType.NUM));
		Update u2 = new Update(new Number(2, NumberType.MEM), new Number(2, NumberType.NUM));
		Update u3 = new Update(new Number(7, NumberType.MEM), new Number(7, NumberType.NUM));

		List<Node> cmd1 = new ArrayList<Node>();
		cmd1.add(u1);
		cmd1.add(a1);
		List<Node> cmd2 = new ArrayList<Node>();
		cmd2.add(u2);
		cmd2.add(a2);
		List<Node> cmd3 = new ArrayList<Node>();
		cmd3.add(u3);
		cmd3.add(a3);

		BinaryCondition bc1 = new BinaryCondition(
				new Relation(new Number(3, NumberType.NUM), new Number(3, NumberType.NUM), TokenType.LE),
				BinaryCondition.Operator.and,
				new Relation(new Number(3, NumberType.NUM), new Number(3, NumberType.NUM), TokenType.LE));
		BinaryCondition bc2 = new BinaryCondition(bc1, BinaryCondition.Operator.and, bc1);
		BinaryCondition bc3 = new BinaryCondition(bc2, BinaryCondition.Operator.and, bc2);
		Rule b1 = new Rule(bc1, cmd1);
		Rule b2 = new Rule(bc2, cmd2);
		Rule b3 = new Rule(bc3, cmd3);
		StringBuilder sb1 = new StringBuilder();
		StringBuilder sb2 = new StringBuilder();
		StringBuilder sb3 = new StringBuilder();
		b1.prettyPrint(sb1);
		b2.prettyPrint(sb2);
		b3.prettyPrint(sb3);
		System.out.println(sb1);
		System.out.println(b1.toString());
		System.out.println(sb2);
		System.out.println(b2.toString());
		System.out.println(sb3);
		System.out.println(b3.toString());
	}



}
