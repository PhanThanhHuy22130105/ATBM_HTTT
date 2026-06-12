package controller.admin.management.user;

import java.io.IOException;
import java.sql.SQLException;

import dao.DBConnectionPool;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/deleteUser")
public class DeleteUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int userId = -1;

        // Lấy tham số id từ request
        try {
            userId = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
            return;
        }

        UserDAO userDAO = null;

        // Tạo đối tượng UserDAO và xử lý kết nối
        try {
            userDAO = new UserDAO(DBConnectionPool.getConnection());
            boolean deleted = userDAO.deleteUser(userId); // Xóa người dùng từ cơ sở dữ liệu
            if (deleted) {
                response.sendRedirect(request.getContextPath() + "/manageUsers"); // Quay lại trang danh sách người dùng
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi xóa người dùng.");
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }
}
