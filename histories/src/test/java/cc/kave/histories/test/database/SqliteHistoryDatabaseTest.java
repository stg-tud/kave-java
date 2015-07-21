package cc.kave.histories.test.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import cc.kave.histories.model.OUHistory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.kave.histories.database.SqliteHistoryDatabase;
import cc.kave.histories.model.OUSnapshot;
import cc.kave.histories.model.SSTSnapshot;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class SqliteHistoryDatabaseTest {

    private SqliteHistoryDatabase database;

    @Before
    public void setUp() throws Exception {
        database = new SqliteHistoryDatabase("test-histories");
    }

    @After
    public void tearDown() throws Exception {
        database.close();
    }

    @Test
    public void loadsSnapshots() throws SQLException, IOException {
        List<SSTSnapshot> histories = database.getSSTSnapshots();
        System.out.println(histories.size());
        List<OUSnapshot> histories2 = database.getOUSnapshots();
        System.out.println(histories2.size());
    }

    @Test
    public void persistsHistory() throws Exception {
        OUHistory history = new OUHistory();
        history.add(new OUSnapshot());
        history.add(new OUSnapshot());
        history.add(new OUSnapshot());

        database.insertHistory(history);

        List<OUHistory> histories = database.getHistories();
        assertThat(histories, contains(history));
    }

    @Test
    public void persistsHistories() throws Exception {
        OUHistory history1 = new OUHistory();
        history1.add(new OUSnapshot());
        history1.add(new OUSnapshot());

        OUHistory history2 = new OUHistory();
        history2.add(new OUSnapshot());

        database.insertHistories(Arrays.asList(history1, history2));

        List<OUHistory> histories = database.getHistories();
        assertThat(histories, containsInAnyOrder(history1, history2));

    }
}
