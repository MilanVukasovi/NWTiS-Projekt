<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ICAO</title>
</head>
<body>
<h1>Pregled udaljenosti između dva aerodroma unutar država</h1>
<%@ include file="zaglavlje.html" %>  
<form id="icaoForma" method="GET" action="${pageContext.servletContext.contextPath}/mvc/aerodromi/">
  <label for="icaoOd">ICAO Od:</label>
  <input type="text" id="icaoOd" name="icaoOd" required>
  <br>
  <label for="icaoDo">ICAO Do:</label>
  <input type="text" id="icaoDo" name="icaoDo" required>
  <br>
  <button type="submit">Pretraži</button>
</form>

<script>
  document.getElementById("icaoForma").addEventListener("submit", function(event) {
    event.preventDefault();

    var icaoOdInput = document.getElementById("icaoOd");
    var icaoDoInput = document.getElementById("icaoDo");
    var icaoOdVrijednost = icaoOdInput.value.trim();
    var icaoDoVrijednost = icaoDoInput.value.trim();

    var baseUrl = "${pageContext.servletContext.contextPath}/mvc/aerodromi/";
    var actionUrl = baseUrl + icaoOdVrijednost + "/" + icaoDoVrijednost;

    window.location.href = actionUrl;
  });
</script>


</body>
</html>