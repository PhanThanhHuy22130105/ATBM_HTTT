package controller.web;

import jakarta.mail.Session;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;
import utool.HelperClass;
import utool.JavaMailUtil;

import java.io.IOException;
import java.sql.Connection;

import dao.DBConnectionPool;
import dao.UserDAO;

/**
 * Servlet implementation class FotgotPasswordServlet
 */
@WebServlet("/getAuthCode")
public class GetAuthCode extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {

		HttpSession session = req.getSession();
		String a = (String) session.getAttribute("authCode");
		System.out.println(a);

		req.getRequestDispatcher("/ResetPassword.jsp").forward(req, res);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		try(Connection connection = DBConnectionPool.getDataSource().getConnection()) {
			String email = req.getParameter("email");
			String errorMessage = "";
			String url = "/ResetPassword.jsp";

			HttpSession session = req.getSession();
			User user = null;

			UserDAO userdao = new UserDAO(connection);
			user = userdao.findByEmail(email);

			if (user == null) {
				errorMessage = "Không tìm thấy người dùng với email này.";
				req.setAttribute("errorMessage", errorMessage);
				req.getRequestDispatcher(url).forward(req, res);
			} else {
				int authCode = HelperClass.generateRandom();
				session.setAttribute("authCode", authCode);
				session.setAttribute("userEmail", user.getEmail());
				req.getRequestDispatcher("/ResetPassword.jsp").forward(req, res);

				
				// Gửi email bằng thread để tăng tốc độ
				new Thread(() -> {
					try {
						JavaMailUtil.sendEmail(email, authCode);
						System.out.println("Email đã được gửi đến: " + email);
					} catch (Exception e) {
						e.printStackTrace();
						System.err.println("Gửi email thất bại: " + e.getMessage());
					}
				}).start();
			}
			
			
		} catch (Exception e2) {
			 throw new ServletException("Error connecting to the database", e2);
		}
	}

}
