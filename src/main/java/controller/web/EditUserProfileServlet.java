package controller.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.DBConnectionPool;
import dao.UserDAO;
import jakarta.mail.Session;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

/**
 * Servlet implementation class EditUserProfileServlet
 */
@WebServlet("/secure/edit")
public class EditUserProfileServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Lấy user_id từ tham số URL
        String userIdStr = request.getParameter("user_id");
		
        if (userIdStr != null) {
            try {
            	
                int userId = Integer.parseInt(userIdStr);
                UserDAO userDAO = new UserDAO(DBConnectionPool.getDataSource().getConnection());
            	
                User user = userDAO.findById(userId);
                if (user != null) {
                    // Đặt người dùng vào request để hiển thị trong JSP
                    request.setAttribute("user", user);
                    // Chuyển hướng tới trang chỉnh sửa thông tin người dùng
                    request.getRequestDispatcher("/secure/editProfile.jsp").forward(request, response);
                } else {
                    response.sendRedirect(request.getContextPath() + "/login");
                }
                
            } catch (NumberFormatException | SQLException e) {
            	HttpSession session = request.getSession();
            	session.setAttribute("errorMessage", e.getMessage());
                response.sendRedirect(request.getContextPath() + "/error.jsp");
            }
        } else {
        	
            response.sendRedirect(request.getContextPath() + "/error.jsp");
        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
		
		HttpSession session = req.getSession();
		User user = (User) session.getAttribute("user");
		System.out.println(user.toString());
		if(user != null) {
			String name = req.getParameter("username");
			String email = req.getParameter("email");
			String phonestr = req.getParameter("phone");
			String address = req.getParameter("address");
			
			int phone = Integer.parseInt(phonestr);
			UserDAO dao = new UserDAO(connection);
			User updatedUser = dao.editProfileUser(user, name, email, phone, address);
			if(updatedUser != null) {
				session.setAttribute("user", updatedUser);
			}
			
			
		}
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		res.sendRedirect(req.getContextPath() +"/Homepage");
		
		
	}

}
