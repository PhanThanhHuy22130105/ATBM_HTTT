package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;

public class PaymentDAO {
    public long saveOrder(int userId, double total, String[] productIds, String[] quantities, String[] prices) throws SQLException {
        Connection conn = null;
        PreparedStatement pstmt = null;
        long orderId = 0;

        try {
            // Lấy kết nối từ HikariCP thông qua DBConnectionPool
            conn = DBConnectionPool.getConnection();
            conn.setAutoCommit(false);

            // Thêm đơn hàng vào bảng orders
            String insertOrderSQL = "INSERT INTO `dbo.orders` (user_id, total, status, created_at) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertOrderSQL, PreparedStatement.RETURN_GENERATED_KEYS);
            pstmt.setInt(1, userId);
            pstmt.setDouble(2, total);
            pstmt.setString(3, "PENDING");
            pstmt.setTimestamp(4, new Timestamp(System.currentTimeMillis()));
            pstmt.executeUpdate();

            // Lấy ID của đơn hàng vừa tạo
            try (var rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    orderId = rs.getLong(1);
                } else {
                    throw new SQLException("Creating order failed, no ID obtained.");
                }
            }

            // Thêm chi tiết đơn hàng vào bảng order_details
            String insertOrderDetailSQL = "INSERT INTO `dbo.order_details` (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
            pstmt = conn.prepareStatement(insertOrderDetailSQL);
            for (int i = 0; i < productIds.length; i++) {
                pstmt.setLong(1, orderId);
                pstmt.setInt(2, Integer.parseInt(productIds[i]));
                pstmt.setInt(3, Integer.parseInt(quantities[i]));
                pstmt.setDouble(4, Double.parseDouble(prices[i]));
                pstmt.executeUpdate();
            }

            // Commit giao dịch
            conn.commit();
        } catch (Exception e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
            throw new SQLException("Error saving order: " + e.getMessage(), e);
        } finally {
            if (pstmt != null) try { pstmt.close(); } catch (SQLException e) { e.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException e) { e.printStackTrace(); }
        }

        return orderId;
    }
}