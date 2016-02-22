package eclipse.commons.analysis.sstanalysistestsuite.blocks;

import java.io.FileInputStream;
import java.io.IOException;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import eclipse.commons.analysis.sstanalysistestsuite.BaseSSTAnalysisTest;
import eclipse.commons.analysis.sstanalysistestsuite.SSTAnalysisFixture;

public class TryBlockAnalysisTest extends BaseSSTAnalysisTest {

	@Test
	public void basicCaseTryCatch() {
		TryBlock tryBlock = new TryBlock();

		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setParameter(constructParameterName(SSTAnalysisFixture.EXCEPTION, "e"));

		tryBlock.getCatchBlocks().add(catchBlock);

		assertMethod(tryBlock);
	}

	@Test
	public void onlyTryBlock() {
		UsingBlock usingBlock = new UsingBlock();
		usingBlock.setReference(newVariableReference("f"));

		assertMethod(newVariableDeclaration("f", SSTAnalysisFixture.FILE_INPUT_STREAM),
				newAssignment("f",
						newInvokeConstructor(SSTAnalysisFixture.FILE_INPUT_STREAM_CTOR, newConstantValue(""))),
				usingBlock);
	}

	@Test
	public void tryFinally() {
		TryBlock tryBlock = new TryBlock();

		assertMethod(tryBlock);
	}

	@Test
	public void tryCatchFinallyWithStatements() {
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setParameter(constructParameterName(SSTAnalysisFixture.EXCEPTION, "e"));
		catchBlock.getBody().add(newVariableDeclaration("j", SSTAnalysisFixture.INT));

		TryBlock tryBlock = new TryBlock();
		tryBlock.getBody().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		tryBlock.getFinally().add(newVariableDeclaration("k", SSTAnalysisFixture.INT));
		tryBlock.getCatchBlocks().add(catchBlock);

		assertMethod(tryBlock);
	}

	@Test
	public void tryCatchWithResource() {
		TryBlock tryBlock = new TryBlock();

		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setParameter(constructParameterName(SSTAnalysisFixture.EXCEPTION, "e"));

		UsingBlock usingBlock = new UsingBlock();
		usingBlock.setReference(newVariableReference("f"));

		tryBlock.getCatchBlocks().add(catchBlock);
		tryBlock.getBody().add(newVariableDeclaration("f", SSTAnalysisFixture.FILE_INPUT_STREAM));
		tryBlock.getBody().add(newAssignment("f",
				newInvokeConstructor(SSTAnalysisFixture.FILE_INPUT_STREAM_CTOR, newConstantValue(""))));
		tryBlock.getBody().add(usingBlock);

		assertMethod(tryBlock);
	}

	@Test
	public void tryCatchWithMultipleResources() {
		TryBlock tryBlock = new TryBlock();
		tryBlock.getBody().add(newVariableDeclaration("f", SSTAnalysisFixture.FILE_INPUT_STREAM));
		tryBlock.getBody().add(newAssignment("f",
				newInvokeConstructor(SSTAnalysisFixture.FILE_INPUT_STREAM_CTOR, newConstantValue(""))));

		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setParameter(constructParameterName(SSTAnalysisFixture.EXCEPTION, "e"));

		UsingBlock usingBlock2 = new UsingBlock();
		usingBlock2.setReference(newVariableReference("g"));

		UsingBlock usingBlock1 = new UsingBlock();
		usingBlock1.setReference(newVariableReference("f"));
		usingBlock1.getBody().add(newVariableDeclaration("g", SSTAnalysisFixture.FILE_INPUT_STREAM));
		usingBlock1.getBody().add(newAssignment("g",
				newInvokeConstructor(SSTAnalysisFixture.FILE_INPUT_STREAM_CTOR, newConstantValue(""))));
		usingBlock1.getBody().add(usingBlock2);

		tryBlock.getCatchBlocks().add(catchBlock);
		tryBlock.getBody().add(usingBlock1);

		assertMethod(tryBlock);
	}

	@Test
	public void tryMultiCatch() {
		TryBlock tryBlock = new TryBlock();
		tryBlock.getBody().add(newVariableDeclaration("f", SSTAnalysisFixture.FILE_INPUT_STREAM));
		tryBlock.getBody().add(newAssignment("f",
				newInvokeConstructor(SSTAnalysisFixture.FILE_INPUT_STREAM_CTOR, newConstantValue(""))));

		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setParameter(constructParameterName(SSTAnalysisFixture.EXCEPTION, "e"));

		tryBlock.getCatchBlocks().add(catchBlock);

		assertMethod(tryBlock);
	}

	@Test
	public void usingCaseWithStatements() {
		UsingBlock usingBlock = new UsingBlock();
		usingBlock.setReference(newVariableReference("f"));
		usingBlock.getBody().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		usingBlock.getBody().add(newAssignment("i", newConstantValue("3")));

		assertMethod(newVariableDeclaration("f", SSTAnalysisFixture.FILE_INPUT_STREAM),
				newAssignment("f",
						newInvokeConstructor(SSTAnalysisFixture.FILE_INPUT_STREAM_CTOR, newConstantValue(""))),
				usingBlock);
	}

	@Test
	public void multipleResourcesWithStatements() {
		TryBlock tryBlock = new TryBlock();
		tryBlock.getBody().add(newVariableDeclaration("f", SSTAnalysisFixture.FILE_INPUT_STREAM));
		tryBlock.getBody().add(newAssignment("f",
				newInvokeConstructor(SSTAnalysisFixture.FILE_INPUT_STREAM_CTOR, newConstantValue(""))));

		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setParameter(constructParameterName(SSTAnalysisFixture.EXCEPTION, "e"));

		UsingBlock usingBlock2 = new UsingBlock();
		usingBlock2.setReference(newVariableReference("g"));
		usingBlock2.getBody().add(newVariableDeclaration("i", SSTAnalysisFixture.INT));
		usingBlock2.getBody().add(newAssignment("i", newConstantValue("3")));

		UsingBlock usingBlock1 = new UsingBlock();
		usingBlock1.setReference(newVariableReference("f"));
		usingBlock1.getBody().add(newVariableDeclaration("g", SSTAnalysisFixture.FILE_INPUT_STREAM));
		usingBlock1.getBody().add(newAssignment("g",
				newInvokeConstructor(SSTAnalysisFixture.FILE_INPUT_STREAM_CTOR, newConstantValue(""))));
		usingBlock1.getBody().add(usingBlock2);

		tryBlock.getCatchBlocks().add(catchBlock);
		tryBlock.getBody().add(usingBlock1);

		assertMethod(tryBlock);
	}
}
