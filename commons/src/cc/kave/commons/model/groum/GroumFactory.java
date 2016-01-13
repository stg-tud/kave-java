package cc.kave.commons.model.groum;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;

public class GroumFactory {
	public List<Groum> extractGroums(ISST sst) {
		Set<IMethodDeclaration> methods = sst.getMethods();
		List<Groum> groums = new LinkedList<>();

		for (IMethodDeclaration method : methods) {
			extractGroum(method);
		}
		return groums;
	}

	private void extractGroum(IMethodDeclaration method) {
		// TODO

	}

}
