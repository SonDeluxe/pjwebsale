//package vn.edu.hcmute.fit.doan;
//
//import jakarta.persistence.*;
//import java.util.List;
//
//@Entity
//@Table(name = "product")
//public class Product {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    private String name;
//    private double price;
//
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "category")
//    private Category category;
//
//    private String description;
//    private String imageUrl;
//
//    private int stock; // Thêm lại trường stock
//
//    // Quan hệ một-nhiều: Một sản phẩm có thể có nhiều LineItem
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
//    private List<LineItem> lineItems;
//
//    public Product() {}
//
//    public Product(String name, double price, Category category, String description, String imageUrl) {
//        this.name = name;
//        this.price = price;
//        this.category = category;
//        this.description = description;
//        this.imageUrl = imageUrl;
//    }
//
//    // --- Business Methods ---
//    public String getInfo() {
//        return name + " - Giá: " + price + " (" + (category != null ? category.getCategoryName() : "N/A") + ")";
//    }
//
//    public void updateStock(int quantity) {
//        stock += quantity;
//        System.out.println("Tồn kho mới: " + stock);
//    }
//
//    public void applyPromotion(double discount) {
//        price = price - (price * discount / 100);
//        System.out.println("Giá sau khuyến mãi: " + price);
//    }
//
//    // --- Getters & Setters ---
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//
//    public String getName() { return name; }
//    public void setName(String name) { this.name = name; }
//
//    public double getPrice() { return price; }
//    public void setPrice(double price) { this.price = price; }
//
//    public Category getCategory() { return category; }
//    public void setCategory(Category category) { this.category = category; }
//
//    public String getDescription() { return description; }
//    public void setDescription(String description) { this.description = description; }
//
//    public String getImageUrl() { return imageUrl; }
//    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
//
//    public int getStock() { return stock; }
//    public void setStock(int stock) { this.stock = stock; }
//
//    public List<LineItem> getLineItems() { return lineItems; }
//    public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems; }
//    // =============================
//// 🔹 Quan hệ 1-nhiều với Review
//// =============================
//    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private List<Review> reviews;
//
//    public List<Review> getReviews() {
//        return reviews;
//    }
//
//    public void setReviews(List<Review> reviews) {
//        this.reviews = reviews;
//    }
//
//}
package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;
    private double price;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category")
    private Category category;

    private String description;
    private String imageUrl;

    private int stock; // Thêm lại trường stock

    // Quan hệ một-nhiều: Một sản phẩm có thể có nhiều LineItem
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<LineItem> lineItems;

    public Product() {}

    public Product(String name, double price, Category category, String description, String imageUrl) {
        this.name = name;
        this.price = price;
        this.category = category;
        this.description = description;
        this.imageUrl = imageUrl;
    }

    // --- Business Methods ---
    public String getInfo() {
        return name + " - Giá: " + price + " (" + (category != null ? category.getCategoryName() : "N/A") + ")";
    }

    public void updateStock(int quantity) {
        stock += quantity;
        System.out.println("Tồn kho mới: " + stock);
    }

    public void applyPromotion(double discount) {
        price = price - (price * discount / 100);
        System.out.println("Giá sau khuyến mãi: " + price);
    }

    // Thêm phương thức kiểm tra stock
    public boolean isStockAvailable(int quantity) {
        return stock >= quantity;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }

    public List<LineItem> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems; }

    // =============================
    // 🔹 Quan hệ 1-nhiều với Review
    // =============================
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Review> reviews;

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }
}