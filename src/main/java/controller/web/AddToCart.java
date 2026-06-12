package controller.web;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import dao.CartDAO;
import dao.CartItemDAO;
import dao.DBConnectionPool;
import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.Product;
import models.User;

@WebServlet("/addToCart")
public class AddToCart extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doPost(req, res);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        int productId = -1;

        if (user == null) {
            res.sendRedirect(req.getContextPath() + "/login?error=" + java.net.URLEncoder.encode("Vui lòng đăng nhập", "UTF-8"));
            return;
        }

        try {
            productId = Integer.parseInt(req.getParameter("productId"));
            int quantity = req.getParameter("inputQuantity") != null ? Integer.parseInt(req.getParameter("inputQuantity")) : 1;

            if (quantity <= 0) {
                throw new IllegalArgumentException("Số lượng phải lớn hơn 0");
            }

            System.out.println("Adding product to cart: ProductId=" + productId + ", Quantity=" + quantity + ", UserId=" + user.getId());

            try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
                ProductDAO productDAO = new ProductDAO();
                Product product = productDAO.getProductById(productId);
                if (product == null) {
                    throw new IllegalArgumentException("Sản phẩm không tồn tại");
                }

                if (quantity > product.getStock()) {
                    throw new IllegalArgumentException("Không đủ hàng trong kho. Chỉ còn " + product.getStock() + " sản phẩm.");
                }

                CartDAO cartDAO = new CartDAO(connection);
                Cart cart = cartDAO.getCartByUserId(user.getId());
                if (cart == null) {
                    System.out.println("Cart not found for userId: " + user.getId() + ". Creating new cart.");
                    cart = new Cart(0, user.getId());
                    cartDAO.createCart(cart);
                    cart = cartDAO.getCartByUserId(user.getId());
                    if (cart == null || cart.getCartId() <= 0) {
                        throw new SQLException("Failed to create cart for userId: " + user.getId());
                    }
                }

                CartItemDAO cartItemDAO = new CartItemDAO();
                if (cart.getItems().containsKey(productId)) {
                    int currentQuantity = cartItemDAO.getQuantity(cart, product);
                    int newQuantity = currentQuantity + quantity;
                    if (newQuantity > product.getStock()) {
                        throw new IllegalArgumentException("Tổng số lượng vượt quá tồn kho. Chỉ còn " + product.getStock() + " sản phẩm.");
                    }
                    cartItemDAO.setQuantity(cart, product, newQuantity);
                } else {
                    cartItemDAO.addCartItem(cart, product, quantity);
                }

                cart = cartDAO.getCartByUserId(user.getId());
                session.setAttribute("cart", cart);
                res.sendRedirect(req.getContextPath() + "/secure/cart?success=" +
                    java.net.URLEncoder.encode("Thêm vào giỏ hàng thành công", "UTF-8"));
            }
        } catch (NumberFormatException e) {
            res.sendRedirect(req.getContextPath() + "/product-detail?id=" + productId + "&error=" +
                java.net.URLEncoder.encode("Dữ liệu không hợp lệ: " + e.getMessage(), "UTF-8"));
        } catch (IllegalArgumentException e) {
            res.sendRedirect(req.getContextPath() + "/product-detail?id=" + productId + "&error=" +
                java.net.URLEncoder.encode(e.getMessage(), "UTF-8"));
        } catch (SQLException e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/product-detail?id=" + productId + "&error=" +
                java.net.URLEncoder.encode("Lỗi cơ sở dữ liệu: " + e.getMessage(), "UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
            res.sendRedirect(req.getContextPath() + "/product-detail?id=" + productId + "&error=" +
                java.net.URLEncoder.encode("Lỗi không xác định: " + e.getMessage(), "UTF-8"));
        }
    }
}