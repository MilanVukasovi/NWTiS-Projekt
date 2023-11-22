<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ICAO</title>
</head>
<body>
<h1>Pregled aerodroma:</h1>
<%@ include file="zaglavlje.html" %>  
<form id="icaoForma" method="GET" action="${pageContext.servletContext.contextPath}/mvc/aerodromi/">
  <label for="icao">ICAO:</label>
  <input type="text" id="icao" name="icao" required>
  <button type="submit">Pretraži</button>
</form>

<script>
  document.getElementById("icaoForma").addEventListener("submit", function(event) {
    event.preventDefault();

    var icaoInput = document.getElementById("icao");
    var icaoVrijednost = icaoInput.value.trim();

    var baseUrl = "${pageContext.servletContext.contextPath}/mvc/aerodromi/";
    var actionUrl = baseUrl + icaoVrijednost;

    window.location.href = actionUrl;
  });
</script>

</body>
</html>