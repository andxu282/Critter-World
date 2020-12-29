package ast;


import java.util.Random;

import ast.BinaryCondition.Operator;
import ast.BinaryExpr.ExprOperator;
import ast.Number.NumberType;
import cms.util.maybe.Maybe;
public class Transform implements Mutation{
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
	
		Random r = new Random();
		switch(n) {
		case ACTION:	
			int index = -1;
			for(int i = 0; i<11; i++) {
				if (((Action)node).tokenTypes[i].equals(((Action)node).getTokenType())) {
					index = i;
				}
			}
			if(index != 10) {
			int changeIndex = index;
			while(changeIndex == index) {
				changeIndex = (int)(Math.random() * 11);
			}
			if(changeIndex ==10) {
				((Action)node).getChildren().add(new Number(r.nextInt(),NumberType.NUM));
			}
			((Action)node).setTokenType(((Action)node).tokenTypes[changeIndex]);
			}
			else if (index == 10) {
				int changeIndex = (int)(Math.random() * 11);
				if (changeIndex == 10) {
					((Action)node).getChildren().set(0, new Number(r.nextInt(), NumberType.NUM));
				}
				else {
					if (((Action)node).getChildren().size() > 0) {
						((Action)node).getChildren().remove(0);
					}
					((Action)node).setTokenType(((Action)node).tokenTypes[changeIndex]);
				}	
			}		
			return Maybe.from(program);
		case BINARYCOND:
			if (((BinaryCondition) node).getOperator().equals(Operator.or)) {
				((BinaryCondition) node).setOperator(Operator.and);
			}
			else {
				((BinaryCondition) node).setOperator(Operator.or);
			}
			
			return Maybe.from(program);
		case BINARYEXPR:
			int index2 = -1;
			for (int i = 0; i < ((BinaryExpr) node).operators.length; i++) {
				if (((BinaryExpr) node).operators[i].equals(((BinaryExpr) node).getTokenType())) {
					index2 = i;
				}
			}
			int index3 = index2;
			while(index3 == index2) {
				index3 = (int)(Math.random()*5);
			}
			((BinaryExpr) node).setTokenType(((BinaryExpr) node).operators[index3]);
			if(index2 <3 && index3 <=3) {	
				((BinaryExpr) node).setExprOperator(ExprOperator.MULOP);
			}
			if(index2 >=3 && index3 <3) {
				((BinaryExpr) node).setExprOperator(ExprOperator.ADDOP);
			}
			
			return Maybe.from(program);
		case RELATION:
			int current = -1;
			for (int i = 0; i < ((Relation) node).tokenTypes.length; i++) {
				if (((Relation) node).tokenTypes[i].equals(((Relation) node).getTokenType())) {
					current = i;
				}
			}
			int change = current;
			while(change == current) {
				change = (int)(Math.random()*6);
			}
			((Relation) node).setTokenType(((Relation) node).tokenTypes[change]);
			
			return Maybe.from(program);
		case NUMBER:
			int increment = Integer.MAX_VALUE/r.nextInt();
			((Number) node).setValue(((Number) node).getValue() + increment);
			return Maybe.from(program);
		case SENSOR:
			if(((Sensor) node).getChildren().size() == 0) {
				int changeIndex = (int)(Math.random()*3);
				((Sensor) node).setTokenType(((Sensor) node).tokenTypes[changeIndex]);
				((Sensor) node).getChildren().add(new Number(r.nextInt(), NumberType.NUM));
			}
			else {
				int current2 = -1;
				for (int i = 0; i < ((Sensor) node).tokenTypes.length; i++) {
					if (((Sensor) node).tokenTypes[i].equals(((Sensor) node).getTokenType())) {
						current = i;
					}
				}
				int changeIndex = current2;
				while(changeIndex ==current2) {
					changeIndex = (int)(Math.random()*4);
				}
				if(changeIndex ==4) {
					((Sensor) node).getChildren().remove(0);
				}
				((Sensor) node).setTokenType(((Sensor) node).tokenTypes[changeIndex]);
			}
			return Maybe.from(program);
		default:
			break;
		}
		return Maybe.none();
	}

	@Override
	public boolean canApply(Node n) {
		NodeCategory category = n.getCategory();
		if(category.equals(NodeCategory.PROGRAM) || 
				category.equals(NodeCategory.RULE) || category.equals(NodeCategory.UPDATE)) {
			return false;
		}
		return true;
	}

}
