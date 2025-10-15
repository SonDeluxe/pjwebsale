<%--<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>--%>
<%--<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>--%>

<%--<header class="honda-header">--%>
<%--    <div class="header-container">--%>
<%--        <div class="logo-section">--%>
<%--            <a href="${pageContext.request.contextPath}/">--%>
<%--                <img src="${pageContext.request.contextPath}/assets/images/logo2.png" alt="Honda Logo" class="honda-logo">--%>
<%--                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">--%>
<%--                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Under.css">--%>
<%--                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/search.css">--%>
<%--            </a>--%>
<%--        </div>--%>

<%--        <nav class="main-navigation">--%>
<%--            <ul>--%>
<%--                <li><a href="${pageContext.request.contextPath}/products?action=list">Sản phẩm</a></li>--%>
<%--                <li><a href="${pageContext.request.contextPath}/spareparts?action=list">Phụ tùng</a></li>--%>
<%--                <c:if test="${not empty sessionScope.user}">--%>
<%--                    <li><a href="${pageContext.request.contextPath}/orders?action=list">Đơn hàng</a></li>--%>
<%--                    <li><a href="${pageContext.request.contextPath}/warranties">Bảo hành</a></li>--%>
<%--                    <li><a href="${pageContext.request.contextPath}/carts?action=view">Giỏ hàng</a></li>--%>
<%--                </c:if>--%>
<%--                <li class="nav-item dropdown-news">--%>
<%--                    <a href="#" class="dropdown-toggle">Tin tức ▼</a>--%>
<%--                    <ul class="dropdown-menu">--%>
<%--                        <li><a href="${pageContext.request.contextPath}/news-general.jsp">Tin tức chung <span class="badge">3.6</span></a></li>--%>
<%--                        <li><a href="${pageContext.request.contextPath}/promotions.jsp">Tin khuyến mãi <span class="badge">1</span></a></li>--%>
<%--                        <li><a href="${pageContext.request.contextPath}/events.jsp">Hoạt động - Sự kiện <span class="badge">2</span></a></li>--%>
<%--                    </ul>--%>
<%--                </li>--%>
<%--            </ul>--%>
<%--        </nav>--%>

<%--        <div class="account-section">--%>
<%--            <div class="account-dropdown">--%>
<%--                <span class="account-btn">--%>
<%--                    <c:choose>--%>
<%--                        <c:when test="${empty sessionScope.user}">Tài khoản ▼</c:when>--%>
<%--                        <c:otherwise>Xin chào, ${sessionScope.user.name} ▼</c:otherwise>--%>
<%--                    </c:choose>--%>
<%--                </span>--%>
<%--                <div class="dropdown-content">--%>
<%--                    <c:if test="${empty sessionScope.user}">--%>
<%--                        <a href="${pageContext.request.contextPath}/auth?action=login">Đăng nhập</a>--%>
<%--                        <a href="${pageContext.request.contextPath}/auth?action=register">Đăng ký</a>--%>
<%--                    </c:if>--%>
<%--                    <c:if test="${not empty sessionScope.user}">--%>
<%--                        <a href="${pageContext.request.contextPath}/profile">Thông tin tài khoản</a>--%>
<%--                        <c:if test="${sessionScope.user.role == 'Admin'}">--%>
<%--                            <a href="${pageContext.request.contextPath}/admin">Trang Admin</a>--%>
<%--                        </c:if>--%>
<%--                        <a href="${pageContext.request.contextPath}/auth?action=logout">Đăng xuất</a>--%>
<%--                    </c:if>--%>
<%--                </div>--%>
<%--            </div>--%>
<%--        </div>--%>
<%--    </div>--%>
<%--</header>--%>

<%--<script>--%>
<%--    window.CONTEXT_PATH = '${pageContext.request.contextPath}';--%>

