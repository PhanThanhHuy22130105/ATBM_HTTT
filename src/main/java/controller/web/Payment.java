package controller.web;

import dao.PaymentDAO;
import dao.SignatureDAO;
import utool.DigitalSignatureUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.User;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.KeyPair;
import java.util.Base64;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@WebServlet("/secure/payment")
public class Payment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PaymentDAO paymentDAO;
    private SignatureDAO signatureDAO;

    @Override
    public void init() throws ServletException {
        paymentDAO = new PaymentDAO();
        signatureDAO = new SignatureDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=Please login to proceed with payment");
            return;
        }
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/secure/cart?error=Your cart is empty");
            return;
        }
        System.out.println("doGet called: Forwarding to payment.jsp");
        request.getRequestDispatcher("/secure/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=Please login to proceed with payment");
            return;
        }
        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/secure/cart?error=Your cart is empty");
            return;
        }

        String action = request.getParameter("action");
        System.out.println("doPost called: action = " + action);

        if ("proceedToPayment".equals(action)) {
            System.out.println("Action = proceedToPayment: Forwarding to payment.jsp");
            request.getRequestDispatcher("/secure/payment.jsp").forward(request, response);

        } else if ("pay".equals(action)) {
            System.out.println("Action = pay: Processing payment");
            String[] productIds = request.getParameterValues("productIds");
            String[] quantities = request.getParameterValues("quantities");
            String[] prices = request.getParameterValues("prices");
            double total = Double.parseDouble(request.getParameter("total"));

            try {
                // Lưu đơn hàng
                long orderId = paymentDAO.saveOrder(user.getId(), total, productIds, quantities, prices);

                // Gom dữ liệu băm
                StringBuilder rawData = new StringBuilder();
                rawData.append("OrderID:").append(orderId).append("|");
                rawData.append("UserID:").append(user.getId()).append("|");
                rawData.append("Total:").append(total).append("|");
                for (int i = 0; i < productIds.length; i++) {
                    rawData.append("Product:").append(productIds[i])
                            .append("-Qty:").append(quantities[i])
                            .append("-Price:").append(prices[i]).append("|");
                }

                // Xử lý thuật toán (Hash, RSA)
                String hashData = DigitalSignatureUtil.hashOrderData(rawData.toString());
                KeyPair keyPair = DigitalSignatureUtil.generateRSAKeyPair();
                String publicKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded());
                String privateKeyBase64 = Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded());

                // Lưu Khóa và Băm xuống DB
                long keyId = signatureDAO.savePublicKey(user.getId(), publicKeyBase64);
                signatureDAO.saveOrderSign(orderId, hashData, keyId);

                // Xóa giỏ hàng
                cart.clearCart();
                session.setAttribute("cart", cart);

                // Trả file ZIP chứa (Hash, Private Key, và file Tool .exe)
                response.setContentType("application/zip");
                response.setHeader("Content-Disposition", "attachment; filename=\"Order_Signature_Kit_" + orderId + ".zip\"");

                try (OutputStream out = response.getOutputStream();
                     ZipOutputStream zos = new ZipOutputStream(out)) {

                    // Gói file Hash Data
                    ZipEntry hashEntry = new ZipEntry("Order_Hash.txt");
                    zos.putNextEntry(hashEntry);
                    zos.write(("Đây là mã băm (Hash) đơn hàng của bạn. Hãy dùng nó trong Tool tạo chữ ký:\n\n" + hashData).getBytes("UTF-8"));
                    zos.closeEntry();

                    // Gói file Private Key
                    ZipEntry keyEntry = new ZipEntry("Private_Key.txt");
                    zos.putNextEntry(keyEntry);
                    zos.write(("Đây là khóa bảo mật cá nhân (Private Key) của bạn. TUYỆT ĐỐI KHÔNG CHIA SẺ CHO AI!\n\n" + privateKeyBase64).getBytes("UTF-8"));
                    zos.closeEntry();

                    // Gói file tool
                    String exactToolPath = "/tools/DigitalSignerTool.exe/DigitalSignerTool.exe";
                    InputStream toolStream = getServletContext().getResourceAsStream(exactToolPath);

                    if (toolStream != null) {
                        // Tên file khi giải nén ra sẽ là DigitalSignerTool.exe
                        ZipEntry toolEntry = new ZipEntry("DigitalSignerTool.exe");
                        zos.putNextEntry(toolEntry);

                        byte[] buffer = new byte[4096];
                        int bytesRead;
                        while ((bytesRead = toolStream.read(buffer)) != -1) {
                            zos.write(buffer, 0, bytesRead);
                        }

                        toolStream.close();
                        zos.closeEntry();
                        System.out.println("Đã đính kèm thành công Tool Ký vào file ZIP.");
                    } else {
                        System.out.println("Cảnh báo: Không tìm thấy file Tool Ký tại đường dẫn: " + exactToolPath);
                    }
                    // ---------------------------------------------------------

                } // Kết thúc khối try (đóng zos tự động)

                session.setAttribute("paymentSuccess", "Thanh toán thành công! Mã đơn: " + orderId + ". Vui lòng kiểm tra file ZIP vừa tải về.");
                return;

            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/secure/payment?error=Payment failed: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid action: Redirecting to cart.jsp");
            response.sendRedirect(request.getContextPath() + "/secure/cart?error=Invalid request: action=" + action);
        }
    }
}