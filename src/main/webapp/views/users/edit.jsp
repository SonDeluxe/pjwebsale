<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <title>Sửa người dùng</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/includes/header.jsp" />
<div class="container mt-4">
  <h1>Sửa thông tin: ${userToEdit.name}</h1>
  <form action="${pageContext.request.contextPath}/users" method="post">
    <input type="hidden" name="action" value="update">
    <input type="hidden" name="id" value="${userToEdit.id}">
    <div class="mb-3">
      <label for="email" class="form-label">Email</label>
      <input type="email" class="form-control" id="email" name="email" value="${userToEdit.email}" required>
    </div>
    <div class="mb-3">
      <label for="address" class="form-label">Địa chỉ</label>
      <input type="text" class="form-control" id="address" name="address" value="${userToEdit.address}">
    </div>
    <div class="mb-3">
      <label for="phone" class="form-label">Số điện thoại</label>
      <input type="text" class="form-control" id="phone" name="phone" value="${userToEdit.phone}">
    </div>
    <!-- Vai trò -->
    <div class="mb-3">
      <label for="role" class="form-label">Vai trò</label>
      <c:choose>
        <!-- Nếu user này là Admin thì không cho thay đổi -->
        <c:when test="${userToEdit.role == 'Admin'}">
          <input type="hidden" name="role" value="Admin" />
          <p class="form-control-plaintext"><b>Admin (không thể thay đổi)</b></p>
        </c:when>

        <!-- Nếu user là Customer thì cho chọn -->
        <c:otherwise>
          <select name="role" id="role" class="form-select">
            <option value="Customer" ${userToEdit.role == 'Customer' ? 'selected' : ''}>Customer</option>
            <option value="Admin">Admin</option>
          </select>
        </c:otherwise>
      </c:choose>
    </div>

    <div class="mb-3">
      <label for="password" class="form-label">Mật khẩu mới (để trống nếu không đổi)</label>
      <input type="password" class="form-control" id="password" name="password">
    </div>
    <button type="submit" class="btn btn-primary">Cập nhật</button>
  </form>
</div>
<jsp:include page="/includes/footer.jsp" />
</body>
</html>