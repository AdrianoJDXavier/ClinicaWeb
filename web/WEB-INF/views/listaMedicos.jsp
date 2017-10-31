<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp"></jsp:include>
<div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Lista de Médicos</h3>
                </div>
                <div class="panel-body">

                    <div class="table-responsive"  style="height: 500px">
                        <table class="table table-striped" id="tabela">
                            <thead>
                                <tr>
                                    <th>Id</th>
                                    <th>Médico</th>
                                    <th>Especialidade</th>
                                    <th></th>
                                </tr>
                                <tr>
                                    <th><input class="form-control" type="text" id="txtColuna1" style="width: 100px" placeholder="Id!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna2" placeholder="Pesquise por médico!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna3" placeholder="Pesquise por CRM!"/></th>
                                    <th><a href="criaMedico.html"><button class="btn btn-info btn-block"><span class="glyphicon glyphicon-plus" aria-hidden="true"> Adicionar Médico</span></button></a></th>
                                </tr>
                            </thead>
                            <tbody style="text-align: left">
                            <c:forEach var="medico" items="${medicos}">
                            <tr>
                                <td>${medico.idmedico}</td>
                                <td>${medico.nomemedico}</td>
                                <td>${medico.especialidade.descricao}</td>
                                <td>
                                    <a href="excluiMedico.html?idMedico=${medico.idmedico}"><button class="btn btn-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"> Excluir</span></button></a>
                                    <a href="editaMedico.html?idMedico=${medico.idmedico}"><button class="btn btn-success"><span class="glyphicon glyphicon-check" aria-hidden="true"> Editar</span></button></a>
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