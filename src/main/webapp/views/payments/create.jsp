<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/create-payments.css">

<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thanh toán đơn hàng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<%@ include file="/includes/header.jsp" %>
<div style="padding: 20px;">
    <h2>🧾 Thanh toán cho đơn hàng #${order.id}</h2>
    <p><strong>Tổng số tiền:</strong> <fmt:formatNumber value="${order.totalAmount}" type="currency" currencyCode="VND"/></p>

    <form method="post" action="${pageContext.request.contextPath}/payments">
        <input type="hidden" name="action" value="add">
        <input type="hidden" name="orderId" value="${order.id}">

        <p>
            <label>Phương thức thanh toán:</label><br>
            <select name="method">
                <option value="Tiền mặt khi nhận hàng">Tiền mặt khi nhận hàng</option>
                <option value="Chuyển khoản ngân hàng">Chuyển khoản ngân hàng</option>
            </select>
        </p>

        <input type="submit" value="Xác nhận thanh toán">
    </form>
    <a href="${pageContext.request.contextPath}/orders?action=list">⬅ Quay lại danh sách đơn hàng</a>
</div>
</body>
</html>