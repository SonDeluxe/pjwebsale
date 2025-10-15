package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.RepairSupportCenter;

import java.util.function.Consumer;
import java.util.function.Function;

public class RepairSupportCenterDAO {
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
            throw new RuntimeException("Lỗi giao dịch khi tương tác với RepairSupportCenterDAO", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Các phương thức CRUD cho RepairSupportCenter
    public void addRepairRequest(RepairSupportCenter request) {
        executeTransaction(em -> em.persist(request));
        System.out.println("Đã thêm yêu cầu sửa chữa mới.");
    }

    public RepairSupportCenter findRepairRequestById(String id) {
        return executeWithEntityManager(em -> em.find(RepairSupportCenter.class, id));
    }

    public void updateRepairRequest(RepairSupportCenter request) {
        executeTransaction(em -> em.merge(request));
        System.out.println("Đã cập nhật yêu cầu sửa chữa.");
    }

    public void deleteRepairRequest(String id) {
        executeTransaction(em -> {
            RepairSupportCenter request = em.find(RepairSupportCenter.class, id);
            if (request != null) {
                em.remove(request);
                System.out.println("Đã xóa yêu cầu sửa chữa với ID: " + id);
            }
        });
    }
}