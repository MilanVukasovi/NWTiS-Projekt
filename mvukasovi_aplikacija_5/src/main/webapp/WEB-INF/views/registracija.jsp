<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Registracija</title>
</head>
<body>
<h1>Registracija</h1>
<%@ include file="zaglavlje.html" %>  
    <form method="post" id="registracija" action="${pageContext.servletContext.contextPath}/mvc/korisnici/registriran">
    <label for="username">Korisničko ime:</label>
    <input type="text" id="korIme" name="korIme" required><br><br>

    <label for="password">Lozinka:</label>
    <input type="password" id="lozinka" name="lozinka" required><br><br>

    <label for="name">Ime:</label>
    <input type="text" id="ime" name="ime" required><br><br>

    <label for="surname">Prezime:</label>
    <input type="text" id="prezime" name="prezime" required><br><br>

    <input type="submit" value="Registracija">
</form>

<script>
    document.getElementById("registracija").addEventListener("submit", function(event) {
        event.preventDefault();
        var form = event.target;
        
        var inputs = form.querySelectorAll("input[type='text'], input[type='password']");

        var podaciForme = new FormData(form);

        fetch(form.action, {
            method: "POST",
            body: podaciForme
        })
        .then(function(response) {
            if (response.ok) {
                alert("Korisnik uspješno registriran");
                window.location.href = "${pageContext.request.contextPath}";
            } else {
                throw new Error("Dogodila se greška prilikom registracije korisnika");
            }
        })
        .catch(function(error) {
            console.log("Dogodila se greška prilikom registracije korisnika: ", error);
        });
    });
</script>
</body>
</html>