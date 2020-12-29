package ast;

import cms.util.maybe.NoMaybeValue;

/** A representation of a binary Boolean condition: 'and' or 'or' */
public class BinaryCondition extends Condition {
	private Operator op;

    /**
     * Create an AST representation of l op r.
     *
     * @param l
     * @param op
     * @param r
     */
    public BinaryCondition(Condition l, Operator op, Condition r) {
		this.addChild(l);
		this.addChild(r);
		this.op = op;
    }

    /** An enumeration of all possible binary condition operators. */
    public enum Operator {
		or, and;
    }

	/**
	 * Returns this condition's operator.
	 * 
	 * @return this condition's operator.
	 */
	public Operator getOperator() {
		return this.op;
	}

	/**
	 * Sets this condition's operator.
	 */
	public void setOperator(Operator op) {
		this.op = op;
	}

	@Override
	public NodeCategory getCategory() {
		return NodeCategory.BINARYCOND;
	}

	@Override
    public boolean classInv() {
		try {
			if (this.getParent().get().getCategory().equals(NodeCategory.BINARYCOND)
					|| this.getParent().get().getCategory().equals(NodeCategory.RULE)) {
				if (this.getChildren().size() == 2) {
					if (this.getChildren().get(0).getCategory().equals(NodeCategory.CONDITION)
							&& this.getChildren().get(1).getCategory().equals(NodeCategory.CONDITION))
						return true;
				}
			}
		} catch (NoMaybeValue e) {
			return false;
		}
        return false;
    }
    
 


}
