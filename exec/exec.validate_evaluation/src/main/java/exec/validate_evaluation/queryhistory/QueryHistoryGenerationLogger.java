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
package exec.validate_evaluation.queryhistory;

import java.util.Map;
import java.util.Set;

import cc.kave.commons.model.events.completionevents.ICompletionEvent;
import cc.recommenders.datastructures.Tuple;
import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.names.ICoReTypeName;
import exec.validate_evaluation.streaks.EditStreak;

public class QueryHistoryGenerationLogger {

	public void foundZips(Set<String> zips) {
		// TODO Auto-generated method stub
		
	}

	public void foundEvents(Set<ICompletionEvent> events) {
		// TODO Auto-generated method stub
		
	}

	public void processingEvent(ICompletionEvent e) {
		// TODO Auto-generated method stub
		
	}

	public void endZip(Map<Tuple<ICoReMethodName, ICoReTypeName>, EditStreak> editStreaks) {
		// TODO Auto-generated method stub
		
	}

	public void finish() {
		// TODO Auto-generated method stub
		
	}

	public void startRemoveSingleEdits() {
		// TODO Auto-generated method stub
		
	}

	public void removeSingleEdit() {
		// TODO Auto-generated method stub
		
	}

}
