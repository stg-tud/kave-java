/**
 * Copyright 2015 Simon Reuß
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations under the License.
 */
package exec.recommender_reimplementation.pbn;

import static cc.kave.commons.model.ssts.impl.SSTUtil.assign;
import static cc.kave.commons.model.ssts.impl.SSTUtil.assignmentToLocal;
import static cc.kave.commons.model.ssts.impl.SSTUtil.constant;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareFields;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareMethod;
import static cc.kave.commons.model.ssts.impl.SSTUtil.declareVar;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationExpression;
import static cc.kave.commons.model.ssts.impl.SSTUtil.invocationStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.loopHeader;
import static cc.kave.commons.model.ssts.impl.SSTUtil.refExpr;
import static cc.kave.commons.model.ssts.impl.SSTUtil.referenceExprToVariable;
import static cc.kave.commons.model.ssts.impl.SSTUtil.returnVariable;
import static cc.kave.commons.model.ssts.impl.SSTUtil.unknownStatement;
import static cc.kave.commons.model.ssts.impl.SSTUtil.variableReference;
import static cc.kave.commons.model.ssts.impl.SSTUtil.whileLoop;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.IFieldName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.IPropertyName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.FieldName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.ParameterName;
import cc.kave.commons.model.names.csharp.PropertyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.IStatement;
import cc.kave.commons.model.ssts.blocks.CatchBlockKind;
import cc.kave.commons.model.ssts.blocks.ICatchBlock;
import cc.kave.commons.model.ssts.blocks.ITryBlock;
import cc.kave.commons.model.ssts.declarations.IFieldDeclaration;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.blocks.CatchBlock;
import cc.kave.commons.model.ssts.impl.blocks.TryBlock;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;
import cc.kave.commons.utils.json.JsonUtils;

import com.google.common.collect.Iterators;
import com.google.common.collect.Sets;

/**
 * Provides functionality related to SST construction for tests.
 */
public class TestSSTBuilder {

	public ITypeName getStringType() {
		return TypeName.newTypeName("System.String, mscorlib");
	}

	public ITypeName getVoidType() {
		return TypeName.newTypeName("System.Void, mscorlib");
	}

	public ITypeName getFileStreamType() {
		return TypeName.newTypeName("System.IO.FileStream, mscorlib");
	}

	public ITypeName getByteArrayType() {
		return TypeName.newTypeName("System.Byte[], mscorlib");
	}

	public ITypeName getInt32Type() {
		return TypeName.newTypeName("System.Int32, mscorlib");
	}

	public ITypeName getBooleanType() {
		return TypeName.newTypeName("System.Boolean, mscorlib");
	}

	public SST createEmptySST(ITypeName enclosingType) {
		SST sst = new SST();
		sst.setEnclosingType(enclosingType);
		sst.setDelegates(Collections.emptySet());
		sst.setEvents(Collections.emptySet());
		sst.setFields(Collections.emptySet());
		sst.setMethods(Collections.emptySet());
		sst.setProperties(Collections.emptySet());

		return sst;
	}

	public Context createContext(ISST sst) {
		Context context = new Context();
		context.setSST(sst);

		ITypeShape typeShape = new TypeShape();
		context.setTypeShape(typeShape);

		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(sst.getEnclosingType());
		typeShape.setTypeHierarchy(typeHierarchy);

		Set<IMethodHierarchy> methodHierarchies = new HashSet<>();
		for (IMethodDeclaration methodDecl : sst.getEntryPoints()) {
			methodHierarchies.add(new MethodHierarchy(methodDecl.getName()));
		}
		typeShape.setMethodHierarchies(methodHierarchies);

		return context;
	}

