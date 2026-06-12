<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<!-- Header -->
<header class="bg-dark py-5 px-5">
    <div class="container px-4 px-lg-5 my-5">
        <div class="text-center text-white">
            <h1 class="display-4 fw-bolder">Boba Station</h1>
            <p class="lead fw-normal text-white-50 mb-0">I never laugh until I have Drink</p>
        </div>
    </div>
</header>

<!-- Section -->
<section class="py-5">
    <div class="container px-4 px-lg-5 mt-5">
        <!-- Hiển thị thông báo lỗi nếu có -->
        <c:if test="${not empty param.error}">
            <div class="alert alert-danger text-center">
                ${param.error}
            </div>
        </c:if>
        
        <c:choose>
            <c:when test="${not empty productList}">
                <div class="row gx-4 gx-lg-5 row-cols-2 row-cols-md-3 row-cols-xl-4 justify-content-center">
                    <c:forEach var="product" items="${productList}">
                        <div class="col mb-5">
                            <div class="card h-100">
                                <a href="${pageContext.request.contextPath}/product?id=${product.id}">
                                    <!-- Product image -->
                                    <img class="card-img-top" 
                                         src="${pageContext.request.contextPath}/uploads/${product.photo}" 
                                         alt="${product.name}" 
                                         onerror="this.src='${pageContext.request.contextPath}/assets/default.jpg';" />
                                </a>
                                <!-- Product details -->
                                <div class="card-body p-4">
                                    <div class="text-center">
                                        <!-- Product name -->
                                        <h5 class="fw-bolder">${product.name}</h5>
                                        <!-- Product price -->
                                        $ ${product.price}
                                    </div>
                                </div>
                                <!-- Product actions -->
                                <div class="card-footer p-4 pt-0 border-top-0 bg-transparent">
                                    <div class="text-center">
                                        <a class="btn btn-outline-dark mt-auto" href="${pageContext.request.contextPath}/product?id=${product.id}">View options</a>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </c:forEach>
                </div>
            </c:when>
            <c:otherwise>
                <div class="col-md-12 text-center">
                    <p class="text-muted">No products available.</p>
                </div>
            </c:otherwise>
        </c:choose>
    </div>
</section>