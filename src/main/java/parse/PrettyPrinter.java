package parse;

import ast.Action;
import ast.BinaryCondition;
import ast.BinaryCondition.Operator;
import ast.BinaryExpr;
import ast.BinaryExpr.ExprOperator;
import ast.Node;
import ast.NodeCategory;
import ast.Number;
import ast.Number.NumberType;
import ast.Program;
import ast.Relation;
import ast.Sensor;
import cms.util.maybe.NoMaybeValue;
public class PrettyPrinter {
	public void PrettyPrint(Program head, StringBuilder sb) throws NoMaybeValue {
		// goes through all the rules
		head.establishParentLinks22();
		for (Node n : head.getChildren()) {
			if (n != null) {
				printer(n, sb);
				sb.append("\n");
			}
		}
	}

	public void printer(Node n, StringBuilder sb) throws NoMaybeValue {
		if (n != null) {
			switch (n.getCategory()) {
			case RULE: // tested
				printer(n.getChildren().get(0), sb);
				sb.append(" -->");
				for (int i = 1; i < n.getChildren().size(); i++) {
					sb.append(" ");
					printer(n.getChildren().get(i), sb);
				}
				sb.append(";");
				break;
			case UPDATE: // tested
				printer(n.getChildren().get(0), sb);
				sb.append(" := ");
				printer(n.getChildren().get(1), sb);
				break;
			case ACTION: // tested
				sb.append(((Action) n).getTokenType());
				if (!n.getChildren().isEmpty()) {
						sb.append("[");
						printer(n.getChildren().get(0), sb);
						sb.append("]");
					}
				break;
			case BINARYCOND: // tested
				BinaryCondition c = (BinaryCondition) n;
				StringBuilder sb1 = new StringBuilder();
				printer(n.getChildren().get(0), sb1); // add braces if an "and" is above an "or"
				sb1.append(" " + ((BinaryCondition) n).getOperator() + " ");
				printer(n.getChildren().get(1), sb1);
				if (c.getParent().isPresent()) {
					if (c.getOperator() == Operator.or
							&& c.getParent().get().getCategory().equals(NodeCategory.BINARYCOND)) {
						if (((BinaryCondition) c.getParent().get()).getOperator() == Operator.and) {
							sb.append("{");
							sb.append(sb1);
							sb.append("}");
							break;
						}
					}
				}
				sb.append(sb1);
				break;
			case RELATION: // tested
					printer(n.getChildren().get(0), sb);
					sb.append(" " + ((Relation) n).getTokenType() + " ");
					printer(n.getChildren().get(1), sb);
					break;
			case NUMBER:
				if (((Number) n).getNumberType() == NumberType.MEM) { // tested
					if (((Number) n).getValue() >= 0 && ((Number) n).getValue() <= 6) {
						sb.append(SyntaxSugar(((Number) n).getValue()));
					} else {
						sb.append("mem[");
						sb.append(((Number) n).getValue());
						sb.append("]");
					}
				} else {
					Number num = (Number) n;
					if (num.getValue() < 0) {
						sb.append("(");
						sb.append(num.getValue());
						sb.append(")");
						break;
					}
					sb.append(num.getValue());
				}
				break;
			case SENSOR: // tested
				if (((Sensor) n).getTokenType() == TokenType.SMELL) {
					sb.append(((Sensor) n).getTokenType());
				} else {
					sb.append(((Sensor) n).getTokenType() + "[");
					printer(n.getChildren().get(0), sb);
					sb.append("]");
				}
				break;
			case BINARYEXPR:
				BinaryExpr bn = (BinaryExpr) n;
				StringBuilder sb2 = new StringBuilder();
				printer(n.getChildren().get(0), sb2);
				sb2.append(" ");
				sb2.append(((BinaryExpr) n).getTokenType());
				sb2.append(" ");
				printer(n.getChildren().get(1), sb2);
				if (bn.getParent().isPresent()) {
					if (bn.getParent().get().getCategory() == NodeCategory.BINARYEXPR) {
						BinaryExpr parent = (BinaryExpr) bn.getParent().get();
						Node secondChild = parent.getChildren().get(1);
						BinaryExpr trueSecondChild;
						if (secondChild.getCategory() == NodeCategory.BINARYEXPR) {
							trueSecondChild = (BinaryExpr) secondChild;
							trueSecondChild.setIsSecondChild(true);
						}
						if ((parent.getExprOperator() == ExprOperator.MULOP
								&& bn.getExprOperator() == ExprOperator.ADDOP)
								|| (parent.getTokenType() == TokenType.MINUS && bn.getIsSecondChild()
										&& bn.getExprOperator() == ExprOperator.ADDOP)
								|| (parent.getExprOperator() == ExprOperator.MULOP && bn.getIsSecondChild()
										&& (bn.getTokenType() == TokenType.MOD
												|| bn.getTokenType() == TokenType.DIV))) {
							sb.append("(");
							sb.append(sb2);
							sb.append(")");
							break;
						}
					}
				}
				sb.append(sb2);
				break;
			default:
				System.out.println("Something went wrong.");
				break;
			}
		}
	}

	public static TokenType SyntaxSugar(int i) {
		switch (i) {
		case 0:
			return TokenType.ABV_MEMSIZE;
		case 1:
			return TokenType.ABV_DEFENSE;
		case 2:
			return TokenType.ABV_OFFENSE;
		case 3:
			return TokenType.ABV_SIZE;
		case 4:
			return TokenType.ABV_ENERGY;
		case 5:
			return TokenType.ABV_PASS;
		case 6:
			return TokenType.ABV_POSTURE;
		default:
			return TokenType.ERROR;
		}
	}

}
