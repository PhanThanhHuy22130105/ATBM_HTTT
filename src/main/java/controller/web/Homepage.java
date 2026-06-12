package controller.web;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Category;
import models.Product;
import dao.CategoryDAO;
import dao.DBConnectionPool;
import dao.ProductDAO;

/**
 * Servlet implementation class Homepage
 */
@WebServlet("/Homepage")
public class Homepage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		List<Product> productList = new ProductDAO().getAllProducts();
        List<Category> categoryList = new CategoryDAO().getAllCategories();

        HttpSession session = request.getSession();
        request.setAttribute("productList", productList);
        ServletContext context = getServletContext();
        context.setAttribute("categoryList", categoryList);
        
        request.getRequestDispatcher("/Homepage.jsp").forward(request, response);
        	
	}



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
