<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <jsp:include page="/template/includes/headerResource.jsp" />
    <title>Trang Chủ</title>
</head>
<body>
    <!-- Navbar -->
    <%@ include file="template/includes/navbar.jsp"%>

    <!-- Product section -->
    <section class="py-5 bg-light">
        <div class="container px-4 px-lg-5 mt-5">
            <h2 class="fw-bolder mb-4">Danh Sách Sản Phẩm</h2>
            <c:choose>
                <c:when test="${not empty productList }">
                    <div id="content"
                         class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
                        <c:forEach var="product" items="${productList}" varStatus="status">
                            <div class="col mb-5 product-count">
                                <div class="card h-100">
                                    <a href="./product?id=${product.id}">
                                        <img class="card-img-top bg-dark"
                                             src="${pageContext.request.contextPath}/image/product/${not empty product.photo ? product.photo : 'no-sample.png'}"
                                             alt="${product.name}"
                                             loading="lazy"
                                             onerror="this.src='${pageContext.request.contextPath}/image/product/no-sample.png'; this.onerror=null;" />
                                    </a>
                                    <!-- Product details-->
                                    <div class="card-body p-4">
                                        <div class="text-center">
                                            <!-- Product name-->
                                            <h5 class="fw-bolder">${product.name != null ? product.name : 'Chưa có tên'}</h5>
                                            <!-- Product reviews-->
                                            <div class="d-flex justify-content-center small text-warning mb-2">
                                                <div class="bi-star-fill">*</div>
                                                <div class="bi-star-fill">*</div>
                                                <div class="bi-star-fill">*</div>
                                                <div class="bi-star-fill">*</div>
                                                <div class="bi-star-fill">*</div>
                                            </div>
                                            <!-- Product price-->
                                            $ ${product.price != null ? product.price : 0.0}
                                        </div>
                                    </div>
                                    <!-- Product actions-->
                                    <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                                        <div class="text-center">
                                            <a class="btn btn-outline-dark mt-auto" href="#">View options</a>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </c:forEach>
                    </div>
                    <div class="text-center">
                        <button id="show-more" onclick="loadMore()"
                                class="btn btn-outline-dark mt-4">Show More</button>
                    </div>
                </c:when>
                <c:otherwise>
                    <div class="col-md-12">
                        <p>No products available.</p>
                    </div>
                </c:otherwise>
            </c:choose>
        </div>
    </section>

    <%@ include file="template/includes/footer.jsp"%>
    <script src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
    <script>
    function loadMore() {
        var amount = document.getElementsByClassName("product-count").length;
        $.ajax({
            url: "/zzzz/load",
            type: "GET",
            data: {
                exists: amount
            },
            success: function(data) {
                var row = document.getElementById("content");
                if (data && data.trim() !== "") {
                    row.innerHTML += data;
                } else {
                    $('#show-more').prop('disabled', true).text('No more products');
                }
            },
            error: function(xhr) {
                console.error("Error loading more products:", xhr);
                alert("Failed to load more products. Please try again.");
            }
        });
    }
    </script>
</body>
</html>