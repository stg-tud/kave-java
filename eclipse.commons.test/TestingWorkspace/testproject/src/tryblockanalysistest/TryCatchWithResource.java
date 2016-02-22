package tryblockanalysistest;

import java.io.FileInputStream;

public class TryCatchWithResource {

	public void method() {
		try (FileInputStream f = new FileInputStream("")) {
		} catch (Exception e) {
		}
	}
}
