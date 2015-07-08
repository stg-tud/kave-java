package gsonexample;

import gsonexample.model.Intbucket;
import gsonexample.serializer.IntbucketSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonUtil {

	public static <T> T fromFile(File file, Class<T> clazz) throws IOException {
		FileReader reader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(reader);
		String gsonString = bufReader.readLine();

		return fromString(gsonString, clazz);
	}

	public static <T> T fromString(String str, Class<T> clazz) {
		Gson gson = new GsonBuilder().create();
		T bag = gson.fromJson(str, clazz);
		return bag;
	}

	public static <T> String toString(T object) {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(object);
	}

	public static <T> String toStringSerializertest(T object) {
		GsonBuilder gsonBuilder = new GsonBuilder().registerTypeAdapter(Intbucket.class, new IntbucketSerializer());
		Gson gson = gsonBuilder.create();
		return gson.toJson(object);
	}

	public static <T> String toStringWithType(T object, Type type) {
		Gson gson = new GsonBuilder().create();
		return gson.toJson(object, type);
	}

	public static <T> void toFile(File file, T object) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(toString(object));
		writer.flush();
		writer.close();
	}

	public static <T> void toFileWithType(File file, T object, Type type) throws IOException {
		FileWriter writer = new FileWriter(file);
		writer.write(toStringWithType(object, type));
		writer.flush();
		writer.close();
	}

}
