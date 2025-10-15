package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "wishlist")
public class Wishlist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wishlist_id")
    private int wishlistId;

    // Corrected relationship: One customer can have many wishlists.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Corrected Many-to-Many relationship with Product
    @ManyToMany
    @JoinTable(
            name = "wishlist_product",
            joinColumns = @JoinColumn(name = "wishlist_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id")
    )
    private List<Product> products = new ArrayList<>();

    // Constructors
    public Wishlist() {}

    public Wishlist(Customer customer) {
        this.customer = customer;
    }

    // Business Methods
    public void addProduct(Product p) {
        products.add(p);
        System.out.println("💖 Đã thêm " + p.getName() + " vào danh sách yêu thích.");
    }

    public void removeProduct(Product p) {
        products.remove(p);
        System.out.println("💔 Đã xóa " + p.getName() + " khỏi danh sách yêu thích.");
    }

    // Getters & Setters
    public int getWishlistId() {
        return wishlistId;
    }

    public void setWishlistId(int wishlistId) {
        this.wishlistId = wishlistId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }
}