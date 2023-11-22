<%@page import="jakarta.ejb.Init"%>
<%@page import="jakarta.ws.rs.QueryParam"%>
<%@page import="org.foi.nwtis.mvukasovi.ws.WsAerodromi.endpoint.AerodromStatusPreuzimanja"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
	import="java.util.List"
    import="java.lang.Integer"
	import="java.util.Properties, java.io.FileInputStream"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Aerodromi za letove</title>
</head>
<body>
<h1>Aerodromi e se preuzimaju podaci o polascima</h1>
<%@ include file="zaglavlje.html" %>  
<%
List<AerodromStatusPreuzimanja> podaci = (List<AerodromStatusPreuzimanja>) request.getAttribute("aerodromi");
%>
<table>
<tr><th>ICAO</th><th>Naziv</th><th>Država</th><th>Latitude</th><th>Longitude</th><th>Status preuzimanja</th><th>Promjeni status</th></tr>
<%for (AerodromStatusPreuzimanja aerodrom : podaci) {
	%>
	<tr>
	  	<td><%=aerodrom.getAerodrom().getIcao()%></td>
	  	<td><%=aerodrom.getAerodrom().getNaziv()%></td>
	  	<td><%=aerodrom.getAerodrom().getDrzava()%></td>
	  	<td><%=aerodrom.getAerodrom().getLokacija().getLatitude()%></td>
	  	<td><%=aerodrom.getAerodrom().getLokacija().getLongitude()%></td>
	  	<% if (aerodrom.isStatus()) { %>
	  	<td>aktivan</td>
	  	<td><button onclick="pauzirajAerodrom('<%= aerodrom.getAerodrom().getIcao() %>')">Pauziraj</button><td>
	  	<% } else { %>
	  	<td>pauziran</td>
	  	<td><button onclick="aktivirajAerodrom('<%= aerodrom.getAerodrom().getIcao() %>')">Aktiviraj</button></td>
	  	<% } %>
  	</tr>
  	<%
}
%>
</table>
<script>
	function pauzirajAerodrom(icao) {
	    fetch("${pageContext.request.contextPath}/mvc/aerodromi/aerodromiBazaPauziraj/?icao=" + icao, {
	      method: "POST"
	    })
	    .then(function(response) {
	      if (response.ok) {
	        alert("Aerodrom je pauziran");
	        osvjeziStranicu();
	      } else {
	        throw new Error("Neuspješno pauziranje aerodroma");
	      }
	    })
	    .catch(function(error) {
	      alert(error.message);
	    });
	}

	function aktivirajAerodrom(icao) {
	    fetch("${pageContext.request.contextPath}/mvc/aerodromi/aerodromiBazaAktiviraj/?icao=" + icao, {
	      method: "POST"
	    })
	    .then(function(response) {
	      if (response.ok) {
	        alert("Aerodrom je aktiviran");
	        osvjeziStranicu();
	      } else {
	        throw new Error("Neuspješno aktiviranje aerodroma");
	      }
	    })
	    .catch(function(error) {
	      alert(error.message);
	    });
	}

	function osvjeziStranicu() {
    	window.location.reload();
	}
</script>
</body>
</html>