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

/**
 *
 * @author Adriano Xavier
 */
@Entity
@Table(name = "agenda")
@NamedQueries({
    @NamedQuery(name = "Agenda.findAll", query = "SELECT a FROM Agenda a")})
public class Agenda implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "REGISTROAGENDA")
    private Integer registroagenda;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CANCELADO")
    private boolean cancelado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATA")
    @Temporal(TemporalType.DATE)
    private Date data;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORA")
    @Temporal(TemporalType.TIME)
    private Date hora;
    @Size(max = 150)
    @Column(name = "MOTIVOCANCELAMENTO")
    private String motivocancelamento;
    @Basic(optional = false)
    @NotNull
    @Column(name = "RETORNO")
    private boolean retorno;
    @Basic(optional = false)
    @NotNull
    @Column(name = "STATUS")
    private boolean status;
    @JoinColumn(name = "Medico", referencedColumnName = "Medico")
    @ManyToOne(optional = false)
    private Horario medico;
    @JoinColumn(name = "Paciente", referencedColumnName = "IDPACIENTE")
    @ManyToOne(optional = false)
    private Paciente paciente;
    @JoinColumn(name = "Usuario", referencedColumnName = "IDUSUARIO")
    @ManyToOne(optional = false)
    private Usuario usuario;
    @OneToMany(mappedBy = "agenda")
    private Collection<Consulta> consultaCollection;

    public Agenda() {
    }

    public Agenda(Integer registroagenda) {
        this.registroagenda = registroagenda;
    }

    public Agenda(Integer registroagenda, boolean cancelado, Date data, Date hora, boolean retorno, boolean status) {
        this.registroagenda = registroagenda;
        this.cancelado = cancelado;
        this.data = data;
        this.hora = hora;
        this.retorno = retorno;
        this.status = status;
    }

    public Integer getRegistroagenda() {
        return registroagenda;
    }

    public void setRegistroagenda(Integer registroagenda) {
        this.registroagenda = registroagenda;
    }

    public boolean getCancelado() {
        return cancelado;
    }

    public void setCancelado(boolean cancelado) {
        this.cancelado = cancelado;
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

    public String getMotivocancelamento() {
        return motivocancelamento;
    }

    public void setMotivocancelamento(String motivocancelamento) {
        this.motivocancelamento = motivocancelamento;
    }

    public boolean getRetorno() {
        return retorno;
    }

    public void setRetorno(boolean retorno) {
        this.retorno = retorno;
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

    public Collection<Consulta> getConsultaCollection() {
        return consultaCollection;
    }

    public void setConsultaCollection(Collection<Consulta> consultaCollection) {
        this.consultaCollection = consultaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (registroagenda != null ? registroagenda.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Agenda)) {
            return false;
        }
        Agenda other = (Agenda) object;
        if ((this.registroagenda == null && other.registroagenda != null) || (this.registroagenda != null && !this.registroagenda.equals(other.registroagenda))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Agenda[ registroagenda=" + registroagenda + " ]";
    }
    
}
