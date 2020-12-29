package model;

import java.util.ArrayList;
import java.util.Random;

import ast.Program;
import ast.ProgramImpl;
import ast.Rule;
import parse.TokenType;

public class Executor {
	public ArrayList<Critter> children = new ArrayList<Critter>(); // stores children created through bud and mate
	/**
	 * Checks if energy is below zero. If so, remove critter from board and drops
	 * food.
	 * 
	 * @param w
	 * @param crit
	 * @return
	 */
	private boolean checkDead(World w, Critter crit) {
		if (crit.getMemory()[4] <= 0) {
			int foodDrop = 200 * crit.getMemory()[3];
			w.changeTile(crit.getC(), crit.getR(), new Tile(foodDrop));
			w.killCritter(crit); // removes from arraylist
			return true;
		}
		return false;
	}

	/**
	 * moves critter forward 1 space if possible(open space and enough energy)
	 * 
	 * @param w
	 * @param crit
	 */
	public void forward(World w, Critter crit) {
		Tile t = w.getTile(crit.getCahead(), crit.getRahead());
		if (t.getTileType() == TileType.EMPTY) {
			crit.setMemPos(4, crit.getMemory()[4] - Constants.MOVE_COST * crit.getMemory()[3]);
			if (!checkDead(w, crit)) {
				int originalC = crit.getC();
				int originalR = crit.getR();
				w.setTile2(crit.getCahead(), crit.getRahead(), crit);
				w.setTile2(originalC, originalR, new Tile(0));
			}
		}
	}

	/**
	 * moves critter backward 1 space if possible(open space and enough energy)
	 * 
	 * @param w
	 * @param crit
	 */
	public void backward(World w, Critter crit) {
		int dir = crit.getDirection();
		crit.setDirection((dir + 3) % 6);
		forward(w, crit);
		crit.setDirection(dir);
	}

	/**
	 * Rotates critter by 60 degrees left using a bit of energy
	 * 
	 * @param w
	 * @param crit
	 */
	public void left(World w, Critter crit) {
		crit.setMemPos(4, crit.getMemory()[4] - crit.getMemory()[3]); // expends energy
		if (!checkDead(w, crit)) {
			int new_dir = crit.getDirection() - 1;
			if (new_dir < 0) {
				new_dir = new_dir + 6;
			}
			crit.setDirection(new_dir);
		}
	}

	/**
	 * Rotates critter by 60 degrees right using a bit of energy
	 * 
	 * @param w
	 * @param crit
	 */
	public void right(World w, Critter crit) {
		crit.setMemPos(4, crit.getMemory()[4] - crit.getMemory()[3]);
		if (!checkDead(w, crit)) {
			int new_dir = crit.getDirection() + 1;
			if (new_dir > 5) {
				new_dir = new_dir - 6;
			}
			crit.setDirection(new_dir);
		}
	}

	/**
	 * Critter consumes food on tile in front if present and gains energy.
	 * 
	 * @param w
	 * @param crit
	 */
	public void eat(World w, Critter crit) {
		Tile t = w.getTile(crit.getCahead(), crit.getRahead());
		if (t.getTileType() == TileType.FOOD) {
			int absorb = crit.getMemory()[3] * 500 - crit.getMemory()[4]; // amount of food it can still absorb
			if (absorb >= t.foodAmount()) {
				crit.setMemPos(4, crit.getMemory()[4] + t.foodAmount());
				t.makeEmpty();
				int[] coords = w.convertToRectangular(crit.getCahead(), crit.getRahead());
				w.addNewEmptyCoordinates(new Integer[] { coords[0], coords[1] });
			} else {
				crit.setMemPos(4, crit.getMemory()[4] + absorb);
				t.setFoodAmount(t.foodAmount() - absorb);
			}
		}
	}

	/**
	 * Critter grows in size if there is enough energy
	 * 
	 * @param w
	 * @param crit
	 */
	public void grow(World w, Critter crit) {
		if (crit.getMemory()[4] > crit.getMemory()[3] * complexity(crit)) {
			crit.setMemPos(4, crit.getMemory()[4] - crit.getMemory()[3] * complexity(crit));
			crit.setMemPos(3, crit.getMemory()[3] + 1);
		}
	}

