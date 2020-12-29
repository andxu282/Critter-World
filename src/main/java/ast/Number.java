package ast;

import cms.util.maybe.NoMaybeValue;

public class Number extends Expr {
	private int value;
	private NumberType numberType;

	/**
	 * Creates a new number of an integer value
	 * 
	 * @param value
	 * @param num
	 */
	public Number(int value, NumberType num) {
		this.value = value;
		this.numberType = num;
	}

	@Override
	public boolean classInv() {
		try {
			if (this.getParent().get().getCategory().equals(NodeCategory.BINARYEXPR)
					|| this.getParent().get().getCategory().equals(NodeCategory.RELATION)
					|| this.getParent().get().getCategory().equals(NodeCategory.SENSOR)) {
				if (this.getChildren().size() == 0) {
					return true;
				}
			}
		} catch (NoMaybeValue e) { // if there's no parent, then the invariant breaks
			return false;
		}
		return false;
	}

	public enum NumberType {
		NUM, MEM
	}

	/**
	 * Returns the integer value of this Number.
	 * 
	 * @return value Value of this Number
	 */
	public int getValue() {
		return this.value;
	}

	/**
	 * Sets the integer value of this Number.
	 */
	public void setValue(int value) {
		this.value = value;
	}

	/**
	 * Returns the type of this number.
	 * 
	 * @return type of this number
	 */
	public NumberType getNumberType() {
		return this.numberType;
	}
	
	public NodeCategory getCategory() {
		return NodeCategory.NUMBER;
	}


}
