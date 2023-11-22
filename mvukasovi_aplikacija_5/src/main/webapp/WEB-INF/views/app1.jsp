<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@page import="org.foi.nwtis.podaci.StatusApp"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Upravljanje aplikacijom 1</title>
</head>
<body>
<h1>Korisnici</h1>
<%@ include file="zaglavlje.html" %>  
<table>
    <tr>
        <td><button onclick="sendAction('STATUS')">STATUS</button></td>
        <td><button onclick="sendAction('KRAJ')">KRAJ</button></td>
        <td><button onclick="sendAction('INIT')">INIT</button></td>
        <td><button onclick="sendAction('PAUZA')">PAUZA</button></td>
        <td><button onclick="sendAction('INFO_DA')">INFO DA</button></td>
        <td><button onclick="sendAction('INFO_NE')">INFO NE</button></td>
    </tr>
</table>
<%if(request.getAttribute("status") != null){
  StatusApp podaci = (StatusApp) request.getAttribute("status");
  String komanda = (String) request.getAttribute("komanda");
  %>
  Korištena komanda: <%=komanda%>
  Status: <%=podaci.status()%>
  Opis: <%=podaci.opis()%>
  <%
}%>
<script>
  function sendAction(action) {
    var forma = document.createElement("form");
    forma.method = "POST";
    forma.action = "${pageContext.request.contextPath}/mvc/app1/komanda";

    var input = document.createElement("input");
    input.type = "hidden";
    input.name = "action";
    input.value = action;

    forma.appendChild(input);
    document.body.appendChild(forma);

    forma.submit();
  }
</script>

</body>
</html>