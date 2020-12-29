package mutation;

import java.io.FileNotFoundException;

import ast.Duplicate;
import cms.util.maybe.NoMaybeValue;

public class DuplicateTest extends GeneralMutationTest {
	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue {
		Duplicate duplicate = new Duplicate();
		for(int i = 0; i<30; i++) {
			test(i, duplicate);
		}
	}
}
