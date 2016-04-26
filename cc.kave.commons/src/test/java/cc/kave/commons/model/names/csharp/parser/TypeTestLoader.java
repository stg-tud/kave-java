package cc.kave.commons.model.names.csharp.parser;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.google.common.collect.Lists;

public class TypeTestLoader {

	private static final Path TESTDIR = Paths.get(System.getProperty("user.dir"), "src", "test", "java", "cc", "kave",
			"commons", "model", "names", "csharp", "parser", "data");
	private static final Path VALID_TYPENAMES = TESTDIR.resolve("valid-typenames.txt");
	private static final Path INVALID_TYPENAMES = TESTDIR.resolve("invalid-typenames.txt");

	private static List<String> loadTests(Path dir) {
		List<String> testFile = Lists.newArrayList();
		try {
			testFile = Lists.newArrayList(FileUtils.readFileToString(dir.toFile()).split("\n"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return testFile;
	}

	public static List<TypeTestCase> validTypeNames() {
		return loadTypeNameTestCases(VALID_TYPENAMES);
	}

	protected static List<TypeTestCase> loadTypeNameTestCases(Path dir) {
		List<TypeTestCase> testCases = Lists.newArrayList();
		List<String> lines = loadTests(dir);
		String[] header = lines.get(0).split("\t");
		lines = lines.subList(1, lines.size());
		for (String s : lines) {
			List<String> fields = Lists.newArrayList(s.split("\t"));
			if (fields.size() < header.length) {
				for (int i = 0; i < (header.length - fields.size() + 2); i++) {
					fields.add("");
				}
			}
			TypeTestCase t = new TypeTestCase(fields.get(0), fields.get(1), fields.get(2));
			testCases.add(t);
		}
		return testCases;
	}

	public static List<TypeTestCase> invalidTypeNames() {
		return loadTypeNameTestCases(INVALID_TYPENAMES);
	}

}
