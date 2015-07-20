package cc.kave.histories.test.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

import cc.kave.histories.database.SqliteHistoryDatabase;
import cc.kave.histories.model.OUSnapshot;
import cc.kave.histories.model.SSTSnapshot;

public class SqliteHistoryDatabaseTest {

	@Test
	public void loadsHistories() throws SQLException, IOException {
		try (SqliteHistoryDatabase database = new SqliteHistoryDatabase()){
			List<SSTSnapshot> histories = database.getSSTHistories();
			System.out.println(histories.size());
			List<OUSnapshot> histories2 = database.getOUHistories();
			System.out.println(histories2.size());
		}
	}
}
