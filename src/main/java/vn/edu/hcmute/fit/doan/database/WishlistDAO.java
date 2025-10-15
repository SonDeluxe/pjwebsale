package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import vn.edu.hcmute.fit.doan.Wishlist;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class WishlistDAO {

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
            throw new RuntimeException("Lỗi giao dịch khi tương tác với WishlistDAO", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Các phương thức CRUD cho Wishlist

    public void addWishlist(Wishlist wishlist) {
        executeTransaction(em -> em.persist(wishlist));
        System.out.println("Đã thêm danh sách yêu thích mới.");
    }

    public Wishlist findWishlistById(int id) {
        return executeWithEntityManager(em -> em.find(Wishlist.class, id));
    }

    // Phương thức tìm Wishlist theo customerId
    public Wishlist findWishlistByCustomerId(int customerId) {
        try {
            return executeWithEntityManager(em ->
                    em.createQuery("SELECT w FROM Wishlist w WHERE w.customer.id = :customerId", Wishlist.class)
                            .setParameter("customerId", customerId)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return null; // Không tìm thấy wishlist cho khách hàng này
        }
    }

    // Phương thức mới: Tìm Wishlist theo userId (cho WishlistServlet)
    public Wishlist findWishlistByUserId(int userId) {
        try {
            return executeWithEntityManager(em ->
                    em.createQuery("SELECT w FROM Wishlist w WHERE w.customer.id = :userId", Wishlist.class)
                            .setParameter("userId", userId)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return null; // Không tìm thấy wishlist cho user này
        }
    }

    // Phương thức tìm tất cả Wishlist theo userId
    public List<Wishlist> findAllWishlistsByUserId(int userId) {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT w FROM Wishlist w WHERE w.customer.id = :userId", Wishlist.class)
                        .setParameter("userId", userId)
                        .getResultList()
        );
    }

    public void deleteWishlist(int id) {
        executeTransaction(em -> {
            Wishlist wishlist = em.find(Wishlist.class, id);
            if (wishlist != null) {
                em.remove(wishlist);
                System.out.println("Đã xóa danh sách yêu thích với ID: " + id);
            }
        });
    }
}