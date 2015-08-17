package cc.kave.commons.model.names;

public interface MemberName extends Name {
	TypeName getDeclaringType();

	TypeName getValueType();

	boolean isStatic();

	String getName();
}
