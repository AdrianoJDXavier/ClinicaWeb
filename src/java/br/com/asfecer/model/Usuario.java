/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PToledo
 */
@Entity
@Table(catalog = "db_asfecer", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Usuario.findAll", query = "SELECT u FROM Usuario u")
    , @NamedQuery(name = "Usuario.findByIdUsuario", query = "SELECT u FROM Usuario u WHERE u.idUsuario = :idUsuario")
    , @NamedQuery(name = "Usuario.findByTipoUsuario", query = "SELECT u FROM Usuario u WHERE u.tipoUsuario = :tipoUsuario")
    , @NamedQuery(name = "Usuario.findByStatus", query = "SELECT u FROM Usuario u WHERE u.status = :status")
    , @NamedQuery(name = "Usuario.findByLogin", query = "SELECT u FROM Usuario u WHERE u.login = :login")
    , @NamedQuery(name = "Usuario.findBySenha", query = "SELECT u FROM Usuario u WHERE u.senha = :senha")
    , @NamedQuery(name = "Usuario.findByModuloAdministrativo", query = "SELECT u FROM Usuario u WHERE u.moduloAdministrativo = :moduloAdministrativo")
    , @NamedQuery(name = "Usuario.findByModuloAgendamento", query = "SELECT u FROM Usuario u WHERE u.moduloAgendamento = :moduloAgendamento")
    , @NamedQuery(name = "Usuario.findByModuloAtendimento", query = "SELECT u FROM Usuario u WHERE u.moduloAtendimento = :moduloAtendimento")
    , @NamedQuery(name = "Usuario.findByModuloAcesso", query = "SELECT u FROM Usuario u WHERE u.moduloAcesso = :moduloAcesso")
    , @NamedQuery(name = "Usuario.findByModuloAdmBD", query = "SELECT u FROM Usuario u WHERE u.moduloAdmBD = :moduloAdmBD")})
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean tipoUsuario;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(nullable = false, length = 30)
    private String login;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(nullable = false, length = 12)
    private String senha;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean moduloAdministrativo;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean moduloAgendamento;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean moduloAtendimento;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean moduloAcesso;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean moduloAdmBD;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<Agenda> agendaCollection;

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

    @XmlTransient
    public Collection<Agenda> getAgendaCollection() {
        return agendaCollection;
    }

    public void setAgendaCollection(Collection<Agenda> agendaCollection) {
        this.agendaCollection = agendaCollection;
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
