package parse;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import ast.Action;
import ast.BinaryCondition;
import ast.BinaryExpr;
import ast.Command;
import ast.Condition;
import ast.Expr;
import ast.Node;
import ast.NodeCategory;
import ast.Number;
import ast.Number.NumberType;
import ast.Program;
import ast.ProgramImpl;
import ast.Relation;
import ast.Rule;
import ast.Sensor;
import ast.Update;
import cms.util.maybe.Maybe;
import exceptions.SyntaxError;

class ParserImpl implements Parser {

    @Override
	public Maybe<Program> parse(Reader r) {
		Tokenizer t = new Tokenizer(r);
		try {
			Program p = parseProgram(t);
			return Maybe.some(p);
		} catch (SyntaxError s) {
			s.printStackTrace();
			return Maybe.none();
		}
    }

    /**
	 * Parses a program from the stream of tokens provided by the Tokenizer,
	 * consuming tokens representing the program. All following methods with a name
	 * "parseX" have the same spec except that they parse syntactic form X.
	 * 
	 * @return the created AST
	 * @throws SyntaxError if there the input tokens have invalid syntax
	 */
	private static ProgramImpl parseProgram(Tokenizer t) throws SyntaxError {
		// takes in first token, makes sure it is a valid rule
		ProgramImpl p = new ProgramImpl(parseRule(t));
		// while there are more tokens, add another rule to p
		while (t.hasNext()) {
			p.addRule(parseRule(t));
		}
		return p;
    }

	private static Rule parseRule(Tokenizer t) throws SyntaxError {
		Rule r;
		// takes in first token, makes sure it is a valid condition
		Condition c = parseCondition(t);
		// checks and consumes a "-->"
		if (!t.next().getType().equals(TokenType.ARR)) {
			throw new SyntaxError("Rule not formatted correctly. Expected \"-->\" after condition");
		}
		// create a new rule
		r = new Rule(c, parseCommand(t));
		if (!t.next().getType().equals(TokenType.SEMICOLON))
			throw new SyntaxError("Rule not formatted correctly. Expected \";\" at end of rule");
		return r;
    }

	private static List<Node> parseCommand(Tokenizer t) throws SyntaxError {
		Command c;
		List<Node> cmdList = new ArrayList<>();
		try {
			c = parseUpdate(t);
			cmdList.add(c);
		} catch (SyntaxError e) {
			c = parseAction(t);
			cmdList.add(c);
		}
		// while there are more tokens,
		while (t.hasNext()) {
			if (t.peek().getType().equals(TokenType.SEMICOLON))
				return cmdList;
			// check if the next token is action, if it is, then it's the end
			if (t.peek().isAction()) {
				c = parseAction(t);
				cmdList.add(c);
				return cmdList;
			}
			// or else, just add another update to command
			c = parseUpdate(t);
			cmdList.add(c);
		}
		return cmdList;
	}

	private static Command parseUpdate(Tokenizer t) throws SyntaxError {
		Expr e;
		// checks if next token is "mem"
		if (t.peek().getType().equals(TokenType.MEM) || t.peek().isMemSugar()) {
			// parse a factor
			e = parseFactor(t);
		} else {
			throw new SyntaxError("Update or action not formatted correctly.");
		}
		// checks and consumes ":="
		
		if (!t.next().getType().equals(TokenType.ASSIGN))
			throw new SyntaxError("Update not formatted correctly. Expected \":=\" after \"]\"");
		Expr j = parseExpression(t);
		Update u = new Update(e, j);
		return u;
		

	}

