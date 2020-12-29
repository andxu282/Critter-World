package controller;

/** DO NOT REMOVE ANY METHODS IN THIS CLASS. Course staff needs this to test your world. */
public final class ControllerFactory {

    /**
     * Private constructor can prevent any {@code new ControllerFactory()} since they do not make
     * sense.
     */
    private ControllerFactory() {}

    /**
     * @return a controller for console to allow course staff to test your code. The returned
     *     controller contains a randomly initialized world.
     */
    public static Controller getConsoleController() {
		return new ConsoleController();
    }
}
