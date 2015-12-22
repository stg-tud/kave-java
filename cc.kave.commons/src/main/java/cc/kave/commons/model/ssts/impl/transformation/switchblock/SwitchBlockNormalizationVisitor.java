package cc.kave.commons.model.ssts.impl.transformation.switchblock;

import java.util.ArrayList;
import java.util.List;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.ISwitchBlock;
import cc.kave.commons.model.ssts.impl.transformation.AbstractStatementNormalizationVisitor;

public class SwitchBlockNormalizationVisitor extends AbstractStatementNormalizationVisitor<Void> {

	@Override
	public List<IStatement> visit(ISwitchBlock block, Void context) {
		super.visit(block, context);
		
		block.getReference();
		block.getSections();
		block.getDefaultSection();
		
		List<IStatement> normalized = new ArrayList<IStatement>();
		return normalized;
	}

}