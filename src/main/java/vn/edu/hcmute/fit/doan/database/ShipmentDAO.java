package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.Shipment;

import java.util.function.Consumer;
import java.util.function.Function;

public class ShipmentDAO {
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
            throw new RuntimeException("Lỗi giao dịch khi tương tác với ShipmentDAO", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Các phương thức CRUD cho Shipment
    public void addShipment(Shipment shipment) {
        executeTransaction(em -> em.persist(shipment));
        System.out.println("Đã thêm lô hàng mới với ID: " + shipment.getShipmentId());
    }

    public Shipment findShipmentById(String id) {
        return executeWithEntityManager(em -> em.find(Shipment.class, id));
    }

    public void updateShipment(Shipment shipment) {
        executeTransaction(em -> em.merge(shipment));
        System.out.println("Đã cập nhật trạng thái lô hàng: " + shipment.getStatus());
    }

    public void deleteShipment(String id) {
        executeTransaction(em -> {
            Shipment shipment = em.find(Shipment.class, id);
            if (shipment != null) {
                em.remove(shipment);
                System.out.println("Đã xóa lô hàng với ID: " + id);
            }
        });
    }
}