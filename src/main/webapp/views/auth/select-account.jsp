<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%-- Giao diện chọn tài khoản khi bị trùng --%>
<!DOCTYPE html>
<html>
<head>
  <title>Chọn tài khoản</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/login.css">
</head>
<body>
<div class="login-page-wrapper">
  <div class="login-box">
    <h2>Chọn tài khoản</h2>
    <p>Chúng tôi tìm thấy nhiều tài khoản với thông tin bạn cung cấp. Vui lòng chọn tài khoản bạn muốn đặt lại mật khẩu.</p>

    <c:forEach var="user" items="${users}">
      <form action="${pageContext.request.contextPath}/forgot-password" method="post" style="margin-bottom: 10px;">
        <input type="hidden" name="action" value="sendTokenForSelectedUser">
        <input type="hidden" name="userId" value="${user.id}">
          <%-- Hiển thị tên tài khoản một cách an toàn --%>
        <button type="submit" class="btn-login" style="background-color: #555;">
          Tài khoản: ${user.name}
        </button>
      </form>
    </c:forEach>
  </div>
</div>
</body>
</html>