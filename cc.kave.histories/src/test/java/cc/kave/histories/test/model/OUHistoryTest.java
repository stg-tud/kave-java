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
package cc.kave.histories.test.model;

import cc.kave.histories.model.OUHistory;
import cc.kave.histories.model.OUSnapshot;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class OUHistoryTest {
    @Test
    public void returnsEnclosingMethod() throws Exception {
        OUHistory history = new OUHistory();
        history.addSnapshot(new OUSnapshot(null, null, "EM", null, null, false));

        assertThat(history.getEnclosingMethod(), is("EM"));
    }

    @Test
    public void returnsFirstSnapshot() throws Exception {
        OUSnapshot first = new OUSnapshot();
        OUHistory history = new OUHistory();
        history.addSnapshot(first);

        assertThat(history.getStart(), is(first));
    }

    @Test
    public void returnsLastSnapshot() throws Exception {
        OUSnapshot last = new OUSnapshot();
        OUHistory history = new OUHistory();
        history.addSnapshot(last);

        assertThat(history.getEnd(), is(last));
    }
}
