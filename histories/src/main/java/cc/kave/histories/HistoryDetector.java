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
        histories.get(id).add(snapshot);
    }

    private String getHistoryId(OUSnapshot snapshot) {
        return String.format("%1$s-%2$tY%2$tm%2$td-%3$s",
                snapshot.getWorkPeriod(),
                snapshot.getTimestamp(),
                snapshot.getEnclosingMethod());
    }

    public Set<OUHistory> getDetectedHistories() {
        return histories.values().stream()
                .filter(ss -> ss.size() > 1)
                .collect(Collectors.toSet());
    }
}
