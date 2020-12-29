package mutation;

import java.io.FileNotFoundException;

import ast.Insert;
import cms.util.maybe.NoMaybeValue;

public class InsertTest extends GeneralMutationTest {
	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue {
		Insert insert = new Insert();
		for(int i = 0; i<30; i++) {
			test(i, insert);
		}
	}
}
