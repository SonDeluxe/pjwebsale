<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8"/>
    <title>Tin khuyến mãi | Honda Vietnam</title>
    <meta name="viewport" content="width=device-width, initial-scale=1"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
    <link href="https://fonts.googleapis.com/css2?family=Inter:wght@400;600;800&display=swap" rel="stylesheet">

    <style>
        :root{
            /* THEME PROMO - xanh lá */
            --brand:#00a86b; --brand-dark:#008b59;
            --ink:#1b1b1b; --muted:#5a5a5a; --card:#fff; --ring:rgba(0,168,107,.25);
            --bg:#f5fbf8;
        }
        *{box-sizing:border-box}
        html,body{margin:0;padding:0}
        body{font-family:'Inter',system-ui,Segoe UI,Roboto,Helvetica,Arial,sans-serif;background:var(--bg);color:var(--ink)}
        .container{width:min(1160px,92%);margin-inline:auto}

        /* Topbar */
        .topbar{position:sticky;top:0;background:#fff;border-bottom:1px solid #eee;z-index:50}
        .nav{display:flex;align-items:center;justify-content:space-between;padding:14px 0}
        .brand{display:flex;align-items:center;gap:10px;text-decoration:none;color:var(--ink);font-weight:800}
        .brand .logo{width:40px;height:40px;border-radius:10px;background:radial-gradient(120% 120% at 20% 20%,#baf1dd,var(--brand))}
        .nav-actions a{margin-left:18px;text-decoration:none;color:#333;font-weight:600}
        .nav-actions .primary{background:var(--brand);color:#fff;padding:8px 14px;border-radius:10px;box-shadow:0 6px 18px var(--ring)}
        .nav-actions .primary:hover{background:var(--brand-dark)}

        /* Hero */
        .hero{color:#fff;border-radius:18px;overflow:hidden;margin-top:18px;
            background:linear-gradient(180deg,#0f3327 0%, #0f0f10 100%);}
        .hero-wrap{display:grid;grid-template-columns:1.15fr .85fr;gap:24px;padding:40px 26px}
        .hero h1{font-size:clamp(28px,4vw,44px);margin:0 0 10px}
        .hero p{color:#d6d6d6;margin:0 0 16px}
        .btn{display:inline-flex;align-items:center;gap:10px;padding:12px 16px;border-radius:12px;text-decoration:none;font-weight:800}
        .btn-primary{background:var(--brand);color:#fff;box-shadow:0 8px 22px var(--ring)}
        .btn-primary:hover{background:var(--brand-dark)}
        .btn-ghost{border:1px solid #333;color:#fff}
        .hero-img{border-radius:14px;min-height:220px;background:url('${pageContext.request.contextPath}/images/honda-promo-hero.jpg') center/cover no-repeat;border:1px solid #262628;box-shadow:0 20px 60px rgba(0,0,0,.35)}

        /* Filters */
        .filters{display:flex;gap:10px;flex-wrap:wrap;margin:18px 0}
        .chip{padding:8px 12px;border:1px solid #e6e6e6;border-radius:999px;background:#fff;color:#222;font-weight:700;text-decoration:none}
        .chip.active,.chip:hover{border-color:#ccc;background:#fafafa}

        /* Grid & Cards */
        .grid{display:grid;gap:18px;grid-template-columns:repeat(3,1fr)}
        @media (max-width:980px){.hero-wrap{grid-template-columns:1fr}.grid{grid-template-columns:repeat(2,1fr)}}
        @media (max-width:640px){.grid{grid-template-columns:1fr}}
        .card{background:var(--card);border:1px solid #e7f3ed;border-radius:16px;overflow:hidden;transition:transform .25s ease,box-shadow .25s ease,border-color .25s ease}
        .card:hover{transform:translateY(-4px);box-shadow:0 16px 38px rgba(0,0,0,.08);border-color:#d7ece4}
        .card-img{aspect-ratio:16/9;background-size:cover;background-position:center;
            background-image:linear-gradient(135deg, rgba(0,168,107,.06), rgba(0,168,107,.12));}
        .card-img.wrap{position:relative}
        .card-img.wrap::after{ /* ribbon góc */
            content:"ƯU ĐÃI"; position:absolute; top:0; right:-44px; transform:rotate(45deg);
            background:var(--brand); color:#fff; padding:6px 64px; font-weight:800; box-shadow:0 8px 18px var(--ring);
        }
        .card-body{padding:14px}
        .title{font-size:18px;font-weight:800;margin:0 0 6px}
        .meta{display:flex;gap:10px;flex-wrap:wrap;color:#666;font-size:14px;margin-bottom:8px}
        .pill{display:inline-flex;align-items:center;gap:6px;padding:6px 10px;background:#fafafa;border:1px solid #eee;border-radius:999px}
        .desc{color:#555;margin:8px 0 12px}
        .actions{display:flex;gap:10px}
        .btn-outline{border:1px solid #e6e6e6;border-radius:12px;padding:10px 14px;text-decoration:none;color:#222;font-weight:800}
        .btn-outline:hover{border-color:#ccc;background:#fafafa}
        .btn-danger{background:var(--brand);color:#fff;border-radius:12px;padding:10px 14px;text-decoration:none;font-weight:800;box-shadow:0 8px 22px var(--ring)}
        .btn-danger:hover{background:var(--brand-dark)}
        .badge-off{position:absolute;top:12px;left:12px;background:#fff;color:var(--brand);border:1px solid #baf1dd;padding:6px 10px;border-radius:999px;font-weight:800;font-size:12px;box-shadow:0 6px 14px var(--ring)}
        .deadline{display:flex;align-items:center;gap:8px;color:#444}
        .countdown{font-weight:800}
        .label-expired{color:#b1000c;font-weight:800}

        /* Progress (thanh tiến độ thời gian) */
        .progress{height:6px;background:#edf7f3;border-radius:999px;overflow:hidden;margin-top:10px}
        .progress>span{display:block;height:100%;background:var(--brand)}

        /* Footer */
        footer{text-align:center;margin:40px 0 26px;color:#777}
        .back-home{display:inline-flex;align-items:center;gap:8px;margin-top:10px;text-decoration:none;color:#333;font-weight:700}
        .back-home:hover{text-decoration:underline}
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
                <a href="${pageContext.request.contextPath}/su-kien">Sự kiện</a>
                <a href="${pageContext.request.contextPath}/tin-tuc">Tin tức</a>
                <a class="primary" href="${pageContext.request.contextPath}/dat-lich-lai-thu">Đặt lịch lái thử</a>
            </div>
        </div>
    </div>
</div>

<!-- HERO -->
<div class="container">
    <section class="hero" aria-label="Khuyến mãi">
        <div class="hero-wrap">
            <div>
                <h1>Tin khuyến mãi</h1>
                <p>Chào mừng bạn đến với trung tâm ưu đãi! Cập nhật khuyến mãi phụ tùng, bảo dưỡng, và mua xe mới – số lượng có hạn.</p>
                <div class="actions" style="margin-top:10px">
                    <a class="btn btn-primary" href="#promo-list">Xem ưu đãi đang diễn ra</a>
                    <a class="btn btn-ghost" href="${pageContext.request.contextPath}/khuyen-mai/quy-dinh">Điều kiện & điều khoản</a>
                </div>
            </div>
            <div class="hero-img" role="img" aria-label="Banner khuyến mãi Honda"></div>
        </div>
    </section>

    <!-- FILTERS -->
    <div class="filters" aria-label="Bộ lọc khuyến mãi">
        <a href="#promo-list" class="chip active" data-filter="all">Tất cả</a>
        <a href="#promo-list" class="chip" data-filter="running">Đang diễn ra</a>
        <a href="#promo-list" class="chip" data-filter="ending">Sắp kết thúc</a>
        <a href="#promo-list" class="chip" data-filter="expired">Đã hết hạn</a>
    </div>

    <!-- PROMO GRID -->
    <section id="promo-list" class="grid" aria-label="Danh sách khuyến mãi">
        <!-- KM 1 -->
        <article class="card" data-start="2025-10-01" data-end="2025-10-31">
            <div class="card-img wrap" style="background-image:url('${pageContext.request.contextPath}/images/promo-parts.jpg')">
                <span class="badge-off">-10% Phụ tùng</span>
            </div>
            <div class="card-body">
                <h3 class="title">Khuyến mãi 10% phụ tùng xe máy</h3>
                <div class="meta">
                    <span class="pill deadline">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M7 2v3M17 2v3M3 9h18M4 7h16a1 1 0 0 1 1 1v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a1 1 0 0 1 1-1Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
                      Hết hạn: <span class="end-date">31/10/2025</span> • <span class="countdown"></span>
                    </span>
                </div>
                <!-- progress -->
                <div class="progress"><span style="width:0%"></span></div>

                <p class="desc">Áp dụng tại hệ thống HEAD toàn quốc cho các hạng mục phụ tùng chính hãng.</p>
                <div class="actions">
                    <a class="btn-outline" href="${pageContext.request.contextPath}/khuyen-mai/phu-tung-10">Chi tiết</a>
                    <a class="btn-danger" href="${pageContext.request.contextPath}/dat-lich-bao-duong?promo=phu-tung-10">Nhận ưu đãi</a>
                </div>
            </div>
        </article>

        <!-- KM 2 -->
        <article class="card" data-start="2025-10-01" data-end="2025-10-31">
            <div class="card-img wrap" style="background-image:url('${pageContext.request.contextPath}/images/promo-newbike.jpg')">
                <span class="badge-off">-5% Xe mới</span>
            </div>
            <div class="card-body">
                <h3 class="title">Giảm 5% khi mua xe mới trong tháng 10</h3>
                <div class="meta">
                    <span class="pill deadline">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M7 2v3M17 2v3M3 9h18M4 7h16a1 1 0 0 1 1 1v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a1 1 0 0 1 1-1Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
                      Hết hạn: <span class="end-date">31/10/2025</span> • <span class="countdown"></span>
                    </span>
                </div>
                <!-- progress -->
                <div class="progress"><span style="width:0%"></span></div>

                <p class="desc">Áp dụng cho một số mẫu xe chọn lọc. Không cộng dồn với chương trình khác.</p>
                <div class="actions">
                    <a class="btn-outline" href="${pageContext.request.contextPath}/khuyen-mai/xe-moi-5">Chi tiết</a>
                    <a class="btn-danger" href="${pageContext.request.contextPath}/dat-lich-lai-thu?promo=xe-moi-5">Nhận ưu đãi</a>
                </div>
            </div>
        </article>

        <!-- Ví dụ KM đã hết hạn -->
        <article class="card" data-start="2025-09-01" data-end="2025-09-30">
            <div class="card-img wrap" style="background-image:url('${pageContext.request.contextPath}/images/promo-service.jpg')">
                <span class="badge-off">Bảo dưỡng</span>
            </div>
            <div class="card-body">
                <h3 class="title">Tặng công bảo dưỡng định kỳ</h3>
                <div class="meta">
                    <span class="pill deadline">
                      <svg width="16" height="16" viewBox="0 0 24 24" fill="none" aria-hidden="true"><path d="M7 2v3M17 2v3M3 9h18M4 7h16a1 1 0 0 1 1 1v12a2 2 0 0 1-2 2H5a2 2 0 0 1-2-2V8a1 1 0 0 1 1-1Z" stroke="currentColor" stroke-width="1.5" stroke-linecap="round"/></svg>
                      Hết hạn: <span class="end-date">30/09/2025</span> • <span class="countdown"></span>
                    </span>
                </div>
                <!-- progress -->
                <div class="progress"><span style="width:100%"></span></div>

                <p class="desc">Áp dụng khi đặt lịch trước. Chương trình đã kết thúc.</p>
                <div class="actions">
                    <a class="btn-outline" href="${pageContext.request.contextPath}/khuyen-mai/bao-duong">Xem lại</a>
                    <a class="btn-danger" href="${pageContext.request.contextPath}/dat-lich-bao-duong">Đặt lịch mới</a>
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

<script>
    // Đếm ngược & phân loại + fill thanh tiến độ
    (function(){
        const cards = document.querySelectorAll('.card[data-start][data-end]');
        const bars  = document.querySelectorAll('.card[data-start][data-end] .progress > span');
        const now = new Date();
        const fmt = n => n.toString().padStart(2,'0');

        cards.forEach((card, idx)=>{
            const end   = new Date(card.dataset.end + 'T23:59:59');
            const start = new Date(card.dataset.start + 'T00:00:00');

            const cdEl = card.querySelector('.countdown');
            const total = end - start;
            const passed = Math.max(0, Math.min(total, now - start));
            const diff = end - now;

            // Countdown text + status
            if (diff <= 0){
                cdEl.innerHTML = '<span class="label-expired">Đã hết hạn</span>';
                card.setAttribute('data-status','expired');
            } else {
                const days  = Math.floor(diff / (1000*60*60*24));
                const hours = Math.floor((diff / (1000*60*60)) % 24);
                cdEl.textContent = `Còn ${days} ngày ${fmt(hours)} giờ`;
                card.setAttribute('data-status', days < 7 ? 'ending' : 'running');
            }
            if (now < start){
                card.setAttribute('data-status','upcoming');
                cdEl.textContent = 'Sắp bắt đầu';
            }

            // Progress bar
            const pct = total>0 ? Math.round((passed/total)*100) : 100;
            if (bars[idx]) bars[idx].style.width = pct + '%';
        });

        // Lọc theo chip
        const chips = document.querySelectorAll('.chip');
        chips.forEach(chip=>{
            chip.addEventListener('click', (e)=>{
                e.preventDefault();
                chips.forEach(c=>c.classList.remove('active'));
                chip.classList.add('active');
                const filter = chip.getAttribute('data-filter');
                cards.forEach(card=>{
                    const status = card.getAttribute('data-status');
                    card.style.display = (filter==='all' || status===filter) ? 'block' : 'none';
                });
                document.getElementById('promo-list').scrollIntoView({behavior:'smooth',block:'start'});
            });
        });
    })();
</script>
</body>
</html>
