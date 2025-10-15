package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "payment_id")
    private int paymentId;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "payment_date", nullable = false)
    private Date paymentDate;

    @Column(length = 50)
    private String status;

    @Column(length = 50)
    private String method;

    @Column(nullable = false)
    private double amount;

    // Quan hệ 1-1 với Order
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ord") // Sửa lại tên cột khóa ngoại
    private Order order;

    public Payment() {
        this.paymentDate = new Date();
        this.status = "Chờ xử lý";
    }

    public Payment(Order order, String method, double amount) {
        this.order = order;
        this.method = method;
        this.amount = amount;
        this.paymentDate = new Date();
        this.status = "Đã thanh toán";
    }

    // ===== GETTER & SETTER =====
    public int getPaymentId() { return paymentId; }
    public void setPaymentId(int paymentId) { this.paymentId = paymentId; }

    public Date getPaymentDate() { return paymentDate; }
    public void setPaymentDate(Date paymentDate) { this.paymentDate = paymentDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getMethod() { return method; }
    public void setMethod(String method) { this.method = method; }

    public double getAmount() { return amount; }
    public void setAmount(double amount) { this.amount = amount; }

    public Order getOrder() { return order; }
    public void setOrder(Order order) { this.order = order; }

    // ===== CÁC HÀM NGHIỆP VỤ =====
    public boolean processPayment() {
        System.out.println("Xử lý thanh toán qua " + method + "...");
        this.status = "Đã thanh toán";
        System.out.println("Thanh toán thành công. Trạng thái: " + this.status);
        return true;
    }

    public void confirmPayment() {
        System.out.println("Xác nhận thanh toán thành công từ cổng thanh toán!");
        this.status = "Đã xác nhận";
        System.out.println("Trạng thái thanh toán mới: " + this.status);
    }

    public boolean refund() {
        System.out.println("Hoàn tiền giao dịch...");
        this.status = "Đã hoàn tiền";
        return true;
    }
}