package lambdaexpressionanalysistest;

import java.util.function.Function;

public class LambdaWithDeclaration {

	public void method() {
		Function<Integer, Integer> func = (Integer i) -> hashCode();
	}
}
