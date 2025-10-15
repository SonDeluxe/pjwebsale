<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>${pageTitle} - Honda</title>

    <%-- SỬA LẠI CÁC ĐƯỜNG DẪN BÊN DƯỚI --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">

    <c:if test="${not empty customCss}">
        <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/${customCss}">
    </c:if>
</head>

<%-- ⚡️ Thêm data-context-path để JS lấy được contextPath --%>
<body data-context-path="${pageContext.request.contextPath}">

<%-- 🧩 Header chung --%>
<jsp:include page="/includes/header.jsp" />

<%-- 📄 Nội dung trang động (content) --%>
<main class="main-container">
    <jsp:include page="${bodyContent}" />
</main>

<%-- 🧱 Footer chung --%>
<jsp:include page="/includes/footer.jsp" />

<%-- ⚙️ Script JS cuối trang để tối ưu hiệu suất --%>
<script src="${pageContext.request.contextPath}/js/main.js"></script>
</body>
</html>
