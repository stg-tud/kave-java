package exec.episodes;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.inject.Named;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.impl.v0.codeelements.MethodName;
import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.commons.model.naming.types.organization.IAssemblyName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.declarations.IPropertyDeclaration;
import cc.kave.episodes.io.EventStreamIo;
import cc.kave.episodes.model.events.Event;
import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;
import cc.recommenders.io.ReadingArchive;

import com.google.common.base.Predicate;

public class SSTNormalization {

	private Directory contextsDir;
	private EventStreamIo streamIo;

	@Inject
	public SSTNormalization(@Named("contexts") Directory directory,
			EventStreamIo streamIo) {
		this.contextsDir = directory;
		this.streamIo = streamIo;
	}

	public void analyze() throws IOException {

		for (String zip : findZips(contextsDir)) {
			Logger.log("Reading zip file %s", zip.toString());
			ReadingArchive ra = contextsDir.getReadingArchive(zip);

			while (ra.hasNext()) {
				Context ctx = ra.getNext(Context.class);
				ISST sst = ctx.getSST();
				
				Set<IMethodDeclaration> methods = sst.getMethods();
				Set<IPropertyDeclaration> properties = sst.getProperties();

				for (IMethodDeclaration md : methods) {
					IMethodName methodName = md.getName();
					Logger.log("Method: %s", methodName.toString());
					Logger.log("");
				}
			}
			Logger.log("");
		}
	}

	public void repoCtxs(int frequency) {
		Map<String, Set<IMethodName>> repoCtxs = streamIo
				.readRepoCtxs(frequency);

		for (Map.Entry<String, Set<IMethodName>> entry : repoCtxs.entrySet()) {
			if (entry.getKey().contains("msgpack-cli")) {

				Logger.log("Repository: %s", entry.getKey());
				for (IMethodName methodName : entry.getValue()) {
					ITypeName typeName = methodName.getDeclaringType();
					IAssemblyName asm = typeName.getAssembly();
					
					if (!asm.isLocalProject()) {
						Logger.log("%s", methodName.toString());
					}
					
					Logger.log("MethodName: %s", methodName.toString());
					Logger.log("Return type: %s", methodName.getReturnType().getIdentifier());
					Logger.log("");
				}
			}
		}
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
}
