<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp"></jsp:include>
<div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Lista de Convênios</h3>
                </div>
                <div class="panel-body">

                    <div class="table-responsive"  style="height: 500px">
                        <table class="table table-striped" id="tabela">
                            <thead>
                                <tr>
                                    <th>Id</th>
                                    <th>Convenio</th>
                                    <th>Tipo de Convenio</th>
                                    <th></th>
                                </tr>
                                <tr>
                                    <th><input class="form-control" type="text" id="txtColuna1" style="width: 100px" placeholder="Id!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna2" placeholder="Pesquise por nome!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna3" placeholder="Pesquise por tipo de convênio!"/></th>
                                    <th><a href="criaConvenio.html"><button class="btn btn-info btn-block"><span class="glyphicon glyphicon-plus" aria-hidden="true"> Adicionar Convênio</span></button></a></th>
                                </tr>
                            </thead>
                            <tbody style="text-align: left">
                            <c:forEach var="convenio" items="${convenios}">
                            <tr>
                                <td>${convenio.idconvenio}</td>
                                <td>${convenio.empresaConvenio}</td>
                                <td>${convenio.tipoConvenio}</td>
                                <td>
                                    <a href="excluiConvenio.html?idConvenio=${convenio.idconvenio}"><button class="btn btn-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"> Excluir</span></button></a>
                                    <a href="editaConvenio.html?idConvenio=${convenio.idconvenio}"><button class="btn btn-success"><span class="glyphicon glyphicon-check" aria-hidden="true"> Editar</span></button></a>
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