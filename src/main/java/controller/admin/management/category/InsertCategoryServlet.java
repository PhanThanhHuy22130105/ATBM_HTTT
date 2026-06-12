package controller.admin.management.category;

import dao.CategoryDAO;
import models.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/InsertCategoryServlet")
public class InsertCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Category category = new Category();
        category.setTitle(title);
        category.setDescription(description);

        CategoryDAO categoryDAO = new CategoryDAO();
        boolean isAdded = categoryDAO.addCategory(category);

        if (isAdded) {
            response.sendRedirect("ManageCategoryServlet");
        } else {
            response.getWriter().write("Error adding category!");
        }
    }
}