	/**
	 * Critter drops a certain amount of food in the tile in front of it.
	 * 
	 * @param w
	 * @param crit
	 * @param amount
	 */
	public void serve(World w, Critter crit, int amount) {
		Tile t = w.getTile(crit.getCahead(), crit.getRahead());
		if (amount < 0) {
			amount = 0;
		}
		if (t.getTileType() == TileType.FOOD || t.getTileType() == TileType.EMPTY) {
			if (t.getTileType() == TileType.EMPTY) {
				int[] coords = w.convertToRectangular(crit.getCahead(), crit.getRahead());
				w.addNewFoodCoordinates(new Integer[] { coords[0], coords[1] });
			}
			if (amount + crit.getMemory()[3] < crit.getMemory()[4]) {
				t.setFoodAmount(t.foodAmount() + amount);
				crit.setMemPos(4, crit.getMemory()[4] - amount - crit.getMemory()[3]);
			} else {
				t.setFoodAmount(t.foodAmount() + crit.getMemory()[4] - crit.getMemory()[3]);
				crit.setMemPos(4, 0);
				checkDead(w, crit);
			}
			if (t.foodAmount() != 0) {
				t.setTileType(TileType.FOOD);
			}
		}
	}

	/**
	 * Logistical function for damage
	 * 
	 * @param x
	 * @return
	 */
	private double P(double x) {
		return 1.0 / (1.0 + Math.pow(Math.E, -x));
	}

	/**
	 * Critter attacks critter in front if possible and reduces targets energy.
	 * 
	 * @param w
	 * @param crit
	 */
	public void attack(World w, Critter crit) {
		Tile t = w.getTile(crit.getCahead(), crit.getRahead());
		if (t.getTileType() == TileType.CRITTER) { // critter in front
			crit.setMemPos(4, crit.getMemory()[4] - Constants.ATTACK_COST * crit.getMemory()[3]);
			Critter c = (Critter) t;
			int damage = (int) Math
					.round(Constants.BASE_DAMAGE * crit.getMemory()[3]
							* P(Constants.DAMAGE_INC * (crit.getMemory()[3] * crit.getMemory()[2]
									- c.getMemory()[3] * c.getMemory()[1])));
			c.setMemPos(4, c.getMemory()[4] - damage);
			checkDead(w, c);
			checkDead(w, crit);
		}
	}

	/**
	 * If the critter has enough energy and has room, it will make an imperfect
	 * clone of itself.
	 * 
	 * @param w
	 * @param crit
	 */
	public void bud(World w, Critter crit) {
		Tile t = w.getTile(crit.getCbehind(), crit.getRbehind());
		if (t.getTileType() == TileType.EMPTY) { // will not bud unless tile behind is empty
			crit.setMemPos(4, crit.getMemory()[4] - Constants.BUD_COST * complexity(crit));
			if (!checkDead(w, crit)) {
				// creates a copy of rules with some mutations
				Program p = (Program) crit.getProgram().clone();
				Random rnd = new Random();
				int mutate = rnd.nextInt(4);
				while (mutate == 0) {
					p.mutate();
					mutate = rnd.nextInt(4);
				}
				mutate = rnd.nextInt(6);
				Critter bud = new Critter(crit.getSpecies(), crit.getMemPos(0), crit.getMemPos(1), crit.getMemPos(2), 1,
						Constants.INITIAL_ENERGY, 0, mutate, p);
				children.add(bud);
				w.setTile2(crit.getCbehind(), crit.getRbehind(), bud);
			}
		}
	}

