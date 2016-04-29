/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package exec.demographics;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.recommenders.io.NestedZipFolders;
import cc.recommenders.names.ICoReTypeName;
import exec.validate_evaluation.microcommits.MicroCommit;
import exec.validate_evaluation.microcommits.MicroCommitIo;
import exec.validate_evaluation.streaks.EditStreakGenerationIo;

public class DemographicsIO implements IDemographicsIO {

	private Map<String, Positions> positions = Maps.newHashMap();
	private MicroCommitIo mcIO;
	private EditStreakGenerationIo esIo;
	private NestedZipFolders<ICoReTypeName> usages;

	public DemographicsIO(MicroCommitIo mcIO, EditStreakGenerationIo esIo, NestedZipFolders<ICoReTypeName> usages) {
		this.mcIO = mcIO;
		this.esIo = esIo;
		this.usages = usages;
		positions.put("DATEV/Events/data-108-130/108.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/110.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/111.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/112.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/113.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/114.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/115.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/116.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/117.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/119.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/120.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/122.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/125.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/126.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/129.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-108-130/130.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/141.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/143.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/144.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/145.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/146.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/148.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/149.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/151.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/153.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/155.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/156.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/157.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-131-160/158.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/161.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/162.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/163.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/165.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/167.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/168.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/169.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/170.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/173.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/174.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/177.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-161-180/180.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-181-193/185.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-181-193/188.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-181-193/189.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-181-193/192.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-181-193/193.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-194-230/194.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-194-230/202.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-194-230/211.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-194-230/215.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-231-269/233.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/41.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/47.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/48.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/49.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/51.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/52.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/54.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/55.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/57.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/59.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/60.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/63.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/64.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/65.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-41-68/66.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-69-89/69.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-69-89/74.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-69-89/75.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-69-89/77.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-69-89/78.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-69-89/80.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-90-107/101.zip", Positions.SoftwareEngineer);
		positions.put("DATEV/Events/data-90-107/90.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/0.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/1.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/10.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/100.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/102.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/103.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/104.zip", Positions.Student);
		positions.put("KaVE/151223-data/105.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/106.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/107.zip", Positions.Student);
		positions.put("KaVE/151223-data/109.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/11.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/110.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/115.zip", Positions.ResearcherAcademic);
		positions.put("KaVE/151223-data/116.zip", Positions.HobbyProgrammer);
		positions.put("KaVE/151223-data/120.zip", Positions.ResearcherAcademic);
		positions.put("KaVE/151223-data/122.zip", Positions.Student); // was:
																		// unknown,
																		// but I
																		// now
																		// the
																		// person
		positions.put("KaVE/151223-data/125.zip", Positions.ResearcherAcademic);
		positions.put("KaVE/151223-data/127.zip", Positions.ResearcherAcademic);
		positions.put("KaVE/151223-data/13.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/136.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/139.zip", Positions.HobbyProgrammer);
		positions.put("KaVE/151223-data/143.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/144.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/150.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/152.zip", Positions.Student);
		positions.put("KaVE/151223-data/19.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/197.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/30.zip", Positions.Student);
		positions.put("KaVE/151223-data/35.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/36.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/38.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/45.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/46.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/48.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/49.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/505.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/54.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/65.zip", Positions.ResearcherIndustry);
		positions.put("KaVE/151223-data/78.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/8.zip", Positions.Student);
		positions.put("KaVE/151223-data/82.zip", Positions.Student);
		positions.put("KaVE/151223-data/88.zip", Positions.HobbyProgrammer);
		positions.put("KaVE/151223-data/89.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/9.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/90.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data/95.zip", Positions.Unknown);
		positions.put("KaVE/151223-data/96.zip", Positions.SoftwareEngineer);
		positions.put("KaVE/151223-data-bp/0.zip", Positions.Unknown);
		positions.put("KaVE/151223-data-bp/10.zip", Positions.Unknown);
		positions.put("KaVE/151223-data-bp/12.zip", Positions.Unknown);
		positions.put("KaVE/151223-data-bp/13.zip", Positions.Unknown);
		positions.put("KaVE/151223-data-bp/17.zip", Positions.Unknown);
		positions.put("KaVE/151223-data-bp/4.zip", Positions.Unknown);
		positions.put("KaVE/160229-data/891.zip", Positions.Unknown);
		positions.put("KaVE/160331-data/1000.zip", Positions.ResearcherAcademic);
		positions.put("KaVE/160331-data/1003.zip", Positions.Student);
		positions.put("KaVE/160421-data/1020.zip", Positions.Student);
		positions.put("KaVE/160421-data/1022.zip", Positions.SoftwareEngineer);

	}

	@Override
	public Set<String> findUsers() {
		return mcIO.findZips();
	}

	@Override
	public Positions getPosition(String user) {
		if (positions.containsKey(user)) {
			return positions.get(user);
		}
		return Positions.Unknown;
	}

	@Override
	public int getNumQueries(String user) {
		int num = 0;
		List<MicroCommit> mcs = mcIO.read(user);
		for (MicroCommit mc : mcs) {
			if (shouldCount(mc)) {
				num++;
			}
		}

		return num;
	}

	private boolean shouldCount(MicroCommit mc) {
		ICoReTypeName type = mc.getType();
		return usages.hasZips(type);
	}

	@Override
	public Set<ICompletionEvent> readEvents(String user) {
		return esIo.readCompletionEvents(user);
	}
}