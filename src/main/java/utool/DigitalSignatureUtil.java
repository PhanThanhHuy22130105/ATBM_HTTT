package utool;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class DigitalSignatureUtil {

    /**
     * Hàm băm dữ liệu đơn hàng bằng thuật toán SHA-256 (Giữ nguyên)
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
     * Hàm tạo cặp khóa RSA (Public Key & Private Key) độ dài 2048 bit (Giữ nguyên)
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

    /**
     * Hàm MỚI: Xác thực chữ ký điện tử do khách hàng upload lên (Dùng cho Bước 4)
     * @param hashData Chuỗi băm (Hash) vừa được băm lại từ CSDL hiện tại
     * @param signatureBytes Mảng byte của file chữ ký (.bin) khách tải lên
     * @param publicKeyBase64 Chuỗi Public Key dạng Base64 lấy từ Database
     * @return true nếu chữ ký hợp lệ, ngược lại false
     */
    public static boolean verifySignature(String hashData, byte[] signatureBytes, String publicKeyBase64) {
        try {
            // 1. Chuyển đổi chuỗi Public Key (Base64) thành đối tượng PublicKey chuẩn của Java
            byte[] publicBytes = Base64.getDecoder().decode(publicKeyBase64);
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(publicBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(keySpec);

            // 2. Khởi tạo bộ máy Signature với thuật toán SHA256withRSA
            Signature signatureEngine = Signature.getInstance("SHA256withRSA");

            // 3. Nạp Public Key vào để chuẩn bị đối chiếu
            signatureEngine.initVerify(publicKey);

            // 4. Nạp dữ liệu gốc đã băm vào (Hash)
            signatureEngine.update(hashData.getBytes("UTF-8"));

            // 5. Thực hiện so khớp chữ ký (Signature)
            return signatureEngine.verify(signatureBytes);

        } catch (Exception e) {
            System.out.println("Lỗi xác thực chữ ký: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}