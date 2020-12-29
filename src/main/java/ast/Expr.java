package ast;

/** A critter program expression that has an integer value. */
public abstract class Expr extends AbstractNode {
	private boolean negative = false;
	private boolean parentheses = false;

	/**
	 * Returns false for positive numbers.
	 * 
	 * @return false for positive numbers
	 */
	public boolean getNegative() {
		return this.negative;
	}

	/**
	 * Set a number to negative.
	 */
	public void setNegative(boolean negative) {
		this.negative = negative;
	}

	/**
	 * Returns false for no parentheses.
	 * 
	 * @return false for no parentheses.
	 */
	public boolean getParentheses() {
		return this.parentheses;
	}

	/**
	 * Set false for no parentheses.
	 */
	public void setParentheses(boolean parentheses) {
		this.parentheses = parentheses;
	}

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.EXPRESSION;
    }
}
