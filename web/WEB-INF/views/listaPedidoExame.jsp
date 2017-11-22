<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp"></jsp:include>
<div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Lista de Exames</h3>
                </div>
                <div class="panel-body">

                    <div class="table-responsive"  style="height: 500px">
                        <table class="table table-striped" id="tabela">
                            <thead>
                                <tr>
                                    <th>Exame</th>
                                    <th>Paciente</th>
                                    <th>Data</th>
                                    <th></th>
                                </tr>
                                <tr>
                                    <th><input class="form-control" type="text" id="txtColuna1" style="width: 100px" placeholder="Exame!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna2" placeholder="Pesquise por Paciente!"/></th>
                                    <th><input class="form-control" type="text" id="txtColuna3" placeholder="Data do Exame!"/></th>
                                    <th><a href="criaPedidoExame.html"><button class="btn btn-info btn-block"><span class="glyphicon glyphicon-plus" aria-hidden="true"> Novo Exame</span></button></a></th>
                                </tr>
                            </thead>
                            <tbody style="text-align: left">
                            <c:forEach var="pedidoExame" items="${pedidoExames}">
                            <tr>
                                <td>${pedidoExame.exame.exame}</td>
                                <td>${pedidoExame.consulta.paciente.nomepaciente}</td>
                                <td><fmt:formatDate value="${pedidoExame.data}" type="date" dateStyle="long" pattern="dd/MM/yyyy" /> </td>
                                <td>
                                    <a href="excluiPedidoExame.html?idPedidoExame=${pedidoExame.idpedidoexame}"><button class="btn btn-danger"><span class="glyphicon glyphicon-remove" aria-hidden="true"> Excluir</span></button></a>
                                    <a href="editaPedidoExame.html?idPedidoExame=${pedidoExame.idpedidoexame}"><button class="btn btn-success"><span class="glyphicon glyphicon-check" aria-hidden="true"> Editar</span></button></a>
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