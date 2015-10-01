package cc.kave.commons.utils.zip;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;

public class ZipFolder {

	private String path;
	private String root;
	private String outputRoot;
	private int fileCounter;
	private int count;

	public ZipFolder(String root, String path, String outputRoot) {
		this.path = path;
		this.root = root;
		this.outputRoot = outputRoot;
		this.fileCounter = 0;
		this.count = 0;
	}

	public String getName() {
		return path;
	}

	public String getRoot() {
		return root;
	}

	public ZipWriter createNewArchive() {
		String fileName = createNextUnusedFileName();
		return new ZipWriter(fileName);
	}

	public List<ZipWriter> createNewArchives() {
		List<ZipWriter> writer = new ArrayList<>();
		for (int i = 0; i < count; i++) {
			writer.add(createNewArchive());
		}
		return writer;
	}

	private String createNextUnusedFileName() {
		String fileName = createNextFileName();
		while (new File(fileName).exists()) {
			fileName = createNextFileName();
		}
		return fileName;
	}

	private String createNextFileName() {
		String fileName = outputRoot + path + "/" + fileCounter + ".zip";
		fileCounter++;
		return fileName;
	}

	public List<ZipReader> openAll() {
		File[] files = new File(root + path).listFiles(new FilenameFilter() {

			@Override
			public boolean accept(File dir, String name) {
				return name.contains(".zip");
			}

		});
		List<ZipReader> readers = new ArrayList<>();
		for (File f : files) {
			readers.add(open(f.getAbsolutePath()));
		}
		this.count = readers.size();
		return readers;
	}

	private ZipReader open(String absolutePath) {
		if (absolutePath.contains("\\")) {
			absolutePath = absolutePath.replace("\\", "/");
		}
		return new ZipReader(absolutePath);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + fileCounter;
		result = prime * result + ((path == null) ? 0 : path.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ZipFolder other = (ZipFolder) obj;
		if (fileCounter != other.fileCounter)
			return false;
		if (path == null) {
			if (other.path != null)
				return false;
		} else if (!path.equals(other.path))
			return false;
		return true;
	}

}
