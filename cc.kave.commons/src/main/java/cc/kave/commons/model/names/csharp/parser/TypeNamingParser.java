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
		T__24=25, T__25=26, UNKNOWN=27, POSNUM=28, LETTER=29, SIGN=30, WS=31, 
		EOL=32;
	public static final int
		RULE_typeEOL = 0, RULE_methodEOL = 1, RULE_type = 2, RULE_typeParameter = 3, 
		RULE_regularType = 4, RULE_delegateType = 5, RULE_arrayType = 6, RULE_nestedType = 7, 
		RULE_nestedTypeName = 8, RULE_resolvedType = 9, RULE_namespace = 10, RULE_typeName = 11, 
		RULE_possiblyGenericTypeName = 12, RULE_enumTypeName = 13, RULE_interfaceTypeName = 14, 
		RULE_structTypeName = 15, RULE_simpleTypeName = 16, RULE_genericTypePart = 17, 
		RULE_genericParam = 18, RULE_boundTypeParameter = 19, RULE_assembly = 20, 
		RULE_assemblyVersion = 21, RULE_method = 22, RULE_regularMethod = 23, 
		RULE_methodParameters = 24, RULE_nonStaticCtor = 25, RULE_staticCctor = 26, 
		RULE_customMethod = 27, RULE_formalParam = 28, RULE_parameterModifier = 29, 
		RULE_staticModifier = 30, RULE_paramsModifier = 31, RULE_optsModifier = 32, 
		RULE_refModifier = 33, RULE_outModifier = 34, RULE_extensionModifier = 35, 
		RULE_id = 36, RULE_num = 37;
	public static final String[] ruleNames = {
		"typeEOL", "methodEOL", "type", "typeParameter", "regularType", "delegateType", 
		"arrayType", "nestedType", "nestedTypeName", "resolvedType", "namespace", 
		"typeName", "possiblyGenericTypeName", "enumTypeName", "interfaceTypeName", 
		"structTypeName", "simpleTypeName", "genericTypePart", "genericParam", 
		"boundTypeParameter", "assembly", "assemblyVersion", "method", "regularMethod", 
		"methodParameters", "nonStaticCtor", "staticCctor", "customMethod", "formalParam", 
		"parameterModifier", "staticModifier", "paramsModifier", "optsModifier", 
		"refModifier", "outModifier", "extensionModifier", "id", "num"
	};

	private static final String[] _LITERAL_NAMES = {
		null, "','", "'d:'", "'arr('", "'):'", "'n:'", "'+'", "'.'", "'e:'", "'i:'", 
		"'s:'", "'''", "'['", "']'", "'->'", "'('", "')'", "']..ctor'", "']..cctor'", 
		"'].'", "'static'", "'params'", "'opts'", "'ref'", "'out'", "'this'", 
		"'0'", "'?'", null, null, null, null, "'\n'"
	};
	private static final String[] _SYMBOLIC_NAMES = {
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, null, null, null, null, null, null, null, null, null, 
		null, null, null, "UNKNOWN", "POSNUM", "LETTER", "SIGN", "WS", "EOL"
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
			setState(76);
			type();
			setState(77);
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
			setState(79);
			method();
			setState(80);
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
			setState(87);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(82);
				match(UNKNOWN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(83);
				typeParameter();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(84);
				regularType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(85);
				delegateType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(86);
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
			setState(89);
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
			setState(93);
			switch (_input.LA(1)) {
			case T__7:
			case T__8:
			case T__9:
			case LETTER:
				{
				setState(91);
				resolvedType();
				}
				break;
			case T__4:
				{
				setState(92);
				nestedType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(95);
			match(T__0);
			setState(97);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(96);
				match(WS);
				}
			}

			setState(99);
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
			setState(101);
			match(T__1);
			setState(102);
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
			setState(104);
			match(T__2);
			setState(105);
			match(POSNUM);
			setState(106);
			match(T__3);
			setState(107);
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
			setState(109);
			match(T__4);
			setState(110);
			nestedTypeName();
			setState(111);
			match(T__5);
			setState(112);
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
			setState(116);
			switch (_input.LA(1)) {
			case T__4:
				enterOuterAlt(_localctx, 1);
				{
				setState(114);
				nestedType();
				}
				break;
			case T__7:
			case T__8:
			case T__9:
			case LETTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(115);
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
			setState(119);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,4,_ctx) ) {
			case 1:
				{
				setState(118);
				namespace();
				}
				break;
			}
			setState(121);
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
			setState(126); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(123);
					id();
					setState(124);
					match(T__6);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(128); 
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
			setState(132);
			switch (_input.LA(1)) {
			case T__7:
				enterOuterAlt(_localctx, 1);
				{
				setState(130);
				enumTypeName();
				}
				break;
			case T__8:
			case T__9:
			case LETTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(131);
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
			setState(137);
			switch (_input.LA(1)) {
			case T__8:
				{
				setState(134);
				interfaceTypeName();
				}
				break;
			case T__9:
				{
				setState(135);
				structTypeName();
				}
				break;
			case LETTER:
				{
				setState(136);
				simpleTypeName();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(140);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(139);
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
			setState(142);
			match(T__7);
			setState(143);
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
			setState(145);
			match(T__8);
			setState(146);
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
			setState(148);
			match(T__9);
			setState(149);
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
			setState(151);
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
			setState(153);
			match(T__10);
			setState(154);
			match(POSNUM);
			setState(155);
			match(T__11);
			setState(156);
			genericParam();
			setState(161);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__0) {
				{
				{
				setState(157);
				match(T__0);
				setState(158);
				genericParam();
				}
				}
				setState(163);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(164);
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
			setState(166);
			match(T__11);
			setState(167);
			boundTypeParameter();
			setState(168);
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
			setState(170);
			typeParameter();
			setState(179);
			_la = _input.LA(1);
			if (_la==T__13 || _la==WS) {
				{
				setState(172);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(171);
					match(WS);
					}
				}

				setState(174);
				match(T__13);
				setState(176);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(175);
					match(WS);
					}
				}

				setState(178);
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
			setState(181);
			id();
			setState(187);
			_la = _input.LA(1);
			if (_la==T__0) {
				{
				setState(182);
				match(T__0);
				setState(184);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(183);
					match(WS);
					}
				}

				setState(186);
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
			setState(189);
			num();
			setState(190);
			match(T__6);
			setState(191);
			num();
			setState(192);
			match(T__6);
			setState(193);
			num();
			setState(194);
			match(T__6);
			setState(195);
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
		public TerminalNode UNKNOWN() { return getToken(TypeNamingParser.UNKNOWN, 0); }
		public RegularMethodContext regularMethod() {
			return getRuleContext(RegularMethodContext.class,0);
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
		try {
			setState(199);
			switch (_input.LA(1)) {
			case UNKNOWN:
				enterOuterAlt(_localctx, 1);
				{
				setState(197);
				match(UNKNOWN);
				}
				break;
			case T__11:
			case T__19:
			case WS:
				enterOuterAlt(_localctx, 2);
				{
				setState(198);
				regularMethod();
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

	public static class RegularMethodContext extends ParserRuleContext {
		public MethodParametersContext methodParameters() {
			return getRuleContext(MethodParametersContext.class,0);
		}
		public NonStaticCtorContext nonStaticCtor() {
			return getRuleContext(NonStaticCtorContext.class,0);
		}
		public StaticCctorContext staticCctor() {
			return getRuleContext(StaticCctorContext.class,0);
		}
		public CustomMethodContext customMethod() {
			return getRuleContext(CustomMethodContext.class,0);
		}
		public RegularMethodContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_regularMethod; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterRegularMethod(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitRegularMethod(this);
		}
	}

	public final RegularMethodContext regularMethod() throws RecognitionException {
		RegularMethodContext _localctx = new RegularMethodContext(_ctx, getState());
		enterRule(_localctx, 46, RULE_regularMethod);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(204);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,16,_ctx) ) {
			case 1:
				{
				setState(201);
				nonStaticCtor();
				}
				break;
			case 2:
				{
				setState(202);
				staticCctor();
				}
				break;
			case 3:
				{
				setState(203);
				customMethod();
				}
				break;
			}
			setState(206);
			methodParameters();
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

	public static class MethodParametersContext extends ParserRuleContext {
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
		public MethodParametersContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_methodParameters; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterMethodParameters(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitMethodParameters(this);
		}
	}

	public final MethodParametersContext methodParameters() throws RecognitionException {
		MethodParametersContext _localctx = new MethodParametersContext(_ctx, getState());
		enterRule(_localctx, 48, RULE_methodParameters);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(208);
			match(T__14);
			setState(210);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,17,_ctx) ) {
			case 1:
				{
				setState(209);
				match(WS);
				}
				break;
			}
			setState(226);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,21,_ctx) ) {
			case 1:
				{
				setState(212);
				formalParam();
				setState(223);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(214);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(213);
							match(WS);
							}
						}

						setState(216);
						match(T__0);
						setState(218);
						_errHandler.sync(this);
						switch ( getInterpreter().adaptivePredict(_input,19,_ctx) ) {
						case 1:
							{
							setState(217);
							match(WS);
							}
							break;
						}
						setState(220);
						formalParam();
						}
						} 
					}
					setState(225);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,20,_ctx);
				}
				}
				break;
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

	public static class NonStaticCtorContext extends ParserRuleContext {
		public TerminalNode UNKNOWN() { return getToken(TypeNamingParser.UNKNOWN, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(TypeNamingParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(TypeNamingParser.WS, i);
		}
		public NonStaticCtorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_nonStaticCtor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterNonStaticCtor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitNonStaticCtor(this);
		}
	}

	public final NonStaticCtorContext nonStaticCtor() throws RecognitionException {
		NonStaticCtorContext _localctx = new NonStaticCtorContext(_ctx, getState());
		enterRule(_localctx, 50, RULE_nonStaticCtor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(234);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(233);
				match(WS);
				}
			}

			setState(236);
			match(T__11);
			setState(237);
			match(UNKNOWN);
			setState(238);
			match(T__12);
			setState(240);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(239);
				match(WS);
				}
			}

			setState(242);
			match(T__11);
			setState(243);
			type();
			setState(244);
			match(T__16);
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

	public static class StaticCctorContext extends ParserRuleContext {
		public StaticModifierContext staticModifier() {
			return getRuleContext(StaticModifierContext.class,0);
		}
		public TerminalNode UNKNOWN() { return getToken(TypeNamingParser.UNKNOWN, 0); }
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(TypeNamingParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(TypeNamingParser.WS, i);
		}
		public StaticCctorContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_staticCctor; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterStaticCctor(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitStaticCctor(this);
		}
	}

	public final StaticCctorContext staticCctor() throws RecognitionException {
		StaticCctorContext _localctx = new StaticCctorContext(_ctx, getState());
		enterRule(_localctx, 52, RULE_staticCctor);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(246);
			staticModifier();
			setState(248);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(247);
				match(WS);
				}
			}

			setState(250);
			match(T__11);
			setState(251);
			match(UNKNOWN);
			setState(252);
			match(T__12);
			setState(254);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(253);
				match(WS);
				}
			}

			setState(256);
			match(T__11);
			setState(257);
			type();
			setState(258);
			match(T__17);
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
		public StaticModifierContext staticModifier() {
			return getRuleContext(StaticModifierContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(TypeNamingParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(TypeNamingParser.WS, i);
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
		enterRule(_localctx, 54, RULE_customMethod);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(261);
			_la = _input.LA(1);
			if (_la==T__19) {
				{
				setState(260);
				staticModifier();
				}
			}

			setState(264);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(263);
				match(WS);
				}
			}

			setState(266);
			match(T__11);
			setState(267);
			type();
			setState(268);
			match(T__12);
			setState(270);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(269);
				match(WS);
				}
			}

			setState(272);
			match(T__11);
			setState(273);
			type();
			setState(274);
			match(T__18);
			setState(275);
			id();
			setState(277);
			_la = _input.LA(1);
			if (_la==T__10) {
				{
				setState(276);
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
		public ParameterModifierContext parameterModifier() {
			return getRuleContext(ParameterModifierContext.class,0);
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
		enterRule(_localctx, 56, RULE_formalParam);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(283);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,32,_ctx) ) {
			case 1:
				{
				setState(280);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(279);
					match(WS);
					}
				}

				setState(282);
				parameterModifier();
				}
				break;
			}
			setState(286);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(285);
				match(WS);
				}
			}

			setState(288);
			match(T__11);
			setState(289);
			type();
			setState(290);
			match(T__12);
			setState(292);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(291);
				match(WS);
				}
			}

			setState(294);
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

	public static class ParameterModifierContext extends ParserRuleContext {
		public ParamsModifierContext paramsModifier() {
			return getRuleContext(ParamsModifierContext.class,0);
		}
		public OptsModifierContext optsModifier() {
			return getRuleContext(OptsModifierContext.class,0);
		}
		public RefModifierContext refModifier() {
			return getRuleContext(RefModifierContext.class,0);
		}
		public OutModifierContext outModifier() {
			return getRuleContext(OutModifierContext.class,0);
		}
		public ExtensionModifierContext extensionModifier() {
			return getRuleContext(ExtensionModifierContext.class,0);
		}
		public ParameterModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_parameterModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterParameterModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitParameterModifier(this);
		}
	}

	public final ParameterModifierContext parameterModifier() throws RecognitionException {
		ParameterModifierContext _localctx = new ParameterModifierContext(_ctx, getState());
		enterRule(_localctx, 58, RULE_parameterModifier);
		try {
			setState(301);
			switch (_input.LA(1)) {
			case T__20:
				enterOuterAlt(_localctx, 1);
				{
				setState(296);
				paramsModifier();
				}
				break;
			case T__21:
				enterOuterAlt(_localctx, 2);
				{
				setState(297);
				optsModifier();
				}
				break;
			case T__22:
				enterOuterAlt(_localctx, 3);
				{
				setState(298);
				refModifier();
				}
				break;
			case T__23:
				enterOuterAlt(_localctx, 4);
				{
				setState(299);
				outModifier();
				}
				break;
			case T__24:
				enterOuterAlt(_localctx, 5);
				{
				setState(300);
				extensionModifier();
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
		enterRule(_localctx, 60, RULE_staticModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(303);
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
		enterRule(_localctx, 62, RULE_paramsModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(305);
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
		enterRule(_localctx, 64, RULE_optsModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(307);
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
		enterRule(_localctx, 66, RULE_refModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(309);
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

	public static class OutModifierContext extends ParserRuleContext {
		public OutModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_outModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterOutModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitOutModifier(this);
		}
	}

	public final OutModifierContext outModifier() throws RecognitionException {
		OutModifierContext _localctx = new OutModifierContext(_ctx, getState());
		enterRule(_localctx, 68, RULE_outModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(311);
			match(T__23);
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

	public static class ExtensionModifierContext extends ParserRuleContext {
		public ExtensionModifierContext(ParserRuleContext parent, int invokingState) {
			super(parent, invokingState);
		}
		@Override public int getRuleIndex() { return RULE_extensionModifier; }
		@Override
		public void enterRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).enterExtensionModifier(this);
		}
		@Override
		public void exitRule(ParseTreeListener listener) {
			if ( listener instanceof TypeNamingListener ) ((TypeNamingListener)listener).exitExtensionModifier(this);
		}
	}

	public final ExtensionModifierContext extensionModifier() throws RecognitionException {
		ExtensionModifierContext _localctx = new ExtensionModifierContext(_ctx, getState());
		enterRule(_localctx, 70, RULE_extensionModifier);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(313);
			match(T__24);
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
		enterRule(_localctx, 72, RULE_id);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(315);
			match(LETTER);
			setState(321);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__25) | (1L << POSNUM) | (1L << LETTER) | (1L << SIGN))) != 0)) {
				{
				setState(319);
				switch (_input.LA(1)) {
				case LETTER:
					{
					setState(316);
					match(LETTER);
					}
					break;
				case T__25:
				case POSNUM:
					{
					setState(317);
					num();
					}
					break;
				case SIGN:
					{
					setState(318);
					match(SIGN);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(323);
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
		enterRule(_localctx, 74, RULE_num);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(324);
			_la = _input.LA(1);
			if ( !(_la==T__25 || _la==POSNUM) ) {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\"\u0149\4\2\t\2\4"+
		"\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13\t"+
		"\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\4\32\t\32\4\33\t\33\4\34\t\34\4\35\t\35\4\36\t\36\4\37\t\37\4 \t \4!"+
		"\t!\4\"\t\"\4#\t#\4$\t$\4%\t%\4&\t&\4\'\t\'\3\2\3\2\3\2\3\3\3\3\3\3\3"+
		"\4\3\4\3\4\3\4\3\4\5\4Z\n\4\3\5\3\5\3\6\3\6\5\6`\n\6\3\6\3\6\5\6d\n\6"+
		"\3\6\3\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\5"+
		"\nw\n\n\3\13\5\13z\n\13\3\13\3\13\3\f\3\f\3\f\6\f\u0081\n\f\r\f\16\f\u0082"+
		"\3\r\3\r\5\r\u0087\n\r\3\16\3\16\3\16\5\16\u008c\n\16\3\16\5\16\u008f"+
		"\n\16\3\17\3\17\3\17\3\20\3\20\3\20\3\21\3\21\3\21\3\22\3\22\3\23\3\23"+
		"\3\23\3\23\3\23\3\23\7\23\u00a2\n\23\f\23\16\23\u00a5\13\23\3\23\3\23"+
		"\3\24\3\24\3\24\3\24\3\25\3\25\5\25\u00af\n\25\3\25\3\25\5\25\u00b3\n"+
		"\25\3\25\5\25\u00b6\n\25\3\26\3\26\3\26\5\26\u00bb\n\26\3\26\5\26\u00be"+
		"\n\26\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\27\3\30\3\30\5\30\u00ca\n\30"+
		"\3\31\3\31\3\31\5\31\u00cf\n\31\3\31\3\31\3\32\3\32\5\32\u00d5\n\32\3"+
		"\32\3\32\5\32\u00d9\n\32\3\32\3\32\5\32\u00dd\n\32\3\32\7\32\u00e0\n\32"+
		"\f\32\16\32\u00e3\13\32\5\32\u00e5\n\32\3\32\5\32\u00e8\n\32\3\32\3\32"+
		"\3\33\5\33\u00ed\n\33\3\33\3\33\3\33\3\33\5\33\u00f3\n\33\3\33\3\33\3"+
		"\33\3\33\3\34\3\34\5\34\u00fb\n\34\3\34\3\34\3\34\3\34\5\34\u0101\n\34"+
		"\3\34\3\34\3\34\3\34\3\35\5\35\u0108\n\35\3\35\5\35\u010b\n\35\3\35\3"+
		"\35\3\35\3\35\5\35\u0111\n\35\3\35\3\35\3\35\3\35\3\35\5\35\u0118\n\35"+
		"\3\36\5\36\u011b\n\36\3\36\5\36\u011e\n\36\3\36\5\36\u0121\n\36\3\36\3"+
		"\36\3\36\3\36\5\36\u0127\n\36\3\36\3\36\3\37\3\37\3\37\3\37\3\37\5\37"+
		"\u0130\n\37\3 \3 \3!\3!\3\"\3\"\3#\3#\3$\3$\3%\3%\3&\3&\3&\3&\7&\u0142"+
		"\n&\f&\16&\u0145\13&\3\'\3\'\3\'\2\2(\2\4\6\b\n\f\16\20\22\24\26\30\32"+
		"\34\36 \"$&(*,.\60\62\64\668:<>@BDFHJL\2\3\4\2\34\34\36\36\u0151\2N\3"+
		"\2\2\2\4Q\3\2\2\2\6Y\3\2\2\2\b[\3\2\2\2\n_\3\2\2\2\fg\3\2\2\2\16j\3\2"+
		"\2\2\20o\3\2\2\2\22v\3\2\2\2\24y\3\2\2\2\26\u0080\3\2\2\2\30\u0086\3\2"+
		"\2\2\32\u008b\3\2\2\2\34\u0090\3\2\2\2\36\u0093\3\2\2\2 \u0096\3\2\2\2"+
		"\"\u0099\3\2\2\2$\u009b\3\2\2\2&\u00a8\3\2\2\2(\u00ac\3\2\2\2*\u00b7\3"+
		"\2\2\2,\u00bf\3\2\2\2.\u00c9\3\2\2\2\60\u00ce\3\2\2\2\62\u00d2\3\2\2\2"+
		"\64\u00ec\3\2\2\2\66\u00f8\3\2\2\28\u0107\3\2\2\2:\u011d\3\2\2\2<\u012f"+
		"\3\2\2\2>\u0131\3\2\2\2@\u0133\3\2\2\2B\u0135\3\2\2\2D\u0137\3\2\2\2F"+
		"\u0139\3\2\2\2H\u013b\3\2\2\2J\u013d\3\2\2\2L\u0146\3\2\2\2NO\5\6\4\2"+
		"OP\7\"\2\2P\3\3\2\2\2QR\5.\30\2RS\7\"\2\2S\5\3\2\2\2TZ\7\35\2\2UZ\5\b"+
		"\5\2VZ\5\n\6\2WZ\5\f\7\2XZ\5\16\b\2YT\3\2\2\2YU\3\2\2\2YV\3\2\2\2YW\3"+
		"\2\2\2YX\3\2\2\2Z\7\3\2\2\2[\\\5J&\2\\\t\3\2\2\2]`\5\24\13\2^`\5\20\t"+
		"\2_]\3\2\2\2_^\3\2\2\2`a\3\2\2\2ac\7\3\2\2bd\7!\2\2cb\3\2\2\2cd\3\2\2"+
		"\2de\3\2\2\2ef\5*\26\2f\13\3\2\2\2gh\7\4\2\2hi\5.\30\2i\r\3\2\2\2jk\7"+
		"\5\2\2kl\7\36\2\2lm\7\6\2\2mn\5\6\4\2n\17\3\2\2\2op\7\7\2\2pq\5\22\n\2"+
		"qr\7\b\2\2rs\5\30\r\2s\21\3\2\2\2tw\5\20\t\2uw\5\24\13\2vt\3\2\2\2vu\3"+
		"\2\2\2w\23\3\2\2\2xz\5\26\f\2yx\3\2\2\2yz\3\2\2\2z{\3\2\2\2{|\5\30\r\2"+
		"|\25\3\2\2\2}~\5J&\2~\177\7\t\2\2\177\u0081\3\2\2\2\u0080}\3\2\2\2\u0081"+
		"\u0082\3\2\2\2\u0082\u0080\3\2\2\2\u0082\u0083\3\2\2\2\u0083\27\3\2\2"+
		"\2\u0084\u0087\5\34\17\2\u0085\u0087\5\32\16\2\u0086\u0084\3\2\2\2\u0086"+
		"\u0085\3\2\2\2\u0087\31\3\2\2\2\u0088\u008c\5\36\20\2\u0089\u008c\5 \21"+
		"\2\u008a\u008c\5\"\22\2\u008b\u0088\3\2\2\2\u008b\u0089\3\2\2\2\u008b"+
		"\u008a\3\2\2\2\u008c\u008e\3\2\2\2\u008d\u008f\5$\23\2\u008e\u008d\3\2"+
		"\2\2\u008e\u008f\3\2\2\2\u008f\33\3\2\2\2\u0090\u0091\7\n\2\2\u0091\u0092"+
		"\5\"\22\2\u0092\35\3\2\2\2\u0093\u0094\7\13\2\2\u0094\u0095\5\"\22\2\u0095"+
		"\37\3\2\2\2\u0096\u0097\7\f\2\2\u0097\u0098\5\"\22\2\u0098!\3\2\2\2\u0099"+
		"\u009a\5J&\2\u009a#\3\2\2\2\u009b\u009c\7\r\2\2\u009c\u009d\7\36\2\2\u009d"+
		"\u009e\7\16\2\2\u009e\u00a3\5&\24\2\u009f\u00a0\7\3\2\2\u00a0\u00a2\5"+
		"&\24\2\u00a1\u009f\3\2\2\2\u00a2\u00a5\3\2\2\2\u00a3\u00a1\3\2\2\2\u00a3"+
		"\u00a4\3\2\2\2\u00a4\u00a6\3\2\2\2\u00a5\u00a3\3\2\2\2\u00a6\u00a7\7\17"+
		"\2\2\u00a7%\3\2\2\2\u00a8\u00a9\7\16\2\2\u00a9\u00aa\5(\25\2\u00aa\u00ab"+
		"\7\17\2\2\u00ab\'\3\2\2\2\u00ac\u00b5\5\b\5\2\u00ad\u00af\7!\2\2\u00ae"+
		"\u00ad\3\2\2\2\u00ae\u00af\3\2\2\2\u00af\u00b0\3\2\2\2\u00b0\u00b2\7\20"+
		"\2\2\u00b1\u00b3\7!\2\2\u00b2\u00b1\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3"+
		"\u00b4\3\2\2\2\u00b4\u00b6\5\6\4\2\u00b5\u00ae\3\2\2\2\u00b5\u00b6\3\2"+
		"\2\2\u00b6)\3\2\2\2\u00b7\u00bd\5J&\2\u00b8\u00ba\7\3\2\2\u00b9\u00bb"+
		"\7!\2\2\u00ba\u00b9\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\3\2\2\2\u00bc"+
		"\u00be\5,\27\2\u00bd\u00b8\3\2\2\2\u00bd\u00be\3\2\2\2\u00be+\3\2\2\2"+
		"\u00bf\u00c0\5L\'\2\u00c0\u00c1\7\t\2\2\u00c1\u00c2\5L\'\2\u00c2\u00c3"+
		"\7\t\2\2\u00c3\u00c4\5L\'\2\u00c4\u00c5\7\t\2\2\u00c5\u00c6\5L\'\2\u00c6"+
		"-\3\2\2\2\u00c7\u00ca\7\35\2\2\u00c8\u00ca\5\60\31\2\u00c9\u00c7\3\2\2"+
		"\2\u00c9\u00c8\3\2\2\2\u00ca/\3\2\2\2\u00cb\u00cf\5\64\33\2\u00cc\u00cf"+
		"\5\66\34\2\u00cd\u00cf\58\35\2\u00ce\u00cb\3\2\2\2\u00ce\u00cc\3\2\2\2"+
		"\u00ce\u00cd\3\2\2\2\u00cf\u00d0\3\2\2\2\u00d0\u00d1\5\62\32\2\u00d1\61"+
		"\3\2\2\2\u00d2\u00d4\7\21\2\2\u00d3\u00d5\7!\2\2\u00d4\u00d3\3\2\2\2\u00d4"+
		"\u00d5\3\2\2\2\u00d5\u00e4\3\2\2\2\u00d6\u00e1\5:\36\2\u00d7\u00d9\7!"+
		"\2\2\u00d8\u00d7\3\2\2\2\u00d8\u00d9\3\2\2\2\u00d9\u00da\3\2\2\2\u00da"+
		"\u00dc\7\3\2\2\u00db\u00dd\7!\2\2\u00dc\u00db\3\2\2\2\u00dc\u00dd\3\2"+
		"\2\2\u00dd\u00de\3\2\2\2\u00de\u00e0\5:\36\2\u00df\u00d8\3\2\2\2\u00e0"+
		"\u00e3\3\2\2\2\u00e1\u00df\3\2\2\2\u00e1\u00e2\3\2\2\2\u00e2\u00e5\3\2"+
		"\2\2\u00e3\u00e1\3\2\2\2\u00e4\u00d6\3\2\2\2\u00e4\u00e5\3\2\2\2\u00e5"+
		"\u00e7\3\2\2\2\u00e6\u00e8\7!\2\2\u00e7\u00e6\3\2\2\2\u00e7\u00e8\3\2"+
		"\2\2\u00e8\u00e9\3\2\2\2\u00e9\u00ea\7\22\2\2\u00ea\63\3\2\2\2\u00eb\u00ed"+
		"\7!\2\2\u00ec\u00eb\3\2\2\2\u00ec\u00ed\3\2\2\2\u00ed\u00ee\3\2\2\2\u00ee"+
		"\u00ef\7\16\2\2\u00ef\u00f0\7\35\2\2\u00f0\u00f2\7\17\2\2\u00f1\u00f3"+
		"\7!\2\2\u00f2\u00f1\3\2\2\2\u00f2\u00f3\3\2\2\2\u00f3\u00f4\3\2\2\2\u00f4"+
		"\u00f5\7\16\2\2\u00f5\u00f6\5\6\4\2\u00f6\u00f7\7\23\2\2\u00f7\65\3\2"+
		"\2\2\u00f8\u00fa\5> \2\u00f9\u00fb\7!\2\2\u00fa\u00f9\3\2\2\2\u00fa\u00fb"+
		"\3\2\2\2\u00fb\u00fc\3\2\2\2\u00fc\u00fd\7\16\2\2\u00fd\u00fe\7\35\2\2"+
		"\u00fe\u0100\7\17\2\2\u00ff\u0101\7!\2\2\u0100\u00ff\3\2\2\2\u0100\u0101"+
		"\3\2\2\2\u0101\u0102\3\2\2\2\u0102\u0103\7\16\2\2\u0103\u0104\5\6\4\2"+
		"\u0104\u0105\7\24\2\2\u0105\67\3\2\2\2\u0106\u0108\5> \2\u0107\u0106\3"+
		"\2\2\2\u0107\u0108\3\2\2\2\u0108\u010a\3\2\2\2\u0109\u010b\7!\2\2\u010a"+
		"\u0109\3\2\2\2\u010a\u010b\3\2\2\2\u010b\u010c\3\2\2\2\u010c\u010d\7\16"+
		"\2\2\u010d\u010e\5\6\4\2\u010e\u0110\7\17\2\2\u010f\u0111\7!\2\2\u0110"+
		"\u010f\3\2\2\2\u0110\u0111\3\2\2\2\u0111\u0112\3\2\2\2\u0112\u0113\7\16"+
		"\2\2\u0113\u0114\5\6\4\2\u0114\u0115\7\25\2\2\u0115\u0117\5J&\2\u0116"+
		"\u0118\5$\23\2\u0117\u0116\3\2\2\2\u0117\u0118\3\2\2\2\u01189\3\2\2\2"+
		"\u0119\u011b\7!\2\2\u011a\u0119\3\2\2\2\u011a\u011b\3\2\2\2\u011b\u011c"+
		"\3\2\2\2\u011c\u011e\5<\37\2\u011d\u011a\3\2\2\2\u011d\u011e\3\2\2\2\u011e"+
		"\u0120\3\2\2\2\u011f\u0121\7!\2\2\u0120\u011f\3\2\2\2\u0120\u0121\3\2"+
		"\2\2\u0121\u0122\3\2\2\2\u0122\u0123\7\16\2\2\u0123\u0124\5\6\4\2\u0124"+
		"\u0126\7\17\2\2\u0125\u0127\7!\2\2\u0126\u0125\3\2\2\2\u0126\u0127\3\2"+
		"\2\2\u0127\u0128\3\2\2\2\u0128\u0129\5J&\2\u0129;\3\2\2\2\u012a\u0130"+
		"\5@!\2\u012b\u0130\5B\"\2\u012c\u0130\5D#\2\u012d\u0130\5F$\2\u012e\u0130"+
		"\5H%\2\u012f\u012a\3\2\2\2\u012f\u012b\3\2\2\2\u012f\u012c\3\2\2\2\u012f"+
		"\u012d\3\2\2\2\u012f\u012e\3\2\2\2\u0130=\3\2\2\2\u0131\u0132\7\26\2\2"+
		"\u0132?\3\2\2\2\u0133\u0134\7\27\2\2\u0134A\3\2\2\2\u0135\u0136\7\30\2"+
		"\2\u0136C\3\2\2\2\u0137\u0138\7\31\2\2\u0138E\3\2\2\2\u0139\u013a\7\32"+
		"\2\2\u013aG\3\2\2\2\u013b\u013c\7\33\2\2\u013cI\3\2\2\2\u013d\u0143\7"+
		"\37\2\2\u013e\u0142\7\37\2\2\u013f\u0142\5L\'\2\u0140\u0142\7 \2\2\u0141"+
		"\u013e\3\2\2\2\u0141\u013f\3\2\2\2\u0141\u0140\3\2\2\2\u0142\u0145\3\2"+
		"\2\2\u0143\u0141\3\2\2\2\u0143\u0144\3\2\2\2\u0144K\3\2\2\2\u0145\u0143"+
		"\3\2\2\2\u0146\u0147\t\2\2\2\u0147M\3\2\2\2(Y_cvy\u0082\u0086\u008b\u008e"+
		"\u00a3\u00ae\u00b2\u00b5\u00ba\u00bd\u00c9\u00ce\u00d4\u00d8\u00dc\u00e1"+
		"\u00e4\u00e7\u00ec\u00f2\u00fa\u0100\u0107\u010a\u0110\u0117\u011a\u011d"+
		"\u0120\u0126\u012f\u0141\u0143";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}