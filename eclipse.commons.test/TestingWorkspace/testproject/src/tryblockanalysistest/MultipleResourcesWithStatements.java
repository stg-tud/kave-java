package tryblockanalysistest;

import java.io.FileInputStream;

public class MultipleResourcesWithStatements {

	public void method() {
		try (FileInputStream f = new FileInputStream(""); FileInputStream g = new FileInputStream("")) {
			int i = 3;
		} catch (Exception e) {
		}
	}
}
