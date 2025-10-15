<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Giao diện đặt mật khẩu mới --%>
<!DOCTYPE html>
<html>
<head>
  <title>Tạo mật khẩu mới</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
</head>
<body>
<div class="login-page-wrapper">
  <div class="login-box">
    <h2>Tạo mật khẩu mới</h2>
    <p>Vui lòng nhập mật khẩu mới cho tài khoản của bạn.</p>

    <c:if test="${not empty sessionScope.error}">
      <p style="color: red;">${sessionScope.error}</p>
      <% session.removeAttribute("error"); %>
    </c:if>

    <form action="${pageContext.request.contextPath}/forgot-password" method="post">
      <input type="hidden" name="action" value="resetPassword">
      <div class="input-group">
        <label for="newPassword">Mật khẩu mới</label>
        <input type="password" id="newPassword" name="newPassword" required>
      </div>
      <div class="input-group">
        <label for="confirmPassword">Xác nhận mật khẩu mới</label>
        <input type="password" id="confirmPassword" name="confirmPassword" required>
      </div>
      <button type="submit" class="btn-login">Lưu mật khẩu</button>
    </form>
  </div>
</div>
</body>
</html>