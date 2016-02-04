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
package cc.kave.histories;

import cc.kave.histories.model.OUHistory;
import cc.kave.histories.model.OUSnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class HistoryDetector {

    private Map<String, OUHistory> histories = new HashMap<>();

    public void process(OUSnapshot snapshot) {
        String id = getHistoryId(snapshot);
        if (!histories.containsKey(id)) {
            histories.put(id, new OUHistory());
        }
        histories.get(id).addSnapshot(snapshot);
    }

    private String getHistoryId(OUSnapshot snapshot) {
        return String.format("%1$s-%2$tY%2$tm%2$td-%3$s-%4$s",
                snapshot.getWorkPeriod(),
                snapshot.getTimestamp(),
                snapshot.getEnclosingMethod(),
                snapshot.getTargetType());
    }

    public Set<OUHistory> getDetectedHistories() {
        return histories.values().stream()
                .filter(ss -> ss.size() > 1)
                .collect(Collectors.toSet());
    }
}
