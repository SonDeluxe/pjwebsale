<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
    <title>Danh sách yêu thích</title>
    <meta charset="UTF-8">
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
    <style>
        .custom-table {
            width: 100%;
            margin-top: 20px;
        }
        .custom-table th, .custom-table td {
            padding: 10px;
            text-align: left;
        }
        .btn-sm {
            margin-right: 5px;
        }
    </style>
</head>
<body>
<div class="container mt-5">
    <h2>Danh sách yêu thích của bạn</h2>
    <p>Dưới đây là các sản phẩm bạn đã thêm vào danh sách yêu thích.</p>

    <%-- Kiểm tra trạng thái đăng nhập --%>
    <c:if test="${empty sessionScope.user}">
        <div class="alert alert-warning">
            Vui lòng <a href="${pageContext.request.contextPath}/auth?action=login" class="alert-link">đăng nhập</a> để xem danh sách yêu thích của bạn.
        </div>
        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary">Quay lại trang chủ</a>
    </c:if>

    <%-- Hiển thị danh sách yêu thích nếu đã đăng nhập --%>
    <c:if test="${not empty sessionScope.user}">
        <c:choose>
            <c:when test="${not empty wishlists and not empty wishlists[0].products}">
                <table class="custom-table table table-bordered">
                    <thead class="thead-dark">
                    <tr>
                        <th>Tên sản phẩm</th>
                        <th>Giá</th>
                        <th>Hành động</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="wishlist" items="${wishlists}">
                        <c:forEach var="product" items="${wishlist.products}">
                            <tr>
                                <td><strong>${product.name}</strong></td>
                                <td>${product.price}</td>
                                <td>
                                    <a href="${pageContext.request.contextPath}/wishlist/remove?productId=${product.id}"
                                       class="btn btn-danger btn-sm">Xóa</a>
                                    <a href="${pageContext.request.contextPath}/cart/add?productId=${product.id}"
                                       class="btn btn-primary btn-sm">Thêm vào giỏ</a>
                                </td>
                            </tr>
                        </c:forEach>
                    </c:forEach>
                    </tbody>
                </table>
            </c:when>
            <c:otherwise>
                <div class="alert alert-info">
                    Danh sách yêu thích của bạn đang trống.
                </div>
            </c:otherwise>
        </c:choose>

        <%-- Debug thông tin wishlists --%>
        <c:if test="${empty wishlists}">
            <p class="text-muted">Debug: Không tìm thấy danh sách yêu thích nào.</p>
        </c:if>
        <c:if test="${not empty wishlists and empty wishlists[0].products}">
            <p class="text-muted">Debug: Danh sách yêu thích tồn tại nhưng không có sản phẩm.</p>
        </c:if>

        <a href="${pageContext.request.contextPath}/" class="btn btn-secondary mt-3">Quay lại trang chủ</a>
    </c:if>
</div>
</body>
</html>