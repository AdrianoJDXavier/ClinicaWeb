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
    @Basic(optional = false)
    @NotNull
    @Column(name = "moduloAcesso")
    private boolean moduloAcesso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "moduloAdmBD")
    private boolean moduloAdmBD;

    public Usuario() {
    }

    public Usuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public Usuario(Integer idUsuario, boolean tipoUsuario, boolean status, String login, String senha, boolean moduloAdministrativo, boolean moduloAgendamento, boolean moduloAtendimento, boolean moduloAcesso, boolean moduloAdmBD) {
        this.idUsuario = idUsuario;
        this.tipoUsuario = tipoUsuario;
        this.status = status;
        this.login = login;
        this.senha = senha;
        this.moduloAdministrativo = moduloAdministrativo;
        this.moduloAgendamento = moduloAgendamento;
        this.moduloAtendimento = moduloAtendimento;
        this.moduloAcesso = moduloAcesso;
        this.moduloAdmBD = moduloAdmBD;
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

    public boolean getModuloAcesso() {
        return moduloAcesso;
    }

    public void setModuloAcesso(boolean moduloAcesso) {
        this.moduloAcesso = moduloAcesso;
    }

    public boolean getModuloAdmBD() {
        return moduloAdmBD;
    }

    public void setModuloAdmBD(boolean moduloAdmBD) {
        this.moduloAdmBD = moduloAdmBD;
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
