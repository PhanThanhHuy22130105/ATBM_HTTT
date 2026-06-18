package controller.web;

import dao.SignatureDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/revoke-key")
public class RevokeKeyServlet extends HttpServlet {

    private SignatureDAO signatureDAO = new SignatureDAO();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy ID của User đang đăng nhập từ form
            String userIdStr = request.getParameter("userId");
            if (userIdStr != null && !userIdStr.isEmpty()) {
                long userId = Long.parseLong(userIdStr);

                // Gọi hàm revokeKey trong SignatureDAO mà chúng ta đã viết lúc trước
                boolean isRevoked = signatureDAO.revokeKey(userId);

                if (isRevoked) {
                    // Chuyển hướng về trang profile kèm thông báo thành công
                    response.sendRedirect(request.getContextPath() + "/secure/editProfile.jsp?revokeSuccess=true");
                } else {
                    response.sendRedirect(request.getContextPath() + "/secure/editProfile.jsp?error=Khong+tim+thay+khoa+de+huy");
                }
            } else {
                response.sendRedirect(request.getContextPath() + "/secure/editProfile.jsp?error=Loi+xac+thuc+User");
            }
        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/secure/editProfile.jsp?error=Loi+he+thong");
        }
    }
}