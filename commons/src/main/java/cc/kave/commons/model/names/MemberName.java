package cc.kave.commons.model.names;

public interface MemberName extends Name {
	TypeName getDeclaringType();

	boolean isStatic();

	String getName();
}
