package eclipse.commons.analysis.sstanalysistestsuite.blocks;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.blocks.CaseBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class SwitchBlockAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void empty() {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.setReference(newVariableReference("i"));

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("3")),
				switchBlock);
	}

	@Test
	public void defaultSectionOnly() {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.getDefaultSection().add(new BreakStatement());
		switchBlock.setReference(newVariableReference("i"));

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("3")),
				switchBlock);
	}

	@Test
	public void constantAsReference() {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.setReference(newVariableReference("$0"));

		assertMethod(newVariableDeclaration("$0", SSTAnalysisFixture.INT), newAssignment("$0", newConstantValue("3")),
				switchBlock);
	}
	

	@Test
	public void Standard() {
		SwitchBlock switchBlock = new SwitchBlock();
		switchBlock.getDefaultSection().add(new BreakStatement());
		switchBlock.setReference(newVariableReference("i"));
		
		CaseBlock caseBlock1 = new CaseBlock();
		caseBlock1.setLabel(newConstantValue("0"));
		
		CaseBlock caseBlock2 = new CaseBlock();
		caseBlock2.setLabel(newConstantValue("1"));
		caseBlock2.getBody().add(new BreakStatement());
		
		switchBlock.getSections().add(caseBlock1);
		switchBlock.getSections().add(caseBlock2);

		assertMethod(newVariableDeclaration("i", SSTAnalysisFixture.INT), newAssignment("i", newConstantValue("3")),
				switchBlock);
	}
}
