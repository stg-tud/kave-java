package namefactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

public class ASTCreator {

	private InformationVisitor visitor = new InformationVisitor();

	public ASTCreator() {
		String path = "D:\\Eclipse Workspace\\de.vogella.jdt.astsimple\\src\\de\\vogella\\jdt\\astsimple\\handler\\GetInfo.java";
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);

		parser.setBindingsRecovery(true);

		Map options = JavaCore.getOptions();
		parser.setCompilerOptions(options);

		String unitName = "GetInfo.java";
		parser.setUnitName(unitName);

		String source;
		try {
			source = readFile(path);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		String[] sources = { "D:\\Eclipse Workspace\\de.vogella.jdt.astsimple\\src" };
		String[] classpath = getJarPaths("D:\\Eclipse\\plugins");
		classpath = Arrays.copyOf(classpath, classpath.length + 1);
		classpath[classpath.length - 1] = getRtJar();

		parser.setEnvironment(classpath, sources, new String[] { "UTF-8" },
				true);
		parser.setSource(source.toCharArray());

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		if (cu.getAST().hasBindingsRecovery()) {
			System.out.println("Binding activated.");
		}

		cu.accept(visitor);
	}

	private String[] getJarPaths(String path) {
		File file = new File(path);
		String[] jarPaths = file.list();

		for (int i = 0; i < jarPaths.length; i++) {
			jarPaths[i] = path + "\\" + jarPaths[i];
		}
		return jarPaths;
	}

	private String getRtJar() {
		File file = new File(System.getenv("JAVA_HOME"));

		for (File f : file.getParentFile().listFiles()) {
			if (f.getName().startsWith("jre")) {
				return f.getPath().concat("\\lib\\rt.jar");
			}
		}

		return "";
	}

	private String readFile(String path) throws IOException {
		File file = new File(path);
		String source = FileUtils.readFileToString(file);
		return source;
	}

	public InformationVisitor getVisitor() {
		return visitor;
	}
}