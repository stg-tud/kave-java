package cc.kave.episodes.preprocessing;

import java.util.Map;
import java.util.Set;

import javax.inject.Inject;

import cc.kave.commons.model.naming.types.ITypeName;
import cc.kave.episodes.io.RepositoriesParser;

import com.google.common.collect.Sets;

public class Checkings {

	private RepositoriesParser reposParser;
	
	@Inject
	public Checkings(RepositoriesParser parser) {
		this.reposParser = parser;
	}
	
	public void noTypeOverlaps() throws Exception {
		reposParser.generateReposEvents();
		Map<String, Set<ITypeName>> reposTypesMapper = reposParser.getRepoTypesMapper();
		Set<ITypeName> types = Sets.newLinkedHashSet();
		
		for (Map.Entry<String, Set<ITypeName>> entry : reposTypesMapper.entrySet()) {
			
			for (ITypeName t : entry.getValue()) {
				
				if (types.contains(t)) {
					throw new Exception("Type already exists in the repositories!");
				} else {
					types.add(t);
				}
			}
		}
	}
}
