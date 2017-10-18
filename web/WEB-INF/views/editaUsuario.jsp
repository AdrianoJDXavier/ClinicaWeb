<jsp:include page="includes/header.jsp"></jsp:include>
    <div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Cadastrar Usuário</h3>
                </div>
                <div class="panel-body">


                    <form action="" method="Post"  style="margin: 0% 0;">
                        <div class="row">
                            <div class="col-md-6">
                                <label>Editar Usuario</label>
                                <select class="form-control" name="tipoUsuario">
                                    <option value="Atendente">Atendente</option>
                                    <option value="Medico">Médico</option>
                                    <option value="Paciente">Paciente</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label>Status</label>
                                <select class="form-control" name="status">
                                    <option value="1" selected="${usuario.status == '1' ? "selected" : "" }">Ativo</option>
                                    <option value="0" selected="${usuario.status == '0' ? "selected" : "" }">Inativo</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                                <label>Login</label>
                                <input class="form-control" type="text" name="login" value="${usuario.login}"/>
                            </div>
                            <div class="col-md-6">
                                <label>Senha</label>
                                <input class="form-control" type="text" name="senha" value="${usuario.senha}"/>
                            </div>
                        </div>
                        <br>
                        <div class="panel panel-default">
                            <div class="panel-heading">
                            <h2 class="panel-title">Módulos de Acesso</h2>
                            </div>
                        <div class="row">
                            <div class="col-md-6">
                            <label>Módulo de Administrativo</label>
                                <select class="form-control" name="moduloAdministrativo">
                                    <option value="0" selected="${usuario.moduloAdministrativo == '0' ? "selected" : "" }">Não permitido</option>
                                    <option value="1" selected="${usuario.moduloAdministrativo == '1' ? "selected" : "" }">Permitido</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                            <label>Módulo de Agendamento</label>
                                <select class="form-control" name="moduloAgendamento">
                                    <option value="0" selected="${usuario.moduloAgendamento == '0' ? "selected" : "" }">Não permitido</option>
                                    <option value="1" selected="${usuario.moduloAgendamento == '1' ? "selected" : "" }">Permitido</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                            <label>Módulo de Atendimento</label>
                                <select class="form-control" name="moduloAtendimento">
                                    <option value="0" selected="${usuario.moduloAtendimento == '0' ? "selected" : "" }">Não permitido</option>
                                    <option value="1" selected="${usuario.moduloAtendimento == '1' ? "selected" : "" }">Permitido</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                            <label>Módulo de Acesso</label>
                                <select class="form-control" name="moduloAcesso">
                                    <option value="0" selected="${usuario.moduloAcesso == "0" ? "selected" : "" }">Não permitido</option>
                                    <option value="1" selected="${usuario.moduloAcesso == "1" ? "selected" : "" }">Permitido</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-6">
                            <label>Módulo de Adimistração de </label>
                                <select class="form-control" name="moduloAdmBD">
                                    <option value="0" selected="${usuario.moduloAdmBD == '0' ? "selected" : "" }">Não permitido</option>
                                    <option value="1" selected="${usuario.moduloAdmBD == '1' ? "selected" : "" }">Permitido</option>
                                </select>
                            </div>
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