<%--    document.addEventListener('DOMContentLoaded', function() {--%>
<%--        const dropdownLi = document.querySelector('.dropdown-news');--%>
<%--        const toggle = document.querySelector('.dropdown-toggle');--%>
<%--        const menu = document.querySelector('.dropdown-menu');--%>

<%--        if (dropdownLi && menu) {--%>
<%--            dropdownLi.addEventListener('mouseenter', function() {--%>
<%--                menu.classList.add('active');--%>
<%--            });--%>
<%--            dropdownLi.addEventListener('mouseleave', function(e) {--%>
<%--                if (!menu.contains(e.relatedTarget)) {--%>
<%--                    menu.classList.remove('active');--%>
<%--                }--%>
<%--            });--%>

<%--            toggle.addEventListener('click', function(e) {--%>
<%--                e.preventDefault();--%>
<%--                e.stopPropagation();--%>
<%--                menu.classList.toggle('active');--%>
<%--            });--%>

<%--            document.addEventListener('click', function(e) {--%>
<%--                if (!dropdownLi.contains(e.target)) {--%>
<%--                    menu.classList.remove('active');--%>
<%--                }--%>
<%--            });--%>

<%--            menu.querySelectorAll('a').forEach(function(link) {--%>
<%--                link.addEventListener('click', function(e) {--%>
<%--                    menu.classList.remove('active');--%>
<%--                });--%>
<%--            });--%>
<%--        }--%>
<%--    });--%>
<%--</script>--%>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<header class="honda-header">
    <div class="header-container">
        <div class="logo-section">
            <a href="${pageContext.request.contextPath}/">
                <img src="${pageContext.request.contextPath}/assets/images/logo2.png" alt="Honda Logo" class="honda-logo">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/Under.css">
                <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/search.css">
            </a>
        </div>

        <nav class="main-navigation">
            <ul>
                <li><a href="${pageContext.request.contextPath}/products?action=list">Sản phẩm</a></li>
                <li><a href="${pageContext.request.contextPath}/spareparts?action=list">Phụ tùng</a></li>
                <c:if test="${not empty sessionScope.user}">
                    <li><a href="${pageContext.request.contextPath}/orders?action=list">Đơn hàng</a></li>
                    <li><a href="${pageContext.request.contextPath}/warranties">Bảo hành</a></li>
                    <li><a href="${pageContext.request.contextPath}/carts?action=view">Giỏ hàng</a></li>
                    <!-- Thêm mục Wishlist -->
                    <li><a href="${pageContext.request.contextPath}/wishlist/">Wishlist</a></li>
                </c:if>
                <li class="nav-item dropdown-news">
                    <a href="#" class="dropdown-toggle">Tin tức ▼</a>
                    <ul class="dropdown-menu">
                        <li><a href="${pageContext.request.contextPath}/news-general.jsp">Tin tức chung <span class="badge">3.6</span></a></li>
                        <li><a href="${pageContext.request.contextPath}/promotions.jsp">Tin khuyến mãi <span class="badge">1</span></a></li>
                        <li><a href="${pageContext.request.contextPath}/events.jsp">Hoạt động - Sự kiện <span class="badge">2</span></a></li>
                    </ul>
                </li>
            </ul>
        </nav>

        <div class="account-section">
            <div class="account-dropdown">
                    <span class="account-btn">
                        <c:choose>
                            <c:when test="${empty sessionScope.user}">Tài khoản ▼</c:when>
                            <c:otherwise>Xin chào, ${sessionScope.user.name} ▼</c:otherwise>
                        </c:choose>
                    </span>
                <div class="dropdown-content">
                    <c:if test="${empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/auth?action=login">Đăng nhập</a>
                        <a href="${pageContext.request.contextPath}/auth?action=register">Đăng ký</a>
                    </c:if>
                    <c:if test="${not empty sessionScope.user}">
                        <a href="${pageContext.request.contextPath}/profile">Thông tin tài khoản</a>
                        <c:if test="${sessionScope.user.role == 'Admin'}">
                            <a href="${pageContext.request.contextPath}/admin">Trang Admin</a>
                        </c:if>
                        <a href="${pageContext.request.contextPath}/auth?action=logout">Đăng xuất</a>
                    </c:if>
                </div>
            </div>
        </div>
    </div>
</header>

<script>
    window.CONTEXT_PATH = '${pageContext.request.contextPath}';

    document.addEventListener('DOMContentLoaded', function() {
        const dropdownLi = document.querySelector('.dropdown-news');
        const toggle = document.querySelector('.dropdown-toggle');
        const menu = document.querySelector('.dropdown-menu');

        if (dropdownLi && menu) {
            dropdownLi.addEventListener('mouseenter', function() {
                menu.classList.add('active');
            });
            dropdownLi.addEventListener('mouseleave', function(e) {
                if (!menu.contains(e.relatedTarget)) {
                    menu.classList.remove('active');
                }
            });

            toggle.addEventListener('click', function(e) {
                e.preventDefault();
                e.stopPropagation();
                menu.classList.toggle('active');
            });

            document.addEventListener('click', function(e) {
                if (!dropdownLi.contains(e.target)) {
                    menu.classList.remove('active');
                }
            });

            menu.querySelectorAll('a').forEach(function(link) {
                link.addEventListener('click', function(e) {
                    menu.classList.remove('active');
                });
            });
        }
    });
</script>