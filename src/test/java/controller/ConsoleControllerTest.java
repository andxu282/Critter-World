package controller;

import static controller.DeterministicHexInformation.NonCritterHex.EMPTY_HEX;
import static controller.DeterministicHexInformation.NonCritterHex.ROCK_HEX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.Test;

import controller.DeterministicHexInformation.CritterHex;
import model.ReadOnlyWorld;
import model.World;

public final class ConsoleControllerTest {
    private static final String COMMON_TEST_PATH = Paths.get("src", "test", "resources", "A5files").toString();

    @Test
    public void testRandomWorld() {
        // Test a randomly generated world only contains empty and rock.
        final var controller = ControllerFactory.getConsoleController();
        controller.newWorld();
        final var world = controller.getReadOnlyWorld();
        for (int i = -1; i < 100; i++) {
            for (int j = -1; j < 100; j++) {
                final var hexValue = world.getTerrainInfo(i, j);
                if (i == -1 || j == -1) {
                    assertEquals(-1, hexValue, "Out of bound tiles must have rock's hex values");
                }
                assertTrue(hexValue == 0 || hexValue == -1,
                        "A randomly initialized world should only contains empty and rock tiles!");
            }
        }
    }

    @Test
    public void emptyWorldTest() {
        // An empty 10x10 empty world.
        // This test mostly tests that the student's solution return -1 for hex outside of the world.
        runTest(
				Paths.get(COMMON_TEST_PATH, "empty.wld").toString(),
                // Step 0
				new StepState(0, "empty", 10, 10, 0, 0, new ExpectedHex(3, 3, EMPTY_HEX))
        );
    }

    @Test
    public void smallWorldTest() {
        // A simple 1x1 empty world.
        // This test mostly tests that the student's solution return -1 for hex outside of the world.
        runTest(
				Paths.get(COMMON_TEST_PATH, "small_world.txt").toString(),
                // Step 0: the state of the world after load
                new StepState(
						0, "Small", 1, 1, 0, 0,
                        new ExpectedHex(0, 0, EMPTY_HEX),
                        new ExpectedHex(1, 0, ROCK_HEX)),
                // Step 1: the state of the world after one step
                new StepState(
						0, "Small", 1, 1, 0, 0,
                        new ExpectedHex(0, 0, EMPTY_HEX),
                        new ExpectedHex(1, 0, ROCK_HEX),
                        new ExpectedHex(-1, 31, ROCK_HEX),
                        new ExpectedHex(-1, -1, ROCK_HEX))
        );
    }

    @Test
    public void spaceWorldTest() {
        // A simple 1x1 world where the critter does nothing.
        runTest(
				Paths.get(COMMON_TEST_PATH, "space_world.txt").toString(),
                // Step 0
                new StepState(
						1, "space world", 1, 1, 0, 0,
                        new ExpectedHex(0, 0, CritterHex.builderWithEnergy(500).build())
                ),
                // Step 1
                new StepState(
						1, "space world", 1, 1, 0, 0,
						new ExpectedHex(0, 0, CritterHex.builderWithEnergy(501).build())
                )
        );
    }

