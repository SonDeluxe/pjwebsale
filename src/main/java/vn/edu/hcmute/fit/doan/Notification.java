package vn.edu.hcmute.fit.doan;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private int notificationId;

    @Column(nullable = false, length = 500)
    private String message;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false)
    private Date date;

    // Mối quan hệ: Nhiều thông báo thuộc về 1 user
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Notification() {
        this.date = new Date();
    }

    public Notification(String message, User user) {
        this.message = message;
        this.user = user;
        this.date = new Date();
    }

    // --- Business Logic ---
    public void sendNotification() {
        System.out.println("📢 Gửi thông báo đến người dùng " +
                (user != null ? user.getName() : "Unknown") +
                ": " + message);
    }

    // --- Getters & Setters ---
    public int getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(int notificationId) {
        this.notificationId = notificationId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
