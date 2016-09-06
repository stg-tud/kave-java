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

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.ssts.ISST;

public class JavaClassPathGenerator {

	private static final String JAVA_PATH_FORMAT = "{0}{1}{2}.java";
	private String rootPath;

	public JavaClassPathGenerator(String rootPath) {
		this.rootPath = rootPath;
	}

	public void generate(Set<ISST> ssts) throws IOException {
		for (ISST sst : ssts) {
			File file = generateClassPath(sst, rootPath);
			writeSST(sst, file);
		}
	}

	private void writeSST(ISST sst, File file) throws IOException {
		String javaCode = printPhantomClass(sst);
		FileUtils.writeStringToFile(file, javaCode);
	}

	public static File generateClassPath(ISST sst, String rootPath) {
		ITypeName type = sst.getEnclosingType();
		if(type.isPredefined()) {
			type = type.asPredefinedTypeName().getFullType();
		}
		String nestedFolderPath = createPackageSubFoldersAndReturnNestedFolderPath(type, rootPath);
		File file = new File(format(JAVA_PATH_FORMAT, nestedFolderPath, File.separator, type.getName()));
		if (file.exists()) {
			throw new RuntimeException("ClassPath file already exists " + file.getAbsolutePath());
		}
		return file;
	}

	public static String createPackageSubFoldersAndReturnNestedFolderPath(ITypeName type, String rootPath) {
		String nestedFolderPath = rootPath;
		String[] packages = type.getFullName().split("\\.");
		for (int i = 0; i < packages.length - 1; i++) {
			String packageName = packages[i];
			nestedFolderPath += File.separator + packageName;
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
