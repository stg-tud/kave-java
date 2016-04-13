package cc.kave.commons.model.names.csharp.parser;

import org.antlr.v4.runtime.ANTLRInputStream;
import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.MethodContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.MethodEOLContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeContext;
import cc.kave.commons.model.names.csharp.parser.TypeNamingParser.TypeEOLContext;
import cc.recommenders.exceptions.AssertionException;

public class TypeNameParseUtil {

	public static TypeContext validateTypeName(String input) {
		MyErrorListener el = new MyErrorListener();
		TypeNamingParser parser = setupParser(input, el);

		// try to parse (required!)
		TypeEOLContext typeEOL = parser.typeEOL();

		// ParseTreeWalker walker = new ParseTreeWalker();
		// walker.walk(new TypeVisitor(), typeEOL);
		if (el.hasError)
			throw new AssertionException("Wrong Syntax: " + input);
		return typeEOL.type();
	}

	public static MethodContext validateMethodName(String input) {
		MyErrorListener el = new MyErrorListener();
		TypeNamingParser parser = setupParser(input, el);

		MethodEOLContext methodEOL = parser.methodEOL();

		// ParseTreeWalker walker = new ParseTreeWalker();
		// walker.walk(new TypeVisitor(), typeEOL);
		if (el.hasError)
			throw new AssertionException("Wrong Syntax: " + input);
		return methodEOL.method();
	}

	private static TypeNamingParser setupParser(String input, MyErrorListener el) {
		ANTLRInputStream is = new ANTLRInputStream(input + "\n");
		TypeNamingLexer lexer = new TypeNamingLexer(is);
		// lexer.removeErrorListeners();
		lexer.addErrorListener(el);

		CommonTokenStream tokens = new CommonTokenStream(lexer);
		TypeNamingParser parser = new TypeNamingParser(tokens);
		// parser.removeErrorListeners();
		parser.addErrorListener(el);
		return parser;
	}

	private static class MyErrorListener extends BaseErrorListener {
		private boolean hasError;

		@Override
		public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine,
				String msg, RecognitionException e) {
			hasError = true;
		}
	}

}
