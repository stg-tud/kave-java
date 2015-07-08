package flexjsonexample;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import flexjson.JSONSerializer;

public class FlexjsonUtil {

	public static <T> String toString(T object) {
		JSONSerializer jsonserializer = new JSONSerializer();
		return jsonserializer.serialize(object);
	}

	public static <T> void toFile(File file, T object) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(toString(object));
		writer.flush();
		writer.close();
	}
}
