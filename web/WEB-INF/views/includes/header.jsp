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
                    <div class="navbar-header">
                        <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#defaultNavbar1">
                            <span class="sr-only">Toggle navigation</span><span class="icon-bar"></span><span class="icon-bar"></span>
                            <span class="icon-bar"></span></button>
                        <div class="collapse navbar-collapse" id="defaultNavbar1">
                            <ul class="nav navbar-nav">
                                <li></li>
                            </ul>
                            <ul class="nav navbar-nav">
                                <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false" style="color: #e3e3e3;">Médicos<span class="caret"></span></a>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="#">Cadastrar Médico</a></li>
                                        <li class="divider"></li>
                                        <li><a href="#">Listar Médicos</a></li>
                                    </ul>
                                </li>
                                <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false" style="color: #e3e3e3;">Pacientes<span class="caret"></span></a>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="cadPaciente">Cadastrar Paciente</a></li>
                                        <li class="divider"></li>
                                        <li><a href="#">Listar Paciente</a></li>
                                    </ul>
                                </li>
                                <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false" style="color: #e3e3e3;">Exames<span class="caret"></span></a>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="#">Marcar Exame</a></li>
                                        <li class="divider"></li>
                                        <li><a href="#">Listar Exames</a></li>
                                        <li class="divider"></li>
                                    </ul>
                                </li>
                                <li class="dropdown"><a class="dropdown-toggle" data-toggle="dropdown" role="button" aria-expanded="false" style="color: #e3e3e3;">Consultas<span class="caret"></span></a>
                                    <ul class="dropdown-menu" role="menu">
                                        <li><a href="#">Marcar Consulta</a></li>
                                        <li class="divider"></li>
                                        <li><a href="#">Listar Consultas</a></li>
                                        <li class="divider"></li>
                                    </ul>
                                </li>
                            </ul>
                        </div>
                    </div>
                </div>
            </header>