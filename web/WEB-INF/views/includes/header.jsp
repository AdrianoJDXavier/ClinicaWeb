<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
    <head>
        <meta charset="utf-8">
        <meta http-equiv="X-UA-Compatible" content="IE=edge">
        <meta name="viewport" content="width=device-width, initial-scale=1">

        <title>Asfecer</title>

        <!-- Bootstrap CSS -->
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/style.css" rel="stylesheet">
    </head>
    <body class="fundo">
        <div class="container">
            <header class="row navbar navbar-default">
                <div class="container-fluid"> 
                    <div class="row">
                        <div class="col-md-8">
                            <div class="navbar-header">
                                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#defaultNavbar1">
                                    <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span>
                                    <span class="icon-bar"></span></button>
                                <div class="collapse navbar-collapse" id="defaultNavbar1">
                                    <c:if test="${usuario == null }">
                                        <jsp:include page="moduloadministrativo.jsp"></jsp:include>
                                    </c:if>
                                    <c:if test="${(usuario.moduloadministrativo == '1' || usuario.moduloadmbd == '1') && (usuario.moduloacesso == '0') }">
                                        <jsp:include page="moduloadministrativo.jsp"></jsp:include>
                                    </c:if>
                                    <c:if test="${(usuario.moduloadministrativo == '1' || usuario.moduloadmbd == '1') && (usuario.moduloacesso == '1') }">
                                        <jsp:include page="moduloadministrativo.jsp"></jsp:include>
                                    </c:if>
                                    <c:if test="${usuario.moduloacesso == '1' && usuario.moduloadministrativo == '0' && usuario.moduloadmbd =='0'}">
                                        <jsp:include page="moduloacesso.jsp"></jsp:include>
                                    </c:if>
                                    <c:if test="${(usuario.moduloagendamento == '1' || usuario.moduloatendimento == '1') && (usuario.moduloadministrativo == '0' && usuario.moduloadmbd =='0')} ">
                                        <jsp:include page="moduloatendimento.jsp"></jsp:include>
                                    </c:if>
                                </div>
                            </div>
                        </div>
                        <div class="col-md-4 wrapper">
                            <div class="linha-vertical"></div>
                            <div style="color: white">Bem vindo: 
                                <c:if test="${usuario == null }">
                                    <strong>Administrador</strong>
                                </c:if>
                                <strong>${usuario.login}</strong></div>
                                &nbsp;<strong><a href="logoff.html" style="color: white">SAIR</a></strong>
                        </div>
                    </div>
                </div>
            </header>