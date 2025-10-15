package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.Notification;

import java.util.function.Consumer;
import java.util.function.Function;

public class NotificationDAO {

    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        EntityManager em = null;
        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            return operation.apply(em);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityManager em = null;
        EntityTransaction transaction = null;
        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            transaction = em.getTransaction();
            transaction.begin();
            operation.accept(em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi giao dịch khi tương tác với NotificationDAO", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Các phương thức CRUD cho Notification
    public void addNotification(Notification notification) {
        executeTransaction(em -> em.persist(notification));
        System.out.println("Đã thêm thông báo mới cho người dùng: " + notification.getUser().getId());
    }

    public Notification findNotificationById(int id) {
        return executeWithEntityManager(em -> em.find(Notification.class, id));
    }

    public void deleteNotification(int id) {
        executeTransaction(em -> {
            Notification notification = em.find(Notification.class, id);
            if (notification != null) {
                em.remove(notification);
                System.out.println("Đã xóa thông báo với ID: " + id);
            }
        });
    }
}