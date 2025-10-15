<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Tin tức chung | Honda Vietnam</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

    <style>
        body{
            font-family:'Inter',sans-serif;
            margin:0;
            background:#f6f7f9;
            color:#1b1b1b;
        }
        .container{width:min(1160px,92%);margin-inline:auto;}
        .topbar{position:sticky;top:0;background:#fff;border-bottom:1px solid #eee;z-index:10;}
        .nav{display:flex;justify-content:space-between;align-items:center;padding:14px 0;}
        .brand{display:flex;align-items:center;gap:10px;text-decoration:none;color:#000;font-weight:800;}
        .brand .logo{width:40px;height:40px;border-radius:10px;background:radial-gradient(100% 100% at 20% 20%,#ff8fa0,#d7000f);}
        .nav-actions a{margin-left:18px;text-decoration:none;color:#333;font-weight:600;}
        .nav-actions .primary{background:#d7000f;color:#fff;padding:8px 14px;border-radius:10px;}
        .nav-actions .primary:hover{background:#b1000c;}

        .hero{background:#0f0f10;color:#fff;border-radius:18px;margin-top:18px;overflow:hidden;}
        .hero-wrap{display:grid;grid-template-columns:1.2fr .8fr;gap:24px;padding:40px 26px;}
        .hero h1{font-size:clamp(28px,4vw,44px);margin:0 0 12px;}
        .hero p{color:#d6d6d6;margin-bottom:18px;}
        .btn{display:inline-block;padding:12px 16px;border-radius:12px;text-decoration:none;font-weight:700;}
        .btn-primary{background:#d7000f;color:#fff;}
        .btn-primary:hover{background:#b1000c;}
        .hero-img{border-radius:14px;min-height:220px;background:url('${pageContext.request.contextPath}/images/honda-news.jpg') center/cover no-repeat;}

        .grid{display:grid;gap:20px;grid-template-columns:repeat(auto-fill,minmax(320px,1fr));margin:30px 0;}
        .card{background:#fff;border:1px solid #eee;border-radius:16px;overflow:hidden;transition:transform .25s ease,box-shadow .25s ease;}
        .card:hover{transform:translateY(-4px);box-shadow:0 10px 25px rgba(0,0,0,.1);}
        .card-img{aspect-ratio:16/9;background-size:cover;background-position:center;}
        .card-body{padding:16px;}
        .card-body h3{margin:0 0 8px;font-size:18px;}
        .card-body p{color:#555;margin-bottom:12px;}
        .btn-outline{border:1px solid #ddd;padding:8px 12px;border-radius:10px;text-decoration:none;font-weight:600;color:#222;}
        .btn-outline:hover{background:#fafafa;}
        footer{text-align:center;margin:40px 0;color:#777;}

        /* =========================
           THEME: NEWS (sáng/xanh)
           ========================= */
        :root{ --brand:#0047ab; }
        .brand .logo{
            background:radial-gradient(100% 100% at 20% 20%, #cfe0ff, #0047ab);
        }
        .hero{
            background:#ffffff; color:#111; border:1px solid #eef0f3;
        }
        .hero p{ color:#555; }
        .btn-primary{ background:var(--brand); }
        .btn-primary:hover{ background:#003a8e; }
        .card{ border:1px solid #e8ecf2; box-shadow:0 6px 18px rgba(0,30,70,.06); }
        .card-img{ position:relative; background-image:linear-gradient(135deg, rgba(0,40,120,.04), rgba(0,40,120,.08)); }
        .card-img::after{
            content:"Tin mới"; position:absolute; top:12px; left:12px;
            background:#fff; color:#0047ab; border:1px solid #bfd3ff;
            padding:6px 10px; border-radius:999px; font-weight:800; font-size:12px;
            box-shadow:0 6px 14px rgba(0,71,171,.12);
        }
        .card-body h3{ font-size:20px; line-height:1.3; }
        .card-body p{ color:#444; }
    </style>
</head>
<body>
<div class="topbar">
    <div class="container">
        <div class="nav">
            <a class="brand" href="${pageContext.request.contextPath}/">
                <div class="logo"></div><span>Honda Vietnam</span>
            </a>
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/su-kien">Sự kiện</a>
                <a class="primary" href="${pageContext.request.contextPath}/dat-lich-lai-thu">Đặt lịch lái thử</a>
            </div>
        </div>
    </div>
</div>

<div class="container">
    <section class="hero">
        <div class="hero-wrap">
            <div>
                <h1>Tin tức chung</h1>
                <p>Cập nhật những thông tin mới nhất về Honda, công nghệ xe, hoạt động xã hội và các thông cáo báo chí chính thức.</p>
                <a class="btn btn-primary" href="#tin-tuc">Xem tin mới nhất</a>
            </div>
            <div class="hero-img" role="img" aria-label="Tin tức Honda"></div>
        </div>
    </section>

    <section id="tin-tuc" class="grid">
        <article class="card">
            <div class="card-img" style="background-image:url('${pageContext.request.contextPath}/images/news1.jpg');"></div>
            <div class="card-body">
                <h3>Honda giới thiệu công nghệ động cơ hybrid thế hệ mới</h3>
                <p>Honda chính thức ra mắt hệ thống e:HEV cải tiến giúp tiết kiệm nhiên liệu và tăng trải nghiệm lái.</p>
                <a class="btn-outline" href="${pageContext.request.contextPath}/tin-tuc/hybrid-moi">Đọc thêm</a>
            </div>
        </article>

        <article class="card">
            <div class="card-img" style="background-image:url('${pageContext.request.contextPath}/images/news2.jpg');"></div>
            <div class="card-body">
                <h3>Chiến dịch “Vững tay lái - Vững tương lai”</h3>
                <p>Honda Việt Nam phát động chiến dịch hướng đến an toàn giao thông và phát triển bền vững.</p>
                <a class="btn-outline" href="${pageContext.request.contextPath}/tin-tuc/vung-tay-lai">Đọc thêm</a>
            </div>
        </article>

        <article class="card">
            <div class="card-img" style="background-image:url('${pageContext.request.contextPath}/images/news3.jpg');"></div>
            <div class="card-body">
                <h3>Kỷ niệm 25 năm Honda Việt Nam</h3>
                <p>Sự kiện đặc biệt đánh dấu hành trình phát triển của Honda với nhiều hoạt động tri ân khách hàng.</p>
                <a class="btn-outline" href="${pageContext.request.contextPath}/tin-tuc/ky-niem-25-nam">Đọc thêm</a>
            </div>
        </article>
    </section>

    <footer>
        © <script>document.write(new Date().getFullYear())</script> Honda Vietnam. All rights reserved.
    </footer>
</div>
</body>
</html>
