/** 🔔 Hiển thị thông báo nhanh (toast) */
function showToast(message, isSuccess = true) {
    const toast = document.createElement("div");
    toast.className = "toast-notification";
    toast.textContent = message;

    toast.style.position = "fixed";
    toast.style.bottom = "20px";
    toast.style.right = "20px";
    toast.style.padding = "14px 22px";
    toast.style.borderRadius = "8px";
    toast.style.fontSize = "15px";
    toast.style.fontWeight = "500";
    toast.style.color = "#fff";
    toast.style.backgroundColor = isSuccess ? "#28a745" : "#dc3545";
    toast.style.boxShadow = "0 4px 10px rgba(0,0,0,0.25)";
    toast.style.opacity = "0";
    toast.style.transform = "translateY(20px)";
    toast.style.transition = "opacity 0.4s ease, transform 0.4s ease";
    toast.style.zIndex = "3000";

    document.body.appendChild(toast);

    setTimeout(() => {
        toast.style.opacity = "1";
        toast.style.transform = "translateY(0)";
    }, 100);

    setTimeout(() => {
        toast.style.opacity = "0";
        toast.style.transform = "translateY(20px)";
        setTimeout(() => toast.remove(), 500);
    }, 3000);
}

/** 🛒 Gửi AJAX thêm sản phẩm vào giỏ hàng */
function handleAddToCart(productId, quantity) {
    // ✅ Tự động nhận contextPath theo môi trường
    const isLocal = window.location.hostname === "localhost";
    const contextPath = isLocal ? "/doan" : "";

    console.log("📦 Gửi request đến:", `${contextPath}/carts?action=addItem`);

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
            const text = await response.text();
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

/** 🎯 Gắn sự kiện cho nút "Thêm vào giỏ" */
document.addEventListener("DOMContentLoaded", () => {
    const buttons = document.querySelectorAll(".add-to-cart-btn");

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