document.addEventListener('DOMContentLoaded', function() {
    // 1. Lấy các phần tử và Context Path
    const searchInput = document.getElementById('product-search-input');
    const suggestionsBox = document.getElementById('search-suggestions');

    // Lấy Context Path từ biến global (PHẢI ĐƯỢC ĐỊNH NGHĨA TRƯỚC TRONG JSP)
    const contextPath = window.CONTEXT_PATH || '';

    let timeout = null;
    const MIN_KEYWORD_LENGTH = 2;
    const SUGGESTION_DELAY_MS = 300;

    // 2. Xử lý sự kiện gõ phím
    searchInput.addEventListener('keyup', function() {
        clearTimeout(timeout);
        const keyword = searchInput.value.trim();

        if (keyword.length < MIN_KEYWORD_LENGTH) {
            hideSuggestions();
            return;
        }

        timeout = setTimeout(function() {
            fetchSuggestions(keyword);
        }, SUGGESTION_DELAY_MS);
    });

    // 3. Ẩn hộp gợi ý khi click ra ngoài
    document.addEventListener('click', function(e) {
        if (!searchInput.contains(e.target) && !suggestionsBox.contains(e.target)) {
            hideSuggestions();
        }
    });

    // 4. Hàm ẩn gợi ý
    function hideSuggestions() {
        suggestionsBox.innerHTML = '';
        suggestionsBox.style.display = 'none';
    }

    // 5. Gửi yêu cầu AJAX
    function fetchSuggestions(keyword) {
        // Xây dựng URL tuyệt đối để tránh lỗi 404 khi người dùng ở đường dẫn sâu
        const absoluteContextPath = window.location.origin + contextPath;
        const url = `${absoluteContextPath}/products?action=searchSuggest&keyword=${encodeURIComponent(keyword)}`;

        fetch(url)
            .then(async response => {
                const contentType = response.headers.get('content-type');

                // Trường hợp 1: Lỗi HTTP status code (404, 500)
                if (!response.ok) {
                    // Nếu server trả về JSON lỗi (500 đã được bọc try-catch trong Java)
                    if (contentType && contentType.includes('application/json')) {
                        const errorData = await response.json();
                        throw new Error(`Server error: ${errorData.error || response.statusText}`);
                    }
                    // Nếu server trả về HTML/text (Lỗi 404 hoặc 500 không bọc try-catch hoàn toàn)
                    throw new Error(`HTTP error! status: ${response.status}`);
                }

                // Trường hợp 2: Thành công (200 OK) nhưng response body không phải JSON
                if (!contentType || !contentType.includes('application/json')) {
                    // Đây là nguyên nhân chính gây lỗi "Unexpected token '<'"
                    const text = await response.text();
                    // Nếu nội dung chỉ là text rỗng, coi là thành công
                    if (text.trim() === '[]' || text.trim() === '') {
                        return [];
                    }
                    // Nếu nội dung là HTML (bắt đầu bằng '<'), coi là lỗi server
                    if (text.trim().startsWith('<')) {
                        throw new Error("Server trả về HTML thay vì JSON (Lỗi cấu hình Servlet).");
                    }
                    // Trường hợp không mong muốn khác, cố gắng parse JSON
                    try {
                        return JSON.parse(text);
                    } catch(e) {
                        throw new Error("Dữ liệu trả về không phải JSON hợp lệ.");
                    }
                }

                // Trường hợp 3: Thành công và trả về JSON hợp lệ
                return response.json();
            })
            .then(data => {
                displaySuggestions(data);
            })
            .catch(error => {
                console.error('Lỗi khi fetch gợi ý tìm kiếm:', error);
                // Hiển thị lỗi rõ ràng cho người dùng
                suggestionsBox.innerHTML = `<div class="search-suggestion-item" style="color: red; cursor: default;">Lỗi tải dữ liệu. (${error.message})</div>`;
                suggestionsBox.style.display = 'block';
            });
    }

    // 6. Hiển thị gợi ý
    function displaySuggestions(suggestions) {
        suggestionsBox.innerHTML = '';

        if (suggestions && suggestions.length > 0) {
            suggestions.forEach(product => {
                const itemLink = document.createElement('a');

                // Đảm bảo liên kết chi tiết cũng dùng URL tuyệt đối
                itemLink.href = `${window.location.origin}${contextPath}/products?action=view&id=${product.id}`;
                itemLink.classList.add('search-suggestion-item');

                // Hiển thị tên và giá sản phẩm
                itemLink.innerHTML = `
                    <span class="suggestion-name">${product.name}</span>
                    <span class="suggestion-price">${formatPrice(product.price)}</span>
                `;

                suggestionsBox.appendChild(itemLink);
            });
            suggestionsBox.style.display = 'block';
        } else {
            // Hiển thị thông báo không tìm thấy và liên kết tìm kiếm đầy đủ
            const keyword = searchInput.value.trim();
            const messageItem = document.createElement('a'); // ĐÃ SỬA: Dùng <a> để nó hoạt động như một link
            messageItem.classList.add('search-suggestion-item');

            // THIẾT LẬP LINK TÌM KIẾM ĐẦY ĐỦ
            if (keyword.length > 0) {
                messageItem.href = `${contextPath}/products?action=list&search=${encodeURIComponent(keyword)}`;
                messageItem.style.cssText = 'color: #aaa; text-align: center;';
                messageItem.innerHTML = `Không tìm thấy sản phẩm cho <strong>"${keyword}"</strong>. Click để tìm kiếm đầy đủ.`;
            } else {
                // Nếu không có từ khóa, đây chỉ là thông báo tĩnh
                messageItem.href = "#";
                messageItem.style.cssText = 'color: #aaa; cursor: default;';
                messageItem.innerText = 'Không tìm thấy sản phẩm.';
            }

            suggestionsBox.appendChild(messageItem);
            suggestionsBox.style.display = 'block';
        }
    }

    // 7. Hàm định dạng giá tiền
    function formatPrice(price) {
        if (price === undefined || price === null) return '';
        try {
            return price.toLocaleString('vi-VN', { style: 'currency', currency: 'VND', minimumFractionDigits: 0 });
        } catch(e) {
            return price + ' VND';
        }
    }
});