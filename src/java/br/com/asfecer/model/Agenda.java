/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    @NamedQuery(name = "Agenda.findAll", query = "SELECT a FROM Agenda a")
    , @NamedQuery(name = "Agenda.findByRegistroAgenda", query = "SELECT a FROM Agenda a WHERE a.registroAgenda = :registroAgenda")
    , @NamedQuery(name = "Agenda.findByData", query = "SELECT a FROM Agenda a WHERE a.data = :data")
    , @NamedQuery(name = "Agenda.findByHora", query = "SELECT a FROM Agenda a WHERE a.hora = :hora")
    , @NamedQuery(name = "Agenda.findByRetorno", query = "SELECT a FROM Agenda a WHERE a.retorno = :retorno")
    , @NamedQuery(name = "Agenda.findByCancelado", query = "SELECT a FROM Agenda a WHERE a.cancelado = :cancelado")
    , @NamedQuery(name = "Agenda.findByMotivoCancelamento", query = "SELECT a FROM Agenda a WHERE a.motivoCancelamento = :motivoCancelamento")
    , @NamedQuery(name = "Agenda.findByStatus", query = "SELECT a FROM Agenda a WHERE a.status = :status")})
public class Agenda implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer registroAgenda;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean retorno;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean cancelado;
    @Size(max = 150)
    @Column(length = 150)
    private String motivoCancelamento;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean status;
    @JoinColumn(name = "Medico", referencedColumnName = "Medico", nullable = false)
    @ManyToOne(optional = false)
    private Horario medico;
    @JoinColumn(name = "Paciente", referencedColumnName = "idPaciente", nullable = false)
    @ManyToOne(optional = false)
    private Paciente paciente;
    @JoinColumn(name = "Usuario", referencedColumnName = "idUsuario", nullable = false)
    @ManyToOne(optional = false)
    private Usuario usuario;
    @OneToMany(mappedBy = "agenda")
    private Collection<Consulta> consultaCollection;

    public Agenda() {
    }

    public Agenda(Integer registroAgenda) {
        this.registroAgenda = registroAgenda;
    }

    public Agenda(Integer registroAgenda, Date data, Date hora, boolean retorno, boolean cancelado, boolean status) {
        this.registroAgenda = registroAgenda;
        this.data = data;
        this.hora = hora;
        this.retorno = retorno;
        this.cancelado = cancelado;
        this.status = status;
    }

    public Integer getRegistroAgenda() {
        return registroAgenda;
    }

    public void setRegistroAgenda(Integer registroAgenda) {
        this.registroAgenda = registroAgenda;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public boolean getRetorno() {
        return retorno;
    }

    public void setRetorno(boolean retorno) {
        this.retorno = retorno;
    }

    public boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
    }

    public String getMotivoCancelamento() {
        return motivoCancelamento;
    }

    public void setMotivoCancelamento(String motivoCancelamento) {
        this.motivoCancelamento = motivoCancelamento;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Horario getMedico() {
        return medico;
    }

    public void setMedico(Horario medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @XmlTransient
    public Collection<Consulta> getConsultaCollection() {
        return consultaCollection;
    }

    public void setConsultaCollection(Collection<Consulta> consultaCollection) {
        this.consultaCollection = consultaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registroAgenda != null ? registroAgenda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agenda)) {
            return false;
        }
        Agenda other = (Agenda) object;
        if ((this.registroAgenda == null && other.registroAgenda != null) || (this.registroAgenda != null && !this.registroAgenda.equals(other.registroAgenda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Agenda[ registroAgenda=" + registroAgenda + " ]";
    }
    
}