	@Test
	public void mediumWorldNoCrittersTest() {
		// 10x10 world with no critters
		runTest(Paths.get(COMMON_TEST_PATH, "medium_world_no_critters.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(0, "Medium World No Critters", 10, 10, 4, 3, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301))));
	}

	@Test
	public void mediumWorldWithCrittersTest() {
		// 10x10 world
		runTest(Paths.get(COMMON_TEST_PATH, "medium_world_with_critters.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(3, "Medium World With Critters", 10, 10, 4, 3, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void bigWorldWithCrittersTest() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "big_world_with_critters.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(5, 69, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(6, 70, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void incorrectName() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "name.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "New World", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void wrongDimensions() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "dimensions.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void negativeDimesions() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "negative_dimensions.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void outOfBoundsRock() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "out_of_bounds_rock.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 3, 6,
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void incorrectRock() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "incorrect_rock.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(6, 7, ROCK_HEX),
						new ExpectedHex(2, 30, ROCK_HEX), new ExpectedHex(6, 70, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void outOfBoundsFood() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "out_of_bounds_food.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void incorrectFood() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "incorrect_food.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 10, 4, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void negativeFood() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "negative_food.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void outOfBoundsCritter() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "out_of_bounds_critter.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void incorrectCritter() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "incorrect_critter.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(4, "Big World With Critters", 200, 200, 10, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(30, 50, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void critterNotFound() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "critter_not_found.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(6, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX),
						new ExpectedHex(5, 7, ROCK_HEX), new ExpectedHex(2, 3, ROCK_HEX),
						new ExpectedHex(6, 7, ROCK_HEX), new ExpectedHex(1, 20, ROCK_HEX),
						new ExpectedHex(50, 7, ROCK_HEX), new ExpectedHex(2, 30, ROCK_HEX),
						new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(2, 2, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(50, 30, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(20, 20, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void incorrectCritterName() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "incorrect_critter_name.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(2, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX), new ExpectedHex(5, 7, ROCK_HEX),
						new ExpectedHex(2, 3, ROCK_HEX), new ExpectedHex(6, 7, ROCK_HEX),
						new ExpectedHex(1, 20, ROCK_HEX), new ExpectedHex(50, 7, ROCK_HEX),
						new ExpectedHex(2, 30, ROCK_HEX), new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3, CritterHex.builderWithEnergy(500).build()),
						new ExpectedHex(3, 5, CritterHex.builderWithEnergy(500).build())));
	}

	@Test
	public void incorrectCritterMemsize() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "incorrect_critter_memsize.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(2, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX), new ExpectedHex(5, 7, ROCK_HEX),
						new ExpectedHex(2, 3, ROCK_HEX), new ExpectedHex(6, 7, ROCK_HEX),
						new ExpectedHex(1, 20, ROCK_HEX), new ExpectedHex(50, 7, ROCK_HEX),
						new ExpectedHex(2, 30, ROCK_HEX), new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3,
								new DeterministicHexInformation.CritterHex("wrong critter", 7, 1, 1, 1, 500, 0,
										List.of())),
						new ExpectedHex(3, 5, new DeterministicHexInformation.CritterHex("wrong critter", 7, 1, 1, 1,
								500, 0, List.of()))));
	}

	@Test
	public void incorrectCritterPosture() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "incorrect_critter_posture.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(2, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX), new ExpectedHex(5, 7, ROCK_HEX),
						new ExpectedHex(2, 3, ROCK_HEX), new ExpectedHex(6, 7, ROCK_HEX),
						new ExpectedHex(1, 20, ROCK_HEX), new ExpectedHex(50, 7, ROCK_HEX),
						new ExpectedHex(2, 30, ROCK_HEX), new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3,
								new DeterministicHexInformation.CritterHex("wrong critter", 7, 1, 1, 1, 500, 10,
										List.of())),
						new ExpectedHex(3, 5,
								new DeterministicHexInformation.CritterHex("wrong critter", 7, 1, 1, 1, 500, 10,
										List.of()))));
	}

	@Test
	public void negativeCritterDefense() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "negative_critter_defense.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(2, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX), new ExpectedHex(5, 7, ROCK_HEX),
						new ExpectedHex(2, 3, ROCK_HEX), new ExpectedHex(6, 7, ROCK_HEX),
						new ExpectedHex(1, 20, ROCK_HEX), new ExpectedHex(50, 7, ROCK_HEX),
						new ExpectedHex(2, 30, ROCK_HEX), new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3,
								new DeterministicHexInformation.CritterHex("wrong critter", 7, 10, 1, 1, 500, 0,
										List.of())),
						new ExpectedHex(3, 5,
								new DeterministicHexInformation.CritterHex("wrong critter", 7, 10, 1, 1, 500, 0,
										List.of()))));
	}

	@Test
	public void negativeCritterEnergy() {
		// 200x200 world
		runTest(Paths.get(COMMON_TEST_PATH, "negative_critter_energy.txt").toString(),
				// Step 0: the state of the world after load
				new StepState(2, "Big World With Critters", 200, 200, 8, 6, new ExpectedHex(0, 0, EMPTY_HEX),
						new ExpectedHex(1, 2, ROCK_HEX), new ExpectedHex(5, 7, ROCK_HEX),
						new ExpectedHex(2, 3, ROCK_HEX), new ExpectedHex(6, 7, ROCK_HEX),
						new ExpectedHex(1, 20, ROCK_HEX), new ExpectedHex(50, 7, ROCK_HEX),
						new ExpectedHex(2, 30, ROCK_HEX), new ExpectedHex(60, 7, ROCK_HEX),
						new ExpectedHex(3, 4, new DeterministicHexInformation.NonCritterHex(-101)),
						new ExpectedHex(4, 3, new DeterministicHexInformation.NonCritterHex(-201)),
						new ExpectedHex(9, 9, new DeterministicHexInformation.NonCritterHex(-301)),
						new ExpectedHex(3, 40, new DeterministicHexInformation.NonCritterHex(-1001)),
						new ExpectedHex(4, 30, new DeterministicHexInformation.NonCritterHex(-2001)),
						new ExpectedHex(9, 90, new DeterministicHexInformation.NonCritterHex(-3001)),
						new ExpectedHex(5, 3,
								new DeterministicHexInformation.CritterHex("wrong critter", 7, 1, 1, 1, 10, 0,
										List.of())),
						new ExpectedHex(3, 5,
								new DeterministicHexInformation.CritterHex("wrong critter", 7, 1, 1, 1, 10, 0,
										List.of()))));
	}

	private void runTest(String worldFile, StepState initialState, StepState... steps) {
        final var controller = ControllerFactory.getConsoleController();
        assertTrue(controller.loadWorld(worldFile, false, false), String.format("World file %s failed to load.", worldFile));
		ReadOnlyWorld readOnlyWorld = controller.getReadOnlyWorld();
		World world = (World) readOnlyWorld;
		checkState(world, 0, initialState);
        int stepId = 0;
        while (stepId < steps.length) {
            final var stepState = steps[stepId];
            controller.advanceTime(1);
            stepId++;
            checkState(controller.getReadOnlyWorld(), stepId, stepState);
        }
    }

    private static void checkState(ReadOnlyWorld world, int stepId, StepState stepState) {
		World trueWorld = (World) world;
		assertEquals(trueWorld.getName(), stepState.name);
		assertEquals(trueWorld.getDimensions()[0], stepState.x);
		assertEquals(trueWorld.getDimensions()[1], stepState.y);
		assertEquals(trueWorld.getNumRocks(), stepState.numRocks);
		assertEquals(trueWorld.getNumFood(), stepState.numFood);
        assertEquals(stepId, world.getSteps(), "Step counter disagrees in step " + stepId);
        assertEquals(stepState.population, world.getNumberOfAliveCritters(), "Critter population disagrees in step " + stepId);
        for (final var expectedHex : stepState.expectedHexList) {
            final var actualHexInformation =
                    DeterministicHexInformation.fromWorldLocation(world, expectedHex.column, expectedHex.row);
            assertEquals(
                    expectedHex.information,
                    actualHexInformation,
                    "Hex information disagrees at (" + expectedHex.column + ", " + expectedHex.row + ") in step " + stepId);
        }
    }

    private static final class StepState {
        public final int population;
        public final List<ExpectedHex> expectedHexList;
		public final String name;
		public final int x;
		public final int y;
		public final int numRocks;
		public final int numFood;

		public StepState(int population, String name, int x, int y, int numRocks, int numFood,
				ExpectedHex... expectedHexList) {
            this.population = population;
            this.expectedHexList = Arrays.asList(expectedHexList);
			this.name = name;
			this.x = x;
			this.y = y;
			this.numRocks = numRocks;
			this.numFood = numFood;

        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StepState stepState = (StepState) o;
            return population == stepState.population &&
                    expectedHexList.equals(stepState.expectedHexList);
        }

        @Override
        public int hashCode() {
            return Objects.hash(population, expectedHexList);
        }

        @Override
        public String toString() {
            return String.format("StepState{population=%d, expectedHexList=%s}", population, expectedHexList);
        }
    }

    private static final class ExpectedHex {
        public final int column;
        public final int row;
        public final DeterministicHexInformation information;

        public ExpectedHex(int column, int row, DeterministicHexInformation information) {
            this.column = column;
            this.row = row;
            this.information = information;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ExpectedHex that = (ExpectedHex) o;
            return column == that.column &&
                    row == that.row &&
                    information.equals(that.information);
        }

        @Override
        public int hashCode() {
            return Objects.hash(column, row, information);
        }

        @Override
        public String toString() {
            return String.format("ExpectedHex{column=%d, row=%d, information=%s}", column, row, information);
        }
    }
}
