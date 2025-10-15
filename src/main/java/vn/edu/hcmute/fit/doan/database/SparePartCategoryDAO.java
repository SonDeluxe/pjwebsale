// src/main/java/vn/edu/hcmute/fit/doan/database/SparePartCategoryDAO.java
package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.SparePartCategory;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class SparePartCategoryDAO {
    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        try {
            return operation.apply(em);
        } finally {
            em.close();
        }
    }

    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();
        try {
            tx.begin();
            operation.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
        }
    }

    public void add(SparePartCategory category) {
        executeTransaction(em -> em.persist(category));
    }

    public void update(SparePartCategory category) {
        executeTransaction(em -> em.merge(category));
    }

    public void delete(int id) {
        executeTransaction(em -> {
            SparePartCategory category = em.find(SparePartCategory.class, id);
            if (category != null) {
                em.remove(category);
            }
        });
    }

    public SparePartCategory findById(int id) {
        return executeWithEntityManager(em -> em.find(SparePartCategory.class, id));
    }

    public List<SparePartCategory> findAll() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT c FROM SparePartCategory c ORDER BY c.name", SparePartCategory.class).getResultList()
        );
    }
}