package vn.edu.hcmute.fit.doan.database;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import vn.edu.hcmute.fit.doan.*;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== 🧪 BẮT ĐẦU TEST GIỎ HÀNG ===");

        EntityManager em = null;
        EntityTransaction tx = null;

        try {
            em = ConnectionPool.getEntityManagerFactory().createEntityManager();
            tx = em.getTransaction();
            tx.begin();

            // 🔹 1️⃣ Lấy Customer có ID = 2
            int customerId = 2;
            Customer customer = em.find(Customer.class, customerId);

            if (customer == null) {
                System.out.println("❌ Không tìm thấy khách hàng ID " + customerId);
                return;
            } else {
                System.out.println("✅ Tìm thấy Customer: " + customer.getName() + " | ID = " + customer.getId());
            }

            // 🔹 2️⃣ Tạo Cart mới nếu chưa có
            CartDAO cartDAO = new CartDAO();
            Cart cart = cartDAO.findCartByCustomerId(customer.getId());
            if (cart == null) {
                cart = new Cart(customer);
                cartDAO.addCart(cart);
                System.out.println("🆕 Đã tạo giỏ hàng mới cho khách hàng " + customer.getName());
            } else {
                System.out.println("🧺 Đã có giỏ hàng sẵn, ID = " + cart.getCartId());
            }

            // 🔹 3️⃣ Tìm sản phẩm (chỉnh ID thật trong DB)
            int productId = 1; // ⚠️ đổi ID này nếu sản phẩm khác
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.findProductById(productId);

            if (product == null) {
                System.out.println("❌ Không tìm thấy sản phẩm ID " + productId);
                return;
            } else {
                System.out.println("✅ Đã tìm thấy sản phẩm: " + product.getName() + " | Giá: " + product.getPrice());
            }

            // 🔹 4️⃣ Thêm sản phẩm vào giỏ hàng
            LineItem lineItem = new LineItem(product, 2, product.getPrice(), 0); // Số lượng = 2
            lineItem.setCart(cart);

            LineItemDAO lineItemDAO = new LineItemDAO();
            lineItemDAO.addLineItem(lineItem);

            System.out.println("🛒 Đã thêm sản phẩm '" + product.getName() + "' vào giỏ hàng!");

            // 🔹 5️⃣ Hiển thị toàn bộ sản phẩm trong giỏ hàng
            var items = lineItemDAO.findByCartId(cart.getCartId());
            double total = items.stream().mapToDouble(LineItem::getTotalPrice).sum();

            System.out.println("\n=== 📋 DANH SÁCH TRONG GIỎ HÀNG ===");
            for (LineItem item : items) {
                System.out.println("- " + item.getProduct().getName() + " x" + item.getQuantity() + " = " + item.getTotalPrice());
            }
            System.out.println("💰 Tổng tiền: " + total);

            tx.commit();
            System.out.println("\n✅ TEST HOÀN TẤT KHÔNG LỖI.");

        } catch (Exception e) {
            System.out.println("❌ Có lỗi khi test giỏ hàng:");
            e.printStackTrace();
            if (tx != null && tx.isActive()) tx.rollback();
        } finally {
            if (em != null) em.close();
        }
    }
}
