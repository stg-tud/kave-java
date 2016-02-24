package invocationexpressionanalysistest;

public class BasicLambdaExpression {

	public void method(){
		Runnable r2 = () -> System.out.println("Hello world two!");
	}
}
