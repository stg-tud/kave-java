package cc.kave.histories.test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.*;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import cc.kave.histories.model.OUSnapshot;

public class StartEndPointExtractorTest {

    @Test
    public void createsNoPairIfNoSnapshots() throws Exception {
        StartEndPointExtractor uut = new StartEndPointExtractor();

        Set<StatePair> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void createsNoPairFromOneSnapshot() throws Exception {
        StartEndPointExtractor uut = new StartEndPointExtractor();

        uut.process(new OUSnapshot());
        Set<StatePair> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void createsAPairFromTwoSnapshots() {
        OUSnapshot s1 = new OUSnapshot();
        OUSnapshot s2 = new OUSnapshot();

        StartEndPointExtractor uut = new StartEndPointExtractor();
        uut.process(s1);
        uut.process(s2);
        Set<StatePair> actuals = uut.getDetectedPairs();

        StatePair expected = new StatePair(s1, s2);
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
        Set<StatePair> actuals = uut.getDetectedPairs();

        StatePair expected = new StatePair(s1, s3);
        assertThat(actuals, contains(expected));
    }

    @Test
    public void separatesSnapshotsByWorkPeriod() throws Exception {
        OUSnapshot s1 = new OUSnapshot("1", null, null, null, null, false);
        OUSnapshot s2 = new OUSnapshot("2", null, null, null, null, false);

        StartEndPointExtractor uut = new StartEndPointExtractor();
        uut.process(s1);
        uut.process(s2);
        Set<StatePair> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    private static class StartEndPointExtractor {

        private Map<String, List<OUSnapshot>> histories = new HashMap<>();

        public void process(OUSnapshot snapshot) {
            String workPeriod = snapshot.getWorkPeriod();
            if (!histories.containsKey(workPeriod)) {
                histories.put(workPeriod, new ArrayList<>());
            }
            histories.get(workPeriod).add(snapshot);
        }

        public Set<StatePair> getDetectedPairs() {
            return histories.values().stream()
                    .filter(ss -> ss.size() > 1)
                    .map(ss -> new StatePair(ss.get(0), ss.get(ss.size() - 1)))
                    .collect(Collectors.toSet());
        }

    }

    private static class StatePair {

        public StatePair(OUSnapshot start, OUSnapshot end) {
            super();
            this.start = start;
            this.end = end;
        }

        private OUSnapshot start;
        private OUSnapshot end;

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            StatePair statePair = (StatePair) o;
            return Objects.equals(start, statePair.start) &&
                    Objects.equals(end, statePair.end);
        }

        @Override
        public int hashCode() {
            return Objects.hash(start, end);
        }
    }
}
