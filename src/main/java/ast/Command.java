package ast;

public abstract class Command extends AbstractNode {

	@Override
	public NodeCategory getCategory() {
		return NodeCategory.COMMAND;
	}


}
