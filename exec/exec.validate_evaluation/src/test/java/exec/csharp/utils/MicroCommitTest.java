/*
 * Copyright 2014 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *    http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package exec.csharp.utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

import org.junit.Test;

import cc.recommenders.usages.Query;

public class MicroCommitTest {
	@Test
	public void defaultValues() {
		MicroCommit sut = new MicroCommit();
		assertNull(sut.Item1);
		assertNull(sut.Item2);
		assertNull(sut.getStart());
		assertNull(sut.getEnd());
	}

	@Test
	public void settingValues() {
		Query q1 = mock(Query.class);
		Query q2 = mock(Query.class);

		MicroCommit sut = new MicroCommit();
		sut.Item1 = q1;
		sut.Item2 = q2;
		assertSame(q1, sut.getStart());
		assertSame(q2, sut.getEnd());
	}

	@Test
	public void equality_default() {
		MicroCommit a = new MicroCommit();
		MicroCommit b = new MicroCommit();
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_reallyTheSame() {
		Query q1 = mock(Query.class);
		Query q2 = mock(Query.class);

		MicroCommit a = new MicroCommit();
		a.Item1 = q1;
		a.Item2 = q2;
		MicroCommit b = new MicroCommit();
		b.Item1 = q1;
		b.Item2 = q2;
		assertEquals(a, b);
		assertEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_differentStart() {
		MicroCommit a = new MicroCommit();
		a.Item1 = mock(Query.class);
		MicroCommit b = new MicroCommit();
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}

	@Test
	public void equality_differentEnd() {
		MicroCommit a = new MicroCommit();
		a.Item2 = mock(Query.class);
		MicroCommit b = new MicroCommit();
		assertNotEquals(a, b);
		assertNotEquals(a.hashCode(), b.hashCode());
	}
}