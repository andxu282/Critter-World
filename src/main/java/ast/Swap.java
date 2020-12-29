package ast;

import cms.util.maybe.Maybe;
import java.util.ArrayList;
public class Swap implements Mutation {
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
		ArrayList<Node> children = (ArrayList<Node>)node.getChildren();
		if(node.getCategory().equals(NodeCategory.PROGRAM))	{	
			int length = children.size();
			if(length >1) {
			int index1 = (int)(Math.random() * length);
			int index2 = index1;
			while(index2 == index1) {
				index2 = (int)(Math.random() * length);
			}
			Node first = children.get(index1);
			children.set(index1, children.get(index2));
			children.set(index2, first);
			return Maybe.from(program);
			}
		}
		else if(canApply(node) && node.getChildren().size()>1){
			Node first2 = children.get(0);
			children.set(0, children.get(1));
			children.set(1, first2);
			return Maybe.from(program);
		}
		
		return Maybe.none();
	}
	@Override
	public boolean canApply(Node n) {
		NodeCategory category = n.getCategory();
		if(category.equals(NodeCategory.PROGRAM) || 
				category.equals(NodeCategory.BINARYCOND) ||
				category.equals(NodeCategory.RELATION) || 
				category.equals(NodeCategory.BINARYEXPR)) {
			return true;
		}
		return false;
	}
}
