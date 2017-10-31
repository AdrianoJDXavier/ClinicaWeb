<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp"></jsp:include>
    <div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Editar Médicos</h3>
                </div>
                <div class="panel-body">


                    <form action="" method="POST"  style="margin: 15% 0;">
                        <label>Nome do Médico</label>
                        <input class="form-control" type="text" name="nomeMedico" value="${medico.nomemedico}" />
                        <div class="row">
                            <div class="col-md-4">
                                <label>CRM</label>
                                <input class="form-control" type="number" name="crm" value="${medico.crm}" readonly="" />
                            </div>
                            <div class="col-md-4">
                                <label>Especialidade</label>
                                <input class="form-control" type="text" name="especialidade" value="${medico.especialidade.descricao}" />
                            </div>
                            <div class="col-md-4">
                                <label>UF do CRM</label>
                                <select class="form-control" name="estados-brasil">
                                <c:forEach var="estado" items="${estados}">
                                    <option value="${estado.sigla}">${estado.estado}</option>
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