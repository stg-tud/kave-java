grammar TypeNaming;

options { language=Java; }

@lexer::header {
/**
 * Copyright 2016 Sebastian Proksch
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
package cc.kave.commons.model.names.csharp.parser;
}

@parser::header {
/**
 * Copyright 2016 Sebastian Proksch
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
package cc.kave.commons.model.names.csharp.parser;
}

typeEOL : type EOL;
methodEOL: method EOL;

type: UNKNOWN | (typeParameter | regularType | delegateType) arrayPart?;
typeParameter : id;
regularType: resolvedType ',' WS? assembly;
delegateType: 'd:' method;
arrayPart: '[' ','* ']';

resolvedType: namespace? typeName ('+' typeName)*;
namespace : (id '.')+;
typeName: simpleTypeName genericTypePart?;

simpleTypeName: id;

genericTypePart: '\'' POSNUM '[' genericParam (',' genericParam)* ']';
genericParam: '[' typeParameter (WS? '->' WS? type)? ']';

assembly: id (',' WS? assemblyVersion)? ;
assemblyVersion: num '.' num '.' num '.' num;	

method: '[' type ']' WS? '[' type'].' id '(' WS? ( formalParam ( WS? ',' WS? formalParam)*)? WS? ')' ;
formalParam: '[' type']' WS? id;

// basic
UNKNOWN:'?';
id: LETTER (LETTER|num|SIGN)*;
num: '0' | POSNUM;
POSNUM:DIGIT_NON_ZERO DIGIT*;
LETTER:'a'..'z'|'A'..'Z';
SIGN:'+'|'-'|'*'|'/'|'_'|';'|':'|'='|'$'|'#'|'@'|'!';
fragment DIGIT:'0'|DIGIT_NON_ZERO;
fragment DIGIT_NON_ZERO: '1'..'9';
//WS: (' ' | '\t') -> skip;
WS: (' '| '\t')+;
EOL:'\n';
