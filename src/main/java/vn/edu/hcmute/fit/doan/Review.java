package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "review")
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private int reviewId;

    @Column(nullable = false)
    private int rating;

    @Column(length = 500)
    private String comment;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    // ==============================
    // 🔹 Mỗi Review thuộc về 1 Customer
    // ==============================
    @ManyToOne(fetch = FetchType.LAZY)  // ⚠️ đổi từ LAZY → EAGER để tránh lỗi session
    @JoinColumn(name = "customer_id", nullable = true) // Cho phép null nếu khách ẩn danh
    private Customer customer;

    // ==============================
    // 🔹 Mỗi Review thuộc về 1 Product
    // ==============================
    @ManyToOne(fetch = FetchType.LAZY)  // ⚠️ đổi từ LAZY → EAGER
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // ==============================
    // 🔹 Constructors
    // ==============================
    public Review() {
        this.date = new Date();
    }

    public Review(int rating, String comment, Customer customer, Product product) {
        this.rating = rating;
        this.comment = comment;
        this.customer = customer;
        this.product = product;
        this.date = new Date();
    }

    // ==============================
    // 🔹 Business Method
    // ==============================
    public String getReviewInfo() {
        return "[" + rating + "⭐] " + comment + " (" + date + ")";
    }

    // ==============================
    // 🔹 Getters & Setters
    // ==============================
    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
