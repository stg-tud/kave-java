package cc.kave.commons.model.ssts.blocks;

import cc.kave.commons.model.ssts.IStatement;

/// <summary>
///     not analyzed (this includes "fixed" statements as they are only allowed in unsafe blocks)
/// </summary>
public interface IUnsafeBlock extends IStatement {
}
