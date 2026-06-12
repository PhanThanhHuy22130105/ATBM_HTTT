<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<jsp:include page="/template/includes/headerResource.jsp" />
<title>Insert title here</title>
</head>
<body>
	<!-- Navbar -->
	<%@ include file="/template/includes/navbar.jsp"%>

	<div>
		<span>YOU RAN TO SOME TROUBLE!!! <br>
		 ${sessionScope.errorMessage}</span>
	</div>


</body>
</html>