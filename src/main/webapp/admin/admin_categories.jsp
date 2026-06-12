<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Danh sách danh mục</title>
    <!-- Thêm link đến Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center">Danh sách danh mục</h1>

        <!-- Form để thêm danh mục mới -->
        <form action="InsertCategoryServlet" method="post" class="mb-4">
            <div class="mb-3">
                <label for="title" class="form-label">Tên danh mục</label>
                <input type="text" class="form-control" name="title" id="title" required />
            </div>
            <div class="mb-3">
                <label for="description" class="form-label">Mô tả</label>
                <textarea class="form-control" name="description" id="description" required></textarea>
            </div>
            <button type="submit" class="btn btn-primary">Thêm danh mục</button>
        </form>

        <!-- Bảng danh mục -->
        <table class="table table-bordered">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Tên danh mục</th>
                    <th>Mô tả</th>
                    <th>Hành động</th>
                </tr>
            </thead>
            <tbody>
                <c:forEach var="category" items="${categoryList}">
                    <tr>
                        <td>${category.id}</td>
                        <td>${category.title}</td>
                        <td>${category.description}</td>
                        <td>
                            <a href="EditCategoryServlet?id=${category.id}" class="btn btn-warning btn-sm">Sửa</a> |
                            <a href="DeleteCategoryServlet?id=${category.id}" onclick="return confirm('Bạn chắc chắn muốn xóa?')" class="btn btn-danger btn-sm">Xóa</a>
                        </td>
                    </tr>
                </c:forEach>
            </tbody>
        </table>
    </div>

    <!-- Thêm link đến Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
