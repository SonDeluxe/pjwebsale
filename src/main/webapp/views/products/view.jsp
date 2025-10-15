<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/view-products.css">

<div class="product-detail-container">

    <!-- 🔹 Breadcrumb -->
    <nav class="breadcrumb">
        <a href="${pageContext.request.contextPath}/index.jsp">Trang chủ</a> ›
        <a href="${pageContext.request.contextPath}/products?action=list">Sản phẩm</a> ›
        <span>${product.name}</span>
    </nav>

    <!-- 🔹 Phần chính -->
    <div class="product-detail-layout">

        <!-- 🖼️ Cột ảnh -->
        <div class="product-image-section">
            <img src="${pageContext.request.contextPath}/assets/images/${product.imageUrl}"
                 alt="${product.name}"
                 class="product-image">
        </div>

        <!-- 📦 Cột thông tin -->
        <div class="product-info-section">
            <p class="category">${product.category.categoryName}</p>
            <h1 class="product-name">${product.name}</h1>

            <div class="price">
                <fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/>
            </div>

            <p class="description">${product.description}</p>

            <!-- 🛒 Form thêm vào giỏ -->
            <form id="addToCartForm" class="add-to-cart-form">
                <input type="hidden" name="productId" value="${product.id}">

                <div class="quantity-input-wrapper">
                    <label for="quantity">Số lượng:</label>
                    <input type="number" id="quantity" name="quantity" value="1" min="1" class="quantity-input">
                </div>

                <button type="submit" class="btn btn-add">🛒 Thêm vào giỏ hàng</button>

                <a href="${pageContext.request.contextPath}/carts?action=view" class="btn btn-view-cart">
                    Xem giỏ hàng
                </a>
            </form>
        </div>
    </div>
    <!-- 🔹 FORM GỬI ĐÁNH GIÁ MỚI -->
    <div class="review-form-section">
        <h3>Viết đánh giá của bạn</h3>

        <form id="reviewForm" method="post" action="${pageContext.request.contextPath}/reviews?action=add">
            <input type="hidden" name="productId" value="${product.id}" />

            <div class="form-group">
                <label for="rating">Chấm điểm:</label>
                <select name="rating" id="rating" required>
                    <option value="">-- Chọn --</option>
                    <option value="5">★★★★★ (5 - Rất tốt)</option>
                    <option value="4">★★★★☆ (4 - Tốt)</option>
                    <option value="3">★★★☆☆ (3 - Trung bình)</option>
                    <option value="2">★★☆☆☆ (2 - Kém)</option>
                    <option value="1">★☆☆☆☆ (1 - Rất tệ)</option>
                </select>
            </div>

            <div class="form-group">
                <label for="comment">Nhận xét của bạn:</label>
                <textarea id="comment" name="comment" rows="3" placeholder="Nhập nội dung đánh giá..." required></textarea>
            </div>

            <button type="submit" class="btn btn-review">Gửi đánh giá</button>
        </form>
    </div>
    <!-- 🔹 Phần đánh giá -->
    <div class="product-reviews-section">
        <h2>Đánh giá từ khách hàng</h2>
        <c:choose>
            <c:when test="${not empty reviews}">
                <c:forEach var="review" items="${reviews}">
                    <div class="review-item">
                        <div class="review-header">
                            <!-- ✅ đổi username → name -->
                            <strong>${review.customer.name}</strong>
                            <span class="rating">${review.rating} ★</span>
                        </div>
                        <p class="review-comment">"${review.comment}"</p>
                        <span class="review-date">
                            <fmt:formatDate value="${review.date}" pattern="dd/MM/yyyy"/>
                        </span>
                    </div>
                </c:forEach>
            </c:when>
            <c:otherwise>
                <p>Chưa có đánh giá nào cho sản phẩm này.</p>
            </c:otherwise>
        </c:choose>
    </div>
</div>



<!-- 🔹 JS xử lý -->
<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
    // Xử lý thêm giỏ hàng AJAX
    document.getElementById('addToCartForm').addEventListener('submit', function (event) {
        event.preventDefault();
        const form = event.target;
        const productId = form.querySelector('input[name="productId"]').value;
        const quantity = form.querySelector('input[name="quantity"]').value;
        handleAddToCart(productId, quantity);
    });
</script>