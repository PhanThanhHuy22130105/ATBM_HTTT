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

    // ================= CÁC HÀM MỚI BỔ SUNG CHO BƯỚC 4 VÀ BƯỚC 5 =================

    /**
     * (BƯỚC 4) Lấy Public Key dựa trên orderId để xác thực chữ ký
     */
    public String getPublicKeyByOrderId(long orderId) {
        // Kết hợp 2 bảng để tìm chính xác Public Key đã dùng cho đơn hàng này
        String sql = "SELECT k.public_key FROM `dbo.user_keys` k INNER JOIN `dbo.orders_sign` o ON k.id = o.key_id WHERE o.order_id = ?";
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("public_key");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy Public Key cho đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * (BƯỚC 4) Cập nhật chữ ký người dùng đã tải lên vào bảng orders_sign
     */
    public boolean updateSignature(long orderId, String signatureBase64) {
        String sql = "UPDATE `dbo.orders_sign` SET signature = ? WHERE order_id = ?";
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, signatureBase64);
            ps.setLong(2, orderId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Lỗi khi cập nhật chữ ký: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * (BƯỚC 5) Báo mất khóa: Đổi trạng thái khóa hiện tại thành REVOKED
     */
    public boolean revokeKey(long userId) {
        String sql = "UPDATE `dbo.user_keys` SET status = 'REVOKED', revoked_at = CURRENT_TIMESTAMP WHERE user_id = ? AND status = 'ACTIVE'";
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, userId);
            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.out.println("Lỗi khi revoke (hủy) khóa: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * (BƯỚC 5) Lấy chữ ký gốc do khách đã ký để kiểm tra xem nhân viên có sửa giá đơn hàng không
     */
    public String getOrderSignature(long orderId) {
        String sql = "SELECT signature FROM `dbo.orders_sign` WHERE order_id = ?";
        try (Connection conn = DBConnectionPool.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setLong(1, orderId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getString("signature");
            }
        } catch (Exception e) {
            System.out.println("Lỗi khi lấy chữ ký đơn hàng: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}