package cc.kave.histories.database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

import cc.kave.histories.model.OUHistory;
import cc.kave.histories.model.OUSnapshot;
import cc.kave.histories.model.SSTSnapshot;

public class SqliteHistoryDatabase implements Closeable {

    private EntityManagerFactory emf;
    private EntityManager em;

    public SqliteHistoryDatabase(String persistenceUnitName) throws SQLException {
        emf = Persistence.createEntityManagerFactory(persistenceUnitName);
        em = emf.createEntityManager();
    }

    @SuppressWarnings("unchecked")
    public List<SSTSnapshot> getSSTHistories() {
        Query q = em.createQuery("SELECT s FROM SSTSnapshot s ORDER BY s.timestamp ASC");
        return (List<SSTSnapshot>) q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<OUSnapshot> getOUHistories() {
        Query q = em.createQuery("SELECT s FROM OUSnapshot s ORDER BY s.timestamp ASC");
        return (List<OUSnapshot>) q.getResultList();
    }

    public void insertHistory(OUHistory history) {
		em.getTransaction().begin();
        em.persist(history);
        em.getTransaction().commit();
    }

    public List<OUHistory> getHistories() {
        Query q = em.createQuery("SELECT h FROM OUHistory h");
        return (List<OUHistory>) q.getResultList();
    }

    @Override
    public void close() throws IOException {
        em.close();
        emf.close();
    }
}
