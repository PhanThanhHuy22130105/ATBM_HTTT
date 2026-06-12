package controller.admin.management.user;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import dao.DBConnectionPool;
import dao.UserDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;

@WebServlet("/manageUsers")
public class ManageUserServlet extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserDAO userDAO = null;
        try {
            userDAO = new UserDAO(DBConnectionPool.getConnection());
            List<User> userList = userDAO.getAllUsers(); // Lấy tất cả người dùng
            request.setAttribute("userList", userList); // Set danh sách người dùng vào request attribute
            request.getRequestDispatcher("/admin/manage_users.jsp").forward(request, response); // Chuyển tiếp tới JSP
        } catch (SQLException e) {
            // Xử lý lỗi khi không thể kết nối cơ sở dữ liệu
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }
}
