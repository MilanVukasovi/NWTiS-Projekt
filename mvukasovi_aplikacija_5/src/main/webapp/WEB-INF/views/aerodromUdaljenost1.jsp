<%@page import="jakarta.ejb.Init"%>
<%@page import="jakarta.ws.rs.QueryParam"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="java.util.List"
	import="org.foi.nwtis.podaci.UdaljenostiIzmeduAerodroma"
	import="org.foi.nwtis.podaci.Lokacija"
    import="java.lang.Integer"
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenost 1</title>
</head>
<body>
<h1></h1>
<%@ include file="zaglavlje.html" %>  
<%
List<UdaljenostiIzmeduAerodroma> podaci = (List<UdaljenostiIzmeduAerodroma>) request.getAttribute("udaljenost");
%>
<table>
<tr><th>ICAO</th><th>Naziv</th><th>Država</th><th>Km</th></tr>
<%for (UdaljenostiIzmeduAerodroma aerodrom : podaci) {
  %>
  <tr>
	  <td><%=aerodrom.icao()%></td>
	  <td><%=aerodrom.naziv()%></td>
	  <td><%=aerodrom.drzava()%></td>
	  <td><%=aerodrom.km()%></td>
  </tr>
  <%
}
%>
</table>
</body>
</html>