<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<link
	href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css"
	rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/success.css"
	rel="stylesheet" />
<title>Success</title>
</head>
<body>

	<!-- Congratulations area starts -->
	<div class="congratulation-area text-center mt-5">
		<div class="container">
			<div class="congratulation-wrapper">
				<div class="congratulation-contents center-text">
					<div class="congratulation-contents-icon">
						<i class="fas fa-check"></i>
					</div>
					<h4 class="congratulation-contents-title">Congratulations!</h4>
					<p class="congratulation-contents-para">Your account is ready
						to submit proposals and get work.</p>
					<form action="${pageContext.request.contextPath}/Homepage" method="POST">
						<div class="btn-wrapper mt-4">
							<button class="cmn-btn btn-bg-1"> Go to Home </button>
						</div>
					</form>

				</div>
			</div>
		</div>
	</div>

	<!-- Bootstrap core JS-->
	<script
		src="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>