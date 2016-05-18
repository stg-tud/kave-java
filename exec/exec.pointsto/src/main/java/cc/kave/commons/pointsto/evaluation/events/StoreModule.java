/**
 * Copyright 2016 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package cc.kave.commons.pointsto.evaluation.events;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import com.google.inject.TypeLiteral;

import cc.kave.commons.pointsto.evaluation.DefaultModule;
import cc.kave.commons.pointsto.stores.ProjectUsageStore;
import cc.kave.commons.pointsto.stores.UsageStore;

public class StoreModule extends DefaultModule {

	private final Path BASE_DIR = Paths.get("E:\\Coding\\MT");
	private final Path USAGES_DIR = BASE_DIR.resolve("Usages");

	@Override
	protected void configure() {
		super.configure();

		configStores();
	}

	private void configStores() {
		List<UsageStore> stores = new ArrayList<>();
		try (DirectoryStream<Path> dirStream = Files.newDirectoryStream(USAGES_DIR)) {
			for (Path dir : dirStream) {
				if (Files.isDirectory(dir)) {
					stores.add(new ProjectUsageStore(dir));
				}
			}
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		bind(new TypeLiteral<List<UsageStore>>() {
		}).toInstance(stores);
	}
}
