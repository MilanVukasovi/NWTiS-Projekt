<%@page import="jakarta.ejb.Init"%>
<%@page import="jakarta.ws.rs.QueryParam"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="java.util.List"
	import="org.foi.nwtis.podaci.Aerodrom"
    import="java.lang.Integer"
	%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Svi</title>
</head>
<body>
<h1>Svi aerodromi</h1>
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

%>
<form id="forma" method="GET" action="${pageContext.servletContext.contextPath}/mvc/aerodromi/pregled">
  <label for="traziNaziv">Naziv:</label>
  <input type="text" id="traziNaziv" name="traziNaziv">
  <br>
  <label for="traziDrzavu">Država:</label>
  <input type="text" id="traziDrzavu" name="traziDrzavu">
  <br>
  <button type="submit">Pretraži</button>
</form>
<% 

List<Aerodrom> podaci = (List<Aerodrom>) request.getAttribute("aerodromi");
%>
<table>
<tr><th>ICAO</th><th>Naziv</th><th>Država</th><th>Pregled</th><th>Dodaj aerodrom</th></tr>
<%for (Aerodrom aerodrom : podaci) {
  String urlPregled = request.getContextPath() + "/mvc/aerodromi/"+aerodrom.getIcao();
  String urlDodaj = request.getContextPath() + "/mvc/aerodromi/dodajAerodrom/"+aerodrom.getIcao();
  %>
  	<tr>
  	<td><%=aerodrom.getIcao()%></td>
  	<td><%=aerodrom.getNaziv()%></td>
  	<td><%=aerodrom.getDrzava()%></td>
  	<td><a href="<%=urlPregled%>"><button>Pogledaj</button></a></td>
  	<td>
	<form action=<%=urlDodaj %> method="POST">
	  	<input type="hidden" class="adresa" value="<%= urlDodaj %>">
	    <input type="submit" value="DODAJ" class="dodaj">
	</form>
	</td>
  </tr>
  <%
}
%>
</table>
<%if(odBroja > 0) {%>
  <a href="?odBroja=<%= odBroja-pomak %>&broj=<%=broj%>">&laquo; Prethodna</a>
 <%}%>
<a href="?odBroja=<%= 0 %>&broj=<%=pomak%>">Početna</a>
<a href="?odBroja=<%= odBroja+pomak %>&broj=<%=broj%>">Sljedeća &raquo;</a>
<script>
	function osvjeziStranicu() {
        window.location.reload();
    }

    var dodajButtons = document.getElementsByClassName("dodaj");

    Array.from(dodajButtons).forEach(function(button) {
        button.addEventListener("click", function(event) {
            event.preventDefault();
            
            var form = button.closest("form");
            var adresa = form.querySelector(".adresa").value;

            fetch(adresa, {
                method: "POST"
            })
            .then(function(response) {
            	if (response.ok) {
                    alert("Dodali ste aerodrom za dohvaćanje letova");
                    osvjeziStranicu();
                } else {
                    throw new Error("Server error");
                }
            })
            .catch(function(error) {
                console.log("Dogodila se greška prilikom dodavanja aerodroma: ", error);
            });
        });
    });

</script>
</body>
</html>