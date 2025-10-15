package vn.edu.hcmute.fit.doan;

// Thêm import
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Admin") // <-- THÊM DÒNG NÀY
public class Admin extends User {

    //public Admin() {
     //   setRole("Admin"); // Gán role mặc định
    //}
    public Admin() {
        super();
}
    public void manageVehicles() {
        System.out.println("🚗 Quản lý danh sách xe trong hệ thống...");
    }

    public void manageOrders() {
        System.out.println("📦 Quản lý đơn hàng của khách hàng...");
    }

    public void managePromotions() {
        System.out.println("💰 Thiết lập khuyến mãi cho sản phẩm...");
    }
}