	/**
	 * Two adjacent critters facing each other that both want to mate will produce a
	 * new critter that has a combination of the parents genomes.
	 * 
	 * @param w
	 * @param crit
	 */
	public void mate(World w, Critter crit) {
		int memsize;
		int defense;
		int offense;
		ProgramImpl p;
		int mix;
		int genomesize;
		String species;

		Tile t = w.getTile(crit.getCahead(), crit.getRahead());
		if (t.getTileType() == TileType.CRITTER) { // possible mate in front
			Critter mate = (Critter) t;
			if (mate.getDirection() == (crit.getDirection() - 3) % 6) { // facing current critter
				Tile behindCritterTile = w.getTile(crit.getCbehind(), crit.getRbehind());
				Tile behindMateTile = w.getTile(mate.getCbehind(), mate.getRbehind());
				if (behindMateTile.tileType == TileType.EMPTY && behindCritterTile.tileType == TileType.EMPTY) { // empty
				Interpreter in = new Interpreter(w);
					if (in.interpret(mate).getTokenType() == TokenType.MATE) { // ensures the mate is also interested
						crit.setMemPos(4, crit.getMemory()[4] - Constants.MATE_COST * complexity(crit));
						mate.setMemPos(4, mate.getMemory()[4] - Constants.MATE_COST * complexity(mate));
						if (!checkDead(w, crit) && !checkDead(w, mate)) {
							Random rnd = new Random();
							mix = rnd.nextInt(2);
							// randomly selects attributes 0-2
							if (mix == 0) {
								memsize = crit.getMemory()[0];
							} else {
								memsize = mate.getMemory()[0];
							}
							mix = rnd.nextInt(2);
							if (mix == 0) {
								defense = crit.getMemory()[1];
							} else {
								defense = mate.getMemory()[1];
							}
							mix = rnd.nextInt(2);
							if (mix == 0) {
								offense = crit.getMemory()[2];
							} else {
								offense = mate.getMemory()[2];
							}

							mix = rnd.nextInt(2);
							if (mix == 0) {
								genomesize = crit.getProgram().getChildren().size();
								species = crit.getSpecies() + " descendant";
							} else {
								genomesize = mate.getProgram().getChildren().size();
								species = mate.getSpecies() + " descendant";
							}

							mix = rnd.nextInt(2);
							if (mix == 0) {
								p = new ProgramImpl((Rule) crit.getProgram().getChildren().get(0));
							} else {
								p = new ProgramImpl((Rule) mate.getProgram().getChildren().get(0));
							}

							for (int i = 0; i < genomesize; ++i) {
								mix = rnd.nextInt(2);
								if (mix == 0) {
									if (i < crit.getProgram().getChildren().size()) {
										p.addRule((Rule) crit.getProgram().getChildren().get(i));
									} else {
										p.addRule((Rule) mate.getProgram().getChildren().get(i));
									}
								} else {
									if (i < mate.getProgram().getChildren().size()) {
										p.addRule((Rule) mate.getProgram().getChildren().get(i));
									} else {
										p.addRule((Rule) crit.getProgram().getChildren().get(i));
									}
								}
							}

							mix = rnd.nextInt(2);
							if (mix == 0) {
								mix = rnd.nextInt(6);
								Critter child = new Critter(species, memsize, defense, offense, 1,
										Constants.INITIAL_ENERGY, 0,
										mix, p);
								w.setTile2(crit.getCbehind(), crit.getRbehind(), child);
								children.add(child);
							} else {
								mix = rnd.nextInt(6);
								Critter child = new Critter(species, memsize, defense, offense, 1,
										Constants.INITIAL_ENERGY, 0,
										mix, p);
								w.setTile2(mate.getCbehind(), mate.getRbehind(), child);
								children.add(child);
							}
							return;
						}
					}
				}
			}
		}
		crit.setMemPos(4, crit.getMemory()[4] - 1); // loses a bit of energy if it doesn't succeed in mating
		checkDead(w, crit);
	}

	/**
	 * Function to calculate a critter's complexity
	 * 
	 * @param crit
	 * @return
	 */
	private int complexity(Critter crit) {
		return crit.getProgram().getChildren().size() * Constants.RULE_COST
				+ (crit.getMemory()[2] + crit.getMemory()[1]) * Constants.ABILITY_COST;
	}

	/**
	 * Critter does nothing and gains energy for a turn
	 * 
	 * @param w
	 * @param crit
	 */
	public void wait(World w, Critter crit) {
		crit.setMemPos(4, crit.getMemory()[4] + Constants.SOLAR_FLUX * crit.getMemory()[3]);
	}

}
