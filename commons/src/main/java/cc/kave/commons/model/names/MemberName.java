package cc.kave.commons.model.names;

public interface MemberName {
	TypeName getDeclaringType();

	boolean isStatic();

	String getName();
}
