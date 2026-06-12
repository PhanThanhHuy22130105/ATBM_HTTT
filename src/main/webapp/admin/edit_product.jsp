<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="vi">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Chỉnh Sửa Sản Phẩm</title>
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.1.3/dist/css/bootstrap.min.css" rel="stylesheet">
    <style>
        .container {
            max-width: 600px;
            margin: 20px auto;
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
        }
        .btn-submit, .btn-back {
            padding: 10px 20px;
            border-radius: 5px;
            text-decoration: none;
            text-align: center;
            transition: background-color 0.3s ease;
        }
        .btn-submit {
            background-color: #4CAF50;
            color: white;
            border: none;
        }
        .btn-submit:hover {
            background-color: #45A049;
        }
        .btn-back {
            background-color: #6c757d;
            color: white;
            margin-right: 10px;
        }
        .btn-back:hover {
            background-color: #5a6268;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>Chỉnh Sửa Sản Phẩm</h1>
        <form action="${pageContext.request.contextPath}/admin/product/edit" method="post" enctype="multipart/form-data">
            <input type="hidden" name="id" value="${product.id}">
            <div class="form-group">
                <label for="name">Tên Sản Phẩm:</label>
                <input type="text" id="name" name="name" class="form-control" value="${product.name}" required>
            </div>
            <div class="form-group">
                <label for="description">Mô Tả:</label>
                <textarea id="description" name="description" class="form-control" required>${product.description}</textarea>
            </div>
            <div class="form-group">
                <label for="price">Giá:</label>
                <input type="number" id="price" name="price" class="form-control" step="0.01" value="${product.price}" required>
            </div>
            <div class="form-group">
                <label for="stock">Số Lượng Tồn Kho:</label>
                <input type="number" id="stock" name="stock" class="form-control" value="${product.stock}" min="0" required>
            </div>
            <div class="form-group">
                <label for="categoryId">Danh Mục:</label>
                <select id="categoryId" name="categoryId" class="form-control" required>
                    <c:forEach var="category" items="${categoryList}">
                        <option value="${category.id}" ${product.category.id == category.id ? 'selected' : ''}>${category.name}</option>
                    </c:forEach>
                </select>
            </div>
            <div class="form-group">
                <label for="photo">Ảnh Sản Phẩm:</label>
                <input type="file" id="photo" name="photo" class="form-control" accept="image/*">
                <c:if test="${not empty product.photo}">
                    <p>Ảnh hiện tại: <img src="${pageContext.request.contextPath}/uploads/${product.photo}" width="100"></p>
                </c:if>
            </div>
            <button type="submit" class="btn-submit">Cập Nhật</button>
            <a href="${pageContext.request.contextPath}/admin/product/manage" class="btn-back">Quay Lại</a>
        </form>
    </div>
</body>
</html>