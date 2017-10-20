<jsp:include page="includes/header.jsp"></jsp:include>
<div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Lista de Pacientes</h3>
                </div>
                <div class="panel-body">

                    <div class="table-responsive"  style="height: 500px">
                        <table class="table table-striped" id="tabela">
                            <thead>
                                <tr>
                                    <th>Id</th>
                                    <th>Nome</th>
                                    <th>Cidade</th>
                                    <th></th>
                                </tr>
                                <tr>
                                    <th><input class="form-control" type="text" id="txtColuna1" style="width: 100px" placeholder="Id!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna2" placeholder="Pesquise por nome!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna3" placeholder="Pesquise por cidade!"/></th>
				</tr>
                            </thead>
                            <tbody style="text-align: left">
                            <c:forEach var="paciente" items="${pacientes}">
                            <tr>
                                <td>${paciente.idPaciente}</td>
                                <td>${paciente.nomePaciente}</td>
                                <td></td>
                                <td>
                                    <button class="btn btn-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"> Excluir</span></button>
                                    <button class="btn btn-success"><span class="glyphicon glyphicon-check" aria-hidden="true"> Editar</span></button>
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