<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Korisnici</title>
</head>
<body>
<h1>Korisnici</h1>
<%@ include file="zaglavlje.html" %>  
<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/registracija">Registracija</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/prijava">Prijava</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/korisnici/pregled">Pregled korisnika</a><br/>
</body>
</html>