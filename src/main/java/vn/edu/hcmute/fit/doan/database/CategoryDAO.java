package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.Category;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class CategoryDAO {

    private static <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
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
            throw new RuntimeException("Lỗi giao dịch khi tương tác với CategoryDAO", e);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Các phương thức CRUD cho Category
    public void addCategory(Category category) {
        executeTransaction(em -> em.persist(category));
        System.out.println("Đã thêm danh mục: " + category.getCategoryName());
    }

    public static Category findCategoryById(int id) {
        return executeWithEntityManager(em -> em.find(Category.class, id));
    }

    public static List<Category> findAllCategories() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT c FROM Category c", Category.class).getResultList()
        );
    }
    // Thêm vào CategoryDAO.java
    public List<Category> findRootCategories() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT c FROM Category c WHERE c.parentCategory IS NULL", Category.class)
                        .getResultList()
        );
    }

    public void updateCategory(Category category) {
        executeTransaction(em -> em.merge(category));
        System.out.println("Đã cập nhật danh mục: " + category.getCategoryName());
    }

    public void deleteCategory(int id) {
        executeTransaction(em -> {
            Category category = em.find(Category.class, id);
            if (category != null) {
                em.remove(category);
                System.out.println("Đã xóa danh mục với ID: " + id);
            }
        });
    }
}