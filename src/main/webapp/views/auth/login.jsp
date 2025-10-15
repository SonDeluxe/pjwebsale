<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Đăng nhập - Honda</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
</head>
<body>
<div class="login-page-wrapper">
    <div class="login-box">
        <a href="${pageContext.request.contextPath}/"><img src="${pageContext.request.contextPath}/assets/images/logo.jpg" alt="Honda Logo" class="logo"></a>
        <h2>Đăng nhập tài khoản</h2>

        <c:if test="${not empty requestScope.error}"><p style="color: red; margin-bottom: 15px;">${requestScope.error}</p></c:if>
        <c:if test="${not empty requestScope.success}"><p style="color: green; margin-bottom: 15px;">${requestScope.success}</p></c:if>

        <form action="${pageContext.request.contextPath}/auth" method="post">
            <input type="hidden" name="action" value="login">
            <div class="input-group">
                <label for="username">Tên đăng nhập</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="input-group">
                <label for="password">Mật khẩu</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn-login">Đăng nhập</button>
        </form>

        <div class="links-group">
            <a href="${pageContext.request.contextPath}/forgot-password">Quên mật khẩu?</a>
            <span>Chưa có tài khoản? <a href="${pageContext.request.contextPath}/auth?action=register">Đăng ký ngay</a></span>
        </div>
    </div>
</div>
</body>
</html>