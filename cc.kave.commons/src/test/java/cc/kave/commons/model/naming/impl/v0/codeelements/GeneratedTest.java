// ##############################################################################
// Attention: This file was auto-generated, do not modify its contents manually!!
// (generated at: 8/13/2016 1:43:28 AM)
// ##############################################################################
/**
* Copyright 2016 Technische Universit„t Darmstadt
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
package cc.kave.commons.model.naming.impl.v0.codeelements;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import cc.kave.commons.model.naming.IParameterizedName;
import cc.kave.commons.model.naming.codeelements.IEventName;
import cc.kave.commons.model.naming.codeelements.IFieldName;
import cc.kave.commons.model.naming.codeelements.IMethodName;
import cc.kave.commons.model.naming.codeelements.IPropertyName;
import cc.kave.commons.model.naming.impl.v0.types.ArrayTypeName;
import cc.kave.commons.model.naming.impl.v0.types.PredefinedTypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeName;
import cc.kave.commons.model.naming.impl.v0.types.TypeParameterName;
public class GeneratedTest {
public static void assertParameterizedName(IParameterizedName n, boolean hasParameters, String... pids){
  assertEquals(hasParameters, n.hasParameters());
  assertEquals(pids.length, n.getParameters().size());
  for(int i = 0; i< pids.length; i++){
    assertEquals(new ParameterName(pids[i]), n.getParameters().get(i));
  }
}
// defaults
@Test
public void FieldNameTest_0_0() {
IFieldName sut = new FieldName();
assertEquals("[?] [?].???", sut.getIdentifier());
assertTrue(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("???", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("???", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
}
@Test
public void EventNameTest_0_0() {
IEventName sut = new EventName();
assertEquals("[?] [?].???", sut.getIdentifier());
assertTrue(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("???", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("???", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getHandlerType());
}
@Test
public void MethodNameTest_0_0() {
IMethodName sut = new MethodName();
assertEquals("[?] [?].???()", sut.getIdentifier());
assertTrue(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("???", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("???", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_0_0() {
IPropertyName sut = new PropertyName();
assertEquals("[?] [?].???", sut.getIdentifier());
assertTrue(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("???", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("???", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
// generated names
@Test
public void FieldNameTest_1_0() {
IFieldName sut = new FieldName("[T,P] [?]._f");
assertEquals("[T,P] [?]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_1_1() {
IFieldName sut = new FieldName("[?] [T,P]._f");
assertEquals("[?] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
}
@Test
public void FieldNameTest_1_2() {
IFieldName sut = new FieldName("static [T,P] [?]._f");
assertEquals("static [T,P] [?]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_1_3() {
IFieldName sut = new FieldName("static [?] [T,P]._f");
assertEquals("static [?] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
}
@Test
public void EventNameTest_1_0() {
IEventName sut = new EventName("[T,P] [?].e");
assertEquals("[T,P] [?].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_1_1() {
IEventName sut = new EventName("[?] [T,P].e");
assertEquals("[?] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getHandlerType());
}
@Test
public void EventNameTest_1_2() {
IEventName sut = new EventName("static [T,P] [?].e");
assertEquals("static [T,P] [?].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_1_3() {
IEventName sut = new EventName("static [?] [T,P].e");
assertEquals("static [?] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getHandlerType());
}
@Test
public void MethodNameTest_1_0() {
IMethodName sut = new MethodName("[T,P] [?].M()");
assertEquals("[T,P] [?].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_1() {
IMethodName sut = new MethodName("[T,P] [?].M`1[[T]]()");
assertEquals("[T,P] [?].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_2() {
IMethodName sut = new MethodName("[T,P] [?].M`1[[T -> ?]]()");
assertEquals("[T,P] [?].M`1[[T -> ?]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_3() {
IMethodName sut = new MethodName("[T,P] [?].M`2[[T],[U]]()");
assertEquals("[T,P] [?].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_4() {
IMethodName sut = new MethodName("[T,P] [?].M()");
assertEquals("[T,P] [?].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_5() {
IMethodName sut = new MethodName("[T,P] [?].M(out [?] p)");
assertEquals("[T,P] [?].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_1_6() {
IMethodName sut = new MethodName("[T,P] [?].M([?] p)");
assertEquals("[T,P] [?].M([?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?] p");
}
@Test
public void MethodNameTest_1_7() {
IMethodName sut = new MethodName("[T,P] [?].M([?] p1, [?] p2)");
assertEquals("[T,P] [?].M([?] p1, [?] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?] p1", "[?] p2");
}
@Test
public void MethodNameTest_1_8() {
IMethodName sut = new MethodName("[?] [T,P].M()");
assertEquals("[?] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_9() {
IMethodName sut = new MethodName("[?] [T,P].M`1[[T]]()");
assertEquals("[?] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_10() {
IMethodName sut = new MethodName("[?] [T,P].M`1[[T -> ?]]()");
assertEquals("[?] [T,P].M`1[[T -> ?]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_11() {
IMethodName sut = new MethodName("[?] [T,P].M`2[[T],[U]]()");
assertEquals("[?] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_12() {
IMethodName sut = new MethodName("[?] [T,P].M()");
assertEquals("[?] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_13() {
IMethodName sut = new MethodName("[?] [T,P].M(out [?] p)");
assertEquals("[?] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_1_14() {
IMethodName sut = new MethodName("[?] [T,P].M([?] p)");
assertEquals("[?] [T,P].M([?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?] p");
}
@Test
public void MethodNameTest_1_15() {
IMethodName sut = new MethodName("[?] [T,P].M([?] p1, [?] p2)");
assertEquals("[?] [T,P].M([?] p1, [?] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?] p1", "[?] p2");
}
@Test
public void MethodNameTest_1_16() {
IMethodName sut = new MethodName("static [T,P] [?].M()");
assertEquals("static [T,P] [?].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_17() {
IMethodName sut = new MethodName("static [T,P] [?].M`1[[T]]()");
assertEquals("static [T,P] [?].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_18() {
IMethodName sut = new MethodName("static [T,P] [?].M`1[[T -> ?]]()");
assertEquals("static [T,P] [?].M`1[[T -> ?]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_19() {
IMethodName sut = new MethodName("static [T,P] [?].M`2[[T],[U]]()");
assertEquals("static [T,P] [?].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_20() {
IMethodName sut = new MethodName("static [T,P] [?].M()");
assertEquals("static [T,P] [?].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_21() {
IMethodName sut = new MethodName("static [T,P] [?].M(out [?] p)");
assertEquals("static [T,P] [?].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_1_22() {
IMethodName sut = new MethodName("static [T,P] [?].M([?] p)");
assertEquals("static [T,P] [?].M([?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?] p");
}
@Test
public void MethodNameTest_1_23() {
IMethodName sut = new MethodName("static [T,P] [?].M([?] p1, [?] p2)");
assertEquals("static [T,P] [?].M([?] p1, [?] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?] p1", "[?] p2");
}
@Test
public void MethodNameTest_1_24() {
IMethodName sut = new MethodName("static [?] [T,P].M()");
assertEquals("static [?] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_25() {
IMethodName sut = new MethodName("static [?] [T,P].M`1[[T]]()");
assertEquals("static [?] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_26() {
IMethodName sut = new MethodName("static [?] [T,P].M`1[[T -> ?]]()");
assertEquals("static [?] [T,P].M`1[[T -> ?]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_27() {
IMethodName sut = new MethodName("static [?] [T,P].M`2[[T],[U]]()");
assertEquals("static [?] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_28() {
IMethodName sut = new MethodName("static [?] [T,P].M()");
assertEquals("static [?] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_1_29() {
IMethodName sut = new MethodName("static [?] [T,P].M(out [?] p)");
assertEquals("static [?] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_1_30() {
IMethodName sut = new MethodName("static [?] [T,P].M([?] p)");
assertEquals("static [?] [T,P].M([?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?] p");
}
@Test
public void MethodNameTest_1_31() {
IMethodName sut = new MethodName("static [?] [T,P].M([?] p1, [?] p2)");
assertEquals("static [?] [T,P].M([?] p1, [?] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertEquals(new TypeName("?"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?] p1", "[?] p2");
}
@Test
public void PropertyNameTest_1_0() {
IPropertyName sut = new PropertyName("[T,P] [?].P()");
assertEquals("[T,P] [?].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_1_1() {
IPropertyName sut = new PropertyName("[?] [T,P].P()");
assertEquals("[?] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_1_2() {
IPropertyName sut = new PropertyName("static [T,P] [?].P()");
assertEquals("static [T,P] [?].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("?"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_1_3() {
IPropertyName sut = new PropertyName("static [?] [T,P].P()");
assertEquals("static [?] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("?"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_2_0() {
IFieldName sut = new FieldName("[T,P] [?[]]._f");
assertEquals("[T,P] [?[]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_2_1() {
IFieldName sut = new FieldName("[?[]] [T,P]._f");
assertEquals("[?[]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
}
@Test
public void FieldNameTest_2_2() {
IFieldName sut = new FieldName("static [T,P] [?[]]._f");
assertEquals("static [T,P] [?[]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_2_3() {
IFieldName sut = new FieldName("static [?[]] [T,P]._f");
assertEquals("static [?[]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
}
@Test
public void EventNameTest_2_0() {
IEventName sut = new EventName("[T,P] [?[]].e");
assertEquals("[T,P] [?[]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_2_1() {
IEventName sut = new EventName("[?[]] [T,P].e");
assertEquals("[?[]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getHandlerType());
}
@Test
public void EventNameTest_2_2() {
IEventName sut = new EventName("static [T,P] [?[]].e");
assertEquals("static [T,P] [?[]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_2_3() {
IEventName sut = new EventName("static [?[]] [T,P].e");
assertEquals("static [?[]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getHandlerType());
}
@Test
public void MethodNameTest_2_0() {
IMethodName sut = new MethodName("[T,P] [?[]].M()");
assertEquals("[T,P] [?[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_1() {
IMethodName sut = new MethodName("[T,P] [?[]].M`1[[T]]()");
assertEquals("[T,P] [?[]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_2() {
IMethodName sut = new MethodName("[T,P] [?[]].M`1[[T -> ?[]]]()");
assertEquals("[T,P] [?[]].M`1[[T -> ?[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?[]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_3() {
IMethodName sut = new MethodName("[T,P] [?[]].M`2[[T],[U]]()");
assertEquals("[T,P] [?[]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_4() {
IMethodName sut = new MethodName("[T,P] [?[]].M()");
assertEquals("[T,P] [?[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_5() {
IMethodName sut = new MethodName("[T,P] [?[]].M(out [?] p)");
assertEquals("[T,P] [?[]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_2_6() {
IMethodName sut = new MethodName("[T,P] [?[]].M([?[]] p)");
assertEquals("[T,P] [?[]].M([?[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[]] p");
}
@Test
public void MethodNameTest_2_7() {
IMethodName sut = new MethodName("[T,P] [?[]].M([?[]] p1, [?[]] p2)");
assertEquals("[T,P] [?[]].M([?[]] p1, [?[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[]] p1", "[?[]] p2");
}
@Test
public void MethodNameTest_2_8() {
IMethodName sut = new MethodName("[?[]] [T,P].M()");
assertEquals("[?[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_9() {
IMethodName sut = new MethodName("[?[]] [T,P].M`1[[T]]()");
assertEquals("[?[]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_10() {
IMethodName sut = new MethodName("[?[]] [T,P].M`1[[T -> ?[]]]()");
assertEquals("[?[]] [T,P].M`1[[T -> ?[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?[]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_11() {
IMethodName sut = new MethodName("[?[]] [T,P].M`2[[T],[U]]()");
assertEquals("[?[]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_12() {
IMethodName sut = new MethodName("[?[]] [T,P].M()");
assertEquals("[?[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_13() {
IMethodName sut = new MethodName("[?[]] [T,P].M(out [?] p)");
assertEquals("[?[]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_2_14() {
IMethodName sut = new MethodName("[?[]] [T,P].M([?[]] p)");
assertEquals("[?[]] [T,P].M([?[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[]] p");
}
@Test
public void MethodNameTest_2_15() {
IMethodName sut = new MethodName("[?[]] [T,P].M([?[]] p1, [?[]] p2)");
assertEquals("[?[]] [T,P].M([?[]] p1, [?[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[]] p1", "[?[]] p2");
}
@Test
public void MethodNameTest_2_16() {
IMethodName sut = new MethodName("static [T,P] [?[]].M()");
assertEquals("static [T,P] [?[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_17() {
IMethodName sut = new MethodName("static [T,P] [?[]].M`1[[T]]()");
assertEquals("static [T,P] [?[]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_18() {
IMethodName sut = new MethodName("static [T,P] [?[]].M`1[[T -> ?[]]]()");
assertEquals("static [T,P] [?[]].M`1[[T -> ?[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?[]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_19() {
IMethodName sut = new MethodName("static [T,P] [?[]].M`2[[T],[U]]()");
assertEquals("static [T,P] [?[]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_20() {
IMethodName sut = new MethodName("static [T,P] [?[]].M()");
assertEquals("static [T,P] [?[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_21() {
IMethodName sut = new MethodName("static [T,P] [?[]].M(out [?] p)");
assertEquals("static [T,P] [?[]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_2_22() {
IMethodName sut = new MethodName("static [T,P] [?[]].M([?[]] p)");
assertEquals("static [T,P] [?[]].M([?[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[]] p");
}
@Test
public void MethodNameTest_2_23() {
IMethodName sut = new MethodName("static [T,P] [?[]].M([?[]] p1, [?[]] p2)");
assertEquals("static [T,P] [?[]].M([?[]] p1, [?[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[]] p1", "[?[]] p2");
}
@Test
public void MethodNameTest_2_24() {
IMethodName sut = new MethodName("static [?[]] [T,P].M()");
assertEquals("static [?[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_25() {
IMethodName sut = new MethodName("static [?[]] [T,P].M`1[[T]]()");
assertEquals("static [?[]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_26() {
IMethodName sut = new MethodName("static [?[]] [T,P].M`1[[T -> ?[]]]()");
assertEquals("static [?[]] [T,P].M`1[[T -> ?[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?[]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_27() {
IMethodName sut = new MethodName("static [?[]] [T,P].M`2[[T],[U]]()");
assertEquals("static [?[]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_28() {
IMethodName sut = new MethodName("static [?[]] [T,P].M()");
assertEquals("static [?[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_2_29() {
IMethodName sut = new MethodName("static [?[]] [T,P].M(out [?] p)");
assertEquals("static [?[]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_2_30() {
IMethodName sut = new MethodName("static [?[]] [T,P].M([?[]] p)");
assertEquals("static [?[]] [T,P].M([?[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[]] p");
}
@Test
public void MethodNameTest_2_31() {
IMethodName sut = new MethodName("static [?[]] [T,P].M([?[]] p1, [?[]] p2)");
assertEquals("static [?[]] [T,P].M([?[]] p1, [?[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[]] p1", "[?[]] p2");
}
@Test
public void PropertyNameTest_2_0() {
IPropertyName sut = new PropertyName("[T,P] [?[]].P()");
assertEquals("[T,P] [?[]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_2_1() {
IPropertyName sut = new PropertyName("[?[]] [T,P].P()");
assertEquals("[?[]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_2_2() {
IPropertyName sut = new PropertyName("static [T,P] [?[]].P()");
assertEquals("static [T,P] [?[]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_2_3() {
IPropertyName sut = new PropertyName("static [?[]] [T,P].P()");
assertEquals("static [?[]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("?[]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_3_0() {
IFieldName sut = new FieldName("[T,P] [?[,]]._f");
assertEquals("[T,P] [?[,]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_3_1() {
IFieldName sut = new FieldName("[?[,]] [T,P]._f");
assertEquals("[?[,]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
}
@Test
public void FieldNameTest_3_2() {
IFieldName sut = new FieldName("static [T,P] [?[,]]._f");
assertEquals("static [T,P] [?[,]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_3_3() {
IFieldName sut = new FieldName("static [?[,]] [T,P]._f");
assertEquals("static [?[,]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
}
@Test
public void EventNameTest_3_0() {
IEventName sut = new EventName("[T,P] [?[,]].e");
assertEquals("[T,P] [?[,]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_3_1() {
IEventName sut = new EventName("[?[,]] [T,P].e");
assertEquals("[?[,]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getHandlerType());
}
@Test
public void EventNameTest_3_2() {
IEventName sut = new EventName("static [T,P] [?[,]].e");
assertEquals("static [T,P] [?[,]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_3_3() {
IEventName sut = new EventName("static [?[,]] [T,P].e");
assertEquals("static [?[,]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getHandlerType());
}
@Test
public void MethodNameTest_3_0() {
IMethodName sut = new MethodName("[T,P] [?[,]].M()");
assertEquals("[T,P] [?[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_1() {
IMethodName sut = new MethodName("[T,P] [?[,]].M`1[[T]]()");
assertEquals("[T,P] [?[,]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_2() {
IMethodName sut = new MethodName("[T,P] [?[,]].M`1[[T -> ?[,]]]()");
assertEquals("[T,P] [?[,]].M`1[[T -> ?[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?[,]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_3() {
IMethodName sut = new MethodName("[T,P] [?[,]].M`2[[T],[U]]()");
assertEquals("[T,P] [?[,]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_4() {
IMethodName sut = new MethodName("[T,P] [?[,]].M()");
assertEquals("[T,P] [?[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_5() {
IMethodName sut = new MethodName("[T,P] [?[,]].M(out [?] p)");
assertEquals("[T,P] [?[,]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_3_6() {
IMethodName sut = new MethodName("[T,P] [?[,]].M([?[,]] p)");
assertEquals("[T,P] [?[,]].M([?[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[,]] p");
}
@Test
public void MethodNameTest_3_7() {
IMethodName sut = new MethodName("[T,P] [?[,]].M([?[,]] p1, [?[,]] p2)");
assertEquals("[T,P] [?[,]].M([?[,]] p1, [?[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[,]] p1", "[?[,]] p2");
}
@Test
public void MethodNameTest_3_8() {
IMethodName sut = new MethodName("[?[,]] [T,P].M()");
assertEquals("[?[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_9() {
IMethodName sut = new MethodName("[?[,]] [T,P].M`1[[T]]()");
assertEquals("[?[,]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_10() {
IMethodName sut = new MethodName("[?[,]] [T,P].M`1[[T -> ?[,]]]()");
assertEquals("[?[,]] [T,P].M`1[[T -> ?[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?[,]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_11() {
IMethodName sut = new MethodName("[?[,]] [T,P].M`2[[T],[U]]()");
assertEquals("[?[,]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_12() {
IMethodName sut = new MethodName("[?[,]] [T,P].M()");
assertEquals("[?[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_13() {
IMethodName sut = new MethodName("[?[,]] [T,P].M(out [?] p)");
assertEquals("[?[,]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_3_14() {
IMethodName sut = new MethodName("[?[,]] [T,P].M([?[,]] p)");
assertEquals("[?[,]] [T,P].M([?[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[,]] p");
}
@Test
public void MethodNameTest_3_15() {
IMethodName sut = new MethodName("[?[,]] [T,P].M([?[,]] p1, [?[,]] p2)");
assertEquals("[?[,]] [T,P].M([?[,]] p1, [?[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[,]] p1", "[?[,]] p2");
}
@Test
public void MethodNameTest_3_16() {
IMethodName sut = new MethodName("static [T,P] [?[,]].M()");
assertEquals("static [T,P] [?[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_17() {
IMethodName sut = new MethodName("static [T,P] [?[,]].M`1[[T]]()");
assertEquals("static [T,P] [?[,]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_18() {
IMethodName sut = new MethodName("static [T,P] [?[,]].M`1[[T -> ?[,]]]()");
assertEquals("static [T,P] [?[,]].M`1[[T -> ?[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?[,]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_19() {
IMethodName sut = new MethodName("static [T,P] [?[,]].M`2[[T],[U]]()");
assertEquals("static [T,P] [?[,]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_20() {
IMethodName sut = new MethodName("static [T,P] [?[,]].M()");
assertEquals("static [T,P] [?[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_21() {
IMethodName sut = new MethodName("static [T,P] [?[,]].M(out [?] p)");
assertEquals("static [T,P] [?[,]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_3_22() {
IMethodName sut = new MethodName("static [T,P] [?[,]].M([?[,]] p)");
assertEquals("static [T,P] [?[,]].M([?[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[,]] p");
}
@Test
public void MethodNameTest_3_23() {
IMethodName sut = new MethodName("static [T,P] [?[,]].M([?[,]] p1, [?[,]] p2)");
assertEquals("static [T,P] [?[,]].M([?[,]] p1, [?[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[,]] p1", "[?[,]] p2");
}
@Test
public void MethodNameTest_3_24() {
IMethodName sut = new MethodName("static [?[,]] [T,P].M()");
assertEquals("static [?[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_25() {
IMethodName sut = new MethodName("static [?[,]] [T,P].M`1[[T]]()");
assertEquals("static [?[,]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_26() {
IMethodName sut = new MethodName("static [?[,]] [T,P].M`1[[T -> ?[,]]]()");
assertEquals("static [?[,]] [T,P].M`1[[T -> ?[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> ?[,]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_27() {
IMethodName sut = new MethodName("static [?[,]] [T,P].M`2[[T],[U]]()");
assertEquals("static [?[,]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_28() {
IMethodName sut = new MethodName("static [?[,]] [T,P].M()");
assertEquals("static [?[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_3_29() {
IMethodName sut = new MethodName("static [?[,]] [T,P].M(out [?] p)");
assertEquals("static [?[,]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_3_30() {
IMethodName sut = new MethodName("static [?[,]] [T,P].M([?[,]] p)");
assertEquals("static [?[,]] [T,P].M([?[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[,]] p");
}
@Test
public void MethodNameTest_3_31() {
IMethodName sut = new MethodName("static [?[,]] [T,P].M([?[,]] p1, [?[,]] p2)");
assertEquals("static [?[,]] [T,P].M([?[,]] p1, [?[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertEquals(new ArrayTypeName("?[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[?[,]] p1", "[?[,]] p2");
}
@Test
public void PropertyNameTest_3_0() {
IPropertyName sut = new PropertyName("[T,P] [?[,]].P()");
assertEquals("[T,P] [?[,]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_3_1() {
IPropertyName sut = new PropertyName("[?[,]] [T,P].P()");
assertEquals("[?[,]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_3_2() {
IPropertyName sut = new PropertyName("static [T,P] [?[,]].P()");
assertEquals("static [T,P] [?[,]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("?[,]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_3_3() {
IPropertyName sut = new PropertyName("static [?[,]] [T,P].P()");
assertEquals("static [?[,]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("?[,]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_4_0() {
IFieldName sut = new FieldName("[T,P] [T,P]._f");
assertEquals("[T,P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_4_1() {
IFieldName sut = new FieldName("[T,P] [T,P]._f");
assertEquals("[T,P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_4_2() {
IFieldName sut = new FieldName("static [T,P] [T,P]._f");
assertEquals("static [T,P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_4_3() {
IFieldName sut = new FieldName("static [T,P] [T,P]._f");
assertEquals("static [T,P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void EventNameTest_4_0() {
IEventName sut = new EventName("[T,P] [T,P].e");
assertEquals("[T,P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_4_1() {
IEventName sut = new EventName("[T,P] [T,P].e");
assertEquals("[T,P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_4_2() {
IEventName sut = new EventName("static [T,P] [T,P].e");
assertEquals("static [T,P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_4_3() {
IEventName sut = new EventName("static [T,P] [T,P].e");
assertEquals("static [T,P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_4_0() {
IMethodName sut = new MethodName("[T,P] [T,P].M()");
assertEquals("[T,P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_1() {
IMethodName sut = new MethodName("[T,P] [T,P].M`1[[T]]()");
assertEquals("[T,P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_2() {
IMethodName sut = new MethodName("[T,P] [T,P].M`1[[T -> T,P]]()");
assertEquals("[T,P] [T,P].M`1[[T -> T,P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T,P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_3() {
IMethodName sut = new MethodName("[T,P] [T,P].M`2[[T],[U]]()");
assertEquals("[T,P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_4() {
IMethodName sut = new MethodName("[T,P] [T,P].M()");
assertEquals("[T,P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_5() {
IMethodName sut = new MethodName("[T,P] [T,P].M(out [?] p)");
assertEquals("[T,P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_4_6() {
IMethodName sut = new MethodName("[T,P] [T,P].M([T,P] p)");
assertEquals("[T,P] [T,P].M([T,P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T,P] p");
}
@Test
public void MethodNameTest_4_7() {
IMethodName sut = new MethodName("[T,P] [T,P].M([T,P] p1, [T,P] p2)");
assertEquals("[T,P] [T,P].M([T,P] p1, [T,P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T,P] p1", "[T,P] p2");
}
@Test
public void MethodNameTest_4_8() {
IMethodName sut = new MethodName("[T,P] [T,P].M()");
assertEquals("[T,P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_9() {
IMethodName sut = new MethodName("[T,P] [T,P].M`1[[T]]()");
assertEquals("[T,P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_10() {
IMethodName sut = new MethodName("[T,P] [T,P].M`1[[T -> T,P]]()");
assertEquals("[T,P] [T,P].M`1[[T -> T,P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T,P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_11() {
IMethodName sut = new MethodName("[T,P] [T,P].M`2[[T],[U]]()");
assertEquals("[T,P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_12() {
IMethodName sut = new MethodName("[T,P] [T,P].M()");
assertEquals("[T,P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_13() {
IMethodName sut = new MethodName("[T,P] [T,P].M(out [?] p)");
assertEquals("[T,P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_4_14() {
IMethodName sut = new MethodName("[T,P] [T,P].M([T,P] p)");
assertEquals("[T,P] [T,P].M([T,P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T,P] p");
}
@Test
public void MethodNameTest_4_15() {
IMethodName sut = new MethodName("[T,P] [T,P].M([T,P] p1, [T,P] p2)");
assertEquals("[T,P] [T,P].M([T,P] p1, [T,P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T,P] p1", "[T,P] p2");
}
@Test
public void MethodNameTest_4_16() {
IMethodName sut = new MethodName("static [T,P] [T,P].M()");
assertEquals("static [T,P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_17() {
IMethodName sut = new MethodName("static [T,P] [T,P].M`1[[T]]()");
assertEquals("static [T,P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_18() {
IMethodName sut = new MethodName("static [T,P] [T,P].M`1[[T -> T,P]]()");
assertEquals("static [T,P] [T,P].M`1[[T -> T,P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T,P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_19() {
IMethodName sut = new MethodName("static [T,P] [T,P].M`2[[T],[U]]()");
assertEquals("static [T,P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_20() {
IMethodName sut = new MethodName("static [T,P] [T,P].M()");
assertEquals("static [T,P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_21() {
IMethodName sut = new MethodName("static [T,P] [T,P].M(out [?] p)");
assertEquals("static [T,P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_4_22() {
IMethodName sut = new MethodName("static [T,P] [T,P].M([T,P] p)");
assertEquals("static [T,P] [T,P].M([T,P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T,P] p");
}
@Test
public void MethodNameTest_4_23() {
IMethodName sut = new MethodName("static [T,P] [T,P].M([T,P] p1, [T,P] p2)");
assertEquals("static [T,P] [T,P].M([T,P] p1, [T,P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T,P] p1", "[T,P] p2");
}
@Test
public void MethodNameTest_4_24() {
IMethodName sut = new MethodName("static [T,P] [T,P].M()");
assertEquals("static [T,P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_25() {
IMethodName sut = new MethodName("static [T,P] [T,P].M`1[[T]]()");
assertEquals("static [T,P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_26() {
IMethodName sut = new MethodName("static [T,P] [T,P].M`1[[T -> T,P]]()");
assertEquals("static [T,P] [T,P].M`1[[T -> T,P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T,P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_27() {
IMethodName sut = new MethodName("static [T,P] [T,P].M`2[[T],[U]]()");
assertEquals("static [T,P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_28() {
IMethodName sut = new MethodName("static [T,P] [T,P].M()");
assertEquals("static [T,P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_4_29() {
IMethodName sut = new MethodName("static [T,P] [T,P].M(out [?] p)");
assertEquals("static [T,P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_4_30() {
IMethodName sut = new MethodName("static [T,P] [T,P].M([T,P] p)");
assertEquals("static [T,P] [T,P].M([T,P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T,P] p");
}
@Test
public void MethodNameTest_4_31() {
IMethodName sut = new MethodName("static [T,P] [T,P].M([T,P] p1, [T,P] p2)");
assertEquals("static [T,P] [T,P].M([T,P] p1, [T,P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T,P] p1", "[T,P] p2");
}
@Test
public void PropertyNameTest_4_0() {
IPropertyName sut = new PropertyName("[T,P] [T,P].P()");
assertEquals("[T,P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_4_1() {
IPropertyName sut = new PropertyName("[T,P] [T,P].P()");
assertEquals("[T,P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_4_2() {
IPropertyName sut = new PropertyName("static [T,P] [T,P].P()");
assertEquals("static [T,P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_4_3() {
IPropertyName sut = new PropertyName("static [T,P] [T,P].P()");
assertEquals("static [T,P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_5_0() {
IFieldName sut = new FieldName("[T,P] [T[],P]._f");
assertEquals("[T,P] [T[],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_5_1() {
IFieldName sut = new FieldName("[T[],P] [T,P]._f");
assertEquals("[T[],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
}
@Test
public void FieldNameTest_5_2() {
IFieldName sut = new FieldName("static [T,P] [T[],P]._f");
assertEquals("static [T,P] [T[],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_5_3() {
IFieldName sut = new FieldName("static [T[],P] [T,P]._f");
assertEquals("static [T[],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
}
@Test
public void EventNameTest_5_0() {
IEventName sut = new EventName("[T,P] [T[],P].e");
assertEquals("[T,P] [T[],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_5_1() {
IEventName sut = new EventName("[T[],P] [T,P].e");
assertEquals("[T[],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getHandlerType());
}
@Test
public void EventNameTest_5_2() {
IEventName sut = new EventName("static [T,P] [T[],P].e");
assertEquals("static [T,P] [T[],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_5_3() {
IEventName sut = new EventName("static [T[],P] [T,P].e");
assertEquals("static [T[],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_5_0() {
IMethodName sut = new MethodName("[T,P] [T[],P].M()");
assertEquals("[T,P] [T[],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_1() {
IMethodName sut = new MethodName("[T,P] [T[],P].M`1[[T]]()");
assertEquals("[T,P] [T[],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_2() {
IMethodName sut = new MethodName("[T,P] [T[],P].M`1[[T -> T[],P]]()");
assertEquals("[T,P] [T[],P].M`1[[T -> T[],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_3() {
IMethodName sut = new MethodName("[T,P] [T[],P].M`2[[T],[U]]()");
assertEquals("[T,P] [T[],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_4() {
IMethodName sut = new MethodName("[T,P] [T[],P].M()");
assertEquals("[T,P] [T[],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_5() {
IMethodName sut = new MethodName("[T,P] [T[],P].M(out [?] p)");
assertEquals("[T,P] [T[],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_5_6() {
IMethodName sut = new MethodName("[T,P] [T[],P].M([T[],P] p)");
assertEquals("[T,P] [T[],P].M([T[],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[],P] p");
}
@Test
public void MethodNameTest_5_7() {
IMethodName sut = new MethodName("[T,P] [T[],P].M([T[],P] p1, [T[],P] p2)");
assertEquals("[T,P] [T[],P].M([T[],P] p1, [T[],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[],P] p1", "[T[],P] p2");
}
@Test
public void MethodNameTest_5_8() {
IMethodName sut = new MethodName("[T[],P] [T,P].M()");
assertEquals("[T[],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_9() {
IMethodName sut = new MethodName("[T[],P] [T,P].M`1[[T]]()");
assertEquals("[T[],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_10() {
IMethodName sut = new MethodName("[T[],P] [T,P].M`1[[T -> T[],P]]()");
assertEquals("[T[],P] [T,P].M`1[[T -> T[],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_11() {
IMethodName sut = new MethodName("[T[],P] [T,P].M`2[[T],[U]]()");
assertEquals("[T[],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_12() {
IMethodName sut = new MethodName("[T[],P] [T,P].M()");
assertEquals("[T[],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_13() {
IMethodName sut = new MethodName("[T[],P] [T,P].M(out [?] p)");
assertEquals("[T[],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_5_14() {
IMethodName sut = new MethodName("[T[],P] [T,P].M([T[],P] p)");
assertEquals("[T[],P] [T,P].M([T[],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[],P] p");
}
@Test
public void MethodNameTest_5_15() {
IMethodName sut = new MethodName("[T[],P] [T,P].M([T[],P] p1, [T[],P] p2)");
assertEquals("[T[],P] [T,P].M([T[],P] p1, [T[],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[],P] p1", "[T[],P] p2");
}
@Test
public void MethodNameTest_5_16() {
IMethodName sut = new MethodName("static [T,P] [T[],P].M()");
assertEquals("static [T,P] [T[],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_17() {
IMethodName sut = new MethodName("static [T,P] [T[],P].M`1[[T]]()");
assertEquals("static [T,P] [T[],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_18() {
IMethodName sut = new MethodName("static [T,P] [T[],P].M`1[[T -> T[],P]]()");
assertEquals("static [T,P] [T[],P].M`1[[T -> T[],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_19() {
IMethodName sut = new MethodName("static [T,P] [T[],P].M`2[[T],[U]]()");
assertEquals("static [T,P] [T[],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_20() {
IMethodName sut = new MethodName("static [T,P] [T[],P].M()");
assertEquals("static [T,P] [T[],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_21() {
IMethodName sut = new MethodName("static [T,P] [T[],P].M(out [?] p)");
assertEquals("static [T,P] [T[],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_5_22() {
IMethodName sut = new MethodName("static [T,P] [T[],P].M([T[],P] p)");
assertEquals("static [T,P] [T[],P].M([T[],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[],P] p");
}
@Test
public void MethodNameTest_5_23() {
IMethodName sut = new MethodName("static [T,P] [T[],P].M([T[],P] p1, [T[],P] p2)");
assertEquals("static [T,P] [T[],P].M([T[],P] p1, [T[],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[],P] p1", "[T[],P] p2");
}
@Test
public void MethodNameTest_5_24() {
IMethodName sut = new MethodName("static [T[],P] [T,P].M()");
assertEquals("static [T[],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_25() {
IMethodName sut = new MethodName("static [T[],P] [T,P].M`1[[T]]()");
assertEquals("static [T[],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_26() {
IMethodName sut = new MethodName("static [T[],P] [T,P].M`1[[T -> T[],P]]()");
assertEquals("static [T[],P] [T,P].M`1[[T -> T[],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_27() {
IMethodName sut = new MethodName("static [T[],P] [T,P].M`2[[T],[U]]()");
assertEquals("static [T[],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_28() {
IMethodName sut = new MethodName("static [T[],P] [T,P].M()");
assertEquals("static [T[],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_5_29() {
IMethodName sut = new MethodName("static [T[],P] [T,P].M(out [?] p)");
assertEquals("static [T[],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_5_30() {
IMethodName sut = new MethodName("static [T[],P] [T,P].M([T[],P] p)");
assertEquals("static [T[],P] [T,P].M([T[],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[],P] p");
}
@Test
public void MethodNameTest_5_31() {
IMethodName sut = new MethodName("static [T[],P] [T,P].M([T[],P] p1, [T[],P] p2)");
assertEquals("static [T[],P] [T,P].M([T[],P] p1, [T[],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[],P] p1", "[T[],P] p2");
}
@Test
public void PropertyNameTest_5_0() {
IPropertyName sut = new PropertyName("[T,P] [T[],P].P()");
assertEquals("[T,P] [T[],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_5_1() {
IPropertyName sut = new PropertyName("[T[],P] [T,P].P()");
assertEquals("[T[],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_5_2() {
IPropertyName sut = new PropertyName("static [T,P] [T[],P].P()");
assertEquals("static [T,P] [T[],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_5_3() {
IPropertyName sut = new PropertyName("static [T[],P] [T,P].P()");
assertEquals("static [T[],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("T[],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_6_0() {
IFieldName sut = new FieldName("[T,P] [T[,],P]._f");
assertEquals("[T,P] [T[,],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_6_1() {
IFieldName sut = new FieldName("[T[,],P] [T,P]._f");
assertEquals("[T[,],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
}
@Test
public void FieldNameTest_6_2() {
IFieldName sut = new FieldName("static [T,P] [T[,],P]._f");
assertEquals("static [T,P] [T[,],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_6_3() {
IFieldName sut = new FieldName("static [T[,],P] [T,P]._f");
assertEquals("static [T[,],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
}
@Test
public void EventNameTest_6_0() {
IEventName sut = new EventName("[T,P] [T[,],P].e");
assertEquals("[T,P] [T[,],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_6_1() {
IEventName sut = new EventName("[T[,],P] [T,P].e");
assertEquals("[T[,],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getHandlerType());
}
@Test
public void EventNameTest_6_2() {
IEventName sut = new EventName("static [T,P] [T[,],P].e");
assertEquals("static [T,P] [T[,],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_6_3() {
IEventName sut = new EventName("static [T[,],P] [T,P].e");
assertEquals("static [T[,],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_6_0() {
IMethodName sut = new MethodName("[T,P] [T[,],P].M()");
assertEquals("[T,P] [T[,],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_1() {
IMethodName sut = new MethodName("[T,P] [T[,],P].M`1[[T]]()");
assertEquals("[T,P] [T[,],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_2() {
IMethodName sut = new MethodName("[T,P] [T[,],P].M`1[[T -> T[,],P]]()");
assertEquals("[T,P] [T[,],P].M`1[[T -> T[,],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[,],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_3() {
IMethodName sut = new MethodName("[T,P] [T[,],P].M`2[[T],[U]]()");
assertEquals("[T,P] [T[,],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_4() {
IMethodName sut = new MethodName("[T,P] [T[,],P].M()");
assertEquals("[T,P] [T[,],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_5() {
IMethodName sut = new MethodName("[T,P] [T[,],P].M(out [?] p)");
assertEquals("[T,P] [T[,],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_6_6() {
IMethodName sut = new MethodName("[T,P] [T[,],P].M([T[,],P] p)");
assertEquals("[T,P] [T[,],P].M([T[,],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,],P] p");
}
@Test
public void MethodNameTest_6_7() {
IMethodName sut = new MethodName("[T,P] [T[,],P].M([T[,],P] p1, [T[,],P] p2)");
assertEquals("[T,P] [T[,],P].M([T[,],P] p1, [T[,],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,],P] p1", "[T[,],P] p2");
}
@Test
public void MethodNameTest_6_8() {
IMethodName sut = new MethodName("[T[,],P] [T,P].M()");
assertEquals("[T[,],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_9() {
IMethodName sut = new MethodName("[T[,],P] [T,P].M`1[[T]]()");
assertEquals("[T[,],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_10() {
IMethodName sut = new MethodName("[T[,],P] [T,P].M`1[[T -> T[,],P]]()");
assertEquals("[T[,],P] [T,P].M`1[[T -> T[,],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[,],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_11() {
IMethodName sut = new MethodName("[T[,],P] [T,P].M`2[[T],[U]]()");
assertEquals("[T[,],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_12() {
IMethodName sut = new MethodName("[T[,],P] [T,P].M()");
assertEquals("[T[,],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_13() {
IMethodName sut = new MethodName("[T[,],P] [T,P].M(out [?] p)");
assertEquals("[T[,],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_6_14() {
IMethodName sut = new MethodName("[T[,],P] [T,P].M([T[,],P] p)");
assertEquals("[T[,],P] [T,P].M([T[,],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,],P] p");
}
@Test
public void MethodNameTest_6_15() {
IMethodName sut = new MethodName("[T[,],P] [T,P].M([T[,],P] p1, [T[,],P] p2)");
assertEquals("[T[,],P] [T,P].M([T[,],P] p1, [T[,],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,],P] p1", "[T[,],P] p2");
}
@Test
public void MethodNameTest_6_16() {
IMethodName sut = new MethodName("static [T,P] [T[,],P].M()");
assertEquals("static [T,P] [T[,],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_17() {
IMethodName sut = new MethodName("static [T,P] [T[,],P].M`1[[T]]()");
assertEquals("static [T,P] [T[,],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_18() {
IMethodName sut = new MethodName("static [T,P] [T[,],P].M`1[[T -> T[,],P]]()");
assertEquals("static [T,P] [T[,],P].M`1[[T -> T[,],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[,],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_19() {
IMethodName sut = new MethodName("static [T,P] [T[,],P].M`2[[T],[U]]()");
assertEquals("static [T,P] [T[,],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_20() {
IMethodName sut = new MethodName("static [T,P] [T[,],P].M()");
assertEquals("static [T,P] [T[,],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_21() {
IMethodName sut = new MethodName("static [T,P] [T[,],P].M(out [?] p)");
assertEquals("static [T,P] [T[,],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_6_22() {
IMethodName sut = new MethodName("static [T,P] [T[,],P].M([T[,],P] p)");
assertEquals("static [T,P] [T[,],P].M([T[,],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,],P] p");
}
@Test
public void MethodNameTest_6_23() {
IMethodName sut = new MethodName("static [T,P] [T[,],P].M([T[,],P] p1, [T[,],P] p2)");
assertEquals("static [T,P] [T[,],P].M([T[,],P] p1, [T[,],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,],P] p1", "[T[,],P] p2");
}
@Test
public void MethodNameTest_6_24() {
IMethodName sut = new MethodName("static [T[,],P] [T,P].M()");
assertEquals("static [T[,],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_25() {
IMethodName sut = new MethodName("static [T[,],P] [T,P].M`1[[T]]()");
assertEquals("static [T[,],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_26() {
IMethodName sut = new MethodName("static [T[,],P] [T,P].M`1[[T -> T[,],P]]()");
assertEquals("static [T[,],P] [T,P].M`1[[T -> T[,],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[,],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_27() {
IMethodName sut = new MethodName("static [T[,],P] [T,P].M`2[[T],[U]]()");
assertEquals("static [T[,],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_28() {
IMethodName sut = new MethodName("static [T[,],P] [T,P].M()");
assertEquals("static [T[,],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_6_29() {
IMethodName sut = new MethodName("static [T[,],P] [T,P].M(out [?] p)");
assertEquals("static [T[,],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_6_30() {
IMethodName sut = new MethodName("static [T[,],P] [T,P].M([T[,],P] p)");
assertEquals("static [T[,],P] [T,P].M([T[,],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,],P] p");
}
@Test
public void MethodNameTest_6_31() {
IMethodName sut = new MethodName("static [T[,],P] [T,P].M([T[,],P] p1, [T[,],P] p2)");
assertEquals("static [T[,],P] [T,P].M([T[,],P] p1, [T[,],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertEquals(new ArrayTypeName("T[,],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,],P] p1", "[T[,],P] p2");
}
@Test
public void PropertyNameTest_6_0() {
IPropertyName sut = new PropertyName("[T,P] [T[,],P].P()");
assertEquals("[T,P] [T[,],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_6_1() {
IPropertyName sut = new PropertyName("[T[,],P] [T,P].P()");
assertEquals("[T[,],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_6_2() {
IPropertyName sut = new PropertyName("static [T,P] [T[,],P].P()");
assertEquals("static [T,P] [T[,],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("T[,],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_6_3() {
IPropertyName sut = new PropertyName("static [T[,],P] [T,P].P()");
assertEquals("static [T[,],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("T[,],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_7_0() {
IFieldName sut = new FieldName("[T,P] [n.T`1[[G]],P]._f");
assertEquals("[T,P] [n.T`1[[G]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_7_1() {
IFieldName sut = new FieldName("[n.T`1[[G]],P] [T,P]._f");
assertEquals("[n.T`1[[G]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
}
@Test
public void FieldNameTest_7_2() {
IFieldName sut = new FieldName("static [T,P] [n.T`1[[G]],P]._f");
assertEquals("static [T,P] [n.T`1[[G]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_7_3() {
IFieldName sut = new FieldName("static [n.T`1[[G]],P] [T,P]._f");
assertEquals("static [n.T`1[[G]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
}
@Test
public void EventNameTest_7_0() {
IEventName sut = new EventName("[T,P] [n.T`1[[G]],P].e");
assertEquals("[T,P] [n.T`1[[G]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_7_1() {
IEventName sut = new EventName("[n.T`1[[G]],P] [T,P].e");
assertEquals("[n.T`1[[G]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getHandlerType());
}
@Test
public void EventNameTest_7_2() {
IEventName sut = new EventName("static [T,P] [n.T`1[[G]],P].e");
assertEquals("static [T,P] [n.T`1[[G]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_7_3() {
IEventName sut = new EventName("static [n.T`1[[G]],P] [T,P].e");
assertEquals("static [n.T`1[[G]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_7_0() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G]],P].M()");
assertEquals("[T,P] [n.T`1[[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_1() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G]],P].M`1[[T]]()");
assertEquals("[T,P] [n.T`1[[G]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_2() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G]],P].M`1[[T -> n.T`1[[G]],P]]()");
assertEquals("[T,P] [n.T`1[[G]],P].M`1[[T -> n.T`1[[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[[G]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_3() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G]],P].M`2[[T],[U]]()");
assertEquals("[T,P] [n.T`1[[G]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_4() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G]],P].M()");
assertEquals("[T,P] [n.T`1[[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_5() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G]],P].M(out [?] p)");
assertEquals("[T,P] [n.T`1[[G]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_7_6() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G]],P].M([n.T`1[[G]],P] p)");
assertEquals("[T,P] [n.T`1[[G]],P].M([n.T`1[[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G]],P] p");
}
@Test
public void MethodNameTest_7_7() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G]],P].M([n.T`1[[G]],P] p1, [n.T`1[[G]],P] p2)");
assertEquals("[T,P] [n.T`1[[G]],P].M([n.T`1[[G]],P] p1, [n.T`1[[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G]],P] p1", "[n.T`1[[G]],P] p2");
}
@Test
public void MethodNameTest_7_8() {
IMethodName sut = new MethodName("[n.T`1[[G]],P] [T,P].M()");
assertEquals("[n.T`1[[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_9() {
IMethodName sut = new MethodName("[n.T`1[[G]],P] [T,P].M`1[[T]]()");
assertEquals("[n.T`1[[G]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_10() {
IMethodName sut = new MethodName("[n.T`1[[G]],P] [T,P].M`1[[T -> n.T`1[[G]],P]]()");
assertEquals("[n.T`1[[G]],P] [T,P].M`1[[T -> n.T`1[[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[[G]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_11() {
IMethodName sut = new MethodName("[n.T`1[[G]],P] [T,P].M`2[[T],[U]]()");
assertEquals("[n.T`1[[G]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_12() {
IMethodName sut = new MethodName("[n.T`1[[G]],P] [T,P].M()");
assertEquals("[n.T`1[[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_13() {
IMethodName sut = new MethodName("[n.T`1[[G]],P] [T,P].M(out [?] p)");
assertEquals("[n.T`1[[G]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_7_14() {
IMethodName sut = new MethodName("[n.T`1[[G]],P] [T,P].M([n.T`1[[G]],P] p)");
assertEquals("[n.T`1[[G]],P] [T,P].M([n.T`1[[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G]],P] p");
}
@Test
public void MethodNameTest_7_15() {
IMethodName sut = new MethodName("[n.T`1[[G]],P] [T,P].M([n.T`1[[G]],P] p1, [n.T`1[[G]],P] p2)");
assertEquals("[n.T`1[[G]],P] [T,P].M([n.T`1[[G]],P] p1, [n.T`1[[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G]],P] p1", "[n.T`1[[G]],P] p2");
}
@Test
public void MethodNameTest_7_16() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G]],P].M()");
assertEquals("static [T,P] [n.T`1[[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_17() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G]],P].M`1[[T]]()");
assertEquals("static [T,P] [n.T`1[[G]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_18() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G]],P].M`1[[T -> n.T`1[[G]],P]]()");
assertEquals("static [T,P] [n.T`1[[G]],P].M`1[[T -> n.T`1[[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[[G]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_19() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G]],P].M`2[[T],[U]]()");
assertEquals("static [T,P] [n.T`1[[G]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_20() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G]],P].M()");
assertEquals("static [T,P] [n.T`1[[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_21() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G]],P].M(out [?] p)");
assertEquals("static [T,P] [n.T`1[[G]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_7_22() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G]],P].M([n.T`1[[G]],P] p)");
assertEquals("static [T,P] [n.T`1[[G]],P].M([n.T`1[[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G]],P] p");
}
@Test
public void MethodNameTest_7_23() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G]],P].M([n.T`1[[G]],P] p1, [n.T`1[[G]],P] p2)");
assertEquals("static [T,P] [n.T`1[[G]],P].M([n.T`1[[G]],P] p1, [n.T`1[[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G]],P] p1", "[n.T`1[[G]],P] p2");
}
@Test
public void MethodNameTest_7_24() {
IMethodName sut = new MethodName("static [n.T`1[[G]],P] [T,P].M()");
assertEquals("static [n.T`1[[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_25() {
IMethodName sut = new MethodName("static [n.T`1[[G]],P] [T,P].M`1[[T]]()");
assertEquals("static [n.T`1[[G]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_26() {
IMethodName sut = new MethodName("static [n.T`1[[G]],P] [T,P].M`1[[T -> n.T`1[[G]],P]]()");
assertEquals("static [n.T`1[[G]],P] [T,P].M`1[[T -> n.T`1[[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[[G]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_27() {
IMethodName sut = new MethodName("static [n.T`1[[G]],P] [T,P].M`2[[T],[U]]()");
assertEquals("static [n.T`1[[G]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_28() {
IMethodName sut = new MethodName("static [n.T`1[[G]],P] [T,P].M()");
assertEquals("static [n.T`1[[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_7_29() {
IMethodName sut = new MethodName("static [n.T`1[[G]],P] [T,P].M(out [?] p)");
assertEquals("static [n.T`1[[G]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_7_30() {
IMethodName sut = new MethodName("static [n.T`1[[G]],P] [T,P].M([n.T`1[[G]],P] p)");
assertEquals("static [n.T`1[[G]],P] [T,P].M([n.T`1[[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G]],P] p");
}
@Test
public void MethodNameTest_7_31() {
IMethodName sut = new MethodName("static [n.T`1[[G]],P] [T,P].M([n.T`1[[G]],P] p1, [n.T`1[[G]],P] p2)");
assertEquals("static [n.T`1[[G]],P] [T,P].M([n.T`1[[G]],P] p1, [n.T`1[[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G]],P] p1", "[n.T`1[[G]],P] p2");
}
@Test
public void PropertyNameTest_7_0() {
IPropertyName sut = new PropertyName("[T,P] [n.T`1[[G]],P].P()");
assertEquals("[T,P] [n.T`1[[G]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_7_1() {
IPropertyName sut = new PropertyName("[n.T`1[[G]],P] [T,P].P()");
assertEquals("[n.T`1[[G]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_7_2() {
IPropertyName sut = new PropertyName("static [T,P] [n.T`1[[G]],P].P()");
assertEquals("static [T,P] [n.T`1[[G]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_7_3() {
IPropertyName sut = new PropertyName("static [n.T`1[[G]],P] [T,P].P()");
assertEquals("static [n.T`1[[G]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("n.T`1[[G]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_8_0() {
IFieldName sut = new FieldName("[T,P] [n.T`1[][[G]],P]._f");
assertEquals("[T,P] [n.T`1[][[G]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_8_1() {
IFieldName sut = new FieldName("[n.T`1[][[G]],P] [T,P]._f");
assertEquals("[n.T`1[][[G]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
}
@Test
public void FieldNameTest_8_2() {
IFieldName sut = new FieldName("static [T,P] [n.T`1[][[G]],P]._f");
assertEquals("static [T,P] [n.T`1[][[G]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_8_3() {
IFieldName sut = new FieldName("static [n.T`1[][[G]],P] [T,P]._f");
assertEquals("static [n.T`1[][[G]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
}
@Test
public void EventNameTest_8_0() {
IEventName sut = new EventName("[T,P] [n.T`1[][[G]],P].e");
assertEquals("[T,P] [n.T`1[][[G]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_8_1() {
IEventName sut = new EventName("[n.T`1[][[G]],P] [T,P].e");
assertEquals("[n.T`1[][[G]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getHandlerType());
}
@Test
public void EventNameTest_8_2() {
IEventName sut = new EventName("static [T,P] [n.T`1[][[G]],P].e");
assertEquals("static [T,P] [n.T`1[][[G]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_8_3() {
IEventName sut = new EventName("static [n.T`1[][[G]],P] [T,P].e");
assertEquals("static [n.T`1[][[G]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_8_0() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G]],P].M()");
assertEquals("[T,P] [n.T`1[][[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_1() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G]],P].M`1[[T]]()");
assertEquals("[T,P] [n.T`1[][[G]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_2() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G]],P].M`1[[T -> n.T`1[][[G]],P]]()");
assertEquals("[T,P] [n.T`1[][[G]],P].M`1[[T -> n.T`1[][[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[][[G]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_3() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G]],P].M`2[[T],[U]]()");
assertEquals("[T,P] [n.T`1[][[G]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_4() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G]],P].M()");
assertEquals("[T,P] [n.T`1[][[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_5() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G]],P].M(out [?] p)");
assertEquals("[T,P] [n.T`1[][[G]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_8_6() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G]],P].M([n.T`1[][[G]],P] p)");
assertEquals("[T,P] [n.T`1[][[G]],P].M([n.T`1[][[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G]],P] p");
}
@Test
public void MethodNameTest_8_7() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G]],P].M([n.T`1[][[G]],P] p1, [n.T`1[][[G]],P] p2)");
assertEquals("[T,P] [n.T`1[][[G]],P].M([n.T`1[][[G]],P] p1, [n.T`1[][[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G]],P] p1", "[n.T`1[][[G]],P] p2");
}
@Test
public void MethodNameTest_8_8() {
IMethodName sut = new MethodName("[n.T`1[][[G]],P] [T,P].M()");
assertEquals("[n.T`1[][[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_9() {
IMethodName sut = new MethodName("[n.T`1[][[G]],P] [T,P].M`1[[T]]()");
assertEquals("[n.T`1[][[G]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_10() {
IMethodName sut = new MethodName("[n.T`1[][[G]],P] [T,P].M`1[[T -> n.T`1[][[G]],P]]()");
assertEquals("[n.T`1[][[G]],P] [T,P].M`1[[T -> n.T`1[][[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[][[G]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_11() {
IMethodName sut = new MethodName("[n.T`1[][[G]],P] [T,P].M`2[[T],[U]]()");
assertEquals("[n.T`1[][[G]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_12() {
IMethodName sut = new MethodName("[n.T`1[][[G]],P] [T,P].M()");
assertEquals("[n.T`1[][[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_13() {
IMethodName sut = new MethodName("[n.T`1[][[G]],P] [T,P].M(out [?] p)");
assertEquals("[n.T`1[][[G]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_8_14() {
IMethodName sut = new MethodName("[n.T`1[][[G]],P] [T,P].M([n.T`1[][[G]],P] p)");
assertEquals("[n.T`1[][[G]],P] [T,P].M([n.T`1[][[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G]],P] p");
}
@Test
public void MethodNameTest_8_15() {
IMethodName sut = new MethodName("[n.T`1[][[G]],P] [T,P].M([n.T`1[][[G]],P] p1, [n.T`1[][[G]],P] p2)");
assertEquals("[n.T`1[][[G]],P] [T,P].M([n.T`1[][[G]],P] p1, [n.T`1[][[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G]],P] p1", "[n.T`1[][[G]],P] p2");
}
@Test
public void MethodNameTest_8_16() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G]],P].M()");
assertEquals("static [T,P] [n.T`1[][[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_17() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G]],P].M`1[[T]]()");
assertEquals("static [T,P] [n.T`1[][[G]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_18() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G]],P].M`1[[T -> n.T`1[][[G]],P]]()");
assertEquals("static [T,P] [n.T`1[][[G]],P].M`1[[T -> n.T`1[][[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[][[G]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_19() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G]],P].M`2[[T],[U]]()");
assertEquals("static [T,P] [n.T`1[][[G]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_20() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G]],P].M()");
assertEquals("static [T,P] [n.T`1[][[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_21() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G]],P].M(out [?] p)");
assertEquals("static [T,P] [n.T`1[][[G]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_8_22() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G]],P].M([n.T`1[][[G]],P] p)");
assertEquals("static [T,P] [n.T`1[][[G]],P].M([n.T`1[][[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G]],P] p");
}
@Test
public void MethodNameTest_8_23() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G]],P].M([n.T`1[][[G]],P] p1, [n.T`1[][[G]],P] p2)");
assertEquals("static [T,P] [n.T`1[][[G]],P].M([n.T`1[][[G]],P] p1, [n.T`1[][[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G]],P] p1", "[n.T`1[][[G]],P] p2");
}
@Test
public void MethodNameTest_8_24() {
IMethodName sut = new MethodName("static [n.T`1[][[G]],P] [T,P].M()");
assertEquals("static [n.T`1[][[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_25() {
IMethodName sut = new MethodName("static [n.T`1[][[G]],P] [T,P].M`1[[T]]()");
assertEquals("static [n.T`1[][[G]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_26() {
IMethodName sut = new MethodName("static [n.T`1[][[G]],P] [T,P].M`1[[T -> n.T`1[][[G]],P]]()");
assertEquals("static [n.T`1[][[G]],P] [T,P].M`1[[T -> n.T`1[][[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[][[G]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_27() {
IMethodName sut = new MethodName("static [n.T`1[][[G]],P] [T,P].M`2[[T],[U]]()");
assertEquals("static [n.T`1[][[G]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_28() {
IMethodName sut = new MethodName("static [n.T`1[][[G]],P] [T,P].M()");
assertEquals("static [n.T`1[][[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_8_29() {
IMethodName sut = new MethodName("static [n.T`1[][[G]],P] [T,P].M(out [?] p)");
assertEquals("static [n.T`1[][[G]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_8_30() {
IMethodName sut = new MethodName("static [n.T`1[][[G]],P] [T,P].M([n.T`1[][[G]],P] p)");
assertEquals("static [n.T`1[][[G]],P] [T,P].M([n.T`1[][[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G]],P] p");
}
@Test
public void MethodNameTest_8_31() {
IMethodName sut = new MethodName("static [n.T`1[][[G]],P] [T,P].M([n.T`1[][[G]],P] p1, [n.T`1[][[G]],P] p2)");
assertEquals("static [n.T`1[][[G]],P] [T,P].M([n.T`1[][[G]],P] p1, [n.T`1[][[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G]],P] p1", "[n.T`1[][[G]],P] p2");
}
@Test
public void PropertyNameTest_8_0() {
IPropertyName sut = new PropertyName("[T,P] [n.T`1[][[G]],P].P()");
assertEquals("[T,P] [n.T`1[][[G]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_8_1() {
IPropertyName sut = new PropertyName("[n.T`1[][[G]],P] [T,P].P()");
assertEquals("[n.T`1[][[G]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_8_2() {
IPropertyName sut = new PropertyName("static [T,P] [n.T`1[][[G]],P].P()");
assertEquals("static [T,P] [n.T`1[][[G]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_8_3() {
IPropertyName sut = new PropertyName("static [n.T`1[][[G]],P] [T,P].P()");
assertEquals("static [n.T`1[][[G]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_9_0() {
IFieldName sut = new FieldName("[T,P] [n.T`1[,][[G]],P]._f");
assertEquals("[T,P] [n.T`1[,][[G]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_9_1() {
IFieldName sut = new FieldName("[n.T`1[,][[G]],P] [T,P]._f");
assertEquals("[n.T`1[,][[G]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
}
@Test
public void FieldNameTest_9_2() {
IFieldName sut = new FieldName("static [T,P] [n.T`1[,][[G]],P]._f");
assertEquals("static [T,P] [n.T`1[,][[G]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_9_3() {
IFieldName sut = new FieldName("static [n.T`1[,][[G]],P] [T,P]._f");
assertEquals("static [n.T`1[,][[G]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
}
@Test
public void EventNameTest_9_0() {
IEventName sut = new EventName("[T,P] [n.T`1[,][[G]],P].e");
assertEquals("[T,P] [n.T`1[,][[G]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_9_1() {
IEventName sut = new EventName("[n.T`1[,][[G]],P] [T,P].e");
assertEquals("[n.T`1[,][[G]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getHandlerType());
}
@Test
public void EventNameTest_9_2() {
IEventName sut = new EventName("static [T,P] [n.T`1[,][[G]],P].e");
assertEquals("static [T,P] [n.T`1[,][[G]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_9_3() {
IEventName sut = new EventName("static [n.T`1[,][[G]],P] [T,P].e");
assertEquals("static [n.T`1[,][[G]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_9_0() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G]],P].M()");
assertEquals("[T,P] [n.T`1[,][[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_1() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G]],P].M`1[[T]]()");
assertEquals("[T,P] [n.T`1[,][[G]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_2() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G]],P].M`1[[T -> n.T`1[,][[G]],P]]()");
assertEquals("[T,P] [n.T`1[,][[G]],P].M`1[[T -> n.T`1[,][[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[,][[G]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_3() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G]],P].M`2[[T],[U]]()");
assertEquals("[T,P] [n.T`1[,][[G]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_4() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G]],P].M()");
assertEquals("[T,P] [n.T`1[,][[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_5() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G]],P].M(out [?] p)");
assertEquals("[T,P] [n.T`1[,][[G]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_9_6() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G]],P].M([n.T`1[,][[G]],P] p)");
assertEquals("[T,P] [n.T`1[,][[G]],P].M([n.T`1[,][[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G]],P] p");
}
@Test
public void MethodNameTest_9_7() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G]],P].M([n.T`1[,][[G]],P] p1, [n.T`1[,][[G]],P] p2)");
assertEquals("[T,P] [n.T`1[,][[G]],P].M([n.T`1[,][[G]],P] p1, [n.T`1[,][[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G]],P] p1", "[n.T`1[,][[G]],P] p2");
}
@Test
public void MethodNameTest_9_8() {
IMethodName sut = new MethodName("[n.T`1[,][[G]],P] [T,P].M()");
assertEquals("[n.T`1[,][[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_9() {
IMethodName sut = new MethodName("[n.T`1[,][[G]],P] [T,P].M`1[[T]]()");
assertEquals("[n.T`1[,][[G]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_10() {
IMethodName sut = new MethodName("[n.T`1[,][[G]],P] [T,P].M`1[[T -> n.T`1[,][[G]],P]]()");
assertEquals("[n.T`1[,][[G]],P] [T,P].M`1[[T -> n.T`1[,][[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[,][[G]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_11() {
IMethodName sut = new MethodName("[n.T`1[,][[G]],P] [T,P].M`2[[T],[U]]()");
assertEquals("[n.T`1[,][[G]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_12() {
IMethodName sut = new MethodName("[n.T`1[,][[G]],P] [T,P].M()");
assertEquals("[n.T`1[,][[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_13() {
IMethodName sut = new MethodName("[n.T`1[,][[G]],P] [T,P].M(out [?] p)");
assertEquals("[n.T`1[,][[G]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_9_14() {
IMethodName sut = new MethodName("[n.T`1[,][[G]],P] [T,P].M([n.T`1[,][[G]],P] p)");
assertEquals("[n.T`1[,][[G]],P] [T,P].M([n.T`1[,][[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G]],P] p");
}
@Test
public void MethodNameTest_9_15() {
IMethodName sut = new MethodName("[n.T`1[,][[G]],P] [T,P].M([n.T`1[,][[G]],P] p1, [n.T`1[,][[G]],P] p2)");
assertEquals("[n.T`1[,][[G]],P] [T,P].M([n.T`1[,][[G]],P] p1, [n.T`1[,][[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G]],P] p1", "[n.T`1[,][[G]],P] p2");
}
@Test
public void MethodNameTest_9_16() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G]],P].M()");
assertEquals("static [T,P] [n.T`1[,][[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_17() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G]],P].M`1[[T]]()");
assertEquals("static [T,P] [n.T`1[,][[G]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_18() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G]],P].M`1[[T -> n.T`1[,][[G]],P]]()");
assertEquals("static [T,P] [n.T`1[,][[G]],P].M`1[[T -> n.T`1[,][[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[,][[G]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_19() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G]],P].M`2[[T],[U]]()");
assertEquals("static [T,P] [n.T`1[,][[G]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_20() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G]],P].M()");
assertEquals("static [T,P] [n.T`1[,][[G]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_21() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G]],P].M(out [?] p)");
assertEquals("static [T,P] [n.T`1[,][[G]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_9_22() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G]],P].M([n.T`1[,][[G]],P] p)");
assertEquals("static [T,P] [n.T`1[,][[G]],P].M([n.T`1[,][[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G]],P] p");
}
@Test
public void MethodNameTest_9_23() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G]],P].M([n.T`1[,][[G]],P] p1, [n.T`1[,][[G]],P] p2)");
assertEquals("static [T,P] [n.T`1[,][[G]],P].M([n.T`1[,][[G]],P] p1, [n.T`1[,][[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G]],P] p1", "[n.T`1[,][[G]],P] p2");
}
@Test
public void MethodNameTest_9_24() {
IMethodName sut = new MethodName("static [n.T`1[,][[G]],P] [T,P].M()");
assertEquals("static [n.T`1[,][[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_25() {
IMethodName sut = new MethodName("static [n.T`1[,][[G]],P] [T,P].M`1[[T]]()");
assertEquals("static [n.T`1[,][[G]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_26() {
IMethodName sut = new MethodName("static [n.T`1[,][[G]],P] [T,P].M`1[[T -> n.T`1[,][[G]],P]]()");
assertEquals("static [n.T`1[,][[G]],P] [T,P].M`1[[T -> n.T`1[,][[G]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[,][[G]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_27() {
IMethodName sut = new MethodName("static [n.T`1[,][[G]],P] [T,P].M`2[[T],[U]]()");
assertEquals("static [n.T`1[,][[G]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_28() {
IMethodName sut = new MethodName("static [n.T`1[,][[G]],P] [T,P].M()");
assertEquals("static [n.T`1[,][[G]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_9_29() {
IMethodName sut = new MethodName("static [n.T`1[,][[G]],P] [T,P].M(out [?] p)");
assertEquals("static [n.T`1[,][[G]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_9_30() {
IMethodName sut = new MethodName("static [n.T`1[,][[G]],P] [T,P].M([n.T`1[,][[G]],P] p)");
assertEquals("static [n.T`1[,][[G]],P] [T,P].M([n.T`1[,][[G]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G]],P] p");
}
@Test
public void MethodNameTest_9_31() {
IMethodName sut = new MethodName("static [n.T`1[,][[G]],P] [T,P].M([n.T`1[,][[G]],P] p1, [n.T`1[,][[G]],P] p2)");
assertEquals("static [n.T`1[,][[G]],P] [T,P].M([n.T`1[,][[G]],P] p1, [n.T`1[,][[G]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G]],P] p1", "[n.T`1[,][[G]],P] p2");
}
@Test
public void PropertyNameTest_9_0() {
IPropertyName sut = new PropertyName("[T,P] [n.T`1[,][[G]],P].P()");
assertEquals("[T,P] [n.T`1[,][[G]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_9_1() {
IPropertyName sut = new PropertyName("[n.T`1[,][[G]],P] [T,P].P()");
assertEquals("[n.T`1[,][[G]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_9_2() {
IPropertyName sut = new PropertyName("static [T,P] [n.T`1[,][[G]],P].P()");
assertEquals("static [T,P] [n.T`1[,][[G]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_9_3() {
IPropertyName sut = new PropertyName("static [n.T`1[,][[G]],P] [T,P].P()");
assertEquals("static [n.T`1[,][[G]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_10_0() {
IFieldName sut = new FieldName("[T,P] [n.T`1[[G -> p:byte]],P]._f");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_10_1() {
IFieldName sut = new FieldName("[n.T`1[[G -> p:byte]],P] [T,P]._f");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
}
@Test
public void FieldNameTest_10_2() {
IFieldName sut = new FieldName("static [T,P] [n.T`1[[G -> p:byte]],P]._f");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_10_3() {
IFieldName sut = new FieldName("static [n.T`1[[G -> p:byte]],P] [T,P]._f");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
}
@Test
public void EventNameTest_10_0() {
IEventName sut = new EventName("[T,P] [n.T`1[[G -> p:byte]],P].e");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_10_1() {
IEventName sut = new EventName("[n.T`1[[G -> p:byte]],P] [T,P].e");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getHandlerType());
}
@Test
public void EventNameTest_10_2() {
IEventName sut = new EventName("static [T,P] [n.T`1[[G -> p:byte]],P].e");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_10_3() {
IEventName sut = new EventName("static [n.T`1[[G -> p:byte]],P] [T,P].e");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_10_0() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G -> p:byte]],P].M()");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_1() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G -> p:byte]],P].M`1[[T]]()");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_2() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G -> p:byte]],P].M`1[[T -> n.T`1[[G -> p:byte]],P]]()");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].M`1[[T -> n.T`1[[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[[G -> p:byte]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_3() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G -> p:byte]],P].M`2[[T],[U]]()");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_4() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G -> p:byte]],P].M()");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_5() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G -> p:byte]],P].M(out [?] p)");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_10_6() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G -> p:byte]],P].M([n.T`1[[G -> p:byte]],P] p)");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].M([n.T`1[[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_10_7() {
IMethodName sut = new MethodName("[T,P] [n.T`1[[G -> p:byte]],P].M([n.T`1[[G -> p:byte]],P] p1, [n.T`1[[G -> p:byte]],P] p2)");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].M([n.T`1[[G -> p:byte]],P] p1, [n.T`1[[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G -> p:byte]],P] p1", "[n.T`1[[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_10_8() {
IMethodName sut = new MethodName("[n.T`1[[G -> p:byte]],P] [T,P].M()");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_9() {
IMethodName sut = new MethodName("[n.T`1[[G -> p:byte]],P] [T,P].M`1[[T]]()");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_10() {
IMethodName sut = new MethodName("[n.T`1[[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[[G -> p:byte]],P]]()");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[[G -> p:byte]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_11() {
IMethodName sut = new MethodName("[n.T`1[[G -> p:byte]],P] [T,P].M`2[[T],[U]]()");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_12() {
IMethodName sut = new MethodName("[n.T`1[[G -> p:byte]],P] [T,P].M()");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_13() {
IMethodName sut = new MethodName("[n.T`1[[G -> p:byte]],P] [T,P].M(out [?] p)");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_10_14() {
IMethodName sut = new MethodName("[n.T`1[[G -> p:byte]],P] [T,P].M([n.T`1[[G -> p:byte]],P] p)");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].M([n.T`1[[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_10_15() {
IMethodName sut = new MethodName("[n.T`1[[G -> p:byte]],P] [T,P].M([n.T`1[[G -> p:byte]],P] p1, [n.T`1[[G -> p:byte]],P] p2)");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].M([n.T`1[[G -> p:byte]],P] p1, [n.T`1[[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G -> p:byte]],P] p1", "[n.T`1[[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_10_16() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G -> p:byte]],P].M()");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_17() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G -> p:byte]],P].M`1[[T]]()");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_18() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G -> p:byte]],P].M`1[[T -> n.T`1[[G -> p:byte]],P]]()");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].M`1[[T -> n.T`1[[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[[G -> p:byte]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_19() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G -> p:byte]],P].M`2[[T],[U]]()");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_20() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G -> p:byte]],P].M()");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_21() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G -> p:byte]],P].M(out [?] p)");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_10_22() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G -> p:byte]],P].M([n.T`1[[G -> p:byte]],P] p)");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].M([n.T`1[[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_10_23() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[[G -> p:byte]],P].M([n.T`1[[G -> p:byte]],P] p1, [n.T`1[[G -> p:byte]],P] p2)");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].M([n.T`1[[G -> p:byte]],P] p1, [n.T`1[[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G -> p:byte]],P] p1", "[n.T`1[[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_10_24() {
IMethodName sut = new MethodName("static [n.T`1[[G -> p:byte]],P] [T,P].M()");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_25() {
IMethodName sut = new MethodName("static [n.T`1[[G -> p:byte]],P] [T,P].M`1[[T]]()");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_26() {
IMethodName sut = new MethodName("static [n.T`1[[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[[G -> p:byte]],P]]()");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[[G -> p:byte]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_27() {
IMethodName sut = new MethodName("static [n.T`1[[G -> p:byte]],P] [T,P].M`2[[T],[U]]()");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_28() {
IMethodName sut = new MethodName("static [n.T`1[[G -> p:byte]],P] [T,P].M()");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_10_29() {
IMethodName sut = new MethodName("static [n.T`1[[G -> p:byte]],P] [T,P].M(out [?] p)");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_10_30() {
IMethodName sut = new MethodName("static [n.T`1[[G -> p:byte]],P] [T,P].M([n.T`1[[G -> p:byte]],P] p)");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].M([n.T`1[[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_10_31() {
IMethodName sut = new MethodName("static [n.T`1[[G -> p:byte]],P] [T,P].M([n.T`1[[G -> p:byte]],P] p1, [n.T`1[[G -> p:byte]],P] p2)");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].M([n.T`1[[G -> p:byte]],P] p1, [n.T`1[[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[[G -> p:byte]],P] p1", "[n.T`1[[G -> p:byte]],P] p2");
}
@Test
public void PropertyNameTest_10_0() {
IPropertyName sut = new PropertyName("[T,P] [n.T`1[[G -> p:byte]],P].P()");
assertEquals("[T,P] [n.T`1[[G -> p:byte]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_10_1() {
IPropertyName sut = new PropertyName("[n.T`1[[G -> p:byte]],P] [T,P].P()");
assertEquals("[n.T`1[[G -> p:byte]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_10_2() {
IPropertyName sut = new PropertyName("static [T,P] [n.T`1[[G -> p:byte]],P].P()");
assertEquals("static [T,P] [n.T`1[[G -> p:byte]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_10_3() {
IPropertyName sut = new PropertyName("static [n.T`1[[G -> p:byte]],P] [T,P].P()");
assertEquals("static [n.T`1[[G -> p:byte]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("n.T`1[[G -> p:byte]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_11_0() {
IFieldName sut = new FieldName("[T,P] [n.T`1[][[G -> p:byte]],P]._f");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_11_1() {
IFieldName sut = new FieldName("[n.T`1[][[G -> p:byte]],P] [T,P]._f");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
}
@Test
public void FieldNameTest_11_2() {
IFieldName sut = new FieldName("static [T,P] [n.T`1[][[G -> p:byte]],P]._f");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_11_3() {
IFieldName sut = new FieldName("static [n.T`1[][[G -> p:byte]],P] [T,P]._f");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
}
@Test
public void EventNameTest_11_0() {
IEventName sut = new EventName("[T,P] [n.T`1[][[G -> p:byte]],P].e");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_11_1() {
IEventName sut = new EventName("[n.T`1[][[G -> p:byte]],P] [T,P].e");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getHandlerType());
}
@Test
public void EventNameTest_11_2() {
IEventName sut = new EventName("static [T,P] [n.T`1[][[G -> p:byte]],P].e");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_11_3() {
IEventName sut = new EventName("static [n.T`1[][[G -> p:byte]],P] [T,P].e");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_11_0() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G -> p:byte]],P].M()");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_1() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G -> p:byte]],P].M`1[[T]]()");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_2() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G -> p:byte]],P].M`1[[T -> n.T`1[][[G -> p:byte]],P]]()");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].M`1[[T -> n.T`1[][[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[][[G -> p:byte]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_3() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G -> p:byte]],P].M`2[[T],[U]]()");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_4() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G -> p:byte]],P].M()");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_5() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G -> p:byte]],P].M(out [?] p)");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_11_6() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G -> p:byte]],P].M([n.T`1[][[G -> p:byte]],P] p)");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].M([n.T`1[][[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_11_7() {
IMethodName sut = new MethodName("[T,P] [n.T`1[][[G -> p:byte]],P].M([n.T`1[][[G -> p:byte]],P] p1, [n.T`1[][[G -> p:byte]],P] p2)");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].M([n.T`1[][[G -> p:byte]],P] p1, [n.T`1[][[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G -> p:byte]],P] p1", "[n.T`1[][[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_11_8() {
IMethodName sut = new MethodName("[n.T`1[][[G -> p:byte]],P] [T,P].M()");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_9() {
IMethodName sut = new MethodName("[n.T`1[][[G -> p:byte]],P] [T,P].M`1[[T]]()");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_10() {
IMethodName sut = new MethodName("[n.T`1[][[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[][[G -> p:byte]],P]]()");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[][[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[][[G -> p:byte]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_11() {
IMethodName sut = new MethodName("[n.T`1[][[G -> p:byte]],P] [T,P].M`2[[T],[U]]()");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_12() {
IMethodName sut = new MethodName("[n.T`1[][[G -> p:byte]],P] [T,P].M()");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_13() {
IMethodName sut = new MethodName("[n.T`1[][[G -> p:byte]],P] [T,P].M(out [?] p)");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_11_14() {
IMethodName sut = new MethodName("[n.T`1[][[G -> p:byte]],P] [T,P].M([n.T`1[][[G -> p:byte]],P] p)");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].M([n.T`1[][[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_11_15() {
IMethodName sut = new MethodName("[n.T`1[][[G -> p:byte]],P] [T,P].M([n.T`1[][[G -> p:byte]],P] p1, [n.T`1[][[G -> p:byte]],P] p2)");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].M([n.T`1[][[G -> p:byte]],P] p1, [n.T`1[][[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G -> p:byte]],P] p1", "[n.T`1[][[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_11_16() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G -> p:byte]],P].M()");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_17() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G -> p:byte]],P].M`1[[T]]()");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_18() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G -> p:byte]],P].M`1[[T -> n.T`1[][[G -> p:byte]],P]]()");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].M`1[[T -> n.T`1[][[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[][[G -> p:byte]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_19() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G -> p:byte]],P].M`2[[T],[U]]()");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_20() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G -> p:byte]],P].M()");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_21() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G -> p:byte]],P].M(out [?] p)");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_11_22() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G -> p:byte]],P].M([n.T`1[][[G -> p:byte]],P] p)");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].M([n.T`1[][[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_11_23() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[][[G -> p:byte]],P].M([n.T`1[][[G -> p:byte]],P] p1, [n.T`1[][[G -> p:byte]],P] p2)");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].M([n.T`1[][[G -> p:byte]],P] p1, [n.T`1[][[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G -> p:byte]],P] p1", "[n.T`1[][[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_11_24() {
IMethodName sut = new MethodName("static [n.T`1[][[G -> p:byte]],P] [T,P].M()");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_25() {
IMethodName sut = new MethodName("static [n.T`1[][[G -> p:byte]],P] [T,P].M`1[[T]]()");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_26() {
IMethodName sut = new MethodName("static [n.T`1[][[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[][[G -> p:byte]],P]]()");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[][[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[][[G -> p:byte]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_27() {
IMethodName sut = new MethodName("static [n.T`1[][[G -> p:byte]],P] [T,P].M`2[[T],[U]]()");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_28() {
IMethodName sut = new MethodName("static [n.T`1[][[G -> p:byte]],P] [T,P].M()");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_11_29() {
IMethodName sut = new MethodName("static [n.T`1[][[G -> p:byte]],P] [T,P].M(out [?] p)");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_11_30() {
IMethodName sut = new MethodName("static [n.T`1[][[G -> p:byte]],P] [T,P].M([n.T`1[][[G -> p:byte]],P] p)");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].M([n.T`1[][[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_11_31() {
IMethodName sut = new MethodName("static [n.T`1[][[G -> p:byte]],P] [T,P].M([n.T`1[][[G -> p:byte]],P] p1, [n.T`1[][[G -> p:byte]],P] p2)");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].M([n.T`1[][[G -> p:byte]],P] p1, [n.T`1[][[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[][[G -> p:byte]],P] p1", "[n.T`1[][[G -> p:byte]],P] p2");
}
@Test
public void PropertyNameTest_11_0() {
IPropertyName sut = new PropertyName("[T,P] [n.T`1[][[G -> p:byte]],P].P()");
assertEquals("[T,P] [n.T`1[][[G -> p:byte]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_11_1() {
IPropertyName sut = new PropertyName("[n.T`1[][[G -> p:byte]],P] [T,P].P()");
assertEquals("[n.T`1[][[G -> p:byte]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_11_2() {
IPropertyName sut = new PropertyName("static [T,P] [n.T`1[][[G -> p:byte]],P].P()");
assertEquals("static [T,P] [n.T`1[][[G -> p:byte]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_11_3() {
IPropertyName sut = new PropertyName("static [n.T`1[][[G -> p:byte]],P] [T,P].P()");
assertEquals("static [n.T`1[][[G -> p:byte]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[][[G -> p:byte]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_12_0() {
IFieldName sut = new FieldName("[T,P] [n.T`1[,][[G -> p:byte]],P]._f");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_12_1() {
IFieldName sut = new FieldName("[n.T`1[,][[G -> p:byte]],P] [T,P]._f");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
}
@Test
public void FieldNameTest_12_2() {
IFieldName sut = new FieldName("static [T,P] [n.T`1[,][[G -> p:byte]],P]._f");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_12_3() {
IFieldName sut = new FieldName("static [n.T`1[,][[G -> p:byte]],P] [T,P]._f");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
}
@Test
public void EventNameTest_12_0() {
IEventName sut = new EventName("[T,P] [n.T`1[,][[G -> p:byte]],P].e");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_12_1() {
IEventName sut = new EventName("[n.T`1[,][[G -> p:byte]],P] [T,P].e");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getHandlerType());
}
@Test
public void EventNameTest_12_2() {
IEventName sut = new EventName("static [T,P] [n.T`1[,][[G -> p:byte]],P].e");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_12_3() {
IEventName sut = new EventName("static [n.T`1[,][[G -> p:byte]],P] [T,P].e");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getHandlerType());
}
@Test
public void MethodNameTest_12_0() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G -> p:byte]],P].M()");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_1() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G -> p:byte]],P].M`1[[T]]()");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_2() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G -> p:byte]],P].M`1[[T -> n.T`1[,][[G -> p:byte]],P]]()");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].M`1[[T -> n.T`1[,][[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[,][[G -> p:byte]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_3() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G -> p:byte]],P].M`2[[T],[U]]()");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_4() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G -> p:byte]],P].M()");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_5() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G -> p:byte]],P].M(out [?] p)");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_12_6() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G -> p:byte]],P].M([n.T`1[,][[G -> p:byte]],P] p)");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].M([n.T`1[,][[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_12_7() {
IMethodName sut = new MethodName("[T,P] [n.T`1[,][[G -> p:byte]],P].M([n.T`1[,][[G -> p:byte]],P] p1, [n.T`1[,][[G -> p:byte]],P] p2)");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].M([n.T`1[,][[G -> p:byte]],P] p1, [n.T`1[,][[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G -> p:byte]],P] p1", "[n.T`1[,][[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_12_8() {
IMethodName sut = new MethodName("[n.T`1[,][[G -> p:byte]],P] [T,P].M()");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_9() {
IMethodName sut = new MethodName("[n.T`1[,][[G -> p:byte]],P] [T,P].M`1[[T]]()");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_10() {
IMethodName sut = new MethodName("[n.T`1[,][[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[,][[G -> p:byte]],P]]()");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[,][[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[,][[G -> p:byte]],P]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_11() {
IMethodName sut = new MethodName("[n.T`1[,][[G -> p:byte]],P] [T,P].M`2[[T],[U]]()");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_12() {
IMethodName sut = new MethodName("[n.T`1[,][[G -> p:byte]],P] [T,P].M()");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_13() {
IMethodName sut = new MethodName("[n.T`1[,][[G -> p:byte]],P] [T,P].M(out [?] p)");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_12_14() {
IMethodName sut = new MethodName("[n.T`1[,][[G -> p:byte]],P] [T,P].M([n.T`1[,][[G -> p:byte]],P] p)");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].M([n.T`1[,][[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_12_15() {
IMethodName sut = new MethodName("[n.T`1[,][[G -> p:byte]],P] [T,P].M([n.T`1[,][[G -> p:byte]],P] p1, [n.T`1[,][[G -> p:byte]],P] p2)");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].M([n.T`1[,][[G -> p:byte]],P] p1, [n.T`1[,][[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G -> p:byte]],P] p1", "[n.T`1[,][[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_12_16() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G -> p:byte]],P].M()");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_17() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G -> p:byte]],P].M`1[[T]]()");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_18() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G -> p:byte]],P].M`1[[T -> n.T`1[,][[G -> p:byte]],P]]()");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].M`1[[T -> n.T`1[,][[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[,][[G -> p:byte]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_19() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G -> p:byte]],P].M`2[[T],[U]]()");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_20() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G -> p:byte]],P].M()");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_21() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G -> p:byte]],P].M(out [?] p)");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_12_22() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G -> p:byte]],P].M([n.T`1[,][[G -> p:byte]],P] p)");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].M([n.T`1[,][[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_12_23() {
IMethodName sut = new MethodName("static [T,P] [n.T`1[,][[G -> p:byte]],P].M([n.T`1[,][[G -> p:byte]],P] p1, [n.T`1[,][[G -> p:byte]],P] p2)");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].M([n.T`1[,][[G -> p:byte]],P] p1, [n.T`1[,][[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G -> p:byte]],P] p1", "[n.T`1[,][[G -> p:byte]],P] p2");
}
@Test
public void MethodNameTest_12_24() {
IMethodName sut = new MethodName("static [n.T`1[,][[G -> p:byte]],P] [T,P].M()");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_25() {
IMethodName sut = new MethodName("static [n.T`1[,][[G -> p:byte]],P] [T,P].M`1[[T]]()");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_26() {
IMethodName sut = new MethodName("static [n.T`1[,][[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[,][[G -> p:byte]],P]]()");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].M`1[[T -> n.T`1[,][[G -> p:byte]],P]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> n.T`1[,][[G -> p:byte]],P]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_27() {
IMethodName sut = new MethodName("static [n.T`1[,][[G -> p:byte]],P] [T,P].M`2[[T],[U]]()");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_28() {
IMethodName sut = new MethodName("static [n.T`1[,][[G -> p:byte]],P] [T,P].M()");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_12_29() {
IMethodName sut = new MethodName("static [n.T`1[,][[G -> p:byte]],P] [T,P].M(out [?] p)");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_12_30() {
IMethodName sut = new MethodName("static [n.T`1[,][[G -> p:byte]],P] [T,P].M([n.T`1[,][[G -> p:byte]],P] p)");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].M([n.T`1[,][[G -> p:byte]],P] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G -> p:byte]],P] p");
}
@Test
public void MethodNameTest_12_31() {
IMethodName sut = new MethodName("static [n.T`1[,][[G -> p:byte]],P] [T,P].M([n.T`1[,][[G -> p:byte]],P] p1, [n.T`1[,][[G -> p:byte]],P] p2)");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].M([n.T`1[,][[G -> p:byte]],P] p1, [n.T`1[,][[G -> p:byte]],P] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[n.T`1[,][[G -> p:byte]],P] p1", "[n.T`1[,][[G -> p:byte]],P] p2");
}
@Test
public void PropertyNameTest_12_0() {
IPropertyName sut = new PropertyName("[T,P] [n.T`1[,][[G -> p:byte]],P].P()");
assertEquals("[T,P] [n.T`1[,][[G -> p:byte]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_12_1() {
IPropertyName sut = new PropertyName("[n.T`1[,][[G -> p:byte]],P] [T,P].P()");
assertEquals("[n.T`1[,][[G -> p:byte]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_12_2() {
IPropertyName sut = new PropertyName("static [T,P] [n.T`1[,][[G -> p:byte]],P].P()");
assertEquals("static [T,P] [n.T`1[,][[G -> p:byte]],P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_12_3() {
IPropertyName sut = new PropertyName("static [n.T`1[,][[G -> p:byte]],P] [T,P].P()");
assertEquals("static [n.T`1[,][[G -> p:byte]],P] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("n.T`1[,][[G -> p:byte]],P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_13_0() {
IFieldName sut = new FieldName("[T,P] [T]._f");
assertEquals("[T,P] [T]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_13_1() {
IFieldName sut = new FieldName("[T] [T,P]._f");
assertEquals("[T] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
}
@Test
public void FieldNameTest_13_2() {
IFieldName sut = new FieldName("static [T,P] [T]._f");
assertEquals("static [T,P] [T]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_13_3() {
IFieldName sut = new FieldName("static [T] [T,P]._f");
assertEquals("static [T] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
}
@Test
public void EventNameTest_13_0() {
IEventName sut = new EventName("[T,P] [T].e");
assertEquals("[T,P] [T].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_13_1() {
IEventName sut = new EventName("[T] [T,P].e");
assertEquals("[T] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getHandlerType());
}
@Test
public void EventNameTest_13_2() {
IEventName sut = new EventName("static [T,P] [T].e");
assertEquals("static [T,P] [T].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_13_3() {
IEventName sut = new EventName("static [T] [T,P].e");
assertEquals("static [T] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getHandlerType());
}
@Test
public void MethodNameTest_13_0() {
IMethodName sut = new MethodName("[T,P] [T].M()");
assertEquals("[T,P] [T].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_1() {
IMethodName sut = new MethodName("[T,P] [T].M`1[[T]]()");
assertEquals("[T,P] [T].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_2() {
IMethodName sut = new MethodName("[T,P] [T].M`1[[T -> T]]()");
assertEquals("[T,P] [T].M`1[[T -> T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M`1[[T -> T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_3() {
IMethodName sut = new MethodName("[T,P] [T].M`2[[T],[U]]()");
assertEquals("[T,P] [T].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_4() {
IMethodName sut = new MethodName("[T,P] [T].M()");
assertEquals("[T,P] [T].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_5() {
IMethodName sut = new MethodName("[T,P] [T].M(out [?] p)");
assertEquals("[T,P] [T].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_13_6() {
IMethodName sut = new MethodName("[T,P] [T].M([T] p)");
assertEquals("[T,P] [T].M([T] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T] p");
}
@Test
public void MethodNameTest_13_7() {
IMethodName sut = new MethodName("[T,P] [T].M([T] p1, [T] p2)");
assertEquals("[T,P] [T].M([T] p1, [T] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T] p1", "[T] p2");
}
@Test
public void MethodNameTest_13_8() {
IMethodName sut = new MethodName("[T] [T,P].M()");
assertEquals("[T] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_9() {
IMethodName sut = new MethodName("[T] [T,P].M`1[[T]]()");
assertEquals("[T] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_10() {
IMethodName sut = new MethodName("[T] [T,P].M`1[[T -> T]]()");
assertEquals("[T] [T,P].M`1[[T -> T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_11() {
IMethodName sut = new MethodName("[T] [T,P].M`2[[T],[U]]()");
assertEquals("[T] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_12() {
IMethodName sut = new MethodName("[T] [T,P].M()");
assertEquals("[T] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_13() {
IMethodName sut = new MethodName("[T] [T,P].M(out [?] p)");
assertEquals("[T] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_13_14() {
IMethodName sut = new MethodName("[T] [T,P].M([T] p)");
assertEquals("[T] [T,P].M([T] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T] p");
}
@Test
public void MethodNameTest_13_15() {
IMethodName sut = new MethodName("[T] [T,P].M([T] p1, [T] p2)");
assertEquals("[T] [T,P].M([T] p1, [T] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T] p1", "[T] p2");
}
@Test
public void MethodNameTest_13_16() {
IMethodName sut = new MethodName("static [T,P] [T].M()");
assertEquals("static [T,P] [T].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_17() {
IMethodName sut = new MethodName("static [T,P] [T].M`1[[T]]()");
assertEquals("static [T,P] [T].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_18() {
IMethodName sut = new MethodName("static [T,P] [T].M`1[[T -> T]]()");
assertEquals("static [T,P] [T].M`1[[T -> T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M`1[[T -> T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_19() {
IMethodName sut = new MethodName("static [T,P] [T].M`2[[T],[U]]()");
assertEquals("static [T,P] [T].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_20() {
IMethodName sut = new MethodName("static [T,P] [T].M()");
assertEquals("static [T,P] [T].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_21() {
IMethodName sut = new MethodName("static [T,P] [T].M(out [?] p)");
assertEquals("static [T,P] [T].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_13_22() {
IMethodName sut = new MethodName("static [T,P] [T].M([T] p)");
assertEquals("static [T,P] [T].M([T] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T] p");
}
@Test
public void MethodNameTest_13_23() {
IMethodName sut = new MethodName("static [T,P] [T].M([T] p1, [T] p2)");
assertEquals("static [T,P] [T].M([T] p1, [T] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T] p1", "[T] p2");
}
@Test
public void MethodNameTest_13_24() {
IMethodName sut = new MethodName("static [T] [T,P].M()");
assertEquals("static [T] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_25() {
IMethodName sut = new MethodName("static [T] [T,P].M`1[[T]]()");
assertEquals("static [T] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_26() {
IMethodName sut = new MethodName("static [T] [T,P].M`1[[T -> T]]()");
assertEquals("static [T] [T,P].M`1[[T -> T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_27() {
IMethodName sut = new MethodName("static [T] [T,P].M`2[[T],[U]]()");
assertEquals("static [T] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_28() {
IMethodName sut = new MethodName("static [T] [T,P].M()");
assertEquals("static [T] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_13_29() {
IMethodName sut = new MethodName("static [T] [T,P].M(out [?] p)");
assertEquals("static [T] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_13_30() {
IMethodName sut = new MethodName("static [T] [T,P].M([T] p)");
assertEquals("static [T] [T,P].M([T] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T] p");
}
@Test
public void MethodNameTest_13_31() {
IMethodName sut = new MethodName("static [T] [T,P].M([T] p1, [T] p2)");
assertEquals("static [T] [T,P].M([T] p1, [T] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertEquals(new TypeParameterName("T"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T] p1", "[T] p2");
}
@Test
public void PropertyNameTest_13_0() {
IPropertyName sut = new PropertyName("[T,P] [T].P()");
assertEquals("[T,P] [T].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_13_1() {
IPropertyName sut = new PropertyName("[T] [T,P].P()");
assertEquals("[T] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_13_2() {
IPropertyName sut = new PropertyName("static [T,P] [T].P()");
assertEquals("static [T,P] [T].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_13_3() {
IPropertyName sut = new PropertyName("static [T] [T,P].P()");
assertEquals("static [T] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeParameterName("T"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_14_0() {
IFieldName sut = new FieldName("[T,P] [T[]]._f");
assertEquals("[T,P] [T[]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_14_1() {
IFieldName sut = new FieldName("[T[]] [T,P]._f");
assertEquals("[T[]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
}
@Test
public void FieldNameTest_14_2() {
IFieldName sut = new FieldName("static [T,P] [T[]]._f");
assertEquals("static [T,P] [T[]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_14_3() {
IFieldName sut = new FieldName("static [T[]] [T,P]._f");
assertEquals("static [T[]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
}
@Test
public void EventNameTest_14_0() {
IEventName sut = new EventName("[T,P] [T[]].e");
assertEquals("[T,P] [T[]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_14_1() {
IEventName sut = new EventName("[T[]] [T,P].e");
assertEquals("[T[]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getHandlerType());
}
@Test
public void EventNameTest_14_2() {
IEventName sut = new EventName("static [T,P] [T[]].e");
assertEquals("static [T,P] [T[]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_14_3() {
IEventName sut = new EventName("static [T[]] [T,P].e");
assertEquals("static [T[]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getHandlerType());
}
@Test
public void MethodNameTest_14_0() {
IMethodName sut = new MethodName("[T,P] [T[]].M()");
assertEquals("[T,P] [T[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_1() {
IMethodName sut = new MethodName("[T,P] [T[]].M`1[[T]]()");
assertEquals("[T,P] [T[]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_2() {
IMethodName sut = new MethodName("[T,P] [T[]].M`1[[T -> T[]]]()");
assertEquals("[T,P] [T[]].M`1[[T -> T[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_3() {
IMethodName sut = new MethodName("[T,P] [T[]].M`2[[T],[U]]()");
assertEquals("[T,P] [T[]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_4() {
IMethodName sut = new MethodName("[T,P] [T[]].M()");
assertEquals("[T,P] [T[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_5() {
IMethodName sut = new MethodName("[T,P] [T[]].M(out [?] p)");
assertEquals("[T,P] [T[]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_14_6() {
IMethodName sut = new MethodName("[T,P] [T[]].M([T[]] p)");
assertEquals("[T,P] [T[]].M([T[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[]] p");
}
@Test
public void MethodNameTest_14_7() {
IMethodName sut = new MethodName("[T,P] [T[]].M([T[]] p1, [T[]] p2)");
assertEquals("[T,P] [T[]].M([T[]] p1, [T[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[]] p1", "[T[]] p2");
}
@Test
public void MethodNameTest_14_8() {
IMethodName sut = new MethodName("[T[]] [T,P].M()");
assertEquals("[T[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_9() {
IMethodName sut = new MethodName("[T[]] [T,P].M`1[[T]]()");
assertEquals("[T[]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_10() {
IMethodName sut = new MethodName("[T[]] [T,P].M`1[[T -> T[]]]()");
assertEquals("[T[]] [T,P].M`1[[T -> T[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_11() {
IMethodName sut = new MethodName("[T[]] [T,P].M`2[[T],[U]]()");
assertEquals("[T[]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_12() {
IMethodName sut = new MethodName("[T[]] [T,P].M()");
assertEquals("[T[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_13() {
IMethodName sut = new MethodName("[T[]] [T,P].M(out [?] p)");
assertEquals("[T[]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_14_14() {
IMethodName sut = new MethodName("[T[]] [T,P].M([T[]] p)");
assertEquals("[T[]] [T,P].M([T[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[]] p");
}
@Test
public void MethodNameTest_14_15() {
IMethodName sut = new MethodName("[T[]] [T,P].M([T[]] p1, [T[]] p2)");
assertEquals("[T[]] [T,P].M([T[]] p1, [T[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[]] p1", "[T[]] p2");
}
@Test
public void MethodNameTest_14_16() {
IMethodName sut = new MethodName("static [T,P] [T[]].M()");
assertEquals("static [T,P] [T[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_17() {
IMethodName sut = new MethodName("static [T,P] [T[]].M`1[[T]]()");
assertEquals("static [T,P] [T[]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_18() {
IMethodName sut = new MethodName("static [T,P] [T[]].M`1[[T -> T[]]]()");
assertEquals("static [T,P] [T[]].M`1[[T -> T[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_19() {
IMethodName sut = new MethodName("static [T,P] [T[]].M`2[[T],[U]]()");
assertEquals("static [T,P] [T[]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_20() {
IMethodName sut = new MethodName("static [T,P] [T[]].M()");
assertEquals("static [T,P] [T[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_21() {
IMethodName sut = new MethodName("static [T,P] [T[]].M(out [?] p)");
assertEquals("static [T,P] [T[]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_14_22() {
IMethodName sut = new MethodName("static [T,P] [T[]].M([T[]] p)");
assertEquals("static [T,P] [T[]].M([T[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[]] p");
}
@Test
public void MethodNameTest_14_23() {
IMethodName sut = new MethodName("static [T,P] [T[]].M([T[]] p1, [T[]] p2)");
assertEquals("static [T,P] [T[]].M([T[]] p1, [T[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[]] p1", "[T[]] p2");
}
@Test
public void MethodNameTest_14_24() {
IMethodName sut = new MethodName("static [T[]] [T,P].M()");
assertEquals("static [T[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_25() {
IMethodName sut = new MethodName("static [T[]] [T,P].M`1[[T]]()");
assertEquals("static [T[]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_26() {
IMethodName sut = new MethodName("static [T[]] [T,P].M`1[[T -> T[]]]()");
assertEquals("static [T[]] [T,P].M`1[[T -> T[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_27() {
IMethodName sut = new MethodName("static [T[]] [T,P].M`2[[T],[U]]()");
assertEquals("static [T[]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_28() {
IMethodName sut = new MethodName("static [T[]] [T,P].M()");
assertEquals("static [T[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_14_29() {
IMethodName sut = new MethodName("static [T[]] [T,P].M(out [?] p)");
assertEquals("static [T[]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_14_30() {
IMethodName sut = new MethodName("static [T[]] [T,P].M([T[]] p)");
assertEquals("static [T[]] [T,P].M([T[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[]] p");
}
@Test
public void MethodNameTest_14_31() {
IMethodName sut = new MethodName("static [T[]] [T,P].M([T[]] p1, [T[]] p2)");
assertEquals("static [T[]] [T,P].M([T[]] p1, [T[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertEquals(new TypeParameterName("T[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[]] p1", "[T[]] p2");
}
@Test
public void PropertyNameTest_14_0() {
IPropertyName sut = new PropertyName("[T,P] [T[]].P()");
assertEquals("[T,P] [T[]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_14_1() {
IPropertyName sut = new PropertyName("[T[]] [T,P].P()");
assertEquals("[T[]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_14_2() {
IPropertyName sut = new PropertyName("static [T,P] [T[]].P()");
assertEquals("static [T,P] [T[]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_14_3() {
IPropertyName sut = new PropertyName("static [T[]] [T,P].P()");
assertEquals("static [T[]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeParameterName("T[]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_15_0() {
IFieldName sut = new FieldName("[T,P] [T[,]]._f");
assertEquals("[T,P] [T[,]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_15_1() {
IFieldName sut = new FieldName("[T[,]] [T,P]._f");
assertEquals("[T[,]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
}
@Test
public void FieldNameTest_15_2() {
IFieldName sut = new FieldName("static [T,P] [T[,]]._f");
assertEquals("static [T,P] [T[,]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_15_3() {
IFieldName sut = new FieldName("static [T[,]] [T,P]._f");
assertEquals("static [T[,]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
}
@Test
public void EventNameTest_15_0() {
IEventName sut = new EventName("[T,P] [T[,]].e");
assertEquals("[T,P] [T[,]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_15_1() {
IEventName sut = new EventName("[T[,]] [T,P].e");
assertEquals("[T[,]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getHandlerType());
}
@Test
public void EventNameTest_15_2() {
IEventName sut = new EventName("static [T,P] [T[,]].e");
assertEquals("static [T,P] [T[,]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_15_3() {
IEventName sut = new EventName("static [T[,]] [T,P].e");
assertEquals("static [T[,]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getHandlerType());
}
@Test
public void MethodNameTest_15_0() {
IMethodName sut = new MethodName("[T,P] [T[,]].M()");
assertEquals("[T,P] [T[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_1() {
IMethodName sut = new MethodName("[T,P] [T[,]].M`1[[T]]()");
assertEquals("[T,P] [T[,]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_2() {
IMethodName sut = new MethodName("[T,P] [T[,]].M`1[[T -> T[,]]]()");
assertEquals("[T,P] [T[,]].M`1[[T -> T[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[,]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_3() {
IMethodName sut = new MethodName("[T,P] [T[,]].M`2[[T],[U]]()");
assertEquals("[T,P] [T[,]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_4() {
IMethodName sut = new MethodName("[T,P] [T[,]].M()");
assertEquals("[T,P] [T[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_5() {
IMethodName sut = new MethodName("[T,P] [T[,]].M(out [?] p)");
assertEquals("[T,P] [T[,]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_15_6() {
IMethodName sut = new MethodName("[T,P] [T[,]].M([T[,]] p)");
assertEquals("[T,P] [T[,]].M([T[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,]] p");
}
@Test
public void MethodNameTest_15_7() {
IMethodName sut = new MethodName("[T,P] [T[,]].M([T[,]] p1, [T[,]] p2)");
assertEquals("[T,P] [T[,]].M([T[,]] p1, [T[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,]] p1", "[T[,]] p2");
}
@Test
public void MethodNameTest_15_8() {
IMethodName sut = new MethodName("[T[,]] [T,P].M()");
assertEquals("[T[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_9() {
IMethodName sut = new MethodName("[T[,]] [T,P].M`1[[T]]()");
assertEquals("[T[,]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_10() {
IMethodName sut = new MethodName("[T[,]] [T,P].M`1[[T -> T[,]]]()");
assertEquals("[T[,]] [T,P].M`1[[T -> T[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[,]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_11() {
IMethodName sut = new MethodName("[T[,]] [T,P].M`2[[T],[U]]()");
assertEquals("[T[,]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_12() {
IMethodName sut = new MethodName("[T[,]] [T,P].M()");
assertEquals("[T[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_13() {
IMethodName sut = new MethodName("[T[,]] [T,P].M(out [?] p)");
assertEquals("[T[,]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_15_14() {
IMethodName sut = new MethodName("[T[,]] [T,P].M([T[,]] p)");
assertEquals("[T[,]] [T,P].M([T[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,]] p");
}
@Test
public void MethodNameTest_15_15() {
IMethodName sut = new MethodName("[T[,]] [T,P].M([T[,]] p1, [T[,]] p2)");
assertEquals("[T[,]] [T,P].M([T[,]] p1, [T[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,]] p1", "[T[,]] p2");
}
@Test
public void MethodNameTest_15_16() {
IMethodName sut = new MethodName("static [T,P] [T[,]].M()");
assertEquals("static [T,P] [T[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_17() {
IMethodName sut = new MethodName("static [T,P] [T[,]].M`1[[T]]()");
assertEquals("static [T,P] [T[,]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_18() {
IMethodName sut = new MethodName("static [T,P] [T[,]].M`1[[T -> T[,]]]()");
assertEquals("static [T,P] [T[,]].M`1[[T -> T[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[,]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_19() {
IMethodName sut = new MethodName("static [T,P] [T[,]].M`2[[T],[U]]()");
assertEquals("static [T,P] [T[,]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_20() {
IMethodName sut = new MethodName("static [T,P] [T[,]].M()");
assertEquals("static [T,P] [T[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_21() {
IMethodName sut = new MethodName("static [T,P] [T[,]].M(out [?] p)");
assertEquals("static [T,P] [T[,]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_15_22() {
IMethodName sut = new MethodName("static [T,P] [T[,]].M([T[,]] p)");
assertEquals("static [T,P] [T[,]].M([T[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,]] p");
}
@Test
public void MethodNameTest_15_23() {
IMethodName sut = new MethodName("static [T,P] [T[,]].M([T[,]] p1, [T[,]] p2)");
assertEquals("static [T,P] [T[,]].M([T[,]] p1, [T[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,]] p1", "[T[,]] p2");
}
@Test
public void MethodNameTest_15_24() {
IMethodName sut = new MethodName("static [T[,]] [T,P].M()");
assertEquals("static [T[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_25() {
IMethodName sut = new MethodName("static [T[,]] [T,P].M`1[[T]]()");
assertEquals("static [T[,]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_26() {
IMethodName sut = new MethodName("static [T[,]] [T,P].M`1[[T -> T[,]]]()");
assertEquals("static [T[,]] [T,P].M`1[[T -> T[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> T[,]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_27() {
IMethodName sut = new MethodName("static [T[,]] [T,P].M`2[[T],[U]]()");
assertEquals("static [T[,]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_28() {
IMethodName sut = new MethodName("static [T[,]] [T,P].M()");
assertEquals("static [T[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_15_29() {
IMethodName sut = new MethodName("static [T[,]] [T,P].M(out [?] p)");
assertEquals("static [T[,]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_15_30() {
IMethodName sut = new MethodName("static [T[,]] [T,P].M([T[,]] p)");
assertEquals("static [T[,]] [T,P].M([T[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,]] p");
}
@Test
public void MethodNameTest_15_31() {
IMethodName sut = new MethodName("static [T[,]] [T,P].M([T[,]] p1, [T[,]] p2)");
assertEquals("static [T[,]] [T,P].M([T[,]] p1, [T[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertEquals(new TypeParameterName("T[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[T[,]] p1", "[T[,]] p2");
}
@Test
public void PropertyNameTest_15_0() {
IPropertyName sut = new PropertyName("[T,P] [T[,]].P()");
assertEquals("[T,P] [T[,]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_15_1() {
IPropertyName sut = new PropertyName("[T[,]] [T,P].P()");
assertEquals("[T[,]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_15_2() {
IPropertyName sut = new PropertyName("static [T,P] [T[,]].P()");
assertEquals("static [T,P] [T[,]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeParameterName("T[,]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_15_3() {
IPropertyName sut = new PropertyName("static [T[,]] [T,P].P()");
assertEquals("static [T[,]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeParameterName("T[,]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_16_0() {
IFieldName sut = new FieldName("[T,P] [p:int]._f");
assertEquals("[T,P] [p:int]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_16_1() {
IFieldName sut = new FieldName("[p:int] [T,P]._f");
assertEquals("[p:int] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
}
@Test
public void FieldNameTest_16_2() {
IFieldName sut = new FieldName("static [T,P] [p:int]._f");
assertEquals("static [T,P] [p:int]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_16_3() {
IFieldName sut = new FieldName("static [p:int] [T,P]._f");
assertEquals("static [p:int] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
}
@Test
public void EventNameTest_16_0() {
IEventName sut = new EventName("[T,P] [p:int].e");
assertEquals("[T,P] [p:int].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_16_1() {
IEventName sut = new EventName("[p:int] [T,P].e");
assertEquals("[p:int] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getHandlerType());
}
@Test
public void EventNameTest_16_2() {
IEventName sut = new EventName("static [T,P] [p:int].e");
assertEquals("static [T,P] [p:int].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_16_3() {
IEventName sut = new EventName("static [p:int] [T,P].e");
assertEquals("static [p:int] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getHandlerType());
}
@Test
public void MethodNameTest_16_0() {
IMethodName sut = new MethodName("[T,P] [p:int].M()");
assertEquals("[T,P] [p:int].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_1() {
IMethodName sut = new MethodName("[T,P] [p:int].M`1[[T]]()");
assertEquals("[T,P] [p:int].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_2() {
IMethodName sut = new MethodName("[T,P] [p:int].M`1[[T -> p:int]]()");
assertEquals("[T,P] [p:int].M`1[[T -> p:int]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_3() {
IMethodName sut = new MethodName("[T,P] [p:int].M`2[[T],[U]]()");
assertEquals("[T,P] [p:int].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_4() {
IMethodName sut = new MethodName("[T,P] [p:int].M()");
assertEquals("[T,P] [p:int].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_5() {
IMethodName sut = new MethodName("[T,P] [p:int].M(out [?] p)");
assertEquals("[T,P] [p:int].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_16_6() {
IMethodName sut = new MethodName("[T,P] [p:int].M([p:int] p)");
assertEquals("[T,P] [p:int].M([p:int] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int] p");
}
@Test
public void MethodNameTest_16_7() {
IMethodName sut = new MethodName("[T,P] [p:int].M([p:int] p1, [p:int] p2)");
assertEquals("[T,P] [p:int].M([p:int] p1, [p:int] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int] p1", "[p:int] p2");
}
@Test
public void MethodNameTest_16_8() {
IMethodName sut = new MethodName("[p:int] [T,P].M()");
assertEquals("[p:int] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_9() {
IMethodName sut = new MethodName("[p:int] [T,P].M`1[[T]]()");
assertEquals("[p:int] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_10() {
IMethodName sut = new MethodName("[p:int] [T,P].M`1[[T -> p:int]]()");
assertEquals("[p:int] [T,P].M`1[[T -> p:int]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_11() {
IMethodName sut = new MethodName("[p:int] [T,P].M`2[[T],[U]]()");
assertEquals("[p:int] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_12() {
IMethodName sut = new MethodName("[p:int] [T,P].M()");
assertEquals("[p:int] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_13() {
IMethodName sut = new MethodName("[p:int] [T,P].M(out [?] p)");
assertEquals("[p:int] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_16_14() {
IMethodName sut = new MethodName("[p:int] [T,P].M([p:int] p)");
assertEquals("[p:int] [T,P].M([p:int] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int] p");
}
@Test
public void MethodNameTest_16_15() {
IMethodName sut = new MethodName("[p:int] [T,P].M([p:int] p1, [p:int] p2)");
assertEquals("[p:int] [T,P].M([p:int] p1, [p:int] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int] p1", "[p:int] p2");
}
@Test
public void MethodNameTest_16_16() {
IMethodName sut = new MethodName("static [T,P] [p:int].M()");
assertEquals("static [T,P] [p:int].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_17() {
IMethodName sut = new MethodName("static [T,P] [p:int].M`1[[T]]()");
assertEquals("static [T,P] [p:int].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_18() {
IMethodName sut = new MethodName("static [T,P] [p:int].M`1[[T -> p:int]]()");
assertEquals("static [T,P] [p:int].M`1[[T -> p:int]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_19() {
IMethodName sut = new MethodName("static [T,P] [p:int].M`2[[T],[U]]()");
assertEquals("static [T,P] [p:int].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_20() {
IMethodName sut = new MethodName("static [T,P] [p:int].M()");
assertEquals("static [T,P] [p:int].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_21() {
IMethodName sut = new MethodName("static [T,P] [p:int].M(out [?] p)");
assertEquals("static [T,P] [p:int].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_16_22() {
IMethodName sut = new MethodName("static [T,P] [p:int].M([p:int] p)");
assertEquals("static [T,P] [p:int].M([p:int] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int] p");
}
@Test
public void MethodNameTest_16_23() {
IMethodName sut = new MethodName("static [T,P] [p:int].M([p:int] p1, [p:int] p2)");
assertEquals("static [T,P] [p:int].M([p:int] p1, [p:int] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int] p1", "[p:int] p2");
}
@Test
public void MethodNameTest_16_24() {
IMethodName sut = new MethodName("static [p:int] [T,P].M()");
assertEquals("static [p:int] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_25() {
IMethodName sut = new MethodName("static [p:int] [T,P].M`1[[T]]()");
assertEquals("static [p:int] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_26() {
IMethodName sut = new MethodName("static [p:int] [T,P].M`1[[T -> p:int]]()");
assertEquals("static [p:int] [T,P].M`1[[T -> p:int]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_27() {
IMethodName sut = new MethodName("static [p:int] [T,P].M`2[[T],[U]]()");
assertEquals("static [p:int] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_28() {
IMethodName sut = new MethodName("static [p:int] [T,P].M()");
assertEquals("static [p:int] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_16_29() {
IMethodName sut = new MethodName("static [p:int] [T,P].M(out [?] p)");
assertEquals("static [p:int] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_16_30() {
IMethodName sut = new MethodName("static [p:int] [T,P].M([p:int] p)");
assertEquals("static [p:int] [T,P].M([p:int] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int] p");
}
@Test
public void MethodNameTest_16_31() {
IMethodName sut = new MethodName("static [p:int] [T,P].M([p:int] p1, [p:int] p2)");
assertEquals("static [p:int] [T,P].M([p:int] p1, [p:int] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int] p1", "[p:int] p2");
}
@Test
public void PropertyNameTest_16_0() {
IPropertyName sut = new PropertyName("[T,P] [p:int].P()");
assertEquals("[T,P] [p:int].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_16_1() {
IPropertyName sut = new PropertyName("[p:int] [T,P].P()");
assertEquals("[p:int] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_16_2() {
IPropertyName sut = new PropertyName("static [T,P] [p:int].P()");
assertEquals("static [T,P] [p:int].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_16_3() {
IPropertyName sut = new PropertyName("static [p:int] [T,P].P()");
assertEquals("static [p:int] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:int"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_17_0() {
IFieldName sut = new FieldName("[T,P] [p:int[]]._f");
assertEquals("[T,P] [p:int[]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_17_1() {
IFieldName sut = new FieldName("[p:int[]] [T,P]._f");
assertEquals("[p:int[]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
}
@Test
public void FieldNameTest_17_2() {
IFieldName sut = new FieldName("static [T,P] [p:int[]]._f");
assertEquals("static [T,P] [p:int[]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_17_3() {
IFieldName sut = new FieldName("static [p:int[]] [T,P]._f");
assertEquals("static [p:int[]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
}
@Test
public void EventNameTest_17_0() {
IEventName sut = new EventName("[T,P] [p:int[]].e");
assertEquals("[T,P] [p:int[]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_17_1() {
IEventName sut = new EventName("[p:int[]] [T,P].e");
assertEquals("[p:int[]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getHandlerType());
}
@Test
public void EventNameTest_17_2() {
IEventName sut = new EventName("static [T,P] [p:int[]].e");
assertEquals("static [T,P] [p:int[]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_17_3() {
IEventName sut = new EventName("static [p:int[]] [T,P].e");
assertEquals("static [p:int[]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getHandlerType());
}
@Test
public void MethodNameTest_17_0() {
IMethodName sut = new MethodName("[T,P] [p:int[]].M()");
assertEquals("[T,P] [p:int[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_1() {
IMethodName sut = new MethodName("[T,P] [p:int[]].M`1[[T]]()");
assertEquals("[T,P] [p:int[]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_2() {
IMethodName sut = new MethodName("[T,P] [p:int[]].M`1[[T -> p:int[]]]()");
assertEquals("[T,P] [p:int[]].M`1[[T -> p:int[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int[]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_3() {
IMethodName sut = new MethodName("[T,P] [p:int[]].M`2[[T],[U]]()");
assertEquals("[T,P] [p:int[]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_4() {
IMethodName sut = new MethodName("[T,P] [p:int[]].M()");
assertEquals("[T,P] [p:int[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_5() {
IMethodName sut = new MethodName("[T,P] [p:int[]].M(out [?] p)");
assertEquals("[T,P] [p:int[]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_17_6() {
IMethodName sut = new MethodName("[T,P] [p:int[]].M([p:int[]] p)");
assertEquals("[T,P] [p:int[]].M([p:int[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[]] p");
}
@Test
public void MethodNameTest_17_7() {
IMethodName sut = new MethodName("[T,P] [p:int[]].M([p:int[]] p1, [p:int[]] p2)");
assertEquals("[T,P] [p:int[]].M([p:int[]] p1, [p:int[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[]] p1", "[p:int[]] p2");
}
@Test
public void MethodNameTest_17_8() {
IMethodName sut = new MethodName("[p:int[]] [T,P].M()");
assertEquals("[p:int[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_9() {
IMethodName sut = new MethodName("[p:int[]] [T,P].M`1[[T]]()");
assertEquals("[p:int[]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_10() {
IMethodName sut = new MethodName("[p:int[]] [T,P].M`1[[T -> p:int[]]]()");
assertEquals("[p:int[]] [T,P].M`1[[T -> p:int[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int[]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_11() {
IMethodName sut = new MethodName("[p:int[]] [T,P].M`2[[T],[U]]()");
assertEquals("[p:int[]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_12() {
IMethodName sut = new MethodName("[p:int[]] [T,P].M()");
assertEquals("[p:int[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_13() {
IMethodName sut = new MethodName("[p:int[]] [T,P].M(out [?] p)");
assertEquals("[p:int[]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_17_14() {
IMethodName sut = new MethodName("[p:int[]] [T,P].M([p:int[]] p)");
assertEquals("[p:int[]] [T,P].M([p:int[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[]] p");
}
@Test
public void MethodNameTest_17_15() {
IMethodName sut = new MethodName("[p:int[]] [T,P].M([p:int[]] p1, [p:int[]] p2)");
assertEquals("[p:int[]] [T,P].M([p:int[]] p1, [p:int[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[]] p1", "[p:int[]] p2");
}
@Test
public void MethodNameTest_17_16() {
IMethodName sut = new MethodName("static [T,P] [p:int[]].M()");
assertEquals("static [T,P] [p:int[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_17() {
IMethodName sut = new MethodName("static [T,P] [p:int[]].M`1[[T]]()");
assertEquals("static [T,P] [p:int[]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_18() {
IMethodName sut = new MethodName("static [T,P] [p:int[]].M`1[[T -> p:int[]]]()");
assertEquals("static [T,P] [p:int[]].M`1[[T -> p:int[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int[]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_19() {
IMethodName sut = new MethodName("static [T,P] [p:int[]].M`2[[T],[U]]()");
assertEquals("static [T,P] [p:int[]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_20() {
IMethodName sut = new MethodName("static [T,P] [p:int[]].M()");
assertEquals("static [T,P] [p:int[]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_21() {
IMethodName sut = new MethodName("static [T,P] [p:int[]].M(out [?] p)");
assertEquals("static [T,P] [p:int[]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_17_22() {
IMethodName sut = new MethodName("static [T,P] [p:int[]].M([p:int[]] p)");
assertEquals("static [T,P] [p:int[]].M([p:int[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[]] p");
}
@Test
public void MethodNameTest_17_23() {
IMethodName sut = new MethodName("static [T,P] [p:int[]].M([p:int[]] p1, [p:int[]] p2)");
assertEquals("static [T,P] [p:int[]].M([p:int[]] p1, [p:int[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[]] p1", "[p:int[]] p2");
}
@Test
public void MethodNameTest_17_24() {
IMethodName sut = new MethodName("static [p:int[]] [T,P].M()");
assertEquals("static [p:int[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_25() {
IMethodName sut = new MethodName("static [p:int[]] [T,P].M`1[[T]]()");
assertEquals("static [p:int[]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_26() {
IMethodName sut = new MethodName("static [p:int[]] [T,P].M`1[[T -> p:int[]]]()");
assertEquals("static [p:int[]] [T,P].M`1[[T -> p:int[]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int[]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_27() {
IMethodName sut = new MethodName("static [p:int[]] [T,P].M`2[[T],[U]]()");
assertEquals("static [p:int[]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_28() {
IMethodName sut = new MethodName("static [p:int[]] [T,P].M()");
assertEquals("static [p:int[]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_17_29() {
IMethodName sut = new MethodName("static [p:int[]] [T,P].M(out [?] p)");
assertEquals("static [p:int[]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_17_30() {
IMethodName sut = new MethodName("static [p:int[]] [T,P].M([p:int[]] p)");
assertEquals("static [p:int[]] [T,P].M([p:int[]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[]] p");
}
@Test
public void MethodNameTest_17_31() {
IMethodName sut = new MethodName("static [p:int[]] [T,P].M([p:int[]] p1, [p:int[]] p2)");
assertEquals("static [p:int[]] [T,P].M([p:int[]] p1, [p:int[]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[]] p1", "[p:int[]] p2");
}
@Test
public void PropertyNameTest_17_0() {
IPropertyName sut = new PropertyName("[T,P] [p:int[]].P()");
assertEquals("[T,P] [p:int[]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_17_1() {
IPropertyName sut = new PropertyName("[p:int[]] [T,P].P()");
assertEquals("[p:int[]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_17_2() {
IPropertyName sut = new PropertyName("static [T,P] [p:int[]].P()");
assertEquals("static [T,P] [p:int[]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_17_3() {
IPropertyName sut = new PropertyName("static [p:int[]] [T,P].P()");
assertEquals("static [p:int[]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:int[]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_18_0() {
IFieldName sut = new FieldName("[T,P] [p:int[,]]._f");
assertEquals("[T,P] [p:int[,]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_18_1() {
IFieldName sut = new FieldName("[p:int[,]] [T,P]._f");
assertEquals("[p:int[,]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
}
@Test
public void FieldNameTest_18_2() {
IFieldName sut = new FieldName("static [T,P] [p:int[,]]._f");
assertEquals("static [T,P] [p:int[,]]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_18_3() {
IFieldName sut = new FieldName("static [p:int[,]] [T,P]._f");
assertEquals("static [p:int[,]] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
}
@Test
public void EventNameTest_18_0() {
IEventName sut = new EventName("[T,P] [p:int[,]].e");
assertEquals("[T,P] [p:int[,]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_18_1() {
IEventName sut = new EventName("[p:int[,]] [T,P].e");
assertEquals("[p:int[,]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getHandlerType());
}
@Test
public void EventNameTest_18_2() {
IEventName sut = new EventName("static [T,P] [p:int[,]].e");
assertEquals("static [T,P] [p:int[,]].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_18_3() {
IEventName sut = new EventName("static [p:int[,]] [T,P].e");
assertEquals("static [p:int[,]] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getHandlerType());
}
@Test
public void MethodNameTest_18_0() {
IMethodName sut = new MethodName("[T,P] [p:int[,]].M()");
assertEquals("[T,P] [p:int[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_1() {
IMethodName sut = new MethodName("[T,P] [p:int[,]].M`1[[T]]()");
assertEquals("[T,P] [p:int[,]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_2() {
IMethodName sut = new MethodName("[T,P] [p:int[,]].M`1[[T -> p:int[,]]]()");
assertEquals("[T,P] [p:int[,]].M`1[[T -> p:int[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int[,]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_3() {
IMethodName sut = new MethodName("[T,P] [p:int[,]].M`2[[T],[U]]()");
assertEquals("[T,P] [p:int[,]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_4() {
IMethodName sut = new MethodName("[T,P] [p:int[,]].M()");
assertEquals("[T,P] [p:int[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_5() {
IMethodName sut = new MethodName("[T,P] [p:int[,]].M(out [?] p)");
assertEquals("[T,P] [p:int[,]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_18_6() {
IMethodName sut = new MethodName("[T,P] [p:int[,]].M([p:int[,]] p)");
assertEquals("[T,P] [p:int[,]].M([p:int[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[,]] p");
}
@Test
public void MethodNameTest_18_7() {
IMethodName sut = new MethodName("[T,P] [p:int[,]].M([p:int[,]] p1, [p:int[,]] p2)");
assertEquals("[T,P] [p:int[,]].M([p:int[,]] p1, [p:int[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[,]] p1", "[p:int[,]] p2");
}
@Test
public void MethodNameTest_18_8() {
IMethodName sut = new MethodName("[p:int[,]] [T,P].M()");
assertEquals("[p:int[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_9() {
IMethodName sut = new MethodName("[p:int[,]] [T,P].M`1[[T]]()");
assertEquals("[p:int[,]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_10() {
IMethodName sut = new MethodName("[p:int[,]] [T,P].M`1[[T -> p:int[,]]]()");
assertEquals("[p:int[,]] [T,P].M`1[[T -> p:int[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int[,]]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_11() {
IMethodName sut = new MethodName("[p:int[,]] [T,P].M`2[[T],[U]]()");
assertEquals("[p:int[,]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_12() {
IMethodName sut = new MethodName("[p:int[,]] [T,P].M()");
assertEquals("[p:int[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_13() {
IMethodName sut = new MethodName("[p:int[,]] [T,P].M(out [?] p)");
assertEquals("[p:int[,]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_18_14() {
IMethodName sut = new MethodName("[p:int[,]] [T,P].M([p:int[,]] p)");
assertEquals("[p:int[,]] [T,P].M([p:int[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[,]] p");
}
@Test
public void MethodNameTest_18_15() {
IMethodName sut = new MethodName("[p:int[,]] [T,P].M([p:int[,]] p1, [p:int[,]] p2)");
assertEquals("[p:int[,]] [T,P].M([p:int[,]] p1, [p:int[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[,]] p1", "[p:int[,]] p2");
}
@Test
public void MethodNameTest_18_16() {
IMethodName sut = new MethodName("static [T,P] [p:int[,]].M()");
assertEquals("static [T,P] [p:int[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_17() {
IMethodName sut = new MethodName("static [T,P] [p:int[,]].M`1[[T]]()");
assertEquals("static [T,P] [p:int[,]].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_18() {
IMethodName sut = new MethodName("static [T,P] [p:int[,]].M`1[[T -> p:int[,]]]()");
assertEquals("static [T,P] [p:int[,]].M`1[[T -> p:int[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int[,]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_19() {
IMethodName sut = new MethodName("static [T,P] [p:int[,]].M`2[[T],[U]]()");
assertEquals("static [T,P] [p:int[,]].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_20() {
IMethodName sut = new MethodName("static [T,P] [p:int[,]].M()");
assertEquals("static [T,P] [p:int[,]].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_21() {
IMethodName sut = new MethodName("static [T,P] [p:int[,]].M(out [?] p)");
assertEquals("static [T,P] [p:int[,]].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_18_22() {
IMethodName sut = new MethodName("static [T,P] [p:int[,]].M([p:int[,]] p)");
assertEquals("static [T,P] [p:int[,]].M([p:int[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[,]] p");
}
@Test
public void MethodNameTest_18_23() {
IMethodName sut = new MethodName("static [T,P] [p:int[,]].M([p:int[,]] p1, [p:int[,]] p2)");
assertEquals("static [T,P] [p:int[,]].M([p:int[,]] p1, [p:int[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[,]] p1", "[p:int[,]] p2");
}
@Test
public void MethodNameTest_18_24() {
IMethodName sut = new MethodName("static [p:int[,]] [T,P].M()");
assertEquals("static [p:int[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_25() {
IMethodName sut = new MethodName("static [p:int[,]] [T,P].M`1[[T]]()");
assertEquals("static [p:int[,]] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_26() {
IMethodName sut = new MethodName("static [p:int[,]] [T,P].M`1[[T -> p:int[,]]]()");
assertEquals("static [p:int[,]] [T,P].M`1[[T -> p:int[,]]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> p:int[,]]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_27() {
IMethodName sut = new MethodName("static [p:int[,]] [T,P].M`2[[T],[U]]()");
assertEquals("static [p:int[,]] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_28() {
IMethodName sut = new MethodName("static [p:int[,]] [T,P].M()");
assertEquals("static [p:int[,]] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_18_29() {
IMethodName sut = new MethodName("static [p:int[,]] [T,P].M(out [?] p)");
assertEquals("static [p:int[,]] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_18_30() {
IMethodName sut = new MethodName("static [p:int[,]] [T,P].M([p:int[,]] p)");
assertEquals("static [p:int[,]] [T,P].M([p:int[,]] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[,]] p");
}
@Test
public void MethodNameTest_18_31() {
IMethodName sut = new MethodName("static [p:int[,]] [T,P].M([p:int[,]] p1, [p:int[,]] p2)");
assertEquals("static [p:int[,]] [T,P].M([p:int[,]] p1, [p:int[,]] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[p:int[,]] p1", "[p:int[,]] p2");
}
@Test
public void PropertyNameTest_18_0() {
IPropertyName sut = new PropertyName("[T,P] [p:int[,]].P()");
assertEquals("[T,P] [p:int[,]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_18_1() {
IPropertyName sut = new PropertyName("[p:int[,]] [T,P].P()");
assertEquals("[p:int[,]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_18_2() {
IPropertyName sut = new PropertyName("static [T,P] [p:int[,]].P()");
assertEquals("static [T,P] [p:int[,]].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_18_3() {
IPropertyName sut = new PropertyName("static [p:int[,]] [T,P].P()");
assertEquals("static [p:int[,]] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:int[,]"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_19_0() {
IFieldName sut = new FieldName("[T,P] [A, B, 1.2.3.4]._f");
assertEquals("[T,P] [A, B, 1.2.3.4]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_19_1() {
IFieldName sut = new FieldName("[A, B, 1.2.3.4] [T,P]._f");
assertEquals("[A, B, 1.2.3.4] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
}
@Test
public void FieldNameTest_19_2() {
IFieldName sut = new FieldName("static [T,P] [A, B, 1.2.3.4]._f");
assertEquals("static [T,P] [A, B, 1.2.3.4]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_19_3() {
IFieldName sut = new FieldName("static [A, B, 1.2.3.4] [T,P]._f");
assertEquals("static [A, B, 1.2.3.4] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
}
@Test
public void EventNameTest_19_0() {
IEventName sut = new EventName("[T,P] [A, B, 1.2.3.4].e");
assertEquals("[T,P] [A, B, 1.2.3.4].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_19_1() {
IEventName sut = new EventName("[A, B, 1.2.3.4] [T,P].e");
assertEquals("[A, B, 1.2.3.4] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getHandlerType());
}
@Test
public void EventNameTest_19_2() {
IEventName sut = new EventName("static [T,P] [A, B, 1.2.3.4].e");
assertEquals("static [T,P] [A, B, 1.2.3.4].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_19_3() {
IEventName sut = new EventName("static [A, B, 1.2.3.4] [T,P].e");
assertEquals("static [A, B, 1.2.3.4] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getHandlerType());
}
@Test
public void MethodNameTest_19_0() {
IMethodName sut = new MethodName("[T,P] [A, B, 1.2.3.4].M()");
assertEquals("[T,P] [A, B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_1() {
IMethodName sut = new MethodName("[T,P] [A, B, 1.2.3.4].M`1[[T]]()");
assertEquals("[T,P] [A, B, 1.2.3.4].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_2() {
IMethodName sut = new MethodName("[T,P] [A, B, 1.2.3.4].M`1[[T -> A, B, 1.2.3.4]]()");
assertEquals("[T,P] [A, B, 1.2.3.4].M`1[[T -> A, B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T -> A, B, 1.2.3.4]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_3() {
IMethodName sut = new MethodName("[T,P] [A, B, 1.2.3.4].M`2[[T],[U]]()");
assertEquals("[T,P] [A, B, 1.2.3.4].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_4() {
IMethodName sut = new MethodName("[T,P] [A, B, 1.2.3.4].M()");
assertEquals("[T,P] [A, B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_5() {
IMethodName sut = new MethodName("[T,P] [A, B, 1.2.3.4].M(out [?] p)");
assertEquals("[T,P] [A, B, 1.2.3.4].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_19_6() {
IMethodName sut = new MethodName("[T,P] [A, B, 1.2.3.4].M([A, B, 1.2.3.4] p)");
assertEquals("[T,P] [A, B, 1.2.3.4].M([A, B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A, B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_19_7() {
IMethodName sut = new MethodName("[T,P] [A, B, 1.2.3.4].M([A, B, 1.2.3.4] p1, [A, B, 1.2.3.4] p2)");
assertEquals("[T,P] [A, B, 1.2.3.4].M([A, B, 1.2.3.4] p1, [A, B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A, B, 1.2.3.4] p1", "[A, B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_19_8() {
IMethodName sut = new MethodName("[A, B, 1.2.3.4] [T,P].M()");
assertEquals("[A, B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_9() {
IMethodName sut = new MethodName("[A, B, 1.2.3.4] [T,P].M`1[[T]]()");
assertEquals("[A, B, 1.2.3.4] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_10() {
IMethodName sut = new MethodName("[A, B, 1.2.3.4] [T,P].M`1[[T -> A, B, 1.2.3.4]]()");
assertEquals("[A, B, 1.2.3.4] [T,P].M`1[[T -> A, B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> A, B, 1.2.3.4]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_11() {
IMethodName sut = new MethodName("[A, B, 1.2.3.4] [T,P].M`2[[T],[U]]()");
assertEquals("[A, B, 1.2.3.4] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_12() {
IMethodName sut = new MethodName("[A, B, 1.2.3.4] [T,P].M()");
assertEquals("[A, B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_13() {
IMethodName sut = new MethodName("[A, B, 1.2.3.4] [T,P].M(out [?] p)");
assertEquals("[A, B, 1.2.3.4] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_19_14() {
IMethodName sut = new MethodName("[A, B, 1.2.3.4] [T,P].M([A, B, 1.2.3.4] p)");
assertEquals("[A, B, 1.2.3.4] [T,P].M([A, B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A, B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_19_15() {
IMethodName sut = new MethodName("[A, B, 1.2.3.4] [T,P].M([A, B, 1.2.3.4] p1, [A, B, 1.2.3.4] p2)");
assertEquals("[A, B, 1.2.3.4] [T,P].M([A, B, 1.2.3.4] p1, [A, B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A, B, 1.2.3.4] p1", "[A, B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_19_16() {
IMethodName sut = new MethodName("static [T,P] [A, B, 1.2.3.4].M()");
assertEquals("static [T,P] [A, B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_17() {
IMethodName sut = new MethodName("static [T,P] [A, B, 1.2.3.4].M`1[[T]]()");
assertEquals("static [T,P] [A, B, 1.2.3.4].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_18() {
IMethodName sut = new MethodName("static [T,P] [A, B, 1.2.3.4].M`1[[T -> A, B, 1.2.3.4]]()");
assertEquals("static [T,P] [A, B, 1.2.3.4].M`1[[T -> A, B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T -> A, B, 1.2.3.4]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_19() {
IMethodName sut = new MethodName("static [T,P] [A, B, 1.2.3.4].M`2[[T],[U]]()");
assertEquals("static [T,P] [A, B, 1.2.3.4].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_20() {
IMethodName sut = new MethodName("static [T,P] [A, B, 1.2.3.4].M()");
assertEquals("static [T,P] [A, B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_21() {
IMethodName sut = new MethodName("static [T,P] [A, B, 1.2.3.4].M(out [?] p)");
assertEquals("static [T,P] [A, B, 1.2.3.4].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_19_22() {
IMethodName sut = new MethodName("static [T,P] [A, B, 1.2.3.4].M([A, B, 1.2.3.4] p)");
assertEquals("static [T,P] [A, B, 1.2.3.4].M([A, B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A, B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_19_23() {
IMethodName sut = new MethodName("static [T,P] [A, B, 1.2.3.4].M([A, B, 1.2.3.4] p1, [A, B, 1.2.3.4] p2)");
assertEquals("static [T,P] [A, B, 1.2.3.4].M([A, B, 1.2.3.4] p1, [A, B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A, B, 1.2.3.4] p1", "[A, B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_19_24() {
IMethodName sut = new MethodName("static [A, B, 1.2.3.4] [T,P].M()");
assertEquals("static [A, B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_25() {
IMethodName sut = new MethodName("static [A, B, 1.2.3.4] [T,P].M`1[[T]]()");
assertEquals("static [A, B, 1.2.3.4] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_26() {
IMethodName sut = new MethodName("static [A, B, 1.2.3.4] [T,P].M`1[[T -> A, B, 1.2.3.4]]()");
assertEquals("static [A, B, 1.2.3.4] [T,P].M`1[[T -> A, B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> A, B, 1.2.3.4]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_27() {
IMethodName sut = new MethodName("static [A, B, 1.2.3.4] [T,P].M`2[[T],[U]]()");
assertEquals("static [A, B, 1.2.3.4] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_28() {
IMethodName sut = new MethodName("static [A, B, 1.2.3.4] [T,P].M()");
assertEquals("static [A, B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_19_29() {
IMethodName sut = new MethodName("static [A, B, 1.2.3.4] [T,P].M(out [?] p)");
assertEquals("static [A, B, 1.2.3.4] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_19_30() {
IMethodName sut = new MethodName("static [A, B, 1.2.3.4] [T,P].M([A, B, 1.2.3.4] p)");
assertEquals("static [A, B, 1.2.3.4] [T,P].M([A, B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A, B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_19_31() {
IMethodName sut = new MethodName("static [A, B, 1.2.3.4] [T,P].M([A, B, 1.2.3.4] p1, [A, B, 1.2.3.4] p2)");
assertEquals("static [A, B, 1.2.3.4] [T,P].M([A, B, 1.2.3.4] p1, [A, B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A, B, 1.2.3.4] p1", "[A, B, 1.2.3.4] p2");
}
@Test
public void PropertyNameTest_19_0() {
IPropertyName sut = new PropertyName("[T,P] [A, B, 1.2.3.4].P()");
assertEquals("[T,P] [A, B, 1.2.3.4].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_19_1() {
IPropertyName sut = new PropertyName("[A, B, 1.2.3.4] [T,P].P()");
assertEquals("[A, B, 1.2.3.4] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_19_2() {
IPropertyName sut = new PropertyName("static [T,P] [A, B, 1.2.3.4].P()");
assertEquals("static [T,P] [A, B, 1.2.3.4].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_19_3() {
IPropertyName sut = new PropertyName("static [A, B, 1.2.3.4] [T,P].P()");
assertEquals("static [A, B, 1.2.3.4] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("A, B, 1.2.3.4"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_20_0() {
IFieldName sut = new FieldName("[T,P] [A[], B, 1.2.3.4]._f");
assertEquals("[T,P] [A[], B, 1.2.3.4]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_20_1() {
IFieldName sut = new FieldName("[A[], B, 1.2.3.4] [T,P]._f");
assertEquals("[A[], B, 1.2.3.4] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
}
@Test
public void FieldNameTest_20_2() {
IFieldName sut = new FieldName("static [T,P] [A[], B, 1.2.3.4]._f");
assertEquals("static [T,P] [A[], B, 1.2.3.4]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_20_3() {
IFieldName sut = new FieldName("static [A[], B, 1.2.3.4] [T,P]._f");
assertEquals("static [A[], B, 1.2.3.4] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
}
@Test
public void EventNameTest_20_0() {
IEventName sut = new EventName("[T,P] [A[], B, 1.2.3.4].e");
assertEquals("[T,P] [A[], B, 1.2.3.4].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_20_1() {
IEventName sut = new EventName("[A[], B, 1.2.3.4] [T,P].e");
assertEquals("[A[], B, 1.2.3.4] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getHandlerType());
}
@Test
public void EventNameTest_20_2() {
IEventName sut = new EventName("static [T,P] [A[], B, 1.2.3.4].e");
assertEquals("static [T,P] [A[], B, 1.2.3.4].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_20_3() {
IEventName sut = new EventName("static [A[], B, 1.2.3.4] [T,P].e");
assertEquals("static [A[], B, 1.2.3.4] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getHandlerType());
}
@Test
public void MethodNameTest_20_0() {
IMethodName sut = new MethodName("[T,P] [A[], B, 1.2.3.4].M()");
assertEquals("[T,P] [A[], B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_1() {
IMethodName sut = new MethodName("[T,P] [A[], B, 1.2.3.4].M`1[[T]]()");
assertEquals("[T,P] [A[], B, 1.2.3.4].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_2() {
IMethodName sut = new MethodName("[T,P] [A[], B, 1.2.3.4].M`1[[T -> A[], B, 1.2.3.4]]()");
assertEquals("[T,P] [A[], B, 1.2.3.4].M`1[[T -> A[], B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T -> A[], B, 1.2.3.4]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_3() {
IMethodName sut = new MethodName("[T,P] [A[], B, 1.2.3.4].M`2[[T],[U]]()");
assertEquals("[T,P] [A[], B, 1.2.3.4].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_4() {
IMethodName sut = new MethodName("[T,P] [A[], B, 1.2.3.4].M()");
assertEquals("[T,P] [A[], B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_5() {
IMethodName sut = new MethodName("[T,P] [A[], B, 1.2.3.4].M(out [?] p)");
assertEquals("[T,P] [A[], B, 1.2.3.4].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_20_6() {
IMethodName sut = new MethodName("[T,P] [A[], B, 1.2.3.4].M([A[], B, 1.2.3.4] p)");
assertEquals("[T,P] [A[], B, 1.2.3.4].M([A[], B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[], B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_20_7() {
IMethodName sut = new MethodName("[T,P] [A[], B, 1.2.3.4].M([A[], B, 1.2.3.4] p1, [A[], B, 1.2.3.4] p2)");
assertEquals("[T,P] [A[], B, 1.2.3.4].M([A[], B, 1.2.3.4] p1, [A[], B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[], B, 1.2.3.4] p1", "[A[], B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_20_8() {
IMethodName sut = new MethodName("[A[], B, 1.2.3.4] [T,P].M()");
assertEquals("[A[], B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_9() {
IMethodName sut = new MethodName("[A[], B, 1.2.3.4] [T,P].M`1[[T]]()");
assertEquals("[A[], B, 1.2.3.4] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_10() {
IMethodName sut = new MethodName("[A[], B, 1.2.3.4] [T,P].M`1[[T -> A[], B, 1.2.3.4]]()");
assertEquals("[A[], B, 1.2.3.4] [T,P].M`1[[T -> A[], B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> A[], B, 1.2.3.4]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_11() {
IMethodName sut = new MethodName("[A[], B, 1.2.3.4] [T,P].M`2[[T],[U]]()");
assertEquals("[A[], B, 1.2.3.4] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_12() {
IMethodName sut = new MethodName("[A[], B, 1.2.3.4] [T,P].M()");
assertEquals("[A[], B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_13() {
IMethodName sut = new MethodName("[A[], B, 1.2.3.4] [T,P].M(out [?] p)");
assertEquals("[A[], B, 1.2.3.4] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_20_14() {
IMethodName sut = new MethodName("[A[], B, 1.2.3.4] [T,P].M([A[], B, 1.2.3.4] p)");
assertEquals("[A[], B, 1.2.3.4] [T,P].M([A[], B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[], B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_20_15() {
IMethodName sut = new MethodName("[A[], B, 1.2.3.4] [T,P].M([A[], B, 1.2.3.4] p1, [A[], B, 1.2.3.4] p2)");
assertEquals("[A[], B, 1.2.3.4] [T,P].M([A[], B, 1.2.3.4] p1, [A[], B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[], B, 1.2.3.4] p1", "[A[], B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_20_16() {
IMethodName sut = new MethodName("static [T,P] [A[], B, 1.2.3.4].M()");
assertEquals("static [T,P] [A[], B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_17() {
IMethodName sut = new MethodName("static [T,P] [A[], B, 1.2.3.4].M`1[[T]]()");
assertEquals("static [T,P] [A[], B, 1.2.3.4].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_18() {
IMethodName sut = new MethodName("static [T,P] [A[], B, 1.2.3.4].M`1[[T -> A[], B, 1.2.3.4]]()");
assertEquals("static [T,P] [A[], B, 1.2.3.4].M`1[[T -> A[], B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T -> A[], B, 1.2.3.4]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_19() {
IMethodName sut = new MethodName("static [T,P] [A[], B, 1.2.3.4].M`2[[T],[U]]()");
assertEquals("static [T,P] [A[], B, 1.2.3.4].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_20() {
IMethodName sut = new MethodName("static [T,P] [A[], B, 1.2.3.4].M()");
assertEquals("static [T,P] [A[], B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_21() {
IMethodName sut = new MethodName("static [T,P] [A[], B, 1.2.3.4].M(out [?] p)");
assertEquals("static [T,P] [A[], B, 1.2.3.4].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_20_22() {
IMethodName sut = new MethodName("static [T,P] [A[], B, 1.2.3.4].M([A[], B, 1.2.3.4] p)");
assertEquals("static [T,P] [A[], B, 1.2.3.4].M([A[], B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[], B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_20_23() {
IMethodName sut = new MethodName("static [T,P] [A[], B, 1.2.3.4].M([A[], B, 1.2.3.4] p1, [A[], B, 1.2.3.4] p2)");
assertEquals("static [T,P] [A[], B, 1.2.3.4].M([A[], B, 1.2.3.4] p1, [A[], B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[], B, 1.2.3.4] p1", "[A[], B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_20_24() {
IMethodName sut = new MethodName("static [A[], B, 1.2.3.4] [T,P].M()");
assertEquals("static [A[], B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_25() {
IMethodName sut = new MethodName("static [A[], B, 1.2.3.4] [T,P].M`1[[T]]()");
assertEquals("static [A[], B, 1.2.3.4] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_26() {
IMethodName sut = new MethodName("static [A[], B, 1.2.3.4] [T,P].M`1[[T -> A[], B, 1.2.3.4]]()");
assertEquals("static [A[], B, 1.2.3.4] [T,P].M`1[[T -> A[], B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> A[], B, 1.2.3.4]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_27() {
IMethodName sut = new MethodName("static [A[], B, 1.2.3.4] [T,P].M`2[[T],[U]]()");
assertEquals("static [A[], B, 1.2.3.4] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_28() {
IMethodName sut = new MethodName("static [A[], B, 1.2.3.4] [T,P].M()");
assertEquals("static [A[], B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_20_29() {
IMethodName sut = new MethodName("static [A[], B, 1.2.3.4] [T,P].M(out [?] p)");
assertEquals("static [A[], B, 1.2.3.4] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_20_30() {
IMethodName sut = new MethodName("static [A[], B, 1.2.3.4] [T,P].M([A[], B, 1.2.3.4] p)");
assertEquals("static [A[], B, 1.2.3.4] [T,P].M([A[], B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[], B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_20_31() {
IMethodName sut = new MethodName("static [A[], B, 1.2.3.4] [T,P].M([A[], B, 1.2.3.4] p1, [A[], B, 1.2.3.4] p2)");
assertEquals("static [A[], B, 1.2.3.4] [T,P].M([A[], B, 1.2.3.4] p1, [A[], B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[], B, 1.2.3.4] p1", "[A[], B, 1.2.3.4] p2");
}
@Test
public void PropertyNameTest_20_0() {
IPropertyName sut = new PropertyName("[T,P] [A[], B, 1.2.3.4].P()");
assertEquals("[T,P] [A[], B, 1.2.3.4].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_20_1() {
IPropertyName sut = new PropertyName("[A[], B, 1.2.3.4] [T,P].P()");
assertEquals("[A[], B, 1.2.3.4] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_20_2() {
IPropertyName sut = new PropertyName("static [T,P] [A[], B, 1.2.3.4].P()");
assertEquals("static [T,P] [A[], B, 1.2.3.4].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_20_3() {
IPropertyName sut = new PropertyName("static [A[], B, 1.2.3.4] [T,P].P()");
assertEquals("static [A[], B, 1.2.3.4] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("A[], B, 1.2.3.4"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void FieldNameTest_21_0() {
IFieldName sut = new FieldName("[T,P] [A[,], B, 1.2.3.4]._f");
assertEquals("[T,P] [A[,], B, 1.2.3.4]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_21_1() {
IFieldName sut = new FieldName("[A[,], B, 1.2.3.4] [T,P]._f");
assertEquals("[A[,], B, 1.2.3.4] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
}
@Test
public void FieldNameTest_21_2() {
IFieldName sut = new FieldName("static [T,P] [A[,], B, 1.2.3.4]._f");
assertEquals("static [T,P] [A[,], B, 1.2.3.4]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
}
@Test
public void FieldNameTest_21_3() {
IFieldName sut = new FieldName("static [A[,], B, 1.2.3.4] [T,P]._f");
assertEquals("static [A[,], B, 1.2.3.4] [T,P]._f", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("_f", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("_f", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
}
@Test
public void EventNameTest_21_0() {
IEventName sut = new EventName("[T,P] [A[,], B, 1.2.3.4].e");
assertEquals("[T,P] [A[,], B, 1.2.3.4].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_21_1() {
IEventName sut = new EventName("[A[,], B, 1.2.3.4] [T,P].e");
assertEquals("[A[,], B, 1.2.3.4] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getHandlerType());
}
@Test
public void EventNameTest_21_2() {
IEventName sut = new EventName("static [T,P] [A[,], B, 1.2.3.4].e");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getHandlerType());
}
@Test
public void EventNameTest_21_3() {
IEventName sut = new EventName("static [A[,], B, 1.2.3.4] [T,P].e");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].e", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("e", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("e", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getHandlerType());
}
@Test
public void MethodNameTest_21_0() {
IMethodName sut = new MethodName("[T,P] [A[,], B, 1.2.3.4].M()");
assertEquals("[T,P] [A[,], B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_1() {
IMethodName sut = new MethodName("[T,P] [A[,], B, 1.2.3.4].M`1[[T]]()");
assertEquals("[T,P] [A[,], B, 1.2.3.4].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_2() {
IMethodName sut = new MethodName("[T,P] [A[,], B, 1.2.3.4].M`1[[T -> A[,], B, 1.2.3.4]]()");
assertEquals("[T,P] [A[,], B, 1.2.3.4].M`1[[T -> A[,], B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T -> A[,], B, 1.2.3.4]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_3() {
IMethodName sut = new MethodName("[T,P] [A[,], B, 1.2.3.4].M`2[[T],[U]]()");
assertEquals("[T,P] [A[,], B, 1.2.3.4].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_4() {
IMethodName sut = new MethodName("[T,P] [A[,], B, 1.2.3.4].M()");
assertEquals("[T,P] [A[,], B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_5() {
IMethodName sut = new MethodName("[T,P] [A[,], B, 1.2.3.4].M(out [?] p)");
assertEquals("[T,P] [A[,], B, 1.2.3.4].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_21_6() {
IMethodName sut = new MethodName("[T,P] [A[,], B, 1.2.3.4].M([A[,], B, 1.2.3.4] p)");
assertEquals("[T,P] [A[,], B, 1.2.3.4].M([A[,], B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[,], B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_21_7() {
IMethodName sut = new MethodName("[T,P] [A[,], B, 1.2.3.4].M([A[,], B, 1.2.3.4] p1, [A[,], B, 1.2.3.4] p2)");
assertEquals("[T,P] [A[,], B, 1.2.3.4].M([A[,], B, 1.2.3.4] p1, [A[,], B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[,], B, 1.2.3.4] p1", "[A[,], B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_21_8() {
IMethodName sut = new MethodName("[A[,], B, 1.2.3.4] [T,P].M()");
assertEquals("[A[,], B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_9() {
IMethodName sut = new MethodName("[A[,], B, 1.2.3.4] [T,P].M`1[[T]]()");
assertEquals("[A[,], B, 1.2.3.4] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_10() {
IMethodName sut = new MethodName("[A[,], B, 1.2.3.4] [T,P].M`1[[T -> A[,], B, 1.2.3.4]]()");
assertEquals("[A[,], B, 1.2.3.4] [T,P].M`1[[T -> A[,], B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> A[,], B, 1.2.3.4]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_11() {
IMethodName sut = new MethodName("[A[,], B, 1.2.3.4] [T,P].M`2[[T],[U]]()");
assertEquals("[A[,], B, 1.2.3.4] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_12() {
IMethodName sut = new MethodName("[A[,], B, 1.2.3.4] [T,P].M()");
assertEquals("[A[,], B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_13() {
IMethodName sut = new MethodName("[A[,], B, 1.2.3.4] [T,P].M(out [?] p)");
assertEquals("[A[,], B, 1.2.3.4] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_21_14() {
IMethodName sut = new MethodName("[A[,], B, 1.2.3.4] [T,P].M([A[,], B, 1.2.3.4] p)");
assertEquals("[A[,], B, 1.2.3.4] [T,P].M([A[,], B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[,], B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_21_15() {
IMethodName sut = new MethodName("[A[,], B, 1.2.3.4] [T,P].M([A[,], B, 1.2.3.4] p1, [A[,], B, 1.2.3.4] p2)");
assertEquals("[A[,], B, 1.2.3.4] [T,P].M([A[,], B, 1.2.3.4] p1, [A[,], B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[,], B, 1.2.3.4] p1", "[A[,], B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_21_16() {
IMethodName sut = new MethodName("static [T,P] [A[,], B, 1.2.3.4].M()");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_17() {
IMethodName sut = new MethodName("static [T,P] [A[,], B, 1.2.3.4].M`1[[T]]()");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_18() {
IMethodName sut = new MethodName("static [T,P] [A[,], B, 1.2.3.4].M`1[[T -> A[,], B, 1.2.3.4]]()");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].M`1[[T -> A[,], B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`1[[T -> A[,], B, 1.2.3.4]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_19() {
IMethodName sut = new MethodName("static [T,P] [A[,], B, 1.2.3.4].M`2[[T],[U]]()");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_20() {
IMethodName sut = new MethodName("static [T,P] [A[,], B, 1.2.3.4].M()");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_21() {
IMethodName sut = new MethodName("static [T,P] [A[,], B, 1.2.3.4].M(out [?] p)");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_21_22() {
IMethodName sut = new MethodName("static [T,P] [A[,], B, 1.2.3.4].M([A[,], B, 1.2.3.4] p)");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].M([A[,], B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[,], B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_21_23() {
IMethodName sut = new MethodName("static [T,P] [A[,], B, 1.2.3.4].M([A[,], B, 1.2.3.4] p1, [A[,], B, 1.2.3.4] p2)");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].M([A[,], B, 1.2.3.4] p1, [A[,], B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertEquals(new TypeName("T,P"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[,], B, 1.2.3.4] p1", "[A[,], B, 1.2.3.4] p2");
}
@Test
public void MethodNameTest_21_24() {
IMethodName sut = new MethodName("static [A[,], B, 1.2.3.4] [T,P].M()");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_25() {
IMethodName sut = new MethodName("static [A[,], B, 1.2.3.4] [T,P].M`1[[T]]()");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].M`1[[T]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_26() {
IMethodName sut = new MethodName("static [A[,], B, 1.2.3.4] [T,P].M`1[[T -> A[,], B, 1.2.3.4]]()");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].M`1[[T -> A[,], B, 1.2.3.4]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`1[[T -> A[,], B, 1.2.3.4]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_27() {
IMethodName sut = new MethodName("static [A[,], B, 1.2.3.4] [T,P].M`2[[T],[U]]()");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].M`2[[T],[U]]()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M`2[[T],[U]]", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_28() {
IMethodName sut = new MethodName("static [A[,], B, 1.2.3.4] [T,P].M()");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].M()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_21_29() {
IMethodName sut = new MethodName("static [A[,], B, 1.2.3.4] [T,P].M(out [?] p)");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].M(out [?] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "out [?] p");
}
@Test
public void MethodNameTest_21_30() {
IMethodName sut = new MethodName("static [A[,], B, 1.2.3.4] [T,P].M([A[,], B, 1.2.3.4] p)");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].M([A[,], B, 1.2.3.4] p)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[,], B, 1.2.3.4] p");
}
@Test
public void MethodNameTest_21_31() {
IMethodName sut = new MethodName("static [A[,], B, 1.2.3.4] [T,P].M([A[,], B, 1.2.3.4] p1, [A[,], B, 1.2.3.4] p2)");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].M([A[,], B, 1.2.3.4] p1, [A[,], B, 1.2.3.4] p2)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("M", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("M", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, true, "[A[,], B, 1.2.3.4] p1", "[A[,], B, 1.2.3.4] p2");
}
@Test
public void PropertyNameTest_21_0() {
IPropertyName sut = new PropertyName("[T,P] [A[,], B, 1.2.3.4].P()");
assertEquals("[T,P] [A[,], B, 1.2.3.4].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_21_1() {
IPropertyName sut = new PropertyName("[A[,], B, 1.2.3.4] [T,P].P()");
assertEquals("[A[,], B, 1.2.3.4] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_21_2() {
IPropertyName sut = new PropertyName("static [T,P] [A[,], B, 1.2.3.4].P()");
assertEquals("static [T,P] [A[,], B, 1.2.3.4].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new TypeName("T,P"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_21_3() {
IPropertyName sut = new PropertyName("static [A[,], B, 1.2.3.4] [T,P].P()");
assertEquals("static [A[,], B, 1.2.3.4] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new ArrayTypeName("A[,], B, 1.2.3.4"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_23_0() {
IMethodName sut = new MethodName("[p:void] [T,P]..ctor()");
assertEquals("[p:void] [T,P]..ctor()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals(".ctor", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals(".ctor", sut.getName());
assertEquals(new PredefinedTypeName("p:void"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:void"), sut.getReturnType());
assertTrue(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_23_1() {
IMethodName sut = new MethodName("[p:void] [T,P]..cctor()");
assertEquals("[p:void] [T,P]..cctor()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals(".cctor", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals(".cctor", sut.getName());
assertEquals(new PredefinedTypeName("p:void"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:void"), sut.getReturnType());
assertTrue(sut.isConstructor());
assertFalse(sut.isExtensionMethod());
assertParameterizedName(sut, false);
}
@Test
public void MethodNameTest_23_2() {
IMethodName sut = new MethodName("static [p:void] [T,P].Ext(this [p:int] i)");
assertEquals("static [p:void] [T,P].Ext(this [p:int] i)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("Ext", sut.getFullName());
assertTrue(sut.isStatic());
assertEquals("Ext", sut.getName());
assertEquals(new PredefinedTypeName("p:void"), sut.getValueType());
assertEquals(new PredefinedTypeName("p:void"), sut.getReturnType());
assertFalse(sut.isConstructor());
assertTrue(sut.isExtensionMethod());
assertParameterizedName(sut, true, "this [p:int] i");
}
@Test
public void PropertyNameTest_24_0() {
IPropertyName sut = new PropertyName("[p:void] [T,P].P()");
assertEquals("[p:void] [T,P].P()", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:void"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertFalse(sut.isIndexer());
assertParameterizedName(sut, false);
}
@Test
public void PropertyNameTest_24_1() {
IPropertyName sut = new PropertyName("[p:void] [T,P].P([p:int] i)");
assertEquals("[p:void] [T,P].P([p:int] i)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:void"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertTrue(sut.isIndexer());
assertParameterizedName(sut, true, "[p:int] i");
}
@Test
public void PropertyNameTest_24_2() {
IPropertyName sut = new PropertyName("[p:void] [T,P].P([p:int] i, [p:int] j)");
assertEquals("[p:void] [T,P].P([p:int] i, [p:int] j)", sut.getIdentifier());
assertFalse(sut.isUnknown());
assertFalse(sut.isHashed());
assertEquals(new TypeName("T,P"), sut.getDeclaringType());
assertEquals("P", sut.getFullName());
assertFalse(sut.isStatic());
assertEquals("P", sut.getName());
assertEquals(new PredefinedTypeName("p:void"), sut.getValueType());
assertFalse(sut.hasGetter());
assertFalse(sut.hasSetter());
assertTrue(sut.isIndexer());
assertParameterizedName(sut, true, "[p:int] i", "[p:int] j");
}
}