<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="vi">
<head>
  <title>Đặt hàng</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/header.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/styles/form.css">
</head>
<body>
<jsp:include page="/includes/header.jsp" />

<div class="form-container">
  <h2>🧾 Xác nhận thông tin đặt hàng</h2>
  <form id="checkoutForm" method="post" action="${pageContext.request.contextPath}/orders">
    <input type="hidden" name="action" value="checkout">

    <div class="form-group">
      <label for="recipientName">Người nhận:</label>
      <input id="recipientName" class="form-control" type="text" value="${sessionScope.user.name}" required>
    </div>

    <div class="form-group">
      <label for="recipientPhone">Số điện thoại:</label>
      <input id="recipientPhone" class="form-control" type="text" value="${sessionScope.user.phone}" required>
    </div>

    <div class="form-group">
      <label for="province">Tỉnh/Thành phố:</label>
      <select id="province" class="form-control" required>
        <option value="">-- Chọn Tỉnh/Thành --</option>
      </select>
    </div>
    <div class="form-group">
      <label for="district">Quận/Huyện:</label>
      <select id="district" class="form-control" required>
        <option value="">-- Chọn Quận/Huyện --</option>
      </select>
    </div>
    <div class="form-group">
      <label for="commune">Phường/Xã:</label>
      <select id="commune" class="form-control" required>
        <option value="">-- Chọn Phường/Xã --</option>
      </select>
    </div>
    <div class="form-group">
      <label for="street">Số nhà, tên đường:</label>
      <input id="street" class="form-control" type="text" placeholder="Ví dụ: 123 Lê Lợi" required>
    </div>

    <input type="hidden" id="fullAddress" name="deliveryAddress">

    <button type="submit" class="form-submit-btn">Hoàn tất đặt hàng</button>
  </form>
</div>

<jsp:include page="/includes/footer.jsp" />

<%-- ========================================================== --%>
<%-- SCRIPT XỬ LÝ LOGIC ĐỊA CHỈ (PHIÊN BẢN CÓ DEBUG) --%>
<%-- ========================================================== --%>
<script>
  const host = "https://provinces.open-api.vn/api/";
  const provinceSelect = document.getElementById("province");
  const districtSelect = document.getElementById("district");
  const communeSelect = document.getElementById("commune");
  const streetInput = document.getElementById("street");
  const fullAddressInput = document.getElementById("fullAddress");
  const checkoutForm = document.getElementById("checkoutForm");

  // --- CÁC HÀM TẢI DỮ LIỆU TỈNH/HUYỆN/XÃ (Giữ nguyên) ---
  function callAPI(api) {
    return fetch(api).then(response => response.json());
  }
  document.addEventListener("DOMContentLoaded", () => {
    callAPI(host + "?depth=1").then(data => {
      renderData(data, provinceSelect);
    });
  });
  provinceSelect.addEventListener("change", () => {
    districtSelect.innerHTML = '<option value="">-- Chọn Quận/Huyện --</option>';
    communeSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';
    const provinceCode = provinceSelect.value;
    if (provinceCode) {
      callAPI(host + "p/" + provinceCode + "?depth=2").then(data => {
        renderData(data.districts, districtSelect);
      });
    }
  });
  districtSelect.addEventListener("change", () => {
    communeSelect.innerHTML = '<option value="">-- Chọn Phường/Xã --</option>';
    const districtCode = districtSelect.value;
    if (districtCode) {
      callAPI(host + "d/" + districtCode + "?depth=2").then(data => {
        renderData(data.wards, communeSelect);
      });
    }
  });
  function renderData(data, selectElement) {
    data.forEach(item => {
      const option = document.createElement("option");
      option.value = item.code;
      option.innerText = item.name;
      selectElement.appendChild(option);
    });
  }

  // ==========================================================
  // SỬA LẠI HOÀN CHỈNH HÀM XỬ LÝ GHÉP CHUỖI
  // ==========================================================
  checkoutForm.addEventListener("submit", function(event) {
    event.preventDefault(); // Ngăn form gửi đi ngay lập tức

    let addressParts = []; // Tạo một mảng để chứa các phần của địa chỉ
    let isValid = true;

    const streetText = streetInput.value.trim();
    const communeText = communeSelect.selectedIndex > 0 ? communeSelect.options[communeSelect.selectedIndex].text : "";
    const districtText = districtSelect.selectedIndex > 0 ? districtSelect.options[districtSelect.selectedIndex].text : "";
    const provinceText = provinceSelect.selectedIndex > 0 ? provinceSelect.options[provinceSelect.selectedIndex].text : "";

    // Kiểm tra và thêm từng phần vào mảng
    if (streetText) {
      addressParts.push(streetText);
    } else {
      isValid = false;
    }

    if (communeText) {
      addressParts.push(communeText);
    } else {
      isValid = false;
    }

    if (districtText) {
      addressParts.push(districtText);
    } else {
      isValid = false;
    }

    if (provinceText) {
      addressParts.push(provinceText);
    } else {
      isValid = false;
    }

    // Nếu tất cả thông tin hợp lệ
    if (isValid) {
      // Dùng join() để ghép các phần tử trong mảng lại thành một chuỗi duy nhất
      const finalAddress = addressParts.join(', ');
      console.log("Địa chỉ cuối cùng được tạo:", finalAddress);

      fullAddressInput.value = finalAddress;
      checkoutForm.submit();
    } else {
      alert("Vui lòng điền đầy đủ thông tin địa chỉ.");
    }
  });
</script>

</body>
</html>