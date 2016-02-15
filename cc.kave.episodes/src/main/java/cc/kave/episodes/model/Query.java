/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cc.kave.episodes.model;

public class Query extends Episode {

	private int numRemovedEvents;
	private QueryTarget queryTarget;
	
	public int getNumRemovedEvents() {
		return numRemovedEvents;
	}
	
	public void setNumRemovedEvents(int numberOfEventsRemoved) {
		this.numRemovedEvents = numberOfEventsRemoved;
	}
	
	public QueryTarget getQueryTarget() {
		return queryTarget;
	}
	
	public void setQueryTarget(QueryTarget queryTarget) {
		this.queryTarget = queryTarget;
	}
	
	public boolean equals(Query query) {
		if (this.getNumEvents() != query.getNumEvents()) {
			return false;
		}
		if (this.getNumFacts() != query.getNumFacts()) {
			return false;
		}
		if (!this.getFacts().equals(query.getFacts())) {
			return false;
		}
		if (this.numRemovedEvents != query.numRemovedEvents) {
			return false;
		}
		if (!this.queryTarget.equals(query.getQueryTarget())) {
			return false;
		}
		return true;
	}
}
