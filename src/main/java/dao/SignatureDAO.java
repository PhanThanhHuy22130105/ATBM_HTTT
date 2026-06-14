package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class SignatureDAO {

    /**
     * Lưu Public Key vào bảng user_keys và trả về ID của khóa vừa tạo
     */
    public long savePublicKey(long userId, String publicKeyBase64) {
        String sql = "INSERT INTO `dbo.user_keys` (user_id, public_key, status) VALUES (?, ?, 'ACTIVE')";
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setLong(1, userId);
            ps.setString(2, publicKeyBase64);
            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lưu Public Key: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Lưu thông tin băm của đơn hàng vào bảng orders_sign
     */
    public void saveOrderSign(long orderId, String hashData, long keyId) {
        String sql = "INSERT INTO `dbo.orders_sign` (order_id, hash_data, key_id) VALUES (?, ?, ?)";
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ps.setString(2, hashData);
            ps.setLong(3, keyId);
            ps.executeUpdate();

        } catch (Exception e) {
            System.out.println("Lỗi khi lưu Order Sign: " + e.getMessage());
            e.printStackTrace();
        }
    }
}