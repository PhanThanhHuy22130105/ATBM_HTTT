<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/template/includes/headerResource.jsp" />
<title>Shopping Cart</title>
<style>
.quantity-input {
	width: 60px;
}

.cart-item {
	transition: all 0.3s ease;
}

.alert {
	margin-top: 10px;
}
</style>
</head>
<body>
	<!-- Navbar -->
	<%@ include file="/template/includes/navbar.jsp"%>

	<div class="container py-5">
		<h1 class="mb-5">Your Shopping Cart</h1>
		<div class="row">
			<div class="col-lg-8">
				<!-- Cart Items -->
				<div class="card mb-4">
					<div class="card-body">
						<c:choose>
							<c:when test="${not empty cart.items}">
								<c:forEach var="cartItem" items="${cart.items.values()}">
									<div id="row-${cartItem.product.id}" class="cart-item mb-3">
										<div class="row align-items-center">
											<div class="col-md-3">
												<img
													src="${pageContext.request.contextPath}/image/product/${cartItem.product.photo}"
													alt="${cartItem.product.name}" class="img-fluid rounded">
											</div>
											<div class="col-md-4">
												<h5 class="card-title">${cartItem.product.name}</h5>
												<p class="text-muted">Category:
													${cartItem.product.category}</p>
											</div>
											<div class="col-md-2">
												<div class="input-group">
													<button class="btn btn-outline-secondary btn-sm"
														type="button"
														onclick="changeQuantity(${cartItem.product.id}, -1)">-</button>
													<input type="number"
														class="form-control form-control-sm text-center quantity-input"
														id="quantity-${cartItem.product.id}"
														onchange="updateQuantity(${cartItem.product.id}, this.value)"
														value="${cartItem.quantity}" min="1"
														max="${cartItem.product.stock}"
														data-price="${cartItem.product.price}"
														data-original-quantity="${cartItem.quantity}">
													<button class="btn btn-outline-secondary btn-sm"
														type="button"
														onclick="changeQuantity(${cartItem.product.id}, 1)">+</button>
												</div>
											</div>
											<div class="col-md-2 text-end">
												<p class="fw-bold">
													$ <span id="item-total-${cartItem.product.id}">${cartItem.product.price * cartItem.quantity}</span>
												</p>
											</div>
											<div class="col-md-1">
												<button class="btn btn-sm btn-outline-danger"
													onclick="removeItem(${cartItem.product.id}, this)">
													<i class="bi bi-trash"></i>
												</button>
											</div>
										</div>
										<hr>
									</div>
								</c:forEach>
							</c:when>
							<c:otherwise>
								<div class="row cart-item mb-3">
									<div class="col-md-12">
										<h5 class="card-title">Your Cart Is Empty</h5>
									</div>
								</div>
							</c:otherwise>
						</c:choose>
					</div>
				</div>
				<!-- Continue Shopping Button -->
				<div class="text-start mb-4">
					<a href="${pageContext.request.contextPath}/Homepage"
						class="btn btn-outline-primary"> <i
						class="bi bi-arrow-left me-2"></i>Continue Shopping
					</a>
				</div>
			</div>

			<!-- Cart Summary -->
			<div class="col-lg-4">
				<div class="card cart-summary">
					<div class="card-body">
						<h5 class="card-title mb-4">Order Summary</h5>
						<form action="${pageContext.request.contextPath}/secure/payment"
							method="POST">
							<!-- Thêm tham số action -->
							<input type="hidden" name="action" value="proceedToPayment">
							<!-- Các trường ẩn khác -->
							<c:forEach var="cartItem" items="${cart.items.values()}">
								<input type="hidden" name="productIds"
									value="${cartItem.product.id}">
								<input type="hidden" name="quantities"
									value="${cartItem.quantity}">
								<input type="hidden" name="prices"
									value="${cartItem.product.price}">
							</c:forEach>
							<input type="hidden" name="subtotal" value="${cart.totalPrice}">
							<input type="hidden" name="shipping"
								value="${cart.totalPrice > 0 ? 10.00 : 0.00}"> <input
								type="hidden" name="total"
								value="${cart.totalPrice + (cart.totalPrice > 0 ? 10.00 : 0.00)}">
							<div class="d-flex justify-content-between mb-3">
								<span>Subtotal</span> <span id="subtotal" class="subtotal"><fmt:formatNumber
										value="${cart.totalPrice}" type="currency" /></span>
							</div>
							<div class="d-flex justify-content-between mb-3">
								<span>Shipping</span> <span id="shipping" class="shipping"><fmt:formatNumber
										value="${cart.totalPrice > 0 ? 10.00 : 0.00}" type="currency" /></span>
							</div>
							<hr>
							<div class="d-flex justify-content-between mb-4">
								<strong>Total</strong> <strong id="total" class="total"><fmt:formatNumber
										value="${cart.totalPrice + (cart.totalPrice > 0 ? 10.00 : 0.00)}"
										type="currency" /></strong>
							</div>
							<c:choose>
								<c:when test="${cart.totalPrice <= 0}">
									<button id="checkoutButton" class="btn btn-warning w-100"
										disabled>Proceed to Checkout</button>
								</c:when>
								<c:otherwise>
									<button id="checkoutButton" class="btn btn-primary w-100">Proceed
										to Checkout</button>
								</c:otherwise>
							</c:choose>
						</form>
						<c:if test="${not empty param.success}">
							<div class="alert alert-success">${param.success}</div>
						</c:if>
						<c:if test="${not empty param.error}">
							<div class="alert alert-danger">${param.error}</div>
						</c:if>
					</div>
				</div>
				<!-- Promo Code -->
				<div class="card mt-4">
					<div class="card-body">
						<h5 class="card-title mb-3">Apply Promo Code</h5>
						<div class="input-group mb-3">
							<input type="text" class="form-control"
								placeholder="Enter promo code">
							<button class="btn btn-outline-secondary" type="button">Apply</button>
						</div>
					</div>
				</div>
			</div>

			<%@ include file="/template/includes/footer.jsp"%>
			<script
				src="${pageContext.request.contextPath}/js/jquery-3.6.0.min.js"></script>
			<script>
		function removeItem(productId, buttonElement) {
			console.log('Remove item clicked for Product ID:', productId);
			$.ajax({
				url: '${pageContext.request.contextPath}/secure/cart',
				type: 'POST',
				data: { action: 'removeItem', id: productId },
				success: function(response) {
					console.log('Remove item response:', response);
					console.log('Response type:', typeof response);
					if (!response) {
						console.error('Empty response from server');
						alert('Phản hồi từ server rỗng.');
						return;
					}
					// Kiểm tra nếu response đã là một đối tượng
					var data = typeof response === 'string' ? JSON.parse(response) : response;
					if (data.success) {
						// Làm nổi bật sản phẩm trước khi xóa
						var $row = $(buttonElement).closest('#row-' + productId);
						$row.css('background-color', '#ffcccc'); // Đổi màu nền thành đỏ nhạt
						// Sử dụng slideUp với hiệu ứng mờ dần tích hợp
						$row.animate({
							height: 0,
							opacity: 0,
							paddingTop: 0,
							paddingBottom: 0,
							marginTop: 0,
							marginBottom: 0
						}, {
							duration: 400,
							easing: 'swing',
							complete: function() {
								$(this).remove();
								updateSummary(data);
								checkCheckoutButton(data.subtotal);
							}
						});
					} else {
						alert('Có lỗi xảy ra: ' + data.message);
					}
				},
				error: function(xhr) {
					console.log('Error removing item:', xhr.responseText);
					alert('Có lỗi xảy ra khi gửi yêu cầu: ' + xhr.responseText);
				}
			});
		}

		function updateQuantity(productId, newQuantity) {
			console.log('Updating quantity for Product ID:', productId, 'New Quantity:', newQuantity);
			newQuantity = parseInt(newQuantity);
			const input = $('#quantity-' + productId);
			const maxQuantity = parseInt(input.attr('max'));
			const price = parseFloat(input.data('price')); // Lấy giá từ data-price
			if (newQuantity < 1) newQuantity = 1;
			if (newQuantity > maxQuantity) newQuantity = maxQuantity;

			$.ajax({
				url: '${pageContext.request.contextPath}/secure/cart',
				type: 'POST',
				data: { action: 'updateQuantity', productId: productId, quantity: newQuantity },
				success: function(response) {
					console.log('Update quantity response:', response);
					console.log('Response type:', typeof response);
					if (!response) {
						console.error('Empty response from server');
						alert('Phản hồi từ server rỗng.');
						return;
					}
					// Kiểm tra nếu response đã là một đối tượng
					var data = typeof response === 'string' ? JSON.parse(response) : response;
					if (data.success) {
						input.val(newQuantity);
						$('#item-total-' + productId).text((newQuantity * price).toFixed(2));
						updateSummary(data);
						checkCheckoutButton(data.subtotal);
					} else {
						alert('Có lỗi xảy ra: ' + data.message);
						input.val(input.data('original-quantity')); // Khôi phục số lượng ban đầu
					}
				},
				error: function(xhr) {
					console.log('Error updating quantity:', xhr.responseText);
					alert('Có lỗi xảy ra khi cập nhật số lượng: ' + xhr.responseText);
					input.val(input.data('original-quantity')); // Khôi phục số lượng ban đầu
				}
			});
		}

		function changeQuantity(productId, delta) {
			const input = $('#quantity-' + productId);
			let inStock = parseInt(input.attr('max'));
			let newQuantity = parseInt(input.val()) + delta;
			newQuantity = Math.max(1, Math.min(newQuantity, inStock));
			input.val(newQuantity);
			updateQuantity(productId, newQuantity);
		}

		function updateSummary(data) {
			$('#subtotal').text('$' + data.subtotal.toFixed(2));
			$('#shipping').text('$' + data.shipping.toFixed(2));
			$('#total').text('$' + data.total.toFixed(2));
		}

		function checkCheckoutButton(subtotal) {
			const checkoutButton = $('#checkoutButton');
			if (subtotal <= 0) {
				checkoutButton.removeClass('btn-primary').addClass('btn-warning').prop('disabled', true).text('Proceed to Checkout');
			} else {
				checkoutButton.removeClass('btn-warning').addClass('btn-primary').prop('disabled', false).text('Proceed to Checkout');
			}
		}

		// Load page and check initial state
		$(document).ready(function() {
			if (typeof jQuery === 'undefined') {
				console.error('jQuery is not loaded!');
			} else {
				console.log('jQuery loaded successfully');
			}
			checkCheckoutButton(${cart.totalPrice});
		});
	</script>
</body>
</html>