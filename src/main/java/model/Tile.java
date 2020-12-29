package model;

public class Tile {
	/*
	 * Class Invariant: if isRock is true, then foodAmount = 0 If foodAmount > 0,
	 * then isRock is false
	 */

	/**
	 * amount of food located at Tile (0 if this tile doesn't represent food)
	 * 
	 */
	private int foodAmount;

	/**
	 * Type of tile this tile is.
	 */
	protected TileType tileType;

	/**
	 * Constructor for rocks.
	 * 
	 */
	public Tile() {
		this.foodAmount = 0;
		tileType = TileType.ROCK;
	}

	/**
	 * Constructor for food and empty.
	 * 
	 * @param foodAmount Amount of food located at this Tile.
	 */
	public Tile(int foodAmount) {
		this.foodAmount = foodAmount;
		if (foodAmount == 0) {
			tileType = TileType.EMPTY;
		} else {
			tileType = TileType.FOOD;
		}
	}

	/**
	 * Returns the amount of food on this Tile.
	 * 
	 * @return amount of food on this Tile.
	 */
	public int foodAmount() {
		return foodAmount;
	}

	/**
	 * Sets the amount of food on this Tile.
	 */
	public void setFoodAmount(int foodAmount) {
		this.foodAmount = foodAmount;
	}

	public void makeEmpty() {
		foodAmount = 0;
		tileType = TileType.EMPTY;
	}

	/**
	 * Returns what type of tile this is.
	 * 
	 * @return tileType, the type of tile this is.
	 */
	public TileType getTileType() {
		return tileType;
	}

	/**
	 * Sets what type of tile this is.
	 */
	public void setTileType(TileType tileType) {
		this.tileType = tileType;
	}

}
