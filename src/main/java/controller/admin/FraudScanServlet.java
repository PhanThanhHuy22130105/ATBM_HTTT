package controller.admin;

import dao.DBConnectionPool;
import dao.OrderDAO;
import dao.SignatureDAO;
import models.Order;
import utool.DigitalSignatureUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Base64;

@WebServlet("/admin/scan-fraud")
public class FraudScanServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int tamperedCount = 0;

        try (Connection conn = DBConnectionPool.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);
            SignatureDAO signatureDAO = new SignatureDAO();

            String sqlGetOrders = "SELECT id FROM `dbo.orders` WHERE status = 'VERIFIED'";
            try (PreparedStatement psOrder = conn.prepareStatement(sqlGetOrders);
                 ResultSet rsOrder = psOrder.executeQuery()) {

                while (rsOrder.next()) {
                    int orderId = rsOrder.getInt("id");
                    Order order = orderDAO.getOrderById(orderId);

                    String publicKeyBase64 = signatureDAO.getPublicKeyByOrderId(orderId);
                    String sqlGetSig = "SELECT signature FROM `dbo.orders_sign` WHERE order_id = ?";
                    String signatureBase64 = null;

                    try (PreparedStatement psSig = conn.prepareStatement(sqlGetSig)) {
                        psSig.setInt(1, orderId);
                        try (ResultSet rsSig = psSig.executeQuery()) {
                            if (rsSig.next()) signatureBase64 = rsSig.getString("signature");
                        }
                    }

                    if (publicKeyBase64 != null && signatureBase64 != null) {
                        byte[] signatureBytes = Base64.getDecoder().decode(signatureBase64);

                        StringBuilder rawData = new StringBuilder();
                        rawData.append("OrderID:").append(orderId).append("|");
                        rawData.append("UserID:").append(order.getUserId()).append("|");
                        rawData.append("Total:").append(order.getTotalAmount()).append("|");

                        String sqlItems = "SELECT product_id, quantity, price FROM `dbo.order_details` WHERE order_id = ?";
                        try (PreparedStatement psItems = conn.prepareStatement(sqlItems)) {
                            psItems.setInt(1, orderId);
                            try (ResultSet rsItems = psItems.executeQuery()) {
                                while (rsItems.next()) {
                                    rawData.append("Product:").append(rsItems.getInt("product_id"))
                                            .append("-Qty:").append(rsItems.getInt("quantity"))
                                            .append("-Price:").append(String.valueOf(rsItems.getDouble("price"))).append("|");
                                }
                            }
                        }

                        String currentData = rawData.toString();
                        String hashData = DigitalSignatureUtil.hashOrderData(currentData);

                        boolean isValid = DigitalSignatureUtil.verifySignature(hashData, signatureBytes, publicKeyBase64);

                        if (!isValid) {
                            orderDAO.updateOrderStatus(orderId, "TAMPERED");
                            tamperedCount++;
                        }
                    }
                }
            }

            // Trả về trang Dashboard hiện tại của bạn
            response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp?fraudCount=" + tamperedCount);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/dashboard.jsp?error=Loi+quet+du+lieu");
        }
    }
}