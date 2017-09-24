<%-- 
    Document   : login
    Created on : 21/09/2017, 10:31:43
    Author     : Adriano Xavier
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<link rel="stylesheet" href="css/font-awesome.min.css">
<script src="js/telafull.js"></script>
<div class="main" onload="fullScren();">

    <div class="container">
        <center>
            <div class="middle">
                <div id="login">
                    <div style="color: red;"><strong>${erro}</strong></div>
                    <form action="login" method="get">

                        <fieldset class="clearfix">

                            <p ><span class="fa fa-user"></span><input type="text"  Placeholder="Digite seu nome" required name="login"></p>
                            <p><span class="fa fa-lock"></span><input type="password"  Placeholder="Digite a senha" required name="senha"></p>
                            <div>
                                <span style="width:90%; text-align:right;  display: inline-block;"><input type="submit" value="Entrar"></span>
                            </div>

                        </fieldset>
                        <div class="clearfix"></div>
                    </form>

                    <div class="clearfix"></div>

                </div> <!-- end login -->
                <div class="logo">
                    <img src="icones/logo.jpg" class="img-responsive img-circle" style="width: 150px; height: 150px; ">
                    <div class="clearfix"></div>
                </div>

            </div>
        </center>
    </div>

</div>