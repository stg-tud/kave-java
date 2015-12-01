/**
 * Copyright 2015 Waldemar Graf
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

package eclipse.commons.test;

import cc.kave.commons.model.names.csharp.CsFieldName;
import cc.kave.commons.model.ssts.impl.declarations.FieldDeclaration;

public class BaseDeclarationTest {

	public FieldDeclaration getFieldDeclaration(String fieldName){
		FieldDeclaration decl = new FieldDeclaration();
		decl.setName(CsFieldName.newFieldName(fieldName));
		
		return decl;
	}
}
