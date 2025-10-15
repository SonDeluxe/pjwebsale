// ===============================
// 📦 main.js — Xử lý giỏ hàng + Toast thông báo
// ===============================

/**
 * 🔔 Hiển thị thông báo nhanh (toast)
 * @param {string} message - Nội dung thông báo
 * @param {boolean} isSuccess - true nếu là thành công (xanh), false nếu là lỗi (đỏ)
 */
function showToast(message, isSuccess = true) {
    const toast = document.createElement("div");
    toast.className = "toast-notification";
    toast.textContent = message;

    // Style cơ bản
    toast.style.position = "fixed";
    toast.style.bottom = "20px";
    toast.style.right = "20px";
    toast.style.padding = "14px 22px";
    toast.style.borderRadius = "8px";
    toast.style.fontSize = "15px";
    toast.style.fontWeight = "500";
    toast.style.color = "#fff";
    toast.style.backgroundColor = isSuccess ? "#28a745" : "#dc3545"; // Xanh lá / Đỏ
    toast.style.boxShadow = "0 4px 10px rgba(0,0,0,0.25)";
    toast.style.opacity = "0";
    toast.style.transform = "translateY(20px)";
    toast.style.transition = "opacity 0.4s ease, transform 0.4s ease";
    toast.style.zIndex = "3000";

    document.body.appendChild(toast);

    // Hiệu ứng hiện lên
    setTimeout(() => {
        toast.style.opacity = "1";
        toast.style.transform = "translateY(0)";
    }, 100);

    // Biến mất sau 3 giây
    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(20px)";
        setTimeout(() => toast.remove(), 500);
    }, 3000);
}

/**
 * 🛒 Gửi AJAX thêm sản phẩm vào giỏ hàng
 * @param {number} productId - ID sản phẩm
 * @param {number} quantity - Số lượng sản phẩm
 */
function handleAddToCart(productId, quantity) {
    // ✅ Xác định contextPath chính xác
    const pathParts = window.location.pathname.split("/");
    const contextPath = pathParts.length > 1 && pathParts[1] ? "/" + pathParts[1] : "/";
    console.log("📦 Gửi request đến:", contextPath + "/carts?action=addItem");

    fetch(`${contextPath}/carts?action=addItem`, {
        method: "POST",
        headers: {
            "Content-Type": "application/x-www-form-urlencoded;charset=UTF-8",
        },
        body: new URLSearchParams({
            productId: productId,
            quantity: quantity,
        }),
    })
        .then(async (response) => {
            const text = await response.text(); // đọc text thô để debug nếu lỗi
            console.log("🔍 Raw Response:", text);
            try {
                return JSON.parse(text);
            } catch (e) {
                throw new Error("Response không phải JSON: " + text);
            }
        })
        .then((data) => {
            console.log("✅ Parsed JSON:", data);
            if (data.status === "success") {
                showToast(data.message || "✅ Đã thêm sản phẩm vào giỏ hàng!", true);
            } else {
                showToast(data.message || "⚠️ Có lỗi xảy ra khi thêm giỏ hàng.", false);
            }
        })
        .catch((error) => {
            console.error("❌ Lỗi thêm giỏ hàng:", error);
            showToast("❌ Không thể thêm sản phẩm vào giỏ hàng.", false);
        });
}

// ===============================
// 🎯 GẮN SỰ KIỆN CHO NÚT "THÊM VÀO GIỎ"
// ===============================
document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".add-to-cart-btn");

    // Tránh gắn trùng listener khi reload JS
    buttons.forEach((btn) => {
        btn.replaceWith(btn.cloneNode(true));
    });

    const newButtons = document.querySelectorAll(".add-to-cart-btn");
    newButtons.forEach((btn) => {
        btn.addEventListener("click", () => {
            const productId = btn.dataset.id;
            const quantity = btn.dataset.qty || 1;
            handleAddToCart(productId, quantity);
        });
    });
});

