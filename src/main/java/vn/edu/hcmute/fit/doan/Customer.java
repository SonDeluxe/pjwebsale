//package vn.edu.hcmute.fit.doan;
//
//// Thêm import
//import jakarta.persistence.*;
//
//import java.util.List;
//
//@Entity
//@DiscriminatorValue("Customer") // <-- THÊM DÒNG NÀY
//public class Customer extends User {
//
//    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    private Cart cart;
//
//    @OneToMany(mappedBy = "customer", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Wishlist> wishlists;
//
//    public Customer() {
//        setRole("Customer"); // Gán role mặc định
//    }
//
//    public Customer(String username, String password, String email, String address, String phone) {
//        super(username, password, email, "Customer", address, phone);
//    }
//
//    public Cart getCart() { return cart; }
//    public void setCart(Cart cart) { this.cart = cart; }
//
//    public List<Wishlist> getWishlists() { return wishlists; }
//    public void setWishlists(List<Wishlist> wishlists) { this.wishlists = wishlists; }
//}
package vn.edu.hcmute.fit.doan;

// Thêm import

import jakarta.persistence.*;

@Entity
@DiscriminatorValue("Customer") // <-- THÊM DÒNG NÀY
public class Customer extends User {

    @OneToOne(mappedBy = "customer", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Cart cart;


    //    public Customer() {
//       setRole("Customer"); // Gán role mặc định
//    }
    public Customer() {
        super();
    }

    public Customer(String username, String password, String email, String address, String phone) {
        super(username, password, email, "Customer", address, phone);
    }

    public Cart getCart() {
        return cart;
    }

    public void setCart(Cart cart) {
        this.cart = cart;
    }

}