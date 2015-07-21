package cc.kave.histories;

import cc.kave.histories.database.SqliteHistoryDatabase;

import java.io.IOException;
import java.sql.SQLException;

public class ToHistoryApp {

    public static void main(String[] args) throws SQLException, IOException {
        try(SqliteHistoryDatabase database = new SqliteHistoryDatabase("histories")) {
            HistoryDetector detector = new HistoryDetector();
            database.getOUSnapshots().stream()
                    .forEach(snapshot -> detector.process(snapshot));
            database.insertHistories(detector.getDetectedHistories());
        }
    }
}
