package commons.utils.json;

import java.util.List;

import cc.kave.commons.model.names.csharp.CsDelegateTypeName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsParameterName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.IReference;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.expressions.IAssignableExpression;
import cc.kave.commons.model.ssts.expressions.loopheader.ILoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.DoLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForEachLoop;
import cc.kave.commons.model.ssts.impl.blocks.ForLoop;
import cc.kave.commons.model.ssts.impl.blocks.IfElseBlock;
import cc.kave.commons.model.ssts.impl.blocks.LockBlock;
import cc.kave.commons.model.ssts.impl.blocks.SwitchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.blocks.UncheckedBlock;
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;
import cc.kave.commons.model.ssts.impl.blocks.UsingBlock;
import cc.kave.commons.model.ssts.impl.blocks.WhileLoop;
import cc.kave.commons.model.ssts.impl.declarations.DelegateDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.EventDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.MethodDeclaration;
import cc.kave.commons.model.ssts.impl.declarations.PropertyDeclaration;
import cc.kave.commons.model.ssts.impl.expressions.assignable.CompletionExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.ComposedExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.IfElseExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.InvocationExpression;
import cc.kave.commons.model.ssts.impl.expressions.assignable.LambdaExpression;
import cc.kave.commons.model.ssts.impl.expressions.loopheader.LoopHeaderBlockExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ConstantValueExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.NullExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.ReferenceExpression;
import cc.kave.commons.model.ssts.impl.expressions.simple.UnknownExpression;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.MethodReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.UnknownReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import cc.kave.commons.model.ssts.impl.statements.Assignment;
import cc.kave.commons.model.ssts.impl.statements.BreakStatement;
import cc.kave.commons.model.ssts.impl.statements.ContinueStatement;
import cc.kave.commons.model.ssts.impl.statements.ExpressionStatement;
import cc.kave.commons.model.ssts.impl.statements.GotoStatement;
import cc.kave.commons.model.ssts.impl.statements.LabelledStatement;
import cc.kave.commons.model.ssts.impl.statements.ReturnStatement;
import cc.kave.commons.model.ssts.impl.statements.ThrowStatement;
import cc.kave.commons.model.ssts.impl.statements.UnknownStatement;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

public class SSTTestfixture {

	public static ISST getExample() {
		SST sut = new SST();

		FieldDeclaration fieldDeclaration = new FieldDeclaration();
		fieldDeclaration.setName(CsFieldName.newFieldName("[T4,P] [T5,P].F"));

		PropertyDeclaration propertyDeclaration = new PropertyDeclaration();
		propertyDeclaration.setName(CsPropertyName.newPropertyName("[T10,P] [T11,P].P"));
		propertyDeclaration.setGet(Lists.newArrayList(new ReturnStatement()));
		propertyDeclaration.setSet(Lists.newArrayList(new Assignment()));

		EventDeclaration eventDeclaration = new EventDeclaration();
		eventDeclaration.setName(CsEventName.newEventName("[T2,P] [T3,P].E"));

		DelegateDeclaration delegateDeclaration = new DelegateDeclaration();
		delegateDeclaration.setName(CsDelegateTypeName.newDelegateTypeName("d:[R,P] [T2,P].()"));

		MethodDeclaration methodDeclaration = new MethodDeclaration();
		methodDeclaration.setName(CsMethodName.newMethodName("[T6,P] [T7,P].M1()"));
		methodDeclaration.setEntryPoint(false);
		methodDeclaration.setBody(SSTTestfixture.createBody(true));
		MethodDeclaration methodDeclaration2 = new MethodDeclaration();
		methodDeclaration2.setName(CsMethodName.newMethodName("[T8,P] [T9,P].M2()"));
		methodDeclaration2.setEntryPoint(true);

		sut.setEnclosingType(CsTypeName.newTypeName("T,P"));
		sut.setProperties(Sets.newHashSet(propertyDeclaration));
		sut.setFields(Sets.newHashSet(fieldDeclaration));
		sut.setDelegates(Sets.newHashSet(delegateDeclaration));
		sut.setEvents(Sets.newHashSet(eventDeclaration));
		sut.setMethods(Sets.newHashSet(methodDeclaration, methodDeclaration2));

		return sut;
	}

	static List<IStatement> createBody(boolean complex) {
		return Lists.newArrayList(new DoLoop(), new ForEachLoop(), new ForLoop(), new IfElseBlock(), new LockBlock(),
				new SwitchBlock(), complex ? SSTTestfixture.createComplexTryBlock() : new TryBlock(),
				new UncheckedBlock(), new UnsafeBlock(), new UsingBlock(), new WhileLoop(), new Assignment(),
				new BreakStatement(), new ContinueStatement(), new ExpressionStatement(), new GotoStatement(),
				new LabelledStatement(), new ReturnStatement(), new ThrowStatement(), new UnknownStatement(),
				SSTTestfixture.nested(new CompletionExpression()), SSTTestfixture.nested(new ComposedExpression()),
				SSTTestfixture.nested(new IfElseExpression()), SSTTestfixture.nested(new InvocationExpression()),
				SSTTestfixture.nested(new LambdaExpression()), SSTTestfixture.nested(new LoopHeaderBlockExpression()),
				SSTTestfixture.nested(new ConstantValueExpression()), SSTTestfixture.nested(new NullExpression()),
				SSTTestfixture.nested(new ReferenceExpression()),
				SSTTestfixture.nested(new UnknownExpression()),
				//
				SSTTestfixture.nested(new EventReference()), SSTTestfixture.nested(new FieldReference()),
				SSTTestfixture.nested(new MethodReference()), SSTTestfixture.nested(new PropertyReference()),
				SSTTestfixture.nested(new UnknownReference()), SSTTestfixture.nested(new VariableReference()));

	}

