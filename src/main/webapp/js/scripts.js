// (Bạn có thể thêm hiệu ứng trượt navbar, animation, carousel,...)
window.addEventListener('scroll', () => {
    const nav = document.querySelector('.navbar');
    if (window.scrollY > 50) {
        nav.style.background = 'rgba(0, 0, 0, 0.9)';
    } else {
        nav.style.background = 'rgba(0, 0, 0, 0.6)';
    }
});
document.addEventListener("DOMContentLoaded", () => {
    document.querySelectorAll(".category-item").forEach(item => {
        const video = item.querySelector("video");
        item.addEventListener("mouseenter", () => {
            if (video) {
                video.currentTime = 0;
                video.play().catch(() => {});
            }
        });
        item.addEventListener("mouseleave", () => {
            if (video) video.pause();
        });
    });
});

document.addEventListener("DOMContentLoaded", () => {
    const footer = document.querySelector(".footer-content");
    const observer = new IntersectionObserver(entries => {
        entries.forEach(entry => {
            if (entry.isIntersecting) {
                footer.classList.add("show");
            }
        });
    });
    observer.observe(footer);
});
window.addEventListener('scroll', () => {
    const header = document.querySelector('.header');
    if (window.scrollY > 50) {
        header.style.background = 'rgba(0, 0, 0, 0.95)';
        header.style.backdropFilter = 'blur(8px)';
    } else {
        header.style.background = 'rgba(0, 0, 0, 0.7)';
        header.style.backdropFilter = 'blur(4px)';
    }
});
