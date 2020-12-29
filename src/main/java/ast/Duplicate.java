package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

public class Duplicate implements Mutation {
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
		switch(n) {
		case PROGRAM:
			if(!program.findNodeOfType(NodeCategory.RULE).equals(Maybe.none())){
				try {
					Node randomRule = program.findNodeOfType(NodeCategory.RULE).get();
					node.getChildren().add(randomRule);
					return Maybe.from(program);
				} catch (NoMaybeValue e) {
					e.printStackTrace();
				}
			}
			break; 
		case RULE:
		
			if(!program.findNodeOfType(NodeCategory.UPDATE).equals(Maybe.none())){
				try {
					Node randomUpdate = program.findNodeOfType(NodeCategory.UPDATE).get();
					int indexOfAction = -1;
					for(int i = 0; i < node.getChildren().size(); i++) {
						if(node.getChildren().get(i).getCategory().equals(NodeCategory.ACTION));
						indexOfAction = i;
						
					}
					if(indexOfAction > 0) {
					Node action = node.getChildren().get(indexOfAction);
					node.getChildren().set(indexOfAction, randomUpdate);
					node.getChildren().add(action);
					}
					else {
						node.getChildren().add(randomUpdate);
					}
					
					
					
					return Maybe.from(program);
				} catch (NoMaybeValue e) {
					e.printStackTrace();
				}
			}
			break;
				
		}
		return Maybe.none();
	}

	@Override
	public boolean canApply(Node n) {
		// TODO Auto-generated method stub
		NodeCategory category = n.getCategory();
		if(category.equals(NodeCategory.PROGRAM) || 
				category.equals(NodeCategory.RULE)) {
			return true;
		}
		return false;
	}

}
