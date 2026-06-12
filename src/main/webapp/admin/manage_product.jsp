<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
<head>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Quản Lý Sản Phẩm</title>
<!-- Bootstrap CSS -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css"
	rel="stylesheet">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/css/admin.css">
<style>
.container {
	max-width: 1200px;
	margin: 0 auto;
	padding: 20px;
	background-color: #fff;
	border-radius: 10px;
	box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
}

h1 {
	color: #333;
	text-align: center;
	margin-bottom: 20px;
}

.btn-back, .btn-add {
	display: inline-block;
	margin-bottom: 20px;
	padding: 10px 20px;
	border-radius: 5px;
	text-decoration: none;
	text-align: center;
	transition: background-color 0.3s ease;
}

.btn-back {
	background-color: #6c757d;
	color: white;
	margin-right: 10px;
}

.btn-back:hover {
	background-color: #5a6268;
}

.btn-add {
	background-color: #4CAF50;
	color: white;
}

.btn-add:hover {
	background-color: #45A049;
}

table {
	width: 100%;
	border-collapse: collapse;
	margin-top: 20px;
}

th, td {
	padding: 12px;
	text-align: left;
	border-bottom: 1px solid #ddd;
}

th {
	background-color: #343a40;
	color: white;
	font-weight: bold;
}

tr:hover {
	background-color: #f1f1f1;
	color: #333;
	transition: background-color 0.3s ease;
}

.action-links a {
	margin-right: 10px;
	padding: 5px 10px;
	text-decoration: none;
	border-radius: 3px;
}

.action-links a.edit {
	background-color: #007bff;
	color: white;
}

.action-links a.delete {
	background-color: #dc3545;
	color: white;
}

.action-links a:hover {
	opacity: 0.9;
}

.product-image {
	max-width: 100px;
	max-height: 100px;
	background-color: #f0f0f0; /* Placeholder màu xám */
	display: block;
}

.product-image[src=""] {
	content: url('${pageContext.request.contextPath}/assets/default.jpg');
}

@media ( max-width : 768px) {
	.container {
		padding: 10px;
	}
	th, td {
		font-size: 14px;
		padding: 8px;
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
			<li><a
				href="${pageContext.request.contextPath}/admin/product/manage"><span
					class="icon">🛠️</span><span class="text">Quản lý sản phẩm</span></a></li>
			<li><a href="${pageContext.request.contextPath}/manageUsers"><span
					class="icon">👥</span><span class="text">Quản lý người dùng</span></a></li>
			<li><a href="admin_categories.jsp"><span class="icon">📋</span><span
					class="text">Quản lý danh mục</span></a></li>
			<li><a href="admin_statistics.jsp"><span class="icon">📈</span><span
					class="text">Thống kê</span></a></li>
			<li><a href="logout"><span class="icon">🚪</span><span
					class="text">Đăng xuất</span></a></li>
		</ul>
	</nav>

	<!-- Main content -->
	<main class="admin-main">
		<div class="container">
			<h1>Quản Lý Sản Phẩm</h1>
			<a href="${pageContext.request.contextPath}/admin/dashboard"
				class="btn-back">Quay lại Dashboard</a> <a
				href="${pageContext.request.contextPath}/admin/product/insert"
				class="btn-add">Thêm Sản Phẩm Mới</a>
			<table>
				<thead>
					<tr>
						<th>Tên Sản Phẩm</th>
						<th>Mô Tả</th>
						<th>Giá</th>
						<th>Danh Mục</th>
						<th>Ảnh</th>
						<th>Số lượng tồn kho</th>
						<th>Thao Tác</th>
					</tr>
				</thead>
				<tbody>
					<c:forEach var="product" items="${productList}">
						<tr>
							<td>${product.name != null ? product.name : 'Chưa có tên'}</td>
							<td>${product.description != null ? product.description : 'Chưa có mô tả'}</td>
							<td>${product.price != null ? product.price : 0.0}</td>
							<td>${product.category != null ? product.category.title : 'Unknown'}</td>
							<td><img
								src="${pageContext.request.contextPath}/image/product/${not empty product.photo ? product.photo : 'no-sample.png'}"
								alt="${product.name}" class="product-image" width="100"
								height="100"
								style="object-fit: cover; transition: opacity 0.3s ease;"
								loading="lazy"
								onerror="this.style.opacity='0.5'; this.src='${pageContext.request.contextPath}/image/product/no-sample.png'; this.style.opacity='1';">
							</td>
							<td>${product.stock != null ? product.stock : 0}</td>
							<td class="action-links"><a
								href="${pageContext.request.contextPath}/admin/product/edit?id=${product.id}"
								class="edit" onclick="return confirm('Bạn muốn chỉnh sửa?');">Chỉnh
									Sửa</a> <a
								href="${pageContext.request.contextPath}/admin/product/delete?id=${product.id}"
								class="delete"
								onclick="return confirm('Bạn có chắc chắn muốn xóa?');">Xóa</a>
							</td>
						</tr>
					</c:forEach>
				</tbody>
			</table>
		</div>
	</main>

	<!-- Bootstrap JS -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
	<script>
        function toggleSidebar() {
            document.querySelector('.admin-nav').classList.toggle('expanded');
            document.querySelector('.admin-main').classList.toggle('expanded');
        }
    </script>
</body>
</html>