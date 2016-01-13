package commons.model.ssts.impl.references;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.references.UnknownReference;

import commons.model.ssts.impl.SSTTestHelper;

public class UnknownReferenceTest {

	@Test
	public void testDefaultValues() {
		UnknownReference sut = new UnknownReference();

		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testEqualityDefault() {
		UnknownReference a = new UnknownReference();
		UnknownReference b = new UnknownReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testVisitorIsImplemented() {
		UnknownReference sut = new UnknownReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
