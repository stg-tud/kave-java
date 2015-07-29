package cc.kave.commons.model.ssts.impl.visitor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class NameGrabber {
	private Set<IVariableReference> fields;
	private NameScopeVisitor visitor;
	private int counter = 0;

	public NameGrabber() {
		fields = new HashSet<>();
		visitor = new NameScopeVisitor();
	}

	public void grabFields(List<IStatement> all) {
		ScopingContext context = new ScopingContext();
		context.setAllFields(true);
		context.setWithBlocks(true);
		for (IStatement statement : all)
			fields.addAll(statement.accept(visitor, context));
	}

	public Set<IVariableReference> getNames(List<IStatement> before, List<IStatement> block) {
		Set<IVariableReference> refs = new HashSet<>();
		ScopingContext beforeContext = new ScopingContext();
		beforeContext.setReturnRef(true);
		if (!before.isEmpty())
			for (IStatement statement : before)
				refs.addAll(statement.accept(visitor, beforeContext));

		ScopingContext blockContext = new ScopingContext();
		blockContext.setReturnRef(true);
		blockContext.setWithBlocks(true);
		if (!block.isEmpty())
			for (IStatement statement : block)
				refs.addAll(statement.accept(visitor, blockContext));

		refs.addAll(fields);
		return refs;
	}

	public Set<IVariableReference> getFields() {
		return fields;
	}

	public void setFields(Set<IVariableReference> fields) {
		this.fields = fields;
	}

	public int getCounter() {
		int out = counter;
		counter++;
		return out;
	}

}
