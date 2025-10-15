//package vn.edu.hcmute.fit.doan.database;
//
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityTransaction;
//import vn.edu.hcmute.fit.doan.Cart;
//
//import java.util.function.Consumer;
//import java.util.function.Function;
//
//public class CartDAO {
//
//    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
//        EntityManager em = null;
//        try {
//            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
//            return operation.apply(em);
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
//    }
//
//    private void executeTransaction(Consumer<EntityManager> operation) {
//        EntityManager em = null;
//        EntityTransaction transaction = null;
//        try {
//            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
//            transaction = em.getTransaction();
//            transaction.begin();
//            operation.accept(em);
//            transaction.commit();
//        } catch (Exception e) {
//            if (transaction != null && transaction.isActive()) {
//                transaction.rollback();
//            }
//            e.printStackTrace();
//            throw new RuntimeException("Lỗi giao dịch khi tương tác với CartDAO", e);
//        } finally {
//            if (em != null) {
//                em.close();
//            }
//        }
//    }
//
//    // === Các phương thức CRUD ===
//
//    public void addCart(Cart cart) {
//        executeTransaction(em -> em.persist(cart));
//        System.out.println("Đã tạo giỏ hàng mới với ID: " + cart.getCartId());
//    }
//
//    public Cart findCartById(String id) {
//        return executeWithEntityManager(em -> em.find(Cart.class, id));
//    }
//    public Cart findCartByCustomerId(int customerId) {
//        try {
//            return executeWithEntityManager(em ->
//                    em.createQuery("SELECT c FROM Cart c WHERE c.customer.id = :customerId", Cart.class)
//                            .setParameter("customerId", customerId)
//                            .getSingleResult()
//            );
//        } catch (jakarta.persistence.NoResultException e) {
//            return null; // Không tìm thấy giỏ hàng
//        }
//    }
//    // Trong file CartDAO.java
//    public void updateCart(Cart cart) {
//        executeTransaction(em -> em.merge(cart));
//    }
//
//
//    public void deleteCart(String id) {
//        executeTransaction(em -> {
//            Cart cart = em.find(Cart.class, id);
//            if (cart != null) {
//                em.remove(cart);
//                System.out.println("Đã xóa giỏ hàng với ID: " + id);
//            }
//        });
//    }
//}
package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.Cart;

import java.util.function.Consumer;
import java.util.function.Function;

public class CartDAO {

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
            throw new RuntimeException("Lỗi giao dịch khi tương tác với CartDAO", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // === Các phương thức CRUD ===

    public void addCart(Cart cart) {
        executeTransaction(em -> em.persist(cart));
        System.out.println("Đã tạo giỏ hàng mới với ID: " + cart.getCartId());
    }

    public Cart findCartById(String id) {
        return executeWithEntityManager(em -> em.find(Cart.class, id));
    }
    public Cart findCartByCustomerId(int customerId) {
        try {
            return executeWithEntityManager(em ->
                    em.createQuery("SELECT c FROM Cart c WHERE c.customer.id = :customerId", Cart.class)
                            .setParameter("customerId", customerId)
                            .getSingleResult()
            );
        } catch (jakarta.persistence.NoResultException e) {
            return null; // Không tìm thấy giỏ hàng
        }
    }
    // Trong file CartDAO.java
    public void updateCart(Cart cart) {
        executeTransaction(em -> em.merge(cart));
    }


    public void deleteCart(String id) {
        executeTransaction(em -> {
            Cart cart = em.find(Cart.class, id);
            if (cart != null) {
                em.remove(cart);
                System.out.println("Đã xóa giỏ hàng với ID: " + id);
            }
        });
    }

    // Thêm phương thức deleteCart với int id
    public void deleteCart(int id) {
        executeTransaction(em -> {
            Cart cart = em.find(Cart.class, id);
            if (cart != null) {
                em.remove(cart);
                System.out.println("Đã xóa giỏ hàng với ID: " + id);
            } else {
                System.out.println("⚠️ Không tìm thấy giỏ hàng với ID: " + id + " để xóa");
            }
        });
    }
}