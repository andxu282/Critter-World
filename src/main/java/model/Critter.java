package model;
import java.util.Arrays;
import java.util.Random;

import ast.Action;
import ast.Node;
import ast.NodeCategory;
import ast.Program;
import ast.Rule;
import cms.util.maybe.NoMaybeValue;
import javafx.scene.image.ImageView;
import parse.PrettyPrinter;
import parse.TokenType;


public class Critter extends Tile implements ReadOnlyCritter {
	/**
	 * Name of critter. Default: "Critter X"
	 */
	private String species;
	/**
	 * Memory array of critter. Default: [7, 10, 10, 10, 10, 1, 10]
	 */
	private int[] mem;
	/**
	 * Rule set of critter.
	 */
	private Program program;
	/**
	 * Last rule executed by the critter.
	 */
	private Rule lastRule;

	/**
	 * Direction of the critter. Assigned randomly at the initalization of a
	 * critter.
	 */
	private int direction;
	/**
	 * Creates a Tile with a default critter on it.
	 * 
	 */

	/**
	 * Column index of critter
	 */
	private int c;

	/**
	 * Row index of critter
	 */
	private int r;

	/**
	 * Links this critter with its icon in the view
	 */
	private ImageView view = null;

	/**
	 * Represents the last action taken by the critter.
	 */
	private TokenType lastAction;

	private Critter nextState;

	public Critter() {
		super();
		this.species = "Critter X";
		this.mem = new int[7];
		this.mem[0] = 7;
		this.mem[1] = 10;
		this.mem[2] = 10;
		this.mem[3] = 10;
		this.mem[4] = 10;
		this.mem[5] = 1;
		this.mem[6] = 10;
		this.program = null;
		this.tileType = TileType.CRITTER;
		Random r = new Random();
		this.direction = r.nextInt(6);
	}

	/**
	 * Creates a Tile with a critter on it.
	 * 
	 */
	
	public Critter(String species, int memSize, int defense, int offense, int size, int energy, int posture,
			int direction, Program program) {
		super();
		this.species = species;
		this.mem = new int[memSize];
		this.mem[0] = memSize;
		this.mem[1] = defense;
		this.mem[2] = offense;
		this.mem[3] = size;
		this.mem[4] = energy;
		this.mem[5] = 1;
		this.mem[6] = posture;
		this.program = program;
		this.tileType = TileType.CRITTER;
		this.direction = direction % 6;
	}

	@Override
	public Critter clone() {
		Critter clone = new Critter(this.getSpecies(), this.getMemPos(0), this.getMemPos(1), this.getMemPos(2),
				this.getMemPos(3), this.getMemPos(4), this.getMemPos(6), this.getDirection(), this.getProgram());
		clone.setPosition(this.getC(), this.getR());
		return clone;
	}

	/**
	 * Gets the previous state
	 * 
	 */
	public Critter getNextState() {
		return this.nextState;
	}

	/**
	 * Sets the previous state
	 * 
	 * @param previousState
	 */
	public void setNextState(Critter nextState) {
		this.nextState = nextState;
	}

	@Override
	public String getSpecies() {
		return this.species;
	}

	@Override
	public int[] getMemory() {
		int[] memCopy = Arrays.copyOf(mem, mem.length);
		return memCopy;
	}

	@Override
	public String getProgramString() {
		PrettyPrinter p = new PrettyPrinter();
		StringBuilder sb = new StringBuilder();
		try {
			p.PrettyPrint(this.program, sb);
		} catch (NoMaybeValue e) {
			e.printStackTrace();
		}
		return sb.toString();
	}

	@Override
	public String getLastRuleString() {
		PrettyPrinter p = new PrettyPrinter();
		StringBuilder sb = new StringBuilder();
		try {
			p.printer(this.lastRule, sb);
		} catch (NoMaybeValue e) {
			
		}
		if (this.lastRule == null)
			sb.append("No last rule executed so far.");
		return sb.toString();
	}

