package cc.kave.commons.model.ssts.expressions.assignable;

public enum BinaryOperator {
	Unknown,

	// Logical
	LessThan, LessThanOrEqual, Equal, GreaterThanOrEqual, GreaterThan, NotEqual, And, Or,

	// Arithmetic
	Plus, Minus, Multiply, Divide, Modulo,

	// Bitwise
	BitwiseAnd, BitwiseOr, BitwiseXor, ShiftLeft, ShiftRight
}