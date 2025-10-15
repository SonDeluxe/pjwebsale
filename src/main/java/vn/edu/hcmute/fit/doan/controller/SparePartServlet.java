// src/main/java/vn/edu/hcmute/fit/doan/controller/SparePartServlet.java
package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import vn.edu.hcmute.fit.doan.SparePart;
import vn.edu.hcmute.fit.doan.SparePartCategory;
import vn.edu.hcmute.fit.doan.database.SparePartDAO;
import vn.edu.hcmute.fit.doan.database.SparePartCategoryDAO;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5,      // 5MB
        maxRequestSize = 1024 * 1024 * 10   // 10MB
)
@WebServlet("/spareparts")
public class SparePartServlet extends HttpServlet {
    private final SparePartDAO sparePartDAO = new SparePartDAO();
    private final SparePartCategoryDAO categoryDAO = new SparePartCategoryDAO();

    // Thư mục lưu ảnh
    private static final String UPLOAD_DIR = "assets/images";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        action = (action == null) ? "list" : action;

        // Kiểm tra quyền Admin cho các action quản lý
        if (action.startsWith("admin") || "add".equals(action) || "edit".equals(action)) {
            HttpSession session = request.getSession(false);
            if (session == null || !"Admin".equals(session.getAttribute("role"))) {
                response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập.");
                return;
            }
        }

        switch (action) {
            case "adminList":
                showAdminList(request, response);
                break;
            case "add":
                showAddForm(request, response);
                break;
            case "edit":
                showEditForm(request, response);
                break;
            case "list":
            default:
                listSparePartsForUser(request, response);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");

        switch (action) {
            case "add":
                addSparePart(request, response);
                break;
            case "update":
                updateSparePart(request, response);
                break;
            case "delete":
                deleteSparePart(request, response);
                break;
        }
    }

    // ========== CÁC PHƯƠNG THỨC CHO USER ==========
    private void listSparePartsForUser(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SparePart> spareParts = sparePartDAO.getAll();
        request.setAttribute("spareParts", spareParts);
        request.setAttribute("pageTitle", "Danh sách Phụ tùng");
        request.setAttribute("bodyContent", "/views/spareparts/list.jsp");
        request.setAttribute("customCss", "list.css");
        request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
    }

    // ========== CÁC PHƯƠNG THỨC CHO ADMIN ==========
    private void showAdminList(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<SparePart> spareParts = sparePartDAO.getAll();
        request.setAttribute("spareParts", spareParts);
        request.setAttribute("pageTitle", "Quản lý Phụ tùng - Admin");
        request.setAttribute("bodyContent", "/views/spareparts/list.jsp");
        request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
    }

    private void showAddForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("categories", categoryDAO.findAll());
        request.setAttribute("pageTitle", "Thêm Phụ tùng mới");
        request.setAttribute("bodyContent", "/views/spareparts/add.jsp");
        request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        SparePart part = sparePartDAO.findById(id);
        if (part != null) {
            request.setAttribute("part", part);
            request.setAttribute("categories", categoryDAO.findAll());
            request.setAttribute("pageTitle", "Sửa thông tin Phụ tùng: " + part.getName());
            request.setAttribute("bodyContent", "/views/spareparts/edit.jsp");
            request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("error", "Không tìm thấy phụ tùng để sửa.");
            response.sendRedirect(request.getContextPath() + "/spareparts?action=adminList");
        }
    }

    // ========== PHƯƠNG THỨC XỬ LÝ UPLOAD ẢNH ==========
    private String handleImageUpload(Part filePart, HttpServletRequest request) throws IOException {
        if (filePart == null || filePart.getSize() == 0) {
            return null;
        }

        String fileName = Paths.get(filePart.getSubmittedFileName()).getFileName().toString();
        String uniqueFileName = System.currentTimeMillis() + "_" + fileName;

        String appPath = request.getServletContext().getRealPath("");
        String uploadPath = Paths.get(appPath, UPLOAD_DIR).toString();

        Files.createDirectories(Paths.get(uploadPath));

        try (InputStream input = filePart.getInputStream()) {
            Files.copy(input, Paths.get(uploadPath, uniqueFileName), StandardCopyOption.REPLACE_EXISTING);
        }

        return uniqueFileName;
    }

    private void addSparePart(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));

            // Xử lý upload ảnh
            Part filePart = request.getPart("imageFile");
            String imageUrl = handleImageUpload(filePart, request);

            // Nếu không upload ảnh, sử dụng URL từ form
            if (imageUrl == null) {
                imageUrl = request.getParameter("imageUrl");
            }

            SparePartCategory category = categoryDAO.findById(categoryId);
            if (category != null) {
                SparePart part = new SparePart(name, description, price, imageUrl, category);
                sparePartDAO.add(part);
                request.getSession().setAttribute("success", "Thêm phụ tùng '" + name + "' thành công!");
            } else {
                request.getSession().setAttribute("error", "Không tìm thấy danh mục!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi thêm phụ tùng: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/spareparts?action=adminList");
    }

    private void updateSparePart(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            SparePart part = sparePartDAO.findById(id);

            if (part != null) {
                part.setName(request.getParameter("name"));
                part.setDescription(request.getParameter("description"));
                part.setPrice(Double.parseDouble(request.getParameter("price")));

                // Xử lý upload ảnh
                Part filePart = request.getPart("imageFile");
                String newImageUrl = handleImageUpload(filePart, request);

                if (newImageUrl != null) {
                    part.setImageUrl(newImageUrl);
                } else {
                    String imageUrlParam = request.getParameter("imageUrl");
                    if (imageUrlParam != null && !imageUrlParam.trim().isEmpty()) {
                        part.setImageUrl(imageUrlParam);
                    }
                }

                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                part.setCategory(categoryDAO.findById(categoryId));
                sparePartDAO.update(part);

                request.getSession().setAttribute("success", "Cập nhật phụ tùng #" + id + " thành công!");
            } else {
                request.getSession().setAttribute("error", "Không tìm thấy phụ tùng để cập nhật!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi cập nhật phụ tùng: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/spareparts?action=adminList");
    }

    private void deleteSparePart(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            sparePartDAO.delete(id);
            request.getSession().setAttribute("success", "Đã xóa phụ tùng #" + id);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi xóa phụ tùng: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/spareparts?action=adminList");
    }
}