package mutation;

import java.io.FileNotFoundException;

import ast.Replace;
import cms.util.maybe.NoMaybeValue;

public class ReplaceTest extends GeneralMutationTest {
	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue {
		Replace rep = new Replace();
		for(int i = 0; i<20; i++) {
			test(i, rep);
		}
	}

}
