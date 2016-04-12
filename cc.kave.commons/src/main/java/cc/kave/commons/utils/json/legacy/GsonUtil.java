/**
 * Copyright (c) 2010 Darmstadt University of Technology.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Marcel Bruch - initial API and implementation.
 */
package cc.kave.commons.utils.json.legacy;

import static cc.recommenders.assertions.Checks.ensureIsNotNull;
import static cc.recommenders.assertions.Throws.throwUnhandledException;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;

import org.apache.commons.io.IOUtils;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import cc.kave.commons.utils.json.RuntimeTypeAdapterFactory;
import cc.recommenders.assertions.Throws;
import cc.recommenders.names.CoReFieldName;
import cc.recommenders.names.CoReMethodName;
import cc.recommenders.names.CoReTypeName;
import cc.recommenders.names.ICoReFieldName;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import cc.recommenders.usages.NoUsage;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;
import cc.recommenders.usages.features.CallFeature;
import cc.recommenders.usages.features.ClassFeature;
import cc.recommenders.usages.features.DefinitionFeature;
import cc.recommenders.usages.features.FirstMethodFeature;
import cc.recommenders.usages.features.ParameterFeature;
import cc.recommenders.usages.features.SuperMethodFeature;
import cc.recommenders.usages.features.TypeFeature;
import cc.recommenders.usages.features.UsageFeature;

public class GsonUtil {
	public static final Type T_LIST_STRING = new TypeToken<List<String>>() {
	}.getType();

	private static Gson gson;

	public static synchronized Gson getInstance() {
		if (gson == null) {
			final GsonBuilder builder = new GsonBuilder();
			addTypeAdapters(builder);

			builder.enableComplexMapKeySerialization();
			gson = builder.create();
		}
		return gson;
	}

	public static void addTypeAdapters(final GsonBuilder builder) {
		builder.registerTypeAdapter(CoReMethodName.class, new GsonNameSerializer());
		builder.registerTypeAdapter(ICoReMethodName.class, new GsonNameSerializer());
		builder.registerTypeAdapter(CoReMethodName.class, new GsonMethodNameDeserializer());
		builder.registerTypeAdapter(ICoReMethodName.class, new GsonMethodNameDeserializer());
		builder.registerTypeAdapter(CoReTypeName.class, new GsonNameSerializer());
		builder.registerTypeAdapter(ICoReTypeName.class, new GsonNameSerializer());
		builder.registerTypeAdapter(CoReTypeName.class, new GsonTypeNameDeserializer());
		builder.registerTypeAdapter(ICoReTypeName.class, new GsonTypeNameDeserializer());
		builder.registerTypeAdapter(CoReFieldName.class, new GsonNameSerializer());
		builder.registerTypeAdapter(ICoReFieldName.class, new GsonNameSerializer());
		builder.registerTypeAdapter(CoReFieldName.class, new GsonFieldNameDeserializer());
		builder.registerTypeAdapter(ICoReFieldName.class, new GsonFieldNameDeserializer());
		//
		builder.registerTypeAdapter(File.class, new GsonFileDeserializer());
		builder.registerTypeAdapter(File.class, new GsonFileSerializer());
		// builder.setPrettyPrinting();
		// builder.setDateFormat("dd.MM.yyyy HH:mm:ss");
		builder.registerTypeAdapter(Date.class, new ISO8601DateParser());

		// add support for new Java 8 date/time framework
		Converters.registerAll(builder);

		builder.registerTypeAdapter(Multimap.class, new MultimapTypeAdapter());
		//
		builder.registerTypeAdapter(Usage.class, new UsageTypeAdapter());
		builder.registerTypeAdapter(Query.class, new UsageTypeAdapter());
		builder.registerTypeAdapter(NoUsage.class, new UsageTypeAdapter());

		RuntimeTypeAdapterFactory<UsageFeature> rtaf = RuntimeTypeAdapterFactory.of(UsageFeature.class, "$type")
				.registerSubtype(CallFeature.class).registerSubtype(ClassFeature.class)
				.registerSubtype(DefinitionFeature.class).registerSubtype(FirstMethodFeature.class)
				.registerSubtype(ParameterFeature.class).registerSubtype(SuperMethodFeature.class)
				.registerSubtype(TypeFeature.class);
		builder.registerTypeAdapterFactory(rtaf);

		builder.registerTypeAdapter(CallFeature.class, new ObjectUsageFeatureRedirector<CallFeature>());
		builder.registerTypeAdapter(ClassFeature.class, new ObjectUsageFeatureRedirector<ClassFeature>());
		builder.registerTypeAdapter(DefinitionFeature.class, new ObjectUsageFeatureRedirector<DefinitionFeature>());
		builder.registerTypeAdapter(FirstMethodFeature.class, new ObjectUsageFeatureRedirector<FirstMethodFeature>());
		builder.registerTypeAdapter(ParameterFeature.class, new ObjectUsageFeatureRedirector<ParameterFeature>());
		builder.registerTypeAdapter(SuperMethodFeature.class, new ObjectUsageFeatureRedirector<SuperMethodFeature>());
		builder.registerTypeAdapter(TypeFeature.class, new ObjectUsageFeatureRedirector<TypeFeature>());
	}

	public static <T> T deserialize(final CharSequence json, final Type classOfT) {
		return deserialize(json.toString(), classOfT);
	}

	public static <T> T deserialize(final String json, final Type classOfT) {
		ensureIsNotNull(json);
		ensureIsNotNull(classOfT);
		return getInstance().fromJson(json, classOfT);
	}

	public static <T> T deserialize(final InputStream jsonStream, final Type classOfT) {
		ensureIsNotNull(jsonStream);
		ensureIsNotNull(classOfT);
		final InputStreamReader reader = new InputStreamReader(jsonStream);
		final T res = getInstance().fromJson(reader, classOfT);
		IOUtils.closeQuietly(reader);
		return res;
	}

	public static <T> T deserialize(final File jsonFile, final Type classOfT) {
		ensureIsNotNull(jsonFile);
		ensureIsNotNull(classOfT);
		InputStream fis;
		try {
			fis = new BufferedInputStream(new FileInputStream(jsonFile));
		} catch (final FileNotFoundException e) {
			throw Throws.throwUnhandledException(e, "Unable to deserialize from file " + jsonFile.getAbsolutePath());
		}
		return deserialize(fis, classOfT);
	}

	public static String serialize(final Object obj) {
		ensureIsNotNull(obj);
		final StringBuilder sb = new StringBuilder();
		serialize(obj, sb);
		return sb.toString();
	}

	public static void serialize(final Object obj, final Appendable writer) {
		ensureIsNotNull(obj);
		ensureIsNotNull(writer);
		getInstance().toJson(obj, writer);
	}

	public static void serialize(final Object obj, final File dest) {
		ensureIsNotNull(obj);
		ensureIsNotNull(dest);
		Writer fw = null;
		try {
			fw = new BufferedWriter(new FileWriter(dest));
			getInstance().toJson(obj, fw);
		} catch (final IOException x) {
			throwUnhandledException(x);
		} finally {
			IOUtils.closeQuietly(fw);
		}
	}
}
