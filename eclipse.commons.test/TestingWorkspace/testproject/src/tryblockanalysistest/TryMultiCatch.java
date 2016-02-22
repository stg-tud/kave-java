package tryblockanalysistest;

import java.io.FileInputStream;
import java.io.IOException;

public class TryMultiCatch {

	public void method() {
		try {
			FileInputStream f = new FileInputStream("");
		} catch (RuntimeException | IOException e) {
		}
	}
}
