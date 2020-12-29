package mutation;

import java.io.FileNotFoundException;

import ast.Swap;
import cms.util.maybe.NoMaybeValue;

public class SwapTest extends GeneralMutationTest {
	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue {
		Swap s = new Swap();
		for(int i = 0; i<15; i++) {
			test(i, s);
		}
	}



}
