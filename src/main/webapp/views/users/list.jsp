<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <title>Quản lý người dùng</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
<jsp:include page="/includes/header.jsp" />
<div class="container mt-4">
    <div class="d-flex justify-content-between align-items-center mb-4">
        <h1>Quản lý người dùng</h1>
        <a href="${pageContext.request.contextPath}/users?action=add" class="btn btn-primary">+ Thêm người dùng</a>
    </div>

    <c:if test="${not empty sessionScope.success}">
        <div class="alert alert-success">${sessionScope.success}</div>
        <c:remove var="success" scope="session" />
    </c:if>

    <table class="table table-striped table-hover">
        <thead class="table-dark">
        <tr>
            <th>ID</th>
            <th>Username</th>
            <th>Email</th>
            <th>Vai trò</th>
            <th>Hành động</th>
        </tr>
        </thead>
        <tbody>
        <c:forEach var="u" items="${users}">
            <tr>
                <td>${u.id}</td>
                <td>${u.name}</td>
                <td>${u.email}</td>
                <td><span class="badge ${u.role == 'Admin' ? 'bg-danger' : 'bg-secondary'}">${u.role}</span></td>
                <td>
                    <a href="${pageContext.request.contextPath}/users?action=edit&id=${u.id}" class="btn btn-sm btn-primary">Sửa</a>
                    <form action="${pageContext.request.contextPath}/users" method="post" style="display: inline-block;">
                        <input type="hidden" name="action" value="delete">
                        <input type="hidden" name="id" value="${u.id}">
                        <button type="submit" class="btn btn-sm btn-danger" onclick="return confirm('Bạn có chắc muốn xóa người dùng này?')">Xóa</button>
                    </form>
                </td>
            </tr>
        </c:forEach>
        </tbody>
    </table>
</div>
<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>