package model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;

import ast.Action;
import ast.BinaryCondition;
import ast.BinaryExpr;
import ast.Condition;
import ast.Expr;
import ast.Node;
import ast.NodeCategory;
import ast.Number;
import ast.Number.NumberType;
import ast.Relation;
import ast.Rule;
import ast.Sensor;
import ast.Update;
import parse.TokenType;

public class Interpreter {
	public World currentWorld;
	public Critter currentCritter;
	
	public Interpreter(World w) {
		this.currentWorld = w;
	}
	/**
	 * Returns the action the critter will take in the next time step.
	 * 
	 * @return An action the critter will take in the next time step,
	 * the returned action is wait if no action is found in the rule set
	 */

	public Action interpret(Critter critter) {
		currentCritter = critter;
		@SuppressWarnings({ "rawtypes", "unchecked" })
		ArrayList<Rule> rules = (ArrayList) critter.getProgram().getChildren();
		int counter = 0;
		while (counter < 999) {
			currentCritter.setMemPos(5, counter++);
			for (int i = 0; i < rules.size(); i++) {
				Rule currentRule = rules.get(i);
				Condition c = (Condition) currentRule.getChildren().get(0);
				if (evaluateCondition(c)) {
					int numCommands = currentRule.getChildren().size();
					for(int j = 1; j < numCommands; j++) {
						Node n = currentRule.getChildren().get(j);
						if(n.getCategory().equals(NodeCategory.ACTION)) {
							currentCritter.setLastRule(currentRule);
							return (Action) n;
						}
						else {
							Update u = (Update) n;
							performUpdate(u, currentCritter);
							currentCritter.setLastRule(currentRule);
						}
					}
				}
			}
			counter++;	
		}
		currentCritter.setLastAction(TokenType.WAIT);
		return new Action(TokenType.WAIT);	
	}
	/**
	 * Evaluates whether a given condition is true
	 * @param c a condition which will be evaluated
	 * @return true if the condition evaluates to true, false otherwise
	 */
	private boolean evaluateCondition(Condition c) {
		if(c.getCategory().equals(NodeCategory.BINARYCOND)) {
			boolean leftChildValue = evaluateCondition((Condition) c.getChildren().get(0));
			boolean rightChildValue = evaluateCondition((Condition) c.getChildren().get(1));
			BinaryCondition bc = (BinaryCondition) c;
			if (bc.getOperator().equals(BinaryCondition.Operator.and)) {
				return leftChildValue && rightChildValue;
			}
			else {
				return leftChildValue || rightChildValue;
			}	
		}
		else {
			double leftChildValue = evaluateExpression((Expr) c.getChildren().get(0));
			double rightChildValue = evaluateExpression((Expr) c.getChildren().get(1));
			Relation r = (Relation) c;
			switch (r.getTokenType()) {
			case LT:
				return leftChildValue < rightChildValue;
			case LE:
				return leftChildValue <= rightChildValue;
			case GT:
				return leftChildValue > rightChildValue;
			case GE:
				return leftChildValue >= rightChildValue;
			case EQ:
				return leftChildValue == rightChildValue;
			case NE:
				return leftChildValue != rightChildValue;
			default:
				return false;
			}
		}
	}
	/**
	 * Evaluates the value of a given expression
	 * @param e the expression which will be evaluated
	 * @return the value of an expression, given as a double
	 */
	public int evaluateExpression(Expr e) {
		switch (e.getCategory()) {
		case BINARYEXPR:
			int leftChildValue = evaluateExpression((Expr)e.getChildren().get(0));
			int rightChildValue = evaluateExpression((Expr)(e.getChildren().get(1)));
			BinaryExpr be = (BinaryExpr) e;
			switch (be.getTokenType()) {
			case PLUS:
				return leftChildValue + rightChildValue;
			case MINUS:
				return leftChildValue - rightChildValue;
			case MUL:
				return leftChildValue * rightChildValue;
			case DIV:
				if(rightChildValue == 0) {
					return 0;
				}
				return Math.floorDiv(leftChildValue, rightChildValue);
			case MOD:
				if(rightChildValue == 0) {
					return 0;
				}
				return Math.floorMod(leftChildValue, rightChildValue);
			default:
				return 0;
			}
		case NUMBER:
			Number x = (Number) e;
			if (x.getNumberType().equals(NumberType.NUM)) {
				return x.getValue();
			}
			else {
				int index = x.getValue();
				try {
					return currentCritter.getMemory()[index];
				} catch (ArrayIndexOutOfBoundsException exception) {
					return 0;
				}
			}
		case SENSOR:
			Sensor s = (Sensor) e;
			Expr exp;
			int val = 0;
			if (s.getTokenType() != TokenType.SMELL) {
				exp = (Expr) s.getChildren().get(0);
				val = evaluateExpression(exp);
			}

			switch (s.getTokenType()) {
			case NEARBY:
				return calculateNearby(val);
			case AHEAD:
				return calculateAhead(val);
			case RANDOM:
				return calculateRandom(val);
			case SMELL:
				return calculateSmell();
			default:
				return 0;
			}
		default:
			return 0;
		}
	}
	
