<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<jsp:include page="includes/header.jsp"></jsp:include>
    <div class="row">
        <div class="main col-md-10 col-md-push-2">

            <div class="panel panel-default" style="margin:10px;">
                <div class="panel-heading">
                    <h3 class="panel-title">Editar Paciente</h3>
                </div>
                <div class="panel-body">


                    <form action="" method="POST"  style="margin: 0% 0;">
                        <label>Nome Completo</label>
                        <input class="form-control" type="text" name="nomePaciente" value="${paciente.nomepaciente}" />
                        <div class="row">
                            <div class="col-md-3">
                                <label>Tipo de Endereço</label>
                                <select class="form-control" name="tipo_endereço">
                                    <option value="AEROPORTO"></option>
                                    <option value="ALAMEDA">ALAMEDA</option>
                                    <option value="APARTAMENTO">APARTAMENTO</option>
                                    <option value="AVENIDA">AVENIDA</option>
                                    <option value="BECO">BECO</option>
                                    <option value="BLOCO">BLOCO</option>
                                    <option value="CAMINHO">CAMINHO</option>
                                    <option value="ESCADINHA">ESCADINHA</option>
                                    <option value="ESTACAO">ESTAÇÃO</option>
                                    <option value="ESTRADA">ESTRADA</option>
                                    <option value="FAZENDA">FAZENDA</option>
                                    <option value="FORTALEZA">FORTALEZA</option>
                                    <option value="GALERIA">GALERIA</option>
                                    <option value="LADEIRA">LADEIRA</option>
                                    <option value="LARGO">LARGO</option>
                                    <option value="PRACA">PRAÇA</option>
                                    <option value="PARQUE">PARQUE</option>
                                    <option value="PRAIA">PRAIA</option>
                                    <option value="QUADRA">QUADRA</option>
                                    <option value="QUILOMETRO">QUILÔMETRO</option>
                                    <option value="QUINTA">QUINTA</option>
                                    <option value="RODOVIA">RODOVIA</option>
                                    <option value="RUA">RUA</option>
                                    <option value="SUPER QUADRA">SUPER QUADRA</option>
                                    <option value="TRAVESSIA">TRAVESSA</option>
                                    <option value="VIADUTO">VIADUTO</option>
                                    <option value="VILA">VILA</option>
                                </select>
                            </div>
                            <div class="col-md-6">
                                <label>Endereço</label>
                                <input class="form-control" type="text" name="endereco" value="${paciente.endereco.nomeNogradouro}" />
                            </div>
                            <div class="col-md-3">
                                <label>cep</label>
                                <input class="form-control" type="text" name="cep" value="${paciente.endereco.cep}" />
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-4">
                                <label>Bairro</label>
                                <input class="form-control" type="text" name="bairro" value="${paciente.endereco.bairro}" />
                            </div>
                            <div class="col-md-4">
                                <label>Complemeto</label>
                                <input class="form-control" type="text" name="complemeto" value="${paciente.endereco.complemento}" />
                            </div>
                            <div class="col-md-2">
                                <label>Número</label>
                                <input class="form-control" type="text" name="numero" value="${paciente.endereco.numero}" />
                            </div>
                            <div class="col-md-2">
                                <label>Sexo</label>
                                <select class="form-control" name="sexo">
                                    <option value="M">Masculino</option>
                                    <option value="F">Feminino</option>
                                </select>
                            </div>
                        </div>
                        <div class="row">
                            <div class="col-md-10">
                                <label>Cidade</label>
                                <select class="form-control" name="cidade">
                                <c:forEach var="cidade" items="${cidades}">
                                    <option value="${cidade.idcidade}">${cidade.cidade}</option>
                                </c:forEach>   
                            </select>
                        </div>
                        <div class="col-md-2">
                            <label>Estado</label>
                            <select class="form-control" name="estados-brasil">
                                <c:forEach var="estado" items="${estados}">
                                    <option value="${estado.sigla}">${estado.estado}</option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <label>CPF</label>
                            <input class="form-control" type="text" name="cpf" value="${paciente.cpf}" />
                        </div>
                        <div class="col-md-4">
                            <label>Tipo Sanguineo</label>
                            <select class="form-control" name="tipoSanguineo">
                                <option value="A">A</option>
                                <option value="B">B</option>
                                <option value="AB">AB</option>
                                <option value="O">O</option>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label>Fator RH</label>
                            <select class="form-control" name="fatorRH">
                                <option value="+">Positivo</option>
                                <option value="-">Negativo</option>
                            </select>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <label>Cartão Convênio</label>
                            <input class="form-control" type="text" name="cartaoConvenio" value="${paciente.cartaoconvenio}" />
                        </div>
                        <div class="col-md-4">
                            <label>Convênio</label>
                            <select class="form-control" name="convenio">
                                <c:forEach var="convenio" items="${convenios}">
                                    <option value="${convenio.idconvenio}">${convenio.empresaConvenio}</option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="col-md-4">
                            <label>Nascimento</label>
                            <input class="form-control" type="date" name="dataNascimento" value="${paciente.datanascimento}" readonly="true"/>
                        </div>
                    </div>
                    <div class="row">
                        <div class="col-md-4">
                            <label>Telefone</label>
                            <input class="form-control" type="tel" name="telefone" value="${paciente.telefone}" />
                        </div>
                        <div class="col-md-4">
                            <label>Celular</label>
                            <input class="form-control" type="tel" name="celular" value="${paciente.celular}" />
                        </div>
                        <div class="col-md-4">
                            <label>Email</label>
                            <input class="form-control" type="email" name="email" value="${paciente.email}" />
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
