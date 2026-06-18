package controller.web;

import dao.DBConnectionPool;
import dao.OrderDAO;
import dao.SignatureDAO;
import models.Order;
import utool.DigitalSignatureUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.InputStream;
import java.io.IOException;
import java.sql.Connection;
import java.util.Base64;

@WebServlet("/verify-signature")
@MultipartConfig(
        fileSizeThreshold = 1024 * 10,  // 10 KB
        maxFileSize = 1024 * 1024,      // 1 MB
        maxRequestSize = 1024 * 1024 * 2 // 2 MB
)
public class VerifySignatureServlet extends HttpServlet {

    private SignatureDAO signatureDAO = new SignatureDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // Mở kết nối Database và truyền vào OrderDAO
        try (Connection conn = DBConnectionPool.getConnection()) {
            OrderDAO orderDAO = new OrderDAO(conn);

            // 1. Lấy ID đơn hàng từ form
            String orderIdStr = request.getParameter("orderId");
            if (orderIdStr == null || orderIdStr.isEmpty()) {
                throw new Exception("Không tìm thấy mã đơn hàng!");
            }
            int orderId = Integer.parseInt(orderIdStr);

            // 2. Nhận file chữ ký (.bin) người dùng upload
            Part filePart = request.getPart("signatureFile");
            if (filePart == null || filePart.getSize() == 0) {
                request.setAttribute("error", "Vui lòng chọn file chữ ký!");
                request.getRequestDispatcher("/secure/editProfile.jsp").forward(request, response);
                return;
            }

            InputStream fileContent = filePart.getInputStream();
            byte[] signatureBytes = fileContent.readAllBytes();

            // 3. Lấy Public Key từ DB
            String publicKeyBase64 = signatureDAO.getPublicKeyByOrderId(orderId);
            if (publicKeyBase64 == null) {
                request.setAttribute("error", "Không tìm thấy khóa công khai cho đơn hàng này!");
                request.getRequestDispatcher("/secure/editProfile.jsp").forward(request, response);
                return;
            }

            // 4. Lấy đơn hàng
            Order order = orderDAO.getOrderById(orderId);
            if (order == null) {
                request.setAttribute("error", "Đơn hàng không tồn tại!");
                request.getRequestDispatcher("/secure/editProfile.jsp").forward(request, response);
                return;
            }

            // ================== TÁI TẠO DỮ LIỆU BĂM KHỚP 100% ==================
            StringBuilder rawData = new StringBuilder();
            rawData.append("OrderID:").append(orderId).append("|");
            rawData.append("UserID:").append(order.getUserId()).append("|");
            rawData.append("Total:").append(order.getTotalAmount()).append("|");

            // Truy vấn lấy danh sách sản phẩm trong đơn hàng
            String sql = "SELECT product_id, quantity, price FROM `dbo.order_details` WHERE order_id = ?";
            try (java.sql.PreparedStatement ps = conn.prepareStatement(sql)) {
                ps.setInt(1, orderId);
                try (java.sql.ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        rawData.append("Product:").append(rs.getInt("product_id"))
                                .append("-Qty:").append(rs.getInt("quantity"))
                                .append("-Price:").append(String.valueOf(rs.getDouble("price"))).append("|");
                    }
                }
            }

            String currentData = rawData.toString();
            System.out.println("Dữ liệu băm lại lúc Verify: " + currentData);
            String hashData = DigitalSignatureUtil.hashOrderData(currentData);
            // =================================================================================

            // 5. Xác thực
            boolean isValid = DigitalSignatureUtil.verifySignature(hashData, signatureBytes, publicKeyBase64);

            if (isValid) {
                String signatureBase64 = Base64.getEncoder().encodeToString(signatureBytes);
                signatureDAO.updateSignature(orderId, signatureBase64);
                orderDAO.updateOrderStatus(orderId, "VERIFIED");

                response.sendRedirect(request.getContextPath() + "/secure/editProfile.jsp?msg=Xac+thuc+thanh+cong");
            } else {
                orderDAO.updateOrderStatus(orderId, "FAILED");
                request.setAttribute("error", "Chữ ký không hợp lệ! Dữ liệu đơn hàng hoặc khóa bảo mật không khớp.");
                request.getRequestDispatcher("/secure/editProfile.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Đã xảy ra lỗi hệ thống: " + e.getMessage());
            request.getRequestDispatcher("/secure/editProfile.jsp").forward(request, response);
        }
    }
}