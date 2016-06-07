/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.pbn;

import org.junit.Test;

import static org.junit.Assert.*;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.ssts.expressions.assignable.IInvocationExpression;
import cc.recommenders.usages.DefinitionSite;
import cc.recommenders.usages.DefinitionSites;
import static exec.recommender_reimplementation.pbn.PBNAnalysisTestFixture.*;
import static cc.kave.commons.pointsto.extraction.CoReNameConverter.*;

public class DefinitionDetectionTest extends PBNAnalysisBaseTest {

    @Test
    public void varDefinitionByConstructor_SameType()
    {
        IInvocationExpression ctor = constructor(type("A"));

        setupDefaultEnclosingMethod(
            varDecl("a", type("A")),
            assign("a", ctor),
            invokeStmt("a", someMethodOnType("A")));

        assertSingleQueryWithType(type("A"));
        assertSingleQueryWithDefinition(DefinitionSites.createDefinitionByConstructor(convert(ctor.getMethodName())));
    }

    @Test
    public void varDefinitionByConstructor_Subtype()
    {
    	IInvocationExpression ctor = constructor(type("B"));

        setupDefaultEnclosingMethod(
            varDecl("a", type("A")),
            assign("a", ctor),
            invokeStmt("a", someMethodOnType("A")));

        assertSingleQueryWithType(type("A"));
        assertSingleQueryWithDefinition(DefinitionSites.createDefinitionByConstructor(convert(ctor.getMethodName())));
    }

    @Test
    public void definitionAsParam_Method()
    {
    	IMethodName enclosingMethod = method(voidType, DefaultClassContext, "M", parameter(type("P"), "p"));

        setupEnclosingMethod(
            enclosingMethod,
            invokeStmt("p", someMethodOnType("Q")));

        assertSingleQueryWithType(type("P"));
        assertSingleQueryWithDefinition(DefinitionSites.createDefinitionByParam(convert(enclosingMethod), 0));
    }


    @Test
    public void varDefinitionByReturn_ReturnsSameType()
    {
        IMethodName callee = method(type("A"), DefaultClassContext, "M");

        setupDefaultEnclosingMethod(
            varDecl("a", type("A")),
            assign("a", invoke("this", callee)),
            invokeStmt("a", someMethodOnType("A")));

        assertQueriesExistFor(type("A"));

        DefinitionSite actual = findQueryWith(type("A")).getDefinitionSite();
        DefinitionSite expected = DefinitionSites.createDefinitionByReturn(convert(callee));
        assertEquals(expected, actual);
    }

    @Test
    public void varDefinitionByReturn_ReturnsSubtype()
    {
    	IMethodName callee = method(type("B"), DefaultClassContext, "M");

        setupDefaultEnclosingMethod(
            varDecl("a", type("A")),
            assign("a", invoke("this", callee)),
            invokeStmt("a", someMethodOnType("A")));

        assertQueriesExistFor(type("A"), DefaultClassContext);

        DefinitionSite actual = findQueryWith(type("A")).getDefinitionSite();
        DefinitionSite expected = DefinitionSites.createDefinitionByReturn(convert(callee));
        assertEquals(expected, actual);
    }

    @Test
    public void definitionByThis()
    {
        setupDefaultEnclosingMethod(
            invokeStmt("this", method(voidType, DefaultClassContext, "M")));

        assertSingleQueryWithDefinition(DefinitionSites.createDefinitionByThis());
    }

    @Test
    public void definitionByThis_Base()
    {
        setupDefaultEnclosingMethod(
            invokeStmt("base", method(voidType, DefaultClassContext, "M")));

        assertSingleQueryWithDefinition(DefinitionSites.createDefinitionByThis());
    }


    @Test
    public void definitionAsField()
    {
        setupDefaultEnclosingMethod(
        	varDecl("tmpF", type("F")),	
        	assign("tmpF", referenceExpr(fieldReference("this", field(type("F"), DefaultClassContext, "f")))),	
            invokeStmt("tmpF", someMethodOnType("F")));

        IFieldName fieldName = field(type("F"), DefaultClassContext, "f");
        context.getSST().getFields().add(fieldDecl(fieldName));          

        assertSingleQueryWithDefinition(DefinitionSites.createDefinitionByField(convert(fieldName)));
    }

    @Test
    public void varDefinitionByAssignment_Variable()
    {
        setupDefaultEnclosingMethod(
            varDecl("a", type("A")),
            varDecl("b", type("B")),
            assign(
                "a",
                referenceExpr(varRef("b"))),
            invokeStmt("a", someMethodOnType("A")));

        assertSingleQueryWithDefinition(DefinitionSites.createUnknownDefinitionSite());
    }

    @Test
    public void varDefinitionByAssignment_Field()
    {
        setupDefaultEnclosingMethod(
            varDecl("other", type("O")),
            varDecl("a", type("A")),
            assign(
                "a",
                referenceExpr(fieldReference("Other", field(type("A"), type("O"), "f")))),
            invokeStmt("a", someMethodOnType("A")));

        assertSingleQueryWithDefinition(DefinitionSites.createUnknownDefinitionSite());
    }
	
}
