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
	<h1>Podaci o ${aerodrom.icao}</h1>
	<%@ include file="zaglavlje.html" %>  
	ICAO: ${aerodrom.icao} <br/>
	Naziv: ${aerodrom.naziv} <br/>
	Država: ${aerodrom.drzava} <br/>
	<br/>
	Meterološki podaci: <br/>
	<%
	MeteoPodaci meteoPodaci = (MeteoPodaci) request.getAttribute("meteo");%>
	<%=meteoPodaci.getCloudsName()%> <br/>
	Vrijednost oblaka: <%=meteoPodaci.getCloudsValue()%> <br/>
	Vlaga: <%=meteoPodaci.getHumidityValue()%> <%=meteoPodaci.getHumidityUnit()%><br/>
	Temperature: <%=meteoPodaci.getTemperatureValue()%> <%=meteoPodaci.getTemperatureUnit()%> <br/>
	Temp/Min: <%=meteoPodaci.getTemperatureMin()%> <%=meteoPodaci.getTemperatureUnit()%> <br/>
	Temp/Max: <%=meteoPodaci.getTemperatureMax()%> <%=meteoPodaci.getTemperatureUnit()%> <br/>
	Tlak: <%=meteoPodaci.getPressureValue()%> <%=meteoPodaci.getPressureUnit()%>  <br/>
	Izlazak sunca: <%=meteoPodaci.getSunRise()%> <br/>
	Zalazak sunca: <%=meteoPodaci.getSunSet()%> <br/>
	Zadnje ažurirano: <%=meteoPodaci.getSunSet()%> <br/>
</body>
</html>