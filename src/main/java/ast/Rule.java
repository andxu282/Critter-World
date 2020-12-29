package ast;
import java.util.ArrayList;
import java.util.List;

import cms.util.maybe.NoMaybeValue;

/** A representation of a critter rule. */
public class Rule extends AbstractNode {
	private ArrayList<Node> commands = new ArrayList<Node>();

	/**
	 * Creates a new rule with a condition and a command.
	 * 
	 * @param c   Condition needed to be satisified for the rule to run.
	 * @param cmd Command that is run after the condition is satisified.
	 */
	public Rule(Condition c, List<Node> cmd) {
		this.addChild(c);
		for (Node n : cmd) {
			this.addChild(n);
		}
	}

	/**
	 * Returns the commands in this rule.
	 * 
	 * @return commands in this rule.
	 */
	public ArrayList<Node> getCommands() {
		return this.commands;
	}

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.RULE;
    }

	@Override
    public boolean classInv() {
    	try {
			if (this.getParent().get().getCategory().equals(NodeCategory.PROGRAM)) {
				if (this.getChildren().get(0).getCategory().equals(NodeCategory.CONDITION)) {
					for (int i = 1; i < this.getChildren().size(); i++) {
						if (!this.getChildren().get(i).getCategory().equals(NodeCategory.UPDATE)
								&& !this.getChildren().get(i).getCategory().equals(NodeCategory.ACTION))
							return false;
					}
					return true;
				}
			}
		} catch (NoMaybeValue e) { // if there's no parent, then the invariant breaks
			return false;
		}
		return false;
    }


}
