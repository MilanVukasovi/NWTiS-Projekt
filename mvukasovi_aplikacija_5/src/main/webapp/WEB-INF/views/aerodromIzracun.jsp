<%@page import="org.foi.nwtis.mvukasovi.ws.WsMeteo.endpoint.MeteoPodaci"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"	
    import="java.util.Properties, java.io.FileInputStream"
%>
 
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodrom</title>
</head>
<body>
<h1>Izračun udaljenosti između ${icaoOd} i ${icaoDo}</h1>
<%@ include file="zaglavlje.html" %>  
Udaljenost je: ${udaljenost} km
</body>
</html>