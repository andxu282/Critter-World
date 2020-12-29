package model;

public interface ReadOnlyCritter {
    /** @return critter species. */
    String getSpecies();

    /**
	 * Hint: you should consider making a defensive copy of the array.
	 *
	 * @return an array representation of critter's memory.
	 */
    int[] getMemory();

    /** @return current program string of the critter. */
    String getProgramString();

    /**
     * @return last rule executed by the critter on its previous turn, or {@code null} if it has not
     *     executed any.
     */
    String getLastRuleString();
}
