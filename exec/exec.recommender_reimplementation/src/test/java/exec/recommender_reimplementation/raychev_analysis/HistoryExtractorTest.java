package exec.recommender_reimplementation.raychev_analysis;

import static org.junit.Assert.*;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.*;

import java.util.Set;

import org.junit.Test;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;

public class HistoryExtractorTest extends RaychevAnalysisBaseTest {

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

		Set<ConcreteHistory> actualConcreteHistories = HistoryExtractor.extractHistories(context);

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
}
