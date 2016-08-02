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
package exec.recommender_reimplementation.java_printer.javaPrinterTestSuite;

import static org.junit.Assert.*;

import java.io.InputStream;

import org.junit.Test;

import cc.kave.commons.model.events.completionevents.Context;
import cc.kave.commons.model.ssts.ISST;
import cc.kave.commons.utils.json.JsonUtils;
import exec.recommender_reimplementation.java_printer.JavaPrintingContext;
import exec.recommender_reimplementation.java_printer.JavaPrintingVisitor;

public class ValueTypeTest {

	// tests translating value types and UnknownExpression resolving
	@Test
	public void valueTypeIntegrationTest() {
		Context c = getTestSST();
		JavaPrintingContext context = new JavaPrintingContext();
		ISST sst = c.getSST();
		sst.accept(new JavaPrintingVisitor(sst), context);
		String expected = String.join("\n", "class ValueTypeTest", "{", "    void M1()", "    {", "        byte a;", "        a = 0;",
				"        byte b;", "        b = 0;", "        short c;", "        c = 0;", "        short d;", "        d = 0;", "        int e;", "        e = 0;",
				"        int f;", "        f = 0;", "        long g;", "        g = 0;", "        long h;", "        h = 0;", "        float i;", "        i = 0.0f;",
				"        double j;", "        j = 0.0d;", "        boolean k;", "        k = false;", "        char l;", "        l = '.';", "        double m;",
				"        m = 0.0d;", "    }", "}");
		String actual = context.toString();
		System.out.print(actual);
		assertEquals(expected, actual);
	}
	
	private Context getTestSST() {
		InputStream resource = getClass().getResourceAsStream("./ValueTypeTest.json");
		return JsonUtils.fromJson(resource, Context.class);
	}

}
