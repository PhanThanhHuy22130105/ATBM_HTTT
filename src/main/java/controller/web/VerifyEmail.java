package controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import utool.JavaMailUtil;

import java.io.IOException;

/**
 * Servlet implementation class VerifyEmail
 */
@WebServlet("/VerifyEmail")
public class VerifyEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		HttpSession session = req.getSession();
		Integer authCode = (Integer) session.getAttribute("authCode");
		String userMail = (String) session.getAttribute("userEmail");
		JavaMailUtil.sendEmail(userMail, authCode);
		super.doGet(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		
		HttpSession session = req.getSession();
		Integer authCode = (Integer) session.getAttribute("authCode");
		String userMail = (String) session.getAttribute("userEmail");
		
//		System.out.println("Session email: " + session.getAttribute("userEmail"));
//		System.out.println("User email: " + userMail);
		
		
		String userInputStr = req.getParameter("authCode");
		// Check if userInputStr is not null and is a valid integer
	    if (userInputStr != null) {
	        try {
	            int userInput = Integer.parseInt(userInputStr);
	            if (authCode != null && authCode.equals(userInput)) {
	            	System.out.println("Session email: " + session.getAttribute("userEmail"));
	        		System.out.println("User email: " + userMail);
	        		
	            	req.getRequestDispatcher("/ChangePassword.jsp").forward(req, res);
	            } else {
	                req.setAttribute("message", "Sai mã xác minh");
	                req.getRequestDispatcher("/ResetPassword.jsp").forward(req, res);
	            }
	        } catch (NumberFormatException e) {
	            req.setAttribute("message", "Mã xác minh không hợp lệ");
	            req.getRequestDispatcher("/ResetPassword.jsp").forward(req, res);
	        }
	    } else {
	        req.setAttribute("message", "Mã xác minh không được để trống");
	        req.getRequestDispatcher("/ResetPassword.jsp").forward(req, res);
	    }
		
	}

}
