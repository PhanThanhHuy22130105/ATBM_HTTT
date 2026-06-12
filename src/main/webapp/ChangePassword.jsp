<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link href="css/styles.css" rel="stylesheet" />
<title>Insert title here</title>
</head>
<body>


	<!-- Navbar -->
	<%@ include file="template/includes/navbar.jsp"%>


	<section class="bg-light py-3 py-md-5">
		<div class="container">
			<div class="row justify-content-center">
				<div class="col-12 col-sm-10 col-md-8 col-lg-6 col-xl-5 col-xxl-4">
					<div class="card border border-light-subtle rounded-3 shadow-sm">
						<div class="container">
							<div class="row justify-content-center">
								<div class="text-center mb-3">
									<h1>Change Password</h1>
								</div>
							</div>
							<div class="row justify-content-center">
								<p class="text-center">Change your password.</p>
								<form method="post" Action="ChangePassword">
									<input type="password" class="input-lg form-control"
										name="password1" id="password1" placeholder="New Password"
										autocomplete="off"> 
									<input type="password"
										class="input-lg form-control" name="password2" id="password2"
										placeholder="Repeat Password" autocomplete="off">
									<p class="danger-text"> ${requestScope.message } </p>
									<input type="submit"
										class="col-xs-12 btn btn-primary btn-load btn-lg"
										data-loading-text="Changing Password..."
										value="Change Password">
								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</section>

	<%@ include file="template/includes/footer.jsp"%>


</body>
</html>