	/**
	 * Searches and reports information about a tile adjacent to the critter in a given direction.
	 * @param dir the direction relative to the critter that will be searched
	 * @return the int representation of the tile being searched
	 */
	private int calculateNearby(int dir) {
		int direction = dir % 6;
		int orientation = currentCritter.getDirection();
		int searchDirection = (orientation + direction) % 6;
		int searchTileC;
		int searchTileR;
		switch(searchDirection) {
		case 0: 
			searchTileC = currentCritter.getC();
			searchTileR = currentCritter.getR() + 1;
			break;
		case 1: 
			searchTileC = currentCritter.getC() + 1;
			searchTileR = currentCritter.getR() + 1;
			break;
		case 2:
			searchTileC = currentCritter.getC() + 1;
			searchTileR = currentCritter.getR();
			break;
		case 3:
			searchTileC = currentCritter.getC();
			searchTileR = currentCritter.getR() - 1;
			break;
		case 4:
			searchTileC = currentCritter.getC() - 1;
			searchTileR = currentCritter.getR() - 1;
			break;
		case 5:
			searchTileC = currentCritter.getC() - 1;
			searchTileR = currentCritter.getR();
			break;
		default:
			searchTileC = 0;
			searchTileR = 0;
			break;
		}
		return currentWorld.getTileInfo(searchTileC, searchTileR);
	}

	/**
	 * Searches and Reports information about a tile ahead of the critter at a given distance
	 * @param dist the distance ahead of the critter that will be searched
	 * @return the int representation of the tile being searched.
	 */
	private int calculateAhead(int dist) {
		int orientation = currentCritter.getDirection();
		int searchTileC;
		int searchTileR;
		if (dist < 0) {
			dist = 0;
		}
		switch(orientation) {
		case 0: 
			searchTileC = currentCritter.getC();
			searchTileR = currentCritter.getR() + dist;
			break;
		case 1: 
			searchTileC = currentCritter.getC() + dist;
			searchTileR = currentCritter.getR() + dist;
			break;
		case 2:
			searchTileC = currentCritter.getC() + dist;
			searchTileR = currentCritter.getR();
			break;
		case 3:
			searchTileC = currentCritter.getC();
			searchTileR = currentCritter.getR() - dist;
			break;
		case 4:
			searchTileC = currentCritter.getC() - dist;
			searchTileR = currentCritter.getR() - dist;
			break;
		case 5:
			searchTileC = currentCritter.getC() - dist;
			searchTileR = currentCritter.getR();
			break;
		default:
			searchTileC = 0;
			searchTileR = 0;
		}	
		return currentWorld.getTileInfo(searchTileC, searchTileR);
	}
	/**
	 * Gives the result of a random sensor expression
	 * @param n a number below which to generate a random number
	 * @return a random number less than n
	 */
	private int calculateRandom(int n) {
		if (n >= 2) {
			Random r = new Random();
			return r.nextInt(n);
		}
		else {
			return 0;
		}
	}

