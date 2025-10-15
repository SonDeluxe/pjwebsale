<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng ký - Honda</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
</head>
<body>
<div class="login-page-wrapper">
    <div class="login-box">
        <a href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/assets/images/logo.jpg" alt="Honda Logo" class="logo"></a>
        <h2>Tạo tài khoản mới</h2>

        <c:if test="${not empty requestScope.error}"><p style="color: red; margin-bottom: 15px;">${requestScope.error}</p></c:if>

        <form action="${pageContext.request.contextPath}/auth" method="post">
            <input type="hidden" name="action" value="register">
            <div class="input-group"><label for="username">Tên đăng nhập</label><input type="text" id="username" name="username" required></div>
            <div class="input-group"><label for="email">Email</label><input type="email" id="email" name="email" required></div>
            <div class="input-group"><label for="password">Mật khẩu</label><input type="password" id="password" name="password" required></div>
            <div class="input-group"><label for="address">Địa chỉ</label><input type="text" id="address" name="address"></div>
            <div class="input-group"><label for="phone">Số điện thoại</label><input type="text" id="phone" name="phone"></div>
            <button type="submit" class="btn-login">Đăng ký</button>
        </form>
        <div class="links-group">
            <span>Đã có tài khoản? <a href="${pageContext.request.contextPath}/auth?action=login">Đăng nhập</a></span>
        </div>
    </div>
</div>
</body>
</html>