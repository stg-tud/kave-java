package cc.kave.commons.model.names.csharp;


public class CsUnknownTypeName extends CsTypeName {

	protected static final String UNKNOWN_TYPE_IDENTIFIER = "?";

	protected CsUnknownTypeName() {
		super(UNKNOWN_TYPE_IDENTIFIER);
	}
}
