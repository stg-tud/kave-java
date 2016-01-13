package commons.model.events.typeshapes;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.names.MethodName;
import cc.kave.commons.model.names.csharp.CsMethodName;
import cc.kave.commons.model.typeshapes.MethodHierarchy;
import commons.testutils.model.names.TestNameFactory;

public class MethodHierarchyTest {

	@Test
	public void testDefaultValues() {
		MethodHierarchy sut = new MethodHierarchy();
		assertThat(CsMethodName.UNKNOWN_NAME, equalTo(sut.getElement()));
		assertNull(sut.getSuper());
		assertNull(sut.getFirst());
		assertFalse(sut.isDeclaredInParentHierarchy());
	}

	@Test
	public void testDefaultValues_CustomConstructor() {
		MethodHierarchy sut = new MethodHierarchy(m("x"));
		assertThat(m("x"), equalTo(sut.getElement()));
		assertNull(sut.getSuper());
		assertNull(sut.getFirst());
		assertFalse(sut.isDeclaredInParentHierarchy());

	}

	@Test
	public void testSettingValues() {
		MethodHierarchy sut = new MethodHierarchy();
		sut.setElement(m("a"));
		sut.setSuper(m("b"));
		sut.setFirst(m("c"));
		assertThat(m("a"), equalTo(sut.getElement()));
		assertThat(m("b"), equalTo(sut.getSuper()));
		assertThat(m("c"), equalTo(sut.getFirst()));
	}

	@Test
	public void testShouldBeOverrideOrImplementationWhenFirstIsSet() {
		MethodHierarchy sut = new MethodHierarchy();
		sut.setFirst(TestNameFactory.GetAnonymousMethodName());
		assertTrue(sut.isDeclaredInParentHierarchy());
	}

	@Test
	public void testEquality_Default() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_ReallyTheSame() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		a.setElement(m("a"));
		a.setSuper(m("b"));
		a.setFirst(m("c"));
		b.setElement(m("a"));
		b.setSuper(m("b"));
		b.setFirst(m("c"));
		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEquality_DifferentElement() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		a.setElement(m("a"));
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEquality_DifferentSuper() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		a.setSuper(m("b"));
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEquality_DifferentFirst() {
		MethodHierarchy a = new MethodHierarchy();
		MethodHierarchy b = new MethodHierarchy();
		a.setFirst(m("c"));
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	private static MethodName m(String s) {
		return CsMethodName.newMethodName("[T1,P1] [T2,P2]." + s + "()");
	}
}
