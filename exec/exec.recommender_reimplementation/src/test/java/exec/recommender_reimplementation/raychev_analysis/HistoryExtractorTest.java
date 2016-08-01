package exec.recommender_reimplementation.raychev_analysis;

import static org.junit.Assert.*;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.*;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;

public class HistoryExtractorTest extends RaychevAnalysisBaseTest {

	HistoryExtractor sut;
	
	@Before
	public void setup() {
		sut = new HistoryExtractor();
	}
	
	@Test
	public void IntegrationTest() {
		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.setThen(Lists.newArrayList(
				varDecl("msgList", type("ArrayList")),
				assign("msgList",
						invokeWithParameters(
								"smsMgr",
								method(type("ArrayList"), type("SmsManager"), "divideMsg",
										parameter(type("Message"), "message")), referenceExpr(varRef("message")))),
				invokeStmt(invokeWithParameters(
						"smsMgr",
						method(voidType, type("SmsManager"), "sendMultipartTextMessage",
								parameter(type("ArrayList"), "msgList")), referenceExpr(varRef("msgList"))))));
		ifElseBlock.setElse(Lists.newArrayList(invokeStmt(invokeWithParameters("smsMgr",
				method(voidType, type("SmsManager"), "sendTextMessage", parameter(type("Message"), "message")),
				referenceExpr(varRef("message"))))));

		setupDefaultEnclosingMethod(varDecl("message", type("Message")), assign("message", new UnknownExpression()),
				varDecl("smsMgr", type("SmsManager")),
				assign("smsMgr", invokeStatic(method(type("SmsManager"), DefaultClassContext, "getDefault"))),
				varDecl("length", intType),
				assign("length", invoke("message", method(intType, DefaultClassContext, "length"))), ifElseBlock);

		Set<ConcreteHistory> actualConcreteHistories = sut.extractHistories(context);

		Set<ConcreteHistory> expectedHistories = Sets.newHashSet(
				createConcreteHistory(
						//
						callAtPosition(method(type("SmsManager"), DefaultClassContext, "getDefault"),
								Interaction.RETURN),
						callAtPosition(
								method(voidType, type("SmsManager"), "sendTextMessage",
										parameter(type("Message"), "message")), 0)),
				createConcreteHistory(//
				callAtPosition(method(intType, DefaultClassContext, "length"), Interaction.RETURN)),
				createConcreteHistory(
						//
						callAtPosition(method(type("SmsManager"), DefaultClassContext, "getDefault"),
								Interaction.RETURN),
						callAtPosition(
								method(type("ArrayList"), type("SmsManager"), "divideMsg",
										parameter(type("Message"), "message")), 0),
						callAtPosition(
								method(voidType, type("SmsManager"), "sendMultipartTextMessage",
										parameter(type("ArrayList"), "msgList")), 0)),
				createConcreteHistory(
						//
						callAtPosition(method(intType, DefaultClassContext, "length"), 0),
						callAtPosition(
								method(type("ArrayList"), type("SmsManager"), "divideMsg",
										parameter(type("Message"), "message")), 1)),
				createConcreteHistory(
						//
						callAtPosition(method(intType, DefaultClassContext, "length"), 0),
						callAtPosition(
								method(voidType, type("SmsManager"), "sendTextMessage",
										parameter(type("Message"), "message")), 1)),
				createConcreteHistory(
						//
						callAtPosition(
								method(type("ArrayList"), type("SmsManager"), "divideMsg",
										parameter(type("Message"), "message")), Interaction.RETURN),
						callAtPosition(
								method(voidType, type("SmsManager"), "sendMultipartTextMessage",
										parameter(type("ArrayList"), "msgList")), 1)));
		assertEquals(expectedHistories, actualConcreteHistories);
	}

