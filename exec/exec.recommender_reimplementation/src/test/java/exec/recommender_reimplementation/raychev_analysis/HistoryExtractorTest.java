package exec.recommender_reimplementation.raychev_analysis;

import static org.junit.Assert.*;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.*;

import java.util.Set;

import org.hamcrest.Matchers;
import org.junit.Test;

import com.google.common.collect.Lists;

import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import exec.recommender_reimplementation.pbn.PBNAnalysisBaseTest;

public class HistoryExtractorTest extends PBNAnalysisBaseTest {

	@Test
	public void IntegrationTest() {
		IfElseBlock ifElseBlock = new IfElseBlock();
		ifElseBlock.setThen(Lists.newArrayList(
				varDecl("msgList", type("ArrayList")),
				assign("msgList",
						invokeWithParameters(
								"smsMgr",
								method(type("ArrayList"), type("SmsManager"),
										"divideMsg",
										parameter(type("Message"), "message")),
								referenceExpr(varRef("message")))),
				invokeStmt(invokeWithParameters(
						"smsMgr",
						method(voidType, type("SmsManager"),
								"sendMultipartTextMessage",
								parameter(type("ArrayList"), "msgList")),
						referenceExpr(varRef("msgList"))))));
		ifElseBlock.setElse(Lists.newArrayList(invokeStmt(invokeWithParameters(
				"smsMgr",
				method(voidType, type("SmsManager"), "sendTextMessage",
						parameter(type("Message"), "message")),
				referenceExpr(varRef("message"))))));

		setupDefaultEnclosingMethod(
				varDecl("message", type("Message")),
				assign("message", new UnknownExpression()),
				varDecl("smsMgr", type("SmsManager")),
				assign("smsMgr",
						invokeStatic(method(type("SmsManager"),
								DefaultClassContext, "getDefault"))),
				varDecl("length", intType),
				assign("length",
						invoke("message",
								method(intType, DefaultClassContext, "length"))),
				ifElseBlock);

		Set<ConcreteHistory> concreteHistories = HistoryExtractor
				.extractHistories(context);

		assertEquals(6, concreteHistories.size());

		assertThat(concreteHistories, Matchers.containsInAnyOrder(
				new ConcreteHistory(new Interaction(method(type("SmsManager"),
						DefaultClassContext, "getDefault"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(
						voidType, type("SmsManager"), "sendTextMessage",
						parameter(type("Message"), "message")), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(intType,
						DefaultClassContext, "length"), Interaction.RETURN,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(type("SmsManager"),
						DefaultClassContext, "getDefault"), Interaction.RETURN,
						InteractionType.MethodCall), new Interaction(method(
						type("ArrayList"), type("SmsManager"), "divideMsg",
						parameter(type("Message"), "message")), 0,
						InteractionType.MethodCall), new Interaction(method(
						voidType, type("SmsManager"),
						"sendMultipartTextMessage",
						parameter(type("ArrayList"), "msgList")), 0,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(intType,
						DefaultClassContext, "length"), 0,
						InteractionType.MethodCall), new Interaction(method(
						type("ArrayList"), type("SmsManager"), "divideMsg",
						parameter(type("Message"), "message")), 1,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(intType,
						DefaultClassContext, "length"), 0,
						InteractionType.MethodCall), new Interaction(method(
						voidType, type("SmsManager"), "sendTextMessage",
						parameter(type("Message"), "message")), 1,
						InteractionType.MethodCall)),
				new ConcreteHistory(new Interaction(method(type("ArrayList"),
						type("SmsManager"), "divideMsg",
						parameter(type("Message"), "message")),
						Interaction.RETURN, InteractionType.MethodCall),
						new Interaction(method(voidType, type("SmsManager"),
								"sendMultipartTextMessage",
								parameter(type("ArrayList"), "msgList")), 1,
								InteractionType.MethodCall))));
	}
}
