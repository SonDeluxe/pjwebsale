<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <title>Thông tin tài khoản</title>
   <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base.css">
</head>
<body>
<jsp:include page="/includes/header.jsp" />
<div class="container mt-5">
    <div class="card">
        <div class="card-header">
            <h2>Thông tin tài khoản</h2>
        </div>
        <div class="card-body">
            <ul class="list-group list-group-flush">
                <li class="list-group-item"><strong>Tên đăng nhập:</strong> ${sessionScope.user.name}</li>
                <li class="list-group-item"><strong>Email:</strong> ${sessionScope.user.email}</li>
                <li class="list-group-item"><strong>Địa chỉ:</strong> ${sessionScope.user.address}</li>
                <li class="list-group-item"><strong>Số điện thoại:</strong> ${sessionScope.user.phone}</li>
                <li class="list-group-item"><strong>Vai trò:</strong> ${sessionScope.user.role}</li>
            </ul>
        </div>
    </div>
</div>
<jsp:include page="/includes/footer.jsp" />
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