	/**
	 * The critter smells nearby food
	 */
	private int calculateSmell() {
		ArrayList<ArrayList<Integer>> adj = new ArrayList<ArrayList<Integer>>();
		ArrayList<Tile> tn = new ArrayList<Tile>();
		ArrayList<ArrayList<Integer>> hexCoordinates = new ArrayList<ArrayList<Integer>>();

		// intializes list of "nodes" and the adjacency organizer only food or empty
		// tiles are connected nodes

		int counter = 0;
		for (int c = 0; c < currentWorld.getWidth(); ++c) {
			for (int r = 0; r < currentWorld.getHeight(); ++r) {
				if (2 * r - c < currentWorld.getHeight() && 2 * r - c >= 0) {
						tn.add(currentWorld.getTile(c, r));
						adj.add(new ArrayList<Integer>());
						hexCoordinates.add(new ArrayList<Integer>());
						hexCoordinates.get(counter).add(r);
						hexCoordinates.get(counter).add(c);
						counter++;
				}
			}
		}

		// creates all the edges
		int index = 0;
		for (int c1 = 0; c1 < currentWorld.getWidth(); ++c1) {
			for (int r1 = 0; r1 < currentWorld.getHeight(); ++r1) {
				if (2 * r1 - c1 < currentWorld.getHeight() && 2 * r1 - c1 >= 0) {
					if (currentWorld.getTile(c1, r1).tileType != TileType.ROCK) {
						createEdge(c1, r1 + 1, adj, tn, index);
						createEdge(c1 + 1, r1 + 1, adj, tn, index);
						createEdge(c1 + 1, r1, adj, tn, index);
						createEdge(c1, r1 - 1, adj, tn, index);
						createEdge(c1 - 1, r1 - 1, adj, tn, index);
						createEdge(c1 - 1, r1, adj, tn, index);
					}
					index++;
				}
			}
		}

		int[] distance = new int[tn.size()];
		for (int k = 0; k < distance.length; ++k) {
			distance[k] = Integer.MAX_VALUE;
		}

		LinkedList<Integer> queue = new LinkedList<Integer>();
		HashSet<Integer> queueMem = new HashSet<Integer>();

		Integer current = tn.indexOf(currentCritter);
		queue.add(current);
		distance[current] = 0;

		// djistra thingy
		while (queue.size() != 0) {
				queue.remove();
				for (int i : adj.get(current)) {
					if (!queueMem.contains(i) && (distance[i] <= 10 || distance[i] == Integer.MAX_VALUE)) {
						queue.add(i);
					if (distance[i] == Integer.MAX_VALUE) {
						distance[i] = distance[current] + 1;
					} else {
						distance[i] = Math.min(distance[i], distance[current] + 1);
					}
				}
				}
				queueMem.add(current);
				if (queue.size() != 0) {
					current = queue.getFirst();
				}
			}


		int min = 1000000;
		ArrayList<Integer> closeFoodTiles = new ArrayList<Integer>();
		for (int b = 0; b < distance.length; ++b) {
			if (distance[b] <= 20 && tn.get(b).tileType == TileType.FOOD) {
				closeFoodTiles.add(b);
				if (distance[b] < min) {
					min = distance[b];
				}
			}
		}


		ArrayList<Integer> shortestPath = null;
		int pathLength = Integer.MAX_VALUE;
		int relativeDir = Integer.MAX_VALUE;

		for (int a : closeFoodTiles) {
			tNode root = new tNode(a, null);
			ArrayList<tNode> leaves = new ArrayList<tNode>();
			createClosePathTree(root, distance, adj, leaves);
			ArrayList<ArrayList<Integer>> paths = new ArrayList<ArrayList<Integer>>();

			int counter4 = 0;
			for (tNode t : leaves) {
				tNode currentLeaf = t;
				paths.add(new ArrayList<Integer>());
				while (currentLeaf != null) {
					paths.get(counter4).add(currentLeaf.index);
					currentLeaf = currentLeaf.parent;
				}
				counter++;
			}
			for (ArrayList<Integer> temp : paths) {
				int minDistance = temp.size() - 2;
				int currDirection = currentCritter.getDirection();
				int nextDirection = 100000;
				int lastDir = 100000;
				for (int f = 0; f < temp.size() - 1; ++f) {
					nextDirection = relativeDirection(hexCoordinates.get(temp.get(f)).get(0),
							hexCoordinates.get(temp.get(f)).get(1), hexCoordinates.get(temp.get(f + 1)).get(0),
							hexCoordinates.get(temp.get(f + 1)).get(1));
					minDistance = minDistance + Math.min(Math.abs(nextDirection - currDirection),
							6 - Math.abs(nextDirection - currDirection));
					if (f == temp.size() - 2) {
						lastDir = nextDirection;
					}
					currDirection = nextDirection;
				}

				if (minDistance < pathLength) {
					pathLength = minDistance;
					shortestPath = temp;
					relativeDir = lastDir;
				}
			}

		}

		int returnval;

		if (shortestPath == null || pathLength > 10) {
			returnval = 1000000;
		} else {
			returnval = pathLength * 1000 + relativeDir;
		}
		return returnval;
	}

