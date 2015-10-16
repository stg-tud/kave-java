package cc.kave.commons.reader;

import static cc.recommenders.assertions.Asserts.assertFalse;
import static cc.recommenders.assertions.Asserts.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.FileUtils;

public class FileReader {

	public List<String> readFile(File file) {
		assertTrue(file.exists(), "File does not exist");
		assertFalse(file.isDirectory(), "File is not a file, but a directory");

		List<String> lines = new LinkedList<String>();

		try {
			lines = FileUtils.readLines(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return lines;
	}
}
