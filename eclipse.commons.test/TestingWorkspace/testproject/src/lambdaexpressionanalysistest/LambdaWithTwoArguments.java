package lambdaexpressionanalysistest;

import java.util.function.BiFunction;

public class LambdaWithTwoArguments {

	public void method() {
		BiFunction<Integer, Boolean, Integer> func = (Integer i, Boolean b) -> i;
	}
}
