package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.PasswordResetToken;
import java.util.function.Consumer;
import java.util.function.Function;

public class PasswordResetTokenDAO {

    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        EntityManager em = null;
        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            return operation.apply(em);
        } finally {
            if (em != null) em.close();
        }
    }

    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityManager em = null;
        EntityTransaction tx = null;
        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            operation.accept(em);
            tx.commit();
        } catch (Exception e) {
            if (tx != null && tx.isActive()) tx.rollback();
            e.printStackTrace();
            throw new RuntimeException("Lỗi transaction trong PasswordResetTokenDAO", e);
        } finally {
            if (em != null) em.close();
        }
    }

    public void createToken(PasswordResetToken token) {
        executeTransaction(em -> em.persist(token));
    }

    public PasswordResetToken findByToken(String token) {
        try {
            return executeWithEntityManager(em ->
                    em.createQuery("SELECT t FROM PasswordResetToken t JOIN FETCH t.user WHERE t.token = :token", PasswordResetToken.class)
                            .setParameter("token", token)
                            .getSingleResult()
            );
        } catch (jakarta.persistence.NoResultException e) {
            return null;
        }
    }

    public void deleteToken(PasswordResetToken token) {
        executeTransaction(em -> {
            PasswordResetToken managedToken = token;
            if (!em.contains(token)) {
                managedToken = em.merge(token);
            }
            em.remove(managedToken);
        });
    }
}