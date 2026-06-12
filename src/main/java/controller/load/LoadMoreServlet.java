package controller.load;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import dao.ProductDAO;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import models.Product;

/**
 * Servlet implementation class LoadMoreServlet
 */
@WebServlet("/load")
public class LoadMoreServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String str = request.getParameter("exists");
        int amount = Integer.parseInt(str);
        ProductDAO dao = new ProductDAO();
        List<Product> products = dao.getNext4(amount); // Assuming this method retrieves the top 4 products
        PrintWriter out = response.getWriter();
        String contextPath = request.getContextPath();
        
        for (Product o : products) {
            out.println("<div class=\"product-count col mb-5\">\r\n"
                    + "    <div class=\"card h-100\">\r\n"
                    + "        <a href=\"./product?id=" + o.getId() + "\"> <!-- Product image-->\r\n"
                    + "            <img class=\"card-img-top bg-dark\"\r\n"
                    + "                 src=\"" + contextPath + "/image/product/" + o.getPhoto() + "\" />\r\n"
                    + "        </a>\r\n"
                    + "        <!-- Product details-->\r\n"
                    + "        <div class=\"card-body p-4\">\r\n"
                    + "            <div class=\"text-center\">\r\n"
                    + "                <!-- Product name-->\r\n"
                    + "                <h5 class=\"fw-bolder\">" + escapeHtml(o.getName()) + "</h5>\r\n"
                    + "                <!-- Product reviews-->\r\n"
                    + "                <div class=\"d-flex justify-content-center small text-warning mb-2\">\r\n"
                    + "                    <div class=\"bi-star-fill\">*</div>\r\n"
                    + "                    <div class=\"bi-star-fill\">*</div>\r\n"
                    + "                    <div class=\"bi-star-fill\">*</div>\r\n"
                    + "                    <div class=\"bi-star-fill\">*</div>\r\n"
                    + "                    <div class=\"bi-star-fill\">*</div>\r\n"
                    + "                </div>\r\n"
                    + "                <!-- Product price-->\r\n"
                    + "                $ " + o.getPrice() + "\r\n"
                    + "            </div>\r\n"
                    + "        </div>\r\n"
                    + "        <!-- Product actions-->\r\n"
                    + "        <div class=\"card-footer p-4 pt-0 border-top-0 bg-transparent\">\r\n"
                    + "            <div class=\"text-center\">\r\n"
                    + "                <a class=\"btn btn-outline-dark mt-auto\" href=\"#\">View options</a>\r\n"
                    + "            </div>\r\n"
                    + "        </div>\r\n"
                    + "    </div>\r\n"
                    + "</div>");
        }

        out.close(); // Close the PrintWriter
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    // Utility method to escape HTML characters
    private String escapeHtml(String input) {
        if (input == null) {
            return null;
        }
        return input.replace("&", "&amp;")
                    .replace("<", "&lt;")
                    .replace(">", "&gt;")
                    .replace("\"", "&quot;")
                    .replace("'", "&#39;");
    }
}