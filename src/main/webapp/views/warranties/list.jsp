<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- Gắn stylesheet riêng cho trang Bảo hành -->
<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/warranty.css"/>

<div class="warranty-page">
    <h1 class="title">Danh Sách Phiếu Bảo Hành</h1>
    <p class="subtitle">Danh sách các phiếu bảo hành đang được áp dụng trong hệ thống.</p>

    <div class="warranty-card">
        <div class="warranty-actions">
            <button class="btn" onclick="window.print()">In</button>
            <button class="btn brand">Xuất CSV</button>
        </div>

        <div class="table-responsive">
            <table class="warranty-table table table-hover align-middle">
                <thead>
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
                        <td data-label="ID">${w.id}</td>
                        <td data-label="Tên người dùng">${w.user.name}</td>
                        <td data-label="Email">${w.user.email}</td>
                        <td data-label="Sản phẩm" class="warranty-product">${w.product.name}</td>
                        <td data-label="Ngày bắt đầu"><fmt:formatDate value="${w.startDateAsDate}" pattern="dd/MM/yyyy"/></td>
                        <td data-label="Ngày kết thúc"><fmt:formatDate value="${w.endDateAsDate}" pattern="dd/MM/yyyy"/></td>

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

                        <td data-label="Ghi chú" class="note">${w.notes}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>
        </div>
    </div>

    <c:if test="${empty warranties}">
        <div class="warranty-card" style="padding:18px;">
            Chưa có phiếu bảo hành nào trong hệ thống.
        </div>
    </c:if>
</div>
