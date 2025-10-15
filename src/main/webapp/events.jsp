<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8" />
    <title>Hoạt động - Sự kiện | Honda</title>
    <meta name="viewport" content="width=device-width, initial-scale=1" />
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link rel="preconnect" href="https://fonts.googleapis.com">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;700&display=swap" rel="stylesheet">

    <style>
        :root{
            --brand:#d7000f; /* Honda-ish red */
            --brand-dark:#b1000c;
            --ink:#1b1b1b;
            --muted:#5a5a5a;
            --bg:#0f0f10;
            --card:#ffffff;
            --ring: rgba(215,0,15,.25);
        }
        *{box-sizing:border-box}
        html,body{margin:0;padding:0}
        body{
            font-family:'Inter',system-ui,-apple-system,Segoe UI,Roboto,Helvetica,Arial,sans-serif;
            color:#111; background:#f6f7f9; line-height:1.5;
        }

        /* Topbar */
        .topbar{position:sticky; top:0; z-index:50; background:linear-gradient(180deg,#fff,#fff0);
            backdrop-filter:saturate(150%) blur(6px); border-bottom:1px solid #eee;}
        .container{width:min(1160px,92%); margin-inline:auto;}
        .nav{display:flex; align-items:center; justify-content:space-between; padding:14px 0;}
        .brand{display:flex; align-items:center; gap:10px; text-decoration:none; color:var(--ink); font-weight:800; letter-spacing:.3px;}
        .brand .logo{width:40px; height:40px; border-radius:10px;
            background:radial-gradient(120% 120% at 20% 20%, #ff8fa0, var(--brand));
            box-shadow:0 8px 24px rgba(215,0,15,.25), inset 0 1px 0 #fff7f7;}
        .nav-actions a{ text-decoration:none; color:#333; font-weight:600; margin-left:18px; padding:8px 14px; border-radius:10px;}
        .nav-actions .primary{ background:var(--brand); color:#fff; box-shadow:0 6px 18px var(--ring);}
        .nav-actions .primary:hover{ background:var(--brand-dark)}

        /* Hero */
        .hero{ position:relative; overflow:hidden; border-radius:18px; background:#0f0f10; color:#fff; margin-top:18px;}
        .hero-wrap{ display:grid; grid-template-columns:1.15fr .85fr; gap:24px; padding:40px 26px 26px;}
        .hero h1{ font-size:clamp(28px,4vw,44px); line-height:1.1; margin:0 0 12px; letter-spacing:.2px;}
        .hero p{ color:#d6d6d6; margin:0 0 16px}
        .cta{ display:flex; gap:12px; flex-wrap:wrap; margin-top:10px;}
        .btn{ display:inline-flex; align-items:center; gap:10px; padding:12px 16px; border-radius:12px; text-decoration:none; font-weight:700;}
        .btn-primary{ background:var(--brand); color:#fff; box-shadow:0 8px 24px var(--ring)}
        .btn-primary:hover{ background:var(--brand-dark)}
        .btn-ghost{ border:1px solid #333; color:#fff; background:transparent}
        .hero-tag{ display:inline-flex; align-items:center; gap:8px; font-weight:700; background:#151517; color:#fff; border:1px solid #252527;
            padding:6px 10px; border-radius:999px; margin-bottom:14px;}
        .hero-img{ position:relative; min-height:220px; border-radius:14px; overflow:hidden;
            background:url("${pageContext.request.contextPath}/images/honda-hero.jpg") center/cover no-repeat;
            box-shadow:0 20px 60px rgba(0,0,0,.35); border:1px solid #262628;}
        .hero-gradient{ position:absolute; inset:0;
            background: radial-gradient(60% 80% at 70% 30%, rgba(255,255,255,.12), transparent 60%),
            radial-gradient(80% 80% at 20% 80%, rgba(215,0,15,.25), transparent 60%); pointer-events:none;}

        /* Breadcrumbs */
        .crumbs{ display:flex; gap:8px; align-items:center; color:#777; font-size:14px; margin:18px 0 8px;}
        .crumbs a{ color:#555; text-decoration:none}
        .crumbs .sep{opacity:.4}

        /* Section header */
        .section-head{ display:flex; align-items:baseline; justify-content:space-between; margin:18px 0 10px;}
        .section-head h2{ margin:0; font-size:clamp(22px,3vw,28px)}
        .section-head p{ margin:0; color:#666}

        /* Cards grid */
        .grid{ display:grid; gap:18px; grid-template-columns:repeat(3,1fr);}
        @media (max-width:980px){ .hero-wrap{grid-template-columns:1fr} .grid{grid-template-columns:repeat(2,1fr)} }
        @media (max-width:640px){ .grid{grid-template-columns:1fr} }

        .card{ background:var(--card); border-radius:16px; overflow:hidden; border:1px solid #eee;
            transition: transform .25s ease, box-shadow .25s ease, border-color .25s ease;}
        .card:hover{ transform:translateY(-4px); box-shadow:0 16px 38px rgba(0,0,0,.08); border-color:#e5e5e5}
        .card-img{ position:relative; aspect-ratio:16/9; background:#ddd; background-size:cover; background-position:center;}
        .badge{ position:absolute; top:12px; left:12px; background:#fff; color:var(--brand); border:1px solid #ffd2d6;
            padding:6px 10px; border-radius:999px; font-weight:800; font-size:12px; box-shadow:0 6px 14px rgba(215,0,15,.15);}
        .card-body{ padding:14px }
        .title{ font-size:18px; font-weight:800; margin:0 0 6px; color:var(--ink)}
        .meta{ display:flex; gap:12px; flex-wrap:wrap; color:var(--muted); font-size:14px; margin-bottom:10px}
        .meta .pill{ display:inline-flex; align-items:center; gap:6px; padding:6px 10px; border:1px solid #eee; border-radius:999px; background:#fafafa; }
        .desc{ color:#555; margin:0 0 12px; min-height:44px}
        .actions{ display:flex; gap:10px; }
        .btn-outline{ border:1px solid #e6e6e6; border-radius:12px; padding:10px 14px; text-decoration:none; color:#222; font-weight:700;}
        .btn-outline:hover{ border-color:#ccc; background:#fafafa}
        .btn-danger{ background:var(--brand); color:#fff; border-radius:12px; padding:10px 14px; text-decoration:none; font-weight:800; box-shadow:0 8px 22px var(--ring);}
        .btn-danger:hover{ background:var(--brand-dark)}

        /* Footer */
        footer{ margin:40px 0 26px; color:#777; font-size:14px; text-align:center;}
        .back-home{ display:inline-flex; align-items:center; gap:8px; margin-top:10px; text-decoration:none; color:#333; font-weight:700;}
        .back-home:hover{ text-decoration:underline}

        /* =========================
           THEME: EVENTS (đậm/đỏ)
           ========================= */
        body{ background:#101113; }
        .hero{
            background:linear-gradient(160deg,#121315 0%,#1a1b1e 60%,#2a0b0d 100%);
        }
        .hero h1{ letter-spacing:.2px; text-shadow:0 8px 24px rgba(0,0,0,.4); }
        .card{ border:1px solid #2a2b2e; background:#141518; color:#e9e9ea; }
        .card .desc{ color:#cfd0d2; }
        .card-img{ filter:saturate(1.12) contrast(1.05); }
        .btn-outline{ color:#e9e9ea; border-color:#3a3b3e; }
        .badge{ background:#fff; color:#d7000f; border-color:#ffd2d6; box-shadow:0 10px 26px rgba(215,0,15,.2); }
        /* Fallback nếu chưa có ảnh: tạo nền pattern nhẹ */
        .card-img{ background-image:linear-gradient(135deg, rgba(255,255,255,.05), rgba(255,255,255,.08)); }
    </style>
</head>
<body>

<!-- NAV -->
<div class="topbar">
    <div class="container">
        <div class="nav">
            <a class="brand" href="${pageContext.request.contextPath}/">
                <div class="logo" aria-hidden="true"></div>
                <span>Honda Vietnam</span>
            </a>
            <div class="nav-actions">
                <a href="${pageContext.request.contextPath}/san-pham">Sản phẩm</a>
                <a href="${pageContext.request.contextPath}/khuyen-mai">Khuyến mãi</a>
                <a class="primary" href="${pageContext.request.contextPath}/dat-lich-lai-thu">Đặt lịch lái thử</a>
            </div>
        </div>
    </div>
</div>

<!-- HERO -->
<div class="container">
    <nav class="crumbs" aria-label="breadcrumbs">
        <a href="${pageContext.request.contextPath}/">Trang chủ</a>
        <span class="sep">/</span>
        <span>Hoạt động & Sự kiện</span>
    </nav>

    <section class="hero" aria-label="Giới thiệu sự kiện">
        <div class="hero-wrap">
            <div>
                <span class="hero-tag">
                    <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M7 2v3M17 2v3M3 9h18M4 7h16a1 1 0 0 1 1 1v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a1 1 0 0 1 1-1Z" stroke="#fff" stroke-width="1.5" stroke-linecap="round"/></svg>
                    Sự kiện nổi bật tháng 10
                </span>
                <h1>Hoạt động &amp; Sự kiện Honda</h1>
                <p>Chào mừng bạn! Khám phá các sự kiện sắp tới, đăng ký tham dự để nhận ưu đãi độc quyền và trải nghiệm các mẫu xe mới nhất.</p>
                <div class="cta">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/dat-cho-su-kien">Đặt chỗ ngay</a>
                    <a class="btn btn-ghost" href="#su-kien">Xem danh sách sự kiện</a>
                </div>
            </div>
            <div class="hero-img" role="img" aria-label="Ảnh xe Honda">
                <div class="hero-gradient"></div>
            </div>
        </div>
    </section>

    <!-- LIST -->
    <div class="section-head" id="su-kien">
        <h2>Sự kiện sắp diễn ra</h2>
        <p>Đăng ký sớm để giữ chỗ và nhận quà tặng khi tham dự.</p>
    </div>

    <section class="grid" aria-label="Danh sách sự kiện">
        <!-- Card 1 -->
        <article class="card">
            <div class="card-img" style="background-image:url('${pageContext.request.contextPath}/images/event-ra-mat.jpg');">
                <span class="badge">20/10/2025</span>
            </div>
            <div class="card-body">
                <h3 class="title">Ra mắt mẫu xe mới</h3>
                <div class="meta">
                    <span class="pill" title="Địa điểm">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M12 22s7-4.5 7-11a7 7 0 1 0-14 0c0 6.5 7 11 7 11Z" stroke="currentColor" stroke-width="1.5"/><circle cx="12" cy="11" r="2.5" stroke="currentColor" stroke-width="1.5"/></svg>
                        TP.HCM
                    </span>
                    <span class="pill" title="Giờ">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M12 7v5l3 2" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.5"/></svg>
                        9:00 - 17:00
                    </span>
                </div>
                <p class="desc">Tham quan, lái thử và nhận ưu đãi khi đặt cọc trong ngày ra mắt mẫu xe hoàn toàn mới.</p>
                <div class="actions">
                    <a class="btn-outline" href="${pageContext.request.contextPath}/su-kien/ra-mat-201025">Chi tiết</a>
                    <a class="btn-danger" href="${pageContext.request.contextPath}/dang-ky?event=ra-mat-201025">Đăng ký tham dự</a>
                </div>
            </div>
        </article>

        <!-- Card 2 -->
        <article class="card">
            <div class="card-img" style="background-image:url('${pageContext.request.contextPath}/images/event-trien-lam.jpg');">
                <span class="badge">25/10/2025</span>
            </div>
            <div class="card-body">
                <h3 class="title">Triển lãm xe máy Honda</h3>
                <div class="meta">
                    <span class="pill" title="Địa điểm">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M12 22s7-4.5 7-11a7 7 0 1 0-14 0c0 6.5 7 11 7 11Z" stroke="currentColor" stroke-width="1.5"/><circle cx="12" cy="11" r="2.5" stroke="currentColor" stroke-width="1.5"/></svg>
                        Hà Nội
                    </span>
                    <span class="pill" title="Giờ">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M12 7v5l3 2" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.5"/></svg>
                        10:00 - 20:00
                    </span>
                </div>
                <p class="desc">Chiêm ngưỡng lineup mới nhất, workshop chăm sóc xe và check-in nhận quà.</p>
                <div class="actions">
                    <a class="btn-outline" href="${pageContext.request.contextPath}/su-kien/trien-lam-251025">Chi tiết</a>
                    <a class="btn-danger" href="${pageContext.request.contextPath}/dang-ky?event=trien-lam-251025">Đăng ký tham dự</a>
                </div>
            </div>
        </article>

        <!-- Card 3 -->
        <article class="card">
            <div class="card-img" style="background-image:url('${pageContext.request.contextPath}/images/event-lai-thu.jpg');">
                <span class="badge">31/10/2025</span>
            </div>
            <div class="card-body">
                <h3 class="title">Ngày hội lái thử toàn quốc</h3>
                <div class="meta">
                    <span class="pill" title="Địa điểm">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M12 22s7-4.5 7-11a7 7 0 1 0-14 0c0 6.5 7 11 7 11Z" stroke="currentColor" stroke-width="1.5"/><circle cx="12" cy="11" r="2.5" stroke="currentColor" stroke-width="1.5"/></svg>
                        Nhiều tỉnh thành
                    </span>
                    <span class="pill" title="Giờ">
                        <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M12 7v5l3 2" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/><circle cx="12" cy="12" r="9" stroke="currentColor" stroke-width="1.5"/></svg>
                        8:00 - 18:00
                    </span>
                </div>
                <p class="desc">Đăng ký lái thử các mẫu xe hot, được tư vấn 1-1 và ưu đãi bảo dưỡng.</p>
                <div class="actions">
                    <a class="btn-outline" href="${pageContext.request.contextPath}/su-kien/lai-thu-toan-quoc-311025">Chi tiết</a>
                    <a class="btn-danger" href="${pageContext.request.contextPath}/dang-ky?event=lai-thu-311025">Đăng ký tham dự</a>
                </div>
            </div>
        </article>
    </section>

    <footer>
        <a class="back-home" href="${pageContext.request.contextPath}/">
            <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M10 6L4 12l6 6M4 12h16" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
            Quay lại trang chủ
        </a>
        <div style="margin-top:10px;">© <script>document.write(new Date().getFullYear())</script> Honda Vietnam. All rights reserved.</div>
    </footer>
</div>
</body>
</html>
