<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<%@ include file="/template/includes/headerResource.jsp"%>

<title>Payment</title>
</head>
<body>
	<!-- Navbar -->
	<%@ include file="/template/includes/navbar.jsp"%>

	<c:if test="${empty sessionScope.user}">
		<div class="container py-5">
			<div class="alert alert-danger">Please login to proceed with
				payment.</div>
			<a href="${pageContext.request.contextPath}/login"
				class="btn btn-primary">Login</a>
		</div>
	</c:if>

	<c:if test="${empty sessionScope.cart.items}">
		<div class="container py-5">
			<div class="alert alert-danger">Your cart is empty.</div>
			<a href="${pageContext.request.contextPath}/Homepage"
				class="btn btn-primary">Continue Shopping</a>
		</div>
	</c:if>

	<c:if
		test="${not empty sessionScope.user and not empty sessionScope.cart.items}">
		<section style="background-color: #eee;">
			<div class="container py-5">
				<div class="row d-flex justify-content-center">
					<div class="col-md-8 col-lg-6 col-xl-4">
						<div class="card rounded-3">
							<div class="card-body mx-1 my-2">
								<div class="d-flex align-items-center">
									<div>
										<i class="fab fa-cc-visa fa-4x text-body pe-3"></i>
									</div>
									<div>
										<p class="d-flex flex-column mb-0">
											<b>${sessionScope.user.username}</b><span
												class="small text-muted"> **** 8880 </span>
										</p>
									</div>
								</div>

								<div class="pt-3">
									<div class="d-flex flex-row pb-3">
										<div
											class="rounded border border-primary border-2 d-flex w-100 p-3 align-items-center"
											style="background-color: rgba(18, 101, 241, 0.07);">
											<div class="d-flex align-items-center pe-3">
												<input class="form-check-input" type="radio"
													name="radioNoLabelX" id="radioNoLabel11" value=""
													aria-label="..." checked />
											</div>
											<div class="d-flex flex-column">
												<p class="mb-1 small text-primary">Total amount due</p>
												<h6 class="mb-0 text-primary">
													<fmt:formatNumber
														value="${sessionScope.cart.totalPrice + (sessionScope.cart.totalPrice > 0 ? 10.00 : 0.00)}"
														type="currency" />
												</h6>
											</div>
										</div>
									</div>
								</div>

								<div
									class="d-flex justify-content-between align-items-center pb-1">
									<a href="${pageContext.request.contextPath}/secure/cart"
										class="text-muted">Go back</a>
									<form
										action="${pageContext.request.contextPath}/secure/payment"
										method="post">
										<!-- Thêm tham số action -->
										<input type="hidden" name="action" value="pay">
										<!-- Các trường ẩn khác -->
										<c:forEach var="cartItem"
											items="${sessionScope.cart.items.values()}">
											<input type="hidden" name="productIds"
												value="${cartItem.product.id}">
											<input type="hidden" name="quantities"
												value="${cartItem.quantity}">
											<input type="hidden" name="prices"
												value="${cartItem.product.price}">
										</c:forEach>
										<input type="hidden" name="subtotal"
											value="${sessionScope.cart.totalPrice}"> <input
											type="hidden" name="shipping"
											value="${sessionScope.cart.totalPrice > 0 ? 10.00 : 0.00}">
										<input type="hidden" name="total"
											value="${sessionScope.cart.totalPrice + (sessionScope.cart.totalPrice > 0 ? 10.00 : 0.00)}">
										<button type="submit" class="btn btn-primary btn-lg">Pay
											amount</button>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</c:if>

	<%@ include file="/template/includes/footer.jsp"%>
</body>
</html>