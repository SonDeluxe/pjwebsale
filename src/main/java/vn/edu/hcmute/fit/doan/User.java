package vn.edu.hcmute.fit.doan;

// Thêm các import cần thiết
import jakarta.persistence.*;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "role") // <-- THÊM DÒNG NÀY: Dùng cột 'role' để phân biệt
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    private String email;

//    @Column(length = 20, insertable = false, updatable = false)
//    private String role;
// Chỉ để Hibernate đọc giá trị role, không chặn update các field khác
    @Column(name = "role", insertable = false, updatable = false)
    private String role;

    private String address;
    private String phone;

    public User() {}

    public User(String username, String password, String email, String role, String address, String phone) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.role = role;
        this.address = address;
        this.phone = phone;
    }

    // --- Getters & Setters ---
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getName() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
}