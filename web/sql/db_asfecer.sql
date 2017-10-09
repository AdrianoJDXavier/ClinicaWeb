-- MySQL Workbench Synchronization
-- Generated: 2017-10-04 07:35
-- Model: New Model
-- Version: 1.0
-- Project: Name of the project
-- Author: Paulo

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Funcionario` (
  `idFuncionario` INT(11) NOT NULL AUTO_INCREMENT,
  `nomeFuncionario` VARCHAR(100) NOT NULL,
  `dataNascimento` DATE NULL DEFAULT NULL,
  `cpf` VARCHAR(14) NULL DEFAULT NULL,
  `rg` VARCHAR(15) NULL DEFAULT NULL,
  `orgaoEmissor` VARCHAR(10) NULL DEFAULT NULL,
  `UfEmissor` CHAR(2) NOT NULL,
  `ctps` VARCHAR(20) NULL DEFAULT NULL,
  `pis` VARCHAR(20) NULL DEFAULT NULL,
  `Cargo` INT(11) NOT NULL,
  `Endereco` INT(11) NULL DEFAULT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `telefone` VARCHAR(20) NULL DEFAULT NULL,
  `celular` VARCHAR(20) NULL DEFAULT NULL,
  `obs` VARCHAR(300) NULL DEFAULT NULL,
  PRIMARY KEY (`idFuncionario`),
  INDEX `idx_Funcionario_Estados` (`UfEmissor` ASC),
  INDEX `idx_Funcionario_Cargo` (`Cargo` ASC),
  INDEX `fk_Funcionario_Endereco_idx` (`Endereco` ASC),
  CONSTRAINT `fk_Funcionario_UfEmissor`
    FOREIGN KEY (`UfEmissor`)
    REFERENCES `db_asfecer`.`Estados` (`sigla`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Funcionario_Cargo`
    FOREIGN KEY (`Cargo`)
    REFERENCES `db_asfecer`.`Cargo` (`idCargo`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Funcionario_Endereco`
    FOREIGN KEY (`Endereco`)
    REFERENCES `db_asfecer`.`Endereco` (`idEndereco`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Paciente` (
  `idPaciente` INT(11) NOT NULL AUTO_INCREMENT,
  `nomePaciente` VARCHAR(100) NOT NULL,
  `dataNascimento` DATE NULL DEFAULT NULL,
  `nomeMae` VARCHAR(100) NULL DEFAULT NULL,
  `NaturalidadeCidade` INT(11) NULL DEFAULT NULL,
  `cpf` VARCHAR(14) NULL DEFAULT NULL,
  `cartaoConvenio` VARCHAR(30) NULL DEFAULT NULL,
  `Convenio` INT(11) NOT NULL,
  `tipoSanguineo` VARCHAR(2) NULL DEFAULT NULL,
  `fatorRH` CHAR(1) NULL DEFAULT NULL COMMENT '+\n-',
  `sexo` CHAR(1) NULL DEFAULT NULL COMMENT 'M\nF',
  `Endereco` INT(11) NULL DEFAULT NULL,
  `email` VARCHAR(100) NULL DEFAULT NULL,
  `telefone` VARCHAR(20) NULL DEFAULT NULL,
  `celular` VARCHAR(20) NULL DEFAULT NULL,
  `obs` VARCHAR(300) NULL DEFAULT NULL,
  PRIMARY KEY (`idPaciente`),
  UNIQUE INDEX `Idx_Paciente_cpf` (`cpf` ASC),
  UNIQUE INDEX `Idx_Paciente_cConv` (`cartaoConvenio` ASC),
  INDEX `Idx_Paciente_nome` (`nomePaciente` ASC),
  INDEX `Idx_Paciente_Conv` (`Convenio` ASC),
  INDEX `fk_Paciente_Endereco_idx` (`Endereco` ASC),
  INDEX `fk_Paciente_Nat_idx` (`NaturalidadeCidade` ASC),
  CONSTRAINT `fk_Paciente_Endereco`
    FOREIGN KEY (`Endereco`)
    REFERENCES `db_asfecer`.`Endereco` (`idEndereco`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Paciente_Conv`
    FOREIGN KEY (`Convenio`)
    REFERENCES `db_asfecer`.`Convenio` (`idConvenio`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Paciente_Nat`
    FOREIGN KEY (`NaturalidadeCidade`)
    REFERENCES `db_asfecer`.`Cidade` (`idCidade`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Estados` (
  `sigla` CHAR(2) NOT NULL,
  `estado` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`sigla`),
  UNIQUE INDEX `sigla_UNIQUE` (`sigla` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Convenio` (
  `idConvenio` INT(11) NOT NULL AUTO_INCREMENT,
  `empresa_convenio` VARCHAR(60) NOT NULL,
  `tipo_convenio` VARCHAR(30) NULL DEFAULT NULL,
  `telefone` VARCHAR(20) NULL DEFAULT NULL,
  `status` BIT(1) NULL DEFAULT NULL COMMENT 'Ativo\nInativo',
  `obs` VARCHAR(300) NULL DEFAULT NULL,
  PRIMARY KEY (`idConvenio`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Usuario` (
  `idUsuario` INT(11) NOT NULL AUTO_INCREMENT,
  `TipoUsuario` VARCHAR(20) NOT NULL COMMENT 'Paciente\nMedico\nAtendente',
  `status` BIT(1) NOT NULL COMMENT 'Ativo\nInativo\n',
  `login` VARCHAR(30) NOT NULL,
  `senha` VARCHAR(12) NOT NULL,
  `moduloAdministrativo` BIT(1) NOT NULL,
  `moduloAgendamento` BIT(1) NOT NULL,
  `moduloAtendimento` BIT(1) NOT NULL,
  `moduloAcesso` BIT(1) NOT NULL,
  `moduloAdmBD` BIT(1) NOT NULL,
  PRIMARY KEY (`idUsuario`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Especialidade` (
  `idEspecialidade` INT(11) NOT NULL AUTO_INCREMENT,
  `descricao` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idEspecialidade`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Medico` (
  `idMedico` INT(11) NOT NULL AUTO_INCREMENT,
  `nomeMedico` VARCHAR(100) NOT NULL,
  `crm` INT(11) NOT NULL,
  `UfCrm` CHAR(2) NOT NULL,
  `Especialidade` INT(11) NOT NULL,
  PRIMARY KEY (`idMedico`),
  INDEX `Idx_Medico_Esp` (`Especialidade` ASC),
  INDEX `Idx_Medico_Ufcrm` (`UfCrm` ASC),
  CONSTRAINT `fk_Medico_Esp`
    FOREIGN KEY (`Especialidade`)
    REFERENCES `db_asfecer`.`Especialidade` (`idEspecialidade`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Medico_Uf`
    FOREIGN KEY (`UfCrm`)
    REFERENCES `db_asfecer`.`Estados` (`sigla`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Agenda` (
  `registroAgenda` INT(11) NOT NULL AUTO_INCREMENT,
  `Usuario` INT(11) NOT NULL,
  `Medico` INT(11) NOT NULL,
  `Paciente` INT(11) NOT NULL,
  `data` DATE NOT NULL,
  `hora` TIME NOT NULL,
  `retorno` BIT(1) NOT NULL DEFAULT 0,
  `cancelado` BIT(1) NOT NULL DEFAULT 0,
  `motivoCancelamento` VARCHAR(150) NULL DEFAULT NULL,
  `status` BIT(1) NOT NULL COMMENT 'Agendado\nAguardando\nEm Atendimento\nAtendido\nCancelado',
  PRIMARY KEY (`registroAgenda`),
  INDEX `idx_Agenda_Usuario` (`Usuario` ASC),
  INDEX `idx_Agenda_Paciente` (`Paciente` ASC),
  INDEX `idx_Agenda_HorMedico` (`Medico` ASC),
  CONSTRAINT `fk_Agenda_Usuario`
    FOREIGN KEY (`Usuario`)
    REFERENCES `db_asfecer`.`Usuario` (`idUsuario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Agenda_Paciente`
    FOREIGN KEY (`Paciente`)
    REFERENCES `db_asfecer`.`Paciente` (`idPaciente`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Agenda_HorMedico`
    FOREIGN KEY (`Medico`)
    REFERENCES `db_asfecer`.`Horario` (`Medico`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Consulta` (
  `idConsulta` INT(11) NOT NULL AUTO_INCREMENT,
  `Agenda` INT(11) NULL DEFAULT NULL,
  `Medico` INT(11) NOT NULL,
  `Paciente` INT(11) NOT NULL,
  `dataConsulta` DATE NOT NULL,
  `horaConsulta` TIME NOT NULL,
  PRIMARY KEY (`idConsulta`),
  INDEX `idx_Consulta_Pac` (`Paciente` ASC),
  INDEX `idx_Consulta_Med` (`Medico` ASC),
  INDEX `idx_Consulta_Ag` (`Agenda` ASC),
  CONSTRAINT `fk_Consulta_Pac`
    FOREIGN KEY (`Paciente`)
    REFERENCES `db_asfecer`.`Paciente` (`idPaciente`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Consulta_Med`
    FOREIGN KEY (`Medico`)
    REFERENCES `db_asfecer`.`Medico` (`idMedico`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Consulta_Ag`
    FOREIGN KEY (`Agenda`)
    REFERENCES `db_asfecer`.`Agenda` (`registroAgenda`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Atestado` (
  `idAtestado` INT(11) NOT NULL AUTO_INCREMENT,
  `Consulta` INT(11) NOT NULL,
  `TipoAtestado` INT(11) NOT NULL,
  `dataAtestado` DATE NOT NULL,
  PRIMARY KEY (`idAtestado`),
  INDEX `idx_Atestado_Consulta` (`Consulta` ASC),
  INDEX `idx_Atestado_TipoAtestado` (`TipoAtestado` ASC),
  CONSTRAINT `fk_Atestado_Consulta`
    FOREIGN KEY (`Consulta`)
    REFERENCES `db_asfecer`.`Consulta` (`idConsulta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Atestado_TipoAtestado`
    FOREIGN KEY (`TipoAtestado`)
    REFERENCES `db_asfecer`.`TipoAtestado` (`idTipoAtestado`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Patologia` (
  `idPatologia` INT(11) NOT NULL AUTO_INCREMENT,
  `patologia` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`idPatologia`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Medicamento` (
  `idMedicamento` INT(11) NOT NULL AUTO_INCREMENT,
  `principioAtivo` VARCHAR(60) NULL DEFAULT NULL,
  `medicamento` VARCHAR(100) NOT NULL,
  `laboratorio` VARCHAR(45) NULL DEFAULT NULL,
  `apresentacao` VARCHAR(200) NULL DEFAULT NULL,
  PRIMARY KEY (`idMedicamento`),
  INDEX `idx_Medicamento_PAtivo` (`principioAtivo` ASC),
  INDEX `idx_Medicamento_Medicamento` (`medicamento` ASC))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`TipoAtestado` (
  `idTipoAtestado` INT(11) NOT NULL,
  `descricao` VARCHAR(45) NOT NULL,
  `localAfastamento` VARCHAR(45) NULL DEFAULT NULL,
  `dias` INT(11) NULL DEFAULT NULL,
  `atividade` VARCHAR(45) NULL DEFAULT NULL,
  `Patologia` INT(11) NULL DEFAULT NULL,
  `Medicamento` INT(11) NULL DEFAULT NULL,
  PRIMARY KEY (`idTipoAtestado`),
  INDEX `idx_TipoAtestado_Patologia` (`Patologia` ASC),
  INDEX `idx_TipoAtestado_Medicamento` (`Medicamento` ASC),
  CONSTRAINT `fk_TipoAtestado_Patologia`
    FOREIGN KEY (`Patologia`)
    REFERENCES `db_asfecer`.`Patologia` (`idPatologia`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_TipoAtestado_Medicamento`
    FOREIGN KEY (`Medicamento`)
    REFERENCES `db_asfecer`.`Medicamento` (`idMedicamento`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Receituario` (
  `idReceituario` INT(11) NOT NULL AUTO_INCREMENT,
  `Consulta` INT(11) NOT NULL,
  `TipoReceituario` INT(11) NOT NULL,
  `data` DATE NOT NULL,
  PRIMARY KEY (`idReceituario`),
  INDEX `idx_Receituario_Consulta` (`Consulta` ASC),
  INDEX `idx_Receituario_TipoReceituario` (`TipoReceituario` ASC),
  CONSTRAINT `fk_Receituario_Consulta`
    FOREIGN KEY (`Consulta`)
    REFERENCES `db_asfecer`.`Consulta` (`idConsulta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_Receituario_TipoReceituario`
    FOREIGN KEY (`TipoReceituario`)
    REFERENCES `db_asfecer`.`ItensReceituario` (`idItensReceituario`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`ItensReceituario` (
  `idItensReceituario` INT(11) NOT NULL AUTO_INCREMENT,
  `ordem` INT(11) NOT NULL,
  `Medicamento` INT(11) NOT NULL,
  `quantidade` INT(11) NOT NULL,
  `posologia` VARCHAR(200) NOT NULL,
  `dose` VARCHAR(45) NOT NULL,
  `TipoUso` BIT(1) NOT NULL COMMENT 'Interno\nExterno',
  PRIMARY KEY (`idItensReceituario`),
  INDEX `idx_ItensReceituario_Medicamento` (`Medicamento` ASC),
  CONSTRAINT `fk_ItensReceituario_Medicamento`
    FOREIGN KEY (`Medicamento`)
    REFERENCES `db_asfecer`.`Medicamento` (`idMedicamento`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`PedidoExame` (
  `idPedidoExame` INT(11) NOT NULL AUTO_INCREMENT,
  `Consulta` INT(11) NOT NULL,
  `Exame` INT(11) NOT NULL,
  `data` DATE NOT NULL,
  PRIMARY KEY (`idPedidoExame`),
  INDEX `idx_PedidoExame_Consulta` (`Consulta` ASC),
  INDEX `idx_PedidoExame_Exame` (`Exame` ASC),
  CONSTRAINT `fk_PedidoExame_Consulta`
    FOREIGN KEY (`Consulta`)
    REFERENCES `db_asfecer`.`Consulta` (`idConsulta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_PedidoExame_Exame`
    FOREIGN KEY (`Exame`)
    REFERENCES `db_asfecer`.`Exame` (`idExame`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Exame` (
  `idExame` INT(11) NOT NULL AUTO_INCREMENT,
  `TipoExame` INT(11) NOT NULL,
  `exame` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idExame`),
  INDEX `idx_Exame_TipoExame` (`TipoExame` ASC),
  CONSTRAINT `fk_Exame_TipoExame`
    FOREIGN KEY (`TipoExame`)
    REFERENCES `db_asfecer`.`TipoExame` (`idTipoExame`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`TipoExame` (
  `idTipoExame` INT(11) NOT NULL AUTO_INCREMENT,
  `tipoExame` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idTipoExame`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Departamento` (
  `idDepto` INT(11) NOT NULL AUTO_INCREMENT,
  `depto` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`idDepto`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Cargo` (
  `idCargo` INT(11) NOT NULL AUTO_INCREMENT,
  `cargo` VARCHAR(45) NOT NULL,
  `Depto` INT(11) NOT NULL,
  PRIMARY KEY (`idCargo`),
  INDEX `idx_Cargo_Depto` (`Depto` ASC),
  CONSTRAINT `fk_Cargo_Depto`
    FOREIGN KEY (`Depto`)
    REFERENCES `db_asfecer`.`Departamento` (`idDepto`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Horario` (
  `idHorario` INT(11) NOT NULL AUTO_INCREMENT,
  `Medico` INT(11) NOT NULL,
  `diaSemana` VARCHAR(20) NOT NULL,
  `inicio` TIME NOT NULL,
  `fim` TIME NOT NULL,
  PRIMARY KEY (`idHorario`),
  INDEX `idx_Horario_Medico` (`Medico` ASC),
  CONSTRAINT `fk_Horario_Medico`
    FOREIGN KEY (`Medico`)
    REFERENCES `db_asfecer`.`Medico` (`idMedico`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Endereco` (
  `idEndereco` INT(11) NOT NULL AUTO_INCREMENT,
  `tipo_logradouro` VARCHAR(20) NOT NULL,
  `nome_nogradouro` VARCHAR(100) NOT NULL,
  `numero` INT(11) NULL DEFAULT NULL,
  `complemento` VARCHAR(15) NULL DEFAULT NULL,
  `bairro` VARCHAR(45) NULL DEFAULT NULL,
  `Cidade` INT(11) NOT NULL,
  `cep` VARCHAR(10) NULL DEFAULT NULL,
  PRIMARY KEY (`idEndereco`),
  INDEX `fk_Endereco_Cdd_idx` (`Cidade` ASC),
  CONSTRAINT `fk_Endereco_Cdd`
    FOREIGN KEY (`Cidade`)
    REFERENCES `db_asfecer`.`Cidade` (`idCidade`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Prontuario` (
  `idProntuario` INT(11) NOT NULL,
  `Consulta` INT(11) NOT NULL,
  `queixaPrincipal` VARCHAR(1000) NULL DEFAULT NULL,
  `anamnese` VARCHAR(1000) NULL DEFAULT NULL,
  `examesFisicos` VARCHAR(1000) NULL DEFAULT NULL,
  `examesComplementares` VARCHAR(1000) NULL DEFAULT NULL,
  `hipotesesDiagnosticas` VARCHAR(1000) NULL DEFAULT NULL,
  `diagnosticoDefinitivo` VARCHAR(1000) NULL DEFAULT NULL,
  `tratamento` VARCHAR(1000) NULL DEFAULT NULL,
  `evolucao` VARCHAR(1000) NULL DEFAULT NULL,
  PRIMARY KEY (`idProntuario`),
  INDEX `fk_Prontuario_Cons_idx` (`Consulta` ASC),
  CONSTRAINT `fk_Prontuario_Cons`
    FOREIGN KEY (`Consulta`)
    REFERENCES `db_asfecer`.`Consulta` (`idConsulta`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;

CREATE TABLE IF NOT EXISTS `db_asfecer`.`Cidade` (
  `idCidade` INT(11) NOT NULL,
  `cidade` VARCHAR(45) NOT NULL,
  `Estado` CHAR(2) NOT NULL,
  PRIMARY KEY (`idCidade`),
  INDEX `fk_Cidade_UF_idx` (`Estado` ASC),
  CONSTRAINT `fk_Cidade_UF`
    FOREIGN KEY (`Estado`)
    REFERENCES `db_asfecer`.`Estados` (`sigla`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
