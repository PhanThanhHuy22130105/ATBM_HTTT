<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Sửa danh mục</title>
    <!-- Thêm link đến Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body>
    <div class="container mt-5">
        <h1 class="text-center">Sửa danh mục</h1>

        <form action="EditCategoryServlet" method="post">
            <input type="hidden" name="id" value="${category.id}" />
            <div class="mb-3">
                <label for="title" class="form-label">Tên danh mục</label>
                <input type="text" class="form-control" name="title" id="title" value="${category.title}" required />
            </div>
            <div class="mb-3">
                <label for="description" class="form-label">Mô tả</label>
                <textarea class="form-control" name="description" id="description" required>${category.description}</textarea>
            </div>
            <button type="submit" class="btn btn-primary">Cập nhật</button>
        </form>
        <a href="ManageCategoryServlet" class="btn btn-secondary mt-3">Quay lại</a>
    </div>

    <!-- Thêm link đến Bootstrap JS -->
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
