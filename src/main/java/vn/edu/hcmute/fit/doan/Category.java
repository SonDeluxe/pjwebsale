package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "category")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private int categoryId;

    @Column(name = "category_name", nullable = false, length = 100)
    private String categoryName;

    @Column(length = 255)
    private String description;

    @Column(length = 20)
    private String phone;

    @Column(length = 255)
    private String message;

    // 🔹 Quan hệ 1-nhiều với Product: 1 Category có thể có nhiều Product
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Product> products = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Category parentCategory;

    @OneToMany(mappedBy = "parentCategory", fetch = FetchType.EAGER) // EAGER để lấy danh mục con ngay lập tức
    private List<Category> subCategories = new ArrayList<>();

    // ===== Constructors =====
    public Category() {}

    public Category(String categoryName, String description, String phone, String message) {
        this.categoryName = categoryName;
        this.description = description;
        this.phone = phone;
        this.message = message;
    }

    // ===== Getters & Setters =====
    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    // ===== Convenience Methods (giúp đồng bộ 2 chiều) =====
    public void addProduct(Product product) {
        products.add(product);
        Category category = null;
        product.setCategory(category);
    }

    public void removeProduct(Product product) {
        products.remove(product);
        Category category = null;
        product.setCategory(category);
    }
    public Category getParentCategory() {
        return parentCategory;
    }

    public void setParentCategory(Category parentCategory) {
        this.parentCategory = parentCategory;
    }

    public List<Category> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(List<Category> subCategories) {
        this.subCategories = subCategories;
    }

}
