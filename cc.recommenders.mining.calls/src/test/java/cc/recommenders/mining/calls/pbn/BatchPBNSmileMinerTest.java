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
package cc.recommenders.mining.calls.pbn;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cc.recommenders.io.Directory;
import cc.recommenders.io.IoUtils;
import cc.recommenders.io.NestedZipFolders;
import cc.recommenders.io.WritingArchive;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.names.ITypeName;
import cc.recommenders.names.VmTypeName;
import cc.recommenders.usages.Usage;
import smile.Network;

public class BatchPBNSmileMinerTest {

	@Mock
	private NestedZipFolders<ITypeName> usageDir;

	@Mock
	private List<Usage> usages;

	@Mock
	private SmileUtils smileUtils;

	@Mock
	private IoUtils io;

	@Mock
	private Directory modelDir;

	@Mock
	private PBNSmileMiner miner;

	@Mock
	private Network network;

	@Mock
	private WritingArchive writingArchive;

	private ITypeName type;

	private BatchPBNSmileMiner sut;

	@Before
	public void setup() throws IOException {
		type = VmTypeName.get("Lp/T");

		MockitoAnnotations.initMocks(this);

		when(usageDir.findKeys()).thenReturn(Sets.newHashSet(type));
		when(usageDir.readAllZips(type, Usage.class)).thenReturn(usages);

		when(miner.learnModel(usages)).thenReturn(network);

		when(smileUtils.toString(network)).thenReturn("XYZ");

		when(io.toNestedFileName(type, "zip")).thenReturn("nestedname.zip");
		when(io.toFlatFileName(type, "xdsl")).thenReturn("flatname.xdsl");

		when(modelDir.getWritingArchive(anyString())).thenReturn(writingArchive);

		sut = new BatchPBNSmileMiner(smileUtils, io, miner, new MiningOptions(), new QueryOptions());
	}

	@Test
	public void happyPath() throws IOException {

		sut.run(usageDir, modelDir);

		verify(usageDir).readAllZips(type, Usage.class);
		verify(miner).learnModel(usages);
		verify(smileUtils).toString(network);

		verify(io).toNestedFileName(type, "zip");
		verify(io).toFlatFileName(type, "xdsl");

		verify(modelDir).getWritingArchive("nestedname.zip");
		verify(writingArchive).addPlain("XYZ", "flatname.xdsl");
		verify(writingArchive).close();
	}

	@Test
	public void noUsages() throws IOException {

		when(usageDir.readAllZips(type, Usage.class)).thenReturn(Lists.newLinkedList());

		sut.run(usageDir, modelDir);

		verify(usageDir).readAllZips(type, Usage.class);
		verify(miner, times(0)).learnModel(anyListOf(Usage.class));
	}

	@Test
	public void unknownTypeIsIgnored() throws IOException {
		when(usageDir.findKeys()).thenReturn(Sets.newHashSet(VmTypeName.get("LUnknown")));
		sut.run(usageDir, modelDir);
		verify(usageDir).findKeys();
		verifyNoMoreInteractions(usageDir);
	}

	@Test
	public void arraysAreIgnored() throws IOException {
		when(usageDir.findKeys()).thenReturn(Sets.newHashSet(VmTypeName.get("[LSomeArrayType")));
		sut.run(usageDir, modelDir);
		verify(usageDir).findKeys();
		verifyNoMoreInteractions(usageDir);
	}
}