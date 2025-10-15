package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;

@Entity
@Table(name = "shipment")
public class Shipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shipment_id")
    private int shipmentId;

    @Column(name = "delivery_date", length = 50)
    private String deliveryDate;

    @Column(name = "tracking_number", length = 100, unique = true)
    private String trackingNumber;

    @Column(length = 50)
    private String status;

    // --- Quan hệ 1-1 với Order (một đơn hàng có một giao hàng) ---
    @OneToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // --- Constructors ---
    public Shipment() {
        this.status = "Đang xử lý";
    }

    public Shipment(String deliveryDate, String trackingNumber, String status) {
        this.deliveryDate = deliveryDate;
        this.trackingNumber = trackingNumber;
        this.status = status;
    }

    // --- Business Method ---
    public void updateStatus(String newStatus) {
        this.status = newStatus;
        System.out.println("📦 Trạng thái giao hàng: " + status);
    }

    // --- Getters & Setters ---
    public int getShipmentId() {
        return shipmentId;
    }

    public void setShipmentId(int shipmentId) {
        this.shipmentId = shipmentId;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getTrackingNumber() {
        return trackingNumber;
    }

    public void setTrackingNumber(String trackingNumber) {
        this.trackingNumber = trackingNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }
}