	/**
	 * Returns the direction the critter faces.
	 * 
	 * @return direction critter faces, an int from 0 to 5 inclusive.
	 */
	public int getDirection() {
		return this.direction;
	}

	/**
	 * @return a deep copy of the AST of this critter
	 */
	public Program getProgram() {
		return this.program;
	}

	/**
	 * Returns critter's column index
	 * 
	 * @return column index
	 */
	public int getC() {
		return this.c;
	}
	
	/**
	 * Returns critter's row index
	 * 
	 * @return row index
	 */
	public int getR() {
		return this.r;
	}
	
	/**
	 * Sets lastRule
	 * 
	 * @param rule, variable that lastRule will be set to
	 */
	public void setLastRule(Rule rule) {
		this.lastRule = rule;
		Node action = rule.getChildren().get((rule.getChildren().size() - 1));
		if (action.getCategory() == NodeCategory.ACTION) {
			this.lastAction = ((Action) action).getTokenType();
		} else {
			this.lastAction = TokenType.WAIT;
		}
	}

	/**
	 * Sets critter's position
	 */
	public void setPosition(int c, int r) {
		this.c = c;
		this.r = r;
	}

	/**
	 * Sets critter's direction
	 */
	public void setDirection(int dir) {
		this.direction = dir;
	}
	
	/**
	 * Gets a memory expression
	 */
	public int getMemPos(int i) {
		return mem[i];
	}

	/**
	 * Sets a memory expression
	 */
	public void setMemPos(int i, int newValue) {
		this.mem[i] = newValue;
	}

	/**
	 * Gets critter's hex in front's column index
	 */
	public int getCahead() {
		switch(direction) {
		case 0:
			return c;
		case 1:
			return c + 1;
		case 2:
			return c + 1;
		case 3:
			return c;
		case 4:
			return c - 1;
		case 5:
			return c - 1;
		default:
			return 0;
		}
	}

	/**
	 * Gets critter's hex behind's column index
	 */
	public int getCbehind() {
		switch (direction) {
		case 0:
			return c;
		case 1:
			return c - 1;
		case 2:
			return c - 1;
		case 3:
			return c;
		case 4:
			return c + 1;
		case 5:
			return c + 1;
		default:
			return 0;
		}
	}

	/**
	 * Gets critter's hex in front's row index
	 */
	public int getRahead() {
		switch (direction) {
		case 0:
			return r + 1;
		case 1:
			return r + 1;
		case 2:
			return r;
		case 3:
			return r - 1;
		case 4:
			return r - 1;
		case 5:
			return r;
		default:
			return 0;
		}

	}

	/**
	 * Gets critter's hex behind's row index
	 */
	public int getRbehind() {
		switch (direction) {
		case 0:
			return r - 1;
		case 1:
			return r - 1;
		case 2:
			return r;
		case 3:
			return r + 1;
		case 4:
			return r + 1;
		case 5:
			return r;
		default:
			return 0;
		}

	}

	/**
	 * Sets posture of critter
	 * 
	 * @param p, int that posture will be set to
	 */
	public void setPosture(int p) {
		if (p >= 0 && p <= 99) {
			mem[6] = p;
		}
	}

	/**
	 * Gets the last action executed by the critter.
	 * 
	 * @return action if there was a last action, null otherwise.
	 */
	public TokenType getLastAction() {
		try {
			return this.lastAction;
		} catch (NullPointerException e) {
			return null;
		}
	}

	/**
	 * Set last action
	 * 
	 * @return
	 */
	public void setLastAction(TokenType action) {
		this.lastAction = action;
	}

	/**
	 * Gets the image view of this critter
	 * 
	 * @return view, the image view of this critter.
	 */
	public ImageView getView() {
		return this.view;
	}

	/**
	 * Sets the image view of this critter
	 * 
	 */
	public void setView(ImageView view) {
		this.view = view;
	}

}
