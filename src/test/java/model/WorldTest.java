package model;

import java.util.Random;

import cms.util.maybe.NoMaybeValue;

public class WorldTest {
	public static void main(String[] args) {
		empty3x3();
		world3x3();
		world4x3();
		world3x4();
		world4x4();
		world7x9();
		testPlacingTiles();
	}

	public static void printWorld(World world) {
		System.out.println(world.getName());
		if (world.getDimensions()[0] % 2 == 0) {
			for (int i = 0; i < world.getDimensions()[1]; i++) {
				if (i % 2 == 0) {
					System.out.print("  ");
				}
				for (int j = 0; j < world.getDimensions()[0] / 2; j++) {
					System.out.print(world.getTileStringRectangular(i, j));
					System.out.print("   ");
				}
				System.out.print("\n");
			}
		} else {
			for (int i = 0; i < world.getDimensions()[1]; i++) {
				if (i % 2 == 0) {
					for (int j = 0; j < (int) Math.ceil(world.getDimensions()[0] / 2.0); j++) {
						System.out.print(world.getTileStringRectangular(i, j));
						System.out.print("   ");
					}
				} else {
					System.out.print("  ");
					for (int j = 0; j < world.getDimensions()[0] / 2; j++) {
						System.out.print(world.getTileStringRectangular(i, j));
						System.out.print("   ");
					}
				}

				System.out.print("\n");
			}
		}

	}

	public static void empty3x3() {
		World world = new World("empty 3x3", 3, 3);
		printWorld(world);
		System.out.println("Tile at (1, 1): " + world.getTerrainInfo(1, 1) + " is empty.");
		System.out.println("Tile at (1, 0): " + world.getTerrainInfo(1, 0) + " is a rock. It's out of bounds.");
	}

