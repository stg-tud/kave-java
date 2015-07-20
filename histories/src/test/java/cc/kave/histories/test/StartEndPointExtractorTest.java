package cc.kave.histories.test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.*;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.Test;

import cc.kave.histories.model.OUSnapshot;

public class StartEndPointExtractorTest {

    @Test
    public void createsNoPairIfNoSnapshots() throws Exception {
        StartEndPointExtractor uut = new StartEndPointExtractor();

        Set<History> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void createsNoPairFromOneSnapshot() throws Exception {
        StartEndPointExtractor uut = new StartEndPointExtractor();

        uut.process(new OUSnapshot());
        Set<History> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void createsAPairFromTwoSnapshots() {
        OUSnapshot s1 = new OUSnapshot();
        OUSnapshot s2 = new OUSnapshot();

        StartEndPointExtractor uut = new StartEndPointExtractor();
        uut.process(s1);
        uut.process(s2);
        Set<History> actuals = uut.getDetectedPairs();

        History expected = history(s1, s2);
        assertThat(actuals, contains(expected));
    }

    @Test
    public void createPairFromFirstAndLastSnapshot() throws Exception {
        OUSnapshot s1 = new OUSnapshot();
        OUSnapshot s2 = new OUSnapshot();
        OUSnapshot s3 = new OUSnapshot();

        StartEndPointExtractor uut = new StartEndPointExtractor();
        uut.process(s1);
        uut.process(s2);
        uut.process(s3);
        Set<History> actuals = uut.getDetectedPairs();

        History expected = history(s1, s2, s3);
        assertThat(actuals, contains(expected));
    }

    @Test
    public void separatesSnapshotsByWorkPeriod() throws Exception {
        OUSnapshot s1 = new OUSnapshot("1", null, null, null, null, false);
        OUSnapshot s2 = new OUSnapshot("2", null, null, null, null, false);

        StartEndPointExtractor uut = new StartEndPointExtractor();
        uut.process(s1);
        uut.process(s2);
        Set<History> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void separatesSnapshotsByEnclosingMethod() throws Exception {
        OUSnapshot s1 = new OUSnapshot("a", null, "M1", null, null, false);
        OUSnapshot s2 = new OUSnapshot("a", null, "M2", null, null, false);

        StartEndPointExtractor uut = new StartEndPointExtractor();
        uut.process(s1);
        uut.process(s2);
        Set<History> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    private History history(OUSnapshot... snapshots){
        History history = new History();
        history.addAll(Arrays.asList(snapshots));
        return history;
    }

    private static class StartEndPointExtractor {

        private Map<String, History> histories = new HashMap<>();

        public void process(OUSnapshot snapshot) {
            String id = getHistoryId(snapshot);
            if (!histories.containsKey(id)) {
                histories.put(id, new History());
            }
            histories.get(id).add(snapshot);
        }

        private String getHistoryId(OUSnapshot snapshot) {
            return snapshot.getWorkPeriod() + snapshot.getEnclosingMethod();
        }

        public Set<History> getDetectedPairs() {
            return histories.values().stream()
                    .filter(ss -> ss.size() > 1)
                    .collect(Collectors.toSet());
        }
    }

}
