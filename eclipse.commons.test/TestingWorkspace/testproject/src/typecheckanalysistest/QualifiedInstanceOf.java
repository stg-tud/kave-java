package typecheckanalysistest;

public class QualifiedInstanceOf {

	public void method() {
		Object o = null;
		if (o instanceof java.lang.Object) {
		}
	}
}
