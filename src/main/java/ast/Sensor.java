package ast;

import cms.util.maybe.NoMaybeValue;
import parse.TokenType;

public class Sensor extends Expr {
	public final TokenType[] tokenTypes = { TokenType.NEARBY, TokenType.AHEAD,
			TokenType.RANDOM, TokenType.SMELL};

	/**
	 * Creates a new Sensor without an expression.
	 * 
	 * @param t
	 */
	public Sensor(TokenType t) {
		tokenType = t;
	}

	/**
	 * Creates a new Sensor with an expression.
	 * 
	 * @param e
	 * @param t
	 */
	public Sensor(Expr e, TokenType t) {
		this.addChild(e);
		
		tokenType = t;

	}

	@Override
	public boolean classInv() {
		try {
			if (this.getParent().get().getCategory().equals(NodeCategory.BINARYEXPR)
					|| this.getParent().get().getCategory().equals(NodeCategory.RELATION)) {
				if (this.getChildren().size() == 1) {
					if (this.getChildren().get(0).getCategory().equals(NodeCategory.EXPRESSION))
						return true;
				} else if (this.getChildren().size() == 0) {
					return true;
				}
			}
		} catch (NoMaybeValue e) { // if there's no parent, then the invariant breaks
			return false;
		}
		return false;
	}
	
	public NodeCategory getCategory() {
		return NodeCategory.SENSOR;
	}


}
