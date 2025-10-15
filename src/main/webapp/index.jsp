<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Honda - Trang chủ</title>

    <%-- Nạp CSS theo thứ tự --%>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/base.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/index.css">

    <style>
        /* --- ĐẨY NỘI DUNG XUỐNG DƯỚI HEADER CỐ ĐỊNH --- */
        body {
            padding-top: 80px !important; /* vừa đủ với chiều cao header */
            background-color: #fff;
        }

        /* --- FIX HERO VIDEO KHÔNG BỊ CHE --- */
        main {
            margin-top: 0;
        }
    </style>
</head>

<body>
<%-- Nạp header chung (cố định trên cùng) --%>
<jsp:include page="/includes/header.jsp" />

<%-- Nội dung chính trang chủ --%>
<main>
    <section class="hero">
        <video autoplay muted loop playsinline>
            <source src="${pageContext.request.contextPath}/assets/videos/honda1.mp4" type="video/mp4">
        </video>
        <div class="hero-text">
            <h1>HONDA</h1>
            <p>The Power of Dreams</p>
        </div>
    </section>

    <div class="container">
        <section class="about">
            <h2 class="page-title">Về chúng tôi</h2>
            <p id="intro-text" style="min-height: 80px;"></p>
        </section>

        <h2 class="page-title">Bạn muốn mua xe?</h2>
        <section class="category-grid">
            <div class="category-item" onclick="window.location.href='${pageContext.request.contextPath}/products?action=list'">
                <img src="${pageContext.request.contextPath}/assets/images/civic.jpg" alt="Ô tô">
                <div class="category-item-content">
                    <div class="label">Ô tô</div>
                </div>
            </div>
            <div class="category-item" onclick="window.location.href='${pageContext.request.contextPath}/products?action=list'">
                <img src="${pageContext.request.contextPath}/assets/images/CB650R.jpg" alt="Xe máy">
                <div class="category-item-content">
                    <div class="label">Xe máy</div>
                </div>
            </div>
        </section>
    </div>
</main>

<%-- Footer --%>
<jsp:include page="/includes/footer.jsp" />

<%-- Script hiệu ứng gõ chữ --%>
<script>
    const text = "Chào mừng bạn đến với Honda - nơi sức mạnh của những giấc mơ dẫn lối. Chúng tôi tự hào mang đến những sản phẩm chất lượng và trải nghiệm vượt trội.";
    let i = 0;
    function typeWriter() {
        if (i < text.length) {
            document.getElementById("intro-text").innerHTML += text.charAt(i++);
            setTimeout(typeWriter, 40);
        }
    }
    window.addEventListener('load', typeWriter);
</script>
</body>
</html>
