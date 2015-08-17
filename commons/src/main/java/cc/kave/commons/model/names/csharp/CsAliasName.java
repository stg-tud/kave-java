package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.AliasName;

/**
 * Aliases are defined by using statements, like "using alias = Some.Reference;"
 * . A special case is the alias "global" that represents the global namespace
 * by convention.
 */
public class CsAliasName extends CsName implements AliasName {
	private static final Map<String, CsAliasName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final AliasName UNKNOWN_NAME = newAliasName(UNKNOWN_NAME_IDENTIFIER);

	/**
	 * Alias names are valid C# identifiers that are not keywords, plus the
	 * special alias 'global'.
	 */
	public static AliasName newAliasName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new CsAliasName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private CsAliasName(String identifier) {
		super(identifier);
	}
}
