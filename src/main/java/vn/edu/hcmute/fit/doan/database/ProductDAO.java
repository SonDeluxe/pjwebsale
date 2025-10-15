package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TypedQuery;
import vn.edu.hcmute.fit.doan.LineItem;
import vn.edu.hcmute.fit.doan.Product;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProductDAO {

    public static void insertProduct(Product p) {
    }

    // Phương thức tiện ích để xử lý các thao tác truy cập dữ liệu
    private static <T> T executeWithEntityManager(Function<EntityManager, T> operation) {
        try (EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager()) {
            return operation.apply(em);
        }
    }

    // Phương thức tiện ích để thực hiện các giao dịch (transaction)
    private void executeTransaction(Consumer<EntityManager> operation) {
        EntityTransaction transaction = null;
        try (EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager()) {
            transaction = em.getTransaction();
            transaction.begin();
            operation.accept(em);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null && transaction.isActive()) {
                transaction.rollback();
            }
            e.printStackTrace();
            throw new RuntimeException("Lỗi giao dịch khi tương tác với ProductDAO", e);
        }
    }

    // === Các phương thức CRUD ===

    public void addProduct(Product product) {
        executeTransaction(em -> em.persist(product));
        System.out.println("Đã thêm sản phẩm mới: " + product.getName());
    }

    public void updateStockForOrder(List<LineItem> items) {
        executeTransaction(em -> {
            for (LineItem item : items) {
                // Tìm lại sản phẩm trong cùng một transaction để đảm bảo nó được "managed"
                Product product = em.find(Product.class, item.getProduct().getId());
                if (product != null) {
                    int newStock = product.getStock() - item.getQuantity();
                    product.setStock(newStock);
                    // Không cần gọi em.merge() hay update(), Hibernate sẽ tự động cập nhật
                    // khi transaction kết thúc (commit) vì đối tượng product đang được quản lý.
                }
            }
        });
        System.out.println("✅ Đã cập nhật tồn kho cho đơn hàng.");
    }

    @SuppressWarnings({"ResultOfMethodCallIgnored", "CallToPrintStackTrace"})
    public Product findProductById(int id) {
        EntityManager em = ConnectionPool.getEntityManagerFactory().createEntityManager();
        try {
            String jpql = "SELECT DISTINCT p FROM Product p " +
                    "LEFT JOIN FETCH p.category c " +
                    "LEFT JOIN FETCH p.reviews r " +
                    "LEFT JOIN FETCH r.customer cu " +
                    "WHERE p.id = :id";
            TypedQuery<Product> query = em.createQuery(jpql, Product.class);
            query.setParameter("id", id);
            Product product = query.getSingleResult();

            // 🔹 Ép Hibernate load hết dữ liệu trước khi đóng session
            if (product.getCategory() != null) product.getCategory().getCategoryName();
            if (product.getReviews() != null) {
                product.getReviews().forEach(rv -> {
                    if (rv.getCustomer() != null) rv.getCustomer().getName();
                });
            }

            return product;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (em != null && em.isOpen()) em.close();
        }
    }

    public List<Product> getProductsByCategory(String category) {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT p FROM Product p WHERE p.category = :category", Product.class)
                        .setParameter("category", category)
                        .getResultList()
        );
    }

    public void updateProduct(Product product) {
        executeTransaction(em -> em.merge(product));
        System.out.println("Đã cập nhật sản phẩm: " + product.getName());
    }
    // Thêm vào file ProductDAO.java

    /**
     * Tìm kiếm sản phẩm theo tên (dùng LIKE để tìm gần đúng).
     * @param keyword Từ khóa tìm kiếm
     * @return Danh sách sản phẩm phù hợp
     */
    public List<Product> findByName(String keyword) {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.name LIKE :keyword", Product.class)
                        .setParameter("keyword", "%" + keyword + "%") // Dấu % để tìm kiếm gần đúng
                        .getResultList()
        );
    }

    /**
     * Lọc sản phẩm theo ID của danh mục.
     * @param categoryId ID của danh mục cần lọc
     * @return Danh sách sản phẩm thuộc danh mục đó
     */
    public List<Product> findByCategoryId(int categoryId) {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE p.category.categoryId = :catId", Product.class)
                        .setParameter("catId", categoryId)
                        .getResultList()
        );
    }

    public long countAll() {
        return executeWithEntityManager(em ->
                em.createQuery("SELECT COUNT(p) FROM Product p", Long.class).getSingleResult()
        );
    }
    /*
    // Thêm vào ProductDAO.java
    public List<Product> findByCriteria(String keyword, Integer categoryId, Double maxPrice) {
        return executeWithEntityManager(em -> {
            // Bắt đầu câu truy vấn
            StringBuilder queryStr = new StringBuilder("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE 1=1");

            if (keyword != null && !keyword.isEmpty()) {
                queryStr.append(" AND p.name LIKE :keyword");
            }
            if (categoryId != null) {
                queryStr.append(" AND p.category.categoryId = :catId");
            }
            if (maxPrice != null) {
                queryStr.append(" AND p.price <= :maxPrice");
            }

            TypedQuery<Product> query = em.createQuery(queryStr.toString(), Product.class);

            // Gán giá trị cho các tham số
            if (keyword != null && !keyword.isEmpty()) {
                query.setParameter("keyword", "%" + keyword + "%");
            }
            if (categoryId != null) {
                query.setParameter("catId", categoryId);
            }
            if (maxPrice != null) {
                query.setParameter("maxPrice", maxPrice);
            }

            return query.getResultList();
        });
    }
     */
    /**
     * Phương thức tìm kiếm, lọc, sắp xếp và phân trang "All-in-One".
     * @param criteria Các tiêu chí lọc (search, categoryId, maxPrice)
     * @param page Số trang hiện tại (bắt đầu từ 1)
     * @param pageSize Số sản phẩm trên mỗi trang
     * @param sortBy Tiêu chí sắp xếp ("price_asc", "price_desc", "name_asc")
     * @return Danh sách sản phẩm cho trang hiện tại
     */
    public List<Product> findWithFiltersAndPagination(Map<String, Object> criteria, int page, int pageSize, String sortBy) {
        return executeWithEntityManager(em -> {
            StringBuilder queryStr = new StringBuilder("SELECT p FROM Product p LEFT JOIN FETCH p.category WHERE 1=1");

            // 🔹 Thêm điều kiện lọc
            if (criteria.get("keyword") != null) queryStr.append(" AND p.name LIKE :keyword");
            if (criteria.get("categoryId") != null) queryStr.append(" AND p.category.categoryId = :catId");
            if (criteria.get("minPrice") != null) queryStr.append(" AND p.price >= :minPrice");
            if (criteria.get("maxPrice") != null) queryStr.append(" AND p.price <= :maxPrice");

            // 🔹 Phần sắp xếp
            switch (sortBy) {
                case "price_asc": queryStr.append(" ORDER BY p.price ASC"); break;
                case "price_desc": queryStr.append(" ORDER BY p.price DESC"); break;
                case "name_asc":
                default: queryStr.append(" ORDER BY p.name ASC"); break;
            }

            TypedQuery<Product> query = em.createQuery(queryStr.toString(), Product.class);

            // 🔹 Gán giá trị
            if (criteria.get("keyword") != null)
                query.setParameter("keyword", "%" + criteria.get("keyword") + "%");
            if (criteria.get("categoryId") != null)
                query.setParameter("catId", criteria.get("categoryId"));
            if (criteria.get("minPrice") != null)
                query.setParameter("minPrice", criteria.get("minPrice"));
            if (criteria.get("maxPrice") != null)
                query.setParameter("maxPrice", criteria.get("maxPrice"));

            // 🔹 Phân trang
            query.setFirstResult((page - 1) * pageSize);
            query.setMaxResults(pageSize);

            return query.getResultList();
        });
    }

    /**
     * Đếm tổng số sản phẩm khớp với tiêu chí lọc (dùng cho phân trang).
     * @param criteria Các tiêu chí lọc
     * @return Tổng số sản phẩm
     */
    public long countWithFilters(Map<String, Object> criteria) {
        return executeWithEntityManager(em -> {
            StringBuilder queryStr = new StringBuilder("SELECT COUNT(p) FROM Product p WHERE 1=1");

            if (criteria.get("keyword") != null) queryStr.append(" AND p.name LIKE :keyword");
            if (criteria.get("categoryId") != null) queryStr.append(" AND p.category.categoryId = :catId");
            if (criteria.get("minPrice") != null) queryStr.append(" AND p.price >= :minPrice");
            if (criteria.get("maxPrice") != null) queryStr.append(" AND p.price <= :maxPrice");

            TypedQuery<Long> query = em.createQuery(queryStr.toString(), Long.class);

            if (criteria.get("keyword") != null) query.setParameter("keyword", "%" + criteria.get("keyword") + "%");
            if (criteria.get("categoryId") != null) query.setParameter("catId", criteria.get("categoryId"));
            if (criteria.get("minPrice") != null) query.setParameter("minPrice", criteria.get("minPrice"));
            if (criteria.get("maxPrice") != null) query.setParameter("maxPrice", criteria.get("maxPrice"));

            return query.getSingleResult();
        });
    }

    public void deleteProduct(int id) {
        executeTransaction(em -> {
            Product product = em.find(Product.class, id);
            if (product != null) {
                em.remove(product);
                System.out.println("Đã xóa sản phẩm với ID: " + id);
            }
        });
    }
}