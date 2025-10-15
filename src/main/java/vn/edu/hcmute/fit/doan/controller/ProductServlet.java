package vn.edu.hcmute.fit.doan.controller;

import com.google.gson.Gson;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import vn.edu.hcmute.fit.doan.*;
import vn.edu.hcmute.fit.doan.database.*;

import java.io.*;
import java.nio.file.*;
import java.util.*;

@MultipartConfig(
        maxFileSize = 1024 * 1024 * 5,      // 5MB
        maxRequestSize = 1024 * 1024 * 10   // 10MB
)
@WebServlet("/products")
public class ProductServlet extends HttpServlet {

    private final ProductDAO productDAO = new ProductDAO();
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final ReviewDAO reviewDAO = new ReviewDAO();
    private final Gson gson = new Gson();

    // Thư mục lưu ảnh
    private static final String UPLOAD_DIR = "assets/images";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "view":
                    showProductDetail(request, response);
                    break;
                case "add":
                    showAddForm(request, response);
                    break;
                case "edit":
                    showEditForm(request, response);
                    break;
                case "searchSuggest":
                    handleSearchSuggest(request, response);
                    break;
                case "adminList": // THÊM TRANG ADMIN
                    adminListProducts(request, response);
                    break;
                case "list":
                default:
                    listProducts(request, response);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Đã xảy ra lỗi không mong muốn.");
            response.sendRedirect(request.getContextPath() + "/products?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Bạn không có quyền truy cập chức năng này.");
            return;
        }

        request.setCharacterEncoding("UTF-8");
        String action = request.getParameter("action");
        if (action == null) action = "list";

        try {
            switch (action) {
                case "add":
                    addProduct(request, response);
                    break;
                case "update":
                    updateProduct(request, response);
                    break;
                case "delete":
                    deleteProduct(request, response);
                    break;
                default:
                    response.sendRedirect(request.getContextPath() + "/products?action=adminList");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Thao tác thất bại: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/products?action=adminList");
        }
    }

    // ===============================
    // 📋 DANH SÁCH SẢN PHẨM CHO ADMIN (THÊM METHOD NÀY)
    // ===============================
    private void adminListProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final int PAGE_SIZE = 10;
        String pageStr = request.getParameter("page");
        int currentPage = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;

        Map<String, Object> criteria = new HashMap<>();
        long totalProducts = productDAO.countWithFilters(criteria);
        List<Product> products = productDAO.findWithFiltersAndPagination(criteria, currentPage, PAGE_SIZE, "id_desc");
        long totalPages = (long) Math.ceil((double) totalProducts / PAGE_SIZE);

        request.setAttribute("products", products);
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("pageTitle", "Quản lý Sản phẩm - Admin");
        request.setAttribute("bodyContent", "/views/products/list.jsp");
        request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
    }

    // ===============================
    // ➕ FORM THÊM SẢN PHẨM (SỬA LẠI METHOD NÀY)
    // ===============================
    private void showAddForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        System.out.println("DEBUG: showAddForm called");

        HttpSession session = request.getSession(false);
        if (session == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }

        try {
            List<Category> categories = categoryDAO.findAllCategories();
            System.out.println("DEBUG: Categories loaded: " + categories.size());

            request.setAttribute("categories", categories);
            request.setAttribute("pageTitle", "Thêm sản phẩm mới");
            request.setAttribute("bodyContent", "/views/products/add.jsp");

            System.out.println("DEBUG: Forwarding to template");
            request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            session.setAttribute("error", "Lỗi khi tải form thêm sản phẩm: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/products?action=adminList");
        }
    }

    // ===============================
    // 🖼️ METHOD XỬ LÝ UPLOAD ẢNH
    // ===============================
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

    // ===============================
    // ➕ THÊM SẢN PHẨM
    // ===============================
    private void addProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            String name = request.getParameter("name");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = Integer.parseInt(request.getParameter("stock"));
            int categoryId = Integer.parseInt(request.getParameter("categoryId"));
            String description = request.getParameter("description");

            // Xử lý upload ảnh
            Part filePart = request.getPart("imageFile");
            String imageUrl = handleImageUpload(filePart, request);

            if (imageUrl == null) {
                imageUrl = request.getParameter("imageUrl");
            }

            Category category = CategoryDAO.findCategoryById(categoryId);
            if (category != null) {
                Product product = new Product(name, price, category, description, imageUrl);
                product.setStock(stock);
                productDAO.addProduct(product);
                request.getSession().setAttribute("success", "Thêm sản phẩm '" + name + "' thành công!");
            } else {
                request.getSession().setAttribute("error", "Không tìm thấy danh mục!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/products?action=adminList");
    }

    // ===============================
    // ✏️ CẬP NHẬT SẢN PHẨM
    // ===============================
    private void updateProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            Product product = productDAO.findProductById(id);

            if (product != null) {
                product.setName(request.getParameter("name"));
                product.setPrice(Double.parseDouble(request.getParameter("price")));
                product.setStock(Integer.parseInt(request.getParameter("stock")));
                product.setDescription(request.getParameter("description"));

                Part filePart = request.getPart("imageFile");
                String newImageUrl = handleImageUpload(filePart, request);

                if (newImageUrl != null) {
                    product.setImageUrl(newImageUrl);
                } else {
                    String imageUrlParam = request.getParameter("imageUrl");
                    if (imageUrlParam != null && !imageUrlParam.trim().isEmpty()) {
                        product.setImageUrl(imageUrlParam);
                    }
                }

                int categoryId = Integer.parseInt(request.getParameter("categoryId"));
                Category category = CategoryDAO.findCategoryById(categoryId);
                product.setCategory(category);

                productDAO.updateProduct(product);
                request.getSession().setAttribute("success", "Cập nhật sản phẩm #" + id + " thành công!");
            } else {
                request.getSession().setAttribute("error", "Không tìm thấy sản phẩm để cập nhật!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi cập nhật sản phẩm: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/products?action=adminList");
    }

    // ===============================
    // 🗑️ XÓA SẢN PHẨM
    // ===============================
    private void deleteProduct(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));
            productDAO.deleteProduct(id);
            request.getSession().setAttribute("success", "Đã xóa sản phẩm #" + id);
        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("error", "Lỗi khi xóa sản phẩm: " + e.getMessage());
        }
        response.sendRedirect(request.getContextPath() + "/products?action=adminList");
    }

    // ===============================
    // 📄 DANH SÁCH SẢN PHẨM CHO USER
    // ===============================
    private void listProducts(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        final int PAGE_SIZE = 8;

        // Lấy tất cả các tham số lọc từ URL
        String searchKeyword = request.getParameter("search");
        String categoryIdStr = request.getParameter("categoryId");
        String maxPriceStr = request.getParameter("maxPrice");
        String minPriceStr = request.getParameter("minPrice");
        String pageStr = request.getParameter("page");
        String sortBy = request.getParameter("sort");

        int currentPage = (pageStr != null && !pageStr.isEmpty()) ? Integer.parseInt(pageStr) : 1;
        if (sortBy == null || sortBy.isEmpty()) sortBy = "name_asc";

        // Xây dựng các tiêu chí lọc
        Map<String, Object> criteria = new HashMap<>();
        if (searchKeyword != null && !searchKeyword.isEmpty()) criteria.put("keyword", searchKeyword);
        if (categoryIdStr != null && !categoryIdStr.isEmpty()) criteria.put("categoryId", Integer.parseInt(categoryIdStr));
        if (maxPriceStr != null && !maxPriceStr.isEmpty()) criteria.put("maxPrice", Double.parseDouble(maxPriceStr));
        if (minPriceStr != null && !minPriceStr.isEmpty()) criteria.put("minPrice", Double.parseDouble(minPriceStr));

        // Lấy dữ liệu từ DAO
        long totalProducts = productDAO.countWithFilters(criteria);
        List<Product> products = productDAO.findWithFiltersAndPagination(criteria, currentPage, PAGE_SIZE, sortBy);
        long totalPages = (long) Math.ceil((double) totalProducts / PAGE_SIZE);

        // Gửi tất cả dữ liệu và tham số lọc ra JSP
        request.setAttribute("products", products);
        request.setAttribute("categories", categoryDAO.findRootCategories());
        request.setAttribute("currentPage", currentPage);
        request.setAttribute("totalPages", totalPages);

        request.setAttribute("sortBy", sortBy);
        request.setAttribute("searchKeyword", searchKeyword);
        request.setAttribute("minPriceFilter", minPriceStr);
        request.setAttribute("maxPriceFilter", maxPriceStr);
        request.setAttribute("categoryIdFilter", categoryIdStr);

        request.setAttribute("pageTitle", "Danh sách sản phẩm");
        request.setAttribute("bodyContent", "/views/products/list.jsp");
        request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
    }

    // CÁC METHOD KHÁC GIỮ NGUYÊN...
    private void showProductDetail(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            Product product = productDAO.findProductById(productId);

            if (product == null) {
                request.getSession().setAttribute("error", "Không tìm thấy sản phẩm bạn yêu cầu.");
                response.sendRedirect(request.getContextPath() + "/products?action=list");
                return;
            }

            List<Category> categories = categoryDAO.findRootCategories();
            List<Review> reviews = reviewDAO.findByProductId(productId);

            request.setAttribute("product", product);
            request.setAttribute("categories", categories);
            request.setAttribute("reviews", reviews);
            request.setAttribute("pageTitle", product.getName());
            request.setAttribute("bodyContent", "/views/products/view.jsp");
            request.setAttribute("customCss", "detail.css");

            request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            request.getSession().setAttribute("error", "ID sản phẩm không hợp lệ.");
            response.sendRedirect(request.getContextPath() + "/products?action=list");
        }
    }

    private void showEditForm(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!"Admin".equals(request.getSession().getAttribute("role"))) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
            return;
        }
        int id = Integer.parseInt(request.getParameter("id"));
        Product product = productDAO.findProductById(id);
        if (product != null) {
            request.setAttribute("product", product);
            request.setAttribute("categories", categoryDAO.findAllCategories());
            request.setAttribute("pageTitle", "Sửa sản phẩm: " + product.getName());
            request.setAttribute("bodyContent", "/views/products/edit.jsp");
            request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
        } else {
            request.getSession().setAttribute("error", "Không tìm thấy sản phẩm để sửa.");
            response.sendRedirect(request.getContextPath() + "/products?action=adminList");
        }
    }

    private void handleSearchSuggest(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String keyword = request.getParameter("keyword");
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        if (keyword != null && keyword.length() >= 2) {
            try {
                List<Product> suggestions = productDAO.findByName(keyword).stream().limit(5).toList();
                List<Map<String, Object>> safeSuggestions = new ArrayList<>();
                for (Product p : suggestions) {
                    Map<String, Object> productMap = new HashMap<>();
                    productMap.put("id", p.getId());
                    productMap.put("name", p.getName());
                    productMap.put("price", p.getPrice());
                    safeSuggestions.add(productMap);
                }
                response.getWriter().write(gson.toJson(safeSuggestions));
                return;
            } catch (Exception e) {
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                response.getWriter().write("{\"error\": \"Server error: Internal server error in search suggest.\"}");
                e.printStackTrace();
                return;
            }
        }
        response.getWriter().write("[]");
    }
}