<%@page import="org.foi.nwtis.mvukasovi.ws.WsKorisnici.endpoint.Korisnik"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List"
    import="java.lang.Integer"
	import="java.util.Properties, java.io.FileInputStream"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Pregled korisnika</title>
</head>
<body>
<h1>Pregled korisnika</h1>
<%@ include file="zaglavlje.html" %>  
<%
List<Korisnik> podaci = (List<Korisnik>) request.getAttribute("korisnici");
%>
<form method="GET" action="${pageContext.request.contextPath}/mvc/korisnici/pregled">
    <label for="name">Ime:</label>
    <input type="text" id="traziImeKorisnika" name="traziImeKorisnika"><br><br>
    
    <label for="surname">Prezime:</label>
    <input type="text" id="traziPrezimeKorisnika" name="traziPrezimeKorisnika"><br><br>
    
    <input type="submit" value="Pretraži">
</form>
<table>
<tr><th>Korisničko ime</th><th>Ime</th><th>Prezime</th></tr>
<%for (Korisnik korisnik : podaci) {
  %>
  <tr>
	  <td><%=korisnik.getKorIme()%></td>
	  <td><%=korisnik.getIme()%></td>
	  <td><%=korisnik.getPrezime()%></td>
  </tr>
  <%
}
%>
</table>
</body>
</html>