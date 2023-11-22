<%@page isErrorPage="true" contentType="text/html" pageEncoding="UTF-8"%>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Pogreška 500!</title>
    </head>
    <body>
        <h1>Pogreška 500!</h1>
        <h2>Detalji:</h2>
        <pre>
            <%= exception.getMessage() %>
        </pre>
    </body>
</html>
