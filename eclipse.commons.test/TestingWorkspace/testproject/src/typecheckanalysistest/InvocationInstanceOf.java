package typecheckanalysistest;

public class InvocationInstanceOf {

	public void method() {
		Object o = null;
		if (getObject() instanceof Object) {
		}
	}

	public Object getObject() {
		return new Object();
	}
}
