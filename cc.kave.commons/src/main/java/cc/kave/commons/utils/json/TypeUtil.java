package cc.kave.commons.utils.json;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TypeUtil {

	public static String toCSharpTypeNames(String json) {
		StringBuffer resultString = new StringBuffer();
		Pattern regex = Pattern.compile("cc\\.kave\\.commons\\.model\\.ssts\\.impl\\.([.a-zA-Z0-9_]+)");
		Matcher regexMatcher = regex.matcher(json);
		while (regexMatcher.find()) {
			String replacement = "[SST:" + toUpperCaseNamespace(regexMatcher.group(1)) + "]";
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		regex = Pattern.compile("cc\\.kave\\.commons\\.model\\.([.a-zA-Z0-9_]+)");
		regexMatcher = regex.matcher(resultString.toString());
		resultString = new StringBuffer();
		while (regexMatcher.find()) {
			String replacement = "KaVE.Commons.Model." + toUpperCaseNamespace(regexMatcher.group(1)) + ", KaVE.Commons";
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		return resultString.toString();
	}

	public static String toJavaTypeNames(String json) {
		return cSharpTypeToJava(sstTypeToJava(json));
	}

	private static String sstTypeToJava(String type) {
		StringBuffer resultString = new StringBuffer();
		Pattern regex = Pattern.compile("\\[SST:([.a-zA-Z0-9_]+)\\]");
		Matcher regexMatcher = regex.matcher(type);
		while (regexMatcher.find()) {
			String replacement = "cc.kave.commons.model.ssts.impl." + toLowerCaseNamespace(regexMatcher.group(1));
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		return resultString.toString();
	}

	private static String cSharpTypeToJava(String type) {
		StringBuffer resultString = new StringBuffer();
		Pattern regex = Pattern.compile("\"KaVE([.a-zA-Z0-9_]+), KaVE.Commons\"");
		Matcher regexMatcher = regex.matcher(type);
		while (regexMatcher.find()) {
			String replacement = "\"cc.kave" + allToLowerCaseNamespace(regexMatcher.group(1)) + "\"";
			regexMatcher.appendReplacement(resultString, replacement);
		}
		regexMatcher.appendTail(resultString);
		return resultString.toString();
	}

	private static String allToLowerCaseNamespace(String group) {
		String result = group;
		int lastIndex = 0;
		int index = group.indexOf('.', lastIndex);
		while (index != -1) {
			int nextIndex = group.indexOf('.', index + 1);
			if (nextIndex < 0)
				break;
			result = result.substring(0, index + 1) + result.substring(index + 1, nextIndex).toLowerCase()
					+ result.substring(nextIndex);
			lastIndex = index + 1;
			index = group.indexOf('.', lastIndex);
		}
		return result;
	}

	private static String toLowerCaseNamespace(String string) {
		if (string.contains(".")) {
			return string.substring(0, string.lastIndexOf(".")).toLowerCase()
					+ string.substring(string.lastIndexOf("."));
		}
		return string;
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
