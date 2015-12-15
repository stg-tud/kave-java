package cc.kave.histories.test.database;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import cc.kave.histories.model.OUHistory;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cc.kave.histories.database.SqliteHistoryDatabase;
import cc.kave.histories.model.OUSnapshot;
import cc.kave.histories.model.SSTSnapshot;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.junit.Assert.assertThat;

public class SqliteHistoryDatabaseTest {

    private SqliteHistoryDatabase database;
    private OUHistory history1;
    private OUHistory history2;

    private EntityManager em;
    private EntityManagerFactory emf;
    private OUSnapshot snapshot1;
    private OUSnapshot snapshot2;
    private OUSnapshot snapshot3;

    @Before
    public void setUp() throws Exception {
        emf = Persistence.createEntityManagerFactory("test-histories");
        em = emf.createEntityManager();

        snapshot1 = new OUSnapshot();
        snapshot2 = new OUSnapshot();
        snapshot3 = new OUSnapshot();

        em.getTransaction().begin();
        em.persist(snapshot1);
        em.persist(snapshot2);
        em.persist(snapshot3);
        em.getTransaction().commit();

        history1 = new OUHistory();
        history1.addSnapshot(snapshot1);

        history2 = new OUHistory();
        history2.addSnapshot(snapshot2);
        history2.addSnapshot(snapshot3);

        database = new SqliteHistoryDatabase("test-histories");
    }

    @After
    public void tearDown() throws Exception {
        database.close();
        em.clear();
        em.close();
        emf.close();
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
        database.insertHistory(history1);

        Set<OUHistory> histories = database.getHistories();
        assertThat(histories, contains(history1));
    }

    @Test
    public void persistsHistories() throws Exception {
        database.insertHistories(Arrays.asList(history1, history2));

        Set<OUHistory> histories = database.getHistories();
        assertThat(histories, containsInAnyOrder(history1, history2));
    }
}
