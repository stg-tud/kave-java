package tryblockanalysistest;

import java.io.FileInputStream;
import java.io.InputStream;

public class TryCatchWithMultipleResources {

	public void method() {
		try (FileInputStream f = new FileInputStream(""); FileInputStream g = new FileInputStream("")) {
		} catch (Exception e) {
		}
	}
}
