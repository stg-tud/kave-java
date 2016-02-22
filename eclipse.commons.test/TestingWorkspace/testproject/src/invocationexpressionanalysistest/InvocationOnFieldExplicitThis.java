package invocationexpressionanalysistest;

public class InvocationOnFieldExplicitThis {

	private String s;

	public void method() {
		this.s.length();
	}
}
