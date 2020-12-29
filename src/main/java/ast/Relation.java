package ast;

import cms.util.maybe.NoMaybeValue;
import parse.TokenType;

public class Relation extends Condition {
	public final TokenType[] tokenTypes = { TokenType.LT, TokenType.LE, TokenType.EQ,
			TokenType.GE, TokenType.GT, TokenType.NE};
	

	/**
	 * Create an AST representation of l op r.
	 *
	 * @param l
	 * @param op
	 * @param r
	 */

	public Relation(Expr l, Expr r, TokenType t) {
		getChildren().add(l);
		getChildren().add(r);
		this.tokenType = t;

	}

	@Override
	public boolean classInv() {
		try {
			if (this.getParent().get().getCategory().equals(NodeCategory.BINARYCOND)
					|| this.getParent().get().getCategory().equals(NodeCategory.RULE)) {
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
		return NodeCategory.RELATION;
	}


}
