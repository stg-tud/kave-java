package invocationexpressionanalysistest;

public class InvocationOnInvocationChain {

	public void method() {
		toString().toString().length();
	}
}
