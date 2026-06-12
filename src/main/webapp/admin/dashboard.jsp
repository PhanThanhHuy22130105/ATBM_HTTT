<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Admin Dashboard</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
</head>
<body>
    <header class="admin-header">
        <div class="logo">Admin</div>
        <div class="header-right">
            <input type="text" class="search-bar" placeholder="Tìm kiếm...">
            <button class="add-btn" onclick="toggleSidebar()">Menu</button>
        </div>
    </header>
    <nav class="admin-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/product/manage"><span class="icon">🛠️</span><span class="text">Quản lý sản phẩm</span></a></li>
            <li><a href="${pageContext.request.contextPath}/manageUsers"><span class="icon">👥</span><span class="text">Quản lý người dùng</span></a></li>
            <li><a href="admin_categories.jsp"><span class="icon">📋</span><span class="text">Quản lý danh mục</span></a></li>
            <li><a href="admin_statistics.jsp"><span class="icon">📈</span><span class="text">Thống kê</span></a></li>
            <li><a href="logout"><span class="icon">🚪</span><span class="text">Đăng xuất</span></a></li>
        </ul>
    </nav>
    <main class="admin-main">
        <h2 class="page-title">Admin Dashboard</h2>
        <p class="subtitle">"Giữ lửa năng lượng – Đánh tan cơn khát!"</p>
        <div class="stat-section">
            <div class="card">
                <h3>Tổng số sản phẩm</h3>
                <p class="value">${totalProducts}</p>
                <p class="trend">+10% so với tháng trước</p>
            </div>
            <div class="card">
                <h3>Tổng số người dùng</h3>
                <p class="value">${totalUsers}</p>
                <p class="trend">+5% so với tuần trước</p>
            </div>
            <div class="card">
                <h3>Doanh thu hôm nay</h3>
                <p class="value">${totalRevenue}</p>
                <p class="trend">+7% so với hôm qua</p>
            </div>
        </div>
        <button class="view-report-btn">Xem báo cáo chi tiết</button>
        <div class="data-table">
            <h3>Danh sách sản phẩm</h3>
            <table>
                <thead>
                    <tr>
                        <th></th>
                        <th>Tên</th>
                        <th>Trạng thái</th>
                        <th>Ngày cập nhật</th>
                        <th>Tiến độ</th>
                        <th>Hành động</th>
                    </tr>
                </thead>
                <tbody>
                    <tr>
                        <td><img src="https://via.placeholder.com/30" alt="Avatar" class="avatar"></td>
                        <td>Sản phẩm A</td>
                        <td><span class="status status-red">Đang xử lý</span></td>
                        <td>12/03/2025</td>
                        <td>
                            <div class="progress-bar"><div class="progress progress-red" style="width: 70%;"></div></div> 70%
                        </td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-primary">Sửa</button>
                                <button class="btn btn-sm btn-danger">Xóa</button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><img src="https://via.placeholder.com/30" alt="Avatar" class="avatar"></td>
                        <td>Sản phẩm B</td>
                        <td><span class="status status-orange">Tạm hoãn</span></td>
                        <td>12/03/2025</td>
                        <td>
                            <div class="progress-bar"><div class="progress progress-orange" style="width: 50%;"></div></div> 50%
                        </td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-primary">Sửa</button>
                                <button class="btn btn-sm btn-danger">Xóa</button>
                            </div>
                        </td>
                    </tr>
                    <tr>
                        <td><img src="https://via.placeholder.com/30" alt="Avatar" class="avatar"></td>
                        <td>Sản phẩm C</td>
                        <td><span class="status status-green">Hoàn thành</span></td>
                        <td>12/03/2025</td>
                        <td>
                            <div class="progress-bar"><div class="progress progress-green" style="width: 90%;"></div></div> 90%
                        </td>
                        <td>
                            <div class="btn-group">
                                <button class="btn btn-sm btn-primary">Sửa</button>
                                <button class="btn btn-sm btn-danger">Xóa</button>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </main>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
    <script>
        function toggleSidebar() {
            document.querySelector('.admin-nav').classList.toggle('expanded');
            document.querySelector('.admin-main').classList.toggle('expanded');
        }
    </script>
</body>
</html>