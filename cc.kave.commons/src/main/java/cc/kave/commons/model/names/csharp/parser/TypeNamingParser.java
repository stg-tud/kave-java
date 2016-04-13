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

import org.antlr.v4.runtime.atn.*;
import org.antlr.v4.runtime.dfa.DFA;
import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.*;
import org.antlr.v4.runtime.tree.*;
import java.util.List;
import java.util.Iterator;
import java.util.ArrayList;

@SuppressWarnings({"all", "warnings", "unchecked", "unused", "cast"})
public class TypeNamingParser extends Parser {
	static { RuntimeMetaData.checkVersion("4.5.3", RuntimeMetaData.VERSION); }

	protected static final DFA[] _decisionToDFA;
	protected static final PredictionContextCache _sharedContextCache =
		new PredictionContextCache();
	public static final int
		T__0=1, T__1=2, T__2=3, T__3=4, T__4=5, T__5=6, T__6=7, T__7=8, T__8=9, 
		T__9=10, T__10=11, T__11=12, UNKNOWN=13, POSNUM=14, LETTER=15, SIGN=16, 
		WS=17, EOL=18;
	public static final int
		RULE_typeEOL = 0, RULE_methodEOL = 1, RULE_type = 2, RULE_typeParameter = 3, 
		RULE_regularType = 4, RULE_delegateType = 5, RULE_arrayPart = 6, RULE_resolvedType = 7, 
		RULE_namespace = 8, RULE_typeName = 9, RULE_simpleTypeName = 10, RULE_genericTypePart = 11, 
		RULE_genericParam = 12, RULE_assembly = 13, RULE_assemblyVersion = 14, 
		RULE_method = 15, RULE_formalParam = 16, RULE_id = 17, RULE_num = 18;
	public static final String[] ruleNames = {
		"typeEOL", "methodEOL", "type", "typeParameter", "regularType", "delegateType", 
		"arrayPart", "resolvedType", "namespace", "typeName", "simpleTypeName", 
		"genericTypePart", "genericParam", "assembly", "assemblyVersion", "method", 
		"formalParam", "id", "num"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'d:'", "'['", "']'", "'+'", "'.'", "'''", "'->'", "'].'", 
		"'('", "')'", "'0'", "'?'", null, null, null, null, "'\n'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
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

	@Override
	public String getGrammarFileName() { return "TypeNaming.g4"; }

	@Override
	public String[] getRuleNames() { return ruleNames; }

	@Override
	public String getSerializedATN() { return _serializedATN; }

	@Override
	public ATN getATN() { return _ATN; }

	public TypeNamingParser(TokenStream input) {
		super(input);
		_interp = new ParserATNSimulator(this,_ATN,_decisionToDFA,_sharedContextCache);
	}
	public static class TypeEOLContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public TerminalNode EOL() { return getToken(TypeNamingParser.EOL, 0); }
		public TypeEOLContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeEOL; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterTypeEOL(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitTypeEOL(this);
		}
	}

	public final TypeEOLContext typeEOL() throws RecognitionException {
		TypeEOLContext _localctx = new TypeEOLContext(_ctx, getState());
		enterRule(_localctx, 0, RULE_typeEOL);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(38);
			type();
			setState(39);
			match(EOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodEOLContext extends ParserRuleContext {
		public MethodContext method() {
			return getRuleContext(MethodContext.class,0);
		}
		public TerminalNode EOL() { return getToken(TypeNamingParser.EOL, 0); }
		public MethodEOLContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodEOL; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterMethodEOL(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitMethodEOL(this);
		}
	}

	public final MethodEOLContext methodEOL() throws RecognitionException {
		MethodEOLContext _localctx = new MethodEOLContext(_ctx, getState());
		enterRule(_localctx, 2, RULE_methodEOL);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(41);
			method();
			setState(42);
			match(EOL);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeContext extends ParserRuleContext {
		public TerminalNode UNKNOWN() { return getToken(TypeNamingParser.UNKNOWN, 0); }
		public TypeParameterContext typeParameter() {
			return getRuleContext(TypeParameterContext.class,0);
		}
		public RegularTypeContext regularType() {
			return getRuleContext(RegularTypeContext.class,0);
		}
		public DelegateTypeContext delegateType() {
			return getRuleContext(DelegateTypeContext.class,0);
		}
		public ArrayPartContext arrayPart() {
			return getRuleContext(ArrayPartContext.class,0);
		}
		public TypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_type; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitType(this);
		}
	}

	public final TypeContext type() throws RecognitionException {
		TypeContext _localctx = new TypeContext(_ctx, getState());
		enterRule(_localctx, 4, RULE_type);
		int _la;
		try {
			setState(53);
			switch (_input.LA(1)) {
			case UNKNOWN:
				enterOuterAlt(_localctx, 1);
				{
				setState(44);
				match(UNKNOWN);
				}
				break;
			case T__1:
			case LETTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(48);
				_errHandler.sync(this);
				switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
				case 1:
					{
					setState(45);
					typeParameter();
					}
					break;
				case 2:
					{
					setState(46);
					regularType();
					}
					break;
				case 3:
					{
					setState(47);
					delegateType();
					}
					break;
				}
				setState(51);
				_la = _input.LA(1);
				if (_la==T__2) {
					{
					setState(50);
					arrayPart();
					}
				}

				}
				break;
			default:
				throw new NoViableAltException(this);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeParameterContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TypeParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterTypeParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitTypeParameter(this);
		}
	}

	public final TypeParameterContext typeParameter() throws RecognitionException {
		TypeParameterContext _localctx = new TypeParameterContext(_ctx, getState());
		enterRule(_localctx, 6, RULE_typeParameter);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(55);
			id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class RegularTypeContext extends ParserRuleContext {
		public ResolvedTypeContext resolvedType() {
			return getRuleContext(ResolvedTypeContext.class,0);
		}
		public AssemblyContext assembly() {
			return getRuleContext(AssemblyContext.class,0);
		}
		public TerminalNode WS() { return getToken(TypeNamingParser.WS, 0); }
		public RegularTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regularType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterRegularType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitRegularType(this);
		}
	}

	public final RegularTypeContext regularType() throws RecognitionException {
		RegularTypeContext _localctx = new RegularTypeContext(_ctx, getState());
		enterRule(_localctx, 8, RULE_regularType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(57);
			resolvedType();
			setState(58);
			match(T__0);
			setState(60);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(59);
				match(WS);
				}
			}

			setState(62);
			assembly();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class DelegateTypeContext extends ParserRuleContext {
		public MethodContext method() {
			return getRuleContext(MethodContext.class,0);
		}
		public DelegateTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_delegateType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterDelegateType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitDelegateType(this);
		}
	}

	public final DelegateTypeContext delegateType() throws RecognitionException {
		DelegateTypeContext _localctx = new DelegateTypeContext(_ctx, getState());
		enterRule(_localctx, 10, RULE_delegateType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(64);
			match(T__1);
			setState(65);
			method();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ArrayPartContext extends ParserRuleContext {
		public ArrayPartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayPart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterArrayPart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitArrayPart(this);
		}
	}

	public final ArrayPartContext arrayPart() throws RecognitionException {
		ArrayPartContext _localctx = new ArrayPartContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arrayPart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(67);
			match(T__2);
			setState(71);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(68);
				match(T__0);
				}
				}
				setState(73);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(74);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class ResolvedTypeContext extends ParserRuleContext {
		public List<TypeNameContext> typeName() {
			return getRuleContexts(TypeNameContext.class);
		}
		public TypeNameContext typeName(int i) {
			return getRuleContext(TypeNameContext.class,i);
		}
		public NamespaceContext namespace() {
			return getRuleContext(NamespaceContext.class,0);
		}
		public ResolvedTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_resolvedType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterResolvedType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitResolvedType(this);
		}
	}

	public final ResolvedTypeContext resolvedType() throws RecognitionException {
		ResolvedTypeContext _localctx = new ResolvedTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_resolvedType);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(77);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,5,_ctx) ) {
			case 1:
				{
				setState(76);
				namespace();
				}
				break;
			}
			setState(79);
			typeName();
			setState(84);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__4) {
				{
				{
				setState(80);
				match(T__4);
				setState(81);
				typeName();
				}
				}
				setState(86);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NamespaceContext extends ParserRuleContext {
		public List<IdContext> id() {
			return getRuleContexts(IdContext.class);
		}
		public IdContext id(int i) {
			return getRuleContext(IdContext.class,i);
		}
		public NamespaceContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_namespace; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterNamespace(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitNamespace(this);
		}
	}

	public final NamespaceContext namespace() throws RecognitionException {
		NamespaceContext _localctx = new NamespaceContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_namespace);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(90); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(87);
					id();
					setState(88);
					match(T__5);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(92); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,7,_ctx);
			} while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER );
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class TypeNameContext extends ParserRuleContext {
		public SimpleTypeNameContext simpleTypeName() {
			return getRuleContext(SimpleTypeNameContext.class,0);
		}
		public GenericTypePartContext genericTypePart() {
			return getRuleContext(GenericTypePartContext.class,0);
		}
		public TypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_typeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitTypeName(this);
		}
	}

	public final TypeNameContext typeName() throws RecognitionException {
		TypeNameContext _localctx = new TypeNameContext(_ctx, getState());
		enterRule(_localctx, 18, RULE_typeName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(94);
			simpleTypeName();
			setState(96);
			_la = _input.LA(1);
			if (_la==T__6) {
				{
				setState(95);
				genericTypePart();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class SimpleTypeNameContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public SimpleTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_simpleTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterSimpleTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitSimpleTypeName(this);
		}
	}

	public final SimpleTypeNameContext simpleTypeName() throws RecognitionException {
		SimpleTypeNameContext _localctx = new SimpleTypeNameContext(_ctx, getState());
		enterRule(_localctx, 20, RULE_simpleTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(98);
			id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GenericTypePartContext extends ParserRuleContext {
		public TerminalNode POSNUM() { return getToken(TypeNamingParser.POSNUM, 0); }
		public List<GenericParamContext> genericParam() {
			return getRuleContexts(GenericParamContext.class);
		}
		public GenericParamContext genericParam(int i) {
			return getRuleContext(GenericParamContext.class,i);
		}
		public GenericTypePartContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericTypePart; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterGenericTypePart(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitGenericTypePart(this);
		}
	}

	public final GenericTypePartContext genericTypePart() throws RecognitionException {
		GenericTypePartContext _localctx = new GenericTypePartContext(_ctx, getState());
		enterRule(_localctx, 22, RULE_genericTypePart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(100);
			match(T__6);
			setState(101);
			match(POSNUM);
			setState(102);
			match(T__2);
			setState(103);
			genericParam();
			setState(108);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(104);
				match(T__0);
				setState(105);
				genericParam();
				}
				}
				setState(110);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(111);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class GenericParamContext extends ParserRuleContext {
		public TypeParameterContext typeParameter() {
			return getRuleContext(TypeParameterContext.class,0);
		}
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(TypeNamingParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(TypeNamingParser.WS, i);
		}
		public GenericParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_genericParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterGenericParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitGenericParam(this);
		}
	}

	public final GenericParamContext genericParam() throws RecognitionException {
		GenericParamContext _localctx = new GenericParamContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_genericParam);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(113);
			match(T__2);
			setState(114);
			typeParameter();
			setState(123);
			_la = _input.LA(1);
			if (_la==T__7 || _la==WS) {
				{
				setState(116);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(115);
					match(WS);
					}
				}

				setState(118);
				match(T__7);
				setState(120);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(119);
					match(WS);
					}
				}

				setState(122);
				type();
				}
			}

			setState(125);
			match(T__3);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssemblyContext extends ParserRuleContext {
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public AssemblyVersionContext assemblyVersion() {
			return getRuleContext(AssemblyVersionContext.class,0);
		}
		public TerminalNode WS() { return getToken(TypeNamingParser.WS, 0); }
		public AssemblyContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assembly; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterAssembly(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitAssembly(this);
		}
	}

	public final AssemblyContext assembly() throws RecognitionException {
		AssemblyContext _localctx = new AssemblyContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_assembly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(127);
			id();
			setState(133);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(128);
				match(T__0);
				setState(130);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(129);
					match(WS);
					}
				}

				setState(132);
				assemblyVersion();
				}
			}

			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class AssemblyVersionContext extends ParserRuleContext {
		public List<NumContext> num() {
			return getRuleContexts(NumContext.class);
		}
		public NumContext num(int i) {
			return getRuleContext(NumContext.class,i);
		}
		public AssemblyVersionContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_assemblyVersion; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterAssemblyVersion(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitAssemblyVersion(this);
		}
	}

	public final AssemblyVersionContext assemblyVersion() throws RecognitionException {
		AssemblyVersionContext _localctx = new AssemblyVersionContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_assemblyVersion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(135);
			num();
			setState(136);
			match(T__5);
			setState(137);
			num();
			setState(138);
			match(T__5);
			setState(139);
			num();
			setState(140);
			match(T__5);
			setState(141);
			num();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class MethodContext extends ParserRuleContext {
		public List<TypeContext> type() {
			return getRuleContexts(TypeContext.class);
		}
		public TypeContext type(int i) {
			return getRuleContext(TypeContext.class,i);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(TypeNamingParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(TypeNamingParser.WS, i);
		}
		public List<FormalParamContext> formalParam() {
			return getRuleContexts(FormalParamContext.class);
		}
		public FormalParamContext formalParam(int i) {
			return getRuleContext(FormalParamContext.class,i);
		}
		public MethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_method; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitMethod(this);
		}
	}

	public final MethodContext method() throws RecognitionException {
		MethodContext _localctx = new MethodContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_method);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(143);
			match(T__2);
			setState(144);
			type();
			setState(145);
			match(T__3);
			setState(147);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(146);
				match(WS);
				}
			}

			setState(149);
			match(T__2);
			setState(150);
			type();
			setState(151);
			match(T__8);
			setState(152);
			id();
			setState(153);
			match(T__9);
			setState(155);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(154);
				match(WS);
				}
				break;
			}
			setState(171);
			_la = _input.LA(1);
			if (_la==T__2) {
				{
				setState(157);
				formalParam();
				setState(168);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(159);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(158);
							match(WS);
							}
						}

						setState(161);
						match(T__0);
						setState(163);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(162);
							match(WS);
							}
						}

						setState(165);
						formalParam();
						}
						} 
					}
					setState(170);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				}
				}
			}

			setState(174);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(173);
				match(WS);
				}
			}

			setState(176);
			match(T__10);
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class FormalParamContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public TerminalNode WS() { return getToken(TypeNamingParser.WS, 0); }
		public FormalParamContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_formalParam; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterFormalParam(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitFormalParam(this);
		}
	}

	public final FormalParamContext formalParam() throws RecognitionException {
		FormalParamContext _localctx = new FormalParamContext(_ctx, getState());
		enterRule(_localctx, 32, RULE_formalParam);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(178);
			match(T__2);
			setState(179);
			type();
			setState(180);
			match(T__3);
			setState(182);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(181);
				match(WS);
				}
			}

			setState(184);
			id();
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class IdContext extends ParserRuleContext {
		public List<TerminalNode> LETTER() { return getTokens(TypeNamingParser.LETTER); }
		public TerminalNode LETTER(int i) {
			return getToken(TypeNamingParser.LETTER, i);
		}
		public List<NumContext> num() {
			return getRuleContexts(NumContext.class);
		}
		public NumContext num(int i) {
			return getRuleContext(NumContext.class,i);
		}
		public List<TerminalNode> SIGN() { return getTokens(TypeNamingParser.SIGN); }
		public TerminalNode SIGN(int i) {
			return getToken(TypeNamingParser.SIGN, i);
		}
		public IdContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_id; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterId(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitId(this);
		}
	}

	public final IdContext id() throws RecognitionException {
		IdContext _localctx = new IdContext(_ctx, getState());
		enterRule(_localctx, 34, RULE_id);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(186);
			match(LETTER);
			setState(192);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__11) | (1L << POSNUM) | (1L << LETTER) | (1L << SIGN))) != 0)) {
				{
				setState(190);
				switch (_input.LA(1)) {
				case LETTER:
					{
					setState(187);
					match(LETTER);
					}
					break;
				case T__11:
				case POSNUM:
					{
					setState(188);
					num();
					}
					break;
				case SIGN:
					{
					setState(189);
					match(SIGN);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(194);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static class NumContext extends ParserRuleContext {
		public TerminalNode POSNUM() { return getToken(TypeNamingParser.POSNUM, 0); }
		public NumContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_num; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterNum(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitNum(this);
		}
	}

	public final NumContext num() throws RecognitionException {
		NumContext _localctx = new NumContext(_ctx, getState());
		enterRule(_localctx, 36, RULE_num);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(195);
			_la = _input.LA(1);
			if ( !(_la==T__11 || _la==POSNUM) ) {
			_errHandler.recoverInline(this);
			} else {
				consume();
			}
			}
		}
		catch (RecognitionException re) {
			_localctx.exception = re;
			_errHandler.reportError(this, re);
			_errHandler.recover(this, re);
		}
		finally {
			exitRule();
		}
		return _localctx;
	}

	public static final String _serializedATN =
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\24\u00c8\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\5\4\63\n"+
		"\4\3\4\5\4\66\n\4\5\48\n\4\3\5\3\5\3\6\3\6\3\6\5\6?\n\6\3\6\3\6\3\7\3"+
		"\7\3\7\3\b\3\b\7\bH\n\b\f\b\16\bK\13\b\3\b\3\b\3\t\5\tP\n\t\3\t\3\t\3"+
		"\t\7\tU\n\t\f\t\16\tX\13\t\3\n\3\n\3\n\6\n]\n\n\r\n\16\n^\3\13\3\13\5"+
		"\13c\n\13\3\f\3\f\3\r\3\r\3\r\3\r\3\r\3\r\7\rm\n\r\f\r\16\rp\13\r\3\r"+
		"\3\r\3\16\3\16\3\16\5\16w\n\16\3\16\3\16\5\16{\n\16\3\16\5\16~\n\16\3"+
		"\16\3\16\3\17\3\17\3\17\5\17\u0085\n\17\3\17\5\17\u0088\n\17\3\20\3\20"+
		"\3\20\3\20\3\20\3\20\3\20\3\20\3\21\3\21\3\21\3\21\5\21\u0096\n\21\3\21"+
		"\3\21\3\21\3\21\3\21\3\21\5\21\u009e\n\21\3\21\3\21\5\21\u00a2\n\21\3"+
		"\21\3\21\5\21\u00a6\n\21\3\21\7\21\u00a9\n\21\f\21\16\21\u00ac\13\21\5"+
		"\21\u00ae\n\21\3\21\5\21\u00b1\n\21\3\21\3\21\3\22\3\22\3\22\3\22\5\22"+
		"\u00b9\n\22\3\22\3\22\3\23\3\23\3\23\3\23\7\23\u00c1\n\23\f\23\16\23\u00c4"+
		"\13\23\3\24\3\24\3\24\2\2\25\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36 \""+
		"$&\2\3\4\2\16\16\20\20\u00cf\2(\3\2\2\2\4+\3\2\2\2\6\67\3\2\2\2\b9\3\2"+
		"\2\2\n;\3\2\2\2\fB\3\2\2\2\16E\3\2\2\2\20O\3\2\2\2\22\\\3\2\2\2\24`\3"+
		"\2\2\2\26d\3\2\2\2\30f\3\2\2\2\32s\3\2\2\2\34\u0081\3\2\2\2\36\u0089\3"+
		"\2\2\2 \u0091\3\2\2\2\"\u00b4\3\2\2\2$\u00bc\3\2\2\2&\u00c5\3\2\2\2()"+
		"\5\6\4\2)*\7\24\2\2*\3\3\2\2\2+,\5 \21\2,-\7\24\2\2-\5\3\2\2\2.8\7\17"+
		"\2\2/\63\5\b\5\2\60\63\5\n\6\2\61\63\5\f\7\2\62/\3\2\2\2\62\60\3\2\2\2"+
		"\62\61\3\2\2\2\63\65\3\2\2\2\64\66\5\16\b\2\65\64\3\2\2\2\65\66\3\2\2"+
		"\2\668\3\2\2\2\67.\3\2\2\2\67\62\3\2\2\28\7\3\2\2\29:\5$\23\2:\t\3\2\2"+
		"\2;<\5\20\t\2<>\7\3\2\2=?\7\23\2\2>=\3\2\2\2>?\3\2\2\2?@\3\2\2\2@A\5\34"+
		"\17\2A\13\3\2\2\2BC\7\4\2\2CD\5 \21\2D\r\3\2\2\2EI\7\5\2\2FH\7\3\2\2G"+
		"F\3\2\2\2HK\3\2\2\2IG\3\2\2\2IJ\3\2\2\2JL\3\2\2\2KI\3\2\2\2LM\7\6\2\2"+
		"M\17\3\2\2\2NP\5\22\n\2ON\3\2\2\2OP\3\2\2\2PQ\3\2\2\2QV\5\24\13\2RS\7"+
		"\7\2\2SU\5\24\13\2TR\3\2\2\2UX\3\2\2\2VT\3\2\2\2VW\3\2\2\2W\21\3\2\2\2"+
		"XV\3\2\2\2YZ\5$\23\2Z[\7\b\2\2[]\3\2\2\2\\Y\3\2\2\2]^\3\2\2\2^\\\3\2\2"+
		"\2^_\3\2\2\2_\23\3\2\2\2`b\5\26\f\2ac\5\30\r\2ba\3\2\2\2bc\3\2\2\2c\25"+
		"\3\2\2\2de\5$\23\2e\27\3\2\2\2fg\7\t\2\2gh\7\20\2\2hi\7\5\2\2in\5\32\16"+
		"\2jk\7\3\2\2km\5\32\16\2lj\3\2\2\2mp\3\2\2\2nl\3\2\2\2no\3\2\2\2oq\3\2"+
		"\2\2pn\3\2\2\2qr\7\6\2\2r\31\3\2\2\2st\7\5\2\2t}\5\b\5\2uw\7\23\2\2vu"+
		"\3\2\2\2vw\3\2\2\2wx\3\2\2\2xz\7\n\2\2y{\7\23\2\2zy\3\2\2\2z{\3\2\2\2"+
		"{|\3\2\2\2|~\5\6\4\2}v\3\2\2\2}~\3\2\2\2~\177\3\2\2\2\177\u0080\7\6\2"+
		"\2\u0080\33\3\2\2\2\u0081\u0087\5$\23\2\u0082\u0084\7\3\2\2\u0083\u0085"+
		"\7\23\2\2\u0084\u0083\3\2\2\2\u0084\u0085\3\2\2\2\u0085\u0086\3\2\2\2"+
		"\u0086\u0088\5\36\20\2\u0087\u0082\3\2\2\2\u0087\u0088\3\2\2\2\u0088\35"+
		"\3\2\2\2\u0089\u008a\5&\24\2\u008a\u008b\7\b\2\2\u008b\u008c\5&\24\2\u008c"+
		"\u008d\7\b\2\2\u008d\u008e\5&\24\2\u008e\u008f\7\b\2\2\u008f\u0090\5&"+
		"\24\2\u0090\37\3\2\2\2\u0091\u0092\7\5\2\2\u0092\u0093\5\6\4\2\u0093\u0095"+
		"\7\6\2\2\u0094\u0096\7\23\2\2\u0095\u0094\3\2\2\2\u0095\u0096\3\2\2\2"+
		"\u0096\u0097\3\2\2\2\u0097\u0098\7\5\2\2\u0098\u0099\5\6\4\2\u0099\u009a"+
		"\7\13\2\2\u009a\u009b\5$\23\2\u009b\u009d\7\f\2\2\u009c\u009e\7\23\2\2"+
		"\u009d\u009c\3\2\2\2\u009d\u009e\3\2\2\2\u009e\u00ad\3\2\2\2\u009f\u00aa"+
		"\5\"\22\2\u00a0\u00a2\7\23\2\2\u00a1\u00a0\3\2\2\2\u00a1\u00a2\3\2\2\2"+
		"\u00a2\u00a3\3\2\2\2\u00a3\u00a5\7\3\2\2\u00a4\u00a6\7\23\2\2\u00a5\u00a4"+
		"\3\2\2\2\u00a5\u00a6\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a9\5\"\22\2"+
		"\u00a8\u00a1\3\2\2\2\u00a9\u00ac\3\2\2\2\u00aa\u00a8\3\2\2\2\u00aa\u00ab"+
		"\3\2\2\2\u00ab\u00ae\3\2\2\2\u00ac\u00aa\3\2\2\2\u00ad\u009f\3\2\2\2\u00ad"+
		"\u00ae\3\2\2\2\u00ae\u00b0\3\2\2\2\u00af\u00b1\7\23\2\2\u00b0\u00af\3"+
		"\2\2\2\u00b0\u00b1\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2\u00b3\7\r\2\2\u00b3"+
		"!\3\2\2\2\u00b4\u00b5\7\5\2\2\u00b5\u00b6\5\6\4\2\u00b6\u00b8\7\6\2\2"+
		"\u00b7\u00b9\7\23\2\2\u00b8\u00b7\3\2\2\2\u00b8\u00b9\3\2\2\2\u00b9\u00ba"+
		"\3\2\2\2\u00ba\u00bb\5$\23\2\u00bb#\3\2\2\2\u00bc\u00c2\7\21\2\2\u00bd"+
		"\u00c1\7\21\2\2\u00be\u00c1\5&\24\2\u00bf\u00c1\7\22\2\2\u00c0\u00bd\3"+
		"\2\2\2\u00c0\u00be\3\2\2\2\u00c0\u00bf\3\2\2\2\u00c1\u00c4\3\2\2\2\u00c2"+
		"\u00c0\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3%\3\2\2\2\u00c4\u00c2\3\2\2\2"+
		"\u00c5\u00c6\t\2\2\2\u00c6\'\3\2\2\2\33\62\65\67>IOV^bnvz}\u0084\u0087"+
		"\u0095\u009d\u00a1\u00a5\u00aa\u00ad\u00b0\u00b8\u00c0\u00c2";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}