<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>test page</title>
</head>
<body>
	<h2>This is a test page</h2>
	<table>
		<tr>
			<td>id</td>
			<td>${testBean.id}</td>
		</tr>
		<tr>
			<td>name</td>
			<td>${testBean.name}</td>
		</tr>
	</table>
</body>
</html>