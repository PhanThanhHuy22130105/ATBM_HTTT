package controller.admin.management.category;

import dao.CategoryDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/DeleteCategoryServlet")
public class DeleteCategoryServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		int categoryId = Integer.parseInt(request.getParameter("id"));
		CategoryDAO categoryDAO = new CategoryDAO();
		boolean isDeleted = categoryDAO.deleteCategory(categoryId);

		if (isDeleted) {
			response.sendRedirect("ManageCategoryServlet");
		} else {
			response.getWriter().write("Error deleting category!");
		}
	}
}
