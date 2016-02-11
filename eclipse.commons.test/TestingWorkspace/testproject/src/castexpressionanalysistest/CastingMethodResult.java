package castexpressionanalysistest;

public class CastingMethodResult {

	public void method() {
		long i = (int) z();
	}

	public int z() {
		return 3;
	}
}
