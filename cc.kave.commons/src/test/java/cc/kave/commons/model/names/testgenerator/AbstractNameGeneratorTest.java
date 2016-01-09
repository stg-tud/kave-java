package cc.kave.commons.model.names.testgenerator;

import java.util.List;

import com.google.common.collect.Lists;

import cc.kave.commons.model.names.ITypeName;
import cc.kave.commons.model.names.csharp.AssemblyName;
import cc.kave.commons.model.names.csharp.TypeName;
import cc.kave.commons.model.names.csharp.TypeParameterName;

public abstract class AbstractNameGeneratorTest {

	protected Iterable<ITypeName> getTypes() {
		return getTypes(1);
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
			for (ITypeName t : getTypeParameters(maxDepth)) {
				types.add(type("%s´1[[%s]], %s", getUniqueName(), t.getIdentifier(), assemblyName));
			}
			for (ITypeName t : getTypeParameters(maxDepth)) {
				types.add(type("%s´2[[%s],[%s]], %s", getUniqueName(), t.getIdentifier(), t.getIdentifier(),
						assemblyName));
			}

			// delegate names
			// TODO
		}
		return types;
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

	protected boolean[] trueAndFalse() {
		return new boolean[] { true, false };
	}

}