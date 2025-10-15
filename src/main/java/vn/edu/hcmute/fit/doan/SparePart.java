// src/main/java/vn/edu/hcmute/fit/doan/SparePart.java
package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;

@Entity
@Table(name = "spare_part")
public class SparePart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String name; // Tên phụ tùng

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String description; // Mô tả chi tiết

    private double price; // Giá bán

    @Column(length = 255)
    private String imageUrl; // Đường dẫn ảnh

    // --- THÊM MỚI QUAN HỆ ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private SparePartCategory category;

    public SparePart() {}

    // --- CẬP NHẬT CONSTRUCTOR ---
    public SparePart(String name, String description, double price, String imageUrl, SparePartCategory category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.imageUrl = imageUrl;
        this.category = category;
    }

    // --- Getters & Setters (thêm getter/setter cho category) ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public SparePartCategory getCategory() { return category; }
    public void setCategory(SparePartCategory category) { this.category = category; }

    @Override
    public String toString() {
        return "SparePart{id=" + id + ", name='" + name + "', price=" + price + "}";
    }
}