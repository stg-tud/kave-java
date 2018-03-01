package cc.kave.episodes.data;

import java.util.Set;

import cc.recommenders.io.Directory;
import cc.recommenders.io.Logger;

import com.google.common.base.Predicate;
import com.google.inject.Inject;
import com.google.inject.name.Named;

public class DataCounter {

	private Directory contextsDir;
	
	@Inject
	public DataCounter(@Named("contexts") Directory directory) {
		this.contextsDir = directory;
	}
	
	public void namespaces() {
		int namespaces = findZips(contextsDir).size();
		Logger.log("Number of namespaces: %d", namespaces);
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
