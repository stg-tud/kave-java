package cc.kave.commons.model.ssts.statements;

import javax.annotation.Nonnull;

import cc.kave.commons.model.ssts.IStatement;

public interface IGotoStatement extends IStatement {

	@Nonnull
	String getLabel();

}
