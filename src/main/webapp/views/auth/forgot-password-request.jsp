<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Quên mật khẩu</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
</head>
<body>
<div class="login-page-wrapper">
  <div class="login-box" style="width: 420px;">
    <h2>Đặt lại mật khẩu</h2>
    <p style="text-align: left; margin-bottom: 20px;">Vui lòng nhập email hoặc số điện thoại đã đăng ký để nhận hướng dẫn.</p>

    <c:if test="${not empty sessionScope.error}">
      <p style="color: red; margin-bottom: 15px;">${sessionScope.error}</p>
      <% session.removeAttribute("error"); %>
    </c:if>

    <form action="${pageContext.request.contextPath}/forgot-password" method="post">
      <input type="hidden" name="action" value="sendToken">
      <div class="input-group">
        <label for="identifier">Email hoặc Số điện thoại</label>
        <input type="text" id="identifier" name="identifier" required>
      </div>
      <button type="submit" class="btn-login">Gửi hướng dẫn</button>
    </form>
  </div>
</div>
</body>
</html>