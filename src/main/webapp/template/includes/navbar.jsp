<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!-- Navigation-->
<nav class="navbar navbar-expand-lg navbar-light bg-light shadow">
	<div class="container px-4 px-lg-5">
		<a class="navbar-brand" href="${pageContext.request.contextPath}/Homepage">Boba Station</a>
		<button class="navbar-toggler" type="button" data-bs-toggle="collapse"
			data-bs-target="#navbarSupportedContent"
			aria-controls="navbarSupportedContent" aria-expanded="false"
			aria-label="Toggle navigation">
			<span class="navbar-toggler-icon"></span>
		</button>
		<div class="collapse navbar-collapse" id="navbarSupportedContent">
			<ul class="navbar-nav me-auto mb-2 mb-lg-0 ms-lg-4">
				<li class="nav-item"><a class="nav-link active"
					aria-current="page" href="#!">Home</a></li>
				<li class="nav-item"><a class="nav-link" href="#!">About</a></li>
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" id="navbarDropdown" href="#"
					role="button" data-bs-toggle="dropdown" aria-expanded="false">Shop</a>
					<ul class="dropdown-menu" aria-labelledby="navbarDropdown">
						<li><a class="dropdown-item" href="#!"
							href="${pageContext.request.contextPath}/category?id=all">All
								Products</a></li>
						<li><hr class="dropdown-divider" /></li>
						<li><a class="dropdown-item" href="#!">Popular Items</a></li>
					</ul></li>
			</ul>

			<c:set var="userId" value="${sessionScope.userId}" />
			<c:set var="userImg" value="${sessionScope.img}" />
			<c:choose>
				<c:when test="${userId != null}">
					<a
						href="${pageContext.request.contextPath}/secure/cart?userId=${sessionScope.user.getId()}"
						class="d-flex px-4" style="text-decoration: none;">
						<button class="btn btn-outline-dark" type="button">
							<i class="bi-cart-fill me-1"></i> Cart <span
								class="badge bg-dark text-white ms-1 rounded-pill"></span>
						</button>
					</a>

					<div class="dropdown">
						<button class="btn dropdown-toggle d-flex align-items-center"
							type="button" id="userDropdown" data-bs-toggle="dropdown"
							aria-expanded="false">
							<img style="height: 40px; width: 40px; padding: 0;"
								class="rounded-circle me-2"
								src="${pageContext.request.contextPath}/${userImg}" alt="avatar">
							<span>Profile</span>
						</button>
						<ul class="dropdown-menu">
							<li class="text-center border-bottom"><a
								class="dropdown-item "
								href="${pageContext.request.contextPath}/secure/edit?user_id=${userId}">Edit
									profile</a></li>
							<li class="text-center border-bottom"><a
								class="dropdown-item "
								href="${pageContext.request.contextPath}/secure/user/saved?id=${userId}">Saved</a>
							</li>
							<li class="text-center border-bottom"><a
								class="dropdown-item "
								href="${pageContext.request.contextPath}/secure/user/history">History</a>
							</li>
							<li class="text-center border-bottom"><a
								class="dropdown-item "
								href="${pageContext.request.contextPath}/logout"
								style="text-decoration: none;">Log out</a>
							</li>
						</ul>
					</div>
				</c:when>
				<c:otherwise>
					<div class="widget-header ">
						<div>
							<a href="${pageContext.request.contextPath}/Login.jsp">
								<button class="btn btn-outline-dark">Login</button>
							</a> <a href="${pageContext.request.contextPath}/Register.jsp">
								<button class="btn btn-outline-dark">Register</button>
							</a>
						</div>
					</div>
				</c:otherwise>
			</c:choose>



		</div>
	</div>
</nav>