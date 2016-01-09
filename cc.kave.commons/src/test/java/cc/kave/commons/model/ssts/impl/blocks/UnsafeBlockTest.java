package cc.kave.commons.model.ssts.impl.blocks;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.*;

import org.junit.Test;

import cc.kave.commons.model.ssts.impl.SSTBaseTest;
import cc.kave.commons.model.ssts.impl.SSTTestHelper;
import cc.kave.commons.model.ssts.impl.blocks.UnsafeBlock;

public class UnsafeBlockTest extends SSTBaseTest {
	
	@Test
	public void testChildrenIdentity() {
		UnsafeBlock sut = new UnsafeBlock();
		assertChildren(sut);
	}

	@Test
	public void testEquality() {
		UnsafeBlock a = new UnsafeBlock();
		UnsafeBlock b = new UnsafeBlock();

		assertThat(a, equalTo(b));
		assertThat(a.hashCode(), equalTo(b.hashCode()));
		assertThat(0, not(equalTo(a.hashCode())));
		assertThat(1, not(equalTo(a.hashCode())));
	}

	@Test
	public void testVisitorIsImplemented() {
		UnsafeBlock sut = new UnsafeBlock();
		SSTTestHelper.accept(sut, 23).verify(sut);
	}

	@Test
	public void testVisitorWithReturnIsImplemented() {
		// TODO: Visitor Test
	}
}
