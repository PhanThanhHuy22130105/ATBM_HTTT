package controller.web;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Part;
import models.User;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

import dao.DBConnectionPool;
import dao.UserDAO;

/**
 * Servlet implementation class UploadServlet
 */
@MultipartConfig(fileSizeThreshold = 1024 * 1024 *2,
	maxFileSize = 1024 * 1024 * 10,
	maxRequestSize = 1024 * 1024 *50
)
@WebServlet("/UploadServlet")
public class UploadServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		request.getRequestDispatcher("upload.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		try {
			HttpSession session = request.getSession();
			User user = (User) session.getAttribute("user");
			String id = user.getId()+"";
			String name = user.getUsername();
			Part part = request.getPart("photo");
			
			String realPath = request.getServletContext().getRealPath("/image/avatars");
			String filename = Path.of(part.getSubmittedFileName()).getFileName().toString();
			
			if(!Files.exists(Path.of(realPath))) {
				Files.createDirectory(Path.of(realPath));
			}
			
			String picPath = realPath + "/" + filename;
			part.write(picPath);
			
			UserDAO dao = new UserDAO(DBConnectionPool.getDataSource().getConnection());
			
			dao.changeImg(user.getId(), "image/avatars/" + filename);
            session.setAttribute("img", "image/avatars/" + filename);
            
			response.sendRedirect(request.getContextPath() + "/Homepage");
			
		} catch (Exception e) {
			e.printStackTrace();
		
		}
		
	}

}
