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

/**
 *
 * @author Adriano Xavier
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
    @Column(name = "IDUSUARIO")
    private Integer idusuario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 30)
    @Column(name = "LOGIN")
    private String login;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODULOACESSO")
    private Character moduloacesso;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODULOADMBD")
    private Character moduloadmbd;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODULOADMINISTRATIVO")
    private Character moduloadministrativo;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODULOAGENDAMENTO")
    private Character moduloagendamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "MODULOATENDIMENTO")
    private Character moduloatendimento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 12)
    @Column(name = "SENHA")
    private String senha;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private Character status;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "TIPOUSUARIO")
    private String tipousuario;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "usuario")
    private Collection<Agenda> agendaCollection;

    public Usuario() {
    }
    
    public Usuario(String tipoUsuario, Character status, String login, String senha, Character moduloAdministrativo, Character moduloAgendamento, Character moduloAtendimento, Character moduloAcesso, Character moduloAdmBD){
        this.tipousuario = tipoUsuario;
        this.status = status;
        this.login = login;
        this.senha = senha;
        this.moduloacesso = moduloAcesso;
        this.moduloadmbd = moduloAdmBD;
        this.moduloadministrativo = moduloAdministrativo;
        this.moduloagendamento = moduloAgendamento;
        this.moduloatendimento = moduloAtendimento;
    }

    public Usuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public Usuario(Integer idusuario, String login, Character moduloacesso, Character moduloadmbd, Character moduloadministrativo, Character moduloagendamento, Character moduloatendimento, String senha, Character status, String tipousuario) {
        this.idusuario = idusuario;
        this.login = login;
        this.moduloacesso = moduloacesso;
        this.moduloadmbd = moduloadmbd;
        this.moduloadministrativo = moduloadministrativo;
        this.moduloagendamento = moduloagendamento;
        this.moduloatendimento = moduloatendimento;
        this.senha = senha;
        this.status = status;
        this.tipousuario = tipousuario;
    }

    public Integer getIdusuario() {
        return idusuario;
    }

    public void setIdusuario(Integer idusuario) {
        this.idusuario = idusuario;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public Character getModuloacesso() {
        return moduloacesso;
    }

    public void setModuloacesso(Character moduloacesso) {
        this.moduloacesso = moduloacesso;
    }

    public Character getModuloadmbd() {
        return moduloadmbd;
    }

    public void setModuloadmbd(Character moduloadmbd) {
        this.moduloadmbd = moduloadmbd;
    }

    public Character getModuloadministrativo() {
        return moduloadministrativo;
    }

    public void setModuloadministrativo(Character moduloadministrativo) {
        this.moduloadministrativo = moduloadministrativo;
    }

    public Character getModuloagendamento() {
        return moduloagendamento;
    }

    public void setModuloagendamento(Character moduloagendamento) {
        this.moduloagendamento = moduloagendamento;
    }

    public Character getModuloatendimento() {
        return moduloatendimento;
    }

    public void setModuloatendimento(Character moduloatendimento) {
        this.moduloatendimento = moduloatendimento;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public Character getStatus() {
        return status;
    }

    public void setStatus(Character status) {
        this.status = status;
    }

    public String getTipousuario() {
        return tipousuario;
    }

    public void setTipousuario(String tipousuario) {
        this.tipousuario = tipousuario;
    }

    public Collection<Agenda> getAgendaCollection() {
        return agendaCollection;
    }

    public void setAgendaCollection(Collection<Agenda> agendaCollection) {
        this.agendaCollection = agendaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idusuario != null ? idusuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Usuario)) {
            return false;
        }
        Usuario other = (Usuario) object;
        if ((this.idusuario == null && other.idusuario != null) || (this.idusuario != null && !this.idusuario.equals(other.idusuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Usuario[ idusuario=" + idusuario + " ]";
    }
    
}
