<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <title>TO DO Dinâmico</title>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <style type="text/css">
            .centro {
                margin: 0 auto;
                width: 480px;
            }
        </style>
    </head>
    <body>
        <div class="centro">
            <h1>TO DO Dinâmico</h1>
            <hr />
            <h5>Desculpe... ocorreu um erro.</h5>
            <p style="color: red">${erro}</p>
        </div>        
    </body>
</html>