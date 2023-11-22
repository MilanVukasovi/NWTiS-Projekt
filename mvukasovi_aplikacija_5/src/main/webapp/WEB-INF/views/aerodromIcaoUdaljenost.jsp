<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List"
	import="org.foi.nwtis.podaci.UdaljenostAerodrom"
	import="java.util.Properties, java.io.FileInputStream"
%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodrom udaljenosti</title>
</head>
<body>
<%@ include file="zaglavlje.html" %>  
<%
int odBroja = 0;
int broj = 0;
int pomak = Integer.parseInt(konfiguracija.getProperty("stranica.brojRedova"));
try {
	odBroja = Integer.parseInt(request.getParameter("odBroja"));
	broj = Integer.parseInt(request.getParameter("broj"));
} catch (NumberFormatException e) {
  odBroja = 0;
  broj = pomak;
}
String icao = request.getParameter("icao");

List<UdaljenostAerodrom> podaci = (List<UdaljenostAerodrom>) request.getAttribute("udaljenosti");
%>
<table>
<tr><th>ICAO</th><th>KM</th></tr>
<%
for(UdaljenostAerodrom udaljenost : podaci) {
  %><tr><td><%=udaljenost.icao()%></td><td><%=udaljenost.km()%></td></tr>
  <%
}
%>
</table>
<%if(odBroja > 0) {
  %>
  <a href="?icao=<%= icao %>&odBroja=<%= odBroja-pomak %>&broj=<%=broj%>">&laquo; Prethodna</a>
  <%
}
%>
<a href="?icao=<%= icao %>&odBroja=<%= 0 %>&broj=<%=pomak%>">Početna</a>
<a href="?icao=<%= icao %>&odBroja=<%= odBroja+pomak %>&broj=<%=broj%>">Sljedeća &raquo;</a>
</body>
</html>