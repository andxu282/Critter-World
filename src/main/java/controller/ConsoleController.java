package controller;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Random;
import java.util.Scanner;

import ast.Action;
import ast.Expr;
import ast.Program;
import ast.ProgramImpl;
import cms.util.maybe.Maybe;
import cms.util.maybe.NoMaybeValue;
import model.Constants;
import model.Critter;
import model.Executor;
import model.Interpreter;
import model.ReadOnlyWorld;
import model.Tile;
import model.World;
import parse.Parser;
import parse.ParserFactory;
import view.WarningMessage;

public class ConsoleController implements Controller {
	private World world;

	@Override
	public ReadOnlyWorld getReadOnlyWorld() {
		return world;
	}

	@Override
	public void newWorld() {
		world = new World();
	}

	@Override
	public boolean loadWorld(String filename, boolean enableManna, boolean enableForcedMutation) {
		boolean worldFormattedCorrectly = true;
		boolean loadedCorrectly = true;
		// reading in the file
		File worldFile = new File(filename);
		File parentFile = worldFile.getParentFile();
		System.out.println(worldFile);
		Scanner sc;
		try {
			sc = new Scanner(worldFile);
		} catch (FileNotFoundException e) {
			WarningMessage.display("Exception: File not found. Generating a random new world...");
			newWorld();
			return false;
		}

		// reading in first line (name)
		String line1 = sc.nextLine();
		Scanner lineScanner1 = new Scanner(line1);
		StringBuilder nameStringBuilder = new StringBuilder();
		StringBuilder loadWorldWarnings = new StringBuilder();
		String name = "New World";
		if (lineScanner1.next().equals("name")) {
			while (lineScanner1.hasNext()) {
				nameStringBuilder.append(lineScanner1.next());
				nameStringBuilder.append(" ");
			}
			lineScanner1.close();
			name = nameStringBuilder.toString().substring(0, nameStringBuilder.length() - 1);
		} else {
			worldFormattedCorrectly = false;
			loadWorldWarnings.append("Warning: World name not formatted correctly. Giving name \"New World\"\n");
			lineScanner1.close();
		}

		// reading in second line (dimensions)
		int x = 200;
		int y = 200;
		String line2 = sc.nextLine();
		Scanner lineScanner2 = new Scanner(line2);
		if (lineScanner2.next().equals("size")) {
			x = lineScanner2.nextInt();
			y = lineScanner2.nextInt();
			if (x <= 0 || y <= 0) {
				worldFormattedCorrectly = false;
				loadWorldWarnings.append(
						"Warning: Cannot create world with nonpositive dimensions. Giving dimensions 200x200.\n");
			}
			lineScanner2.close();
		} else {
			worldFormattedCorrectly = false;
			loadWorldWarnings.append("Warning: Dimensions not formatted correctly. Giving dimensions 200x200.\n");
			lineScanner2.close();
		}
		this.world = new World(name, x, y);
		this.world.setEnableManna(enableManna);
		this.world.setEnableForcedMutation(enableForcedMutation);
		if (!worldFormattedCorrectly) {
			WarningMessage.display(
					"While loading in the world, the following warnings occured: \n" + loadWorldWarnings.toString());
		}

		// reading in rest of file (tiles)
		boolean tilesFormattedCorrectly = true;
		StringBuilder warningString = new StringBuilder();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			Scanner lineScanner = new Scanner(line);
			String tileType = lineScanner.next();
			
			switch (tileType) {
			case ("rock"):
				try {
					int c1 = lineScanner.nextInt();
					int r1 = lineScanner.nextInt();
					this.world.setTile(c1, r1, new Tile());
				} catch (ArrayIndexOutOfBoundsException a) {
					tilesFormattedCorrectly = false;
					warningString.append(
							"Warning: Rock placed out of bounds, but note that placing rocks outside the board does nothing. Moving onto the next one.\n");
				} catch (InputMismatchException e) {
					tilesFormattedCorrectly = false;
					warningString.append("Warning: Rock not formatted correctly. Placing the rock randomly...\n");
					this.world.placeTileRandomly(new Tile());
				} finally {
					lineScanner.close();
				}
				break;
			case ("food"):
				try {
					int c2 = lineScanner.nextInt();
					int r2 = lineScanner.nextInt();
					int foodAmount = lineScanner.nextInt();
					if (foodAmount <= 0) {
						tilesFormattedCorrectly = false;
						warningString
								.append("Warning: Can't place nonpositive amount of food. Placing default of 100.\n");
						foodAmount = 100;
					}
					this.world.setTile(c2, r2, new Tile(foodAmount));
				} catch (ArrayIndexOutOfBoundsException a) {
					tilesFormattedCorrectly = false;
					warningString.append("Warning: Food placed out of bounds. Placing 100 food randomly...\n");
					this.world.placeTileRandomly(new Tile(100));
				} catch (InputMismatchException e) {
					tilesFormattedCorrectly = false;
					warningString.append("Warning: Food not formatted correctly. Placing 100 food randomly...\n");
					this.world.placeTileRandomly(new Tile(100));
				} finally {
					lineScanner.close();
				}
				break;
			case ("critter"):
				try {
					String critterFile = lineScanner.next();
					int c3 = lineScanner.nextInt();
					int r3 = lineScanner.nextInt();
					int direction = lineScanner.nextInt();
					String absoluteCritterFile = parentFile.toString() + "/" + critterFile;
					Maybe<Critter> maybeCritter = loadCritter(absoluteCritterFile, direction);
					Critter critter;
					try {
						critter = maybeCritter.get();
						this.world.setTile(c3, r3, critter);
					} catch (NoMaybeValue e) {
						tilesFormattedCorrectly = false;
						warningString.append("Warning: Critter file not found. Placing a new critter randomly...\n");
						this.world.placeTileRandomly(new Critter());
					} catch (ArrayIndexOutOfBoundsException a) {
						tilesFormattedCorrectly = false;
						warningString
								.append("Warning: Critter placed out of bounds. Placing a new critter randomly...\n");
						this.world.placeTileRandomly(new Critter());
					}
				} catch (InputMismatchException e) {
					tilesFormattedCorrectly = false;
					warningString.append("Warning: Critter not formatted correctly. Placing the critter randomly...\n");
					this.world.placeTileRandomly(new Critter());
				} finally {
					lineScanner.close();
				}

				break;
			default:
				tilesFormattedCorrectly = false;
				warningString.append("Warning: Tile not formatted correctly. Placing a rock randomly...\n");
				this.world.placeTileRandomly(new Tile());
				break;
			}
		}
		if (!tilesFormattedCorrectly) {
			WarningMessage
					.display(
							"While some of the tiles were being placed, the following warnings occurred: \n"
									+ warningString.toString());
		}
		sc.close();
		return loadedCorrectly;
	}

	/**
	 * Helper method that loads a critter from a file.
	 * 
	 * @param filename  Name of the file of the critter.
	 * @param direction Direction of the critter.
	 * @return Maybe of a critter, returns Maybe.none() if something goes wrong.
	 */
	private Maybe<Critter> loadCritter(String filename, int direction) {
		// reading in critter file
		BufferedReader critterReader;
		try {
			critterReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			return Maybe.none();
		}

		// reading in species name
		String critterLine1;
		try {
			critterLine1 = critterReader.readLine();
		} catch (IOException e) {
			try {
				critterReader.close();
			} catch (IOException e1) {
			}
			return Maybe.none();
		}
		Scanner critterLineScanner1 = new Scanner(critterLine1);
		StringBuilder speciesStringBuilder = new StringBuilder();
		String species = "Critter X";
		StringBuilder warningString = new StringBuilder();
		boolean critterFormattedCorrectly = true;

		if (critterLineScanner1.next().equals("species:")) {
			while (critterLineScanner1.hasNext()) {
				speciesStringBuilder.append(critterLineScanner1.next());
				speciesStringBuilder.append(" ");
			}
			critterLineScanner1.close();
			species = speciesStringBuilder.toString().substring(0, speciesStringBuilder.length() - 1);
		} else {
			critterFormattedCorrectly = false;
			warningString.append("Warning: Species name not formatted correctly. Giving name \"Critter X\"\n");
			critterLineScanner1.close();
		}

		int memSize = 7;
		int defense = 10;
		int offense = 10;
		int size = 10;
		int energy = 10;
		int posture = 10;
		for (int i = 0; i < 6; i++) {
			Scanner critterLineScanner;
			try {
				critterLineScanner = new Scanner(critterReader.readLine());
			} catch (IOException e) {
				return null;
			}
			String memElem = critterLineScanner.next();
			switch (memElem) {
			case ("memsize:"):
				memSize = critterLineScanner.nextInt();
				if (memSize < 7) {
					memSize = 7;
				}
				critterLineScanner.close();
				break;
			case ("defense:"):
				defense = critterLineScanner.nextInt();
				if (defense < 1) {
					defense = 10;
				}
				critterLineScanner.close();
				break;
			case ("offense:"):
				offense = critterLineScanner.nextInt();
				if (offense < 1) {
					offense = 10;
				}
				critterLineScanner.close();
				break;
			case ("size:"):
				size = critterLineScanner.nextInt();
				if (size < 1) {
					size = 10;
				}
				critterLineScanner.close();
				break;
			case ("energy:"):
				energy = critterLineScanner.nextInt();
				if (energy < 1) {
					energy = 10;
				}
				critterLineScanner.close();
				break;
			case ("posture:"):
				posture = critterLineScanner.nextInt();
				if (posture < 0 || posture > 99) {
					posture = 10;
				}
				critterLineScanner.close();
				break;
			default:
				critterFormattedCorrectly = false;
				warningString.append("Warning: Memory not formatted correctly. Giving default value and moving on.\n");
				critterLineScanner.close();
				break;
			}
		}
		if (!critterFormattedCorrectly) {
			WarningMessage.display(
					"While loading in the critters, the following warnings occurred:\n" + warningString.toString());
		}

		Parser parser = ParserFactory.getParser();
		Maybe<Program> prog = parser.parse(critterReader);
		ProgramImpl program;
		try {
			program = (ProgramImpl) prog.get();
		} catch (NoMaybeValue e) {
			WarningMessage.display(
					"Syntax Error: the critter's program had syntax errors. Check the console for more information.");
			return Maybe.none();
		}
		return Maybe.some(new Critter(species, memSize, defense, offense, size, energy, posture, direction, program));
	}

	@Override
	public boolean loadCritters(String filename, int n) {
		boolean loadedCorrectly = true;

		Random random = new Random();
		for (int i = 0; i < n; i++) {
			Critter critter;
			int d = random.nextInt(6);
			try {
				Maybe<Critter> maybeCritter = loadCritter(filename, 0);
				critter = maybeCritter.get();
				critter.setDirection(d);
				this.world.placeTileRandomly(critter);
			} catch (NoMaybeValue e) {
				return false;
			}
		}
		return loadedCorrectly;
	}

	public boolean loadCritterOnSpecificTile(String filename, int c, int r) {
		boolean loadedCorrectly = true;
		Random random = new Random();
		int d = random.nextInt(6);
		Maybe<Critter> maybeCritter = loadCritter(filename, d);
		Critter critter;
		try {
			critter = maybeCritter.get();
			critter.setDirection(d);
		} catch (NoMaybeValue e) {
			return false;
		}
		try {
			world.setTile(c, r, critter);
		} catch (ArrayIndexOutOfBoundsException e) {
			return false;
		}
		return loadedCorrectly;
	}

	@Override
	public boolean advanceTime(int n) {
		Interpreter intr = new Interpreter(world);
		Action a;
		Executor e = new Executor();
		if (n < 0 || this.world == null)
			return false;
		for (int i = 0; i < n; i++) {
			this.world.setSteps(1);
			ArrayList<Critter> critters = this.world.getCritters();
			for (int j = 0; j < this.world.getNumberOfAliveCritters(); j++) {
				Critter crit = critters.get(j);
				a = intr.interpret(crit);
				switch (a.getTokenType()) {
				case ATTACK:
					e.attack(world, crit);
					break;
				case BACKWARD:
					e.backward(world, crit);
					break;
				case BUD:
					e.bud(world, crit);
					break;
				case EAT:
					e.eat(world, crit);
					break;
				case FORWARD:
					e.forward(world, crit);
					break;
				case GROW:
					e.grow(world, crit);
					break;
				case MATE:
					e.mate(world, crit);
					break;
				case SERVE:
					e.serve(world, crit, intr.evaluateExpression((Expr) a.getChildren().get(0)));
					break;
				case WAIT:
					e.wait(world, crit);
					break;
				case LEFT:
					e.left(world, crit);
					break;
				case RIGHT:
					e.right(world, crit);
					break;
				default:
					e.wait(world, crit);
					break;
				}

				if (this.world.getEnableForcedMutation()) {
					Program p = crit.getProgram();
					p.mutate();
				}
			}
			// so children don't get read through a pass through
			world.addNewCritters(e.children);
			e.children.clear();
			// add food if manna is enabled
			if (this.world.getEnableManna()) {
				Random r = new Random();
				if (this.world.getNumberOfAliveCritters() == 0) {

				} else if (r.nextInt(this.world.getNumberOfAliveCritters()) == 0) {
					int numHexes = this.world.getNumHexes();
					int[] indices = r.ints(0, numHexes).distinct()
							.limit(Constants.MANNA_COUNT * Math.floorDiv(numHexes, 1000)).toArray();
					for (int index : indices) {
						int x = index / (int) Math.ceil(this.world.getDimensions()[0] / 2.0);
						int y = index % (int) Math.ceil(this.world.getDimensions()[0] / 2.0);
						String tileString = this.world.getTileStringRectangular(x, y);
						switch (tileString) {
						case ("-"):
							this.world.addNewFoodCoordinates(new Integer[] { x, y });
							this.world.setTileRectangular(x, y, new Tile(Constants.MANNA_AMOUNT));
							break;
						case ("F"):
							this.world.setTileRectangular(x, y, new Tile(
									Constants.MANNA_AMOUNT + (this.world.getTerrainInfoRectangular(x, y) * -1 - 1)));
							break;
						default:
							break;
						}
					}
				}
			}
		}
		int maxSize = 1;
		for(Critter c: this.world.getCritters()) {
			if (c.getMemory()[3] > maxSize) {
				maxSize = c.getMemory()[3];
			}
		}
		this.world.maxSize = maxSize;
		return true;
	}

	@Override
	public void printWorld(PrintStream out) {
		if (this.world.getDimensions()[0] % 2 == 0) {
			for (int i = 0; i < this.world.getDimensions()[1]; i++) {
				if (i % 2 == 0) {
					System.out.print("  ");
				}
				for (int j = 0; j < this.world.getDimensions()[0] / 2; j++) {
					out.print(this.world.getTileStringRectangular(i, j));
					out.print("   ");
				}
				out.print("\n");
			}
		} else {
			for (int i = 0; i < this.world.getDimensions()[1]; i++) {
				if (i % 2 == 0) {
					for (int j = 0; j < (int) Math.ceil(this.world.getDimensions()[0] / 2.0); j++) {
						out.print(this.world.getTileStringRectangular(i, j));
						out.print("   ");
					}
				} else {
					out.print("  ");
					for (int j = 0; j < this.world.getDimensions()[0] / 2; j++) {
						out.print(this.world.getTileStringRectangular(i, j));
						out.print("   ");
					}
				}
				out.print("\n");
			}
		}
	}
}
