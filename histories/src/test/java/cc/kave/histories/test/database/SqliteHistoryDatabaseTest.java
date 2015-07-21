package cc.kave.histories.test.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import cc.kave.histories.model.OUHistory;
import org.junit.Ignore;
import org.junit.Test;

import cc.kave.histories.database.SqliteHistoryDatabase;
import cc.kave.histories.model.OUSnapshot;
import cc.kave.histories.model.SSTSnapshot;

import static org.hamcrest.Matchers.contains;
import static org.junit.Assert.assertThat;

public class SqliteHistoryDatabaseTest {

	@Test
	public void loadsHistories() throws SQLException, IOException {
		try (SqliteHistoryDatabase database = new SqliteHistoryDatabase("test-histories")){
			List<SSTSnapshot> histories = database.getSSTHistories();
			System.out.println(histories.size());
			List<OUSnapshot> histories2 = database.getOUHistories();
			System.out.println(histories2.size());
		}
	}

    @Test
    public void persistsHistory() throws Exception {
        OUHistory history = new OUHistory();
        history.add(new OUSnapshot());
        history.add(new OUSnapshot());
        history.add(new OUSnapshot());

        SqliteHistoryDatabase database = new SqliteHistoryDatabase("test-histories");
        database.insertHistory(history);

        List<OUHistory> histories = database.getHistories();
        assertThat(histories, contains(history));
    }
}
