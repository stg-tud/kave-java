package commons.model.ssts.impl.references;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.PropertyName;
import cc.kave.commons.model.names.csharp.CsPropertyName;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.PropertyReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import commons.model.ssts.impl.SSTBaseTest;
import commons.model.ssts.impl.SSTTestHelper;

public class PropertyReferenceTest extends SSTBaseTest {

	private static PropertyName someProperty() {
		return CsPropertyName.newPropertyName("[T1,P1] [T2,P2].P");
	}

	@Test
	public void testDefaultValues() {
		PropertyReference sut = new PropertyReference();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(CsPropertyName.UNKNOWN_NAME, equalTo(sut.getPropertyName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		PropertyReference sut = new PropertyReference();
		sut.setPropertyName(someProperty());

		assertThat(someProperty(), equalTo(sut.getPropertyName()));
	}

	@Test
	public void testEqualityDefault() {
		PropertyReference a = new PropertyReference();
		PropertyReference b = new PropertyReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		PropertyReference a = new PropertyReference();
		PropertyReference b = new PropertyReference();
		a.setPropertyName(someProperty());
		b.setPropertyName(someProperty());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		PropertyReference a = new PropertyReference();
		PropertyReference b = new PropertyReference();
		a.setPropertyName(someProperty());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentReference() {
		PropertyReference a = new PropertyReference();
		PropertyReference b = new PropertyReference();
		a.setReference(someVarRef());
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		PropertyReference sut = new PropertyReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
