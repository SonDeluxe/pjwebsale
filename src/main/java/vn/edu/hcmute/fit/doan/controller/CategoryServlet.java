package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.Category;
import vn.edu.hcmute.fit.doan.database.CategoryDAO;
import vn.edu.hcmute.fit.doan.database.UserDAO;  // Để check role từ session

import java.io.IOException;
import java.util.List;

@WebServlet("/categories")
public class CategoryServlet extends HttpServlet {
    private final CategoryDAO categoryDAO = new CategoryDAO();
    private final UserDAO userDAO = new UserDAO();  // Giả sử dùng để lấy user từ session

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("auth?action=login");  // Redirect login hoặc error nếu không phải admin
            return;
        }

        if ("list".equals(action)) {
            List<Category> categories = getAllCategories();  // Placeholder; thêm findAll vào DAO
            request.setAttribute("categories", categories);
            request.getRequestDispatcher("/views/categories/list.jsp").forward(request, response);
        } else if ("view".equals(action) || "edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                Category category = CategoryDAO.findCategoryById(id);  // Gọi method (non-static)
                if (category != null) {
                    request.setAttribute("category", category);
                    String jsp = "view".equals(action) ? "/views/categories/view.jsp" : "/views/categories/edit.jsp";
                    request.getRequestDispatcher(jsp).forward(request, response);
                } else {
                    request.setAttribute("error", "Không tìm thấy danh mục!");
                    response.sendRedirect("categories?action=list");
                }
            }
        } else {
            response.sendRedirect("categories?action=list");  // Default list
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null || !"Admin".equals(session.getAttribute("role"))) {
            response.sendRedirect("auth?action=login");
            return;
        }

        request.setCharacterEncoding("UTF-8");  // Để hỗ trợ tiếng Việt

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String name = request.getParameter("categoryName");
            String description = request.getParameter("description");
            String phone = request.getParameter("phone");
            String message = request.getParameter("message");

            Category newCategory = new Category(name, description, phone, message);
            categoryDAO.addCategory(newCategory);
            request.setAttribute("success", "Thêm danh mục thành công!");
            response.sendRedirect("categories?action=view&id=" + newCategory.getCategoryId());
        } else if ("update".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                Category category = CategoryDAO.findCategoryById(id);
                if (category != null) {
                    category.setCategoryName(request.getParameter("categoryName"));
                    category.setDescription(request.getParameter("description"));
                    category.setPhone(request.getParameter("phone"));
                    category.setMessage(request.getParameter("message"));
                    categoryDAO.updateCategory(category);
                    request.setAttribute("success", "Cập nhật thành công!");
                }
            }
            response.sendRedirect("categories?action=list");
        } else if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null) {
                int id = Integer.parseInt(idStr);
                categoryDAO.deleteCategory(id);
                request.setAttribute("success", "Xóa danh mục thành công!");
            }
            response.sendRedirect("categories?action=list");
        }
    }

    // Placeholder cho findAll; thêm vào DAO: return em.createQuery("SELECT c FROM Category c", Category.class).getResultList();
    private List<Category> getAllCategories() {
        // Tạm return empty; thực tế gọi categoryDAO.getAllCategories();
        return java.util.List.of();  // Import java.util.List nếu cần
    }
}