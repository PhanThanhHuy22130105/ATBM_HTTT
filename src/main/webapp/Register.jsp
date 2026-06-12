<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registration</title>
<link rel="icon" type="image/x-icon" href="assets/favicon.ico" />
<link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.5.0/font/bootstrap-icons.css" rel="stylesheet" />
<link href="css/styles.css" rel="stylesheet" />
</head>
<body>

	<!-- Navbar -->
	<%@ include file="template/includes/navbar.jsp"%>
	

<!-- Content -->
<section class="bg-light p-3 p-md-4 p-xl-5">
  <div class="container">
    <div class="row justify-content-center">
      <div class="col-12 col-md-9 col-lg-7 col-xl-6 col-xxl-5">
        <div class="card border border-light-subtle rounded-4">
          <div class="card-body p-3 p-md-4 p-xl-5">
            <div class="row">
              <div class="col-12">
                <div class="mb-5">
                  <div class="text-center mb-4">
                    <a href="#!">
                      <img src="image/website/logo.png" alt="Logo" width="100" height="100">
                    </a>
                  </div>
                  <h2 class="h4 text-center">Registration</h2>
                  <h3 class="fs-6 fw-normal text-secondary text-center m-0">Enter your details to register</h3>
                </div>
              </div>
            </div>
            
            
            <form action="Register" method ="POST">
              <div class="row gy-3 overflow-hidden">
                <div class="col-12">
                  <div class="form-floating mb-3">
                    <input type="text" class="form-control" name="username" id="Username" placeholder="Username" required>
                    <label for="username" class="form-label">Username</label>
                  </div>
                </div>
                <div class="col-12">
                  <div class="form-floating mb-3">
                    <input type="password" class="form-control" name="password" id="password" placeholder="Password" required>
                    <label for="password" class="form-label">Password</label>
                  </div>
                </div>
                <div class="col-12">
                  <div class="form-floating mb-3">
                    <input type="email" class="form-control" name="email" id="email" placeholder="name@example.com" required>
                    <label for="email" class="form-label">Email</label>
                  </div>
                </div>
                
                <div class="col-12">
                  <div class="form-floating mb-3">
                    <input type="tel" class="form-control" name="phone" id="Phone-Number" value="" placeholder="Password" required>
                    <label for="phone" class="form-label">Phone Number</label>
                  </div>
                </div>
                	<c:set var="Rmessage" value="${requestScope.Rmessage}"/>
				    <c:if test="${not empty Rmessage}">
				    	<p id="error-email" style="color: red;"> ${Rmessage}</p>
				    </c:if>
                <div class="col-12">
                  <div class="form-check">
                    <input class="form-check-input" type="checkbox" value="" name="iAgree" id="iAgree" required>
                    <label class="form-check-label text-secondary" for="iAgree">
                      I agree to the <a href="#!" class="link-primary text-decoration-none">terms and conditions</a>
                    </label>
                  </div>
                </div>
                <div class="col-12">
                  <div class="d-grid">
                    <button class="btn bsb-btn-xl btn-primary" type="submit">Sign up</button>
                  </div>
                </div>
              </div>
            </form>
            <div class="row">
              <div class="col-12">
                <hr class="mt-5 mb-4 border-secondary-subtle">
                <p class="m-0 text-secondary text-center">Already have an account? <a href="Login.jsp" class="link-primary text-decoration-none">Sign in</a></p>
              </div>
            </div>
            <div class="row">
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</section>

	<!-- footer -->
	<%@ include file="template/includes/footer.jsp"%>

</body>
</html>