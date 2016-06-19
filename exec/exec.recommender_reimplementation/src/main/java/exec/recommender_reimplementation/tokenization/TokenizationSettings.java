/**
 * Copyright 2016 Technische Universität Darmstadt
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
package exec.recommender_reimplementation.tokenization;

public class TokenizationSettings {

	private boolean useFullyQualifiedTypes;
	private boolean activeBrackets;
	private boolean activePuncutuation;
	private boolean activeOperators;
	private boolean activeKeywords;
	private boolean activeWrapKeywords;
	
	
	public TokenizationSettings(boolean allCharactersActive) {
		activeBrackets = true;
		activeKeywords = true;
		activeOperators = true;
		activePuncutuation = true;
	}

	public TokenizationSettings() {}

	public boolean isUseFullyQualifiedTypes() {
		return useFullyQualifiedTypes;
	}

	public void setUseFullyQualifiedTypes(boolean useFullyQualifiedTypes) {
		this.useFullyQualifiedTypes = useFullyQualifiedTypes;
	}
	
	
	public boolean isActivePuncutuation() {
		return activePuncutuation;
	}

	public void setActivePuncutuation(boolean activatePuncutuation) {
		this.activePuncutuation = activatePuncutuation;
	}


	public boolean isActiveBrackets() {
		return activeBrackets;
	}

	public void setActiveBrackets(boolean activateBrackets) {
		this.activeBrackets = activateBrackets;
	}


	public boolean isActiveOperators() {
		return activeOperators;
	}

	public void setActiveOperators(boolean activateOperators) {
		this.activeOperators = activateOperators;
	}

	public boolean isActiveKeywords() {
		return activeKeywords;
	}

	public void setActiveKeywords(boolean activeKeywords) {
		this.activeKeywords = activeKeywords;
	}

	public boolean isActiveWrapKeywords() {
		return activeWrapKeywords;
	}

	public void setActiveWrapKeywords(boolean activeWrapKeywords) {
		this.activeWrapKeywords = activeWrapKeywords;
	}

	

}
