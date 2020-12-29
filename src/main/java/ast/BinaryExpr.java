package ast;

import cms.util.maybe.NoMaybeValue;
import parse.TokenType;
/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryExpr extends Expr {
	private boolean isSecondChild = false;
	private ExprOperator op;
	private ExprType exprType;
	public final TokenType[] operators = { TokenType.PLUS, TokenType.MINUS, TokenType.MUL,
			TokenType.DIV, TokenType.MOD};

	/**
	 * Create an AST representation of l op r.
	 *
	 * @param l  Left expression
	 * @param op Some operator (+, -, *, /, mod)
	 * @param r  Right expression
	 */
	public BinaryExpr(Expr l, ExprOperator op, Expr r, TokenType tt) {
		this.addChild(l);
		this.addChild(r);
		this.op = op;
		this.tokenType = tt;
	}

	/** An enumeration of all possible binary expression operators. */
	public enum ExprOperator {
		ADDOP, MULOP;
	}
	
	public enum ExprType {
		MEM, NORM
	}

	/**
	 * Gets the operator of this expression.
	 */
	public ExprOperator getExprOperator() {
		return this.op;
	}

	/**
	 * Sets the operator of this expression.
	 */
	public void setExprOperator(ExprOperator op) {
		this.op = op;
	}

	/**
	 * Gets the expression type of this expression.
	 */
	public ExprType getExprType() {
		return this.exprType;
	}

	/**
	 * Sets if this binary expression is a second child.
	 */
	public void setIsSecondChild(boolean isSecondChild) {
		this.isSecondChild = isSecondChild;
	}

	/**
	 * Gets if this binary expression is a second child.
	 */
	public boolean getIsSecondChild() {
		return this.isSecondChild;
	}

	@Override
	public boolean classInv() {
		try {
			if (this.getParent().get().getCategory().equals(NodeCategory.RELATION)
					|| this.getParent().get().getCategory().equals(NodeCategory.EXPRESSION)) {
				if (this.getChildren().size() == 2) {
					if (this.getChildren().get(0).getCategory().equals(NodeCategory.EXPRESSION)
							&& this.getChildren().get(1).getCategory().equals(NodeCategory.EXPRESSION))
						return true;
				}
			}
		} catch (NoMaybeValue e) {
			return false;
		}
		return false;
	}
	
	public NodeCategory getCategory() {
		return NodeCategory.BINARYEXPR;
	}

}
