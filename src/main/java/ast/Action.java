package ast;


import cms.util.maybe.NoMaybeValue;
import parse.TokenCategory;
import parse.TokenType;

public class Action extends Command {
	public final TokenType[] tokenTypes = { TokenType.WAIT, TokenType.FORWARD, TokenType.BACKWARD,
			TokenType.LEFT, TokenType.RIGHT, TokenType.EAT, TokenType.ATTACK, TokenType.GROW,
			TokenType.BUD, TokenType.MATE, TokenType.SERVE}; 


	/**
	 * Constructs an instance of an action.
	 * 
	 * @param act Type of action performed.
	 */
	public Action(TokenType act) {
		this.tokenType = act;
	}

	/**
	 * Constructs an instance of an action given an expression (used for serve).
	 * 
	 * @param act Type of action performed.
	 */
	public Action(TokenType act, Expr e) {
		this.tokenType = act;
		getChildren().add(e);
	}

	@Override
	public NodeCategory getCategory() {
		return NodeCategory.ACTION;
	}

	@Override
	public boolean classInv() {
		// checks if the parent is a rule
		try {
			if (this.getParent().get().getCategory().equals(NodeCategory.RULE)) {
				// checks if the action is serve and the child is expression
				if ((this.tokenType.equals(TokenType.SERVE) && this.getChildren().size() == 1)) {
					if (this.getChildren().get(0).getCategory().equals(NodeCategory.EXPRESSION))
						return true;
				}
				// checks if the action is valid, isn't serve, and has no children
				else if (this.getChildren().size() == 0 && this.tokenType.category().equals(TokenCategory.ACTION)
						&& !this.tokenType.equals(TokenType.SERVE)) {
					return true;
				}
			}
		} catch (NoMaybeValue e) { // if there's no parent, then the invariant breaks
			return false;
		}
		return false;
	}
	
}
