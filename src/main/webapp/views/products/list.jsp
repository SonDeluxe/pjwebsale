<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/styles/list.css">
<div class="container">
    <div class="product-page-layout">

        <%-- ======================= SIDEBAR (THANH BÊN) ======================= --%>
        <aside class="filter-sidebar">
            <div class="search-section-sidebar">
                <form action="${pageContext.request.contextPath}/products" method="GET" class="search-form">
                    <input type="hidden" name="action" value="list">
                    <input type="text" name="search" placeholder="Tìm kiếm sản phẩm..." class="search-input" value="${searchKeyword}">
                    <button type="submit" class="search-button">&#x1F50D;</button>
                </form>
            </div>

            <div class="filter-box">
                <div class="category-list">
                    <h3>Danh mục</h3>
                    <ul>
                        <c:url var="allCatLink" value="/products"><c:param name="action" value="list"/><c:if test="${not empty searchKeyword}"><c:param name="search" value="${searchKeyword}"/></c:if></c:url>
                        <li class="${empty categoryIdFilter ? 'active' : ''}"><a href="${allCatLink}">Tất cả sản phẩm</a></li>
                        <c:forEach var="cat" items="${categories}">
                            <c:url var="catLink" value="/products"><c:param name="action" value="list"/><c:if test="${not empty searchKeyword}"><c:param name="search" value="${searchKeyword}"/></c:if><c:param name="categoryId" value="${cat.categoryId}"/></c:url>
                            <li class="${categoryIdFilter == cat.categoryId ? 'active' : ''}"><a href="${catLink}">${cat.categoryName}</a></li>
                        </c:forEach>
                    </ul>
                </div>

                <div class="price-filter">
                    <h3>Lọc theo Giá</h3>
                    <form action="${pageContext.request.contextPath}/products" method="GET" class="price-input-form">
                        <input type="hidden" name="action" value="list">
                        <c:if test="${not empty categoryIdFilter}"><input type="hidden" name="categoryId" value="${categoryIdFilter}"></c:if>
                        <c:if test="${not empty searchKeyword}"><input type="hidden" name="search" value="${searchKeyword}"></c:if>
                        <div class="price-inputs">
                            <input type="number" name="minPrice" placeholder="Giá từ" value="${minPriceFilter}" class="form-control">
                            <span>-</span>
                            <input type="number" name="maxPrice" placeholder="Giá đến" value="${maxPriceFilter}" class="form-control">
                        </div>
                        <button type="submit">Áp dụng</button>
                    </form>
                    <ul class="price-ranges">
                        <c:url var="priceLink1" value="/products"><c:param name="action" value="list"/><c:if test="${not empty categoryIdFilter}"><c:param name="categoryId" value="${categoryIdFilter}"/></c:if><c:if test="${not empty searchKeyword}"><c:param name="search" value="${searchKeyword}"/></c:if><c:param name="minPrice" value="0"/><c:param name="maxPrice" value="50000000"/></c:url>
                        <li><a href="${priceLink1}">0 - 50 triệu</a></li>
                        <c:url var="priceLink2" value="/products"><c:param name="action" value="list"/><c:if test="${not empty categoryIdFilter}"><c:param name="categoryId" value="${categoryIdFilter}"/></c:if><c:if test="${not empty searchKeyword}"><c:param name="search" value="${searchKeyword}"/></c:if><c:param name="minPrice" value="50000001"/><c:param name="maxPrice" value="100000000"/></c:url>
                        <li><a href="${priceLink2}">50 - 100 triệu</a></li>
                        <c:url var="clearPriceLink" value="/products"><c:param name="action" value="list"/><c:if test="${not empty categoryIdFilter}"><c:param name="categoryId" value="${categoryIdFilter}"/></c:if><c:if test="${not empty searchKeyword}"><c:param name="search" value="${searchKeyword}"/></c:if></c:url>
                        <li class="clear-filter"><a href="${clearPriceLink}">Xóa lọc giá</a></li>
                    </ul>

                    <div class="toolbar">

                        <form id="sortForm" action="${pageContext.request.contextPath}/products" method="GET">
                            <input type="hidden" name="action" value="list">
                            <c:if test="${not empty categoryIdFilter}"><input type="hidden" name="categoryId" value="${categoryIdFilter}"></c:if>
                            <c:if test="${not empty searchKeyword}"><input type="hidden" name="search" value="${searchKeyword}"></c:if>
                            <c:if test="${not empty minPriceFilter}"><input type="hidden" name="minPrice" value="${minPriceFilter}"></c:if>
                            <c:if test="${not empty maxPriceFilter}"><input type="hidden" name="maxPrice" value="${maxPriceFilter}"></c:if>
                            <select name="sort" onchange="this.form.submit()" class="form-select">
                                <option value="name_asc" ${sortBy == 'name_asc' ? 'selected' : ''}>Sắp xếp: Mặc định</option>
                                <option value="price_asc" ${sortBy == 'price_asc' ? 'selected' : ''}>Giá: Tăng dần</option>
                                <option value="price_desc" ${sortBy == 'price_desc' ? 'selected' : ''}>Giá: Giảm dần</option>
                            </select>
                        </form>
                    </div>
                </div>
            </div>
        </aside>

        <%-- ======================= NỘI DUNG CHÍNH ======================= --%>
        <div class="product-main-content">
            <h1 class="page-title">Sản phẩm</h1>


            <%-- NÚT THÊM SẢN PHẨM CHO ADMIN --%>
            <c:if test="${sessionScope.user.role == 'Admin'}">
                <div style="margin-bottom: 20px;">
                    <a href="${pageContext.request.contextPath}/products?action=add" class="btn btn-primary">➕ Thêm sản phẩm mới</a>
                </div>
            </c:if>

            <c:if test="${empty products}"><div class="no-products-found"><p>Không tìm thấy sản phẩm nào phù hợp.</p></div></c:if>

            <div class="product-grid">
                <c:forEach var="product" items="${products}">
                    <div class="product-card">
                        <a href="${pageContext.request.contextPath}/products?action=view&id=${product.id}" class="product-card-image-link">
                            <img src="${pageContext.request.contextPath}/assets/images/${product.imageUrl}" alt="${product.name}">
                        </a>
                        <div class="product-info">
                            <h3><a href="${pageContext.request.contextPath}/products?action=view&id=${product.id}">${product.name}</a></h3>
                            <div class="price"><fmt:formatNumber value="${product.price}" type="currency" currencyCode="VND"/></div>
                            <div class="product-actions">
                                <button type="button" class="btn add-to-cart-btn" data-id="${product.id}" data-qty="1">🛒 Thêm vào giỏ</button>
                                <a href="${pageContext.request.contextPath}/products?action=view&id=${product.id}" class="btn btn-secondary">Xem chi tiết</a>
                            </div>
                                <%-- CÁC NÚT SỬA/XÓA CHO ADMIN --%>
                            <c:if test="${sessionScope.user.role == 'Admin'}">
                                <div class="admin-actions" style="margin-top: 10px; display: flex; gap: 10px;">
                                    <a href="${pageContext.request.contextPath}/products?action=edit&id=${product.id}" class="btn btn-sm btn-warning">Sửa</a>
                                    <form action="${pageContext.request.contextPath}/products" method="post" style="display:inline-block;">
                                        <input type="hidden" name="action" value="delete">
                                        <input type="hidden" name="id" value="${product.id}">
                                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này?');">Xóa</button>
                                    </form>
                                </div>
                            </c:if>
                        </div>
                    </div>
                </c:forEach>
            </div>

            <nav class="pagination">
                <c:if test="${totalPages > 1}">
                    <ul>
                        <c:if test="${currentPage > 1}"><c:url var="pageLink" value="/products"><c:param name="action" value="list"/><c:param name="page" value="${currentPage - 1}"/><c:if test="${not empty categoryIdFilter}"><c:param name="categoryId" value="${categoryIdFilter}"/></c:if><c:if test="${not empty searchKeyword}"><c:param name="search" value="${searchKeyword}"/></c:if><c:if test="${not empty minPriceFilter}"><c:param name="minPrice" value="${minPriceFilter}"/></c:if><c:if test="${not empty maxPriceFilter}"><c:param name="maxPrice" value="${maxPriceFilter}"/></c:if><c:if test="${not empty sortBy}"><c:param name="sort" value="${sortBy}"/></c:if></c:url><li><a href="${pageLink}">&laquo;</a></li></c:if>
                        <c:forEach begin="1" end="${totalPages}" var="i"><c:url var="pageLink" value="/products"><c:param name="action" value="list"/><c:param name="page" value="${i}"/><c:if test="${not empty categoryIdFilter}"><c:param name="categoryId" value="${categoryIdFilter}"/></c:if><c:if test="${not empty searchKeyword}"><c:param name="search" value="${searchKeyword}"/></c:if><c:if test="${not empty minPriceFilter}"><c:param name="minPrice" value="${minPriceFilter}"/></c:if><c:if test="${not empty maxPriceFilter}"><c:param name="maxPrice" value="${maxPriceFilter}"/></c:if><c:if test="${not empty sortBy}"><c:param name="sort" value="${sortBy}"/></c:if></c:url><li class="${currentPage == i ? 'active' : ''}"><a href="${pageLink}">${i}</a></li></c:forEach>
                        <c:if test="${currentPage < totalPages}"><c:url var="pageLink" value="/products"><c:param name="action" value="list"/><c:param name="page" value="${currentPage + 1}"/><c:if test="${not empty categoryIdFilter}"><c:param name="categoryId" value="${categoryIdFilter}"/></c:if><c:if test="${not empty searchKeyword}"><c:param name="search" value="${searchKeyword}"/></c:if><c:if test="${not empty minPriceFilter}"><c:param name="minPrice" value="${minPriceFilter}"/></c:if><c:if test="${not empty maxPriceFilter}"><c:param name="maxPrice" value="${maxPriceFilter}"/></c:if><c:if test="${not empty sortBy}"><c:param name="sort" value="${sortBy}"/></c:if></c:url><li><a href="${pageLink}">&raquo;</a></li></c:if>
                    </ul>
                </c:if>
            </nav>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/js/main.js"></script>
<script>
    document.addEventListener("DOMContentLoaded", function () {
        const buttons = document.querySelectorAll(".add-to-cart-btn");
        buttons.forEach((btn) => {
            btn.addEventListener("click", function () {
                const productId = this.dataset.id;
                const quantity = this.dataset.qty || 1;
                if (typeof handleAddToCart === 'function') {
                    handleAddToCart(productId, quantity);
                }
            });
        });
    });
</script>