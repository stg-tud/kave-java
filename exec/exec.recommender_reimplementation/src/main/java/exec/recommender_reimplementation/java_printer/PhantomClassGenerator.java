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

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.impl.SST;
import exec.recommender_reimplementation.java_printer.printer.StandardJavaPrinter;

public class PhantomClassGenerator {

	private String rootPath;

	public PhantomClassGenerator(String rootPath) {
		this.rootPath = rootPath;
	}
	
	public void generatePhantomClassesForSST(ISST sst) throws IOException {
		PhantomClassVisitor phantomClassVisitor = new PhantomClassVisitor();
		sst.accept(phantomClassVisitor, null);
		Map<ITypeName, SST> phantomSSTs = phantomClassVisitor.getPhantomSSTs();
		for (Entry<ITypeName, SST> entry : phantomSSTs.entrySet()) {
			ITypeName type = entry.getKey();
			String[] packages = type.getFullName().split("\\.");
			String nestedFolderPath = rootPath;
			String fullPackageName = "";
			for (int i = 0; i < packages.length-1; i++) {
				String packageName = packages[i];
				if (i < packages.length - 2) {
					fullPackageName += packageName + ".";
				}
				else {
					fullPackageName += packageName + ";";
				}
				nestedFolderPath += "\\" + packageName;
			}
			new File(nestedFolderPath).mkdirs();
			
			String javaCode = new StandardJavaPrinter().print(entry.getValue());
			
			javaCode = appendPackageDeclaration(fullPackageName, javaCode);
			
			FileUtils.writeStringToFile(new File(nestedFolderPath + "\\"
					+ type.getName() + ".java"), javaCode);
		}
	}

	private String appendPackageDeclaration(String fullPackageName, String javaCode) {
		javaCode = String.join("\n", "package " + fullPackageName, javaCode);
		return javaCode;
	}

}
