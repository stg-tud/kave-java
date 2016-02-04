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
package cc.kave.histories.database;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.StreamSupport;

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
    public List<SSTSnapshot> getSSTSnapshots() {
        Query q = em.createQuery("SELECT s FROM SSTSnapshot s ORDER BY s.timestamp ASC");
        return (List<SSTSnapshot>) q.getResultList();
    }

    @SuppressWarnings("unchecked")
    public List<OUSnapshot> getOUSnapshots() {
        Query q = em.createQuery("SELECT s FROM OUSnapshot s ORDER BY s.timestamp ASC");
        return (List<OUSnapshot>) q.getResultList();
    }

    public void insertHistory(OUHistory history) {
        em.getTransaction().begin();
        em.persist(history);
        em.getTransaction().commit();
    }

    public void insertHistories(Iterable<OUHistory> histories) {
        em.getTransaction().begin();
        StreamSupport.stream(histories.spliterator(), false).forEach(history -> em.persist(history));
        em.getTransaction().commit();
    }

    public Set<OUHistory> getHistories() {
        Query q = em.createQuery("SELECT h FROM OUHistory h");
        return new HashSet<>((List<OUHistory>) q.getResultList());
    }

    @Override
    public void close() throws IOException {
        em.close();
        emf.close();
    }
}
