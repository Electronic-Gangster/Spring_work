<%@ page language="java" contentType="text/html; charset=UTF-8"
   pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>   

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<script src="https://code.jquery.com/jquery-3.6.3.js"></script>
<link href="https://cdn.jsdelivr.net/npm/bootstrap@5.2.3/dist/css/bootstrap.min.css" rel="stylesheet">
<link href="https://fonts.googleapis.com/css2?family=Gamja+Flower&family=Jua&family=Lobster&family=Nanum+Pen+Script&family=Single+Day&display=swap" rel="stylesheet">
<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.8.0/font/bootstrap-icons.css">
<style>
	body, body *{

		font-family: 'Jua'

	}
	.memberphoto{
		width: 50px;
		height: 50px;
		margin-right: 10px;
		border: 1px solid black;
		border-radius: 100px;
	}
</style>
</head>
<body>
	<i class="bi bi-house" style="margin:30px 50px;font-size: 30px; cursor: pointer; color: red;"
	onclick="location.href='../'"></i>
	<hr>
	<c:if test="${sessionScope.loginok==null}">
		<script type="text/javascript">
			alert("먼저 로그인을 해주세요");
			location.href="../login/login";
		</script>
	</c:if>
	<c:if test="${sessionScope.loginok!=null}">
		<h2 align="center">${sessionScope.loginemail}
		(
			<img src="../photo/${dto.photo}" class="memberphoto">
			${dto.name}
		)님이 로그인 중입니다.</h2>
	</c:if>
	<hr>
	<table class="table table-bordered" style="width: 700px;">
		<caption align="top"><h4><b>회원게시판 목록</b></h4>
			<span style="float:right; font-size: 20px; cursor: pointer;"
			onclick="location.href='./form'">
				<i class="bi bi-pencil">글쓰기</i>
			</span>
		</caption>
		<tr style="background-color: #ddd">
			<th style="width: 40px">번 호</th>
			<th style="width: 200px">제 목</th>
			<th style="width: 70px">작성자</th>
			<th style="width: 120px">작성일</th>
			<th style="width: 70px">조회수</th>
		</tr>
		<c:if test="${totalCount==0 }">
			<tr height="50">
				<td colspan="5" align="center" valign="middle">
					<b style="font-size: 1.3em">등록된 게시글이 없습니다.</b>
				</td>
			</tr>
		</c:if>
	</table>
</body>
</html>