	private int relativeDirection(int r0, int c0, int r1, int c1) {
		if (r1 - r0 == 1 && c1 - c0 == 0) {
			return 0;
		} else if (r1 - r0 == 1 && c1 - c0 == 1) {
			return 1;
		} else if (r1 - r0 == 0 && c1 - c0 == 1) {
			return 2;
		} else if (r1 - r0 == -1 && c1 - c0 == 0) {
			return 3;
		} else if (r1 - r0 == -1 && c1 - c0 == -1) {
			return 4;
		} else if (r1 - r0 == 0 && c1 - c0 == -1) {
			return 4;
		} else if (r1 - r0 == 0 && c1 - c0 == -1) {
			return 5;
		} else {
			System.out.println("HUMONGOUS PROBLEM");
			return -5000;
		}
	}

	private class tNode {
		tNode parent;
		ArrayList<tNode> children = new ArrayList<tNode>();
		int index;

		public tNode(int index, tNode parent) {
			this.index = index;
			this.parent = parent;
		}
	}

	public void createClosePathTree(tNode t, int[] distance, ArrayList<ArrayList<Integer>> adj,
			ArrayList<tNode> leaves) {
		for (int a : adj.get(t.index)) {
			if (distance[a] + 1 == distance[t.index]) {
				if (distance[a] == 0) {
					tNode leaf = new tNode(a, t);
					t.children.add(leaf);
					leaves.add(leaf);
					return;
				} else {
					tNode child = new tNode(a, t);
					t.children.add(child);
					createClosePathTree(child, distance, adj, leaves);
				}
			}
		}
	}

	private void createEdge(int c, int r, ArrayList<ArrayList<Integer>> adj, ArrayList<Tile> tn, int index) {
		Tile t;
		if (2 * r - c > currentWorld.getHeight() - 1 || c > currentWorld.getWidth() - 1 || c < 0 || r < 0) {
			
		}
		else {
			t = currentWorld.getTile(c, r);
			if (tn.indexOf(t) != -1) {
				if(t.tileType != TileType.ROCK) {
					adj.get(index).add(tn.indexOf(t));
				}
			}

		}
	}

	private int evaluateMemIndex(Number e) {
		return e.getValue();
	}

	/**
	 * Updates the critter memory if legal
	 * @param u an update that may be performed
	 * @param c a critter whose mem will be updated.
	 */
	private void performUpdate(Update u, Critter c) {
		int index = evaluateMemIndex((Number) u.getChildren().get(0));
		int value = evaluateExpression((Expr) u.getChildren().get(1));
		if (index < 6) {
			return;
		} else if (index == 6) {
			if (value >= 0 && value <= 99) {
				c.setPosture(value);
				return;
			} else {
				return;
			}
		}
		try {
			currentCritter.setMemPos(index, value);
		} catch (ArrayIndexOutOfBoundsException exception) {

		}
	}
}
