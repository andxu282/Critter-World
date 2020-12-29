//Random Comment
package ast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import parse.Parser;
import parse.ParserFactory;
import parse.PrettyPrinter;
import parse.TokenType;

public abstract class AbstractNode implements Node {
	private List<Node> children = new ArrayList<Node>();
	private Maybe<Node> parent = Maybe.none();
	private ArrayList<AbstractNode> subtree;
	protected TokenType tokenType;
	protected TokenType[] tokenTypes;

	/**
	 * Returns this action's token types.
	 */
	public TokenType getTokenType() {
		return this.tokenType;
	}

	/**
	 * Returns this action's token types.
	 */
	public void setTokenType(TokenType tokenType) {
		this.tokenType = tokenType;
	}

	/**
	 * Returns this action's possible token types.
	 */
	public TokenType[] getTokenTypes() {
		return this.tokenTypes;
	}

	/**
	 * Returns this action's possible token types.
	 */
	public void setTokenTypes(TokenType[] tokenTypes) {
		this.tokenTypes = tokenTypes;
	}

    @Override
    public int size() {
		int size = 0;
		List<Node> currentChildren = children;
		if (currentChildren.size() == 0) {
			return 1;
		}
		for (Node e : currentChildren) {
			currentChildren = e.getChildren();
			size += ((AbstractNode) e).size();
		}
		return size + 1;
	}

	/**
	 * Traverses the AST in a preorder fashion to implment nodeAt.
	 * 
	 * @param a Arraylist of abstract nodes
	 */
	private void traverse(ArrayList<AbstractNode> a) {
		Node n = this;
		a.add((AbstractNode) n);
		for (Node each : n.getChildren()) {
			((AbstractNode) each).traverse(a);
		}
	}

    @Override
	public Node nodeAt(int index) {
		subtree = new ArrayList<>();
		traverse(subtree);
		return subtree.get(index);
	}
    
	/**
	 * Returns the number of descendants.
	 * 
	 * @return The subtree's size, which is just the number of children.
	 */
    public int numChildren() {
    	subtree = new ArrayList<>();
    	traverse(subtree);
    	return subtree.size();
    }

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		PrettyPrinter p = new PrettyPrinter();
		try {
			p.printer(this, sb);
		} catch (NoMaybeValue e) {
			e.printStackTrace();
		}
		return sb;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		return this.prettyPrint(s).toString();
	}

	/**
	 * Makes a deep copy of this Node and all its descendants
	 * 
	 */
    public Node clone() {
		Node copy = null;
		switch (this.getCategory()) {
		case ACTION:
			Action a = (Action) this;
			if (a.getChildren().size() == 0) {
				copy = new Action(a.getTokenType());
			} else {
				copy = new Action(a.getTokenType(), (Expr) a.getChildren().get(0).clone());
			}
			break;
		case BINARYCOND:
			BinaryCondition bc = (BinaryCondition) this;
			copy = new BinaryCondition((Condition) bc.getChildren().get(0).clone(), bc.getOperator(),
					(Condition) bc.getChildren().get(1).clone());
			break;
		case BINARYEXPR:
			BinaryExpr be = (BinaryExpr) this;
			copy = new BinaryExpr((Expr) be.getChildren().get(0).clone(), be.getExprOperator(),
					(Expr) be.getChildren().get(1).clone(), be.getTokenType());
			break;
		case COMMAND:
			break;
		case CONDITION:
			break;
		case EXPRESSION:
			break;
		case NUMBER:
			Number n = (Number) this;
			copy = new Number(n.getValue(), n.getNumberType());
			break;
		case PROGRAM:
			ProgramImpl pi = (ProgramImpl) this;
			copy = new ProgramImpl((Rule) pi.getChildren().get(0).clone());
			if (pi.getChildren().size() > 1) {
				for (int i = 1; i < pi.getChildren().size(); ++i) {
					((ProgramImpl) copy).addRule((Rule) pi.getChildren().get(i).clone());
				}
			}
			break;
		case RELATION:
			Relation rl = (Relation) this;
			copy = new Relation((Expr) rl.getChildren().get(0).clone(), (Expr) rl.getChildren().get(1).clone(),
					rl.getTokenType());
			break;
		case RULE:
			Rule r = (Rule) this;
			List<Node> list = new ArrayList<>();
			Node cond = r.getChildren().get(0).clone();

			for (int j = 1; j < r.getChildren().size(); ++j) {
				list.add(r.getChildren().get(j).clone());
			}
			copy = new Rule((Condition) cond, list);
			break;
		case SENSOR:
			Sensor s = (Sensor) this;
			if (s.getChildren().size() == 0) {
				copy = new Sensor(s.getTokenType());
			} else {
				copy = new Sensor((Expr) s.getChildren().get(0).clone(), s.getTokenType());
			}
			break;
		case UPDATE:
			Update u = (Update) this;
			copy = new Update((Expr) u.getChildren().get(0).clone(), (Expr) u.getChildren().get(1).clone());
			break;
		default:
			break;
		}
		try {
			return copy;
		} catch (NullPointerException e) {
			return null;
		}
    }

    @Override
    public List<Node> getChildren() {
		return children;
    }

	/**
	 * Adds a child to this node.
	 * 
	 * @param child
	 */
	public void addChild(Node child) {
		this.children.add(child);
	}

    public void setChildren(List<Node> children) {
		this.children = children;
    }


    public Maybe<Node> getParent() {
		return this.parent;
    }

    public void setParent(Node p) {
		parent = Maybe.from(p);
	}

	/**
	 * Called After AST is created to created parent links in the entire tree
	 * Requires: must be invoked after AST has been created and invoked on the
	 * program node
	 */
	public void establishParentLinks22() {
		Node n = this;

		for (Node each : n.getChildren()) {
			if (each != null && n != null) {
				((AbstractNode) each).setParent(n);
			}
		}

		for (Node each : n.getChildren()) {
			if (each != null) {
				((AbstractNode) each).establishParentLinks22();
			}
		}
	}

	public static void main(String[] args) throws NoMaybeValue, FileNotFoundException {
		InputStream in = new FileInputStream(
				"/Users/andrew/eclipse-workspace/ajx8-ezj4-ic254-critterworld/src/test/resources/files/critter_1.txt");
		Reader r = new BufferedReader(new InputStreamReader(in));
		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(r);
		Program p = prog.get();
		System.out.println(p.toString());
		Program pCopy = (Program) p.clone();
		System.out.println(pCopy.toString());
	}
}