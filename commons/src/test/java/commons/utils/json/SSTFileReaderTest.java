package commons.utils.json;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.utils.json.JsonUtils;

public class SSTFileReaderTest {

	@Test
	public void readsEachLine() throws IOException {
		InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
				"KaVE.Feedback.sln_SyntaxTrees.SST.log");
		List<SST> SSTList = parseSSTs(inputStream);
		assertEquals(14, SSTList.size());
	}

	private List<SST> parseSSTs(InputStream inputStream) throws IOException {
		InputStreamReader reader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(reader);
		String line;

		List<SST> SSTList = new LinkedList<>();

		while ((line = bufferedReader.readLine()) != null) {
			SSTList.add(JsonUtils.parseJson(line, SST.class));
		}
		return SSTList;
	}

	@Test
	public void parsesLineToSST() throws IOException {
		String input = "{\"$type\":\"[SST:SST]\",\"EnclosingType\":\"CSharp.TypeName:T,P\",\"Fields\":[{\"$type\":\"[SST:Declarations.FieldDeclaration]\",\"Name\":\"CSharp.FieldName:[T4,P] [T5,P].F\"}],\"Properties\":[{\"$type\":\"[SST:Declarations.PropertyDeclaration]\",\"Name\":\"CSharp.PropertyName:[T10,P] [T11,P].P\",\"Get\":[{\"$type\":\"[SST:Statements.ReturnStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"IsVoid\":false}],\"Set\":[{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}}]}],\"Methods\":[{\"$type\":\"[SST:Declarations.MethodDeclaration]\",\"Name\":\"CSharp.MethodName:[T6,P] [T7,P].M1()\",\"IsEntryPoint\":false,\"Body\":[{\"$type\":\"[SST:Blocks.DoLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.ForEachLoop]\",\"Declaration\":{\"$type\":\"[SST:Statements.VariableDeclaration]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Type\":\"CSharp.UnknownTypeName:?\"},\"LoopedReference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.ForLoop]\",\"Init\":[],\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Step\":[],\"Body\":[]},{\"$type\":\"[SST:Blocks.IfElseBlock]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Then\":[],\"Else\":[]},{\"$type\":\"[SST:Blocks.LockBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.SwitchBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Sections\":[],\"DefaultSection\":[]},{\"$type\":\"[SST:Blocks.TryBlock]\",\"Body\":[{\"$type\":\"[SST:Statements.ContinueStatement]\"}],\"CatchBlocks\":[{\"$type\":\"[SST:Blocks.CatchBlock]\",\"Parameter\":\"CSharp.ParameterName:[T,P] p\",\"Body\":[{\"$type\":\"[SST:Statements.ContinueStatement]\"}],\"IsGeneral\":true,\"IsUnnamed\":true}],\"Finally\":[{\"$type\":\"[SST:Statements.BreakStatement]\"}]},{\"$type\":\"[SST:Blocks.UncheckedBlock]\",\"Body\":[]},{\"$type\":\"[SST:Blocks.UnsafeBlock]\"},{\"$type\":\"[SST:Blocks.UsingBlock]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"Body\":[]},{\"$type\":\"[SST:Blocks.WhileLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"Body\":[]},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.BreakStatement]\"},{\"$type\":\"[SST:Statements.ContinueStatement]\"},{\"$type\":\"[SST:Statements.ExpressionStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.GotoStatement]\",\"Label\":\"\"},{\"$type\":\"[SST:Statements.LabelledStatement]\",\"Label\":\"\",\"Statement\":{\"$type\":\"[SST:Statements.UnknownStatement]\"}},{\"$type\":\"[SST:Statements.ReturnStatement]\",\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"IsVoid\":false},{\"$type\":\"[SST:Statements.ThrowStatement]\",\"Exception\":\"CSharp.UnknownTypeName:?\"},{\"$type\":\"[SST:Statements.UnknownStatement]\"},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.CompletionExpression]\",\"Token\":\"\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.ComposedExpression]\",\"References\":[]}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.IfElseExpression]\",\"Condition\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"ThenExpression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"},\"ElseExpression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.InvocationExpression]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"MethodName\":\"CSharp.MethodName:[?] [?].???()\",\"Parameters\":[]}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Assignable.LambdaExpression]\",\"Name\":\"CSharp.LambdaName:???\",\"Body\":[]}},{\"$type\":\"[SST:Blocks.WhileLoop]\",\"Condition\":{\"$type\":\"[SST:Expressions.LoopHeader.LoopHeaderBlockExpression]\",\"Body\":[]},\"Body\":[]},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ConstantValueExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.NullExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.UnknownExpression]\"}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.EventReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"EventName\":\"CSharp.EventName:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.FieldReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"FieldName\":\"CSharp.FieldName:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.MethodReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"MethodName\":\"CSharp.MethodName:[?] [?].???()\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.PropertyReference]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"},\"PropertyName\":\"CSharp.PropertyName:[?] [?].???\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"}}},{\"$type\":\"[SST:Statements.Assignment]\",\"Reference\":{\"$type\":\"[SST:References.UnknownReference]\"},\"Expression\":{\"$type\":\"[SST:Expressions.Simple.ReferenceExpression]\",\"Reference\":{\"$type\":\"[SST:References.VariableReference]\",\"Identifier\":\"\"}}}]},{\"$type\":\"[SST:Declarations.MethodDeclaration]\",\"Name\":\"CSharp.MethodName:[T8,P] [T9,P].M2()\",\"IsEntryPoint\":true,\"Body\":[]}],\"Events\":[{\"$type\":\"[SST:Declarations.EventDeclaration]\",\"Name\":\"CSharp.EventName:[T2,P] [T3,P].E\"}],\"Delegates\":[{\"$type\":\"[SST:Declarations.DelegateDeclaration]\",\"Name\":\"CSharp.DelegateTypeName:d:[R,P] [T2,P].()\"}]}";
		ByteArrayInputStream inputStream = new ByteArrayInputStream(input.getBytes());
		List<SST> SSTList = parseSSTs(inputStream);

		assertThat(SSTList.get(0), equalTo(SSTDeserializationTest.getExample()));
	}

}
