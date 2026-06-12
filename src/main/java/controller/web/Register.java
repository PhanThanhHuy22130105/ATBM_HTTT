package controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.User;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;

import dao.DBConnectionPool;
import dao.UserDAO;

/**
 * Servlet implementation class Register
 */
@WebServlet("/Register")
public class Register extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		// TODO Auto-generated method stub
	    req.getRequestDispatcher("/Register.jsp").forward(req, res);

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		try (Connection connection = DBConnectionPool.getDataSource().getConnection()) { // Lấy connection từ pool
			
			String message = "";
            String username = req.getParameter("username");
            String password = req.getParameter("password");
            String email = req.getParameter("email");
            String phoneNumber = req.getParameter("phoneNumber");

			UserDAO userDAO = new UserDAO(connection);
            if(userDAO.checkEmailExist(email)) {
    			message += " Invalid Email";
                req.setAttribute("Rmessage", message);
                req.getRequestDispatcher("/Register.jsp").forward(req, res);
                return;
                
            }
            if(userDAO.checkUsername(username)) {
            	message += " Username existed";
                req.setAttribute("Rmessage", message);
                req.getRequestDispatcher("/Register.jsp").forward(req, res);
                return;

            }
            
            User user = new User();
            user.setUsername(username);
            user.setPassword(password);
            user.setEmail(email);
            user.setPhone(phoneNumber);
            user.setIsAdmin(false);
            
            
            if (userDAO.registerUser(user)) {
                res.sendRedirect(req.getContextPath() + "/Login.jsp");
            }
            
		} catch (Exception e) {
            throw new ServletException("Error connecting to the database", e);
        }
		
		
		
	}

}
