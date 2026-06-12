<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/template/includes/headerResource.jsp" />
<title>${requestScope.product}</title>
</head>
<body>
    <!-- Navbar -->
    <%@ include file="template/includes/navbar.jsp"%>

    <c:set var="singleProduct" value="${requestScope.product}" />
    <!-- Product section-->
    <section class="py-5">
        <div class="container px-4 px-lg-5 my-5">
            <div class="row gx-4 gx-lg-5 align-items-center">
                <div class="col-md-6">
                    <img class="card-img-top mb-5 mb-md-0"
                         src="${pageContext.request.contextPath}/image/product/${not empty product.photo ? product.photo : 'no-sample.png'}"
                         alt="${product.name }"
                         onerror="this.src='${pageContext.request.contextPath}/image/product/no-sample.png'; this.onerror=null;" />
                </div>
                <div class="col-md-6">
                    <div class="small mb-1">
                        <i>PRODUCT_ID :</i> ${product.id }
                    </div>
                    <h1 class="display-5 fw-bolder">${product.name }</h1>
                    <div class="fs-5 mb-5">
                        <span>$ ${product.price }</span>
                    </div>
                    <p class="lead">${not empty product.description ? product.description : 'Chưa có mô tả'}</p>
                    <form action="addToCart" method="POST">
                        <div class="d-flex">
                            <input class="form-control text-center me-3" id="inputQuantity" name="inputQuantity"
                                   type="number" value="1" min="1" style="max-width: 3rem"
                                   oninput="this.value = Math.max(1, this.value);" />
                            <input name="productId" type="hidden" value="${product.id }" />
                            <button class="btn btn-outline-dark flex-shrink-0" type="submit">
                                <i class="bi-cart-fill me-1"></i> Add to cart
                            </button>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </section>

    <section class="py-5 bg-light">
        <div class="container px-4 px-lg-5 mt-5">
            <h2 class="fw-bolder mb-4">Other Item</h2>
            <c:choose>
                <c:when test="${not empty productList }">
                    <div id="content"
                         class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
                        <c:forEach var="product" items="${productList}" varStatus="status">
                            <c:if test="${status.index < 4}">
                                <!-- Show only the first 4 products -->
                                <div class="col mb-5 product-count">
                                    <div class="card h-100">
                                        <a href="./product?id=${product.id}">
                                            <img class="card-img-top bg-dark"
                                                 src="${pageContext.request.contextPath}/image/product/${not empty product.photo ? product.photo : 'no-sample.png'}"
                                                 alt="${product.name}"
                                                 onerror="this.src='${pageContext.request.contextPath}/image/product/no-sample.png'; this.onerror=null;" />
                                        </a>
                                        <!-- Product details-->
                                        <div class="card-body p-4">
                                            <div class="text-center">
                                                <!-- Product name-->
                                                <h5 class="fw-bolder">${product.name }</h5>
                                                <!-- Product reviews-->
                                                <div class="d-flex justify-content-center small text-warning mb-2">
                                                    <div class="bi-star-fill">*</div>
                                                    <div class="bi-star-fill">*</div>
                                                    <div class="bi-star-fill">*</div>
                                                    <div class="bi-star-fill">*</div>
                                                    <div class="bi-star-fill">*</div>
                                                </div>
                                                <!-- Product price-->
                                                $ ${product.price}
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
                            </c:if>
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