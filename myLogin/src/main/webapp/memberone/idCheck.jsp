<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="memberone.*" %>
<%
		StudentDAO dao = StudentDAO.getInstance();
	String id = request.getParameter("id");
	boolean check = dao.idCheck(id);
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ID 중복체크</title>
<link href="style.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="script.js"></script>
</head>
<body bgcolor="#FFFFCC">
<br>
	<center>
		<b><%=id %></b>
		<%
			if(check){
				out.println("는 이미 존자하는 id 입니다.<br>");
				
			}else{
				out.println("는 사용 가능 합니다.<br>");
			}
		%>
		<a href="#" onClick="javascript:self.close()">닫기</a>
	
	</center>

</body>
</html>