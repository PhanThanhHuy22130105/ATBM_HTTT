package controller.web;

import dao.PaymentDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.User;

import java.io.IOException;

@WebServlet("/secure/payment")
public class Payment extends HttpServlet {
    private static final long serialVersionUID = 1L;
    private PaymentDAO paymentDAO;

    @Override
    public void init() throws ServletException {
        paymentDAO = new PaymentDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=Please login to proceed with payment");
            return;
        }

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/secure/cart?error=Your cart is empty");
            return;
        }

        System.out.println("doGet called: Forwarding to payment.jsp");
        request.getRequestDispatcher("/secure/payment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        Cart cart = (Cart) session.getAttribute("cart");

        if (user == null) {
            response.sendRedirect(request.getContextPath() + "/login?error=Please login to proceed with payment");
            return;
        }

        if (cart == null || cart.getItems().isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/secure/cart?error=Your cart is empty");
            return;
        }

        String action = request.getParameter("action");
        System.out.println("doPost called: action = " + action);

        if ("proceedToPayment".equals(action)) {
            System.out.println("Action = proceedToPayment: Forwarding to payment.jsp");
            request.getRequestDispatcher("/secure/payment.jsp").forward(request, response);
        } else if ("pay".equals(action)) {
            System.out.println("Action = pay: Processing payment");
            String[] productIds = request.getParameterValues("productIds");
            String[] quantities = request.getParameterValues("quantities");
            String[] prices = request.getParameterValues("prices");
            double subtotal = Double.parseDouble(request.getParameter("subtotal"));
            double shipping = Double.parseDouble(request.getParameter("shipping"));
            double total = Double.parseDouble(request.getParameter("total"));

            try {
                // Lưu đơn hàng vào cơ sở dữ liệu
                long orderId = paymentDAO.saveOrder(user.getId(), total, productIds, quantities, prices);

                // Xóa giỏ hàng
                System.out.println("Before clearing cart: Cart size = " + cart.getItems().size());
                cart.clearCart();
                session.setAttribute("cart", cart);
                System.out.println("After clearing cart: Cart size = " + cart.getItems().size());
                // Kiểm tra session sau khi cập nhật
                Cart updatedCart = (Cart) session.getAttribute("cart");
                System.out.println("Cart size in session after update: " + updatedCart.getItems().size());

                // Chuyển hướng đến trang xác nhận
                System.out.println("Payment successful: Redirecting to Success.jsp with orderId = " + orderId);
                response.sendRedirect(request.getContextPath() + "/template/static/Success.jsp?orderId=" + orderId);
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/secure/payment?error=Payment failed: " + e.getMessage());
            }
        } else {
            System.out.println("Invalid action: Redirecting to cart.jsp");
            response.sendRedirect(request.getContextPath() + "/secure/cart?error=Invalid request: action=" + action);
        }
    }
}