	/**
	 * Creates a SST which realizes a class that copies one source file to a specific destination via FileStream
	 * instances. The source file is specified as constructor argument and saved in a field. The source stream is
	 * constructed in a separate helper method, whereas the destination stream is created in the single entry point
	 * method <i>CopyTo</i>.
	 */
	public Context createStreamTest() {
		ITypeName streamTestType = TypeName.newTypeName("Test.StreamTest, Test");
		SST sst = createEmptySST(streamTestType);

		IFieldName sourceFieldName = FieldName
				.newFieldName("[" + getStringType().getIdentifier() + "] [Test.StreamTest, Test].source");
		sst.setFields(declareFields(sourceFieldName.getIdentifier()));

		IMethodName constructorName = MethodName.newMethodName("[" + getVoidType().getIdentifier()
				+ "] [Test.StreamTest, Test]..ctor([" + getStringType().getIdentifier() + "] source)");
		IMethodDeclaration constructorDecl = declareMethod(constructorName, true,
				assign(buildFieldReference(sourceFieldName), referenceExprToVariable("source")));

		ITypeName fileStreamType = getFileStreamType();
		IMethodName fileStreamCtorName = MethodName.newMethodName(
				"[" + getVoidType().getIdentifier() + "] [" + fileStreamType.getIdentifier() + "]..ctor(["
						+ getStringType().getIdentifier() + "] path, [" + TypeName.UNKNOWN_NAME.getIdentifier()
						+ "] mode, [" + TypeName.UNKNOWN_NAME.getIdentifier() + "] access)");
		IMethodName openSourceName = MethodName
				.newMethodName("[" + fileStreamType.getIdentifier() + "] [Test.StreamTest, Test].OpenSource()");
		IMethodDeclaration openSourceDecl = declareMethod(openSourceName, false, declareVar("tmp", fileStreamType),
				assign(variableReference("tmp"),
						invocationExpression(fileStreamCtorName, Iterators.forArray(
								refExpr(buildFieldReference(sourceFieldName)), constant("Open"), constant("Read")))),
				returnVariable("tmp"));

		IMethodName copyToName = MethodName.newMethodName("[" + getVoidType().getIdentifier()
				+ "] [Test.StreamTest, Test].CopyTo([" + getStringType().getIdentifier() + "] dest)");
		IMethodName byteArrayCtorName = MethodName.newMethodName("[" + getVoidType().getIdentifier() + "] ["
				+ getByteArrayType().getIdentifier() + "]..ctor([" + getInt32Type().getIdentifier() + "] length)");
		IMethodName fsReadName = MethodName.newMethodName("[" + getInt32Type().getIdentifier() + "] ["
				+ fileStreamType.getIdentifier() + "].Read([" + getByteArrayType().getIdentifier() + "] array, ["
				+ getInt32Type().getIdentifier() + "] offset, [" + getInt32Type().getIdentifier() + "] size)");
		IPropertyName intArrLengthName = PropertyName.newPropertyName(
				"get [" + getInt32Type().getIdentifier() + "] [" + getByteArrayType().getIdentifier() + "].Length()");
		IMethodName fsWriteName = MethodName.newMethodName(fsReadName.getIdentifier().replace(".Read(", ".Write("));
		IMethodName fsCloseName = MethodName.newMethodName(
				"[" + getVoidType().getIdentifier() + "] [" + fileStreamType.getIdentifier() + "].Close()");
		IMethodDeclaration copyToDecl = declareMethod(copyToName, true, declareVar("input", fileStreamType),
				assignmentToLocal("input", invocationExpression("this", openSourceName)),
				declareVar("output", fileStreamType),
				assignmentToLocal("output",
						invocationExpr(fileStreamCtorName, refExpr(variableReference("dest")), constant("Create"),
								constant("Write"))),
				declareVar("buffer", getByteArrayType()),
				assignmentToLocal("buffer",
						invocationExpr(byteArrayCtorName, constant("1024"))),
				declareVar("read",
						getInt32Type()),
				whileLoop(
						loopHeader(
								assignmentToLocal("read",
										invocationExpression("input", fsReadName,
												Iterators.forArray(refExpr("buffer"), constant("0"),
														refExpr(buildPropertyRef("buffer", intArrLengthName))))),
								unknownStatement()),
						invocationStatement("output", fsWriteName,
								Iterators.forArray(refExpr("buffer"), constant("0"), refExpr("read")))),
				invocationStatement("input", fsCloseName), invocationStatement("output", fsCloseName));

		Set<IMethodDeclaration> methods = new HashSet<>(Arrays.asList(constructorDecl, copyToDecl, openSourceDecl));
		sst.setMethods(methods);

		return createContext(sst);
	}

