<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>Sửa sản phẩm</title>
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/includes/header.jsp" />

<div class="container mt-4">
  <h1 class="mb-4">Sửa thông tin sản phẩm: ${product.name}</h1>

  <div class="card">
    <div class="card-body">
      <!-- QUAN TRỌNG: cần multipart để upload ảnh -->
      <form action="${pageContext.request.contextPath}/products" method="post" enctype="multipart/form-data">
        <input type="hidden" name="action" value="update">
        <input type="hidden" name="id" value="${product.id}">

        <!-- Giữ ảnh cũ nếu không upload ảnh mới -->
        <input type="hidden" name="imageUrl" value="${product.imageUrl}" />

        <div class="mb-3">
          <label for="name" class="form-label">Tên sản phẩm</label>
          <input type="text" class="form-control" id="name" name="name" value="${product.name}" required>
        </div>

        <div class="row">
          <div class="col-md-6 mb-3">
            <label for="price" class="form-label">Giá</label>
            <!-- dùng priceStr nếu có, fallback product.price -->
            <input
                    type="number"
                    class="form-control"
                    id="price"
                    name="price"
                    step="0.01"
                    inputmode="decimal"
                    value="${not empty priceStr ? priceStr : product.price}"
                    required
            >
          </div>
          <div class="col-md-6 mb-3">
            <label for="stock" class="form-label">Số lượng tồn kho</label>
            <input
                    type="number"
                    class="form-control"
                    id="stock"
                    name="stock"
                    min="0"
                    step="1"
                    value="${product.stock}"
                    required
            >
          </div>
        </div>

        <div class="mb-3">
          <label for="categoryId" class="form-label">Danh mục</label>
          <select class="form-select" id="categoryId" name="categoryId" required>
            <c:forEach var="cat" items="${categories}">
              <option value="${cat.categoryId}" ${cat.categoryId == product.category.categoryId ? 'selected="selected"' : ''}>
                  ${cat.categoryName}
              </option>
            </c:forEach>
          </select>
        </div>

        <div class="mb-3">
          <label class="form-label d-block">Ảnh hiện tại</label>
          <c:if test="${not empty product.imageUrl}">
            <img
                    src="${pageContext.request.contextPath}/assets/images/${product.imageUrl}"
                    alt="${product.name}"
                    style="max-width: 220px; height: auto; border-radius: .5rem; display:block; margin-bottom:.5rem;"
            />
            <div class="text-muted small">Tên file đang dùng: ${product.imageUrl}</div>
          </c:if>
        </div>

        <div class="mb-3">
          <label for="imageFile" class="form-label">Chọn ảnh mới (tuỳ chọn)</label>
          <input type="file" class="form-control" id="imageFile" name="imageFile" accept="image/*">
          <div class="form-text">Nếu không chọn, hệ thống sẽ giữ nguyên ảnh hiện tại.</div>
        </div>

        <div class="mb-3">
          <label for="description" class="form-label">Mô tả</label>
          <textarea class="form-control" id="description" name="description" rows="4">${product.description}</textarea>
        </div>

        <button type="submit" class="btn btn-primary">Cập nhật</button>
        <a href="${pageContext.request.contextPath}/products?action=list" class="btn btn-secondary">Hủy</a>
      </form>
    </div>
  </div>
</div>

<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
