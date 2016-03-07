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
package exec.validate_evaluation.streaks;

import java.util.Date;

import cc.recommenders.names.ICoReMethodName;
import cc.recommenders.usages.Usage;

public class Edit {

	private Date date;
	private Usage usage;
	private ICoReMethodName selection;

	private Edit() {
		// for de-/serialization
	}

	public Date getDate() {
		return date;
	}

	public Usage getUsage() {
		return usage;
	}

	public ICoReMethodName getSelection() {
		return selection;
	}

	public boolean isQuery() {
		return false;
	}

	public static Edit create(Date date, Usage usage, ICoReMethodName selection) {
		Edit e = new Edit();
		e.date = date;
		e.usage = usage;
		e.selection = selection;
		return e;
	}
}