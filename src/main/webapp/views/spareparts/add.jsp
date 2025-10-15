<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
  <title>Thêm Phụ tùng</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
<div class="admin-container">
  <div class="admin-header">
    <h1>Thêm Phụ tùng mới</h1>
  </div>

  <!-- Hiển thị thông báo -->
  <c:if test="${not empty success}">
    <div class="alert alert-success">${success}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-error">${error}</div>
  </c:if>

  <!-- Thêm enctype="multipart/form-data" để hỗ trợ upload file -->
  <form class="admin-form" action="${pageContext.request.contextPath}/spareparts" method="post" enctype="multipart/form-data">
    <input type="hidden" name="action" value="add">

    <div class="form-group">
      <label for="name" class="form-label">Tên Phụ tùng</label>
      <input type="text" class="form-control" id="name" name="name" required>
    </div>

    <div class="form-group">
      <label for="price" class="form-label">Giá</label>
      <input type="number" class="form-control" id="price" name="price" step="0.01" required>
    </div>

    <div class="form-group">
      <label for="categoryId" class="form-label">Danh mục</label>
      <select class="form-select" id="categoryId" name="categoryId" required>
        <c:forEach var="cat" items="${categories}">
          <option value="${cat.id}">${cat.name}</option>
        </c:forEach>
      </select>
    </div>

    <div class="form-group">
      <label for="imageFile" class="form-label">Upload ảnh</label>
      <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*">
      <small class="form-text">Chọn file ảnh từ máy tính của bạn</small>
    </div>

    <div class="form-group">
      <label for="imageUrl" class="form-label">Hoặc nhập tên file ảnh</label>
      <input type="text" class="form-control" id="imageUrl" name="imageUrl" placeholder="ví dụ: ten-anh.jpg">
      <small class="form-text">Chỉ sử dụng nếu không upload ảnh</small>
    </div>

    <button type="submit" class="btn">Thêm Phụ tùng</button>
    <a href="${pageContext.request.contextPath}/spareparts?action=adminList" class="btn btn-secondary">Quay lại</a>
  </form>
</div>
</body>
</html>