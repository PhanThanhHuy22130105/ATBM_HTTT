package controller.admin.management.user;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.DBConnectionPool;
import dao.UserDAO;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.User;

@WebServlet("/admin/EditUserServlet")
public class EditUserServlet extends HttpServlet {

    // GET method: Lấy thông tin người dùng để hiển thị trên form chỉnh sửa
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = -1;

        // Lấy tham số ID từ request
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            // Nếu id không hợp lệ, trả về lỗi
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
            return;
        }

        // Sử dụng try-with-resources để quản lý kết nối cơ sở dữ liệu
        try (Connection connection = DBConnectionPool.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);

            // Lấy thông tin người dùng từ cơ sở dữ liệu
            User user = userDAO.getUser(id);
            if (user != null) {
                // Truyền đối tượng user vào request và forward đến trang editUser.jsp
                request.setAttribute("user", user);
                RequestDispatcher dispatcher = request.getRequestDispatcher("/admin/edit_user.jsp");
                dispatcher.forward(request, response);
            } else {
                // Nếu không tìm thấy người dùng, trả về lỗi 404
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Người dùng không tìm thấy.");
            }
        } catch (SQLException e) {
            // Xử lý lỗi khi không thể kết nối cơ sở dữ liệu
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi kết nối cơ sở dữ liệu.");
            e.printStackTrace();
        }
    }

    // POST method: Cập nhật thông tin người dùng
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int id = -1;

        // Lấy tham số ID từ request
        try {
            id = Integer.parseInt(request.getParameter("id"));
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "ID không hợp lệ.");
            return;
        }

        // Lấy các tham số chỉnh sửa từ form
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String phone = request.getParameter("phone");
        String address = request.getParameter("address");

        // Kiểm tra tính hợp lệ của các tham số
        if (username == null || email == null || phone == null || address == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Dữ liệu không hợp lệ.");
            return;
        }

        // Sử dụng try-with-resources để quản lý kết nối cơ sở dữ liệu
        try (Connection connection = DBConnectionPool.getConnection()) {
            UserDAO userDAO = new UserDAO(connection);

            // Lấy thông tin người dùng
            User user = userDAO.getUser(id);
            if (user != null) {
                // Cập nhật thông tin người dùng
                boolean isUpdated = userDAO.editProfile(user, username, email, Integer.parseInt(phone), address);
                if (isUpdated) {
                    // Nếu cập nhật thành công, chuyển hướng đến trang quản lý người dùng
                    response.sendRedirect("ManageUserServlet");
                } else {
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Cập nhật thông tin người dùng thất bại.");
                }
            } else {
                response.sendError(HttpServletResponse.SC_NOT_FOUND, "Người dùng không tìm thấy.");
            }
        } catch (SQLException e) {
            // Xử lý lỗi khi thực hiện thao tác với cơ sở dữ liệu
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Lỗi truy vấn dữ liệu.");
            e.printStackTrace();
        }
    }
}
