package console;

import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import cms.util.maybe.Maybe;
import controller.Controller;
import controller.ControllerFactory;
import model.ReadOnlyCritter;
import model.ReadOnlyWorld;

/**
 * The provided shows you how the course staff may call the methods in Controller to test your code.
 *
 * <p>You don't need to modify anything in this file. You can choose to make it prettier, but you
 * still need to support the same set of commands and print out the same set of information.
 *
 * <p>NEVER remove this file, since it will be your entry point of the JAR file.
 */
public final class Console {
    private final Controller controller = ControllerFactory.getConsoleController();
    private final Scanner scan = new Scanner(System.in);

    private Console() {}

    /* =========================== */
    /* DO NOT EDIT ABOVE THIS LINE */
    /* (except imports...) */
    /* =========================== */

    /** Prints current time step, number of critters, and world map of the simulation. */
    private void worldInfo() {
        ReadOnlyWorld world = controller.getReadOnlyWorld();
        System.out.println("steps: " + world.getSteps());
        System.out.println("critters: " + world.getNumberOfAliveCritters());
        controller.printWorld(System.out);
    }

    /**
     * Prints description of the contents of hex (c,r).
     *
     * @param c column of hex
     * @param r row of hex
     */
    private void hexInfo(int c, int r) {
        ReadOnlyWorld world = controller.getReadOnlyWorld();
        Maybe<ReadOnlyCritter> maybeCritter = world.getReadOnlyCritter(c, r);
        maybeCritter.thenElse(critter -> {
            System.out.println("Species: " + critter.getSpecies());
            String memory =
                    Arrays.stream(critter.getMemory())
                            .mapToObj(String::valueOf)
                            .collect(Collectors.joining(" "));
			System.out.println("Memory: " + memory);
            System.out.println("Program: " + critter.getProgramString());
            System.out.println("Last rule: " + critter.getLastRuleString());
        }, () -> {
            int terrain = world.getTerrainInfo(c, r);
            if (terrain == 0) {
                System.out.println("Empty");
            } else if (terrain == -1) {
                System.out.println("Rock");
            } else {
                System.out.println("Food: " + (-terrain - 1));
            }
        });
    }

    /* =========================== */
    /* DO NOT EDIT BELOW THIS LINE */
    /* =========================== */

    /** Prints a list of possible commands to the standard output. */
    private void printHelp() {
        System.out.println("new: start a new simulation with a random world");
        System.out.println(
                "load <world_file>: start a new simulation with the world loaded from world_file");
        System.out.println(
                "critters <critter_file> <n>: add n critters defined by critter_file randomly into the world");
        System.out.println("step <n>: advance the world by n timesteps");
        System.out.println(
                "info: print current timestep, number of critters living, and map of world");
        System.out.println("hex <c> <r>: print contents of hex at column c, row r");
        System.out.println("exit: exit the program");
    }

    /**
     * Processes a single console command provided by the user.
     *
     * @return whether we should continue handling commands.
     */
    private boolean handleCommand() {
        System.out.print("Enter a command or \"help\" for a list of commands.\n> ");
        String command = scan.next();
        switch (command) {
            case "new":
                controller.newWorld();
                break;
            case "load":
                {
                    String filename = scan.next();
					controller.loadWorld(filename,
							true, false);
                    break;
                }
            case "critters":
                {
                    String filename = scan.next();
                    int n = scan.nextInt();
					controller.loadCritters(filename, n);
                    break;
                }
            case "step":
                {
                    int n = scan.nextInt();
                    controller.advanceTime(n);
                    break;
                }
            case "info":
                worldInfo();
                break;
            case "hex":
                {
                    int c = scan.nextInt();
                    int r = scan.nextInt();
                    hexInfo(c, r);
                    break;
                }
            case "help":
                printHelp();
                break;
            case "exit":
                return false;
            default:
                System.out.println(command + " is not a valid command.");
        }
        return true;
    }

    public static void main(String[] args) {
        Console console = new Console();
        while (console.handleCommand()) ;
    }
}
