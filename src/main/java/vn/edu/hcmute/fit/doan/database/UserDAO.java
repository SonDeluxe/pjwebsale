package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import vn.edu.hcmute.fit.doan.User;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class UserDAO {

    // Phương thức tiện ích để xử lý các thao tác truy cập dữ liệu
    private <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        try {
            return operation.apply(em);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // Phương thức tiện ích để thực hiện các giao dịch (transaction)
    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            operation.accept(em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw e; // Ném lại ngoại lệ để lớp gọi biết rằng có lỗi xảy ra
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    // === Các phương thức CRUD đã được refactor ===

    public void addUser(User user) {
        executeTransaction(em -> em.persist(user));
        System.out.println("Đã thêm người dùng mới: " + user.getName());
    }

    public User findUserById(int id) {
        return executeWithEntityManager(em -> em.find(User.class, id));
    }

    public User findUserByUsername(String username) {
        try {
            return executeWithEntityManager(em ->
                    em.createQuery("SELECT u FROM User u WHERE u.username = :username", User.class)
                            .setParameter("username", username)
                            .getSingleResult()
            );
        } catch (NoResultException e) {
            return null;
        }
    }
    public List<User> findUsersByEmailOrPhone(String identifier) {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT u FROM User u WHERE u.email = :identifier OR u.phone = :identifier", User.class)
                        .setParameter("identifier", identifier)
                        .getResultList()
        );
    }

    public List<User> getAllUsers() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT u FROM User u", User.class).getResultList()
        );
    }




    public void updateUser(User user) {
        executeTransaction(em -> {
            User managed = em.find(User.class, user.getId());
            if (managed != null) {
                managed.setEmail(user.getEmail());
                managed.setAddress(user.getAddress());
                managed.setPhone(user.getPhone());

                if (user.getPassword() != null && !user.getPassword().isEmpty()) {
                    managed.setPassword(user.getPassword());
                }

                // ✅ Cập nhật vai trò nếu có thay đổi
                if (user.getRole() != null && !user.getRole().isEmpty()) {
                    managed.setRole(user.getRole());
                }

                em.flush(); // Ghi xuống DB ngay
            }
        });
        System.out.println("✅ Đã cập nhật người dùng ID: " + user.getId());
    }




    public long countAll() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT COUNT(u) FROM User u", Long.class).getSingleResult()
        );
    }

    public void deleteUser(int id) {
        executeTransaction(em -> {
            User user = em.find(User.class, id);
            if (user != null) {
                em.remove(user);
                System.out.println("Đã xóa người dùng với ID: " + id);
            }
        });
    }
}