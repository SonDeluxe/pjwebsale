//package vn.edu.hcmute.fit.doan;
//
//import jakarta.persistence.*;
//import java.time.LocalDate;
//
//@Entity
//@Table(name = "warranty")
//public class Warranty {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private int id;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private User user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_id", nullable = false)
//    private Product product;
//
//    // ⚠️ phải match đúng cột trong SQL Server (start_date, end_date)
//    @Column(name = "start_date", nullable = false)
//    private LocalDate startDate;
//
//    @Column(name = "end_date", nullable = false)
//    private LocalDate endDate;
//
//    @Column(length = 50)
//    private String status;
//
//    @Column(length = 255)
//    private String notes;
//
//    public Warranty() {}
//
//    public Warranty(User user, Product product, LocalDate startDate, LocalDate endDate, String status) {
//        this.user = user;
//        this.product = product;
//        this.startDate = startDate;
//        this.endDate = endDate;
//        this.status = status;
//    }
//
//    // Getters & setters
//    public int getId() { return id; }
//    public void setId(int id) { this.id = id; }
//
//    public User getUser() { return user; }
//    public void setUser(User user) { this.user = user; }
//
//    public Product getProduct() { return product; }
//    public void setProduct(Product product) { this.product = product; }
//
//    public LocalDate getStartDate() { return startDate; }
//    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
//
//    public LocalDate getEndDate() { return endDate; }
//    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
//
//    public String getStatus() { return status; }
//    public void setStatus(String status) { this.status = status; }
//
//    public String getNotes() { return notes; }
//    public void setNotes(String notes) { this.notes = notes; }
//
//    @Override
//    public String toString() {
//        return "Warranty{id=" + id +
//                ", user=" + (user != null ? user.getName() : "N/A") +
//                ", product=" + (product != null ? product.getName() : "N/A") +
//                ", startDate=" + startDate +
//                ", endDate=" + endDate +
//                ", status='" + status + '\'' +
//                '}';
//    }
//}
package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Entity
@Table(name = "warranty")
public class Warranty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(length = 50)
    private String status;

    @Column(length = 255)
    private String notes;

    public Warranty() {}

    public Warranty(User user, Product product, LocalDate startDate, LocalDate endDate, String status) {
        this.user = user;
        this.product = product;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

    // Getters & setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    public Product getProduct() { return product; }
    public void setProduct(Product product) { this.product = product; }

    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }

    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getNotes() { return notes; }
    public void setNotes(String notes) { this.notes = notes; }

    // Thêm getter cho java.util.Date
    public Date getStartDateAsDate() {
        if (startDate == null) return null;
        return Date.from(startDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    public Date getEndDateAsDate() {
        if (endDate == null) return null;
        return Date.from(endDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
    }

    @Override
    public String toString() {
        return "Warranty{id=" + id +
                ", user=" + (user != null ? user.getName() : "N/A") +
                ", product=" + (product != null ? product.getName() : "N/A") +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", status='" + status + '\'' +
                '}';
    }
}