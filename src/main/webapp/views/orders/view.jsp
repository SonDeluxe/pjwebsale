<%-- File: /views/orders/view.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container my-5">
    <h1 class="page-title">Chi tiết đơn hàng #${order.id}</h1>

    <div class="row">
        <div class="col-md-4">
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
                    <span class="badge bg-primary rounded-pill">${order.status}</span>
                </li>
                <li class="list-group-item">
                    Địa chỉ giao hàng:
                    <p class="mt-2"><strong>${order.deliveryAddress}</strong></p>
                </li>
            </ul>
        </div>

        <div class="col-md-8">
            <h4>Các sản phẩm đã đặt</h4>
            <table class="table table-hover">
                <thead class="table-light">
                <tr>
                    <th scope="col" colspan="2">Sản phẩm</th>
                    <th scope="col" class="text-center">Số lượng</th>
                    <th scope="col" class="text-end">Đơn giá</th>
                    <th scope="col" class="text-end">Thành tiền</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach var="item" items="${order.lineItems}">
                    <tr>
                        <td style="width: 80px;">
                            <img src="${pageContext.request.contextPath}/assets/images/${item.product.imageUrl}" class="img-fluid rounded" alt="${item.product.name}">
                        </td>
                        <td>
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
                    <td colspan="4" class="text-end"><strong>Tổng cộng:</strong></td>
                    <td class="text-end h5"><strong><fmt:formatNumber value="${order.totalAmount}" type="currency" currencyCode="VND"/></strong></td>
                </tr>
                </tfoot>
            </table>

            <div class="text-end mt-4">
                <a href="${pageContext.request.contextPath}/orders?action=list" class="btn btn-secondary">‹ Quay lại danh sách</a>
            </div>
        </div>
    </div>
</div>