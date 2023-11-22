<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"
    import="java.util.List"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Poruke</title>
</head>
<body>
<h1>JMS poruke</h1>
<%@ include file="zaglavlje.html" %>  
<%
int i = 1;
List<String> podaci = (List<String>) request.getAttribute("poruke");
for(String p : podaci) {%>
  Poruka:<%=i%> <%=p%><br/>
  <%i++;
}
%> 
<form action="poruke" method="get">
    <button type="submit" name="obrisi" text="" value="obrisi">Obriši sve poruke</button>
 </form>

</body>
</html>