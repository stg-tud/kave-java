package cc.kave.commons.model.events.typeshapes;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import java.util.HashSet;

import org.junit.Test;

import com.google.common.collect.Sets;

import cc.kave.commons.model.names.csharp.CsTypeName;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import cc.kave.commons.model.typeshapes.TypeHierarchy;
import cc.kave.commons.model.typeshapes.TypeShape;

public class TypeShapeTest {

	private static TypeHierarchy someTypeHierarchy() {
		TypeHierarchy typeHierarchy = new TypeHierarchy();
		typeHierarchy.setElement(CsTypeName.newTypeName("T,P"));
		return typeHierarchy;
	}

	@Test
	public void testDefaultValues() {
		TypeShape sut = new TypeShape();
		assertThat(new TypeHierarchy(), equalTo(sut.getTypeHierarchy()));
		assertThat(new HashSet<MethodHierarchy>(), equalTo(sut.getMethodHierarchies()));
	}

	@Test
	public void testSettingValues() {
		TypeShape sut = new TypeShape();
		sut.setTypeHierarchy(someTypeHierarchy());
		sut.setMethodHierarchies(Sets.newHashSet(new MethodHierarchy()));

		assertThat(someTypeHierarchy(), equalTo(sut.getTypeHierarchy()));
		assertThat(Sets.newHashSet(new MethodHierarchy()), equalTo(sut.getMethodHierarchies()));
	}

	@Test
	public void testEquality_Default() {
		TypeShape a = new TypeShape();
		TypeShape b = new TypeShape();
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_ReallyTheSame() {
		TypeShape a = new TypeShape();
		TypeShape b = new TypeShape();
		a.setTypeHierarchy(someTypeHierarchy());
		a.setMethodHierarchies(Sets.newHashSet(new MethodHierarchy()));
		b.setMethodHierarchies(Sets.newHashSet(new MethodHierarchy()));
		b.setTypeHierarchy(someTypeHierarchy());
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_DifferentType() {
		TypeShape a = new TypeShape();
		a.setTypeHierarchy(someTypeHierarchy());
		TypeShape b = new TypeShape();
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b)));
	}

	@Test
	public void testEquality_DifferentMethods() {
		TypeShape a = new TypeShape();
		a.setMethodHierarchies(Sets.newHashSet(new MethodHierarchy()));
		TypeShape b = new TypeShape();
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b)));
	}
}
