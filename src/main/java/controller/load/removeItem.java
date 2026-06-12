package controller.load;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;

import dao.CartDAO;
import dao.DBConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;

/**
 * Servlet implementation class removeItem
 */
@WebServlet("/removeItem")
public class removeItem extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("application/json;charset=UTF-8");
		PrintWriter out = response.getWriter();

		HttpSession session = request.getSession();
		Cart cart = (Cart) session.getAttribute("cart");
		String productId = request.getParameter("id");
		int pId = Integer.parseInt(productId);
		boolean success = false;
		String message = "";

		if (cart != null && productId != null) {
			try (Connection connection = DBConnectionPool.getDataSource().getConnection()) {
				CartDAO cartDAO = new CartDAO(connection);

				// Xóa sản phẩm khỏi giỏ hàng
				success = cartDAO.removeCartItem(cart.getCartId(), pId);
				cart.removeItem(pId);

				if (success) {
					message = "Sản phẩm đã được xóa khỏi giỏ hàng.";
					
					// Tính toán lại tổng giá trị
					double subtotal1 = cart.getTotalPrice();
					double shipping1 = 10.00; // Giá ship cố định
					double total1 = subtotal1 + shipping1;

					// Trả về JSON response
					out.print("{\"success\": true, \"message\": \"" + message + "\", \"subtotal\": " 
							+ subtotal1 + ", \"shipping\": " + shipping1 + ", \"total\": " + total1 + "}");

				} else {
					message = "Không thể xóa sản phẩm. Vui lòng thử lại.";
					out.print("{\"success\": false, \"message\": \"" + message + "\"}");
				}
			} catch (SQLException e) {
				e.printStackTrace();
				message = "Có lỗi xảy ra khi kết nối đến cơ sở dữ liệu.";
				out.print("{\"success\": false, \"message\": \"" + message + "\"}");
			}
		} else {
			message = "Giỏ hàng không tồn tại hoặc ID sản phẩm không hợp lệ.";
			out.print("{\"success\": false, \"message\": \"" + message + "\"}");
		}
		out.flush();

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
