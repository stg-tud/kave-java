package cc.kave.commons.utils.zip;

import java.io.File;
import java.util.LinkedHashSet;
import java.util.Set;

public class ZipUtils {

	public static Set<ZipFolder> getAllZips(String path, String inputPath, String outputPath) {
		File directory = new File(path);
		Set<ZipFolder> files = new LinkedHashSet<>();
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile() && file.getName().contains(".zip")) {
				files.add(new ZipFolder(inputPath, directory.getAbsolutePath().replace(inputPath, ""), outputPath));
			} else if (file.isDirectory()) {
				files.addAll(getAllZips(file.getAbsolutePath(), inputPath, outputPath));
			}
		}
		return files;
	}
}
