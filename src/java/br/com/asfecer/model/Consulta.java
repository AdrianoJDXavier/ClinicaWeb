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
import javax.persistence.CascadeType;
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

/**
 *
 * @author Adriano Xavier
 */
@Entity
@Table(name = "consulta")
@NamedQueries({
    @NamedQuery(name = "Consulta.findAll", query = "SELECT c FROM Consulta c")})
public class Consulta implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDCONSULTA")
    private Integer idconsulta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATACONSULTA")
    @Temporal(TemporalType.DATE)
    private Date dataconsulta;
    @Basic(optional = false)
    @NotNull
    @Column(name = "HORACONSULTA")
    @Temporal(TemporalType.TIME)
    private Date horaconsulta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
    private Collection<Prontuario> prontuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
    private Collection<Atestado> atestadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
    private Collection<Receituario> receituarioCollection;
    @JoinColumn(name = "Agenda", referencedColumnName = "REGISTROAGENDA")
    @ManyToOne
    private Agenda agenda;
    @JoinColumn(name = "Medico", referencedColumnName = "IDMEDICO")
    @ManyToOne(optional = false)
    private Medico medico;
    @JoinColumn(name = "Paciente", referencedColumnName = "IDPACIENTE")
    @ManyToOne(optional = false)
    private Paciente paciente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
    private Collection<Pedidoexame> pedidoexameCollection;

    public Consulta() {
    }

    public Consulta(Integer idconsulta) {
        this.idconsulta = idconsulta;
    }

    public Consulta(Integer idconsulta, Date dataconsulta, Date horaconsulta) {
        this.idconsulta = idconsulta;
        this.dataconsulta = dataconsulta;
        this.horaconsulta = horaconsulta;
    }

    public Integer getIdconsulta() {
        return idconsulta;
    }

    public void setIdconsulta(Integer idconsulta) {
        this.idconsulta = idconsulta;
    }

    public Date getDataconsulta() {
        return dataconsulta;
    }

    public void setDataconsulta(Date dataconsulta) {
        this.dataconsulta = dataconsulta;
    }

    public Date getHoraconsulta() {
        return horaconsulta;
    }

    public void setHoraconsulta(Date horaconsulta) {
        this.horaconsulta = horaconsulta;
    }

    public Collection<Prontuario> getProntuarioCollection() {
        return prontuarioCollection;
    }

    public void setProntuarioCollection(Collection<Prontuario> prontuarioCollection) {
        this.prontuarioCollection = prontuarioCollection;
    }

    public Collection<Atestado> getAtestadoCollection() {
        return atestadoCollection;
    }

    public void setAtestadoCollection(Collection<Atestado> atestadoCollection) {
        this.atestadoCollection = atestadoCollection;
    }

    public Collection<Receituario> getReceituarioCollection() {
        return receituarioCollection;
    }

    public void setReceituarioCollection(Collection<Receituario> receituarioCollection) {
        this.receituarioCollection = receituarioCollection;
    }

    public Agenda getAgenda() {
        return agenda;
    }

    public void setAgenda(Agenda agenda) {
        this.agenda = agenda;
    }

    public Medico getMedico() {
        return medico;
    }

    public void setMedico(Medico medico) {
        this.medico = medico;
    }

    public Paciente getPaciente() {
        return paciente;
    }

    public void setPaciente(Paciente paciente) {
        this.paciente = paciente;
    }

    public Collection<Pedidoexame> getPedidoexameCollection() {
        return pedidoexameCollection;
    }

    public void setPedidoexameCollection(Collection<Pedidoexame> pedidoexameCollection) {
        this.pedidoexameCollection = pedidoexameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idconsulta != null ? idconsulta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consulta)) {
            return false;
        }
        Consulta other = (Consulta) object;
        if ((this.idconsulta == null && other.idconsulta != null) || (this.idconsulta != null && !this.idconsulta.equals(other.idconsulta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Consulta[ idconsulta=" + idconsulta + " ]";
    }
    
}
