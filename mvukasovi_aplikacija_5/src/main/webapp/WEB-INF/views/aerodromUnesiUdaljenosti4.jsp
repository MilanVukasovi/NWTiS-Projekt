<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>ICAO</title>
</head>
<body>
<h1>Izračunaj udaljenosti između aerodroma:</h1>
<%@ include file="zaglavlje.html" %>  
<form id="icaoForma" method="GET" action="${pageContext.servletContext.contextPath}/mvc/aerodromi/">
  <label for="icaoOd">ICAO Od:</label>
  <input type="text" id="icaoOd" name="icaoOd" required>
  <br>
  <label for="drzava">Država:</label>
  <input type="text" id="drzava" name="drzava" required>
  <br>
  <label for="km">Km:</label>
  <input type="text" id="km" name="km" required>
  <br>
  <button type="submit">Pretraži</button>
</form>

<script>
  document.getElementById("icaoForma").addEventListener("submit", function(event) {
    event.preventDefault(); 
    
    var icaoOdInput = document.getElementById("icaoOd");
    var icaoOdVrijednost = icaoOdInput.value.trim();
    var drzavaInput = document.getElementById("drzava");
    var drzavaVrijednost = drzavaInput.value.trim();
    var kmInput = document.getElementById("km");
    var kmVrijednost = kmInput.value.trim();
    
    var baseUrl = "${pageContext.servletContext.contextPath}/mvc/aerodromi/";
    var actionUrl = baseUrl + icaoOdVrijednost + "/udaljenost2/"+"?drzava="+drzavaVrijednost+"&km="+kmVrijednost;

    window.location.href = actionUrl;
  });
</script>


</body>
</html>