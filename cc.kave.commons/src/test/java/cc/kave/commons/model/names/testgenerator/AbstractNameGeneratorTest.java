package cc.kave.commons.model.names.testgenerator;

import java.util.List;

import org.junit.Assert;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.IDelegateTypeName;
import cc.kave.commons.model.names.IMethodName;
import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.AssemblyName;
import cc.kave.commons.model.names.csharp.DelegateTypeName;
import cc.kave.commons.model.names.csharp.MethodName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.names.csharp.TypeParameterName;

public abstract class AbstractNameGeneratorTest {

	protected Iterable<ITypeName> getTypes() {
		return getTypes(1);
	}

	protected Iterable<ITypeName> getTypeParameters() {
		return getTypeParameters(1);
	}

	// Depth: 1 --> 70 Types generated
	// Depth: 2 --> 430 Types generated
	// Depth: 3 --> 2590 Types generated
	private Iterable<ITypeName> getTypes(int maxDepth) {
		List<ITypeName> types = Lists.newLinkedList();
		types.add(TypeName.UNKNOWN_NAME);
		for (String assemblyName : getAssemblyName()) {

			types.add(type("%s, %s", getUniqueName(), assemblyName));

			// resolved generic
			getGenericNames(maxDepth, types, assemblyName);

			if (maxDepth > 0) {
				Iterable<ITypeName> types2 = getTypes(maxDepth - 1);
				for (ITypeName t : types2) {
					for (ITypeName t2 : types2) {
						types.add(delegate("d:[%s] [%s].[%s]()", t, t2, getUniqueName()));
					}
				}
			}
		}
		/*
		 * List<IDelegateTypeName> delegateTypeNames = Lists.newLinkedList();
		 * for(ITypeName t : types){ delegateTypeNames.add(delegate(
		 * "d:[%s] [%s].()", t.getIdentifier(), getUniqueName())); }
		 * types.addAll(delegateTypeNames);
		 */
		return types;
	}

	private void getGenericNames(int maxDepth, List<ITypeName> types, String assemblyName) {
		for (ITypeName t : getTypeParameters(maxDepth)) {
			types.add(type("%s´1[[%s]], %s", getUniqueName(), t.getIdentifier(), assemblyName));
		}
		for (ITypeName t : getTypeParameters(maxDepth)) {
			types.add(type("%s´2[[%s],[%s]], %s", getUniqueName(), t.getIdentifier(), t.getIdentifier(),
					assemblyName));
		}
	}

	private Iterable<ITypeName> getTypeParameters(int maxDepth) {
		List<ITypeName> types = Lists.newLinkedList();

		// unresolved generic
		// TODO invalid name?!
		types.add(TypeParameterName.newTypeParameterName(getUniqueName()));
		// resolved generic
		if (maxDepth > 0) {
			for (ITypeName t : getTypes(maxDepth - 1)) {
				types.add(TypeParameterName.newTypeParameterName(getUniqueName(), t.getIdentifier()));
			}
		}
		return types;
	}

	
	public Iterable<IMethodName> getMethodNames(){
		return getMethodNames(1);
	}

	private Iterable<IMethodName> getMethodNames(int maxDepth) {
		List<IMethodName> methodNames = Lists.newLinkedList();
		// TODO: TypeParameters and Generic MethodNames
		// for (List<ITypeName> typeParams : createTypeParameters()) {
		for (String simpleMethodName : new String[] { "M", ".ctor", ".cctor" }) {
			for (ITypeName declaringType : getTypes(maxDepth)) {
				for (ITypeName returnType : getTypes(maxDepth)) {
					// String genericPart = createGenericPart(typeParams);
					methodNames.add(method("[%s] [%s].%s%s()", returnType, declaringType, simpleMethodName, ""));
				}
			}
		}
		// }
		return methodNames;
	}
	
	private Iterable<String> getAssemblyName() {
		return Lists.newArrayList(AssemblyName.UNKNOWN_NAME.getIdentifier(), getUniqueName(), "mscorlib, 4.0.0.0");
	}

	private int num = 0;

	private String getUniqueName() {
		return "T" + (num++);
	}

	private ITypeName type(String id, Object... args) {
		return TypeName.newTypeName(String.format(id, args));
	}
	
	private IMethodName method(String id, Object...args){
		return MethodName.newMethodName(String.format(id, args));
	}

	private IDelegateTypeName delegate(String id, Object... args) {
		return DelegateTypeName.newDelegateTypeName(String.format(id, args));
	}

	protected boolean[] trueAndFalse() {
		return new boolean[] { true, false };
	}

}