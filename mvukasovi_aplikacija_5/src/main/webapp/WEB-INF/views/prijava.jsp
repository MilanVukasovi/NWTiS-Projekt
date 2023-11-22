<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Prijava</title>
</head>
<body>
<h1>Prijava</h1>
<%@ include file="zaglavlje.html" %>  
<form method="post" id="prijavaForma" action="${pageContext.servletContext.contextPath}/mvc/korisnici/prijavljen">
    <label for="username">Korisničko ime:</label>
    <input type="text" id="korIme" name="korIme"><br><br>

    <label for="password">Lozinka:</label>
    <input type="password" id="lozinka" name="lozinka"><br><br>

    <input type="submit" value="Prijavi se">
</form>

<script>
    document.getElementById("prijavaForma").addEventListener("submit", function(event) {
        event.preventDefault();
        var form = event.target;

        var korIme = form.querySelector("#korIme").value.trim();
        var lozinka = form.querySelector("#lozinka").value.trim();

        if (korIme === "" || lozinka === "") {
            alert("Molimo unesite korisničko ime i lozinku");
            return;
        }

        var formData = new FormData();
        formData.append("korIme", korIme);
        formData.append("lozinka", lozinka);

        fetch(form.action, {
            method: "POST",
            body: formData
        })
        .then(function(response) {
            if (response.ok) {
            	alert("Uspješna prijava");
                window.location.href = "${pageContext.request.contextPath}";
            } else {
                throw new Error("Neuspješna prijava");
            }
        })
        .catch(function(error) {
            alert(error.message);
        });
    });
</script>
</body>
</html>