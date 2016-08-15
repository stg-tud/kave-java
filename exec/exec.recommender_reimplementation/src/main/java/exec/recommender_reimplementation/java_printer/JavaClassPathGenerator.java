/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.recommender_reimplementation.java_printer;

import static java.text.MessageFormat.format;

import java.io.File;
import java.io.IOException;
import java.util.Set;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;

public class JavaClassPathGenerator {

	private String rootPath;

	public JavaClassPathGenerator(String rootPath) {
		this.rootPath = rootPath;
	}

	public void generate(Set<ISST> ssts) throws IOException {
		PhantomClassGenerator classGenerator = new PhantomClassGenerator();
		Set<ISST> convertedSSTs = classGenerator.convert(ssts);
		for (ISST sst : convertedSSTs) {
			File file = generateClassPath(sst);
			writeSST(sst, file);
		}
	}

	private void writeSST(ISST sst, File file) throws IOException {
		String javaCode = printPhantomClass(sst);
		FileUtils.writeStringToFile(file, javaCode);
	}

	private File generateClassPath(ISST sst) {
		ITypeName type = sst.getEnclosingType();
		String nestedFolderPath = createPackageSubFoldersAndReturnNestedFolderPath(type);
		File file = new File(format("{0}\\{1}.java", nestedFolderPath, type.getName()));
		if (file.exists()) {
			throw new RuntimeException("ClassPath file already exists " + file.getAbsolutePath());
		}
		return file;
	}

	private String createPackageSubFoldersAndReturnNestedFolderPath(ITypeName type) {
		String nestedFolderPath = rootPath;
		String[] packages = type.getFullName().split("\\.");
		for (int i = 0; i < packages.length - 1; i++) {
			String packageName = packages[i];
			nestedFolderPath += "\\" + packageName;
		}
		new File(nestedFolderPath).mkdirs();
		return nestedFolderPath;
	}

	private String printPhantomClass(ISST sst) {
		JavaPrintingContext context = new JavaPrintingContext();
		sst.accept(new JavaPrintingVisitor(sst, true), context);
		return context.toString();
	}

}
