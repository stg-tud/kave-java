package cc.kave.commons.utils.visitor;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.impl.visitor.inlining.InliningIStatementVisitor;

public class InliningUtils {
	public static void visit(List<IStatement> body, List<IStatement> blockBody, InliningContext context) {
		context.enterBlock();
		for (IStatement statement : body)
			statement.accept(new InliningIStatementVisitor(), context);
		context.leaveBlock(blockBody);
	}

	public static IStatement visit(IStatement statement, InliningContext context) {
		List<IStatement> body = new ArrayList<>();
		visit(Lists.newArrayList(statement), body, context);
		return body.get(0);
	}
}
