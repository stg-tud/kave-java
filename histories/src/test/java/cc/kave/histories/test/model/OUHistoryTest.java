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
