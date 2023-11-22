<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List"
	import="org.foi.nwtis.podaci.Udaljenost"
	import="java.util.Properties, java.io.FileInputStream"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Udaljenosti</title>
</head>
<body>
<%@ include file="zaglavlje.html" %>  
<%
List<Udaljenost> podaci = (List<Udaljenost>) request.getAttribute("udaljenosti");
%>
<table>
<tr><th>Država</th><th>Km</th></tr>
<%
float ukupno = 0;
for(Udaljenost udaljenost : podaci) {
  ukupno += udaljenost.km();
  %><tr><td><%=udaljenost.drzava()%></td><td><%=udaljenost.km()%></td></tr>
  <%
}
%>
</table>
Ukupna udaljenost = <%=ukupno %> km
</body>
</html>