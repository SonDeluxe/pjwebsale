<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!DOCTYPE html>
<html lang="vi">
<head>
  <meta charset="UTF-8">
  <title>Lịch sử thanh toán</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/list.css">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</head>
<body>
<%@ include file="/includes/header.jsp" %>
<main class="product-page">
  <h1 class="page-title">Lịch sử thanh toán</h1>

  <c:choose>
    <c:when test="${empty payments}">
      <p style="text-align: center;">Chưa có giao dịch thanh toán nào.</p>
    </c:when>
    <c:otherwise>
      <table border="1" style="width: 90%; margin: 20px auto; text-align: center;">
        <thead>
        <tr>
          <th>Mã TT</th>
          <th>Mã ĐH</th>
          <th>Ngày thanh toán</th>
          <th>Số tiền</th>
          <th>Phương thức</th>
          <th>Trạng thái</th>
        </tr>
        </thead>
        <tbody>
          <%-- Dữ liệu 'payments' được gửi từ PaymentServlet --%>
        <c:forEach var="payment" items="${payments}">
          <tr>
            <td>#${payment.paymentId}</td>
            <td>#${payment.order.id}</td>
            <td><fmt:formatDate value="${payment.paymentDate}" pattern="dd-MM-yyyy HH:mm"/></td>
            <td><fmt:formatNumber value="${payment.amount}" type="currency" currencyCode="VND"/></td>
            <td>${payment.method}</td>
            <td>${payment.status}</td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </c:otherwise>
  </c:choose>
</main>
</body>
</html>