package cc.kave.commons.model.ssts.expressions.assignable;

public enum UnaryOperator {
	Unknown,

	// Logical
	Not, // !b

	// Arithmetic
	PreIncrement, // ++i
	PostIncrement, // i++
	PreDecrement, // --i
	PostDecrement, // i--
	Plus, // -i
	Minus, // +i

	// Bitwise
	Complement // ~i
}
