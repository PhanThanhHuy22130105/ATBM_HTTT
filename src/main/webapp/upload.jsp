<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/template/includes/headerResource.jsp" />
<title>Upload Avatar</title>
</head>
<body>


	<!-- Navbar -->
	<%@ include file="template/includes/navbar.jsp"%>

	<div align="center">

		<h1>File Upload</h1>
		<div class="container">
			<div class="row justify-content-center">
				<div class="col-12 col-sm-10 col-md-8 col-lg-6 col-xl-5 col-xxl-4">
					<div class="card border border-light-subtle rounded-3 shadow-sm">
						<div class="card-body p-3 p-md-4 p-xl-5">
							<div class="text-center mb-3">
								<form method="post" action="UploadServlet"
									enctype="multipart/form-data">

									<p>
										Select file to upload: <input type="file" name="photo"
											size="60" />
									</p>

									<input type="submit" value="Upload" />

								</form>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>

</body>
</html>