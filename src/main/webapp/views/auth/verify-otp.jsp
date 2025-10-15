<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <title>Xác thực OTP</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
</head>
<body>
<div class="login-page-wrapper">
    <div class="login-box">
        <h2>Nhập mã OTP</h2>
        <p style="text-align: left; margin-bottom: 20px;">Một mã OTP đã được gửi đến email của bạn. Vui lòng nhập vào ô bên dưới.</p>

        <c:if test="${not empty sessionScope.error}">
            <p style="color: red; margin-bottom: 15px;">${sessionScope.error}</p>
            <% session.removeAttribute("error"); %>
        </c:if>
        <c:if test="${not empty sessionScope.message}">
            <p style="color: green; margin-bottom: 15px;">${sessionScope.message}</p>
            <% session.removeAttribute("message"); %>
        </c:if>

        <form action="${pageContext.request.contextPath}/forgot-password" method="post">
            <input type="hidden" name="action" value="verifyToken">
            <input type="hidden" name="userId" value="${param.userId}">
            <div class="input-group">
                <label for="otp">Mã OTP (6 chữ số)</label>
                <input type="text" id="otp" name="otp" required maxlength="6" pattern="\d{6}" title="Vui lòng nhập 6 chữ số">
            </div>
            <button type="submit" class="btn-login">Xác nhận</button>
        </form>
    </div>
</div>
</body>
</html>