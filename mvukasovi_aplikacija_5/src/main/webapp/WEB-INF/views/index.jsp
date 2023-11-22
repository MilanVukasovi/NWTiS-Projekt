<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Projekt</title>
</head>
<body>
<h1>Projekt</h1>
<%@ include file="zaglavlje.html" %>  
<a href="${pageContext.servletContext.contextPath}/mvc/korisnici">Upravljanje korisnicima</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/app1">Upravljanje aplikacijom 1</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/poruke">Pregled JMS poruka</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi">Aerodromi</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/letovi">Letovi - NIJE IMPLEMENTIRANO</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/dnevnik">Dnevnik - NIJE IMPLEMENTIRANO</a><br/>
<h3>Izradio: Milan Vukasović</h3>
</body>
</html>