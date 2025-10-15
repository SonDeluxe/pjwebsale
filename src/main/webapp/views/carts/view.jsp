<%-- /views/carts/view.jsp --%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<div class="container">
  <h1 class="cart-page-title">Giỏ hàng của bạn</h1>

  <c:if test="${empty items}">
    <div class="alert alert-info" style="text-align: center;">
      Giỏ hàng của bạn đang trống.
      <br><br>
      <a href="${pageContext.request.contextPath}/products?action=list" class="btn">Tiếp tục mua sắm</a>
    </div>
  </c:if>

  <c:if test="${not empty items}">
    <table class="cart-table">
      <thead>
      <tr>
        <th>Sản phẩm</th>
        <th>Giá</th>
        <th>Số lượng</th>
        <th>Tổng cộng</th>
        <th></th>
      </tr>
      </thead>
      <tbody>
      <c:forEach var="item" items="${items}">
        <tr>
          <td>
            <div class="product-item-info">
              <img src="${pageContext.request.contextPath}/assets/images/${item.product.imageUrl}" alt="${item.product.name}">
              <div>
                <strong>${item.product.name}</strong>
              </div>
            </div>
          </td>
          <td><fmt:formatNumber value="${item.unitPrice}" type="currency" currencyCode="VND"/></td>
          <td>
            <form class="quantity-form" action="${pageContext.request.contextPath}/carts" method="post">
              <input type="hidden" name="action" value="updateItem">
              <input type="hidden" name="lineItemId" value="${item.id}">
              <input type="number" name="quantity" value="${item.quantity}" min="1" onchange="this.form.submit()">
            </form>
          </td>
          <td><strong><fmt:formatNumber value="${item.totalPrice}" type="currency" currencyCode="VND"/></strong></td>
          <td>
            <form action="${pageContext.request.contextPath}/carts" method="post">
              <input type="hidden" name="action" value="removeItem">
              <input type="hidden" name="lineItemId" value="${item.id}">
              <button type="submit" style="background:none; border:none; color:var(--color-primary); cursor:pointer;">Xóa</button>
            </form>
          </td>
        </tr>
      </c:forEach>
      </tbody>
    </table>

    <div class="cart-summary">
      <div class="summary-box">
        <div class="total">
          <span>Tổng tiền:</span>
          <span class="price"><fmt:formatNumber value="${total}" type="currency" currencyCode="VND"/></span>
        </div>
        <a href="${pageContext.request.contextPath}/orders?action=create" class="btn checkout-btn">Tiến hành thanh toán</a>
      </div>
    </div>
  </c:if>
</div>