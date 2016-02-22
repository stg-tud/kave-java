package tryblockanalysistest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class OnlyTryBlock {

	public void method() throws FileNotFoundException, IOException {
		try (FileInputStream f = new FileInputStream("")) {
		}
	}
}
