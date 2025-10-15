<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container mt-4">
    <h2 class="mb-3 text-center">Danh Sách Phiếu Bảo Hành</h2>

    <c:if test="${not empty warranties}">
        <table class="table table-bordered table-striped text-center align-middle">
            <thead class="table-dark">
            <tr>
                <th>ID</th>
                <th>Tên người dùng</th>
                <th>Email</th>
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
                    <td>${w.id}</td>
                    <td>${w.user.name}</td>
                    <td>${w.user.email}</td>
                    <td><strong>${w.product.name}</strong></td>
                    <td><fmt:formatDate value="${w.startDateAsDate}" pattern="dd/MM/yyyy"/></td>
                    <td><fmt:formatDate value="${w.endDateAsDate}" pattern="dd/MM/yyyy"/></td>
                    <td>
              <span class="badge ${w.status == 'Còn hạn' ? 'bg-success' : 'bg-secondary'}">
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
        <div class="alert alert-info text-center">
            Chưa có phiếu bảo hành nào trong hệ thống.
        </div>
    </c:if>
</div>

<style>
    table {
        border-radius: 10px;
        overflow: hidden;
    }
    .badge {
        padding: 5px 10px;
        border-radius: 12px;
        font-size: 0.9rem;
    }
</style>
