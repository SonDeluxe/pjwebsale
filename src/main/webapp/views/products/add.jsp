<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <style>
    body { padding-top: 120px !important; }
    .image-preview {
      max-width: 200px;
      max-height: 200px;
      margin-top: 10px;
      display: none;
    }
  </style>
  <title>Thêm sản phẩm mới</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>

<div class="container mt-4">
  <h1 class="mb-4">Thêm sản phẩm mới</h1>

  <!-- Hiển thị thông báo lỗi/thành công -->
  <c:if test="${not empty success}">
    <div class="alert alert-success">${success}</div>
  </c:if>
  <c:if test="${not empty error}">
    <div class="alert alert-danger">${error}</div>
  </c:if>

  <div class="card">
    <div class="card-body">
      <!-- THÊM enctype="multipart/form-data" để hỗ trợ upload file -->
      <form action="${pageContext.request.contextPath}/products" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="add">
        <div class="mb-3">
          <label for="name" class="form-label">Tên sản phẩm</label>
          <input type="text" class="form-control" id="name" name="name" required>
        </div>
        <div class="row">
          <div class="col-md-6 mb-3">
            <label for="price" class="form-label">Giá</label>
            <input type="number" class="form-control" id="price" name="price" required min="0" step="1000">
          </div>
          <div class="col-md-6 mb-3">
            <label for="stock" class="form-label">Số lượng tồn kho</label>
            <input type="number" class="form-control" id="stock" name="stock" required value="0" min="0">
          </div>
        </div>
        <div class="mb-3">
          <label for="categoryId" class="form-label">Danh mục</label>
          <select class="form-select" id="categoryId" name="categoryId" required>
            <option value="">-- Chọn một danh mục --</option>
            <c:forEach var="cat" items="${categories}">
              <option value="${cat.categoryId}">${cat.categoryName}</option>
            </c:forEach>
          </select>
        </div>

        <!-- PHẦN UPLOAD ẢNH MỚI -->
        <div class="mb-3">
          <label for="imageFile" class="form-label">Tải ảnh sản phẩm lên</label>
          <input type="file" class="form-control" id="imageFile" name="imageFile"
                 accept="image/*" onchange="previewImage(this)">
          <small class="form-text text-muted">Chấp nhận: JPG, PNG, GIF (tối đa 5MB)</small>
          <img id="imagePreview" class="image-preview" alt="Preview">
        </div>

        <div class="mb-3">
          <label for="imageUrl" class="form-label">Hoặc nhập URL ảnh</label>
          <input type="text" class="form-control" id="imageUrl" name="imageUrl"
                 placeholder="https://example.com/image.jpg">
          <small class="form-text text-muted">Chỉ sử dụng nếu không tải ảnh lên</small>
        </div>

        <div class="mb-3">
          <label for="description" class="form-label">Mô tả</label>
          <textarea class="form-control" id="description" name="description" rows="4"></textarea>
        </div>
        <button type="submit" class="btn btn-primary">Thêm sản phẩm</button>
        <a href="${pageContext.request.contextPath}/products?action=list" class="btn btn-secondary">Hủy</a>
      </form>
    </div>
  </div>
</div>
<script>
  // Hiển thị preview ảnh khi chọn file
  function previewImage(input) {
    const preview = document.getElementById('imagePreview');
    const file = input.files[0];

    if (file) {
      const reader = new FileReader();
      reader.onload = function(e) {
        preview.src = e.target.result;
        preview.style.display = 'block';
      }
      reader.readAsDataURL(file);
    } else {
      preview.style.display = 'none';
    }
  }
</script>
</body>
</html>