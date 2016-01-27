package invocationexpressionanalysistest;

public class InvocationOnVariable {

	public void method() {
		Object o = new Object().hashCode();
		o.hashCode();
	}
}
