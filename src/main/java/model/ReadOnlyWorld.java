package model;

import cms.util.maybe.Maybe;

public interface ReadOnlyWorld {
    /** @return number of steps */
    int getSteps();

    /** @return number of alive critters. */
    int getNumberOfAliveCritters();

    /**
     * @param c column id.
     * @param r row id.
     * @return the critter at the specified hex.
     */
    Maybe<ReadOnlyCritter> getReadOnlyCritter(int c, int r);

    /**
     * @param c column id.
     * @param r row id.
     * @return 0 is empty, -1 is rock, -X is (X-1) food. Treat out-of-bound hex as rock.
     * @throws IllegalArgumentException if the hex is occupied by a critter.
     */
    int getTerrainInfo(int c, int r);
}
