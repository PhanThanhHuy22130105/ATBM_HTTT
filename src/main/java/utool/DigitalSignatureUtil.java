package utool;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.util.Base64;

public class DigitalSignatureUtil {

    /**
     * Hàm băm dữ liệu đơn hàng bằng thuật toán SHA-256
     */
    public static String hashOrderData(String data) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(data.getBytes("UTF-8"));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi băm dữ liệu đơn hàng", e);
        }
    }

    /**
     * Hàm tạo cặp khóa RSA (Public Key & Private Key) độ dài 2048 bit
     */
    public static KeyPair generateRSAKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            return keyGen.generateKeyPair();
        } catch (Exception e) {
            throw new RuntimeException("Lỗi khi tạo cặp khóa RSA", e);
        }
    }
}