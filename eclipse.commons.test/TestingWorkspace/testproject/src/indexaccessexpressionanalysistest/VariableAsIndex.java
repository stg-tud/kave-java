package indexaccessexpressionanalysistest;

public class VariableAsIndex {

	int[] i = { 1, 2, 3 };

	public void method() {
		int k = 1;
		int j = i[k];
	}

}
