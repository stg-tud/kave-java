package cc.kave.commons.model.ssts.statements;

import org.eclipse.jdt.annotation.NonNull;

import cc.kave.commons.model.ssts.IStatement;

public interface ILabelledStatement extends IStatement {

	@NonNull
	String getLabel();

	@NonNull
	IStatement getStatement();

}
