package cc.kave.commons.model.ssts.impl.visitor;

import java.util.HashSet;
import java.util.Set;

import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;

public class SSTBuilder {

	public static ISST inlineSST(ISST sst) {

		SST newSST = new SST();
		newSST.setDelegates(sst.getDelegates());
		newSST.setEnclosingType(sst.getEnclosingType());
		newSST.setEvents(sst.getEvents());
		newSST.setFields(sst.getFields());
		newSST.setProperties(sst.getProperties());
		Set<IMethodDeclaration> methods = new HashSet<>();
		for (IMethodDeclaration method : sst.getEntryPoints()) {
			InliningVisitor visitor = new InliningVisitor();
			InliningContext context = new InliningContext();
			context.setNonEntryPoints(sst.getNonEntryPoints());
			MethodDeclaration clone = (MethodDeclaration) method.accept(visitor, context);
			methods.add(clone);
		}
		newSST.setMethods(methods);
		return newSST;
	}
}
