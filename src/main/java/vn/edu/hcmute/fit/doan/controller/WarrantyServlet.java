//// src/main/java/vn/edu/hcmute/fit/doan/controller/WarrantyServlet.java
//package vn.edu.hcmute.fit.doan.controller;
//
//import jakarta.servlet.ServletException;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//import vn.edu.hcmute.fit.doan.User;
//import vn.edu.hcmute.fit.doan.Warranty;
//import vn.edu.hcmute.fit.doan.database.WarrantyDAO;
//
//import java.io.IOException;
//import java.util.Collections;
//import java.util.List;
//
//@WebServlet("/warranties")
//public class WarrantyServlet extends HttpServlet {
//    private final WarrantyDAO warrantyDAO = new WarrantyDAO();
//
//    // src/main/java/vn/edu/hcmute/fit/doan/controller/WarrantyServlet.java
//    @Override
//    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
//        HttpSession session = request.getSession(false);
//        if (session == null || session.getAttribute("user") == null) {
//            response.sendRedirect(request.getContextPath() + "/auth?action=login");
//            return;
//        }
//
//        String action = request.getParameter("action");
//        User user = (User) session.getAttribute("user");
//
//        // Action cho Admin
//        if ("adminList".equals(action) && "Admin".equals(user.getRole())) {
//            List<Warranty> warranties = warrantyDAO.getAll();
//            request.setAttribute("warranties", warranties);
//            request.getRequestDispatcher("/views/warranties/list.jsp").forward(request, response);
//        }
//        // Action cho User (mặc định)
//        else {
//            List<Warranty> warranties = warrantyDAO.findByUserId(user.getId());
//            request.setAttribute("warranties", warranties);
//            request.setAttribute("pageTitle", "Kiểm Tra Bảo Hành");
//            request.setAttribute("bodyContent", "/views/warranties/check.jsp");
//            request.setAttribute("customCss", "profile.css"); // Dùng chung style
//            request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
//        }
//    }
//}
package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.User;
import vn.edu.hcmute.fit.doan.Warranty;
import vn.edu.hcmute.fit.doan.database.WarrantyDAO;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@WebServlet("/warranties")
public class WarrantyServlet extends HttpServlet {
    private final WarrantyDAO warrantyDAO = new WarrantyDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect(request.getContextPath() + "/auth?action=login");
            return;
        }

        String action = request.getParameter("action");
        User user = (User) session.getAttribute("user");

        // Action cho Admin
        if ("adminList".equals(action) && "Admin".equals(user.getRole())) {
            List<Warranty> warranties = warrantyDAO.getAll();
            request.setAttribute("warranties", warranties);
            request.getRequestDispatcher("/views/warranties/list.jsp").forward(request, response);
        }
        // Action cho User (mặc định)
        else {
            List<Warranty> warranties = warrantyDAO.findByUserId(user.getId());
            System.out.println("Số lượng bảo hành tìm thấy: " + (warranties != null ? warranties.size() : 0));
            request.setAttribute("warranties", warranties);
            request.setAttribute("pageTitle", "Kiểm Tra Bảo Hành");
            request.setAttribute("bodyContent", "/views/warranties/check.jsp");
            request.setAttribute("customCss", "profile.css"); // Dùng chung style
            request.getRequestDispatcher("/views/layout/template.jsp").forward(request, response);
        }
    }
}