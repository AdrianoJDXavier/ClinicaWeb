<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp"></jsp:include>
    <div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Marcar Exame</h3>
                </div>
                <div class="panel-body">


                    <form action="" method="POST"  style="margin: 10% 0;">
                        <div class="row">
                            <div class="col-md-4">
                                <label>Data do Pedido</label>
                                <input class="form-control" type="date" name="data"/>
                            </div>
                        </div>
                        <label>Consulta</label>
                        <select class="form-control" name="consulta"/>
                    <c:forEach var="consulta" items="${consultas}">
                        <option value="${consulta.idconsulta}">Paciente: ${consulta.paciente.nomepaciente}</option>
                    </c:forEach>           
                    </select> 
                    <label>Exame</label>
                    <select class="form-control" name="exame"/>
                    <c:forEach var="exame" items="${exames}">
                        <option value="${exame.idexame}">${exame.exame}</option>
                    </c:forEach>           
                    </select> 
                    <div class="col-sm-offset-2 col-sm-10">
                        <button type="submit" class="btn btn-primary btn-lg pull-right"><span class="glyphicon glyphicon-floppy-save" aria-hidden="true"></span> Salvar</button>
                    </div>
                </form>
            </div>
        </div>
    </div>

    <jsp:include page="includes/menu.jsp"></jsp:include>
    <jsp:include page="includes/footer.jsp"></jsp:include>