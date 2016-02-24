package lambdaexpressionanalysistest;

import java.util.function.Function;

public class LambdaWithReturnAndBlock {

	public void method() {
		Function<Integer, Integer> func = (Integer i) -> {
			hashCode();
			return i;
		};
	}
}