	@Test
	public void printSentencesTest() {
		Set<ConcreteHistory> histories = Sets.newHashSet(
				createConcreteHistory(
						//
						callAtPosition(method(type("SmsManager"), DefaultClassContext, "getDefault"),
								Interaction.RETURN),
						callAtPosition(
								method(voidType, type("SmsManager"), "sendTextMessage",
										parameter(type("Message"), "message")), 0)),
				createConcreteHistory(//
				callAtPosition(method(intType, DefaultClassContext, "length"), Interaction.RETURN)),
				createConcreteHistory(
						//
						callAtPosition(method(type("SmsManager"), DefaultClassContext, "getDefault"),
								Interaction.RETURN),
						callAtPosition(
								method(type("ArrayList"), type("SmsManager"), "divideMsg",
										parameter(type("Message"), "message")), 0),
						callAtPosition(
								method(voidType, type("SmsManager"), "sendMultipartTextMessage",
										parameter(type("ArrayList"), "msgList")), 0)),
				createConcreteHistory(
						//
						callAtPosition(method(intType, DefaultClassContext, "length"), 0),
						callAtPosition(
								method(type("ArrayList"), type("SmsManager"), "divideMsg",
										parameter(type("Message"), "message")), 1)),
				createConcreteHistory(
						//
						callAtPosition(method(intType, DefaultClassContext, "length"), 0),
						callAtPosition(
								method(voidType, type("SmsManager"), "sendTextMessage",
										parameter(type("Message"), "message")), 1)),
				createConcreteHistory(
						//
						callAtPosition(
								method(type("ArrayList"), type("SmsManager"), "divideMsg",
										parameter(type("Message"), "message")), Interaction.RETURN),
						callAtPosition(
								method(voidType, type("SmsManager"), "sendMultipartTextMessage",
										parameter(type("ArrayList"), "msgList")), 1)));

		List<String> expectedStrings = Lists.newArrayList("TDecl.getDefault()S:R SmsManager.sendTextMessage(M)v:0/2",
				"TDecl.length()i:R",
				"TDecl.getDefault()S:R SmsManager.divideMsg(M)A:0/2 SmsManager.sendMultipartTextMessage(A)v:0/2",
				"TDecl.length()i:0/1 SmsManager.divideMsg(M)A:1/2",
				"TDecl.length()i:0/1 SmsManager.sendTextMessage(M)v:1/2",
				"SmsManager.divideMsg(M)A:R SmsManager.sendMultipartTextMessage(A)v:1/2");

		assertSentencesString(expectedStrings, sut.getHistoryAsString(histories));
	}

	@Test
	public void handlesGenerics() {
		Set<ConcreteHistory> histories = Sets
				.newHashSet(createConcreteHistory(
						//
						callAtPosition(
								method(voidType,
										TypeName.newTypeName("System.Collections.Dictionary`2[[TKey -> Int32, P1],[TValue -> String, P1]], P1"),
										"m1"), 0),
						callAtPosition(
								method(voidType, TypeName.newTypeName("System.Nullable`1[[T -> Int32, P1]], P1"), "m2"),
								0),
						callAtPosition(
								method(voidType,
										TypeName.newTypeName("d:System.Converter`2[[TInput],[TOutput -> i:System.Collections.Generic.IEnumerable`1[[T]], mscorlib, 2.0.0.0]], P1"),
										"m3"), 0)));

		List<String> expectedStrings = Lists
				.newArrayList("System.Collections.Dictionary`2[[TKey],[TValue]].m1()v:0/1 System.Nullable`1[[T]].m2()v:0/1 System.Converter`2[[TInput],[TOutput]].m3()v:0/1");

		assertSentencesString(expectedStrings, sut.getHistoryAsString(histories));
	}

	private void assertSentencesString(List<String> expectedStrings, String historyString) {
		List<String> actual = Arrays.asList(historyString.split("\n"));
		assertEquals("Different Sizes", expectedStrings.size(), actual.size());
		for (String expectedString : expectedStrings) {
			assertTrue("String missing: " + expectedString + " but was: \n" + historyString,
					actual.contains(expectedString));
		}
	}

}
