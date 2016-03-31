package parsetesting;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import org.apache.commons.io.FileUtils;
import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.Invoker;
import org.apache.maven.shared.invoker.MavenInvocationException;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.utils.json.JsonUtils;
import cc.kave.eclipse.commons.analysis.transformer.DeclarationVisitor;
import cc.kave.eclipse.namefactory.NodeFactory;

public class ProjectParser {

	private List<File> javaFiles = new LinkedList<File>();
	private final File src;

	public ProjectParser(String projectPath) {
		File dir = new File(projectPath);
		src = new File(dir, "src");
		findJavaFiles(dir);
	}

	public static void main(String[] args) {
		if (args.length < 2) {
			System.out.println("not enough parameters");
			System.out.println("usage: <inputDirectory> <outputDirectory>");
			return;
		}

		String inputDir = args[0];
		String outputDir = args[1];

		File projectDir = new File(inputDir);
		String projectName = projectDir.getName();

		ProjectParser projectParser = new ProjectParser(inputDir);
		List<File> files = projectParser.getJavaFiles();

		String workingPath = System.getProperty("user.dir");
		String dependencyPath = workingPath + "\\dependencies\\" + projectName + "_dependencies";

		System.out.println("Sourcepath: " + projectParser.src.getPath());
		System.out.println("DependencyPath: " + dependencyPath + "\n");
		System.out.println("Loading Dependencies for resolving:\n");

		projectParser.loadDependencies(projectDir, dependencyPath);
		addJarsToClassLoader(dependencyPath);

		System.out.println("\nAnalyzing next project: " + projectName);
		List<String> jsonLines = new ArrayList<String>();

		for (int i = 0; i < files.size(); i++) {
			CompilationUnit parsedCu = projectParser.parseJavaFile(files.get(i),
					projectParser.getJarsFromFile(dependencyPath));

			System.out.println("#" + (i + 1) + "/" + files.size() + " Name: " + files.get(i).getName());

			SST sst = new SST();
			parsedCu.accept(new DeclarationVisitor(sst));

			jsonLines.add(JsonUtils.toJson(sst));
		}
		try {
			File sstFile = new File(outputDir + "\\" + projectName + "SSTs.txt");
			FileUtils.writeLines(sstFile, jsonLines);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void findJavaFiles(File dir) {
		File[] files = dir.listFiles();

		for (int i = 0; i < files.length; i++) {
			if (files[i].isDirectory()) {
				findJavaFiles(files[i]);
			} else if (files[i].getName().endsWith(".java")) {
				javaFiles.add(files[i]);
			}
		}

	}

	public static List<File> findProjects(File dir) {
		File[] files = dir.listFiles();
		ArrayList<File> projects = new ArrayList<File>();

		for (int i = 0; i < files.length; i++) {

			if (new File(files[i], ".project").exists()) {
				projects.add(files[i]);
			}
		}

		return projects;
	}

	public CompilationUnit parseJavaFile(File file, String[] dependencies) {
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setResolveBindings(true);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setStatementsRecovery(true);
		parser.setBindingsRecovery(true);

		String unitName = file.getPath().substring(src.getParentFile().getParentFile().getPath().length() + 1,
				file.getPath().length());
		parser.setUnitName(unitName);

		Hashtable options = JavaCore.getOptions();
		options.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_8);

		parser.setCompilerOptions(options);

		String[] sourcepathEntries = { src.getPath() };
		String[] classpathEntries = dependencies;

		parser.setEnvironment(classpathEntries, sourcepathEntries, new String[] { "UTF-8" }, true);

		try {
			parser.setSource(FileUtils.readFileToString(file).toCharArray());
		} catch (IOException e) {
			e.printStackTrace();
		}

		CompilationUnit cu = (CompilationUnit) parser.createAST(null);

		return cu;
	}

	private void loadDependencies(File projectDir, String depsOutputDir) {
		File targetPom = new File(projectDir, "pom.xml");

		if (targetPom.exists()) {

			InvocationRequest request = new DefaultInvocationRequest();
			request.setPomFile(targetPom);
			request.setGoals(Arrays.asList("dependency:copy-dependencies", "package", "jar:test-jar"));
			request.setMavenOpts("-DaddParentPoms=true -DoutputDirectory=" + depsOutputDir + " -DskipTests=true");

			Invoker invoker = new DefaultInvoker();
			try {
				invoker.execute(request);
			} catch (MavenInvocationException e) {
				e.printStackTrace();
			}

			File target = new File(projectDir + "\\target");

			if (!target.exists()) {
				target = new File(projectDir.getParentFile().getPath() + "\\target");
			}

			File[] files = target.listFiles();

			if (files != null) {
				for (int i = 0; i < files.length; i++) {
					if (files[i].getName().endsWith(".jar")) {
						try {
							FileUtils.copyFile(files[i], new File(depsOutputDir + "\\" + files[i].getName()));
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
	}

	public List<File> getJavaFiles() {
		return javaFiles;
	}

	private String[] getJarsFromFile(String filePath) {
		File[] listFiles = new File(filePath).listFiles();
		String[] jars = new String[listFiles.length];

		for (int i = 0; i < jars.length; i++) {
			jars[i] = listFiles[i].getPath();
		}

		return jars;
	}

	public static void addJarsToClassLoader(String path) {
		File[] listFiles = new File(path).listFiles();
		URL[] urls = new URL[listFiles.length];

		for (int i = 0; i < listFiles.length; i++) {
			try {
				urls[i] = listFiles[i].toURI().toURL();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			}
		}

		NodeFactory.setCustomClassLoader(new URLClassLoader(urls));
	}
}
