<%-- /views/spareparts/list.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%--
    Phần <head> này sẽ được ghi đè hoặc hợp nhất bởi template.jsp.
    Chúng ta đặt link CSS ở đây để báo cho template biết cần nạp file này.
    Bạn cần có một cơ chế trong template để xử lý các link css/js riêng của từng trang.
--%>
<head>
  <title>Danh sách Phụ tùng</title>
  <%-- Nạp file CSS riêng cho trang này --%>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/spa_list.css">
</head>


<%--
    Bắt đầu nội dung chính của trang.
    Không cần thẻ <html>, <body>, header hay footer ở đây
    vì chúng đã được định nghĩa trong /views/layout/template.jsp
--%>
<main class="main-content">
  <div class="container">

    <div class="toolbar">
      <h1 class="page-title">Danh sách Phụ tùng</h1>

      <%-- ========================================================== --%>
      <%-- BẮT ĐẦU: THÊM FORM TÌM KIẾM --%>
      <%-- ========================================================== --%>
      <div class="search-section-main">
        <form action="${pageContext.request.contextPath}/spareparts" method="GET" class="search-form">
          <input type="hidden" name="action" value="list">
          <input type="text" name="search" placeholder="Tìm kiếm phụ tùng..." class="search-input" value="${searchKeyword}">
          <button type="submit" class="search-button">&#x1F50D;</button>
        </form>
      </div>
      <%-- ========================================================== --%>
      <%-- KẾT THÚC: FORM TÌM KIẾM --%>
      <%-- ========================================================== --%>
    </div>

    <c:if test="${empty spareParts}">
      <div class="alert alert-info">Không tìm thấy phụ tùng nào.</div>
    </c:if>

    <c:if test="${not empty spareParts}">
      <c:if test="${sessionScope.user.role == 'Admin'}">
        <div style="text-align: right; margin-bottom: 20px;">
          <a href="${pageContext.request.contextPath}/spareparts?action=add" class="btn btn-primary">+ Thêm Phụ tùng mới</a>
        </div>
      </c:if>

      <table class="table table-striped table-hover">
        <thead class="table-dark">
        <tr>
          <th>ID</th>
          <th>Hình ảnh</th>
          <th>Tên phụ tùng</th>
          <th>Danh mục</th>
          <th>Giá</th>
          <c:if test="${sessionScope.user.role == 'Admin'}">
            <th>Hành động</th>
          </c:if>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="part" items="${spareParts}">
          <tr>
            <td>${part.id}</td>
            <td><img src="${pageContext.request.contextPath}/assets/images/${part.imageUrl}" alt="${part.name}"></td>
            <td><strong>${part.name}</strong></td>
            <td>${part.category.name}</td>
            <td><fmt:formatNumber value="${part.price}" type="currency" currencyCode="VND"/></td>
            <c:if test="${sessionScope.user.role == 'Admin'}">
              <td>
                <a href="${pageContext.request.contextPath}/spareparts?action=edit&id=${part.id}" class="btn btn-sm btn-primary">Sửa</a>
                <form action="${pageContext.request.contextPath}/spareparts" method="post" style="display: inline-block;">
                  <input type="hidden" name="action" value="delete">
                  <input type="hidden" name="id" value="${part.id}">
                  <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn xóa phụ tùng này?')">Xóa</button>
                </form>
              </td>
            </c:if>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:if>
  </div>
</main>