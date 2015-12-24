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
package cc.kave.commons.pointsto.tests;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.names.FieldName;
import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.TypeName;
import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.model.ssts.declarations.IMethodDeclaration;
import cc.kave.commons.model.ssts.impl.SST;
import cc.kave.commons.model.ssts.impl.references.FieldReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.references.IFieldReference;
import cc.kave.commons.model.ssts.references.IPropertyReference;
import cc.kave.commons.model.typeshapes.IMethodHierarchy;
import cc.kave.commons.model.typeshapes.ITypeHierarchy;
import cc.kave.commons.model.typeshapes.ITypeShape;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

import static cc.kave.commons.model.ssts.impl.SSTUtil.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.Iterators;

/**
 * Provides functionality related to SST construction for tests.
 */
public class TestSSTBuilder {

	public TypeName getStringType() {
		return CsTypeName.newTypeName("System.String, mscorlib");
	}

	public TypeName getVoidType() {
		return CsTypeName.newTypeName("System.Void, mscorlib");
	}

	public TypeName getFileStreamType() {
		return CsTypeName.newTypeName("System.IO.FileStream, mscorlib");
	}

	public TypeName getByteArrayType() {
		return CsTypeName.newTypeName("System.Byte[], mscorlib");
	}

	public TypeName getInt32Type() {
		return CsTypeName.newTypeName("System.Int32, mscorlib");
	}

	public TypeName getBooleanType() {
		return CsTypeName.newTypeName("System.Boolean, mscorlib");
	}

	public SST createEmptySST(TypeName enclosingType) {
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

		ITypeHierarchy typeHierarchy = new TypeHierarchy(sst.getEnclosingType().getIdentifier());
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
		TypeName streamTestType = CsTypeName.newTypeName("Test.StreamTest, Test");
		SST sst = createEmptySST(streamTestType);

		FieldName sourceFieldName = CsFieldName
				.newFieldName("[" + getStringType().getIdentifier() + "] [Test.StreamTest, Test].source");
		sst.setFields(declareFields(sourceFieldName.getIdentifier()));

		MethodName constructorName = CsMethodName.newMethodName("[" + getVoidType().getIdentifier()
				+ "] [Test.StreamTest, Test]..ctor([" + getStringType().getIdentifier() + "] source)");
		IMethodDeclaration constructorDecl = declareMethod(constructorName, true,
				assign(buildFieldReference(sourceFieldName), referenceExprToVariable("source")));

		TypeName fileStreamType = getFileStreamType();
		MethodName fileStreamCtorName = CsMethodName.newMethodName(
				"[" + getVoidType().getIdentifier() + "] [" + fileStreamType.getIdentifier() + "]..ctor(["
						+ getStringType().getIdentifier() + "] path, [" + CsTypeName.UNKNOWN_NAME.getIdentifier()
						+ "] mode, [" + CsTypeName.UNKNOWN_NAME.getIdentifier() + "] access)");
		MethodName openSourceName = CsMethodName
				.newMethodName("[" + fileStreamType.getIdentifier() + "] [Test.StreamTest, Test].OpenSource()");
		IMethodDeclaration openSourceDecl = declareMethod(openSourceName, false, declareVar("tmp", fileStreamType),
				assign(variableReference("tmp"),
						invocationExpression(fileStreamCtorName, Iterators.forArray(
								refExpr(buildFieldReference(sourceFieldName)), constant("Open"), constant("Read")))),
				returnVariable("tmp"));

		MethodName copyToName = CsMethodName.newMethodName("[" + getVoidType().getIdentifier()
				+ "] [Test.StreamTest, Test].CopyTo([" + getStringType().getIdentifier() + "] dest)");
		MethodName byteArrayCtorName = CsMethodName.newMethodName("[" + getVoidType().getIdentifier() + "] ["
				+ getByteArrayType().getIdentifier() + "]..ctor([" + getInt32Type().getIdentifier() + "] length)");
		MethodName fsReadName = CsMethodName.newMethodName("[" + getInt32Type().getIdentifier() + "] ["
				+ fileStreamType.getIdentifier() + "].Read([" + getByteArrayType().getIdentifier() + "] array, ["
				+ getInt32Type().getIdentifier() + "] offset, [" + getInt32Type().getIdentifier() + "] size)");
		PropertyName intArrLengthName = CsPropertyName.newPropertyName(
				"get [" + getInt32Type().getIdentifier() + "] [" + getByteArrayType().getIdentifier() + "].Length()");
		MethodName fsWriteName = CsMethodName.newMethodName(fsReadName.getIdentifier().replace(".Read(", ".Write("));
		MethodName fsCloseName = CsMethodName.newMethodName(
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

	private IFieldReference buildFieldReference(FieldName field) {
		return buildFieldReference("this", field);
	}

	public IFieldReference buildFieldReference(String id, FieldName field) {
		FieldReference fieldRef = new FieldReference();
		fieldRef.setFieldName(field);
		fieldRef.setReference(variableReference(id));
		return fieldRef;
	}

	public IPropertyReference buildPropertyRef(String instance, PropertyName property) {
		PropertyReference propertyRef = new PropertyReference();
		propertyRef.setReference(variableReference(instance));
		propertyRef.setPropertyName(property);
		return propertyRef;
	}
}
