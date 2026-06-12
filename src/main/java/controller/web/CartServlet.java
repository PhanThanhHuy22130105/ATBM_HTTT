package controller.web;

import java.io.IOException;
import java.io.PrintWriter;
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

@WebServlet("/secure/cart")
public class CartServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private static final String CART_PAGE = "/secure/cart.jsp";
    private static final String ERROR_INVALID_INPUT = "Invalid input.";
    private static final String ERROR_PRODUCT_NOT_FOUND = "Product not found.";
    private static final String ERROR_PROCESSING_REQUEST = "Error processing request.";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login");
            return;
        }

        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            System.out.println("Fetching the cart for userId: " + user.getId());
            CartDAO cartDAO = new CartDAO(connection);
            Cart cart = getOrCreateCart(user.getId(), cartDAO);
            session.setAttribute("cart", cart);
            handleCartDisplay(request, response, user.getId(), cartDAO);
        } catch (SQLException e) {
            e.printStackTrace();
            sendErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, ERROR_PROCESSING_REQUEST);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        if (user == null) {
            out.write("{\"success\": false, \"message\": \"Please log in.\"}");
            out.flush();
            return;
        }

        String action = request.getParameter("action");
        try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
            CartDAO cartDAO = new CartDAO(connection);
            Cart cart = getOrCreateCart(user.getId(), cartDAO);

            switch (action) {
                case "addToCart":
                    handleAddToCart(request, response, user.getId(), cartDAO, new ProductDAO(), out);
                    break;
                case "removeItem":
                    removeItem(request, response, user.getId(), cartDAO, out);
                    break;
                case "updateQuantity":
                    updateQuantity(request, response, user.getId(), cartDAO, out);
                    break;
                default:
                    out.write("{\"success\": false, \"message\": \"Invalid action.\"}");
                    out.flush();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            out.write("{\"success\": false, \"message\": \"Database error: " + e.getMessage() + "\"}");
            out.flush();
        } finally {
            out.close();
        }
    }

    private void handleCartDisplay(HttpServletRequest request, HttpServletResponse response, int userId, CartDAO cartDAO) throws ServletException, IOException, SQLException {
        Cart cart = getOrCreateCart(userId, cartDAO);
        System.out.println(cart != null ? "Cart loaded: " + cart.toString() : "Cart is null for userId: " + userId);

        if (cart != null) {
            if (cart.getItems().isEmpty()) {
                System.out.println("Cart is empty for userId: " + userId);
                request.setAttribute("errorMessage", "Your cart is empty.");
            }
            request.getSession().setAttribute("cart", cart);
        } else {
            request.setAttribute("errorMessage", "Failed to load cart.");
        }

        request.getRequestDispatcher(CART_PAGE).forward(request, response);
    }

    private Cart getOrCreateCart(int userId, CartDAO cartDAO) throws SQLException {
        Cart cart = cartDAO.getCartByUserId(userId);
        if (cart == null) {
            System.out.println("Cart not found for userId: " + userId + ". Creating new cart.");
            cart = new Cart(0, userId);
            cartDAO.createCart(cart);
            cart = cartDAO.getCartByUserId(userId);
            if (cart == null || cart.getCartId() <= 0) {
                throw new SQLException("Failed to create cart for userId: " + userId);
            }
        }
        return cart;
    }

    private void handleAddToCart(HttpServletRequest request, HttpServletResponse response, int userId, CartDAO cartDAO, ProductDAO productDAO, PrintWriter out) throws IOException, SQLException {
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");

        System.out.println("Add to Cart invoked - ProductId: " + productIdParam + ", Quantity: " + quantityParam);

        if (isInvalidInput(productIdParam) || isInvalidInput(quantityParam)) {
            out.write("{\"success\": false, \"message\": \"" + ERROR_INVALID_INPUT + "\"}");
            out.flush();
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);

            validateProductQuantity(quantity, out);

            Product product = productDAO.getProductById(productId);
            if (product == null) {
                System.out.println("Product not found for productId: " + productId);
                out.write("{\"success\": false, \"message\": \"" + ERROR_PRODUCT_NOT_FOUND + "\"}");
                out.flush();
                return;
            }

            if (quantity > product.getStock()) {
                out.write("{\"success\": false, \"message\": \"Not enough stock. Only " + product.getStock() + " available.\"}");
                out.flush();
                return;
            }

            Cart cart = getOrCreateCart(userId, cartDAO);
            CartItemDAO cartItemDAO = new CartItemDAO();
            if (cart.getItems().containsKey(productId)) {
                int currentQuantity = cartItemDAO.getQuantity(cart, product);
                int newQuantity = currentQuantity + quantity;
                if (newQuantity > product.getStock()) {
                    out.write("{\"success\": false, \"message\": \"Total quantity exceeds stock. Only " + product.getStock() + " available.\"}");
                    out.flush();
                    return;
                }
                cartItemDAO.setQuantity(cart, product, newQuantity);
            } else {
                cartItemDAO.addCartItem(cart, product, quantity);
            }

            cart = cartDAO.getCartByUserId(userId);
            double subtotal = cart.getTotalPrice();
            double shipping = (subtotal > 0) ? 10.00 : 0.00;
            double total = subtotal + shipping;

            request.getSession().setAttribute("cart", cart);
            out.write("{\"success\": true, \"message\": \"Product added successfully.\", \"subtotal\": " + subtotal + ", \"shipping\": " + shipping + ", \"total\": " + total + "}");
            out.flush();
        } catch (NumberFormatException e) {
            out.write("{\"success\": false, \"message\": \"" + ERROR_INVALID_INPUT + "\"}");
            out.flush();
        }
    }

    private void removeItem(HttpServletRequest request, HttpServletResponse response, int userId, CartDAO cartDAO, PrintWriter out) throws IOException, SQLException {
        String productIdParam = request.getParameter("id");

        if (isInvalidInput(productIdParam)) {
            out.write("{\"success\": false, \"message\": \"Invalid product ID.\"}");
            out.flush();
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);

            Cart cart = getOrCreateCart(userId, cartDAO);
            cartDAO.removeCartItem(cart.getCartId(), productId);
            cart = cartDAO.getCartByUserId(userId);
            double subtotal = cart.getTotalPrice();
            double shipping = (subtotal > 0) ? 10.00 : 0.00;
            double total = subtotal + shipping;

            request.getSession().setAttribute("cart", cart);
            out.write("{\"success\": true, \"message\": \"Product removed successfully.\", \"subtotal\": " + subtotal + ", \"shipping\": " + shipping + ", \"total\": " + total + "}");
            out.flush();
        } catch (NumberFormatException e) {
            out.write("{\"success\": false, \"message\": \"Invalid product ID.\"}");
            out.flush();
        }
    }

    private void updateQuantity(HttpServletRequest request, HttpServletResponse response, int userId, CartDAO cartDAO, PrintWriter out) throws IOException, SQLException {
        String productIdParam = request.getParameter("productId");
        String quantityParam = request.getParameter("quantity");

        System.out.println("Update quantity invoked - ProductId: " + productIdParam + ", Quantity: " + quantityParam);

        if (isInvalidInput(productIdParam) || isInvalidInput(quantityParam)) {
            out.write("{\"success\": false, \"message\": \"Invalid input.\"}");
            out.flush();
            return;
        }

        try {
            int productId = Integer.parseInt(productIdParam);
            int quantity = Integer.parseInt(quantityParam);

            if (quantity < 0) {
                out.write("{\"success\": false, \"message\": \"Quantity cannot be negative.\"}");
                out.flush();
                return;
            }

            Cart cart = getOrCreateCart(userId, cartDAO);
            ProductDAO productDAO = new ProductDAO();
            Product product = productDAO.getProductById(productId);
            if (product == null) {
                out.write("{\"success\": false, \"message\": \"Product not found.\"}");
                out.flush();
                return;
            }

            if (quantity > product.getStock()) {
                out.write("{\"success\": false, \"message\": \"Not enough stock. Only " + product.getStock() + " available.\"}");
                out.flush();
                return;
            }

            CartItemDAO cartItemDAO = new CartItemDAO();
            cartItemDAO.setQuantity(cart, product, quantity);
            cart = cartDAO.getCartByUserId(userId);
            double subtotal = cart.getTotalPrice();
            double shipping = (subtotal > 0) ? 10.00 : 0.00;
            double total = subtotal + shipping;

            request.getSession().setAttribute("cart", cart);
            out.write("{\"success\": true, \"message\": \"Quantity updated successfully.\", \"subtotal\": " + subtotal + ", \"shipping\": " + shipping + ", \"total\": " + total + "}");
            out.flush();
        } catch (NumberFormatException e) {
            out.write("{\"success\": false, \"message\": \"Invalid product ID or quantity.\"}");
            out.flush();
        }
    }

    private boolean isInvalidInput(String param) {
        return param == null || param.trim().isEmpty();
    }

    private void validateProductQuantity(int quantity, PrintWriter out) throws IOException {
        if (quantity <= 0) {
            out.write("{\"success\": false, \"message\": \"Quantity must be greater than 0.\"}");
            out.flush();
        }
    }

    private void sendErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
        response.sendError(status, message);
    }
}