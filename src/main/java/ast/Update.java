package ast;

import cms.util.maybe.NoMaybeValue;

public class Update extends Command {
	/**
	 * Creates a new update expression.
	 * 
	 * @param l
	 * @param r
	 */
	public Update(Expr l, Expr r) {
		this.addChild(l);
		this.addChild(r);
	}

	@Override
	public boolean classInv() {
		try {
			if (this.getParent().get().getCategory().equals(NodeCategory.RULE)) {
				if (this.getChildren().size() == 2) {
					if (this.getChildren().get(0).getCategory().equals(NodeCategory.EXPRESSION)
							&& this.getChildren().get(1).getCategory().equals(NodeCategory.EXPRESSION))
						return true;
				}
			}
		} catch (NoMaybeValue e) { // if there's no parent, then the invariant breaks
			return false;
		}
		return false;
	}
	
	public NodeCategory getCategory() {
		return NodeCategory.UPDATE;
	}

}
