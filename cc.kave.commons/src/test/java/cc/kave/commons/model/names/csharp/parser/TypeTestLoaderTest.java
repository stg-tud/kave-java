package cc.kave.commons.model.names.csharp.parser;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TypeTestLoaderTest {

	private static final String expectedFirstCsv = "identifier\tnamespace\tassembly\nT,P,0.0.0.0\t\tP,0.0.0.0";

	private Path dir;
	private Path firstTestCsv;

	@Before
	public void buildTestDirectory() throws IOException {
		dir = Files.createTempDirectory(null);
		firstTestCsv = dir.resolve("test.csv");
		Files.write(firstTestCsv, expectedFirstCsv.getBytes());
	}

	@After
	public void beleteTmpFolders() throws IOException {
		File baseDir = dir.toFile();
		if (baseDir.exists()) {
			FileUtils.deleteDirectory(baseDir);
		}
	}

	@Test
	public void loadTypeNameTestCases() {
		List<TypeTestCase> actual = TypeTestLoader.loadTypeNameTestCases(firstTestCsv);
		assertEquals(1, actual.size());
		TypeTestCase actualTest = actual.get(0);
		assertEquals("T,P,0.0.0.0", actualTest.getIdentifier());
		assertEquals("", actualTest.getNamespace());
		assertEquals("P,0.0.0.0", actualTest.getAssembly());
	}
}
