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

import java.io.IOException;
import java.util.List;
import java.util.Set;

import com.google.inject.Inject;

import cc.recommenders.collections.SublistSelector;
import cc.recommenders.io.Directory;
import cc.recommenders.io.IoUtils;
import cc.recommenders.io.Logger;
import cc.recommenders.io.NestedZipFolders;
import cc.recommenders.io.WritingArchive;
import cc.recommenders.mining.calls.MiningOptions;
import cc.recommenders.mining.calls.QueryOptions;
import cc.recommenders.names.ITypeName;
import cc.recommenders.names.VmTypeName;
import cc.recommenders.usages.Usage;
import smile.Network;

public class BatchPBNSmileMiner {

	private int MAX_NUM_OF_USAGES = 20000;
	private static final ITypeName unknownType = VmTypeName.get("LUnknown");

	private IoUtils io;
	private SmileUtils smileUtils;
	private PBNSmileMiner miner;
	private MiningOptions mOpts;
	private QueryOptions qOpts;

	@Inject
	public BatchPBNSmileMiner(SmileUtils smileUtils, IoUtils io, PBNSmileMiner miner, MiningOptions mOpts,
			QueryOptions qOpts) {
		this.smileUtils = smileUtils;
		this.io = io;
		this.miner = miner;
		this.mOpts = mOpts;
		this.qOpts = qOpts;
	}

	public void run(NestedZipFolders<ITypeName> usagesDir, Directory modelDir) {
		int numMined = 0;
		int numUsages = 0;

		Logger.log("options for learning: %s%s\n", mOpts, qOpts);

		Set<ITypeName> types = usagesDir.findKeys();
		for (ITypeName t : types) {
			Logger.log("%s", t);

			if (t.equals(unknownType) || t.isArrayType()) {
				Logger.log("skipping...");
				continue;
			}

			List<Usage> usages = usagesDir.readAllZips(t, Usage.class);
			if (usages.isEmpty()) {
				Logger.log("\t\tno usages, ignored");
				continue;
			}

			int curSize = usages.size();
			if (curSize > MAX_NUM_OF_USAGES) {
				Logger.log("\t\ttoo many usages (%d)... selecting %d random ones", curSize, MAX_NUM_OF_USAGES);
				usages = SublistSelector.pickRandomSublist(usages, MAX_NUM_OF_USAGES);
			}

			numMined++;
			numUsages += usages.size();
			Logger.log("\t\t%d usages", usages.size());

			Network network = miner.learnModel(usages);

			int numPatterns = smileUtils.getNumPatterns(network);
			Logger.append(" --> %d patterns", numPatterns);

			String xml = smileUtils.toString(network);

			String zipFile = io.toNestedFileName(t, "zip");
			String fileName = io.toFlatFileName(t, "xdsl");

			write(xml, fileName, zipFile, modelDir);
		}

		Logger.log("");
		Logger.log("--> mined models for %d type from %d total usages", numMined, numUsages);
	}

	private void write(String xml, String nameOfFileInZip, String zipFile, Directory modelDir) {
		try {
			WritingArchive wa = modelDir.getWritingArchive(zipFile);
			wa.addPlain(xml, nameOfFileInZip);
			wa.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}