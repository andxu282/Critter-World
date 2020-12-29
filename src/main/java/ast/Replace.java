package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
public class Replace implements Mutation {
	@Override
	public boolean equals(Mutation m) {
		if(m.getClass().equals(this.getClass())) {
			return true;
		}
		return false;
	}

	@Override
	public Maybe<Program> apply(Program program, Node node) { 
		NodeCategory  n = node.getCategory();	

			if(!(program.findNodeOfType(n).equals(Maybe.none()))){
				try {
					//Clone method needs to work?
					Node x = program.findNodeOfType(n).get().clone();		
					int current = node.getParent().get().getChildren().indexOf(node);
					node.getParent().get().getChildren().set(current, x);
					return Maybe.from(program);
				} 
				catch (NoMaybeValue e) {
					e.printStackTrace();
				}
			}			
		return Maybe.none();
	}

	@Override
	public boolean canApply(Node n) {
		NodeCategory category = n.getCategory();
		if(category.equals(NodeCategory.PROGRAM)){
			return false;
		}
		return true;
	}

}
