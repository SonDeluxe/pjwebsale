<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <title>Lịch sử đơn hàng</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/cart-order.css">
  <style>
    /* Thêm style để link ID trông đẹp hơn */
    .order-id-link {
      text-decoration: none;
      color: inherit; /* Giữ màu của class .order-id */
    }
    .order-id-link:hover .order-id {
      text-decoration: underline;
      color: #c00; /* Đổi màu khi hover */
    }
  </style>
</head>
<body>

<jsp:include page="/includes/header.jsp"/>

<main class="main-content">
  <div class="container">
    <h1 class="page-title">Lịch sử đơn hàng</h1>
    <c:choose>
      <c:when test="${empty orders}">
        <div class="no-orders-message">
          <p>Bạn chưa có đơn hàng nào.</p>
        </div>
      </c:when>
      <c:otherwise>
        <table class="order-history-table">
          <thead>
          <tr>
            <th>Mã ĐH</th>
            <th>Ngày đặt</th>
            <th>Tổng tiền</th>
            <th>Trạng thái</th>
            <th>Hành động</th>
          </tr>
          </thead>
          <tbody>
          <c:forEach var="order" items="${orders}">
            <tr>
              <td>
                  <%-- ================================================================ --%>
                  <%-- SỬA ĐỔI CHÍNH: BỌC THẺ <span> BẰNG THẺ <a> ĐỂ TẠO LINK --%>
                  <%-- ================================================================ --%>
                <a href="${pageContext.request.contextPath}/orders?action=view&id=${order.id}" class="order-id-link">
                  <span class="order-id">#${order.id}</span>
                </a>
              </td>
              <td><fmt:formatDate value="${order.orderDate}" pattern="dd-MM-yyyy"/></td>
              <td><fmt:formatNumber value="${order.totalAmount}" type="currency" currencyCode="VND"/></td>
              <td>
                <c:set var="statusClass" value="status-cancelled"/>
                <c:if test="${order.status == 'Đã thanh toán'}"><c:set var="statusClass" value="status-paid"/></c:if>
                <c:if test="${order.status == 'Chờ thanh toán' || order.status == 'Chờ xử lý'}"><c:set var="statusClass" value="status-pending"/></c:if>
                <c:if test="${order.status == 'Đang giao'}"><c:set var="statusClass" value="status-shipped"/></c:if>

                <span class="status-badge ${statusClass}">
                    ${order.status}
                </span>
              </td>
              <td>
                <c:if test="${order.status != 'Đã thanh toán'}">
                  <a href="${pageContext.request.contextPath}/payments?action=create&orderId=${order.id}" class="btn-pay">Thanh toán</a>
                </c:if>
              </td>
            </tr>
          </c:forEach>
          </tbody>
        </table>
      </c:otherwise>
    </c:choose>
  </div>
</main>

<jsp:include page="/includes/footer.jsp"/>

</body>
</html>