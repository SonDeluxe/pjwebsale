package vn.edu.hcmute.fit.doan.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import vn.edu.hcmute.fit.doan.Shipment;
import vn.edu.hcmute.fit.doan.Order;
import vn.edu.hcmute.fit.doan.database.ShipmentDAO;
import vn.edu.hcmute.fit.doan.database.OrderDAO;

import java.io.IOException;
import java.util.List;

@WebServlet("/shipments")
public class ShipmentServlet extends HttpServlet {
    private ShipmentDAO shipmentDAO = new ShipmentDAO();
    private OrderDAO orderDAO = new OrderDAO();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        boolean isAdmin = "Admin".equals(session.getAttribute("role"));

        if ("list".equals(action)) {
            List<Shipment> shipments = getAllShipments();
            request.setAttribute("shipments", shipments);
            request.getRequestDispatcher("/views/shipments/list.jsp").forward(request, response);
        } else if ("view".equals(action) || "edit".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                Shipment shipment = shipmentDAO.findShipmentById(idStr);
                if (shipment != null) {
                    request.setAttribute("shipment", shipment);
                    if (shipment.getOrder() != null) {
                        request.setAttribute("order", shipment.getOrder());
                    }
                    String jsp = "view".equals(action) ? "/views/shipments/view.jsp" : "/views/shipments/edit.jsp";
                    request.getRequestDispatcher(jsp).forward(request, response);
                } else {
                    request.setAttribute("error", "Không tìm thấy lô hàng!");
                    response.sendRedirect("shipments?action=list");
                }
            } else {
                response.sendRedirect("shipments?action=list");
            }
        } else {
            response.sendRedirect("shipments?action=list");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            response.sendRedirect("auth?action=login");
            return;
        }

        boolean isAdmin = "Admin".equals(session.getAttribute("role"));

        request.setCharacterEncoding("UTF-8");

        String action = request.getParameter("action");
        if ("add".equals(action)) {
            String orderIdStr = request.getParameter("orderId");
            String deliveryDate = request.getParameter("deliveryDate");
            String trackingNumber = request.getParameter("trackingNumber");
            String status = request.getParameter("status");

            if (orderIdStr != null && !orderIdStr.isEmpty()) {
                Order order = orderDAO.findOrderById(Integer.parseInt(orderIdStr));
                if (order != null) {
                    Shipment newShipment = new Shipment(deliveryDate, trackingNumber, status);
                    newShipment.setOrder(order);
                    shipmentDAO.addShipment(newShipment);
                    request.setAttribute("success", "Thêm lô hàng thành công!");
                    response.sendRedirect("shipments?action=view&id=" + newShipment.getShipmentId());
                } else {
                    request.setAttribute("error", "Đơn hàng không hợp lệ!");
                    request.getRequestDispatcher("/views/shipments/add.jsp").forward(request, response);
                }
            }
        } else if ("update".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty()) {
                Shipment shipment = shipmentDAO.findShipmentById(idStr);
                if (shipment != null && isAdmin) {
                    String status = request.getParameter("status");
                    if (status != null) {
                        shipment.setStatus(status);
                        shipment.updateStatus(status);
                    }
                    String deliveryDate = request.getParameter("deliveryDate");
                    if (deliveryDate != null) shipment.setDeliveryDate(deliveryDate);
                    String trackingNumber = request.getParameter("trackingNumber");
                    if (trackingNumber != null) shipment.setTrackingNumber(trackingNumber);
                    shipmentDAO.updateShipment(shipment);
                    request.setAttribute("success", "Cập nhật lô hàng thành công!");
                }
            }
            response.sendRedirect("shipments?action=list");
        } else if ("delete".equals(action)) {
            String idStr = request.getParameter("id");
            if (idStr != null && !idStr.isEmpty() && isAdmin) {
                shipmentDAO.deleteShipment(idStr);
                request.setAttribute("success", "Xóa lô hàng thành công!");
            }
            response.sendRedirect("shipments?action=list");
        }
    }

    // Placeholder cho findAll; thêm vào DAO: return em.createQuery("SELECT s FROM Shipment s", Shipment.class).getResultList();
    private List<Shipment> getAllShipments() {
        return java.util.List.of();
    }

    // Placeholder cho getByOrderId; thêm vào DAO: em.createQuery("SELECT s FROM Shipment s WHERE s.order.id = :orderId", Shipment.class).setParameter("orderId", orderId).getResultList();
    private List<Shipment> getShipmentsByOrderId(int orderId) {
        return java.util.List.of();
    }
}