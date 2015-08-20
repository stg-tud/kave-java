package cc.kave.commons.utils.zip;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import cc.kave.commons.utils.json.JsonUtils;

public class ZipWriter {
	private String path;
	private List<String> entries;

	public ZipWriter(String path) {
		// assertTrue(new File(path).exists() && new File(path).isDirectory());
		this.path = path;
		this.entries = new ArrayList<>();
	}

	public <T> void add(T obj) {
		entries.add(JsonUtils.toJson(obj, obj.getClass()));
	}

	public <T> void addAll(List<T> entries) {
		for (T obj : entries) {
			String json = JsonUtils.toJson(obj, obj.getClass());
			this.entries.add(json);
		}
	}

	public void dispose() {
		if (entries.isEmpty())
			return;
		int num = 0;
		try {
			File f = new File(path.substring(0, path.lastIndexOf("/") + 1));
			if (!f.exists())
				f.mkdirs();
			FileOutputStream fos = new FileOutputStream(path, false);
			ZipOutputStream zos = new ZipOutputStream(fos);
			for (String s : entries) {
				String fileName = num + ".json";
				num++;
				ZipEntry entry = new ZipEntry(fileName);
				zos.putNextEntry(entry);
				zos.write(s.getBytes(), 0, s.length());
				zos.closeEntry();
			}
			zos.close();
			fos.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		entries.clear();
	}
}