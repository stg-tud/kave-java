package loopheaderexpressionanalysistest;

public class MethodInvocation {

	public void method() {
		while (isX()) {
		}
	}

	public boolean isX() {
		return true;
	}
}
