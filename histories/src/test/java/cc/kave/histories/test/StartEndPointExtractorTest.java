package cc.kave.histories.test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.*;

import java.time.Instant;
import java.util.*;

import org.junit.Test;

import cc.kave.histories.model.OUSnapshot;

public class StartEndPointExtractorTest {

    @Test
    public void createsNoPairIfNoSnapshots() throws Exception {
        StartEndPointExtractor uut = new StartEndPointExtractor();

        List<StatePair> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void createsNoPairFromOneSnapshot() throws Exception {
        StartEndPointExtractor uut = new StartEndPointExtractor();

        uut.process(new OUSnapshot());
        List<StatePair> actuals = uut.getDetectedPairs();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void createsAPairFromTwoSnapshots() {
        OUSnapshot s1 = new OUSnapshot();
        OUSnapshot s2 = new OUSnapshot();

        StartEndPointExtractor uut = new StartEndPointExtractor();
        uut.process(s1);
        uut.process(s2);
        List<StatePair> actuals = uut.getDetectedPairs();

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
        List<StatePair> actuals = uut.getDetectedPairs();

        StatePair expected = new StatePair(s1, s3);
        assertThat(actuals, contains(expected));
    }

    private static class StartEndPointExtractor {

        private OUSnapshot start;
        private OUSnapshot end;

        public void process(OUSnapshot snapshot) {
            if (start == null) {
                start = snapshot;
            } else {
                end = snapshot;
            }
        }

        public List<StatePair> getDetectedPairs() {
            ArrayList<StatePair> pairs = new ArrayList<>();
            if (start != null && end != null){
                pairs.add(new StatePair(start, end));
            }
            return pairs;
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

        public OUSnapshot getStart() {
            return start;
        }

        public OUSnapshot getEnd() {
            return end;
        }

        public void setStart(OUSnapshot start) {
            this.start = start;
        }

        public void setEnd(OUSnapshot end) {
            this.end = end;
        }

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
