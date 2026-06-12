package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OrderDAO {
    private Connection conn;

    public OrderDAO(Connection connection) {
        this.conn = connection;
    }

    // Tạo đơn hàng mới
    public int createOrder(int userId, double totalPrice, String status) {
        String sql = "INSERT INTO `dbo.orders` (user_id, total_price, status) VALUES (?, ?, ?)";
        try ( PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, totalPrice);
            stmt.setString(3, status);
            stmt.executeUpdate();

            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) return rs.getInt(1); // Trả về orderId
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Lỗi
    }

    // Thêm item vào đơn hàng
    public void addOrderItem(int orderId, int productId, int quantity, double price) {
        String sql = "INSERT INTO `dbo.order_items` (order_id, product_id, quantity, price) VALUES (?, ?, ?, ?)";
        try ( PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.setInt(2, productId);
            stmt.setInt(3, quantity);
            stmt.setDouble(4, price);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}