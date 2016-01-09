package cc.kave.commons.utils.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeUtil {

	public static String toCSharpTypeNames(String json) {
		return replacePattern(
				replacePattern(json, "cc\\.kave\\.commons\\.model\\.ssts\\.impl\\.([.a-zA-Z0-9_]+)", "[SST:", "]",
						false),
				"cc\\.kave\\.commons\\.model\\.([.a-zA-Z0-9_]+)", "KaVE.Commons.Model.", ", KaVE.Commons", false);
	}

	public static String toJavaTypeNames(String json) {
		return replacePattern(
				replacePattern(json, "\\[SST:([.a-zA-Z0-9_]+)\\]", "cc.kave.commons.model.ssts.impl.", "", true),
				"\"KaVE([.a-zA-Z0-9_]+), KaVE.Commons\"", "\"cc.kave", "\"", true);
	}

	private static String replacePattern(String type, String pattern, String prefix, String suffix, boolean lower) {
		StringBuffer resultString = new StringBuffer();
		Pattern regex = Pattern.compile(pattern);
		Matcher regexMatcher = regex.matcher(type);
		while (regexMatcher.find()) {
			String replacement = prefix + (lower ? allToLowerCaseNamespace(regexMatcher.group(1))
					: toUpperCaseNamespace(regexMatcher.group(1))) + suffix;
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		return resultString.toString();
	}

	private static String allToLowerCaseNamespace(String group) {
		if (group.lastIndexOf('.') != -1) {
			return group.substring(0, group.lastIndexOf('.')).toLowerCase() + group.substring(group.lastIndexOf('.'));
		}
		return group;
	}

	private static String toUpperCaseNamespace(String string) {
		String[] path = string.split("[.]");
		String type = "";
		for (int i = 0; i < path.length; i++) {
			if (i != path.length - 1) {
				if (path[i].equals("loopheader"))
					path[i] = "loopHeader";
				else if (path[i].equals("completionevents"))
					path[i] = "completionEvents";
				else if (path[i].equals("typeshapes"))
					path[i] = "typeShapes";
				type += path[i].substring(0, 1).toUpperCase() + path[i].substring(1) + ".";
			} else
				type += path[i];
		}
		return type;
	}
}
