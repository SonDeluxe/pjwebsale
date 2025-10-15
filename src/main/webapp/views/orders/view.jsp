<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link href="${pageContext.request.contextPath}/styles/view_order.css" rel="stylesheet">

<div class="order-detail-container">
    <h1 class="page-title">Chi tiết đơn hàng #${order.id}</h1>

    <div class="row">
        <div class="col-md-4">
            <div class="order-info-section">
                <h4>Thông tin chung</h4>
                <ul class="list-group">
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Mã đơn hàng:
                        <span><strong>#${order.id}</strong></span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Ngày đặt:
                        <span><fmt:formatDate value="${order.orderDate}" pattern="HH:mm dd/MM/yyyy"/></span>
                    </li>
                    <li class="list-group-item d-flex justify-content-between align-items-center">
                        Trạng thái:
                        <c:choose>
                            <c:when test="${order.status == 'Đã thanh toán'}">
                                <span class="badge status-paid">${order.status}</span>
                            </c:when>
                            <c:when test="${order.status == 'Chờ xử lý'}">
                                <span class="badge status-pending">${order.status}</span>
                            </c:when>
                            <c:otherwise>
                                <span class="badge status-cancelled">${order.status}</span>
                            </c:otherwise>
                        </c:choose>
                    </li>
                    <li class="list-group-item">
                        Địa chỉ giao hàng:
                        <p class="delivery-address mt-2">${order.deliveryAddress}</p>
                    </li>
                </ul>
            </div>
        </div>

        <div class="col-md-8">
            <div class="order-info-section">
                <h4>Các sản phẩm đã đặt</h4>
                <table class="table product-table">
                    <thead>
                    <tr>
                        <th class="text-start">Sản phẩm</th>
                        <th class="text-center">Số lượng</th>
                        <th class="text-end">Đơn giá</th>
                        <th class="text-end">Thành tiền</th>
                    </tr>
                    </thead>
                    <tbody>
                    <c:forEach var="item" items="${order.lineItems}">
                        <tr>
                            <td class="text-start">
                                <strong>${item.product.name}</strong>
                            </td>
                            <td class="text-center">${item.quantity}</td>
                            <td class="text-end"><fmt:formatNumber value="${item.unitPrice}" type="currency" currencyCode="VND"/></td>
                            <td class="text-end"><fmt:formatNumber value="${item.totalPrice}" type="currency" currencyCode="VND"/></td>
                        </tr>
                    </c:forEach>
                    </tbody>
                    <tfoot>
                    <tr>
                        <td colspan="3" class="text-end"><strong>Tổng cộng:</strong></td>
                        <td class="text-end total-amount"><strong><fmt:formatNumber value="${order.totalAmount}" type="currency" currencyCode="VND"/></strong></td>
                    </tr>
                    </tfoot>
                </table>

                <div class="text-end mt-4">
                    <a href="${pageContext.request.contextPath}/orders?action=list" class="btn btn-back">‹ Quay lại danh sách</a>
                </div>
            </div>
        </div>
    </div>
</div>