package controller.admin.management.category;

import dao.CategoryDAO;
import models.Category;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/EditCategoryServlet")
public class EditCategoryServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int categoryId = Integer.parseInt(request.getParameter("id"));
        CategoryDAO categoryDAO = new CategoryDAO();
        Category category = categoryDAO.getCategoryById(categoryId);
        request.setAttribute("category", category);
        request.getRequestDispatcher("/admin/edit_category.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        String title = request.getParameter("title");
        String description = request.getParameter("description");

        Category category = new Category();
        category.setId(id);
        category.setTitle(title);
        category.setDescription(description);

        CategoryDAO categoryDAO = new CategoryDAO();
        boolean isUpdated = categoryDAO.updateCategory(category);

        if (isUpdated) {
            response.sendRedirect("ManageCategoryServlet");
        } else {
            response.getWriter().write("Error updating category!");
        }
    }
}
