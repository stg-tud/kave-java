package cc.kave.commons.model.ssts.impl.visitor.inlining;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.SSTUtil;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.references.IVariableReference;

public class InliningUtil {
	public static final TypeName GOT_RESULT_TYPE = CsTypeName.newTypeName("Boolean");
	public static final String RESULT_NAME = "$result_";
	public static final String RESULT_FLAG = "$gotNoResult_";

	public static void visitBlock(List<IStatement> body, List<IStatement> blockBody, InliningContext context) {
		context.enterBlock();
		int index = 0;
		for (IStatement statement : body) {
			statement.accept(context.getStatementVisitor(), context);
			if ((context.isGuardNeeded()) && index < body.size() - 1
					&& body.subList(index + 1, body.size()).size() > 0) {
				IfElseBlock block = new IfElseBlock();
				block.setCondition(SSTUtil.referenceExprToVariable(context.getGotResultName()));
				context.setGuardNeeded(false);
				visitBlock(body.subList(index + 1, body.size()), block.getThen(), context);
				context.addStatement(block);
				break;
			}
			index++;

		}
		context.leaveBlock(blockBody);
	}

	public static IStatement visit(IStatement statement, InliningContext context) {
		List<IStatement> body = new ArrayList<>();
		visitBlock(Lists.newArrayList(statement), body, context);
		return body.get(0);
	}

	public static void visitScope(List<IStatement> body, InliningContext context) {
		visitScope(body, context, null);
	}

	public static void visitScope(List<IStatement> body, InliningContext context,
			Map<IVariableReference, IVariableReference> preChangedNames) {
		context.enterScope(body, preChangedNames);
		int index = 0;
		for (IStatement statement : body) {
			statement.accept(context.getStatementVisitor(), context);

			if ((context.isGuardNeeded() || context.isGlobalGuardNeeded())
					&& body.subList(index + 1, body.size()).size() > 0) {
				IfElseBlock block = new IfElseBlock();
				block.setCondition(SSTUtil.referenceExprToVariable(context.getGotResultName()));
				context.setGuardNeeded(false);
				context.setGlobalGuardNeeded(false);
				visitBlock(body.subList(index + 1, body.size()), block.getThen(), context);
				context.addStatement(block);
				break;
			}
			index++;

		}
		// newBody.addAll(context.getBody());
		context.leaveScope();
	}
}
