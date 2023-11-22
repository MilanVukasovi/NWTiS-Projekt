<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi</title>
</head>
<body>
<h1>Aerodromi</h1>
<%@ include file="zaglavlje.html" %>  
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregled">Pregled svih aerodroma</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/icao">Pregled izabranog aerodroma</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/aerodromiBaza">Pregled aerodroma za koje se preuzimaju podaci o polascima</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenosti2aerodromaUnesi">Pregled udaljenosti između dva aerodroma unutar država preko kojih leti</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/izracunaj">Izračun udaljenosti između dva aerodroma</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenost1">Pregled aerodroma i udaljenosti do polaznog aerodroma unutar države odredišnog aerodroma</a><br/>
<a href="${pageContext.servletContext.contextPath}/mvc/aerodromi/udaljenost2">Pregled aerodroma i udaljenosti do polaznog aerodroma unutar zadane države</a><br/>
</body>
</html>