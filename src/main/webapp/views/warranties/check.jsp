<%-- src/main/webapp/views/warranties/check.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Gắn stylesheet riêng cho trang Bảo hành -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/warranty.css"/>

<div class="warranty-page">
  <h1 class="title">Lịch sử bảo hành của bạn</h1>
  <p class="subtitle">Dưới đây là danh sách các sản phẩm của bạn đang được áp dụng chính sách bảo hành.</p>

  <c:if test="${not empty warranties}">
    <div class="warranty-card">
      <table class="warranty-table">
        <thead>
        <tr>
          <th>Sản phẩm</th>
          <th>Ngày bắt đầu</th>
          <th>Ngày kết thúc</th>
          <th>Trạng thái</th>
          <th>Ghi chú</th>
        </tr>
        </thead>

        <tbody>
        <c:forEach var="w" items="${warranties}">
          <tr>
            <td class="warranty-product" data-label="Sản phẩm">
                ${w.product.name}
            </td>

            <td data-label="Ngày bắt đầu">
              <fmt:formatDate value="${w.startDateAsDate}" pattern="dd/MM/yyyy"/>
            </td>

            <td data-label="Ngày kết thúc">
              <fmt:formatDate value="${w.endDateAsDate}" pattern="dd/MM/yyyy"/>
            </td>

            <td data-label="Trạng thái">
              <c:choose>
                <c:when test="${w.status eq 'Còn hạn'}">
                  <span class="status status-active"><i></i>${w.status}</span>
                </c:when>
                <c:when test="${w.status eq 'Sắp hết hạn'}">
                  <span class="status status-soon"><i></i>${w.status}</span>
                </c:when>
                <c:when test="${w.status eq 'Hết hạn'}">
                  <span class="status status-expired"><i></i>${w.status}</span>
                </c:when>
                <c:otherwise>
                  <span class="status"><i></i>${w.status}</span>
                </c:otherwise>
              </c:choose>
            </td>

            <td class="note" data-label="Ghi chú">
                ${w.notes}
            </td>
          </tr>
        </c:forEach>
        </tbody>
      </table>
    </div>
  </c:if>

  <c:if test="${empty warranties}">
    <div class="warranty-card" style="padding:18px;">
      Bạn chưa có lịch sử bảo hành nào.
    </div>
  </c:if>
</div>
