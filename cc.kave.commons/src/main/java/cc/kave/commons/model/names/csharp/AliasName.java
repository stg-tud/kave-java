package cc.kave.commons.model.names.csharp;

import java.util.Map;

import com.google.common.collect.MapMaker;

import cc.kave.commons.model.names.IAliasName;

/**
 * Aliases are defined by using statements, like "using alias = Some.Reference;"
 * . A special case is the alias "global" that represents the global namespace
 * by convention.
 */
public class AliasName extends Name implements IAliasName {
	private static final Map<String, AliasName> nameRegistry = new MapMaker().weakValues().makeMap();

	public static final IAliasName UNKNOWN_NAME = newAliasName(UNKNOWN_NAME_IDENTIFIER);

	/**
	 * Alias names are valid C# identifiers that are not keywords, plus the
	 * special alias 'global'.
	 */
	public static IAliasName newAliasName(String identifier) {
		if (!nameRegistry.containsKey(identifier)) {
			nameRegistry.put(identifier, new AliasName(identifier));
		}
		return nameRegistry.get(identifier);
	}

	private AliasName(String identifier) {
		super(identifier);
	}
}
