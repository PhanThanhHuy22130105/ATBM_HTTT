package controller.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.CategoryDAO;
import dao.DBConnectionPool;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Product;

@WebServlet("/product")
public class ProductServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String errorMessage = null;
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) { // Lấy connection từ pool
            // Nhận ID sản phẩm từ yêu cầu
            String productId = request.getParameter("id");

            // Kiểm tra ID sản phẩm
            if (productId == null || productId.trim().isEmpty()) {
                errorMessage = "Product ID is required";
                response.sendRedirect(request.getContextPath() + "/Homepage?error=" + 
                    java.net.URLEncoder.encode(errorMessage, "UTF-8"));
                return;
            }

            // Chuyển đổi ID sang số nguyên
            int id;
            try {
                id = Integer.parseInt(productId);
                if (id <= 0) {
                    errorMessage = "Invalid product ID";
                    response.sendRedirect(request.getContextPath() + "/Homepage?error=" + 
                        java.net.URLEncoder.encode(errorMessage, "UTF-8"));
                    return;
                }
            } catch (NumberFormatException e) {
                errorMessage = "Invalid product ID format";
                response.sendRedirect(request.getContextPath() + "/Homepage?error=" + 
                    java.net.URLEncoder.encode(errorMessage, "UTF-8"));
                return;
            }

            // Lấy thông tin sản phẩm từ ProductDAO
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductById(id);

            // Kiểm tra nếu sản phẩm không tồn tại
            if (product == null) {
                errorMessage = "Product not found";
                response.sendRedirect(request.getContextPath() + "/Homepage?error=" + 
                    java.net.URLEncoder.encode(errorMessage, "UTF-8"));
                return;
            }

            // Nếu sản phẩm tồn tại, lấy danh sách sản phẩm liên quan (nếu cần)
            List<Product> productList = productDAO.getAllProducts();
            request.setAttribute("productList", productList);

            // Thiết lập thuộc tính cho request
            request.setAttribute("product", product);

            // Chuyển hướng đến product-detail.jsp
            request.getRequestDispatcher("product-detail.jsp").forward(request, response);
        } catch (SQLException e) {
            errorMessage = "Error connecting to the database: " + e.getMessage();
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/Homepage?error=" + 
                java.net.URLEncoder.encode(errorMessage, "UTF-8"));
        } catch (Exception e) {
            errorMessage = "An unexpected error occurred: " + e.getMessage();
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/Homepage?error=" + 
                java.net.URLEncoder.encode(errorMessage, "UTF-8"));
        }
    }
}