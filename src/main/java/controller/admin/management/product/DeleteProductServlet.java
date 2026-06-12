package controller.admin.management.product;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/admin/product/delete")
public class DeleteProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int productId = Integer.parseInt(request.getParameter("id"));
            ProductDAO productDAO = new ProductDAO();

            if (productDAO.getProductById(productId) != null) {
                productDAO.deleteProduct(productId);
                response.sendRedirect("/admin/product/manage?success=Product deleted successfully");
            } else {
                response.sendRedirect("/admin/product/manage?error=Product not found");
            }
        } catch (NumberFormatException e) {
            response.sendRedirect("/admin/product/manage?error=Invalid Product ID");
        } catch (Exception e) {
            response.sendRedirect("/admin/product/manage?error=An error occurred");
        }
    }
}