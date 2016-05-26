package exec.recommender_reimplementation.pbn;

import static cc.kave.commons.pointsto.extraction.CoReNameConverter.convert;
import static org.junit.Assert.*;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Test;
import org.mockito.Mockito;

import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.recommenders.usages.CallSite;
import cc.recommenders.usages.CallSites;
import cc.recommenders.usages.DefinitionSites;
import cc.recommenders.usages.Query;
import cc.recommenders.usages.Usage;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import exec.recommender_reimplementation.frequency_recommender.TestUtil;

public class PBNAnalysisVisitorTest extends PBNAnalysisBaseTest {

	@Test
	public void createsUsageListForExampleSST() {
		// also tests creation of Field and Parameter Definition Site
		List<Usage> usageList = new ArrayList<>();
		sst.accept(pbnAnalysisVisitor, usageList);
		
		Query queryA = new Query();
		queryA.setType(convert(int32Type));
		queryA.setClassContext(convert(enclosingType));
		queryA.setMethodContext(convert(methodDecl.getName()));
		queryA.setDefinition(DefinitionSites.createDefinitionByField(convert(fieldDecl.getName())));
		HashSet<CallSite> callSiteSet = Sets.newHashSet(
				CallSites.createReceiverCallSite(convert(invocation.getMethodName())),
				CallSites.createReceiverCallSite(convert(invocation2.getMethodName())));
		
		queryA.setAllCallsites(callSiteSet);
		
		Query queryB = new Query();
		queryB.setType(convert(stringType));
		queryB.setClassContext(convert(enclosingType));
		queryB.setMethodContext(convert(methodDecl.getName()));
		queryB.setDefinition(DefinitionSites.createDefinitionByParam(convert(methodDecl.getName()), 0));
		queryB.setAllCallsites(Sets.newHashSet(CallSites.createParameterCallSite(convert(invocation.getMethodName()),0)));
		
		assertThat(usageList, Matchers.containsInAnyOrder(queryA, queryB));
	}
	
	@Test
	public void ignoresStatementsInExceptionHandling() {
		InvocationExpression otherInvocation = Mockito.mock(InvocationExpression.class);
		otherInvocation.setReference(varRef);
		otherInvocation.setMethodName(TestUtil.method1);
		exprStatement = new ExpressionStatement();
		exprStatement.setExpression(otherInvocation);
		
		TryBlock tryBlock = new TryBlock();
		CatchBlock catchBlock = new CatchBlock();
		
		catchBlock.setBody(Lists.newArrayList(exprStatement));
		List<ICatchBlock> catchBlocks = Lists.newArrayList(catchBlock); 
		tryBlock.setCatchBlocks(catchBlocks);
		
		methodDecl.getBody().add(tryBlock);
		
		methodDecl.accept(pbnAnalysisVisitor, Lists.newArrayList());
		
		verify(otherInvocation, never()).accept(eq(pbnAnalysisVisitor), Mockito.any());
	}

}
