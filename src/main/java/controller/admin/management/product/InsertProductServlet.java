package controller.admin.management.product;

import dao.ProductDAO;
import models.Product;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.MultipartConfig;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Part;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

@WebServlet("/admin/product/insert")
@MultipartConfig(
    fileSizeThreshold = 1024 * 1024 * 2, // 2MB
    maxFileSize = 1024 * 1024 * 10,      // 10MB
    maxRequestSize = 1024 * 1024 * 50    // 50MB
)
public class InsertProductServlet extends HttpServlet {
    private ProductDAO productDAO;

    @Override
    public void init() throws ServletException {
        productDAO = new ProductDAO();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.setAttribute("categoryList", productDAO.getAllCategories());
        request.getRequestDispatcher("/admin/insert_product.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            request.setCharacterEncoding("UTF-8");
            String name = request.getParameter("name");
            String description = request.getParameter("description");
            double price = Double.parseDouble(request.getParameter("price"));
            int stock = request.getParameter("stock") != null ? Integer.parseInt(request.getParameter("stock")) : 0;

            // Lấy thông tin file ảnh
            Part filePart = request.getPart("photo");
            String fileName = extractFileName(filePart);
            String imagePath = getServletContext().getRealPath("") + File.separator + "image" + File.separator + "product";

            // Tạo thư mục nếu chưa tồn tại
            File imageDir = new File(imagePath);
            if (!imageDir.exists()) {
                if (!imageDir.mkdirs()) {
                    throw new IOException("Failed to create image/product directory");
                }
            }

            // Tạo tên file duy nhất với timestamp
            String finalFileName = fileName;
            if (filePart != null && filePart.getSize() > 0) {
                String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                String extension = fileName.substring(fileName.lastIndexOf("."));
                finalFileName = timestamp + "_" + fileName.replace(extension, "") + extension;
                String filePath = imagePath + File.separator + finalFileName;
                filePart.write(filePath);
                System.out.println("File uploaded to: " + filePath);
            } else {
                finalFileName = "no-sample.png"; // Sử dụng ảnh mặc định từ CSDL
                File defaultFile = new File(imagePath + File.separator + "no-sample.png");
                if (!defaultFile.exists()) {
                    throw new IOException("Default image no-sample.png not found in image/product directory");
                }
                System.out.println("No file uploaded, using no-sample.png");
            }

            // Lưu thông tin sản phẩm
            Product product = new Product();
            product.setName(name);
            product.setDescription(description);
            product.setPhoto(finalFileName);
            product.setPrice(price);
            product.setStock(stock);

            productDAO.addProduct(product);
            response.sendRedirect(request.getContextPath() + "/admin/product/manage?success=" +
                java.net.URLEncoder.encode("Product added successfully", "UTF-8"));
        } catch (NumberFormatException e) {
            System.out.println("NumberFormatException: " + e.getMessage());
            response.sendRedirect(request.getContextPath() + "/admin/product/insert?error=" +
                java.net.URLEncoder.encode("Invalid input format", "UTF-8"));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/admin/product/insert?error=" +
                java.net.URLEncoder.encode("An error occurred: " + e.getMessage(), "UTF-8"));
        }
    }

    private String extractFileName(Part part) {
        String contentDisp = part.getHeader("content-disposition");
        if (contentDisp != null) {
            for (String content : contentDisp.split(";")) {
                if (content.trim().startsWith("filename")) {
                    return content.substring(content.indexOf("=") + 2, content.length() - 1);
                }
            }
        }
        return "no-sample.png";
    }
}