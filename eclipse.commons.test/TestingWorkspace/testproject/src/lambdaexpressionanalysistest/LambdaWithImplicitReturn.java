package lambdaexpressionanalysistest;

import java.util.function.Function;

public class LambdaWithImplicitReturn {

	public void method() {
		Function<Integer, Integer> func = (Integer i) -> i;
	}
}
