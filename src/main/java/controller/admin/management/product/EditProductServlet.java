
package controller.admin.management.product;

import dao.CategoryDAO;
import dao.ProductDAO;
import models.Category;
import models.Product;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/admin/product/edit")
public class EditProductServlet extends HttpServlet {
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int productId = Integer.parseInt(request.getParameter("id"));
			ProductDAO productDAO = new ProductDAO();
			Product product = productDAO.getProductById(productId);

			if (product == null) {
				response.sendRedirect("/admin/product/manage?error=Product not found");
				return;
			}

			List<Category> categories = new CategoryDAO().getAllCategories();
			request.setAttribute("product", product);
			request.setAttribute("categories", categories);
			request.getRequestDispatcher("/admin/edit_product.jsp").forward(request, response);
		} catch (NumberFormatException e) {
			response.sendRedirect("/admin/product/manage?error=Invalid Product ID");
		} catch (Exception e) {
			response.sendRedirect("/admin/product/manage?error=An error occurred");
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		try {
			int productId = Integer.parseInt(request.getParameter("id"));
			String name = request.getParameter("name");
			String description = request.getParameter("description");
			String photo = request.getParameter("photo");
			double price = Double.parseDouble(request.getParameter("price"));
			int categoryId = Integer.parseInt(request.getParameter("category_id"));

			Category category = new CategoryDAO().getCategoryById(categoryId);
			if (category == null) {
				response.sendRedirect("/admin/product/manage?error=Category not found");
				return;
			}

			Product product = new Product();
			product.setId(productId);
			product.setName(name);
			product.setDescription(description);
			product.setPhoto(photo);
			product.setPrice(price);
			product.setCategory(category);

			ProductDAO productDAO = new ProductDAO();
			productDAO.updateProduct(product);

			response.sendRedirect("/admin/product/manage?success=Product updated successfully");
		} catch (NumberFormatException e) {
			response.sendRedirect("/admin/product/manage?error=Invalid input format");
		} catch (Exception e) {
			response.sendRedirect("/admin/product/manage?error=An error occurred");
		}
	}
}