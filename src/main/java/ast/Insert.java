package ast;

import ast.BinaryCondition.Operator;
import ast.BinaryExpr.ExprOperator;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

public class Insert implements Mutation {
	
	public boolean equals(Mutation m) {
		if(m.getClass().equals(this.getClass())) {
			return true;
		}
		return false;
	}

	public Maybe<Program> apply(Program program, Node node) {
		
		if(!node.getCategory().equals(NodeCategory.PROGRAM)) {
			try {
				NodeCategory n = node.getParent().get().getCategory();
				switch (n) {
				case BINARYCOND:
					Node r = program.findNodeOfType(NodeCategory.RELATION).get();
					int conditionType = (int) (Math.random() * 2);

					int parentVacancy = node.getParent().get().getChildren().indexOf(node);

					node.getParent().get().getChildren().remove(parentVacancy);

					if (conditionType == 0) {
						BinaryCondition replacement = new BinaryCondition((Condition) node, Operator.and,
								(Condition) r);
						node.getParent().get().getChildren().add(replacement);
					} else {
						BinaryCondition replacement = new BinaryCondition((Condition) node, Operator.or, (Condition) r);
						node.getParent().get().getChildren().add(replacement);
					}

					return Maybe.from(program);

				case BINARYEXPR:
					BinaryExpr b = (BinaryExpr) node.getParent().get();
					Node e = program.findNodeOfType(NodeCategory.NUMBER).get();
					int expressionType = (int) (Math.random() * 5);
					int parentVacancy2 = node.getParent().get().getChildren().indexOf(node);
					node.getParent().get().getChildren().remove(parentVacancy2);

					if (expressionType < 3) {
						BinaryExpr replacement = new BinaryExpr((Expr) node, ExprOperator.ADDOP, (Expr) e,
								b.operators[expressionType]);
						node.getParent().get().getChildren().add(replacement);
					} else {
						BinaryExpr replacement = new BinaryExpr((Expr) node, ExprOperator.MULOP, (Expr) e,
								b.operators[expressionType]);
						node.getParent().get().getChildren().add(replacement);
					}

					return Maybe.from(program);
				default:
					break;
				}
			} catch (NoMaybeValue e) {
				e.printStackTrace();
			}
		}
		return Maybe.none();
	}

	@Override
	public boolean canApply(Node n) {
		
		if(!(n.getCategory().equals(NodeCategory.PROGRAM))) {
			try {
				NodeCategory category = n.getParent().get().getCategory();
				if(category.equals(NodeCategory.BINARYCOND) || category.equals(NodeCategory.BINARYEXPR)) {
					return true;
				}
			} catch (NoMaybeValue e) {
				e.printStackTrace();
			}
		}
		return false;
	}

}