	private static Command parseAction(Tokenizer t) throws SyntaxError {
		Action a;
		Token token = t.next();
		// if the action is anything but serve
		if (token.isAction() && !token.getType().equals(TokenType.SERVE)) {
			// System.out.println(token.getType());
			a = new Action(token.getType());
			return a;
			// otherwise, if it is serve
		} else if (token.getType().equals(TokenType.SERVE)) {
			// checks and consumes "["
			if (!t.next().getType().equals(TokenType.LBRACKET)) {
				throw new SyntaxError("Action not formatted correctly. Expected \"[\" after \"serve\"");
			}
			a = new Action(TokenType.SERVE, parseExpression(t));
			// checks and consumes a "]"
			if (!t.next().getType().equals(TokenType.RBRACKET)) {
				throw new SyntaxError("Action not formatted correctly. Expected \"]\" after expression");
			}
			return a;
		}
		else {
		// otherwise, it's an invalid action
		throw new SyntaxError(
				"Invalid action. Valid actions include wait, forward, backward, "
						+ "left, right, at, attack, grow, bud, mate, serve[expr]");
	}

	}

	private static Condition parseCondition(Tokenizer t) throws SyntaxError {
		// takes in first token, makes sure it is a valid conjunction
		Condition c = parseConjunction(t);
		while (t.peek().getType().equals(TokenType.OR)) { // makes it left associative
			t.next();
			c = new BinaryCondition(c, BinaryCondition.Operator.or, parseConjunction(t));
		}
		return c;
	}

	private static Condition parseConjunction(Tokenizer t) throws SyntaxError {
		// takes in first token, makes sure it is a valid relation
		Condition c = parseRelation(t);
		while (t.peek().getType().equals(TokenType.AND)) {
			t.next();
			c = new BinaryCondition(c, BinaryCondition.Operator.and, parseRelation(t));
		}
		return c;
	}

	private static Condition parseRelation(Tokenizer t) throws SyntaxError {
		Condition c;
		// checks for the {condition} case, then consumes the "{" in the loop;
		if (t.peek().getType().equals(TokenType.LBRACE)) {
			t.next();
			c = parseCondition(t);
			if (!t.next().getType().equals(TokenType.RBRACE)) {
				throw new SyntaxError("Relation not formatted correctly. Expected \"}\" after condition");
			}
			return c;
		} else {
			Expr e = parseExpression(t);
			// checks and consumes a token of type rel
			if (!t.peek().isRelation()) {
				throw new SyntaxError("Invalid relation. Valid relations include <, <=, =, >=, >, !=");
			}
			TokenType tokentype = TokenType.getTypeFromString(t.next().toString());
			// otherwise if it is a relation, create a new relation
			c = new Relation(e, parseExpression(t), tokentype);
			return c;
		}
    }

	private static Expr parseExpression(Tokenizer t) throws SyntaxError {
		// takes in first token, makes sure it is a valid term
		Expr e = parseTerm(t);
		while (t.peek().isAddOp()) {

			TokenType tokentype = TokenType.getTypeFromString(t.next().toString());
			e = new BinaryExpr(e, BinaryExpr.ExprOperator.ADDOP, parseTerm(t), tokentype);

		}
		return e;
    }

	private static Expr parseTerm(Tokenizer t) throws SyntaxError {
		// takes in first token, makes sure it is a valid factor
		Expr e = parseFactor(t);
		while (t.peek().isMulOp()) {
			TokenType tokentype = TokenType.getTypeFromString(t.next().toString());
			e = new BinaryExpr(e, BinaryExpr.ExprOperator.MULOP, parseFactor(t), tokentype);

		}
		return e;
    }

