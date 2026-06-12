<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Thêm Sản Phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/admin.css">
    <style>
        .container {
            max-width: 600px;
            margin: 0 auto;
            padding: 20px;
            background-color: #fff;
            border-radius: 10px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            animation: fadeIn 0.5s ease-in-out;
        }
        h1 {
            color: #333;
            text-align: center;
            margin-bottom: 20px;
        }
        .form-group {
            margin-bottom: 15px;
        }
        label {
            font-weight: bold;
        }
        .form-control {
            width: 100%;
            padding: 8px;
            border: 1px solid #ddd;
            border-radius: 4px;
            transition: border-color 0.3s ease, box-shadow 0.3s ease;
        }
        .form-control:focus {
            border-color: #4CAF50;
            box-shadow: 0 0 5px rgba(76, 175, 80, 0.5);
        }
        .btn-submit, .btn-back {
            display: inline-block;
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            text-align: center;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }
        .btn-submit {
            background-color: #4CAF50;
            color: white;
            border: none;
        }
        .btn-submit:hover {
            background-color: #45A049;
            transform: translateY(-2px);
        }
        .btn-back {
            background-color: #6c757d;
            color: white;
            margin-right: 10px;
        }
        .btn-back:hover {
            background-color: #5a6268;
            transform: translateY(-2px);
        }
        .alert-danger {
            background-color: #f8d7da;
            color: #721c24;
            padding: 10px;
            border-radius: 5px;
            margin-bottom: 20px;
        }
        @keyframes fadeIn {
            from { opacity: 0; }
            to { opacity: 1; }
        }
        @media (max-width: 768px) {
            .container {
                margin: 20px;
                padding: 10px;
            }
            .admin-main {
                margin-left: 0;
            }
        }
    </style>
</head>
<body>
    <!-- Header đồng bộ -->
    <header class="admin-header">
        <div class="logo">Admin</div>
        <div class="header-right">
            <input type="text" class="search-bar" placeholder="Tìm kiếm...">
            <button class="add-btn" onclick="toggleSidebar()">Thêm mới</button>
        </div>
    </header>

    <!-- Sidebar đồng bộ -->
    <nav class="admin-nav">
        <ul>
            <li><a href="${pageContext.request.contextPath}/admin/product/manage"><span class="icon">🛠️</span><span class="text">Quản lý sản phẩm</span></a></li>
            <li><a href="${pageContext.request.contextPath}/manageUsers"><span class="icon">👥</span><span class="text">Quản lý người dùng</span></a></li>
            <li><a href="admin_categories.jsp"><span class="icon">📋</span><span class="text">Quản lý danh mục</span></a></li>
            <li><a href="admin_statistics.jsp"><span class="icon">📈</span><span class="text">Thống kê</span></a></li>
            <li><a href="logout"><span class="icon">🚪</span><span class="text">Đăng xuất</span></a></li>
        </ul>
    </nav>

    <!-- Main content -->
    <main class="admin-main">
        <div class="container">
            <h1>Thêm Sản Phẩm Mới</h1>
            <c:if test="${param.error != null}">
                <div class="alert-danger">${param.error}</div>
            </c:if>
            <form action="${pageContext.request.contextPath}/admin/product/insert" method="post" enctype="multipart/form-data">
                <div class="form-group">
                    <label for="name">Tên Sản Phẩm:</label>
                    <input type="text" id="name" name="name" class="form-control" required>
                </div>
                <div class="form-group">
                    <label for="description">Mô Tả:</label>
                    <textarea id="description" name="description" class="form-control" required></textarea>
                </div>
                <div class="form-group">
                    <label for="photo">Ảnh Sản Phẩm:</label>
                    <input type="file" id="photo" name="photo" class="form-control" accept="image/*" required>
                </div>
                <div class="form-group">
                    <label for="price">Giá:</label>
                    <input type="number" id="price" name="price" class="form-control" step="0.01" required>
                </div>
                <div class="form-group">
                    <label for="stock">Số Lượng Tồn Kho:</label>
                    <input type="number" id="stock" name="stock" class="form-control" value="0" min="0">
                </div>
                <button type="submit" class="btn-submit">Thêm Sản Phẩm</button>
                <a href="${pageContext.request.contextPath}/admin/product/manage" class="btn-back">Quay lại</a>
            </form>
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