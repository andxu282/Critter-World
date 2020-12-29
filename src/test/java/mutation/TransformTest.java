package mutation;

import java.io.FileNotFoundException;

import ast.Transform;
import cms.util.maybe.NoMaybeValue;

public class TransformTest extends GeneralMutationTest {
	public static void main(String[] args) throws FileNotFoundException, NoMaybeValue {
		Transform t = new Transform();
		for(int i = 0; i <100; i++) {
			test(i ,t);
		}
	}
}
