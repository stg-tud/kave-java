package indexaccessexpressionanalysistest;

public class IndexAccessOnMethodResult {

	public void method() {
		int i = getArray()[1];
	}

	public int[] getArray() {
		return new int[] { 0, 1, 2 };
	}
}
