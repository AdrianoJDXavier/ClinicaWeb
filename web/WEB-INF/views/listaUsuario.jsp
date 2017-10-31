<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp"></jsp:include>
    <div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Lista de Usuários</h3>
                </div>
                <div class="panel-body">

                    <div class="table-responsive"  style="height: 500px">
                        <table class="table table-striped" id="tabela">
                            <thead>
                                <tr>
                                    <th>Id</th>
                                    <th>Nome</th>
                                    <th>Tipo de Usuário</th>
                                    <th></th>
                                </tr>
                                <tr>
                                    <th><input class="form-control" type="text" id="txtColuna1" style="width: 100px" placeholder="Id!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna2" placeholder="Pesquise por nome!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna3" placeholder="Pesquise por cidade!"/></th>
                                    <th><a href="criaUsuario.html"><button class="btn btn-info btn-block"><span class="glyphicon glyphicon-plus" aria-hidden="true"> Adicionar Usuário</span></button></a></th>
                                </tr>
                            </thead>
                            <tbody style="text-align: left">
                            <c:forEach var="usuario" items="${usuarios}">
                                <tr>
                                    <td>${usuario.idusuario}</td>
                                    <td>${usuario.login}</td>
                                    <td>${usuario.tipousuario}</td>
                                    <td>
                                        <a href="excluiUsuario.html?idUsuario=${usuario.idusuario}"><button class="btn btn-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"> Excluir</span></button></a>
                                        <a href="editaUsuario.html?idUsuario=${usuario.idusuario}"><button class="btn btn-success"><span class="glyphicon glyphicon-check" aria-hidden="true"> Editar</span></button></a>
                                    </td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>

            </div>
        </div>
    </div>


    <jsp:include page="includes/menu.jsp"></jsp:include>
    <jsp:include page="includes/footer.jsp"></jsp:include>