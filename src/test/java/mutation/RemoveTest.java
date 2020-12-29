package mutation;

import java.io.FileNotFoundException;

import ast.Remove;
import cms.util.maybe.NoMaybeValue;

public class RemoveTest extends GeneralMutationTest {
	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue {
		Remove rem = new Remove();
		for(int i = 0; i<15; i++) {
		test(i, rem);
		}
	}
	
}
