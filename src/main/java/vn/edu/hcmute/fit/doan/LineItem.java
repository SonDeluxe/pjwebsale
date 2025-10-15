package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Table(name = "line_item")
public class LineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    // --- Quan hệ nhiều-1 với Product ---
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    // --- Quan hệ nhiều-1 với Cart ---
    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "cart_id", nullable = true)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Cart cart;

    // --- Quan hệ nhiều-1 với Order (dùng cho đặt hàng sau này) ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = true)
    private Order order;

    // --- Các thuộc tính khác ---
    @Column(nullable = false)
    private int quantity;

    @Column(name = "unit_price", nullable = false)
    private double unitPrice;

    @Column(nullable = false)
    private double discount = 0.0;

    @Column(name = "total_price", nullable = false)
    private double totalPrice;

    // --- Constructors ---
    public LineItem() {}

    public LineItem(Product product, int quantity, double unitPrice, double discount) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.discount = discount;
        this.totalPrice = unitPrice * quantity - discount;
    }

    // --- Các hàm nghiệp vụ ---
    public String getInfo() {
        return product != null
                ? product.getName() + " x" + quantity + " = " + totalPrice
                : "Chưa có sản phẩm";
    }

    public void updateQuantity(int newQuantity) {
        this.quantity = newQuantity;
        this.totalPrice = unitPrice * newQuantity - discount;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public Cart getCart() { return cart; }
    public void setCart(Cart cart) { this.cart = cart; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = quantity; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) { this.unitPrice = unitPrice; }

    public double getDiscount() { return discount; }
    public void setDiscount(double discount) { this.discount = discount; }

    public double getTotalPrice() { return totalPrice; }
    public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }
}
