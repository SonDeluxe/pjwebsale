<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Trang quản trị</title>
    <%-- Sử dụng CSS thuần cho admin --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/admin.css">
</head>
<body>
<%-- Nạp header chung --%>
<jsp:include page="/includes/header.jsp" />

<div class="admin-container">
    <div class="admin-header">
        <h1>Bảng điều khiển</h1>
    </div>

    <%-- Các thẻ thống kê --%>
    <div class="dashboard-stats">
        <div class="stat-card primary">
            <div class="stat-header">Tổng số người dùng</div>
            <div class="stat-body">${userCount}</div>
        </div>
        <div class="stat-card success">
            <div class="stat-header">Tổng sản phẩm</div>
            <div class="stat-body">${productCount}</div>
        </div>
        <div class="stat-card warning">
            <div class="stat-header">Đơn hàng mới</div>
            <div class="stat-body">${newOrderCount}</div>
        </div>
    </div>

    <%-- Danh sách các chức năng quản lý --%>
    <div class="management-links">
        <h3>Chức năng quản lý</h3>
        <a href="${pageContext.request.contextPath}/users?action=list">Quản lý Người dùng</a>
        <a href="${pageContext.request.contextPath}/products?action=adminList">Quản lý Sản phẩm</a>
        <a href="${pageContext.request.contextPath}/orders?action=list">Quản lý Đơn hàng</a>

        <%-- CÁC LINK ĐÃ ĐƯỢC CẬP NHẬT --%>
        <a href="${pageContext.request.contextPath}/spareparts?action=list">Quản lý Phụ tùng</a>
        <a href="${pageContext.request.contextPath}/sparepart-categories?action=list">Quản lý Danh mục Phụ tùng</a>
        <a href="${pageContext.request.contextPath}/warranties?action=adminList">Quản lý Bảo hành</a>
    </div>
</div>
</body>
</html>