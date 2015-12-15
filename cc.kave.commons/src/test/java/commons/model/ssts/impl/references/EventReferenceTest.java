package commons.model.ssts.impl.references;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import org.junit.Test;

import cc.kave.commons.model.names.EventName;
import cc.kave.commons.model.names.csharp.CsEventName;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.EventReference;
import cc.kave.commons.model.ssts.impl.references.VariableReference;
import commons.model.ssts.impl.SSTBaseTest;
import commons.model.ssts.impl.SSTTestHelper;

public class EventReferenceTest extends SSTBaseTest {

	private static EventName someEvent() {
		return CsEventName.newEventName("[T1,P1] [T2,P2].E");
	}

	@Test
	public void testDefaultValues() {
		EventReference sut = new EventReference();

		assertThat(new VariableReference(), equalTo(sut.getReference()));
		assertThat(CsEventName.UNKNOWN_NAME, equalTo(sut.getEventName()));
		assertThat(0, not(equalTo(sut.hashCode())));
		assertThat(1, not(equalTo(sut.hashCode())));
	}

	@Test
	public void testSettingValues() {
		EventReference sut = new EventReference();
		sut.setEventName(someEvent());

		assertThat(someEvent(), equalTo(sut.getEventName()));
	}

	@Test
	public void testEqualityDefault() {
		EventReference a = new EventReference();
		EventReference b = new EventReference();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityReallyTheSame() {
		EventReference a = new EventReference();
		EventReference b = new EventReference();
		a.setEventName(someEvent());
		b.setEventName(someEvent());

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
	}

	@Test
	public void testEqualityDifferentName() {
		EventReference a = new EventReference();
		EventReference b = new EventReference();
		a.setEventName(someEvent());

		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testEqualityDifferentReference() {
		EventReference a = new EventReference();
		EventReference b = new EventReference();
		a.setReference(someVarRef());
		assertThat(a, not(equalTo(b)));
		assertThat(a.hashCode(), not(equalTo(b.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		EventReference sut = new EventReference();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testWithReturnIsImplemented() {
		// TODO : Visitor Test
	}
}
