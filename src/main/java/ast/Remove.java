package ast;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

public class Remove implements Mutation {

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
		case RULE:
			if(program.getChildren().size() > 1) {
				program.getChildren().remove(node);
				return Maybe.from(program);
			}
			break;
		case UPDATE:
		
			try {
				if(node.getParent().get().getChildren().size() >2) {
					node.getParent().get().getChildren().remove(node);
					return Maybe.from(program);
				}
			} catch (NoMaybeValue e) {
				e.printStackTrace();
			}
			break;
		case ACTION:
			try {
				if(node.getParent().get().getChildren().size()>2) {
					node.getParent().get().getChildren().remove(node);
					return Maybe.from(program);
				}
			} catch (NoMaybeValue e) {
				e.printStackTrace();
			}	
			break;
		case BINARYCOND:
			int index = (int)(Math.random() * node.getChildren().size());
			try {
				int current = node.getParent().get().getChildren().indexOf(node);
				node.getParent().get().getChildren().set(current, node.getChildren().get(index));		
				return Maybe.from(program);
			} catch (NoMaybeValue e) {
				e.printStackTrace();
			}
			break;
		case BINARYEXPR:
			int index2 = (int)(Math.random() * 2);
			try {
				int current = node.getParent().get().getChildren().indexOf(node);
				node.getParent().get().getChildren().set(current, node.getChildren().get(index2));
				return Maybe.from(program);
			} catch (NoMaybeValue e) {
				e.printStackTrace();
			}
			break;		
		}
		return Maybe.none();
	}

	@Override
	public boolean canApply(Node n) {
		NodeCategory category = n.getCategory();
		if(category.equals(NodeCategory.PROGRAM) || 
				category.equals(NodeCategory.RELATION) ||
				category.equals(NodeCategory.NUMBER) || 
				category.equals(NodeCategory.SENSOR)) {
			return false;
		}
		return true;
	}

}
