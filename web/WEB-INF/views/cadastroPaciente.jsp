<jsp:include page="includes/header.jsp"></jsp:include>
    <div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Cadastrar Paciente</h3>
                </div>
                <div class="panel-body">


                    <form action="pacienteController" method="POST"  style="margin: 0% 0;">
                        <label>Nome Completo</label>
                        <input class="form-control" type="text" name="nome" placeholder="Nome Completo" />
                        <label>Endereço</label>
                        <input class="form-control" type="text" name="endereco" placeholder="Endereço" />
                        <div class="row">
                            <div class="col-md-6">
                                <label>Bairro</label>
                                <input class="form-control" type="text" name="bairro" placeholder="Bairro" />
                            </div>
                            <div class="col-md-4">
                                <label>Complemeto</label>
                                <input class="form-control" type="text" name="complemeto" placeholder="Complemento" />
                            </div>
                            <div class="col-md-2">
                                <label>Número</label>
                                <input class="form-control" type="text" name="numero" placeholder="Número" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-10">
                                <label>Cidade</label>
                                <input class="form-control" type="text" name="cidade" placeholder="Cidade" />
                            </div>
                            <div class="col-md-2">
                                <label>Estado</label>
                                <select class="form-control" name="estados-brasil">
                                    <option value="AC">Acre</option>
                                    <option value="AL">Alagoas</option>
                                    <option value="AP">Amapá</option>
                                    <option value="AM">Amazonas</option>
                                    <option value="BA">Bahia</option>
                                    <option value="CE">Ceará</option>
                                    <option value="DF">Distrito Federal</option>
                                    <option value="ES">Espírito Santo</option>
                                    <option value="GO">Goiás</option>
                                    <option value="MA">Maranhão</option>
                                    <option value="MT">Mato Grosso</option>
                                    <option value="MS">Mato Grosso do Sul</option>
                                    <option value="MG">Minas Gerais</option>
                                    <option value="PA">Pará</option>
                                    <option value="PB">Paraíba</option>
                                    <option value="PR">Paraná</option>
                                    <option value="PE">Pernambuco</option>
                                    <option value="PI">Piauí</option>
                                    <option value="RJ">Rio de Janeiro</option>
                                    <option value="RN">Rio Grande do Norte</option>
                                    <option value="RS">Rio Grande do Sul</option>
                                    <option value="RO">Rondônia</option>
                                    <option value="RR">Roraima</option>
                                    <option value="SC">Santa Catarina</option>
                                    <option value="SP">São Paulo</option>
                                    <option value="SE">Sergipe</option>
                                    <option value="TO">Tocantins</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <label>CPF</label>
                                <input class="form-control" type="text" name="cpf" placeholder="CPF" />
                            </div>
                            <div class="col-md-4">
                                <label>RG</label>
                                <input class="form-control" type="text" name="rg" placeholder="RG" />
                            </div>
                            <div class="col-md-4">
                                <label>Orgão Expedidor</label>
                                <input class="form-control" type="text" name="org_expedidor" placeholder="Orgão Expedidor" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <label>Cartão SUS</label>
                                <input class="form-control" type="text" name="cartao_sus" placeholder="Número do SUS" />
                            </div>
                            <div class="col-md-4">
                                <label>Convênio</label>
                                <input class="form-control" type="text" name="convenio" placeholder="Convênio" />
                            </div>
                            <div class="col-md-4">
                                <label>Nascimento</label>
                                <input class="form-control" type="date" name="dataNascimento" placeholder="Data de Nascimento" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <label>Telefone</label>
                                <input class="form-control" type="tel" name="telefoen" placeholder="Telefone" />
                            </div>
                            <div class="col-md-4">
                                <label>Celular</label>
                                <input class="form-control" type="tel" name="celular" placeholder="Celular" />
                            </div>
                            <div class="col-md-4">
                                <label>Email</label>
                                <input class="form-control" type="email" name="email" placeholder="Email" />
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
