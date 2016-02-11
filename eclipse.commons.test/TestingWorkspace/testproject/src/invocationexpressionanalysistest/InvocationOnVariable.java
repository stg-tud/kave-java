package invocationexpressionanalysistest;

public class InvocationOnVariable {

	public void method() {
		Object o = new Object();
		o.hashCode();
	}
}
