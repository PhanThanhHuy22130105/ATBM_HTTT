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
import models.User;

@WebServlet("/insertUser")
public class InsertUserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Kiểm tra các trường có null hoặc trống không
        if (username == null || password == null || email == null || phone == null || address == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Các trường không được để trống.");
            return;
        }

        UserDAO userDAO = null;

        try {
            userDAO = new UserDAO(DBConnectionPool.getConnection());
            User newUser = new User();
            newUser.setUsername(username);
            newUser.setPassword(password);
            newUser.setEmail(email);
            newUser.setPhone(phone);
            newUser.setAddress(address);
            newUser.setImg("image/avatars/default-avatar.png"); // Đường dẫn ảnh mặc định
            newUser.setIsAdmin(false); // Đặt là người dùng bình thường (có thể thay đổi sau)

            boolean inserted = userDAO.insertUser(newUser); // Thêm người dùng mới vào cơ sở dữ liệu
            if (inserted) {
                response.sendRedirect("ManageUserServlet"); // Quay lại trang danh sách người dùng
            } else {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi khi thêm người dùng.");
            }
        } catch (SQLException e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }
}
