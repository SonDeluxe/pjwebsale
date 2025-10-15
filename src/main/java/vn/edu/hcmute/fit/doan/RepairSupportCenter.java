package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;

@Entity
@Table(name = "repair_support_center")
public class RepairSupportCenter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "repair_id")
    private int id;

    // Liên kết với đối tượng Product
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_pd", nullable = false)
    private Product product;

    // Liên kết với đối tượng Customer
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ct", nullable = false)
    private Customer customer;

    // Liên kết với đối tượng Order
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_ord", nullable = false)
    private Order order;

    // === Constructor ===
    public RepairSupportCenter() {}

    public RepairSupportCenter(Product product, Customer customer, Order order) {
        this.product = product;
        this.customer = customer;
        this.order = order;
    }

    // === Các hàm nghiệp vụ ===
    public void receiveRequest() {
        System.out.println("📩 Đã nhận yêu cầu sửa chữa cho sản phẩm " + product.getName());
    }

    public void assignTechnician() {
        System.out.println("🧰 Phân công kỹ thuật viên cho phiếu " + order.getId());
    }

    public void repair() {
        System.out.println("🔧 Đang tiến hành sửa chữa sản phẩm " + product.getName() + " ...");
    }

    public void generateReport() {
        System.out.println("📄 Báo cáo kết quả sửa chữa đã được tạo cho phiếu " + order.getId());
    }

    public void promotion() {
        System.out.println("🎁 Áp dụng chương trình khuyến mãi hậu mãi sau sửa chữa.");
    }

    // === Getters & Setters ===
    // (Cần cập nhật lại getters/setters cho các thuộc tính mới)
}