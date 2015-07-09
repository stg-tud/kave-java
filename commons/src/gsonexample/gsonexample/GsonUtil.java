package gsonexample;

import gsonexample.model.BagOfPrimitives;
import gsonexample.model.ExtendedBag;
import gsonexample.model.IBucket;
import gsonexample.model.Intbucket;
import gsonexample.model.Stringbucket;
import gsonexample.serializer.IntbucketSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;

import cc.kave.commons.model.ssts.IExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;

public class GsonUtil {

	public static <T> T fromFile(File file, Class<T> clazz) throws IOException {
		FileReader reader = new FileReader(file);
		BufferedReader bufReader = new BufferedReader(reader);
		String gsonString = bufReader.readLine();

		return fromString(gsonString, clazz);
	}

	public static <T> T fromString(String str, Class<T> clazz) {
		Gson gson = createGson();
		T bag = gson.fromJson(str, clazz);
		return bag;
	}

	public static <T> String toString(T object) {
		Gson gson = createGson();
		return gson.toJson(object);
	}

	private static Gson createGson() {
		RuntimeTypeAdapterFactory<BagOfPrimitives> bag = RuntimeTypeAdapterFactory.of(BagOfPrimitives.class, "$type")
				.registerSubtype(BagOfPrimitives.class, BagOfPrimitives.class.getName());
		RuntimeTypeAdapterFactory<IBucket> bucketFactory = RuntimeTypeAdapterFactory.of(IBucket.class, "$type")
				.registerSubtype(Intbucket.class, Intbucket.class.getName())
				.registerSubtype(Stringbucket.class, Stringbucket.class.getName());
		RuntimeTypeAdapterFactory<ExtendedBag> extendedBagFactory = RuntimeTypeAdapterFactory.of(ExtendedBag.class,
				"$type").registerSubtype(ExtendedBag.class, ExtendedBag.class.getName());

		RuntimeTypeAdapterFactory<SST> sstFactory = factoryFor(SST.class, SST.class);
		RuntimeTypeAdapterFactory<?> expFactory = factoryFor(IExpression.class, ComposedExpression.class,
				CompletionExpression.class, IfElseExpression.class);

		Gson gson = new GsonBuilder().registerTypeAdapterFactory(bag).registerTypeAdapterFactory(bucketFactory)
				.registerTypeAdapterFactory(extendedBagFactory).registerTypeAdapterFactory(sstFactory).create();
		return gson;
	}

	@SafeVarargs
	private static <T> RuntimeTypeAdapterFactory<T> factoryFor(Class<T> baseclass, Class<? extends T>... subclasses) {
		RuntimeTypeAdapterFactory<T> factory = RuntimeTypeAdapterFactory.of(baseclass, "$type");
		for (Class<? extends T> subclass : subclasses) {
			String typename = subclass.getName();
			factory.registerSubtype(subclass, "[" + abbreviateNamespace(typename) + "]");
		}
		return factory;
	}

	private static String abbreviateNamespace(String typename) {
		return typename.replaceAll("cc\\.kave\\.commons\\.model\\.ssts\\.impl\\.", "SST:");
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