	public List<Context> createPaperTest() {
		ITypeName sType = TypeName.newTypeName("Test.PaperTest.S, Test");
		ITypeName aType = TypeName.newTypeName("Test.PaperTest.A, Test");
		ITypeName bType = TypeName.newTypeName("Test.PaperTest.B, Test");
		ITypeName cType = TypeName.newTypeName("Test.PaperTest.C, Test");
		ITypeName dType = TypeName.newTypeName("Test.PaperTest.D, Test");
		SST sst = createEmptySST(aType);

		sst.setFields(
				declareFields(String.format(Locale.US, "[%s] [%s].b", bType.getIdentifier(), aType.getIdentifier())));
		IFieldDeclaration bFieldDecl = sst.getFields().iterator().next();
		IMethodName helperName = MethodName.newMethodName(
				String.format(Locale.US, "[%s] [%s].helper()", getVoidType().getIdentifier(), aType.getIdentifier()));
		IMethodName fromSName = MethodName.newMethodName(
				String.format(Locale.US, "[%s] [%s].fromS()", cType.getIdentifier(), sType.getIdentifier()));
		IMethodName entry2Name = MethodName.newMethodName(String.format(Locale.US, "[%s] [%s].entry2([%s] b)",
				getVoidType().getIdentifier(), cType.getIdentifier(), bType.getIdentifier()));
		IMethodDeclaration entry1Decl = declareMethod(
				MethodName.newMethodName(String.format(Locale.US, "[%s] [%s].entry1()", getVoidType().getIdentifier(),
						aType.getIdentifier())),
				true, declareVar("tmpB", bType),
				assignmentToLocal("tmpB", refExpr(buildFieldReference(bFieldDecl.getName()))),
				invocationStatement("tmpB",
						MethodName.newMethodName(String.format(Locale.US, "[%s] [%s].m1()",
								getVoidType().getIdentifier(), bType.getIdentifier()))),
				invocationStatement("this", helperName), declareVar("c", cType),
				assignmentToLocal("c", invocationExpression("this", fromSName)), invocationStatement("c", entry2Name,
						Iterators.forArray(refExpr(buildFieldReference(bFieldDecl.getName())))));

		IMethodDeclaration helperDecl = declareMethod(
				MethodName.newMethodName(String.format(Locale.US, "[%s] [%s].helper()", getVoidType().getIdentifier(),
						aType.getIdentifier())),
				false, declareVar("tmpB", bType),
				assignmentToLocal("tmpB", refExpr(buildFieldReference(bFieldDecl.getName()))),
				invocationStatement("tmpB", MethodName.newMethodName(String.format(Locale.US, "[%s] [%s].m2()",
						getVoidType().getIdentifier(), bType.getIdentifier()))));

		sst.setMethods(Sets.newHashSet(entry1Decl, helperDecl));

		Context aContext = createContext(sst);
		TypeHierarchy typeHierarchy = (TypeHierarchy) aContext.getTypeShape().getTypeHierarchy();
		typeHierarchy.setExtends(new TypeHierarchy(sType.getIdentifier()));
		MethodHierarchy methodHierarchy = (MethodHierarchy) aContext.getTypeShape().getMethodHierarchies().iterator()
				.next();
		methodHierarchy.setSuper(MethodName.newMethodName(
				String.format(Locale.US, "[%s] [%s].entry1()", getVoidType().getIdentifier(), sType.getIdentifier())));

		// C
		sst = createEmptySST(cType);
		IMethodName entry3Name = MethodName.newMethodName(
				String.format(Locale.US, "[%s] [%s].entry3()", getVoidType().getIdentifier(), cType.getIdentifier()));
		IMethodName dConstructor = MethodName.newMethodName(
				String.format(Locale.US, "[%s] [%s]..ctor()", getVoidType().getIdentifier(), dType.getIdentifier()));
		IMethodDeclaration entry2Decl = declareMethod(entry2Name, true,
				invocationStatement("b",
						MethodName.newMethodName(String.format(Locale.US, "[%s] [%s].m3()",
								getVoidType().getIdentifier(), bType.getIdentifier()))),
				invocationStatement("this", entry3Name));
		IMethodDeclaration entry3Decl = declareMethod(entry3Name, true, declareVar("d", dType),
				assignmentToLocal("d", invocationExpr(dConstructor)),
				buildTryBlock(
						invocationStatement("d",
								MethodName.newMethodName(String.format(Locale.US, "[%s] [%s].m4()",
										getVoidType().getIdentifier(), dType.getIdentifier()))),
						buildCatchBlock(invocationStatement("d", MethodName.newMethodName(String.format(Locale.US,
								"[%s] [%s].m5()", getVoidType().getIdentifier(), dType.getIdentifier()))))));
		sst.setMethods(Sets.newHashSet(entry2Decl, entry3Decl));
		Context cContext = createContext(sst);

		return Arrays.asList(aContext, cContext);
	}

	public Context createDelegateTest() {
		InputStream resource = getClass().getResourceAsStream("./DelegateTest.json");
		return JsonUtils.fromJson(resource, Context.class);
	}

	public Context createParameterArrayTest() {
		InputStream resource = getClass().getResourceAsStream("./ParameterArrayTest.json");
		return JsonUtils.fromJson(resource, Context.class);
	}

	public Context createRecursionTest() {
		InputStream resource = getClass().getResourceAsStream("./RecursionTest.json");
		return JsonUtils.fromJson(resource, Context.class);
	}

	private IFieldReference buildFieldReference(IFieldName field) {
		return buildFieldReference("this", field);
	}

	public IFieldReference buildFieldReference(String id, IFieldName field) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setFieldName(field);
		fieldRef.setReference(variableReference(id));
		return fieldRef;
	}

	public IPropertyReference buildPropertyRef(String instance, IPropertyName property) {
		PropertyReference propertyRef = new PropertyReference();
		propertyRef.setReference(variableReference(instance));
		propertyRef.setPropertyName(property);
		return propertyRef;
	}

	public ITryBlock buildTryBlock(IStatement body, ICatchBlock catchBlock) {
		TryBlock tryBlock = new TryBlock();
		tryBlock.setBody(Arrays.asList(body));
		tryBlock.setCatchBlocks(Arrays.asList(catchBlock));
		tryBlock.setFinally(Collections.emptyList());
		return tryBlock;
	}

	public ICatchBlock buildCatchBlock(IStatement... body) {
		CatchBlock catchBlock = new CatchBlock();
		catchBlock.setKind(CatchBlockKind.Default);
		catchBlock.setBody(Arrays.asList(body));
		catchBlock.setParameter(ParameterName.newParameterName("[System.Exception, mscorlib] ex"));
		return catchBlock;
	}

}
