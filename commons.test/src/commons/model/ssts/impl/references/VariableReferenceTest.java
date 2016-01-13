package commons.model.ssts.impl.references;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.references.VariableReference;

import commons.model.ssts.impl.SSTTestHelper;

public class VariableReferenceTest {

	@Test
	public void testDefaultValues() {
		VariableReference sut = new VariableReference();

		assertThat("", equalTo(sut.getIdentifier()));
		assertThat(true, equalTo(sut.isMissing()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		VariableReference sut = new VariableReference();
		sut.setIdentifier("a");

		assertThat(false, equalTo(sut.isMissing()));
		assertThat("a", equalTo(sut.getIdentifier()));
	}

	@Test
	public void testEqualityDefault() {
		VariableReference a = new VariableReference();
		VariableReference b = new VariableReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		VariableReference a = new VariableReference();
		VariableReference b = new VariableReference();
		a.setIdentifier("a");
		b.setIdentifier("a");

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentIdentifier() {
		VariableReference a = new VariableReference();
		VariableReference b = new VariableReference();
		a.setIdentifier("a");

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		VariableReference sut = new VariableReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}

}
