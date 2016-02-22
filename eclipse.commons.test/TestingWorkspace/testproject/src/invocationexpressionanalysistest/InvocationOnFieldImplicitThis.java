package invocationexpressionanalysistest;

public class InvocationOnFieldImplicitThis {

	private String s;

	public void method() {
		s.length();
	}
}
