package model;

import java.util.ArrayList;
import java.util.Random;

import cms.util.maybe.Maybe;

public class World implements ReadOnlyWorld {
	/**
	 * Order in which critters execute their rules.
	 */
	private ArrayList<Critter> critters = new ArrayList<Critter>();

	/**
	 * Name of world
	 */
	private String name = "New World";

	/**
	 * Width of world
	 */
	private int x = 50;
	/**
	 * Height of world
	 */
	private int y = 50;
	/**
	 * Number of time steps elapsed.
	 */
	private int timeElapsed = 0;
	/**
	 * Contents of the world. Will have dimensions y and ceiling of x/2.
	 */
	private Tile[][] contents = new Tile[y][x / 2]; // nulls represent empty tiles

	/**
	 * True if the dimensions have the same parity (both odd or both even).
	 */
	private boolean sameParity = true;

	/**
	 * Number of rocks in the world.
	 */
	private int numRocks = 0;

	/**
	 * Number of food tiles in the world.
	 */
	private int numFood = 0;

	/**
	 * Number of hexes in the world.
	 */
	private int numHexes = x * y / 2;

	/**
	 * Whether or not manna is on
	 */
	private boolean enableManna;

	/**
	 * Whether or not forced mutation is on
	 */
	private boolean enableForcedMutation;

	/**
	 * Newly added food coordinates via manna
	 */
	private ArrayList<Integer[]> newFoodCoordinates = new ArrayList<Integer[]>();

	/**
	 * Newly added empty coordinates when eating
	 */
	private ArrayList<Integer[]> newEmptyCoordinates = new ArrayList<Integer[]>();

	/**
	 * Constructor to create a new world with dimensions x, y and some contents.
	 * 
	 * @param name     Name of world.
	 * @param x        Width of world.
	 * @param y        Height of world.
	 */
	
	public int maxSize = 1;
	
