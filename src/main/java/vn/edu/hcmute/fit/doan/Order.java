package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.TIMESTAMP)
    private Date orderDate;

    private double totalAmount;
    private String status;
    private String deliveryAddress;
    private String deliveryDate;

    // Một đơn hàng có thể có nhiều LineItem
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<LineItem> lineItems = new ArrayList<>();

    // Liên kết với Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id")
    private Customer customer;

    public Order() {
        this.orderDate = new Date();
        this.status = "Chờ xử lý";
    }

    public void calculateTotal() {
        this.totalAmount = lineItems.stream().mapToDouble(LineItem::getTotalPrice).sum();
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public Date getOrderDate() { return orderDate; }
    public void setOrderDate(Date orderDate) { this.orderDate = orderDate; }

    public double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(double totalAmount) { this.totalAmount = totalAmount; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDeliveryAddress() { return deliveryAddress; }
    public void setDeliveryAddress(String deliveryAddress) { this.deliveryAddress = deliveryAddress; }

    public String getDeliveryDate() { return deliveryDate; }
    public void setDeliveryDate(String deliveryDate) { this.deliveryDate = deliveryDate; }

    public List<LineItem> getLineItems() { return lineItems; }
    public void setLineItems(List<LineItem> lineItems) { this.lineItems = lineItems; }

    public Customer getCustomer() { return customer; }
    public void setCustomer(Customer customer) { this.customer = customer; }

}