	static TryBlock createComplexTryBlock() {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(Lists.newArrayList(new ContinueStatement()));
		tryBlock.setFinally(Lists.newArrayList(new BreakStatement()));
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setParameter(CsParameterName.newParameterName("[T,P] p"));
		catchBlock.setBody(Lists.newArrayList(new ContinueStatement()));
		catchBlock.setGeneral(true);
		catchBlock.setUnnamed(true);
		tryBlock.setCatchBlocks(Lists.newArrayList(catchBlock));
		return tryBlock;
	}

	static IStatement nested(ILoopHeaderBlockExpression expr) {
		WhileLoop whileLoop = new WhileLoop();
		whileLoop.setCondition(expr);
		return whileLoop;
	}

	static IStatement nested(IAssignableExpression expr) {
		Assignment assignment = new Assignment();
		assignment.setExpression(expr);
		return assignment;
	}

	static IStatement nested(IReference reference) {
		Assignment assignment = new Assignment();
		ReferenceExpression referenceExpression = new ReferenceExpression();
		referenceExpression.setReference(reference);
		assignment.setExpression(referenceExpression);
		return assignment;
	}

	public static String getExampleJson_Current() {
		return "{\"$type\":\"[SST:SST]\",\"EnclosingType\":\"CSharp.TypeName:T,P\",\"Fields\":[{\"$type\":\"[SST:Declarations.FieldDeclaration]\",\"Name\":\"CSharp.FieldName:[T4,P] [T5,P].F\"}],\"Properties\":[{\"$type\":\"[SST:Declarations.PropertyDeclaration]\",\"Name\":\"CSharp.PropertyName:[T10,P] [T11,P].P\",\"Get\":[{\"$type\":\"[SST:Statements.ReturnStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"IsVoid\":false}],\"Set\":[{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}}]}],\"Methods\":[{\"$type\":\"[SST:Declarations.MethodDeclaration]\",\"Name\":\"CSharp.MethodName:[T6,P] [T7,P].M1()\",\"IsEntryPoint\":false,\"Body\":[{\"$type\":\"[SST:Blocks.DoLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.ForEachLoop]\",\"Declaration\":{\"$type\":\"[SST:Statements.VariableDeclaration]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Type\":\"CSharp.UnknownTypeName:?\"},\"LoopedReference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.ForLoop]\",\"Init\":[],\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Step\":[],\"Body\":[]},{\"$type\":\"[SST:Blocks.IfElseBlock]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Then\":[],\"Else\":[]},{\"$type\":\"[SST:Blocks.LockBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.SwitchBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Sections\":[],\"DefaultSection\":[]},{\"$type\":\"[SST:Blocks.TryBlock]\",\"Body\":[{\"$type\":\"[SST:Statements.ContinueStatement]\"}],\"CatchBlocks\":[{\"$type\":\"[SST:Blocks.CatchBlock]\",\"Parameter\":\"CSharp.ParameterName:[T,P] p\",\"Body\":[{\"$type\":\"[SST:Statements.ContinueStatement]\"}],\"IsGeneral\":true,\"IsUnnamed\":true}],\"Finally\":[{\"$type\":\"[SST:Statements.BreakStatement]\"}]},{\"$type\":\"[SST:Blocks.UncheckedBlock]\",\"Body\":[]},{\"$type\":\"[SST:Blocks.UnsafeBlock]\"},{\"$type\":\"[SST:Blocks.UsingBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.WhileLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Body\":[]},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.BreakStatement]\"},{\"$type\":\"[SST:Statements.ContinueStatement]\"},{\"$type\":\"[SST:Statements.ExpressionStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.GotoStatement]\",\"Label\":\"\"},{\"$type\":\"[SST:Statements.LabelledStatement]\",\"Label\":\"\",\"Statement\":{\"$type\":\"[SST:Statements.UnknownStatement]\"}},{\"$type\":\"[SST:Statements.ReturnStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"IsVoid\":false},{\"$type\":\"[SST:Statements.ThrowStatement]\",\"Exception\":\"CSharp.UnknownTypeName:?\"},{\"$type\":\"[SST:Statements.UnknownStatement]\"},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.CompletionExpression]\",\"Token\":\"\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.ComposedExpression]\",\"References\":[]}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.IfElseExpression]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"ThenExpression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"ElseExpression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.InvocationExpression]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"MethodName\":\"CSharp.MethodName:[?] [?].???()\",\"Parameters\":[]}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.LambdaExpression]\",\"Name\":\"CSharp.LambdaName:???\",\"Body\":[]}},{\"$type\":\"[SST:Blocks.WhileLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.LoopHeader.LoopHeaderBlockExpression]\",\"Body\":[]},\"Body\":[]},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ConstantValueExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.NullExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.EventReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"EventName\":\"CSharp.EventName:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.FieldReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"FieldName\":\"CSharp.FieldName:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.MethodReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"MethodName\":\"CSharp.MethodName:[?] [?].???()\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.PropertyReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"PropertyName\":\"CSharp.PropertyName:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"}}}]},{\"$type\":\"[SST:Declarations.MethodDeclaration]\",\"Name\":\"CSharp.MethodName:[T8,P] [T9,P].M2()\",\"IsEntryPoint\":true,\"Body\":[]}],\"Events\":[{\"$type\":\"[SST:Declarations.EventDeclaration]\",\"Name\":\"CSharp.EventName:[T2,P] [T3,P].E\"}],\"Delegates\":[{\"$type\":\"[SST:Declarations.DelegateDeclaration]\",\"Name\":\"CSharp.DelegateTypeName:d:[R,P] [T2,P].()\"}]}";
	}

}
