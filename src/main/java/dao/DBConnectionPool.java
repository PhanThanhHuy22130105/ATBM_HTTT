package dao;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import models.Product;
import java.util.List;

public class DBConnectionPool {
    private static HikariDataSource dataSource;

    static {
        try {
            // Cấu hình HikariCP
            HikariConfig config = new HikariConfig();
            config.setJdbcUrl("jdbc:mysql://localhost:3306/database");  // Đảm bảo URL đúng
            config.setDriverClassName("com.mysql.cj.jdbc.Driver");     // Đảm bảo class name chính xác
            config.setUsername("root");
            config.setPassword("");
            // Không cần thiết lập setDriverClassName, HikariCP sẽ tự động chọn đúng driver
            config.setMaximumPoolSize(20); // Số lượng kết nối tối đa
            config.setMinimumIdle(5); // Số lượng kết nối tối thiểu
            config.setIdleTimeout(30000); // Thời gian idle tối đa (ms)
            config.setMaxLifetime(1800000); // Thời gian tồn tại tối đa của một connection (ms)
            config.setConnectionTimeout(20000); // Thời gian chờ để lấy một connection (ms)

            // Tạo DataSource
            dataSource = new HikariDataSource(config);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error initializing HikariCP connection pool", e);
        }
    }

    // Phương thức để lấy DataSource
    public static DataSource getDataSource() {
        return dataSource;
    }

    // Optional: Đóng Connection Pool khi không sử dụng nữa
    public static void shutdown() {
        if (dataSource != null) {
            dataSource.close();
        }
    }

    // Lấy kết nối từ DataSource
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    public static void main(String[] args) {
        try (Connection connection = getConnection()) {
            if (connection != null) {
                System.out.println("Kết nối cơ sở dữ liệu thành công!");
            } else {
                System.out.println("Kết nối cơ sở dữ liệu thất bại!");
            }

            // Tạo ProductDAO để lấy sản phẩm
            ProductDAO dao = new ProductDAO();
            List<Product> list = dao.getAllProducts();

            // In ra thông tin sản phẩm
            for (Product p : list) {
                System.out.println(p.toString());
            }

        } catch (SQLException e) {
            System.out.println("Lỗi khi kết nối cơ sở dữ liệu: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
