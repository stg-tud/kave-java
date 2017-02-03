package cc.kave.episodes.preprocessing;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipException;

import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.io.FileUtils;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.Names;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.INamespaceName;
import cc.kave.episodes.eventstream.EventStreamGenerator;
import cc.kave.episodes.model.events.Event;
import cc.kave.episodes.model.events.EventKind;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class ProjectsOverlap {

	private Directory contextsDir;
	private File eventsFile;

	private static final int FOLD_NUMS = 10;
	private static final int FOLD = 0;

	@Inject
	public ProjectsOverlap(@Named("contexts") Directory directory,
			@Named("events") File eventsFile) {
		this.contextsDir = directory;
		this.eventsFile = eventsFile;
	}

	public void checkReposOverlaps() throws ZipException, IOException {
		List<Tuple<String, Set<ITypeName>>> reposProjects = generateRepos();

		Logger.log("Number of generated repositories is: %d",
				reposProjects.size());

		StringBuilder sb = new StringBuilder();
		sb.append("RepoId\tRepoId\tOverlaps\n");

		for (int idx1 = 0; idx1 < reposProjects.size() - 1; idx1++) {
			Set<ITypeName> set1 = reposProjects.get(idx1).getSecond();

			for (int idx2 = idx1 + 1; idx2 < reposProjects.size(); idx2++) {
				Set<ITypeName> set2 = reposProjects.get(idx2).getSecond();

				int overlaps = getSetOverlaps(set1, set2);
				sb.append(idx1 + "\t" + idx2 + "\t" + overlaps + "\n");
			}
			sb.append("\n");
		}
		FileUtils.writeStringToFile(getFilePath(), sb.toString());
	}

	private File getFilePath() {
		File fileName = new File(eventsFile.getAbsolutePath()
				+ "/reposOverlaps.txt");
		return fileName;
	}

	public void checkFoldsOverlap() throws ZipException, IOException {
		List<Tuple<String, Set<ITypeName>>> reposProjects = generateRepos();

		Set<ITypeName> trainingSet = createTrainingData(FOLD, FOLD_NUMS,
				reposProjects);
		Set<ITypeName> validationSet = createValidationData(FOLD, FOLD_NUMS,
				reposProjects);

		int overlaps = getSetOverlaps(trainingSet, validationSet);

		Logger.log(
				"There are %d project overlaps in training and validation code!",
				overlaps);
		Logger.log("The validation code has in total %d projects!",
				validationSet.size());
		Logger.log("The training code has in total %d projects!",
				trainingSet.size());
	}

	public Set<ITypeName> createTrainingData(int curFold, int numFolds,
			List<Tuple<String, Set<ITypeName>>> in) {

		Set<ITypeName> outs = Sets.newLinkedHashSet();

		int numRepos = 0;
		int i = 0;
		for (Tuple<String, Set<ITypeName>> tuple : in) {
			if (i != curFold) {
				outs.addAll(tuple.getSecond());
				numRepos++;
			}
			i = (i + 1) % numFolds;
		}
		Logger.log("\tWritting event stream ... (training: %d repositories)",
				numRepos);
		return outs;
	}

	public Set<ITypeName> createValidationData(int curFold, int numFolds,
			List<Tuple<String, Set<ITypeName>>> in) {

		Set<ITypeName> outs = Sets.newLinkedHashSet();

		int numRepos = 0;
		int i = 0;
		for (Tuple<String, Set<ITypeName>> tuple : in) {
			if (i == curFold) {
				outs.addAll(tuple.getSecond());
				numRepos++;
			}
			i = (i + 1) % numFolds;
		}
		Logger.log("\tWritting event stream ... (validation: %d repositories)",
				numRepos);
		return outs;
	}

	public void testCtxEventNames() throws Exception {
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if ((repoName.equalsIgnoreCase("")) || (!zip.startsWith(repoName))) {
				repoName = getRepoName(zip);
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx == null) {
					continue;
				}
				EventStreamGenerator generator = new EventStreamGenerator();
				generator.add(ctx);
				List<Event> events = generator.getEventStream();

				for (Event e : events) {
					INamespaceName ctxNamespace = ctx.getSST().getEnclosingType().getNamespace();
					
					if (e.getKind() == EventKind.METHOD_DECLARATION) {
						INamespaceName eventNamespace = e.getMethod().getDeclaringType().getNamespace();
						
						if (!ctxNamespace.equals(eventNamespace)) {
							if (e.getMethod().equals(Names.getUnknownMethod())) {
								continue;
							}
							Logger.log("Conmtext namespace: %s", ctxNamespace);
							Logger.log("Event namespace: %s", eventNamespace);
							throw new Exception("Different namespaces!");
						}
					}
				}
			}
			ra.close();
		}
	}

	private int getSetOverlaps(Set<ITypeName> set1, Set<ITypeName> set2) {
		int overlaps = 0;

		for (ITypeName typeName : set1) {
			if (set2.contains(typeName)) {
				overlaps++;
			}
		}
		return overlaps;
	}

	private List<Tuple<String, Set<ITypeName>>> generateRepos()
			throws ZipException, IOException {
		List<Context> allCtx = Lists.newLinkedList();
		List<Tuple<String, Set<ITypeName>>> results = Lists.newLinkedList();
		String repoName = "";

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			if ((repoName.equalsIgnoreCase("")) || (!zip.startsWith(repoName))) {
				if (!allCtx.isEmpty()) {
					results.add(Tuple.newTuple(repoName,
							getEnclosingTypes(allCtx)));
					allCtx = Lists.newLinkedList();
				}
				repoName = getRepoName(zip);
			}
			ReadingArchive ra = contextsDir.getReadingArchive(zip);

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				if (ctx == null) {
					continue;
				}
				allCtx.add(ctx);
			}
			ra.close();
		}
		results.add(Tuple.newTuple(repoName, getEnclosingTypes(allCtx)));
		return results;
	}

	private Set<ITypeName> getEnclosingTypes(List<Context> allCtx) {
		Set<ITypeName> typeNames = Sets.newLinkedHashSet();

		for (Context ctx : allCtx) {
			typeNames.add(ctx.getSST().getEnclosingType());
		}
		return typeNames;
	}

	private Set<String> findZips(Directory contextsDir) {
		Set<String> zips = contextsDir.findFiles(new Predicate<String>() {

			@Override
			public boolean apply(String arg0) {
				return arg0.endsWith(".zip");
			}
		});
		return zips;
	}

	private String getRepoName(String zipName) {
		int index = zipName.indexOf("/",
				zipName.indexOf("/", zipName.indexOf("/") + 1) + 1);
		String startPrefix = zipName.substring(0, index);

		return startPrefix;
	}
}
