package controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.DBConnectionPool;
import dao.UserDAO;

/**
 * Servlet implementation class ChangePassword
 */
@WebServlet("/ChangePassword")
public class ChangePassword extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		req.getRequestDispatcher("/ChangePassword.jsp").forward(req, res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try(Connection connection = DBConnectionPool.getDataSource().getConnection()) {


			HttpSession session = req.getSession();
			UserDAO userDAO = new UserDAO(connection);

			String  p1 = req.getParameter("password1");
			String  p2 = req.getParameter("password2");

			String email = (String) session.getAttribute("userEmail");
			System.out.println("mail la " + email);
			System.out.println("mail la " + p1);
			if(p1.equals(p2) ) {
				try {
					userDAO.updatePassword(email, p1);
					session.invalidate();
					res.sendRedirect(req.getContextPath() + "/Login.jsp");
				} catch (SQLException e) {
					req.setAttribute("message", "Có lỗi xảy ra khi cập nhật mật khẩu. Vui lòng thử lại.");
					req.getRequestDispatcher("/ChangePassword.jsp").forward(req, res);
					e.printStackTrace();
				}
			} else {
				req.setAttribute("message", "mật khẩu không giống nhau");
				req.getRequestDispatcher("/ChangePassword.jsp").forward(req, res);
			}

		} catch (Exception e) {
			throw new ServletException("Error connecting to the database", e);		}
	}

}
