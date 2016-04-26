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
		T__17=18, UNKNOWN=19, POSNUM=20, LETTER=21, SIGN=22, WS=23, EOL=24;
	public static final int
		RULE_typeEOL = 0, RULE_methodEOL = 1, RULE_type = 2, RULE_typeParameter = 3, 
		RULE_regularType = 4, RULE_delegateType = 5, RULE_arrayType = 6, RULE_nestedType = 7, 
		RULE_nestedTypeName = 8, RULE_resolvedType = 9, RULE_namespace = 10, RULE_typeName = 11, 
		RULE_enumTypeName = 12, RULE_interfaceTypeName = 13, RULE_structTypeName = 14, 
		RULE_simpleTypeName = 15, RULE_genericTypePart = 16, RULE_genericParam = 17, 
		RULE_assembly = 18, RULE_assemblyVersion = 19, RULE_method = 20, RULE_formalParam = 21, 
		RULE_id = 22, RULE_num = 23;
	public static final String[] ruleNames = {
		"typeEOL", "methodEOL", "type", "typeParameter", "regularType", "delegateType", 
		"arrayType", "nestedType", "nestedTypeName", "resolvedType", "namespace", 
		"typeName", "enumTypeName", "interfaceTypeName", "structTypeName", "simpleTypeName", 
		"genericTypePart", "genericParam", "assembly", "assemblyVersion", "method", 
		"formalParam", "id", "num"
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
			setState(48);
			type();
			setState(49);
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
			setState(51);
			method();
			setState(52);
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
			setState(59);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,0,_ctx) ) {
			case 1:
				enterOuterAlt(_localctx, 1);
				{
				setState(54);
				match(UNKNOWN);
				}
				break;
			case 2:
				enterOuterAlt(_localctx, 2);
				{
				setState(55);
				typeParameter();
				}
				break;
			case 3:
				enterOuterAlt(_localctx, 3);
				{
				setState(56);
				regularType();
				}
				break;
			case 4:
				enterOuterAlt(_localctx, 4);
				{
				setState(57);
				delegateType();
				}
				break;
			case 5:
				enterOuterAlt(_localctx, 5);
				{
				setState(58);
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
		public TypeContext type() {
			return getRuleContext(TypeContext.class,0);
		}
		public List<TerminalNode> WS() { return getTokens(TypeNamingParser.WS); }
		public TerminalNode WS(int i) {
			return getToken(TypeNamingParser.WS, i);
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(61);
			id();
			setState(70);
			_la = _input.LA(1);
			if (_la==T__0 || _la==WS) {
				{
				setState(63);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(62);
					match(WS);
					}
				}

				setState(65);
				match(T__0);
				setState(67);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(66);
					match(WS);
					}
				}

				setState(69);
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
			setState(74);
			switch (_input.LA(1)) {
			case T__8:
			case T__9:
			case T__10:
			case LETTER:
				{
				setState(72);
				resolvedType();
				}
				break;
			case T__5:
				{
				setState(73);
				nestedType();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(76);
			match(T__1);
			setState(78);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(77);
				match(WS);
				}
			}

			setState(80);
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
			setState(82);
			match(T__2);
			setState(83);
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
			setState(85);
			match(T__3);
			setState(86);
			match(POSNUM);
			setState(87);
			match(T__4);
			setState(88);
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
			setState(90);
			match(T__5);
			setState(91);
			nestedTypeName();
			setState(92);
			match(T__6);
			setState(93);
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
			setState(97);
			switch (_input.LA(1)) {
			case T__5:
				enterOuterAlt(_localctx, 1);
				{
				setState(95);
				nestedType();
				}
				break;
			case T__8:
			case T__9:
			case T__10:
			case LETTER:
				enterOuterAlt(_localctx, 2);
				{
				setState(96);
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
			setState(100);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,7,_ctx) ) {
			case 1:
				{
				setState(99);
				namespace();
				}
				break;
			}
			setState(102);
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
			setState(107); 
			_errHandler.sync(this);
			_alt = 1;
			do {
				switch (_alt) {
				case 1:
					{
					{
					setState(104);
					id();
					setState(105);
					match(T__7);
					}
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				setState(109); 
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,8,_ctx);
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
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(115);
			switch (_input.LA(1)) {
			case T__8:
				{
				setState(111);
				enumTypeName();
				}
				break;
			case T__9:
				{
				setState(112);
				interfaceTypeName();
				}
				break;
			case T__10:
				{
				setState(113);
				structTypeName();
				}
				break;
			case LETTER:
				{
				setState(114);
				simpleTypeName();
				}
				break;
			default:
				throw new NoViableAltException(this);
			}
			setState(118);
			_la = _input.LA(1);
			if (_la==T__11) {
				{
				setState(117);
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
		enterRule(_localctx, 24, RULE_enumTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(120);
			match(T__8);
			setState(121);
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
		enterRule(_localctx, 26, RULE_interfaceTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(123);
			match(T__9);
			setState(124);
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
		enterRule(_localctx, 28, RULE_structTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(126);
			match(T__10);
			setState(127);
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
		enterRule(_localctx, 30, RULE_simpleTypeName);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(129);
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
		enterRule(_localctx, 32, RULE_genericTypePart);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(131);
			match(T__11);
			setState(132);
			match(POSNUM);
			setState(133);
			match(T__12);
			setState(134);
			genericParam();
			setState(139);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while (_la==T__1) {
				{
				{
				setState(135);
				match(T__1);
				setState(136);
				genericParam();
				}
				}
				setState(141);
				_errHandler.sync(this);
				_la = _input.LA(1);
			}
			setState(142);
			match(T__13);
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
		enterRule(_localctx, 34, RULE_genericParam);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(144);
			match(T__12);
			setState(145);
			typeParameter();
			setState(146);
			match(T__13);
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
		enterRule(_localctx, 36, RULE_assembly);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(148);
			id();
			setState(154);
			_la = _input.LA(1);
			if (_la==T__1) {
				{
				setState(149);
				match(T__1);
				setState(151);
				_la = _input.LA(1);
				if (_la==WS) {
					{
					setState(150);
					match(WS);
					}
				}

				setState(153);
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
		enterRule(_localctx, 38, RULE_assemblyVersion);
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(156);
			num();
			setState(157);
			match(T__7);
			setState(158);
			num();
			setState(159);
			match(T__7);
			setState(160);
			num();
			setState(161);
			match(T__7);
			setState(162);
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
		enterRule(_localctx, 40, RULE_method);
		int _la;
		try {
			int _alt;
			enterOuterAlt(_localctx, 1);
			{
			setState(164);
			match(T__12);
			setState(165);
			type();
			setState(166);
			match(T__13);
			setState(168);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(167);
				match(WS);
				}
			}

			setState(170);
			match(T__12);
			setState(171);
			type();
			setState(172);
			match(T__14);
			setState(173);
			id();
			setState(174);
			match(T__15);
			setState(176);
			_errHandler.sync(this);
			switch ( getInterpreter().adaptivePredict(_input,15,_ctx) ) {
			case 1:
				{
				setState(175);
				match(WS);
				}
				break;
			}
			setState(192);
			_la = _input.LA(1);
			if (_la==T__12) {
				{
				setState(178);
				formalParam();
				setState(189);
				_errHandler.sync(this);
				_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				while ( _alt!=2 && _alt!=org.antlr.v4.runtime.atn.ATN.INVALID_ALT_NUMBER ) {
					if ( _alt==1 ) {
						{
						{
						setState(180);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(179);
							match(WS);
							}
						}

						setState(182);
						match(T__1);
						setState(184);
						_la = _input.LA(1);
						if (_la==WS) {
							{
							setState(183);
							match(WS);
							}
						}

						setState(186);
						formalParam();
						}
						} 
					}
					setState(191);
					_errHandler.sync(this);
					_alt = getInterpreter().adaptivePredict(_input,18,_ctx);
				}
				}
			}

			setState(195);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(194);
				match(WS);
				}
			}

			setState(197);
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
		enterRule(_localctx, 42, RULE_formalParam);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(199);
			match(T__12);
			setState(200);
			type();
			setState(201);
			match(T__13);
			setState(203);
			_la = _input.LA(1);
			if (_la==WS) {
				{
				setState(202);
				match(WS);
				}
			}

			setState(205);
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
		enterRule(_localctx, 44, RULE_id);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(207);
			match(LETTER);
			setState(213);
			_errHandler.sync(this);
			_la = _input.LA(1);
			while ((((_la) & ~0x3f) == 0 && ((1L << _la) & ((1L << T__17) | (1L << POSNUM) | (1L << LETTER) | (1L << SIGN))) != 0)) {
				{
				setState(211);
				switch (_input.LA(1)) {
				case LETTER:
					{
					setState(208);
					match(LETTER);
					}
					break;
				case T__17:
				case POSNUM:
					{
					setState(209);
					num();
					}
					break;
				case SIGN:
					{
					setState(210);
					match(SIGN);
					}
					break;
				default:
					throw new NoViableAltException(this);
				}
				}
				setState(215);
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
		enterRule(_localctx, 46, RULE_num);
		int _la;
		try {
			enterOuterAlt(_localctx, 1);
			{
			setState(216);
			_la = _input.LA(1);
			if ( !(_la==T__17 || _la==POSNUM) ) {
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
		"\3\u0430\ud6d1\u8206\uad2d\u4417\uaef1\u8d80\uaadd\3\32\u00dd\4\2\t\2"+
		"\4\3\t\3\4\4\t\4\4\5\t\5\4\6\t\6\4\7\t\7\4\b\t\b\4\t\t\t\4\n\t\n\4\13"+
		"\t\13\4\f\t\f\4\r\t\r\4\16\t\16\4\17\t\17\4\20\t\20\4\21\t\21\4\22\t\22"+
		"\4\23\t\23\4\24\t\24\4\25\t\25\4\26\t\26\4\27\t\27\4\30\t\30\4\31\t\31"+
		"\3\2\3\2\3\2\3\3\3\3\3\3\3\4\3\4\3\4\3\4\3\4\5\4>\n\4\3\5\3\5\5\5B\n\5"+
		"\3\5\3\5\5\5F\n\5\3\5\5\5I\n\5\3\6\3\6\5\6M\n\6\3\6\3\6\5\6Q\n\6\3\6\3"+
		"\6\3\7\3\7\3\7\3\b\3\b\3\b\3\b\3\b\3\t\3\t\3\t\3\t\3\t\3\n\3\n\5\nd\n"+
		"\n\3\13\5\13g\n\13\3\13\3\13\3\f\3\f\3\f\6\fn\n\f\r\f\16\fo\3\r\3\r\3"+
		"\r\3\r\5\rv\n\r\3\r\5\ry\n\r\3\16\3\16\3\16\3\17\3\17\3\17\3\20\3\20\3"+
		"\20\3\21\3\21\3\22\3\22\3\22\3\22\3\22\3\22\7\22\u008c\n\22\f\22\16\22"+
		"\u008f\13\22\3\22\3\22\3\23\3\23\3\23\3\23\3\24\3\24\3\24\5\24\u009a\n"+
		"\24\3\24\5\24\u009d\n\24\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\25\3\26"+
		"\3\26\3\26\3\26\5\26\u00ab\n\26\3\26\3\26\3\26\3\26\3\26\3\26\5\26\u00b3"+
		"\n\26\3\26\3\26\5\26\u00b7\n\26\3\26\3\26\5\26\u00bb\n\26\3\26\7\26\u00be"+
		"\n\26\f\26\16\26\u00c1\13\26\5\26\u00c3\n\26\3\26\5\26\u00c6\n\26\3\26"+
		"\3\26\3\27\3\27\3\27\3\27\5\27\u00ce\n\27\3\27\3\27\3\30\3\30\3\30\3\30"+
		"\7\30\u00d6\n\30\f\30\16\30\u00d9\13\30\3\31\3\31\3\31\2\2\32\2\4\6\b"+
		"\n\f\16\20\22\24\26\30\32\34\36 \"$&(*,.\60\2\3\4\2\24\24\26\26\u00e2"+
		"\2\62\3\2\2\2\4\65\3\2\2\2\6=\3\2\2\2\b?\3\2\2\2\nL\3\2\2\2\fT\3\2\2\2"+
		"\16W\3\2\2\2\20\\\3\2\2\2\22c\3\2\2\2\24f\3\2\2\2\26m\3\2\2\2\30u\3\2"+
		"\2\2\32z\3\2\2\2\34}\3\2\2\2\36\u0080\3\2\2\2 \u0083\3\2\2\2\"\u0085\3"+
		"\2\2\2$\u0092\3\2\2\2&\u0096\3\2\2\2(\u009e\3\2\2\2*\u00a6\3\2\2\2,\u00c9"+
		"\3\2\2\2.\u00d1\3\2\2\2\60\u00da\3\2\2\2\62\63\5\6\4\2\63\64\7\32\2\2"+
		"\64\3\3\2\2\2\65\66\5*\26\2\66\67\7\32\2\2\67\5\3\2\2\28>\7\25\2\29>\5"+
		"\b\5\2:>\5\n\6\2;>\5\f\7\2<>\5\16\b\2=8\3\2\2\2=9\3\2\2\2=:\3\2\2\2=;"+
		"\3\2\2\2=<\3\2\2\2>\7\3\2\2\2?H\5.\30\2@B\7\31\2\2A@\3\2\2\2AB\3\2\2\2"+
		"BC\3\2\2\2CE\7\3\2\2DF\7\31\2\2ED\3\2\2\2EF\3\2\2\2FG\3\2\2\2GI\5\6\4"+
		"\2HA\3\2\2\2HI\3\2\2\2I\t\3\2\2\2JM\5\24\13\2KM\5\20\t\2LJ\3\2\2\2LK\3"+
		"\2\2\2MN\3\2\2\2NP\7\4\2\2OQ\7\31\2\2PO\3\2\2\2PQ\3\2\2\2QR\3\2\2\2RS"+
		"\5&\24\2S\13\3\2\2\2TU\7\5\2\2UV\5*\26\2V\r\3\2\2\2WX\7\6\2\2XY\7\26\2"+
		"\2YZ\7\7\2\2Z[\5\6\4\2[\17\3\2\2\2\\]\7\b\2\2]^\5\22\n\2^_\7\t\2\2_`\5"+
		"\30\r\2`\21\3\2\2\2ad\5\20\t\2bd\5\24\13\2ca\3\2\2\2cb\3\2\2\2d\23\3\2"+
		"\2\2eg\5\26\f\2fe\3\2\2\2fg\3\2\2\2gh\3\2\2\2hi\5\30\r\2i\25\3\2\2\2j"+
		"k\5.\30\2kl\7\n\2\2ln\3\2\2\2mj\3\2\2\2no\3\2\2\2om\3\2\2\2op\3\2\2\2"+
		"p\27\3\2\2\2qv\5\32\16\2rv\5\34\17\2sv\5\36\20\2tv\5 \21\2uq\3\2\2\2u"+
		"r\3\2\2\2us\3\2\2\2ut\3\2\2\2vx\3\2\2\2wy\5\"\22\2xw\3\2\2\2xy\3\2\2\2"+
		"y\31\3\2\2\2z{\7\13\2\2{|\5 \21\2|\33\3\2\2\2}~\7\f\2\2~\177\5 \21\2\177"+
		"\35\3\2\2\2\u0080\u0081\7\r\2\2\u0081\u0082\5 \21\2\u0082\37\3\2\2\2\u0083"+
		"\u0084\5.\30\2\u0084!\3\2\2\2\u0085\u0086\7\16\2\2\u0086\u0087\7\26\2"+
		"\2\u0087\u0088\7\17\2\2\u0088\u008d\5$\23\2\u0089\u008a\7\4\2\2\u008a"+
		"\u008c\5$\23\2\u008b\u0089\3\2\2\2\u008c\u008f\3\2\2\2\u008d\u008b\3\2"+
		"\2\2\u008d\u008e\3\2\2\2\u008e\u0090\3\2\2\2\u008f\u008d\3\2\2\2\u0090"+
		"\u0091\7\20\2\2\u0091#\3\2\2\2\u0092\u0093\7\17\2\2\u0093\u0094\5\b\5"+
		"\2\u0094\u0095\7\20\2\2\u0095%\3\2\2\2\u0096\u009c\5.\30\2\u0097\u0099"+
		"\7\4\2\2\u0098\u009a\7\31\2\2\u0099\u0098\3\2\2\2\u0099\u009a\3\2\2\2"+
		"\u009a\u009b\3\2\2\2\u009b\u009d\5(\25\2\u009c\u0097\3\2\2\2\u009c\u009d"+
		"\3\2\2\2\u009d\'\3\2\2\2\u009e\u009f\5\60\31\2\u009f\u00a0\7\n\2\2\u00a0"+
		"\u00a1\5\60\31\2\u00a1\u00a2\7\n\2\2\u00a2\u00a3\5\60\31\2\u00a3\u00a4"+
		"\7\n\2\2\u00a4\u00a5\5\60\31\2\u00a5)\3\2\2\2\u00a6\u00a7\7\17\2\2\u00a7"+
		"\u00a8\5\6\4\2\u00a8\u00aa\7\20\2\2\u00a9\u00ab\7\31\2\2\u00aa\u00a9\3"+
		"\2\2\2\u00aa\u00ab\3\2\2\2\u00ab\u00ac\3\2\2\2\u00ac\u00ad\7\17\2\2\u00ad"+
		"\u00ae\5\6\4\2\u00ae\u00af\7\21\2\2\u00af\u00b0\5.\30\2\u00b0\u00b2\7"+
		"\22\2\2\u00b1\u00b3\7\31\2\2\u00b2\u00b1\3\2\2\2\u00b2\u00b3\3\2\2\2\u00b3"+
		"\u00c2\3\2\2\2\u00b4\u00bf\5,\27\2\u00b5\u00b7\7\31\2\2\u00b6\u00b5\3"+
		"\2\2\2\u00b6\u00b7\3\2\2\2\u00b7\u00b8\3\2\2\2\u00b8\u00ba\7\4\2\2\u00b9"+
		"\u00bb\7\31\2\2\u00ba\u00b9\3\2\2\2\u00ba\u00bb\3\2\2\2\u00bb\u00bc\3"+
		"\2\2\2\u00bc\u00be\5,\27\2\u00bd\u00b6\3\2\2\2\u00be\u00c1\3\2\2\2\u00bf"+
		"\u00bd\3\2\2\2\u00bf\u00c0\3\2\2\2\u00c0\u00c3\3\2\2\2\u00c1\u00bf\3\2"+
		"\2\2\u00c2\u00b4\3\2\2\2\u00c2\u00c3\3\2\2\2\u00c3\u00c5\3\2\2\2\u00c4"+
		"\u00c6\7\31\2\2\u00c5\u00c4\3\2\2\2\u00c5\u00c6\3\2\2\2\u00c6\u00c7\3"+
		"\2\2\2\u00c7\u00c8\7\23\2\2\u00c8+\3\2\2\2\u00c9\u00ca\7\17\2\2\u00ca"+
		"\u00cb\5\6\4\2\u00cb\u00cd\7\20\2\2\u00cc\u00ce\7\31\2\2\u00cd\u00cc\3"+
		"\2\2\2\u00cd\u00ce\3\2\2\2\u00ce\u00cf\3\2\2\2\u00cf\u00d0\5.\30\2\u00d0"+
		"-\3\2\2\2\u00d1\u00d7\7\27\2\2\u00d2\u00d6\7\27\2\2\u00d3\u00d6\5\60\31"+
		"\2\u00d4\u00d6\7\30\2\2\u00d5\u00d2\3\2\2\2\u00d5\u00d3\3\2\2\2\u00d5"+
		"\u00d4\3\2\2\2\u00d6\u00d9\3\2\2\2\u00d7\u00d5\3\2\2\2\u00d7\u00d8\3\2"+
		"\2\2\u00d8/\3\2\2\2\u00d9\u00d7\3\2\2\2\u00da\u00db\t\2\2\2\u00db\61\3"+
		"\2\2\2\32=AEHLPcfoux\u008d\u0099\u009c\u00aa\u00b2\u00b6\u00ba\u00bf\u00c2"+
		"\u00c5\u00cd\u00d5\u00d7";
	public static final ATN _ATN =
		new ATNDeserializer().deserialize(_serializedATN.toCharArray());
	static {
		_decisionToDFA = new DFA[_ATN.getNumberOfDecisions()];
		for (int i = 0; i < _ATN.getNumberOfDecisions(); i++) {
			_decisionToDFA[i] = new DFA(_ATN.getDecisionState(i), i);
		}
	}
}