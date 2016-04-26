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
		T__17=18, UNKNOWN=19, POSNUM=20, LETTER=21, SIGN=22, WS=23, EOL=24;
	public static String[] modeNames = {
		"DEFAULT_MODE"
	};

	public static final String[] ruleNames = {
		"T__0", "T__1", "T__2", "T__3", "T__4", "T__5", "T__6", "T__7", "T__8", 
		"T__9", "T__10", "T__11", "T__12", "T__13", "T__14", "T__15", "T__16", 
		"T__17", "UNKNOWN", "POSNUM", "LETTER", "SIGN", "DIGIT", "DIGIT_NON_ZERO", 
		"WS", "EOL"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "'->'", "','", "'d:'", "'arr('", "'):'", "'n:'", "'+'", "'.'", "'e:'", 
		"'i:'", "'s:'", "'''", "'['", "']'", "'].'", "'('", "')'", "'0'", "'?'", 
		null, null, null, null, "'\n'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, "UNKNOWN", "POSNUM", "LETTER", 
		"SIGN", "WS", "EOL"
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\2\32\u0080\b\1\4\2"+
		"\t\2\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4"+
		"\13\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22"+
		"\t\22\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31"+
		"\t\31\4\32\t\32\4\33\t\33\3\2\3\2\3\2\3\3\3\3\3\4\3\4\3\4\3\5\3\5\3\5"+
		"\3\5\3\5\3\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\t\3\t\3\n\3\n\3\n\3\13\3\13"+
		"\3\13\3\f\3\f\3\f\3\r\3\r\3\16\3\16\3\17\3\17\3\20\3\20\3\20\3\21\3\21"+
		"\3\22\3\22\3\23\3\23\3\24\3\24\3\25\3\25\7\25k\n\25\f\25\16\25n\13\25"+
		"\3\26\3\26\3\27\3\27\3\30\3\30\5\30v\n\30\3\31\3\31\3\32\6\32{\n\32\r"+
		"\32\16\32|\3\33\3\33\2\2\34\3\3\5\4\7\5\t\6\13\7\r\b\17\t\21\n\23\13\25"+
		"\f\27\r\31\16\33\17\35\20\37\21!\22#\23%\24\'\25)\26+\27-\30/\2\61\2\63"+
		"\31\65\32\3\2\5\4\2C\\c|\13\2##%&,-//\61\61<=??BBaa\4\2\13\13\"\"\u0080"+
		"\2\3\3\2\2\2\2\5\3\2\2\2\2\7\3\2\2\2\2\t\3\2\2\2\2\13\3\2\2\2\2\r\3\2"+
		"\2\2\2\17\3\2\2\2\2\21\3\2\2\2\2\23\3\2\2\2\2\25\3\2\2\2\2\27\3\2\2\2"+
		"\2\31\3\2\2\2\2\33\3\2\2\2\2\35\3\2\2\2\2\37\3\2\2\2\2!\3\2\2\2\2#\3\2"+
		"\2\2\2%\3\2\2\2\2\'\3\2\2\2\2)\3\2\2\2\2+\3\2\2\2\2-\3\2\2\2\2\63\3\2"+
		"\2\2\2\65\3\2\2\2\3\67\3\2\2\2\5:\3\2\2\2\7<\3\2\2\2\t?\3\2\2\2\13D\3"+
		"\2\2\2\rG\3\2\2\2\17J\3\2\2\2\21L\3\2\2\2\23N\3\2\2\2\25Q\3\2\2\2\27T"+
		"\3\2\2\2\31W\3\2\2\2\33Y\3\2\2\2\35[\3\2\2\2\37]\3\2\2\2!`\3\2\2\2#b\3"+
		"\2\2\2%d\3\2\2\2\'f\3\2\2\2)h\3\2\2\2+o\3\2\2\2-q\3\2\2\2/u\3\2\2\2\61"+
		"w\3\2\2\2\63z\3\2\2\2\65~\3\2\2\2\678\7/\2\289\7@\2\29\4\3\2\2\2:;\7."+
		"\2\2;\6\3\2\2\2<=\7f\2\2=>\7<\2\2>\b\3\2\2\2?@\7c\2\2@A\7t\2\2AB\7t\2"+
		"\2BC\7*\2\2C\n\3\2\2\2DE\7+\2\2EF\7<\2\2F\f\3\2\2\2GH\7p\2\2HI\7<\2\2"+
		"I\16\3\2\2\2JK\7-\2\2K\20\3\2\2\2LM\7\60\2\2M\22\3\2\2\2NO\7g\2\2OP\7"+
		"<\2\2P\24\3\2\2\2QR\7k\2\2RS\7<\2\2S\26\3\2\2\2TU\7u\2\2UV\7<\2\2V\30"+
		"\3\2\2\2WX\7)\2\2X\32\3\2\2\2YZ\7]\2\2Z\34\3\2\2\2[\\\7_\2\2\\\36\3\2"+
		"\2\2]^\7_\2\2^_\7\60\2\2_ \3\2\2\2`a\7*\2\2a\"\3\2\2\2bc\7+\2\2c$\3\2"+
		"\2\2de\7\62\2\2e&\3\2\2\2fg\7A\2\2g(\3\2\2\2hl\5\61\31\2ik\5/\30\2ji\3"+
		"\2\2\2kn\3\2\2\2lj\3\2\2\2lm\3\2\2\2m*\3\2\2\2nl\3\2\2\2op\t\2\2\2p,\3"+
		"\2\2\2qr\t\3\2\2r.\3\2\2\2sv\7\62\2\2tv\5\61\31\2us\3\2\2\2ut\3\2\2\2"+
		"v\60\3\2\2\2wx\4\63;\2x\62\3\2\2\2y{\t\4\2\2zy\3\2\2\2{|\3\2\2\2|z\3\2"+
		"\2\2|}\3\2\2\2}\64\3\2\2\2~\177\7\f\2\2\177\66\3\2\2\2\6\2lu|\2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}