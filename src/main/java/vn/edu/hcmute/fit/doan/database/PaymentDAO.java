package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.Payment;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class PaymentDAO {

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
            throw new RuntimeException("Lỗi giao dịch khi tương tác với PaymentDAO", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Các phương thức CRUD cho Payment
    public void addPayment(Payment payment) {
        executeTransaction(em -> em.persist(payment));
        System.out.println("Đã thêm giao dịch thanh toán mới với ID: " + payment.getPaymentId());
    }
    public List<Payment> findByCustomerId(int customerId) {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT p FROM Payment p WHERE p.order.customer.id = :customerId ORDER BY p.paymentDate DESC", Payment.class)
                        .setParameter("customerId", customerId)
                        .getResultList()
        );
    }

    public List<Payment> findAll() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT p FROM Payment p ORDER BY p.paymentDate DESC", Payment.class).getResultList()
        );
    }

    public Payment findPaymentById(String id) {
        return executeWithEntityManager(em -> em.find(Payment.class, id));
    }

    public void updatePayment(Payment payment) {
        executeTransaction(em -> em.merge(payment));
        System.out.println("Đã cập nhật trạng thái thanh toán: " + payment.getStatus());
    }

    public void deletePayment(String id) {
        executeTransaction(em -> {
            Payment payment = em.find(Payment.class, id);
            if (payment != null) {
                em.remove(payment);
                System.out.println("Đã xóa giao dịch với ID: " + id);
            }
        });
    }
}