package cc.kave.commons.model.names;

public interface PropertyName extends MemberName {
	boolean hasSetter();

	boolean hasGetter();

	TypeName getValueType();
}