	public static void world3x3() {
		World world = new World("3x3", 3, 3);
		Tile food1 = new Tile(300);
		Tile food2 = new Tile(200);
		Tile rock1 = new Tile();
		Random r = new Random();
		Tile critter1 = new Critter("E. Coli", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		world.setTile(1, 1, food1);
		world.setTile(2, 1, food2);
		world.setTile(0, 1, rock1);
		world.setTile(2, 2, critter1);
		printWorld(world);
		try {
			System.out
					.println("Tile at (2, 2): " + world.getReadOnlyCritter(2, 2).get().getSpecies() + " is a critter.");
		} catch (NoMaybeValue e) {
			e.printStackTrace();
		}
		System.out.println("Tile at (1, 1): " + world.getTerrainInfo(1, 1) + " has 300 food.");
		System.out.println("Tile at (0, 0): " + world.getTerrainInfo(0, 0) + " is empty.");
		System.out.println("Tile at (0, 1): " + world.getTerrainInfo(0, 1) + " is a rock.");
		System.out.println("Tile at (3, 1): " + world.getTerrainInfo(3, 1) + " is a rock. It's out of bounds.");
		System.out.println();
	}

	public static void world4x3() {
		World world = new World("4x3", 4, 3);
		Tile food1 = new Tile(300);
		Tile food2 = new Tile(200);
		Tile rock1 = new Tile();
		Random r = new Random();
		Tile critter1 = new Critter("E. Coli", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		Tile critter2 = new Critter("Streptococcus Pneumoniae", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		world.setTile(1, 1, food1);
		world.setTile(2, 1, food2);
		world.setTile(3, 2, rock1);
		world.setTile(0, 0, critter1);
		world.setTile(1, 0, critter2);
		printWorld(world);
		try {
			System.out
					.println("Tile at (0, 0): " + world.getReadOnlyCritter(0, 0).get().getSpecies() + " is a critter.");
			System.out
					.println("Tile at (1, 0): " + world.getReadOnlyCritter(1, 0).get().getSpecies() + " is a critter.");
		} catch (NoMaybeValue e) {
			e.printStackTrace();
		}
		System.out.println("Tile at (1, 1): " + world.getTerrainInfo(1, 1) + " has 300 food.");
		System.out.println("Tile at (3, 1): " + world.getTerrainInfo(3, 1) + " is empty.");
		System.out.println("Tile at (3, 2): " + world.getTerrainInfo(3, 2) + " is a rock.");
		System.out.println("Tile at (4, 2): " + world.getTerrainInfo(4, 2) + " is a rock. It's out of bounds.");
		System.out.println();
	}

	public static void world3x4() {
		World world = new World("3x4", 3, 4);
		Tile food1 = new Tile(300);
		Tile food2 = new Tile(200);
		Tile rock1 = new Tile();
		Random r = new Random();
		Tile critter1 = new Critter("E. Coli", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		Tile critter2 = new Critter("Streptococcus Pneumoniae", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		world.setTile(1, 1, food1);
		world.setTile(2, 1, food2);
		world.setTile(0, 1, rock1);
		world.setTile(0, 0, critter1);
		world.setTile(1, 0, critter2);
		printWorld(world);
		try {
			System.out
					.println("Tile at (0, 0): " + world.getReadOnlyCritter(0, 0).get().getSpecies() + " is a critter.");
			System.out
					.println("Tile at (1, 0): " + world.getReadOnlyCritter(1, 0).get().getSpecies() + " is a critter.");
		} catch (NoMaybeValue e) {
			e.printStackTrace();
		}
		System.out.println("Tile at (1, 1): " + world.getTerrainInfo(1, 1) + " has 300 food.");
		System.out.println("Tile at (2, 2): " + world.getTerrainInfo(2, 2) + " is empty.");
		System.out.println("Tile at (0, 1): " + world.getTerrainInfo(0, 1) + " is a rock.");
		System.out.println("Tile at (3, 2): " + world.getTerrainInfo(3, 2) + " is a rock. It's out of bounds.");
		System.out.println();
	}

	public static void world4x4() {
		World world = new World("4x4", 4, 4);
		Tile food1 = new Tile(300);
		Tile food2 = new Tile(200);
		Tile rock1 = new Tile();
		Tile rock2 = new Tile();
		Random r = new Random();
		Tile critter1 = new Critter("E. Coli", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		Tile critter2 = new Critter("Streptococcus Pneumoniae", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		world.setTile(1, 1, food1);
		world.setTile(2, 1, food2);
		world.setTile(0, 1, rock1);
		world.setTile(0, 0, rock2);
		world.setTile(3, 2, critter1);
		world.setTile(3, 3, critter2);
		printWorld(world);
		try {
			System.out
					.println("Tile at (3, 2): " + world.getReadOnlyCritter(3, 2).get().getSpecies() + " is a critter.");
			System.out
					.println("Tile at (3, 3): " + world.getReadOnlyCritter(3, 3).get().getSpecies() + " is a critter.");
		} catch (NoMaybeValue e) {
			e.printStackTrace();
		}
		System.out.println("Tile at (1, 1): " + world.getTerrainInfo(1, 1) + " has 300 food.");
		System.out.println("Tile at (1, 2): " + world.getTerrainInfo(1, 2) + " is empty.");
		System.out.println("Tile at (0, 1): " + world.getTerrainInfo(0, 1) + " is a rock.");
		System.out.println("Tile at (4, 4): " + world.getTerrainInfo(4, 4) + " is a rock. It's out of bounds.");
		System.out.println();
	}

	public static void world7x9() {
		World world = new World("7x9", 7, 9);
		Tile food1 = new Tile(300);
		Tile rock1 = new Tile();
		Tile rock2 = new Tile();
		Tile rock3 = new Tile();
		Random r = new Random();
		Tile critter1 = new Critter("E. Coli", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		Tile critter2 = new Critter("Streptococcus Pneumoniae", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		world.setTile(2, 5, food1);
		world.setTile(0, 4, rock1);
		world.setTile(6, 6, rock2);
		world.setTile(0, 1, rock3);
		world.setTile(2, 1, critter1);
		world.setTile(2, 3, critter2);
		printWorld(world);
		System.out.println("Tile at (0, 4): " + world.getTerrainInfo(0, 4) + " is a rock.");
		System.out.println("Tile at (2, 5): " + world.getTerrainInfo(2, 5) + " has 300 food.");
		System.out.println("Tile at (6, 6): " + world.getTerrainInfo(6, 6) + " is a rock.");
		System.out.println("Tile at (0, 1): " + world.getTerrainInfo(0, 1) + " is a rock.");
		try {
			System.out
					.println("Tile at (2, 1): " + world.getReadOnlyCritter(2, 1).get().getSpecies() + " is a critter.");
			System.out
					.println("Tile at (2, 3): " + world.getReadOnlyCritter(2, 3).get().getSpecies() + " is a critter.");
		} catch (NoMaybeValue e) {
			e.printStackTrace();
		}
		System.out.println();
	}

	public static void testPlacingTiles() {
		World world = new World("50x50", 50, 50);
		Random random = new Random();
		for (int i = 0; i < 1000; i++) {
			int c = random.nextInt(world.getDimensions()[0]);
			int r = random.ints((int) Math.ceil(c / 2.0), (int) Math.ceil((c + world.getDimensions()[1]) / 2.0))
					.findFirst().getAsInt();
			Tile rock = new Tile();
			world.setTile(c, r, rock);
		}
		printWorld(world);
		System.out.println();
	}
}
