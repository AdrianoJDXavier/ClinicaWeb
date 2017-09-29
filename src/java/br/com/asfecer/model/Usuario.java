/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Paulo
 */
@Entity
@Table(name = "usuario")
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "idUsuario")
    private Integer idUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "TipoUsuario")
    private boolean tipoUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "status")
    private boolean status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "login")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "senha")
    private String senha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadFuncionario")
    private boolean cadFuncionario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadFuncao")
    private boolean cadFuncao;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadDepartamento")
    private boolean cadDepartamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadUsuario")
    private boolean cadUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadPaciente")
    private boolean cadPaciente;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadEndereco")
    private boolean cadEndereco;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadEspecialidade")
    private boolean cadEspecialidade;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadMedico")
    private boolean cadMedico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadConvenio")
    private boolean cadConvenio;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadTipoAtestado")
    private boolean cadTipoAtestado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadTipoReceituario")
    private boolean cadTipoReceituario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadMedicamento")
    private boolean cadMedicamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadPatologia")
    private boolean cadPatologia;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadExame")
    private boolean cadExame;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cadTipoExame")
    private boolean cadTipoExame;
    @Basic(optional = false)
    @NotNull
    @Column(name = "receituario")
    private boolean receituario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pedidoExame")
    private boolean pedidoExame;
    @Basic(optional = false)
    @NotNull
    @Column(name = "atestado")
    private boolean atestado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "agendaConsulta")
    private boolean agendaConsulta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cancelaConsulta")
    private boolean cancelaConsulta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "moduloAdministrativo")
    private boolean moduloAdministrativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "moduloAgendamento")
    private boolean moduloAgendamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "moduloAtendimento")
    private boolean moduloAtendimento;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(Integer idUsuario, boolean tipoUsuario, boolean status, String login, String senha, boolean cadFuncionario, boolean cadFuncao, boolean cadDepartamento, boolean cadUsuario, boolean cadPaciente, boolean cadEndereco, boolean cadEspecialidade, boolean cadMedico, boolean cadConvenio, boolean cadTipoAtestado, boolean cadTipoReceituario, boolean cadMedicamento, boolean cadPatologia, boolean cadExame, boolean cadTipoExame, boolean receituario, boolean pedidoExame, boolean atestado, boolean agendaConsulta, boolean cancelaConsulta, boolean moduloAdministrativo, boolean moduloAgendamento, boolean moduloAtendimento) {
        this.idUsuario = idUsuario;
        this.tipoUsuario = tipoUsuario;
        this.status = status;
        this.login = login;
        this.senha = senha;
        this.cadFuncionario = cadFuncionario;
        this.cadFuncao = cadFuncao;
        this.cadDepartamento = cadDepartamento;
        this.cadUsuario = cadUsuario;
        this.cadPaciente = cadPaciente;
        this.cadEndereco = cadEndereco;
        this.cadEspecialidade = cadEspecialidade;
        this.cadMedico = cadMedico;
        this.cadConvenio = cadConvenio;
        this.cadTipoAtestado = cadTipoAtestado;
        this.cadTipoReceituario = cadTipoReceituario;
        this.cadMedicamento = cadMedicamento;
        this.cadPatologia = cadPatologia;
        this.cadExame = cadExame;
        this.cadTipoExame = cadTipoExame;
        this.receituario = receituario;
        this.pedidoExame = pedidoExame;
        this.atestado = atestado;
        this.agendaConsulta = agendaConsulta;
        this.cancelaConsulta = cancelaConsulta;
        this.moduloAdministrativo = moduloAdministrativo;
        this.moduloAgendamento = moduloAgendamento;
        this.moduloAtendimento = moduloAtendimento;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public boolean getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(boolean tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public boolean getCadFuncionario() {
        return cadFuncionario;
    }

    public void setCadFuncionario(boolean cadFuncionario) {
        this.cadFuncionario = cadFuncionario;
    }

    public boolean getCadFuncao() {
        return cadFuncao;
    }

    public void setCadFuncao(boolean cadFuncao) {
        this.cadFuncao = cadFuncao;
    }

    public boolean getCadDepartamento() {
        return cadDepartamento;
    }

    public void setCadDepartamento(boolean cadDepartamento) {
        this.cadDepartamento = cadDepartamento;
    }

    public boolean getCadUsuario() {
        return cadUsuario;
    }

    public void setCadUsuario(boolean cadUsuario) {
        this.cadUsuario = cadUsuario;
    }

    public boolean getCadPaciente() {
        return cadPaciente;
    }

    public void setCadPaciente(boolean cadPaciente) {
        this.cadPaciente = cadPaciente;
    }

    public boolean getCadEndereco() {
        return cadEndereco;
    }

    public void setCadEndereco(boolean cadEndereco) {
        this.cadEndereco = cadEndereco;
    }

    public boolean getCadEspecialidade() {
        return cadEspecialidade;
    }

    public void setCadEspecialidade(boolean cadEspecialidade) {
        this.cadEspecialidade = cadEspecialidade;
    }

    public boolean getCadMedico() {
        return cadMedico;
    }

    public void setCadMedico(boolean cadMedico) {
        this.cadMedico = cadMedico;
    }

    public boolean getCadConvenio() {
        return cadConvenio;
    }

    public void setCadConvenio(boolean cadConvenio) {
        this.cadConvenio = cadConvenio;
    }

    public boolean getCadTipoAtestado() {
        return cadTipoAtestado;
    }

    public void setCadTipoAtestado(boolean cadTipoAtestado) {
        this.cadTipoAtestado = cadTipoAtestado;
    }

    public boolean getCadTipoReceituario() {
        return cadTipoReceituario;
    }

    public void setCadTipoReceituario(boolean cadTipoReceituario) {
        this.cadTipoReceituario = cadTipoReceituario;
    }

    public boolean getCadMedicamento() {
        return cadMedicamento;
    }

    public void setCadMedicamento(boolean cadMedicamento) {
        this.cadMedicamento = cadMedicamento;
    }

    public boolean getCadPatologia() {
        return cadPatologia;
    }

    public void setCadPatologia(boolean cadPatologia) {
        this.cadPatologia = cadPatologia;
    }

    public boolean getCadExame() {
        return cadExame;
    }

    public void setCadExame(boolean cadExame) {
        this.cadExame = cadExame;
    }

    public boolean getCadTipoExame() {
        return cadTipoExame;
    }

    public void setCadTipoExame(boolean cadTipoExame) {
        this.cadTipoExame = cadTipoExame;
    }

    public boolean getReceituario() {
        return receituario;
    }

    public void setReceituario(boolean receituario) {
        this.receituario = receituario;
    }

    public boolean getPedidoExame() {
        return pedidoExame;
    }

    public void setPedidoExame(boolean pedidoExame) {
        this.pedidoExame = pedidoExame;
    }

    public boolean getAtestado() {
        return atestado;
    }

    public void setAtestado(boolean atestado) {
        this.atestado = atestado;
    }

    public boolean getAgendaConsulta() {
        return agendaConsulta;
    }

    public void setAgendaConsulta(boolean agendaConsulta) {
        this.agendaConsulta = agendaConsulta;
    }

    public boolean getCancelaConsulta() {
        return cancelaConsulta;
    }

    public void setCancelaConsulta(boolean cancelaConsulta) {
        this.cancelaConsulta = cancelaConsulta;
    }

    public boolean getModuloAdministrativo() {
        return moduloAdministrativo;
    }

    public void setModuloAdministrativo(boolean moduloAdministrativo) {
        this.moduloAdministrativo = moduloAdministrativo;
    }

    public boolean getModuloAgendamento() {
        return moduloAgendamento;
    }

    public void setModuloAgendamento(boolean moduloAgendamento) {
        this.moduloAgendamento = moduloAgendamento;
    }

    public boolean getModuloAtendimento() {
        return moduloAtendimento;
    }

    public void setModuloAtendimento(boolean moduloAtendimento) {
        this.moduloAtendimento = moduloAtendimento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idUsuario != null ? idUsuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idUsuario == null && other.idUsuario != null) || (this.idUsuario != null && !this.idUsuario.equals(other.idUsuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Usuario[ idUsuario=" + idUsuario + " ]";
    }
    
}
