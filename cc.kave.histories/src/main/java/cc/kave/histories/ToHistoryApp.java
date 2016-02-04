/**
 * Copyright 2016 Technische Universität Darmstadt
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
