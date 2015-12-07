package c.kave.commons.evaluation.queries;

import com.google.inject.Inject;

public class QueryBuilderFactory {

	private QueryMethodDeclaration md;
	private QueryMethodInvocation mi;
	private QueryPartialBuilder nm;
	
	@Inject
	public QueryBuilderFactory(QueryMethodDeclaration md, QueryMethodInvocation mi, QueryPartialBuilder nm) {
		this.md = md;
		this.mi = mi;
		this.nm = nm;
	}
}
