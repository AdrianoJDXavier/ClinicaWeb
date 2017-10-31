<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp"></jsp:include>
    <div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Marcar Consulta</h3>
                </div>
                <div class="panel-body">


                    <form action="" method="POST"  style="margin: 10% 0;">
                        <div class="row">
                            <div class="col-md-4">
                                <label>Data da Consulta</label>
                                <input class="form-control" type="date" name="dataConsulta"/>
                            </div>
                            <div class="col-md-4">
                                <label>Hora</label>
                                <input class="form-control" type="time" name="horaConsulta"/>
                            </div>
                            <div class="col-md-4">
                                <label>Confirmação</label>
                                <select class="form-control" name="agenda" />
                                <option value="0">Cancelado</option>
                                <option value="1">Confirmado</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label>Médico</label>
                                <select class="form-control" name="medico">
                                <c:forEach var="medico" items="${medico}">
                                    <option value="${medico.idmedico}">${medico.nomemedico}</option>
                                </c:forEach>
                            </select>    
                        </div>
                        <div class="col-md-6">
                            <label>Paciente</label>
                            <select class="form-control" name="paciente">
                                <c:forEach var="paciente" items="${paciente}">
                                    <option value="${paciente.idpaciente}">${paciente.nomepaciente}</option>
                                </c:forEach>
                            </select>    
                        </div>
                        </div>
                        <div class="col-sm-offset-2 col-sm-10">
                            <button type="submit" class="btn btn-primary btn-lg pull-right"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar</button>
                        </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="includes/menu.jsp"></jsp:include>
    <jsp:include page="includes/footer.jsp"></jsp:include>
