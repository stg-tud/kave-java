package cc.kave.histories.test;

import static org.junit.Assert.assertThat;

import static org.hamcrest.Matchers.*;

import java.util.*;

import cc.kave.histories.HistoryDetector;
import cc.kave.histories.model.OUHistory;
import org.junit.Test;

import cc.kave.histories.model.OUSnapshot;

public class HistoryDetectorTest {

    @Test
    public void createsNoPairIfNoSnapshots() throws Exception {
        HistoryDetector uut = new HistoryDetector();

        Set<OUHistory> actuals = uut.getDetectedHistories();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void createsNoPairFromOneSnapshot() throws Exception {
        HistoryDetector uut = new HistoryDetector();

        uut.process(new OUSnapshot());
        Set<OUHistory> actuals = uut.getDetectedHistories();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void createsAPairFromTwoSnapshots() {
        OUSnapshot s1 = new OUSnapshot();
        OUSnapshot s2 = new OUSnapshot();

        HistoryDetector uut = new HistoryDetector();
        uut.process(s1);
        uut.process(s2);
        Set<OUHistory> actuals = uut.getDetectedHistories();

        OUHistory expected = history(s1, s2);
        assertThat(actuals, contains(expected));
    }

    @Test
    public void createPairFromFirstAndLastSnapshot() throws Exception {
        OUSnapshot s1 = new OUSnapshot();
        OUSnapshot s2 = new OUSnapshot();
        OUSnapshot s3 = new OUSnapshot();

        HistoryDetector uut = new HistoryDetector();
        uut.process(s1);
        uut.process(s2);
        uut.process(s3);
        Set<OUHistory> actuals = uut.getDetectedHistories();

        OUHistory expected = history(s1, s2, s3);
        assertThat(actuals, contains(expected));
    }

    @Test
    public void separatesSnapshotsByWorkPeriod() throws Exception {
        OUSnapshot s1 = new OUSnapshot("1", null, null, null, null, false);
        OUSnapshot s2 = new OUSnapshot("2", null, null, null, null, false);

        HistoryDetector uut = new HistoryDetector();
        uut.process(s1);
        uut.process(s2);
        Set<OUHistory> actuals = uut.getDetectedHistories();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void separatesSnapshotsByDay() throws Exception {
        Calendar calendar = Calendar.getInstance();
        calendar.set(2015, 7, 14, 10, 23, 42);
        Date t1 = calendar.getTime();
        calendar.add(Calendar.DAY_OF_MONTH, 1);
        Date t2 = calendar.getTime();

        OUSnapshot s1 = new OUSnapshot("1", t1, "M", null, null, false);
        OUSnapshot s2 = new OUSnapshot("1", t2, "M", null, null, false);

        HistoryDetector uut = new HistoryDetector();
        uut.process(s1);
        uut.process(s2);
        Set<OUHistory> actuals = uut.getDetectedHistories();

        assertThat(actuals, is(empty()));
    }

    @Test
    public void separatesSnapshotsByEnclosingMethod() throws Exception {
        OUSnapshot s1 = new OUSnapshot("a", null, "M1", null, null, false);
        OUSnapshot s2 = new OUSnapshot("a", null, "M2", null, null, false);

        HistoryDetector uut = new HistoryDetector();
        uut.process(s1);
        uut.process(s2);
        Set<OUHistory> actuals = uut.getDetectedHistories();

        assertThat(actuals, is(empty()));
    }

    private OUHistory history(OUSnapshot... snapshots) {
        OUHistory history = new OUHistory();
        history.addAll(Arrays.asList(snapshots));
        return history;
    }

}
