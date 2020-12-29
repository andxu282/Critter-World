package ast;

import java.util.ArrayList;

import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;

/** A data structure representing a critter program. */
public class ProgramImpl extends AbstractNode implements Program {
	/**
	 * Creates a new ProgramImpl with one rule
	 * 
	 * @param r First rule in the Program
	 */
	public ProgramImpl(Rule r) {
		this.addChild(r);
	}

	/**
	 * Adds a rule to the list of rules in the Program.
	 * 
	 * @param r Rule added to the Program
	 */
	public void addRule(Rule r) {
		this.addChild(r);
	}

	@Override
	public StringBuilder prettyPrint(StringBuilder sb) {
		this.establishParentLinks22();
		for (Node n : this.getChildren()) {
			if (n != null) {
				n.prettyPrint(sb);
				sb.append("\n");
			}
		}
		return sb;
	}

    @Override
    public Program mutate() {
        boolean flag = false;
        Program program = this;
		while (flag == false && getChildren().size() > 0) { // this
        int children = getChildren().size();
        int nodeToMutate = (int)(Math.random() * children);
        int mutationType = (int)(Math.random() * 6);
        Maybe<Program> prog = Maybe.none();
        if(mutationType == 0) {
        	prog = mutate(nodeToMutate,new Remove());
        	//System.out.println("remove");
        }
        else if(mutationType ==1) {
        	prog = mutate(nodeToMutate, new Swap());
        	//System.out.println("swap");
        }
        else if(mutationType ==2) {
        	prog = mutate(nodeToMutate,new Replace());
        	//System.out.println("replace");
        }
        else if(mutationType ==3) {
        	prog = mutate(nodeToMutate, new Transform());
        	//System.out.println("transform");
        } 
        else if(mutationType == 4) {
        	prog = mutate(nodeToMutate, new Insert());
        	//System.out.println("insert");
        }
        else {
        	prog = mutate(nodeToMutate, new Duplicate());
        	//System.out.println("duplicate");
        }   
        if(!prog.equals(Maybe.none())) {
        	try {
				program = prog.get();
				program.establishParentLinks22();
			} catch (NoMaybeValue e) {
				e.printStackTrace();
			}
        	//System.out.println("success");
        	flag = true;
        }
        }
        
        return program;
    }

    @Override
    public Maybe<Program> mutate(int index, Mutation m) {
    	try {
    	if(m.canApply(this.nodeAt(index))) {
    		Maybe<Program> mutated = m.apply(this, this.nodeAt(index));
    		return mutated;
    	}
        return Maybe.none();
    	}
    	catch(NullPointerException e) {
    		//e.printStackTrace();
    		return Maybe.none();
    	}
    }

    @Override
    public Maybe<Node> findNodeOfType(NodeCategory type) {
        Node[] queue = new Node[500];
        ArrayList <Node> matches = new ArrayList<Node>();
        queue[0] = this;
        int occupied = 1;   
        for(int i = 0; i <occupied; i++) {
        	if(queue[i].getCategory().equals(type)){
        		matches.add(queue[i]);
        	}
        	for(Node x: queue[i].getChildren()) {
        		if(occupied == queue.length-1) {
        			queue = resizeArray(queue);
        		}
        		queue[occupied] = x;
        		occupied ++;
        	}
        }
        if(matches.size() > 0) {
        	int index = (int)(Math.random() * matches.size());
        	return Maybe.from(matches.get(index));
        } 
        return Maybe.none();
    }   
    
    public Node [] resizeArray(Node [] x) {
    	Node [] replacement = new Node [x.length*2];
    	for(int i = 0; i <x.length; i++) {
    		replacement[i] = x[i];
    	}
    	return replacement;

    }

    @Override
    public NodeCategory getCategory() {
        return NodeCategory.PROGRAM;
    }

	@Override
    public boolean classInv() {
		if (!this.getParent().isPresent()) {
			for (Node n : this.getChildren()) {
				if (!n.getCategory().equals(NodeCategory.RULE))
					return false;
			}
		}
		if (this.getParent().isPresent())
			return false;
		return true;
    }

}
