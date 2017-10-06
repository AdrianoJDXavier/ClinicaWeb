<%@ page isErrorPage="true" %> 
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<head>
    <title>Ops - Erro na aplicação</title>
    <meta charset="UTF-8">
    <style>
        body{
            margin: 0;
            padding: 0;
            background: #e7ecf0;
            font-family: Arial, Helvetica, sans-serif;
        }
        *{
            margin: 0;
            padding: 0;
        }
        p{
            font-size: 14px;
            color: #373737;
            font-family: Arial, Helvetica, sans-serif;
            line-height: 18px;
        }
        p a{
            color: #D71920;
            font-size: 14px;
            text-decoration: none;
        }
        a{
            outline: none;
        }
        .f-left{
            float:left;
        }
        .f-right{
            float:right;
        }
        .clear{
            clear: both;
            overflow: hidden;
        }
        #block_error{
            width: 844px;
            height: 366px;
            border: 1px solid #cccccc;
            margin: 89px auto 0;
            -moz-border-radius: 4px;
            -webkit-border-radius: 4px;
            border-radius: 4px;
            background: #fff ;
        }
        #block_error div{
            padding: 102px 43px 0 244px;
        }
        #block_error div h2{
            color: #D71920;
            font-size: 24px;
            display: block;
            padding: 0 0 14px 0;
            border-bottom: 1px solid #cccccc;
            margin-bottom: 12px;
            font-weight: normal;
        }
    </style>
    <title>Ops - Erro na aplicão</title>
</head>
<body marginwidth="0" marginheight="0">
    <div id="block_error">
        <div>
            <h2>Erro - &nbspOps Aconteceu um erro!</h2>

            <p>
                - Parece que alguma coisa deu errado ou a página não existe mais.<br /><br />
            </p>
            <p>
                <strong><a>${erro}</a></strong> 
            </p>

            <br />
            <p>Voltar a <a href="javascript:window.history.go(-1)">página anterior.</a </p>
        </div>
    </div>
</body>

</html>