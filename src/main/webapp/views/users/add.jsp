<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <title>Thêm người dùng</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/includes/header.jsp" />
<div class="container mt-4">
  <h1>Thêm người dùng mới</h1>
  <form action="${pageContext.request.contextPath}/users" method="post">
    <input type="hidden" name="action" value="add">
    <div class="mb-3">
      <label for="username" class="form-label">Username</label>
      <input type="text" class="form-control" id="username" name="username" required>
    </div>
    <div class="mb-3">
      <label for="email" class="form-label">Email</label>
      <input type="email" class="form-control" id="email" name="email" required>
    </div>
    <div class="mb-3">
      <label for="password" class="form-label">Mật khẩu</label>
      <input type="password" class="form-control" id="password" name="password" required>
    </div>
    <div class="mb-3">
      <label for="role" class="form-label">Vai trò</label>
      <select class="form-select" id="role" name="role">
        <option value="Customer">Customer</option>
        <option value="Admin">Admin</option>
      </select>
    </div>
    <button type="submit" class="btn btn-primary">Thêm</button>
  </form>
</div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>