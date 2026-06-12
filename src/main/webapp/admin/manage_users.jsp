<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<title>Danh sách người dùng</title>

<!-- Kết nối với Bootstrap 5 -->
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/css/bootstrap.min.css"
	rel="stylesheet" crossorigin="anonymous">
</head>
<body>
	<div class="container mt-5">
		<h2 class="text-center">Danh sách người dùng</h2>
		<a href="${pageContext.request.contextPath}/admin/insert_user.jsp"
			class="btn btn-primary mb-3">Thêm người dùng mới</a>

		<table class="table table-bordered">
			<thead>
				<tr>
					<th>ID</th>
					<th>Tên người dùng</th>
					<th>Email</th>
					<th>Số điện thoại</th>
					<th>Địa chỉ</th>
					<th>Chỉnh sửa</th>
					<th>Xóa</th>
				</tr>
			</thead>
			<tbody>
				<c:forEach var="user" items="${userList}">
					<tr>
						<td>${user.id}</td>
						<td>${user.username}</td>
						<td>${user.email}</td>
						<td>${user.phone}</td>
						<td>${user.address}</td>
						<td><a
							href="${pageContext.request.contextPath}/admin/edit_user.jsp?id=${user.id}"
							class="btn btn-warning">Chỉnh sửa</a></td>
						<td>
							<form action="${pageContext.request.contextPath}/deleteUser" method="post">
								<input type="hidden" name="id" value="${user.id}" />
								<button type="submit" class="btn btn-danger">Xóa</button>
							</form>
						</td>
					</tr>
				</c:forEach>
			</tbody>
		</table>
	</div>

	<!-- Kết nối với JavaScript của Bootstrap -->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0-alpha1/dist/js/bootstrap.bundle.min.js"
		crossorigin="anonymous"></script>
</body>
</html>
