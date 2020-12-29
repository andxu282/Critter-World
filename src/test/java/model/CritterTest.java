package model;

import java.util.Arrays;
import java.util.Random;

public class CritterTest {
	public static void main(String[] args) {
		critter1();
	}

	public static void critter1() {
		Random r = new Random();
		Critter critter1 = new Critter("E. Coli", 7, 10, 10, 1, 10, 3, r.nextInt(6), null);
		System.out.println(critter1.getSpecies());
		System.out.println(critter1.getDirection());
		System.out.println(critter1.foodAmount());
		System.out.println(Arrays.toString(critter1.getMemory()));
	}
}
