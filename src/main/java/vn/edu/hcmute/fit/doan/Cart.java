package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private int cartId;

    // ✅ Quan hệ 1-nhiều với LineItem
    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<LineItem> lineItems = new ArrayList<>();

    // ✅ Quan hệ 1-1 với Customer (trong cùng bảng users)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Customer customer;

    // --- Constructors ---
    public Cart() {}

    public Cart(Customer customer) {
        this.customer = customer;
    }

    // ✅ Thêm sản phẩm vào giỏ
    public void addItem(Product product, int quantity) {
        LineItem item = new LineItem();
        item.setProduct(product);
        item.setQuantity(quantity);
        item.setUnitPrice(product.getPrice());
        item.setTotalPrice(product.getPrice() * quantity);
        item.setCart(this); // Gắn ngược lại cart
        lineItems.add(item);
    }

    // ✅ Xóa sản phẩm khỏi giỏ
    public void removeItem(Product product) {
        lineItems.removeIf(item -> item.getProduct().equals(product));
    }

    // ✅ Tính tổng giá trị
    public double calculateTotal() {
        return lineItems.stream().mapToDouble(LineItem::getTotalPrice).sum();
    }

    // --- Getters & Setters ---
    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public List<LineItem> getLineItems() {
        return lineItems;
    }

    public void setLineItems(List<LineItem> lineItems) {
        this.lineItems = lineItems;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