	public World(String name, int x, int y) {
		this.name = name;
		this.x = x;
		this.y = y;
		this.contents = new Tile[y][(int) Math.ceil(x / 2.0)];
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < (int) Math.ceil(x / 2.0); j++) {
				this.contents[i][j] = new Tile(0); // sets all tiles to empty
			}
		}
		if ((x % 2 == 0 && y % 2 == 0) || (x % 2 == 1 && y % 2 == 1)) {
			this.sameParity = true;
		} else {
			this.sameParity = false;
		}
		this.numHexes = (int) Math.ceil((x * y) / 2.0);
	}

	/**
	 * Constructor to create a new world with name "New World," dimensions 200, 200
	 * and random rocks.
	 */
	public World() {
		for (int i = 0; i < y; i++) {
			for (int j = 0; j < x / 2; j++) {
				this.contents[i][j] = new Tile(0); // sets all tiles to empty
			}
		}
		Random random = new Random();
		int numRocks = random.nextInt(numHexes / 4);
		this.numRocks = numRocks;
		int[] rockIndices = random.ints(0, numHexes).distinct().limit(numRocks).toArray();
		for (int n : rockIndices) {
			int c = n / (x / 2);
			int r = n % (x / 2);
			this.contents[c][r] = new Tile(); // adds a rock at this index
		}
	}

	/**
	 * Returns this world's name.
	 * 
	 * @return name of world.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns number of hexes in the world.
	 * 
	 * @return numHexes, number of hexes in the world.
	 */
	public int getNumHexes() {
		return this.numHexes;
	}

	/**
	 * Returns number of rocks in the world
	 * 
	 * @return numRocks, number of rocks in the world.
	 */
	public int getNumRocks() {
		return this.numRocks;
	}

	/**
	 * Returns number of food tiles in the world
	 * 
	 * @return numFood, number of food tiles in the world.
	 */
	public int getNumFood() {
		return this.numFood;
	}

	/**
	 * Returns the critters in the world.
	 * 
	 * @return critters, a list of the critters (in the order they were added)
	 */
	public ArrayList<Critter> getCritters() {
		ArrayList<Critter> copy = new ArrayList<Critter>(this.critters);
		return copy;
	}

	/**
	 * Adds new critters to the critter order.
	 */
	public void addNewCritters(ArrayList<Critter> newCritters) {
		
		this.critters.addAll(newCritters);
		for(Critter c: critters) {
			if(c.getMemory()[3]>maxSize) {
				maxSize = c.getMemory()[3];
			}
		}
	}

	/**
	 * Removes a critter from the critter order because it died.
	 */
	public void killCritter(Critter deadCritter) {
		this.critters.remove(deadCritter);
	}

	/**
	 * Returns the dimensions of the world.
	 * 
	 * @return an array containing x, y, the width and height of the world.
	 */
	public int[] getDimensions() {
		return new int[] { x, y };
	}

	/**
	 * Set a particular tile to be a food, rock, or critter.
	 * 
	 * @param c    Column index of the tile.
	 * @param r    Row index of the tile.
	 * @param tile Food, rock, or critter that will be placed at this tile.
	 */
	public void changeTile(int c, int r, Tile tile) throws ArrayIndexOutOfBoundsException {
		int[] rectangularCoordinates;
		rectangularCoordinates = convertToRectangular(c, r);
		int x = rectangularCoordinates[0];
		int y = rectangularCoordinates[1];
		contents[x][y] = tile;

		switch (tile.getTileType()) {
		case CRITTER:

			this.critters.add((Critter) tile);
			((Critter) tile).setPosition(c, r);
			for (Critter critt : critters) {
				if (critt.getMemory()[3] > maxSize) {
					maxSize = critt.getMemory()[3];
				}
			}
			break;
		case ROCK:
			this.numRocks++;
			break;
		case FOOD:
			this.numFood++;
			break;
		default:
			break;

		}
	}

	/**
	 * Set a particular tile to be a food, rock, or critter.
	 * 
	 * @param c    Column index of the tile.
	 * @param r    Row index of the tile.
	 * @param tile Food, rock, or critter that will be placed at this tile.
	 */
	public void setTile(int c, int r, Tile tile) throws ArrayIndexOutOfBoundsException {
		int[] rectangularCoordinates;
		rectangularCoordinates = convertToRectangular(c, r);
		int x = rectangularCoordinates[0];
		int y = rectangularCoordinates[1];
		Tile currentTile = contents[x][y];
		if (!currentTile.getTileType().equals(TileType.EMPTY)) {
			placeTileRandomly(tile);
			return;
		}
		contents[x][y] = tile;	

		switch (tile.getTileType()) {
		case CRITTER:

			this.critters.add((Critter)tile);
			((Critter)tile).setPosition(c, r);
			for(Critter critt: critters) {
				if(critt.getMemory()[3]>maxSize) {
					maxSize = critt.getMemory()[3];
				}
			}
			break;
		case ROCK:
			this.numRocks++;
			break;
		case FOOD:
			this.numFood++;
			break;
		default:
			break;

		}
	}

	/**
	 * Move a critter to this tile
	 * 
	 * @param c       Column index of the tile.
	 * @param r       Row index of the tile.
	 * @param critter that will move to this tile
	 */
	public void setTile2(int c, int r, Tile tile) throws ArrayIndexOutOfBoundsException {
		int[] rectangularCoordinates;
		rectangularCoordinates = convertToRectangular(c, r);
		int x = rectangularCoordinates[0];
		int y = rectangularCoordinates[1];
		contents[x][y] = tile;
		if (tile.getTileType().equals(TileType.CRITTER)) {
			((Critter) tile).setPosition(c, r);
		}
	}

	/**
	 * Helper method that randomly places a tile in the world.
	 * 
	 * @param tile Tile to be placed randomly in the world.
	 */
	public void placeTileRandomly(Tile tile) {
		if (this.getNumHexes() == this.getNumFood() + this.getNumberOfAliveCritters() + this.getNumRocks()) {
			return;
		}

		Random random = new Random();
		int c = random.nextInt(this.getDimensions()[0]);
		int r = random.ints((int) Math.ceil(c / 2.0), (int) Math.ceil((c + this.getDimensions()[1]) / 2.0)).findFirst()
				.getAsInt();
		// if the tile isn't empty, we can't place the critter there, so try again
		if (!this.getTile(c, r).getTileType().equals(TileType.EMPTY)) {
			placeTileRandomly(tile);
			return;
		}
		this.setTile(c, r, tile);
	}

	/**
	 * Set tile using rectangular coordinates
	 * 
	 * @param x       First index of contents.
	 * @param y       Second index of contents
	 * @param critter that will move to this tile
	 */
	public void setTileRectangular(int x, int y, Tile tile) throws ArrayIndexOutOfBoundsException {
		contents[x][y] = tile;
	}

	/**
	 * Converts hexagonal coordinates to indices in 2D array of contents.
	 * 
	 * @param c Column index of the tile.
	 * @param r Row index of the tile.
	 * @return rectangularCoordinates x, y which are indices in contents
	 */
	public int[] convertToRectangular(int c, int r) throws ArrayIndexOutOfBoundsException {
		if (2 * r - c > this.y - 1 || c > this.x - 1 || c < 0 || r < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		int x;
		int y;
		int[] rectangularCoordinates;
		x = this.y - 2 * (r - c / 2) - 1;
		y = c / 2;
		if (sameParity) {
			if (c % 2 == 1) {
				x++;
			}
		} else {
			if (c % 2 == 0) {
				x--;
			}
		}
		if (x > this.y - 1 || y > this.x - 1 || x < 0 || y < 0) {
			throw new ArrayIndexOutOfBoundsException();
		}
		rectangularCoordinates = new int[] { x, y };
	
		return rectangularCoordinates;
	}

	/**
	 * Gets the particular tile and returns the corresponding string.
	 * 
	 * @param c Column index of the tile.
	 * @param r Row index of the tile.
	 * @return String representation of that tile, "-" for empty, "d" for a critter
	 *         with direction d, "#" for a rock, "F" for food
	 */
	public String getTileString(int c, int r) {
		int[] rectangularCoordinates;
		try {
			rectangularCoordinates = convertToRectangular(c, r);
		} catch (ArrayIndexOutOfBoundsException e) {
			return "#";
		}
		int x = rectangularCoordinates[0];
		int y = rectangularCoordinates[1];
		switch (contents[x][y].getTileType()) {
		case CRITTER:
			return "" + ((Critter) contents[x][y]).getDirection();
		case FOOD:
			return "F";
		case ROCK:
			return "#";
		case EMPTY:
			return "-";
		default:
			return "E"; // error
		}
	}

	/**
	 * Gets the particular tile and returns the corresponding string using
	 * rectangular coordinates.
	 * 
	 * @param x First index of contents.
	 * @param y Second index of contents
	 * @return String representation of that tile, "-" for empty, "d" for a critter
	 *         with direction d, "#" for a rock, "F" for food
	 */
	public String getTileStringRectangular(int x, int y) {
		switch (contents[x][y].getTileType()) {
		case CRITTER:
			return "" + ((Critter) contents[x][y]).getDirection();
		case FOOD:
			return "F";
		case ROCK:
			return "#";
		case EMPTY:
			return "-";
		default:
			return "E"; // error
		}
	}

	/**
	 * Return the number of time steps elapsed.
	 */
	@Override
	public int getSteps() {
		return this.timeElapsed;
	}

	/**
	 * Set the number of time steps elapsed.
	 */
	public void setSteps(int n) {
		this.timeElapsed += n;
	}

	/**
	 * Return the number of critters in the world.
	 */
	@Override
	public int getNumberOfAliveCritters() {
		try {

			return this.critters.size();
		} catch (NullPointerException e) {
			return 0;
		}

	}

	/**
	 * Return the critter at a selected location. Returns Maybe.none() for
	 * non-critter types.
	 */
	@Override
	public Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r) {
		int[] rectangularCoordinates;
		try {
			rectangularCoordinates = convertToRectangular(c, r);
		} catch (ArrayIndexOutOfBoundsException e) {
			return Maybe.none(); // it's a rock
		}
		int x = rectangularCoordinates[0];
		int y = rectangularCoordinates[1];
		Tile tile = contents[x][y];
		if (tile == null) {
			return Maybe.none(); // it's an empty tile
		} else if (tile.getTileType().equals(TileType.CRITTER)) {
			return Maybe.some((Critter) tile);
		}
		return Maybe.none(); // it's a non-critter tile
	}

	/**
	 * Returns the int representation of a particular non-critter tile.
	 */
	@Override
	public int getTerrainInfo(int c, int r) {
		int[] rectangularCoordinates;
		try {
			rectangularCoordinates = convertToRectangular(c, r);
		} catch (ArrayIndexOutOfBoundsException e) {
			return -1; // it's a rock
		}
		int x = rectangularCoordinates[0];
		int y = rectangularCoordinates[1];
		Tile tile = contents[x][y];
		switch (tile.getTileType()) {
		case FOOD:
			return -1 * (tile.foodAmount() + 1);
		case ROCK:
			return -1;
		case EMPTY:
			return 0;
		default:
			throw new IllegalArgumentException(); // it's a critter
		}
	}
	
	/**
	 * Returns the int representation of a particular non-critter tile using
	 * rectangular coordinates
	 * 
	 * @param x First index of contents.
	 * @param y Second index of contents
	 */
	public int getTerrainInfoRectangular(int x, int y) {
		Tile tile = contents[x][y];
		switch (tile.getTileType()) {
		case FOOD:
			return -1 * (tile.foodAmount() + 1);
		case ROCK:
			return -1;
		case EMPTY:
			return 0;
		default:
			throw new IllegalArgumentException(); // it's a critter
		}
	}

	public int getWidth() {
		return this.x;
	}
	
	public int getHeight() {
		return this.y;
	}
	
	public Tile [][] getContents(){
		return this.contents;
	}

	public Tile getTile(int c, int r) {
		int[] rectangularCoordinates;
		try {
			rectangularCoordinates = convertToRectangular(c, r);
		} catch (ArrayIndexOutOfBoundsException e) {
			return new Tile();
		}
		int x = rectangularCoordinates[0];
		int y = rectangularCoordinates[1];
		return contents[x][y];
	}

	public Tile getTileRectangular(int x, int y) {
		Tile tile;
		try {
			tile = contents[x][y];
		} catch (ArrayIndexOutOfBoundsException e) {
			return new Tile();
		}
		return tile;
	}

	/**
	 * Reports information about a tile with hex coordinates (x,y)
	 * 
	 * @param x the column hex coordinate
	 * @param y the row hex coordinate
	 * @return the int representation of the tile at the given coordinates
	 */
	public int getTileInfo(int c, int r) {
		Tile t = this.getTile(c, r);
		switch (t.getTileType()) {
		case CRITTER:
			Critter critter = (Critter) t;
			return critter.getMemory()[6];
		case EMPTY:
			return 0;
		case FOOD:
			return -1 * (t.foodAmount() + 1);
		case ROCK:
			return -1;
		default:
			return -1;
		}
	}

	/**
	 * Gets enable manna
	 * 
	 * @return
	 */
	public boolean getEnableManna() {
		return this.enableManna;
	}

	/**
	 * Gets enable forced mutation
	 * 
	 * @return
	 */
	public boolean getEnableForcedMutation() {
		return this.enableForcedMutation;
	}

	/**
	 * Sets enable manna
	 * 
	 * @return
	 */
	public void setEnableManna(boolean enableManna) {
		this.enableManna = enableManna;
	}

	/**
	 * Sets forced mutation
	 * 
	 * @return
	 */
	public void setEnableForcedMutation(boolean enableForcedMutation) {
		this.enableForcedMutation = enableForcedMutation;
	}

	/**
	 * Gets new food coordinates
	 * 
	 * @return
	 */
	public ArrayList<Integer[]> getNewFoodCoordinates() {
		return this.newFoodCoordinates;
	}

	/**
	 * Adds new food coordinates
	 * 
	 * @param newFoodCoords
	 */
	public void addNewFoodCoordinates(Integer[] newFoodCoords) {
		this.newFoodCoordinates.add(newFoodCoords);
	}

	/**
	 * Clears the new food coordinates
	 */
	public void clearNewFoodCoordinates() {
		this.newFoodCoordinates = new ArrayList<Integer[]>();
	}

	/**
	 * Gets new empty coordinates
	 * 
	 * @return
	 */
	public ArrayList<Integer[]> getNewEmptyCoordinates() {
		return this.newEmptyCoordinates;
	}

	/**
	 * Adds new empty coordinates
	 * 
	 * @param newFoodCoords
	 */
	public void addNewEmptyCoordinates(Integer[] newEmptyCoords) {
		this.newEmptyCoordinates.add(newEmptyCoords);
	}

	/**
	 * Clears the new empty coordinates
	 */
	public void clearNewEmptyCoordinates() {
		this.newEmptyCoordinates = new ArrayList<Integer[]>();
	}
}
