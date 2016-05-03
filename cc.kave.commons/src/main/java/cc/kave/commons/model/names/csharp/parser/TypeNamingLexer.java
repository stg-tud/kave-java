// Generated from TypeNaming.g4 by ANTLR 4.5.3

/**
 * Copyright 2016 Sebastian Proksch
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.commons.model.names.csharp.parser;

import org.antlr.v4.runtime.Lexer;
import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.TokenStream;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.misc.*;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TypeNamingLexer extends Lexer {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		UNKNOWN=25, POSNUM=26, LETTER=27, SIGN=28, WS=29, EOL=30;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "T__18", "T__19", "T__20", "T__21", "T__22", "T__23", "UNKNOWN", 
		"POSNUM", "LETTER", "SIGN", "DIGIT", "DIGIT_NON_ZERO", "WS", "EOL"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'d:'", "'arr('", "'):'", "'n:'", "'+'", "'.'", "'e:'", "'i:'", 
		"'s:'", "'''", "'['", "']'", "'->'", "'('", "')'", "'].'", "'.ctor'", 
		"'.cctor'", "'static'", "'params'", "'opts'", "'ref'", "'0'", "'?'", null, 
		null, null, null, "'\n'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, "UNKNOWN", "POSNUM", "LETTER", "SIGN", "WS", "EOL"
	};
	public static final Vocabulary VOCABULARY = new VocabularyImpl(_LITERAL_NAMES, _SYMBOLIC_NAMES);

	/**
	 * @deprecated Use {@link #VOCABULARY} instead.
	 */
	@Deprecated
	public static final String[] tokenNames;
	static {
		tokenNames = new String[_SYMBOLIC_NAMES.length];
		for (int i = 0; i < tokenNames.length; i++) {
			tokenNames[i] = VOCABULARY.getLiteralName(i);
			if (tokenNames[i] == null) {
				tokenNames[i] = VOCABULARY.getSymbolicName(i);
			}

			if (tokenNames[i] == null) {
				tokenNames[i] = "<INVALID>";
			}
		}
	}

	@Override
	@Deprecated
	public String[] getTokenNames() {
		return tokenNames;
	}

	@Override

	public Vocabulary getVocabulary() {
		return VOCABULARY;
	}


	public TypeNamingLexer(CharStream input) {
		super(input);
		_interp = new LexerATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}

	@Override
	public String getGrammarFileName() { return "TypeNaming.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public String[] getModeNames() { return modeNames; }

	@Override
	public ATN getATN() { return _ATN; }

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2 \u00b0\b\1\4\2\t"+
		"\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\3\5\3\5\3\5\3\6\3\6\3\6\3"+
		"\7\3\7\3\b\3\b\3\t\3\t\3\t\3\n\3\n\3\n\3\13\3\13\3\13\3\f\3\f\3\r\3\r"+
		"\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3\21\3\21\3\22\3\22\3\22\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\3\24\3\24\3\24\3\24\3\24\3\24\3\24\3\25\3\25\3\25"+
		"\3\25\3\25\3\25\3\25\3\26\3\26\3\26\3\26\3\26\3\26\3\26\3\27\3\27\3\27"+
		"\3\27\3\27\3\30\3\30\3\30\3\30\3\31\3\31\3\32\3\32\3\33\3\33\7\33\u009b"+
		"\n\33\f\33\16\33\u009e\13\33\3\34\3\34\3\35\3\35\3\36\3\36\5\36\u00a6"+
		"\n\36\3\37\3\37\3 \6 \u00ab\n \r \16 \u00ac\3!\3!\2\2\"\3\3\5\4\7\5\t"+
		"\6\13\7\r\b\17\t\21\n\23\13\25\f\27\r\31\16\33\17\35\20\37\21!\22#\23"+
		"%\24\'\25)\26+\27-\30/\31\61\32\63\33\65\34\67\359\36;\2=\2?\37A \3\2"+
		"\5\4\2C\\c|\13\2##%&,-//\61\61<=??BBaa\4\2\13\13\"\"\u00b0\2\3\3\2\2\2"+
		"\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2\2\2\2\17\3\2"+
		"\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2\2\31\3\2\2\2"+
		"\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2\2\2\2%\3\2\2"+
		"\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2/\3\2\2\2\2\61\3\2\2"+
		"\2\2\63\3\2\2\2\2\65\3\2\2\2\2\67\3\2\2\2\29\3\2\2\2\2?\3\2\2\2\2A\3\2"+
		"\2\2\3C\3\2\2\2\5E\3\2\2\2\7H\3\2\2\2\tM\3\2\2\2\13P\3\2\2\2\rS\3\2\2"+
		"\2\17U\3\2\2\2\21W\3\2\2\2\23Z\3\2\2\2\25]\3\2\2\2\27`\3\2\2\2\31b\3\2"+
		"\2\2\33d\3\2\2\2\35f\3\2\2\2\37i\3\2\2\2!k\3\2\2\2#m\3\2\2\2%p\3\2\2\2"+
		"\'v\3\2\2\2)}\3\2\2\2+\u0084\3\2\2\2-\u008b\3\2\2\2/\u0090\3\2\2\2\61"+
		"\u0094\3\2\2\2\63\u0096\3\2\2\2\65\u0098\3\2\2\2\67\u009f\3\2\2\29\u00a1"+
		"\3\2\2\2;\u00a5\3\2\2\2=\u00a7\3\2\2\2?\u00aa\3\2\2\2A\u00ae\3\2\2\2C"+
		"D\7.\2\2D\4\3\2\2\2EF\7f\2\2FG\7<\2\2G\6\3\2\2\2HI\7c\2\2IJ\7t\2\2JK\7"+
		"t\2\2KL\7*\2\2L\b\3\2\2\2MN\7+\2\2NO\7<\2\2O\n\3\2\2\2PQ\7p\2\2QR\7<\2"+
		"\2R\f\3\2\2\2ST\7-\2\2T\16\3\2\2\2UV\7\60\2\2V\20\3\2\2\2WX\7g\2\2XY\7"+
		"<\2\2Y\22\3\2\2\2Z[\7k\2\2[\\\7<\2\2\\\24\3\2\2\2]^\7u\2\2^_\7<\2\2_\26"+
		"\3\2\2\2`a\7)\2\2a\30\3\2\2\2bc\7]\2\2c\32\3\2\2\2de\7_\2\2e\34\3\2\2"+
		"\2fg\7/\2\2gh\7@\2\2h\36\3\2\2\2ij\7*\2\2j \3\2\2\2kl\7+\2\2l\"\3\2\2"+
		"\2mn\7_\2\2no\7\60\2\2o$\3\2\2\2pq\7\60\2\2qr\7e\2\2rs\7v\2\2st\7q\2\2"+
		"tu\7t\2\2u&\3\2\2\2vw\7\60\2\2wx\7e\2\2xy\7e\2\2yz\7v\2\2z{\7q\2\2{|\7"+
		"t\2\2|(\3\2\2\2}~\7u\2\2~\177\7v\2\2\177\u0080\7c\2\2\u0080\u0081\7v\2"+
		"\2\u0081\u0082\7k\2\2\u0082\u0083\7e\2\2\u0083*\3\2\2\2\u0084\u0085\7"+
		"r\2\2\u0085\u0086\7c\2\2\u0086\u0087\7t\2\2\u0087\u0088\7c\2\2\u0088\u0089"+
		"\7o\2\2\u0089\u008a\7u\2\2\u008a,\3\2\2\2\u008b\u008c\7q\2\2\u008c\u008d"+
		"\7r\2\2\u008d\u008e\7v\2\2\u008e\u008f\7u\2\2\u008f.\3\2\2\2\u0090\u0091"+
		"\7t\2\2\u0091\u0092\7g\2\2\u0092\u0093\7h\2\2\u0093\60\3\2\2\2\u0094\u0095"+
		"\7\62\2\2\u0095\62\3\2\2\2\u0096\u0097\7A\2\2\u0097\64\3\2\2\2\u0098\u009c"+
		"\5=\37\2\u0099\u009b\5;\36\2\u009a\u0099\3\2\2\2\u009b\u009e\3\2\2\2\u009c"+
		"\u009a\3\2\2\2\u009c\u009d\3\2\2\2\u009d\66\3\2\2\2\u009e\u009c\3\2\2"+
		"\2\u009f\u00a0\t\2\2\2\u00a08\3\2\2\2\u00a1\u00a2\t\3\2\2\u00a2:\3\2\2"+
		"\2\u00a3\u00a6\7\62\2\2\u00a4\u00a6\5=\37\2\u00a5\u00a3\3\2\2\2\u00a5"+
		"\u00a4\3\2\2\2\u00a6<\3\2\2\2\u00a7\u00a8\4\63;\2\u00a8>\3\2\2\2\u00a9"+
		"\u00ab\t\4\2\2\u00aa\u00a9\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00aa\3\2"+
		"\2\2\u00ac\u00ad\3\2\2\2\u00ad@\3\2\2\2\u00ae\u00af\7\f\2\2\u00afB\3\2"+
		"\2\2\6\2\u009c\u00a5\u00ac\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}