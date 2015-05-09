package cc.kave.commons.model.ssts.impl.blocks;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ICaseBlock;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.references.IVariableReference;
import cc.kave.commons.model.ssts.visitor.ISSTNodeVisitor;

public class SwitchBlock implements ISwitchBlock {

	private IVariableReference reference;
	private List<ICaseBlock> sections;
	private List<IStatement> defaultSection;

	public SwitchBlock() {
		this.reference = new VariableReference();
		this.sections = new ArrayList<ICaseBlock>();
		this.defaultSection = new ArrayList<IStatement>();
	}

	@Override
	public IVariableReference getReference() {
		return this.reference;
	}

	@Override
	public List<ICaseBlock> getSections() {
		return this.sections;
	}

	@Override
	public List<IStatement> getDefaultSection() {
		return this.defaultSection;
	}

	public void setReference(IVariableReference reference) {
		this.reference = reference;
	}

	public void setSections(List<ICaseBlock> sections) {
		this.sections = sections;
	}

	public void setDefaultSection(List<IStatement> defaultSection) {
		this.defaultSection = defaultSection;
	}

	private boolean equals(SwitchBlock other) {
		return this.reference.equals(other.getReference()) && this.sections.equals(other.getSections())
				&& this.defaultSection.equals(other.getDefaultSection());
	}

	public boolean equals(Object obj) {
		return obj instanceof SwitchBlock ? this.equals((SwitchBlock) obj) : false;
	}

	public int hashCode() {
		int hashCode = 36 + this.sections.hashCode();
		hashCode = (hashCode * 397) ^ this.defaultSection.hashCode();
		hashCode = (hashCode * 397) ^ this.reference.hashCode();
		return hashCode;
	}

	@Override
	public <TContext, TReturn> TReturn accept(ISSTNodeVisitor<TContext, TReturn> visitor, TContext context) {
		return visitor.visit(this, context);
	}

}
