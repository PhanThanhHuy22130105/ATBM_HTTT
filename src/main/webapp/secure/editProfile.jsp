<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/template/includes/headerResource.jsp" />
<title>Profile Edit</title>
</head>
<body>
	<!-- Navbar -->
	<%@ include file="/template/includes/navbar.jsp"%>

	<header>
		<!-- MAIN -->
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
												<h5 class="user-name">${sessionScope.user.username }</h5>
												<i class="user-email">Email: ${sessionScope.user.email }
												</i> <i class="user-email">Phone: ${sessionScope.user.phone }
												</i> <i class="user-email">Address:
													${sessionScope.user.address } </i>
											</div>
										</div>
									</div>
								</div>
							</div>
							<div class="col-xl-9 col-lg-9 col-md-12 col-sm-12 col-12">
								<div class="card h-100">
									<form
										action="${pageContext.request.contextPath}/secure/edit?user_id=${sessionScope.user.id}"
										method="POST">
										<input type="hidden" name="user_id"
											value="${sessionScope.user.id}">
										<div class="card-body">
											<div class="row gutters">
												<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
													<h6 class="mb-2 text-primary">Personal Details</h6>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
													<div class="form-group">
														<label for="fullName">Full Name</label> <input type="text"
															class="form-control" id="fullName" name="username"
															value="${sessionScope.user.username }" required>
													</div>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
													<div class="form-group">
														<label for="eMail">Email</label> <input type="email"
															name="email" class="form-control" id="eMail"
															value="${sessionScope.user.email }" required>
													</div>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
													<div class="form-group">
														<label for="phone">Phone</label> <input type="text"
															name="phone" class="form-control" id="phone"
															value="${sessionScope.user.phone }" required>
													</div>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">


												</div>
											</div>
											<div class="row gutters">
												<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
													<h6 class="mt-3 mb-2 text-primary">Address</h6>
												</div>
												<div class="col-xl-6 col-lg-6 col-md-6 col-sm-6 col-12">
													<div class="form-group">
														<label for="Street">Address</label> <input type="text"
															name="address" class="form-control" id="Street"
															value="${sessionScope.user.address }" required>
													</div>
												</div>
											</div>
											<c:set var="message"
												value="${not empty param.message ? param.message : ''}" />
											<span class="text-danger">${message}</span>
											<div class="row gutters">
												<div class="col-xl-12 col-lg-12 col-md-12 col-sm-12 col-12">
													<div class="col-6">
														<div class="my-3">
															<button class="btn btn-primary btn-lg" type="submit">update
																profile</button>
														</div>
														<div class="my-3">
															<a
																href="${pageContext.request.contextPath}/UploadServlet"
																class="btn btn-primary btn-lg">Change Avatar</a>
														</div>
													</div>
												</div>
											</div>
										</div>
									</form>
								</div>
							</div>
						</div>
					</div>
				</div>
			</div>

		</section>
	</header>


	<!-- FOOTER -->
	<%@ include file="/template/includes/footer.jsp"%>
	
</body>
</html>