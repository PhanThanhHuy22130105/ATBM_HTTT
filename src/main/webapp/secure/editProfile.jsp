<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/template/includes/headerResource.jsp" />
<title>Profile Edit & Digital Signature</title>
</head>
<body>
	<%@ include file="/template/includes/navbar.jsp"%>

	<header>
		<section>
			<div class="container py-5">
				<h1 class="mb-5">YOUR PROFILE</h1>
				<div class="row">
					<div class="container">
						<div class="row gutters">
							<div class="col-xl-3 col-lg-3 col-md-12 col-sm-12 col-12">
								<div class="card h-100">
									<div class="card-body">
										<div class="account-settings">
											<div class="user-profile row justify-content-center">
												<div class="user-avatar">
													<img
														src="${pageContext.request.contextPath}/${sessionScope.user.getImg()}"
														alt="avatar" style="max-width: 100px; max-height: 100px;">
												</div>
												<h5 class="user-name mt-2">${sessionScope.user.username }</h5>
												<i class="user-email d-block">Email: ${sessionScope.user.email }</i>
												<i class="user-email d-block">Phone: ${sessionScope.user.phone }</i>
												<i class="user-email d-block">Address: ${sessionScope.user.address }</i>
											</div>
										</div>
									</div>
								</div>
							</div>

							<div class="col-xl-9 col-lg-9 col-md-12 col-sm-12 col-12">

								<div class="card">
									<form action="${pageContext.request.contextPath}/secure/edit?user_id=${sessionScope.user.id}" method="POST">
										<input type="hidden" name="user_id" value="${sessionScope.user.id}">
										<div class="card-body">
											<div class="row gutters">
												<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
													<h6 class="mb-2 text-primary">Personal Details</h6>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
													<div class="form-group">
														<label for="fullName">Full Name</label>
														<input type="text" class="form-control" id="fullName" name="username" value="${sessionScope.user.username }" required>
													</div>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
													<div class="form-group">
														<label for="eMail">Email</label>
														<input type="email" name="email" class="form-control" id="eMail" value="${sessionScope.user.email }" required>
													</div>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
													<div class="form-group">
														<label for="phone">Phone</label>
														<input type="text" name="phone" class="form-control" id="phone" value="${sessionScope.user.phone }" required>
													</div>
												</div>
											</div>
											<div class="row gutters">
												<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
													<h6 class="mt-3 mb-2 text-primary">Address</h6>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
													<div class="form-group">
														<label for="Street">Address</label>
														<input type="text" name="address" class="form-control" id="Street" value="${sessionScope.user.address }" required>
													</div>
												</div>
											</div>
											<c:set var="message" value="${not empty param.message ? param.message : ''}" />
											<span class="text-danger">${message}</span>
											<div class="row gutters">
												<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
													<div class="my-3">
														<button class="btn btn-primary" type="submit">Update Profile</button>
														<a href="${pageContext.request.contextPath}/UploadServlet" class="btn btn-secondary ml-2">Change Avatar</a>
													</div>
												</div>
											</div>
										</div>
									</form>
								</div>

								<div class="card mt-4 mb-5 border-success">
									<div class="card-header bg-success text-white">
										<h5 class="mb-0"><i class="fa fa-shield"></i> Xác Thực Đơn Hàng Bằng Chữ Ký Điện Tử</h5>
									</div>
									<div class="card-body">
										<p>Vui lòng nhập <strong>Mã đơn hàng</strong> và tải lên file <strong>signature.bin</strong> bạn vừa tạo từ Tool để hệ thống xác thực.</p>

										<c:if test="${not empty param.msg}">
											<div class="alert alert-success"><strong>Thành công: </strong>${param.msg}</div>
										</c:if>
										<c:if test="${not empty param.error}">
											<div class="alert alert-danger"><strong>Thất bại: </strong>${param.error}</div>
										</c:if>

										<form action="${pageContext.request.contextPath}/secure/verify-signature" method="post" enctype="multipart/form-data">
											<div class="row">
												<div class="col-md-4">
													<div class="form-group">
														<label for="orderId" class="font-weight-bold">Mã Đơn Hàng:</label>
														<input type="number" class="form-control" name="orderId" placeholder="VD: 10" required>
													</div>
												</div>
												<div class="col-md-8">
													<div class="form-group">
														<label for="signatureFile" class="font-weight-bold">File Chữ Ký (.bin):</label>
														<input type="file" class="form-control p-1" name="signatureFile" accept=".bin" required>
													</div>
												</div>
											</div>
											<button class="btn btn-success mt-2" type="submit">Tiến Hành Xác Thực</button>
										</form>
									</div>
								</div>
								</div>
						</div>
					</div>
				</div>
			</div>
		</section>
	</header>

	<%@ include file="/template/includes/footer.jsp"%>
	
</body>
</html>