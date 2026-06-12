package controller.admin;

import dao.ProductDAO;
import dao.UserDAO;
import dao.CategoryDAO;  // Thêm CategoryDAO
import models.Product;
import models.User;
import models.Category;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.util.List;

@WebServlet({"/admin/dashboard", "/admin/users", "/admin/categories", "/admin/statistics"})
public class AdminDashboardServlet extends HttpServlet {
    private UserDAO userDAO;
    private ProductDAO productDAO;
    private CategoryDAO categoryDAO;

    @Override
    public void init() throws ServletException {
        Connection connection = (Connection) getServletContext().getAttribute("DBConnection");
        userDAO = new UserDAO(connection);
        productDAO = new ProductDAO(connection);
        categoryDAO = new CategoryDAO(connection);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Lấy danh sách tất cả người dùng, sản phẩm, danh mục
            List<User> userList = userDAO.getAllUsers();
            List<Product> productList = productDAO.getAllProducts();
            List<Category> categoryList = categoryDAO.getAllCategories();

            // Gán thuộc tính cho JSP
            request.setAttribute("userList", userList);
            request.setAttribute("productList", productList);
            request.setAttribute("categoryList", categoryList);

            // Gán thuộc tính cho tổng số sản phẩm
            request.setAttribute("totalProducts", (productList != null) ? productList.size() : 0);
            request.setAttribute("totalUsers", (userList != null) ? userList.size() : 0);
            request.setAttribute("totalCategories", (categoryList != null) ? categoryList.size() : 0);

            // Tính doanh thu
            double totalRevenue = productDAO.getTotalRevenue();
            request.setAttribute("totalRevenue", totalRevenue);

            // Xử lý các URL khác
            String path = request.getRequestURI();

            if (path.endsWith("/dashboard")) {
                request.setAttribute("products", productList);
                request.getRequestDispatcher("/admin/dashboard.jsp").forward(request, response);
            } else if (path.endsWith("/users")) {
                request.getRequestDispatcher("/admin/admin_users.jsp").forward(request, response);
            } else if (path.endsWith("/categories")) {
                request.getRequestDispatcher("/admin/admin_categories.jsp").forward(request, response);
            } else if (path.endsWith("/statistics")) {
                request.getRequestDispatcher("/admin/admin_statistics.jsp").forward(request, response);
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xử lý yêu cầu.");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("deleteUser".equals(action)) {
                String userIdStr = request.getParameter("userId");

                if (userIdStr == null || userIdStr.isEmpty()) {
                    request.setAttribute("error", "ID người dùng không hợp lệ.");
                    doGet(request, response);
                    return;
                }

                int userId = Integer.parseInt(userIdStr);
                if (userDAO.deleteUser(userId)) {
                    request.setAttribute("message", "Xóa người dùng thành công.");
                } else {
                    request.setAttribute("error", "Không thể xóa người dùng.");
                }

            } else if ("deleteProduct".equals(action)) {
                String productIdStr = request.getParameter("productId");

                if (productIdStr == null || productIdStr.isEmpty()) {
                    request.setAttribute("error", "ID sản phẩm không hợp lệ.");
                    doGet(request, response);
                    return;
                }

                int productId = Integer.parseInt(productIdStr);
                productDAO.deleteProduct(productId);
                request.setAttribute("message", "Xóa sản phẩm thành công.");
            }

            // Tải lại dữ liệu
            doGet(request, response);

        } catch (NumberFormatException e) {
            request.setAttribute("error", "ID không hợp lệ.");
            doGet(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Đã xảy ra lỗi khi xử lý yêu cầu.");
        }
    }
}