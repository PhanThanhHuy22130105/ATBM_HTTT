package controller.load;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.CartDAO;
import dao.DBConnectionPool;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import models.Cart;
import models.CartItem;

/**
 * Servlet implementation class LoadCartServlet
 */
@WebServlet("/loadCart")
public class LoadCartServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		
		try(Connection connection = DBConnectionPool.getDataSource().getConnection())
		{
			HttpSession session = request.getSession();
			Cart cart =  (Cart) session.getAttribute("cart");
			if(cart!=null) {
				CartDAO cartDAO = new CartDAO(connection);
				PrintWriter out = response.getWriter();
				List<CartItem> list = cartDAO.getCartItems(cart.getCartId());
				
				for(CartItem ci : list) {
					out.println("<div class=\"row cart-item mb-3\">\r\n"
							+ "										<div class=\"col-md-3\">\r\n"
							+ "											<img src=\"${pageContext.request.contextPath}/image/product/"+ ci.getProduct().getPhoto() +"\"\r\n"
							+ "												alt=\""+ ci.getProduct().getName() +"\" class=\"img-fluid rounded\">\r\n"
							+ "										</div>\r\n"
							+ "										<div class=\"col-md-5\">\r\n"
							+ "											<h5 class=\"card-title\">"+ ci.getProduct().getName() +"</h5>\r\n"
							+ "											<p class=\"text-muted\">Category: \"+ ci.getProduct().getName() +\"</p>\r\n"
							+ "										</div>\r\n"
							+ "										<div class=\"col-md-2\">\r\n"
							+ "												<div class=\"input-group\">\r\n"
							+ "													<button class=\"btn btn-outline-secondary btn-sm\" type=\"button\" onclick=\"changeQuantity(-1, this)\">-</button>\r\n"
							+ "													<input style=\"max-width: 100px\" type=\"number\"\r\n"
							+ "														class=\"form-control  form-control-sm text-center quantity-input\"\r\n"
							+ "														onchange=\"updateQuantity()\"\r\n"
							+ "														value=\"${cartItem.quantity}\" min=\"1\"/>\r\n"
							+ "													<button class=\"btn btn-outline-secondary btn-sm\"\r\n"
							+ "														type=\"button onclick=\"changeQuantity(-1, this)>+</button> \r\n"
							+ "												</div>\r\n"
							+ "										</div>\r\n"
							+ "										<div class=\"col-md-2 text-end\">\r\n"
							+ "											<p class=\"fw-bold\">$ "+ ci.getProduct().getPrice() +"</p>\r\n"
							+ "											\r\n"
							+ "											<!-- delete cartItem -->\r\n"
							+ "											<button class=\"btn btn-sm btn-outline-danger\" onclick=\"removeItem()\"> \r\n"
							+ "												<i class=\"bi bi-trash\"></i>\r\n"
							+ "											</button>\r\n"
							+ "										</div>\r\n"
							+ "									</div>\r\n"
							+ "									<hr>");
				}
				
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
