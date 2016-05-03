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
		T__9=10, T__10=11, T__11=12, T__12=13, T__13=14, T__14=15, T__15=16, T__16=17, 
		T__17=18, T__18=19, T__19=20, T__20=21, T__21=22, T__22=23, T__23=24, 
		UNKNOWN=25, POSNUM=26, LETTER=27, SIGN=28, WS=29, EOL=30;
	public static final int
		RULE_typeEOL = 0, RULE_methodEOL = 1, RULE_type = 2, RULE_typeParameter = 3, 
		RULE_regularType = 4, RULE_delegateType = 5, RULE_arrayType = 6, RULE_nestedType = 7, 
		RULE_nestedTypeName = 8, RULE_resolvedType = 9, RULE_namespace = 10, RULE_typeName = 11, 
		RULE_possiblyGenericTypeName = 12, RULE_enumTypeName = 13, RULE_interfaceTypeName = 14, 
		RULE_structTypeName = 15, RULE_simpleTypeName = 16, RULE_genericTypePart = 17, 
		RULE_genericParam = 18, RULE_boundTypeParameter = 19, RULE_assembly = 20, 
		RULE_assemblyVersion = 21, RULE_method = 22, RULE_constructor = 23, RULE_customMethod = 24, 
		RULE_formalParam = 25, RULE_staticModifier = 26, RULE_paramsModifier = 27, 
		RULE_optsModifier = 28, RULE_refModifier = 29, RULE_id = 30, RULE_num = 31;
	public static final String[] ruleNames = {
		"typeEOL", "methodEOL", "type", "typeParameter", "regularType", "delegateType", 
		"arrayType", "nestedType", "nestedTypeName", "resolvedType", "namespace", 
		"typeName", "possiblyGenericTypeName", "enumTypeName", "interfaceTypeName", 
		"structTypeName", "simpleTypeName", "genericTypePart", "genericParam", 
		"boundTypeParameter", "assembly", "assemblyVersion", "method", "constructor", 
		"customMethod", "formalParam", "staticModifier", "paramsModifier", "optsModifier", 
		"refModifier", "id", "num"
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
			setState(64);
			type();
			setState(65);
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
			setState(67);
			method();
			setState(68);
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
		public ArrayTypeContext arrayType() {
			return getRuleContext(ArrayTypeContext.class,0);
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
		try {
			setState(75);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(70);
				match(UNKNOWN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(71);
				typeParameter();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(72);
				regularType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(73);
				delegateType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(74);
				arrayType();
				}
				break;
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
			setState(77);
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
		public AssemblyContext assembly() {
			return getRuleContext(AssemblyContext.class,0);
		}
		public ResolvedTypeContext resolvedType() {
			return getRuleContext(ResolvedTypeContext.class,0);
		}
		public NestedTypeContext nestedType() {
			return getRuleContext(NestedTypeContext.class,0);
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
			setState(81);
			switch (_input.LA(1)) {
			case T__7:
			case T__8:
			case T__9:
			case LETTER:
				{
				setState(79);
				resolvedType();
				}
				break;
			case T__4:
				{
				setState(80);
				nestedType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(83);
			match(T__0);
			setState(85);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(84);
				match(WS);
				}
			}

			setState(87);
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
			setState(89);
			match(T__1);
			setState(90);
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

	public static class ArrayTypeContext extends ParserRuleContext {
		public TerminalNode POSNUM() { return getToken(TypeNamingParser.POSNUM, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ArrayTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_arrayType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterArrayType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitArrayType(this);
		}
	}

	public final ArrayTypeContext arrayType() throws RecognitionException {
		ArrayTypeContext _localctx = new ArrayTypeContext(_ctx, getState());
		enterRule(_localctx, 12, RULE_arrayType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(92);
			match(T__2);
			setState(93);
			match(POSNUM);
			setState(94);
			match(T__3);
			setState(95);
			type();
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

	public static class NestedTypeContext extends ParserRuleContext {
		public NestedTypeNameContext nestedTypeName() {
			return getRuleContext(NestedTypeNameContext.class,0);
		}
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
		}
		public NestedTypeContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedType; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterNestedType(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitNestedType(this);
		}
	}

	public final NestedTypeContext nestedType() throws RecognitionException {
		NestedTypeContext _localctx = new NestedTypeContext(_ctx, getState());
		enterRule(_localctx, 14, RULE_nestedType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(97);
			match(T__4);
			setState(98);
			nestedTypeName();
			setState(99);
			match(T__5);
			setState(100);
			typeName();
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

	public static class NestedTypeNameContext extends ParserRuleContext {
		public NestedTypeContext nestedType() {
			return getRuleContext(NestedTypeContext.class,0);
		}
		public ResolvedTypeContext resolvedType() {
			return getRuleContext(ResolvedTypeContext.class,0);
		}
		public NestedTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nestedTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterNestedTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitNestedTypeName(this);
		}
	}

	public final NestedTypeNameContext nestedTypeName() throws RecognitionException {
		NestedTypeNameContext _localctx = new NestedTypeNameContext(_ctx, getState());
		enterRule(_localctx, 16, RULE_nestedTypeName);
		try {
			setState(104);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(102);
				nestedType();
				}
				break;
			case T__7:
			case T__8:
			case T__9:
			case LETTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(103);
				resolvedType();
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

	public static class ResolvedTypeContext extends ParserRuleContext {
		public TypeNameContext typeName() {
			return getRuleContext(TypeNameContext.class,0);
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
		enterRule(_localctx, 18, RULE_resolvedType);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(107);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(106);
				namespace();
				}
				break;
			}
			setState(109);
			typeName();
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
		enterRule(_localctx, 20, RULE_namespace);
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(114); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(111);
					id();
					setState(112);
					match(T__6);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(116); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,5,_ctx);
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
		public EnumTypeNameContext enumTypeName() {
			return getRuleContext(EnumTypeNameContext.class,0);
		}
		public PossiblyGenericTypeNameContext possiblyGenericTypeName() {
			return getRuleContext(PossiblyGenericTypeNameContext.class,0);
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
		enterRule(_localctx, 22, RULE_typeName);
		try {
			setState(120);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(118);
				enumTypeName();
				}
				break;
			case T__8:
			case T__9:
			case LETTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(119);
				possiblyGenericTypeName();
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

	public static class PossiblyGenericTypeNameContext extends ParserRuleContext {
		public InterfaceTypeNameContext interfaceTypeName() {
			return getRuleContext(InterfaceTypeNameContext.class,0);
		}
		public StructTypeNameContext structTypeName() {
			return getRuleContext(StructTypeNameContext.class,0);
		}
		public SimpleTypeNameContext simpleTypeName() {
			return getRuleContext(SimpleTypeNameContext.class,0);
		}
		public GenericTypePartContext genericTypePart() {
			return getRuleContext(GenericTypePartContext.class,0);
		}
		public PossiblyGenericTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_possiblyGenericTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterPossiblyGenericTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitPossiblyGenericTypeName(this);
		}
	}

	public final PossiblyGenericTypeNameContext possiblyGenericTypeName() throws RecognitionException {
		PossiblyGenericTypeNameContext _localctx = new PossiblyGenericTypeNameContext(_ctx, getState());
		enterRule(_localctx, 24, RULE_possiblyGenericTypeName);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(125);
			switch (_input.LA(1)) {
			case T__8:
				{
				setState(122);
				interfaceTypeName();
				}
				break;
			case T__9:
				{
				setState(123);
				structTypeName();
				}
				break;
			case LETTER:
				{
				setState(124);
				simpleTypeName();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(128);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(127);
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

	public static class EnumTypeNameContext extends ParserRuleContext {
		public SimpleTypeNameContext simpleTypeName() {
			return getRuleContext(SimpleTypeNameContext.class,0);
		}
		public EnumTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_enumTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterEnumTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitEnumTypeName(this);
		}
	}

	public final EnumTypeNameContext enumTypeName() throws RecognitionException {
		EnumTypeNameContext _localctx = new EnumTypeNameContext(_ctx, getState());
		enterRule(_localctx, 26, RULE_enumTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(130);
			match(T__7);
			setState(131);
			simpleTypeName();
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

	public static class InterfaceTypeNameContext extends ParserRuleContext {
		public SimpleTypeNameContext simpleTypeName() {
			return getRuleContext(SimpleTypeNameContext.class,0);
		}
		public InterfaceTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_interfaceTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterInterfaceTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitInterfaceTypeName(this);
		}
	}

	public final InterfaceTypeNameContext interfaceTypeName() throws RecognitionException {
		InterfaceTypeNameContext _localctx = new InterfaceTypeNameContext(_ctx, getState());
		enterRule(_localctx, 28, RULE_interfaceTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(133);
			match(T__8);
			setState(134);
			simpleTypeName();
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

	public static class StructTypeNameContext extends ParserRuleContext {
		public SimpleTypeNameContext simpleTypeName() {
			return getRuleContext(SimpleTypeNameContext.class,0);
		}
		public StructTypeNameContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_structTypeName; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterStructTypeName(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitStructTypeName(this);
		}
	}

	public final StructTypeNameContext structTypeName() throws RecognitionException {
		StructTypeNameContext _localctx = new StructTypeNameContext(_ctx, getState());
		enterRule(_localctx, 30, RULE_structTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(136);
			match(T__9);
			setState(137);
			simpleTypeName();
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
		enterRule(_localctx, 32, RULE_simpleTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(139);
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
		enterRule(_localctx, 34, RULE_genericTypePart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(141);
			match(T__10);
			setState(142);
			match(POSNUM);
			setState(143);
			match(T__11);
			setState(144);
			genericParam();
			setState(149);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(145);
				match(T__0);
				setState(146);
				genericParam();
				}
				}
				setState(151);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(152);
			match(T__12);
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
		public BoundTypeParameterContext boundTypeParameter() {
			return getRuleContext(BoundTypeParameterContext.class,0);
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
		enterRule(_localctx, 36, RULE_genericParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(154);
			match(T__11);
			setState(155);
			boundTypeParameter();
			setState(156);
			match(T__12);
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

	public static class BoundTypeParameterContext extends ParserRuleContext {
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
		public BoundTypeParameterContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_boundTypeParameter; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterBoundTypeParameter(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitBoundTypeParameter(this);
		}
	}

	public final BoundTypeParameterContext boundTypeParameter() throws RecognitionException {
		BoundTypeParameterContext _localctx = new BoundTypeParameterContext(_ctx, getState());
		enterRule(_localctx, 38, RULE_boundTypeParameter);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(158);
			typeParameter();
			setState(167);
			_la = _input.LA(1);
			if (_la==T__13 || _la==WS) {
				{
				setState(160);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(159);
					match(WS);
					}
				}

				setState(162);
				match(T__13);
				setState(164);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(163);
					match(WS);
					}
				}

				setState(166);
				type();
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
		enterRule(_localctx, 40, RULE_assembly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(169);
			id();
			setState(175);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(170);
				match(T__0);
				setState(172);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(171);
					match(WS);
					}
				}

				setState(174);
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
		enterRule(_localctx, 42, RULE_assemblyVersion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(177);
			num();
			setState(178);
			match(T__6);
			setState(179);
			num();
			setState(180);
			match(T__6);
			setState(181);
			num();
			setState(182);
			match(T__6);
			setState(183);
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
		public ConstructorContext constructor() {
			return getRuleContext(ConstructorContext.class,0);
		}
		public CustomMethodContext customMethod() {
			return getRuleContext(CustomMethodContext.class,0);
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
		enterRule(_localctx, 44, RULE_method);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(187);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(185);
				constructor();
				}
				break;
			case 2:
				{
				setState(186);
				customMethod();
				}
				break;
			}
			setState(189);
			match(T__14);
			setState(191);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(190);
				match(WS);
				}
				break;
			}
			setState(207);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,20,_ctx) ) {
			case 1:
				{
				setState(193);
				formalParam();
				setState(204);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(195);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(194);
							match(WS);
							}
						}

						setState(197);
						match(T__0);
						setState(199);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,18,_ctx) ) {
						case 1:
							{
							setState(198);
							match(WS);
							}
							break;
						}
						setState(201);
						formalParam();
						}
						} 
					}
					setState(206);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,19,_ctx);
				}
				}
				break;
			}
			setState(210);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(209);
				match(WS);
				}
			}

			setState(212);
			match(T__15);
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

	public static class ConstructorContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public ConstructorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_constructor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterConstructor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitConstructor(this);
		}
	}

	public final ConstructorContext constructor() throws RecognitionException {
		ConstructorContext _localctx = new ConstructorContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_constructor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(214);
			match(T__11);
			setState(215);
			type();
			setState(216);
			match(T__16);
			setState(217);
			_la = _input.LA(1);
			if ( !(_la==T__17 || _la==T__18) ) {
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

	public static class CustomMethodContext extends ParserRuleContext {
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
		public StaticModifierContext staticModifier() {
			return getRuleContext(StaticModifierContext.class,0);
		}
		public GenericTypePartContext genericTypePart() {
			return getRuleContext(GenericTypePartContext.class,0);
		}
		public CustomMethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_customMethod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterCustomMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitCustomMethod(this);
		}
	}

	public final CustomMethodContext customMethod() throws RecognitionException {
		CustomMethodContext _localctx = new CustomMethodContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_customMethod);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(219);
			match(T__11);
			setState(220);
			type();
			setState(221);
			match(T__12);
			setState(223);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,22,_ctx) ) {
			case 1:
				{
				setState(222);
				match(WS);
				}
				break;
			}
			setState(226);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(225);
				staticModifier();
				}
			}

			setState(229);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(228);
				match(WS);
				}
			}

			setState(231);
			match(T__11);
			setState(232);
			type();
			setState(233);
			match(T__16);
			setState(234);
			id();
			setState(236);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(235);
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

	public static class FormalParamContext extends ParserRuleContext {
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public IdContext id() {
			return getRuleContext(IdContext.class,0);
		}
		public ParamsModifierContext paramsModifier() {
			return getRuleContext(ParamsModifierContext.class,0);
		}
		public OptsModifierContext optsModifier() {
			return getRuleContext(OptsModifierContext.class,0);
		}
		public RefModifierContext refModifier() {
			return getRuleContext(RefModifierContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(TypeNamingParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(TypeNamingParser.WS, i);
		}
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
		enterRule(_localctx, 50, RULE_formalParam);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(241);
			switch (_input.LA(1)) {
			case T__20:
				{
				setState(238);
				paramsModifier();
				}
				break;
			case T__21:
				{
				setState(239);
				optsModifier();
				}
				break;
			case T__22:
				{
				setState(240);
				refModifier();
				}
				break;
			case T__11:
			case WS:
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(244);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(243);
				match(WS);
				}
			}

			setState(246);
			match(T__11);
			setState(247);
			type();
			setState(248);
			match(T__12);
			setState(250);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(249);
				match(WS);
				}
			}

			setState(252);
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

	public static class StaticModifierContext extends ParserRuleContext {
		public StaticModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_staticModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterStaticModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitStaticModifier(this);
		}
	}

	public final StaticModifierContext staticModifier() throws RecognitionException {
		StaticModifierContext _localctx = new StaticModifierContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_staticModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(254);
			match(T__19);
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

	public static class ParamsModifierContext extends ParserRuleContext {
		public ParamsModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_paramsModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterParamsModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitParamsModifier(this);
		}
	}

	public final ParamsModifierContext paramsModifier() throws RecognitionException {
		ParamsModifierContext _localctx = new ParamsModifierContext(_ctx, getState());
		enterRule(_localctx, 54, RULE_paramsModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(256);
			match(T__20);
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

	public static class OptsModifierContext extends ParserRuleContext {
		public OptsModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_optsModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterOptsModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitOptsModifier(this);
		}
	}

	public final OptsModifierContext optsModifier() throws RecognitionException {
		OptsModifierContext _localctx = new OptsModifierContext(_ctx, getState());
		enterRule(_localctx, 56, RULE_optsModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(258);
			match(T__21);
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

	public static class RefModifierContext extends ParserRuleContext {
		public RefModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_refModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterRefModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitRefModifier(this);
		}
	}

	public final RefModifierContext refModifier() throws RecognitionException {
		RefModifierContext _localctx = new RefModifierContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_refModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(260);
			match(T__22);
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
		enterRule(_localctx, 60, RULE_id);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(262);
			match(LETTER);
			setState(268);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__23) | (1L << POSNUM) | (1L << LETTER) | (1L << SIGN))) != 0)) {
				{
				setState(266);
				switch (_input.LA(1)) {
				case LETTER:
					{
					setState(263);
					match(LETTER);
					}
					break;
				case T__23:
				case POSNUM:
					{
					setState(264);
					num();
					}
					break;
				case SIGN:
					{
					setState(265);
					match(SIGN);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(270);
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
		enterRule(_localctx, 62, RULE_num);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(271);
			_la = _input.LA(1);
			if ( !(_la==T__23 || _la==POSNUM) ) {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3 \u0114\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\5\4N\n\4\3\5\3\5\3\6\3"+
		"\6\5\6T\n\6\3\6\3\6\5\6X\n\6\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3"+
		"\t\3\t\3\t\3\t\3\t\3\n\3\n\5\nk\n\n\3\13\5\13n\n\13\3\13\3\13\3\f\3\f"+
		"\3\f\6\fu\n\f\r\f\16\fv\3\r\3\r\5\r{\n\r\3\16\3\16\3\16\5\16\u0080\n\16"+
		"\3\16\5\16\u0083\n\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22"+
		"\3\22\3\23\3\23\3\23\3\23\3\23\3\23\7\23\u0096\n\23\f\23\16\23\u0099\13"+
		"\23\3\23\3\23\3\24\3\24\3\24\3\24\3\25\3\25\5\25\u00a3\n\25\3\25\3\25"+
		"\5\25\u00a7\n\25\3\25\5\25\u00aa\n\25\3\26\3\26\3\26\5\26\u00af\n\26\3"+
		"\26\5\26\u00b2\n\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30"+
		"\5\30\u00be\n\30\3\30\3\30\5\30\u00c2\n\30\3\30\3\30\5\30\u00c6\n\30\3"+
		"\30\3\30\5\30\u00ca\n\30\3\30\7\30\u00cd\n\30\f\30\16\30\u00d0\13\30\5"+
		"\30\u00d2\n\30\3\30\5\30\u00d5\n\30\3\30\3\30\3\31\3\31\3\31\3\31\3\31"+
		"\3\32\3\32\3\32\3\32\5\32\u00e2\n\32\3\32\5\32\u00e5\n\32\3\32\5\32\u00e8"+
		"\n\32\3\32\3\32\3\32\3\32\3\32\5\32\u00ef\n\32\3\33\3\33\3\33\5\33\u00f4"+
		"\n\33\3\33\5\33\u00f7\n\33\3\33\3\33\3\33\3\33\5\33\u00fd\n\33\3\33\3"+
		"\33\3\34\3\34\3\35\3\35\3\36\3\36\3\37\3\37\3 \3 \3 \3 \7 \u010d\n \f"+
		" \16 \u0110\13 \3!\3!\3!\2\2\"\2\4\6\b\n\f\16\20\22\24\26\30\32\34\36"+
		" \"$&(*,.\60\62\64\668:<>@\2\4\3\2\24\25\4\2\32\32\34\34\u0119\2B\3\2"+
		"\2\2\4E\3\2\2\2\6M\3\2\2\2\bO\3\2\2\2\nS\3\2\2\2\f[\3\2\2\2\16^\3\2\2"+
		"\2\20c\3\2\2\2\22j\3\2\2\2\24m\3\2\2\2\26t\3\2\2\2\30z\3\2\2\2\32\177"+
		"\3\2\2\2\34\u0084\3\2\2\2\36\u0087\3\2\2\2 \u008a\3\2\2\2\"\u008d\3\2"+
		"\2\2$\u008f\3\2\2\2&\u009c\3\2\2\2(\u00a0\3\2\2\2*\u00ab\3\2\2\2,\u00b3"+
		"\3\2\2\2.\u00bd\3\2\2\2\60\u00d8\3\2\2\2\62\u00dd\3\2\2\2\64\u00f3\3\2"+
		"\2\2\66\u0100\3\2\2\28\u0102\3\2\2\2:\u0104\3\2\2\2<\u0106\3\2\2\2>\u0108"+
		"\3\2\2\2@\u0111\3\2\2\2BC\5\6\4\2CD\7 \2\2D\3\3\2\2\2EF\5.\30\2FG\7 \2"+
		"\2G\5\3\2\2\2HN\7\33\2\2IN\5\b\5\2JN\5\n\6\2KN\5\f\7\2LN\5\16\b\2MH\3"+
		"\2\2\2MI\3\2\2\2MJ\3\2\2\2MK\3\2\2\2ML\3\2\2\2N\7\3\2\2\2OP\5> \2P\t\3"+
		"\2\2\2QT\5\24\13\2RT\5\20\t\2SQ\3\2\2\2SR\3\2\2\2TU\3\2\2\2UW\7\3\2\2"+
		"VX\7\37\2\2WV\3\2\2\2WX\3\2\2\2XY\3\2\2\2YZ\5*\26\2Z\13\3\2\2\2[\\\7\4"+
		"\2\2\\]\5.\30\2]\r\3\2\2\2^_\7\5\2\2_`\7\34\2\2`a\7\6\2\2ab\5\6\4\2b\17"+
		"\3\2\2\2cd\7\7\2\2de\5\22\n\2ef\7\b\2\2fg\5\30\r\2g\21\3\2\2\2hk\5\20"+
		"\t\2ik\5\24\13\2jh\3\2\2\2ji\3\2\2\2k\23\3\2\2\2ln\5\26\f\2ml\3\2\2\2"+
		"mn\3\2\2\2no\3\2\2\2op\5\30\r\2p\25\3\2\2\2qr\5> \2rs\7\t\2\2su\3\2\2"+
		"\2tq\3\2\2\2uv\3\2\2\2vt\3\2\2\2vw\3\2\2\2w\27\3\2\2\2x{\5\34\17\2y{\5"+
		"\32\16\2zx\3\2\2\2zy\3\2\2\2{\31\3\2\2\2|\u0080\5\36\20\2}\u0080\5 \21"+
		"\2~\u0080\5\"\22\2\177|\3\2\2\2\177}\3\2\2\2\177~\3\2\2\2\u0080\u0082"+
		"\3\2\2\2\u0081\u0083\5$\23\2\u0082\u0081\3\2\2\2\u0082\u0083\3\2\2\2\u0083"+
		"\33\3\2\2\2\u0084\u0085\7\n\2\2\u0085\u0086\5\"\22\2\u0086\35\3\2\2\2"+
		"\u0087\u0088\7\13\2\2\u0088\u0089\5\"\22\2\u0089\37\3\2\2\2\u008a\u008b"+
		"\7\f\2\2\u008b\u008c\5\"\22\2\u008c!\3\2\2\2\u008d\u008e\5> \2\u008e#"+
		"\3\2\2\2\u008f\u0090\7\r\2\2\u0090\u0091\7\34\2\2\u0091\u0092\7\16\2\2"+
		"\u0092\u0097\5&\24\2\u0093\u0094\7\3\2\2\u0094\u0096\5&\24\2\u0095\u0093"+
		"\3\2\2\2\u0096\u0099\3\2\2\2\u0097\u0095\3\2\2\2\u0097\u0098\3\2\2\2\u0098"+
		"\u009a\3\2\2\2\u0099\u0097\3\2\2\2\u009a\u009b\7\17\2\2\u009b%\3\2\2\2"+
		"\u009c\u009d\7\16\2\2\u009d\u009e\5(\25\2\u009e\u009f\7\17\2\2\u009f\'"+
		"\3\2\2\2\u00a0\u00a9\5\b\5\2\u00a1\u00a3\7\37\2\2\u00a2\u00a1\3\2\2\2"+
		"\u00a2\u00a3\3\2\2\2\u00a3\u00a4\3\2\2\2\u00a4\u00a6\7\20\2\2\u00a5\u00a7"+
		"\7\37\2\2\u00a6\u00a5\3\2\2\2\u00a6\u00a7\3\2\2\2\u00a7\u00a8\3\2\2\2"+
		"\u00a8\u00aa\5\6\4\2\u00a9\u00a2\3\2\2\2\u00a9\u00aa\3\2\2\2\u00aa)\3"+
		"\2\2\2\u00ab\u00b1\5> \2\u00ac\u00ae\7\3\2\2\u00ad\u00af\7\37\2\2\u00ae"+
		"\u00ad\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\5,"+
		"\27\2\u00b1\u00ac\3\2\2\2\u00b1\u00b2\3\2\2\2\u00b2+\3\2\2\2\u00b3\u00b4"+
		"\5@!\2\u00b4\u00b5\7\t\2\2\u00b5\u00b6\5@!\2\u00b6\u00b7\7\t\2\2\u00b7"+
		"\u00b8\5@!\2\u00b8\u00b9\7\t\2\2\u00b9\u00ba\5@!\2\u00ba-\3\2\2\2\u00bb"+
		"\u00be\5\60\31\2\u00bc\u00be\5\62\32\2\u00bd\u00bb\3\2\2\2\u00bd\u00bc"+
		"\3\2\2\2\u00be\u00bf\3\2\2\2\u00bf\u00c1\7\21\2\2\u00c0\u00c2\7\37\2\2"+
		"\u00c1\u00c0\3\2\2\2\u00c1\u00c2\3\2\2\2\u00c2\u00d1\3\2\2\2\u00c3\u00ce"+
		"\5\64\33\2\u00c4\u00c6\7\37\2\2\u00c5\u00c4\3\2\2\2\u00c5\u00c6\3\2\2"+
		"\2\u00c6\u00c7\3\2\2\2\u00c7\u00c9\7\3\2\2\u00c8\u00ca\7\37\2\2\u00c9"+
		"\u00c8\3\2\2\2\u00c9\u00ca\3\2\2\2\u00ca\u00cb\3\2\2\2\u00cb\u00cd\5\64"+
		"\33\2\u00cc\u00c5\3\2\2\2\u00cd\u00d0\3\2\2\2\u00ce\u00cc\3\2\2\2\u00ce"+
		"\u00cf\3\2\2\2\u00cf\u00d2\3\2\2\2\u00d0\u00ce\3\2\2\2\u00d1\u00c3\3\2"+
		"\2\2\u00d1\u00d2\3\2\2\2\u00d2\u00d4\3\2\2\2\u00d3\u00d5\7\37\2\2\u00d4"+
		"\u00d3\3\2\2\2\u00d4\u00d5\3\2\2\2\u00d5\u00d6\3\2\2\2\u00d6\u00d7\7\22"+
		"\2\2\u00d7/\3\2\2\2\u00d8\u00d9\7\16\2\2\u00d9\u00da\5\6\4\2\u00da\u00db"+
		"\7\23\2\2\u00db\u00dc\t\2\2\2\u00dc\61\3\2\2\2\u00dd\u00de\7\16\2\2\u00de"+
		"\u00df\5\6\4\2\u00df\u00e1\7\17\2\2\u00e0\u00e2\7\37\2\2\u00e1\u00e0\3"+
		"\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e4\3\2\2\2\u00e3\u00e5\5\66\34\2\u00e4"+
		"\u00e3\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5\u00e7\3\2\2\2\u00e6\u00e8\7\37"+
		"\2\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2\2\2\u00e8\u00e9\3\2\2\2\u00e9"+
		"\u00ea\7\16\2\2\u00ea\u00eb\5\6\4\2\u00eb\u00ec\7\23\2\2\u00ec\u00ee\5"+
		"> \2\u00ed\u00ef\5$\23\2\u00ee\u00ed\3\2\2\2\u00ee\u00ef\3\2\2\2\u00ef"+
		"\63\3\2\2\2\u00f0\u00f4\58\35\2\u00f1\u00f4\5:\36\2\u00f2\u00f4\5<\37"+
		"\2\u00f3\u00f0\3\2\2\2\u00f3\u00f1\3\2\2\2\u00f3\u00f2\3\2\2\2\u00f3\u00f4"+
		"\3\2\2\2\u00f4\u00f6\3\2\2\2\u00f5\u00f7\7\37\2\2\u00f6\u00f5\3\2\2\2"+
		"\u00f6\u00f7\3\2\2\2\u00f7\u00f8\3\2\2\2\u00f8\u00f9\7\16\2\2\u00f9\u00fa"+
		"\5\6\4\2\u00fa\u00fc\7\17\2\2\u00fb\u00fd\7\37\2\2\u00fc\u00fb\3\2\2\2"+
		"\u00fc\u00fd\3\2\2\2\u00fd\u00fe\3\2\2\2\u00fe\u00ff\5> \2\u00ff\65\3"+
		"\2\2\2\u0100\u0101\7\26\2\2\u0101\67\3\2\2\2\u0102\u0103\7\27\2\2\u0103"+
		"9\3\2\2\2\u0104\u0105\7\30\2\2\u0105;\3\2\2\2\u0106\u0107\7\31\2\2\u0107"+
		"=\3\2\2\2\u0108\u010e\7\35\2\2\u0109\u010d\7\35\2\2\u010a\u010d\5@!\2"+
		"\u010b\u010d\7\36\2\2\u010c\u0109\3\2\2\2\u010c\u010a\3\2\2\2\u010c\u010b"+
		"\3\2\2\2\u010d\u0110\3\2\2\2\u010e\u010c\3\2\2\2\u010e\u010f\3\2\2\2\u010f"+
		"?\3\2\2\2\u0110\u010e\3\2\2\2\u0111\u0112\t\3\2\2\u0112A\3\2\2\2!MSWj"+
		"mvz\177\u0082\u0097\u00a2\u00a6\u00a9\u00ae\u00b1\u00bd\u00c1\u00c5\u00c9"+
		"\u00ce\u00d1\u00d4\u00e1\u00e4\u00e7\u00ee\u00f3\u00f6\u00fc\u010c\u010e";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}