	private static Expr parseFactor(Tokenizer t) throws SyntaxError {
		Expr e = null;
		if (t.peek().isMemSugar()) {
			switch (t.next().getType()) {
			case ABV_MEMSIZE:
				e = new Number(0, NumberType.MEM);
				break;
			case ABV_DEFENSE:
				e = new Number(1, NumberType.MEM);
				break;
			case ABV_OFFENSE:
				e = new Number(2, NumberType.MEM);
				break;
			case ABV_SIZE:
				e = new Number(3, NumberType.MEM);
				break;
			case ABV_ENERGY:
				e = new Number(4, NumberType.MEM);
				break;
			case ABV_PASS:
				e = new Number(5, NumberType.MEM);
				break;
			case ABV_POSTURE:
				e = new Number(6, NumberType.MEM);
				break;
			default:
				throw new SyntaxError("Factor not formatted correctly. Invalid factor.");
			}
			return e;
		}
		switch (t.peek().getType()) {
		case NUM:
			e = new Number(Integer.parseInt(t.next().toString()), NumberType.NUM);
			break;
		case MEM:
			// consumes "mem"
			t.next();
			// checks if there is a left square bracket and consumes it
			if (!t.next().getType().equals(TokenType.LBRACKET))
				throw new SyntaxError("Factor not formatted correctly. Expected \"[\" after \"mem\"");
			// parses another expression
			e = new Number(evaluateExpression(parseExpression(t)), NumberType.MEM);
			// checks if there is a right square bracket and consumes it
			if (!t.next().getType().equals(TokenType.RBRACKET))
				throw new SyntaxError("Factor not formatted correctly. Expected \"]\" after expression");
			break;
		case LPAREN:
			// consumes "("
			t.next();
			// parses another expression
			e = parseExpression(t);
			e.setParentheses(true);
			// checks if there is a right parentheses and consumes it
			if (!t.next().getType().equals(TokenType.RPAREN))
				throw new SyntaxError("Factor not formatted correctly. Expected \")\" after expression");
			break;
		case MINUS:
			t.next();
			e = new Number(-1 * Integer.parseInt(t.next().toString()), NumberType.NUM);
			break;
		default:
			e = parseSensor(t);
		}
		return e;
	}

	private static Expr parseSensor(Tokenizer t) throws SyntaxError {
		Expr sensor = null;
		Expr e;
		// if the sensor is smell
		if (t.peek().getType().equals(TokenType.SMELL)) {
			TokenType tokentype = TokenType.getTypeFromString(t.next().toString());
			// creates a sensor of type "smell"
			sensor = new Sensor(tokentype);
		} else {
			// checks and consumes a sensor token
			Token sensorToken = t.next();
			if (!sensorToken.isSensor())
				throw new SyntaxError(
						"Invalid sensor. Valid sensors include nearby[expr], ahead[expr], random[expr], smell");
			// checks if there is a left bracket and consumes it
			if (!t.next().getType().equals(TokenType.LBRACKET))
				throw new SyntaxError("Sensor not of right form. Expected [");
			// parses an expression
			e = parseExpression(t);
			// checks if there is a right bracket and consumes it
			if (!t.next().getType().equals(TokenType.RBRACKET))
				throw new SyntaxError("Sensor not of right form. Expected ]");
			// creates new sensor with expr
			sensor = new Sensor(e, sensorToken.getType());
		}
		return sensor;
    }
	
	/**
	 * Evaluates an expression.
	 * 
	 * @param e expression to be evaluated.
	 * @return value of expression
	 */
	public static int evaluateExpression(Expr e) {
		if(e.getCategory().equals(NodeCategory.BINARYEXPR)) {
		
			int leftChildValue = evaluateExpression((Expr)e.getChildren().get(0));
			int rightChildValue = evaluateExpression((Expr)(e.getChildren().get(1)));
			BinaryExpr be = (BinaryExpr) e;
			if (be.getTokenType().equals(TokenType.PLUS)) {
				return leftChildValue + rightChildValue;
			}
			else if (be.getTokenType().equals(TokenType.MINUS)) {
				return leftChildValue - rightChildValue;
			}
			else if (be.getTokenType().equals(TokenType.MUL)) {
				return leftChildValue * rightChildValue;
			}
			else if (be.getTokenType().equals(TokenType.DIV)) {
				return Math.floorDiv(leftChildValue, rightChildValue);
			}
			else {
				return Math.floorMod(leftChildValue, rightChildValue);
			}
		}
		else {
			Number x = (Number) e;
			return x.getValue();
		}
	}

    /**
     * Consumes a token of the expected type.
     *
     * @throws SyntaxError if the wrong kind of token is encountered.
     */
    public static void consume(Tokenizer t, TokenType tt) throws SyntaxError {
		TokenType actual = t.peek().getType();
		if (!actual.equals(tt))
			throw new SyntaxError("Unexpected Token Type");
		else
			t.next();
    }
}