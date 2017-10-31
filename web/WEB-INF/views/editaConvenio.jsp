<jsp:include page="includes/header.jsp"></jsp:include>
    <div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Editar Convenio</h3>
                </div>
                <div class="panel-body">


                    <form action="" method="Post"  style="margin: 10% 0;">
                        <div class="row">
                            <div class="col-md-6">
                                <label>Empresa de Convenio</label>
                                <input class="form-control" type="text" name="convenio" value="${convenio.empresaConvenio}"/>
                            </div>
                            <div class="col-md-6">
                                <label>Telefone</label>
                                <input class="form-control" type="tel" name="telefone" value="${convenio.telefone}"/>
                            </div>
                        </div>
                        <br>
                        <div class="row">
                            <div class="col-md-6">
                            <label>Status</label>
                                <select class="form-control" name="status">
                                    <option value="0">Não permitido</option>
                                    <option value="1">Permitido</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                            <label>Tipo de Convenio</label>
                                <select class="form-control" name="tipo_convenio">
                                    <option value="0">Particular</option>
                                    <option value="1">Público</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <label>Observações</label>
                            <textarea class="form-control" name="obs" rows="4" cols="20">
                            ${convenio.obs}
                            </textarea>
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
