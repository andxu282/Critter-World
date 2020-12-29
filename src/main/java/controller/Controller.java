package controller;

import java.io.PrintStream;
import model.ReadOnlyWorld;

/**
 * Controller interface that allows the course staff to test your critter world implementation.
 *
 * <p>NEVER remove or change any methods in this file except reformatting. Feel free to add
 * additional methods in this file. It might be helpful in later assignments.
 *
 * <p>You have to provide one implementation of {@code ConsoleController} to {@code
 * ControllerFactory}.
 */
public interface Controller {
    /** @return the readonly world. */
    ReadOnlyWorld getReadOnlyWorld();

    /** Starts new random world simulation. */
    void newWorld();

    /**
     * Starts new simulation with world specified in filename.
     *
     * @param filename name of the world file.
     * @param enableManna if enableManna is false, then the world should not drop any manna.
     * This is important for deterministic unit testing.
     * @param enableForcedMutation if enableForcedMutation is true, then a critter's program
     * will mutate every time it finishes its action.
     * @return whether the world is successfully loaded.
     */
    boolean loadWorld(String filename, boolean enableManna, boolean enableForcedMutation);

    /**
     * Loads critter definition from filename and randomly places n critters with that definition
     * into the world.
     *
     * @param filename name of the critter spec file.
     * @param n number of critter to add.
     * @return whether all critters are successfully loaded.
     */
    boolean loadCritters(String filename, int n);

    /**
     * Advances the world by n time steps.
     *
     * @param n number of steps.
     * @return false if the world has not been initialized or n is negative, true otherwise.
     */
    boolean advanceTime(int n);

    /**
     * Print the world to the specified stream.
     *
     * @param out the stream to print the world.
     */
    void printWorld(PrintStream out);
}
