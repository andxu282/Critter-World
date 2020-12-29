package controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import model.ReadOnlyWorld;

/**
 * Sealed class that records the hex information that is guaranteed to be deterministic.
 */
public abstract class DeterministicHexInformation {
    private DeterministicHexInformation() {}

    public static final class NonCritterHex extends DeterministicHexInformation {
        public static final DeterministicHexInformation ROCK_HEX = new NonCritterHex(-1);
        public static final DeterministicHexInformation EMPTY_HEX = new NonCritterHex(0);

        public final int hexValue;

        public NonCritterHex(int hexValue) {
            this.hexValue = hexValue;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            NonCritterHex that = (NonCritterHex) o;
            return hexValue == that.hexValue;
        }

        @Override
        public int hashCode() {
            return Integer.hashCode(hexValue);
        }

        @Override
        public String toString() {
            return "NonCritterHex{ hexValue=" + hexValue + " }";
        }
    }

    public static final class CritterHex extends DeterministicHexInformation {
		public final String species;
        public final int memorySize;
        public final int defense;
        public final int offense;
        public final int size;
        public final int energy;
        public final int posture;
        public final List<Integer> otherMemorySlots;

		public CritterHex(String species,
                int memorySize,
                int defense,
                int offense,
                int size,
                int energy,
                int posture,
                List<Integer> otherMemorySlots) {
			this.species = species;
            this.memorySize = memorySize;
            this.defense = defense;
            this.offense = offense;
            this.size = size;
            this.energy = energy;
            this.posture = posture;
            this.otherMemorySlots = otherMemorySlots;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            CritterHex that = (CritterHex) o;
            return memorySize == that.memorySize &&
                    defense == that.defense &&
                    offense == that.offense &&
                    size == that.size &&
                    energy == that.energy &&
                    posture == that.posture &&
                    otherMemorySlots.equals(that.otherMemorySlots);
        }

        @Override
        public int hashCode() {
            return Objects.hash(memorySize, defense, offense, size, energy, posture, otherMemorySlots);
        }

        @Override
        public String toString() {
            return String.format(
                    "CritterHex{memorySize=%d, defense=%d, offense=%d, size=%d, energy=%d, posture=%d, otherMemorySlots=%s}",
                    memorySize,
                    defense,
                    offense,
                    size,
                    energy,
                    posture,
                    otherMemorySlots);
        }

        static Builder builderWithEnergy(int energy) {
            return new Builder(energy);
        }

        /**
         * Use this builder so that we don't have to supply some default values every time.
         */
        public static final class Builder {
			private String species = "space critter";
            private int memorySize = 7;
            private int defense = 1;
            private int offense = 1;
            private int size = 1;
            private final int energy;
            private int posture = 0;
            private List<Integer> otherMemorySlots = List.of();

            private Builder(int energy) {
                this.energy = energy;
            }

			public Builder setSpecies(String species) {
				this.species = species;
				return this;
			}

            public Builder setMemorySize(int memorySize) {
                this.memorySize = memorySize;
                return this;
            }

            public Builder setDefense(int defense) {
                this.defense = defense;
                return this;
            }

            public Builder setOffense(int offense) {
                this.offense = offense;
                return this;
            }

            public Builder setSize(int size) {
                this.size = size;
                return this;
            }

            public Builder setPosture(int posture) {
                this.posture = posture;
                return this;
            }

            public Builder setOtherMemorySlots(List<Integer> otherMemorySlots) {
                this.otherMemorySlots = otherMemorySlots;
                return this;
            }

            public CritterHex build() {
				return new CritterHex(species, memorySize, defense, offense, size, energy, posture, otherMemorySlots);
            }
        }
    }

    public static DeterministicHexInformation fromWorldLocation(ReadOnlyWorld world, int column, int row) {
        return world.getReadOnlyCritter(column, row).<DeterministicHexInformation>then(critter -> {
            final var memory = critter.getMemory();
            List<Integer> otherMemorySlots = new ArrayList<>();
            for (int i = 7; i < memory.length; i++) {
                otherMemorySlots.add(memory[i]);
            }
			return new CritterHex(critter.getSpecies(), memory[0], memory[1], memory[2], memory[3], memory[4],
					memory[6], otherMemorySlots);
        }).orElseGet(() -> new NonCritterHex(world.getTerrainInfo(column, row)));
    }

    public static DeterministicHexInformation foodHex(int amount) {
        return new NonCritterHex(-1 - amount);
    }
}
