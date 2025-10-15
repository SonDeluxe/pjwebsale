<%--&lt;%&ndash; src/main/webapp/views/warranties/check.jsp &ndash;%&gt;--%>
<%--<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>
<%--&lt;%&ndash; BẠN KHÔNG CẦN DÙNG THƯ VIỆN FMT NỮA &ndash;%&gt;--%>
<%--&lt;%&ndash; <%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %> &ndash;%&gt;--%>

<%--<div>--%>
<%--  <h2>Lịch sử bảo hành của bạn</h2>--%>
<%--  <p>Dưới đây là danh sách các sản phẩm của bạn đang được áp dụng chính sách bảo hành.</p>--%>

<%--  <c:if test="${not empty warranties}">--%>
<%--    <table class="custom-table">--%>
<%--      <thead>--%>
<%--      <tr>--%>
<%--        <th>Sản phẩm</th>--%>
<%--        <th>Ngày bắt đầu</th>--%>
<%--        <th>Ngày kết thúc</th>--%>
<%--        <th>Trạng thái</th>--%>
<%--        <th>Ghi chú</th>--%>
<%--      </tr>--%>
<%--      </thead>--%>
<%--      <tbody>--%>
<%--      <c:forEach var="w" items="${warranties}">--%>
<%--        <tr>--%>
<%--          <td><strong>${w.product.name}</strong></td>--%>

<%--            &lt;%&ndash; SỬA Ở ĐÂY: Hiển thị trực tiếp, không dùng fmt:formatDate &ndash;%&gt;--%>
<%--          <td>${w.startDate}</td>--%>
<%--          <td>${w.endDate}</td>--%>

<%--          <td>--%>
<%--            <span class="badge ${w.status == 'Còn hạn' ? 'badge-success' : 'badge-secondary'}">--%>
<%--                ${w.status}--%>
<%--            </span>--%>
<%--          </td>--%>
<%--          <td>${w.notes}</td>--%>
<%--        </tr>--%>
<%--      </c:forEach>--%>
<%--      </tbody>--%>
<%--    </table>--%>
<%--  </c:if>--%>
<%--  <c:if test="${empty warranties}">--%>
<%--    <div class="alert alert-info">--%>
<%--      Bạn chưa có lịch sử bảo hành nào.--%>
<%--    </div>--%>
<%--  </c:if>--%>
<%--</div>--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div>
  <h2>Lịch sử bảo hành của bạn</h2>
  <p>Dưới đây là danh sách các sản phẩm của bạn đang được áp dụng chính sách bảo hành.</p>

  <c:if test="${not empty warranties}">
    <table class="custom-table">
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
          <td><strong>${w.product.name}</strong></td>
          <td><fmt:formatDate value="${w.startDateAsDate}" pattern="dd/MM/yyyy"/></td>
          <td><fmt:formatDate value="${w.endDateAsDate}" pattern="dd/MM/yyyy"/></td>
          <td>
            <span class="badge ${w.status == 'Còn hạn' ? 'badge-success' : 'badge-secondary'}">
                ${w.status}
            </span>
          </td>
          <td>${w.notes}</td>
        </tr>
      </c:forEach>
      </tbody>
    </table>
  </c:if>
  <c:if test="${empty warranties}">
    <div class="alert alert-info">
      Bạn chưa có lịch sử bảo hành nào.
    </div>
  </c:if>
</div>