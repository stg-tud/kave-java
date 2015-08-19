package cc.kave.commons.utils.zip;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import cc.kave.commons.utils.json.JsonUtils;

public class ZipReader {

	private int count;
	private List<String> entries;
	private Iterator<String> files;

	public ZipReader(String path) {
		this.count = 0;
		// assertTrue(new File(path).exists());
		List<String> files = new ArrayList<>();
		try {
			ZipFile zipFile = new ZipFile(new File(path));
			Enumeration<? extends ZipEntry> en = zipFile.entries();
			while (en.hasMoreElements()) {
				ZipEntry entry = en.nextElement();
				if (!entry.isDirectory()) {
					count++;
					StringBuilder sb = new StringBuilder();
					BufferedReader reader = new BufferedReader(new InputStreamReader(zipFile.getInputStream(entry)));
					String line;
					while ((line = reader.readLine()) != null) {
						sb.append(line);
					}
					files.add(sb.toString());
				}
			}
			zipFile.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.entries = files;
		this.files = entries.iterator();
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public boolean hasNext() {
		return files.hasNext();
	}

	public <T> T getNext(Type targetType) {
		return JsonUtils.parseJson(files.next(), targetType);
	}

	public <T> List<T> getAll(Class<T> targetType) {
		List<T> all = new ArrayList<>();
		while (hasNext()) {
			all.add(getNext(targetType));
		}
		return all;
	}

}
