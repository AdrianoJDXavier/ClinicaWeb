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
    @NamedQuery(name = "Consulta.findAll", query = "SELECT c FROM Consulta c")
    , @NamedQuery(name = "Consulta.findByIdConsulta", query = "SELECT c FROM Consulta c WHERE c.idConsulta = :idConsulta")
    , @NamedQuery(name = "Consulta.findByDataConsulta", query = "SELECT c FROM Consulta c WHERE c.dataConsulta = :dataConsulta")
    , @NamedQuery(name = "Consulta.findByHoraConsulta", query = "SELECT c FROM Consulta c WHERE c.horaConsulta = :horaConsulta")})
public class Consulta implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idConsulta;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataConsulta;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date horaConsulta;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
    private Collection<Prontuario> prontuarioCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
    private Collection<Atestado> atestadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
    private Collection<Receituario> receituarioCollection;
    @JoinColumn(name = "Agenda", referencedColumnName = "registroAgenda")
    @ManyToOne
    private Agenda agenda;
    @JoinColumn(name = "Medico", referencedColumnName = "idMedico", nullable = false)
    @ManyToOne(optional = false)
    private Medico medico;
    @JoinColumn(name = "Paciente", referencedColumnName = "idPaciente", nullable = false)
    @ManyToOne(optional = false)
    private Paciente paciente;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "consulta")
    private Collection<Pedidoexame> pedidoexameCollection;

    public Consulta() {
    }

    public Consulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Consulta(Integer idConsulta, Date dataConsulta, Date horaConsulta) {
        this.idConsulta = idConsulta;
        this.dataConsulta = dataConsulta;
        this.horaConsulta = horaConsulta;
    }

    public Integer getIdConsulta() {
        return idConsulta;
    }

    public void setIdConsulta(Integer idConsulta) {
        this.idConsulta = idConsulta;
    }

    public Date getDataConsulta() {
        return dataConsulta;
    }

    public void setDataConsulta(Date dataConsulta) {
        this.dataConsulta = dataConsulta;
    }

    public Date getHoraConsulta() {
        return horaConsulta;
    }

    public void setHoraConsulta(Date horaConsulta) {
        this.horaConsulta = horaConsulta;
    }

    @XmlTransient
    public Collection<Prontuario> getProntuarioCollection() {
        return prontuarioCollection;
    }

    public void setProntuarioCollection(Collection<Prontuario> prontuarioCollection) {
        this.prontuarioCollection = prontuarioCollection;
    }

    @XmlTransient
    public Collection<Atestado> getAtestadoCollection() {
        return atestadoCollection;
    }

    public void setAtestadoCollection(Collection<Atestado> atestadoCollection) {
        this.atestadoCollection = atestadoCollection;
    }

    @XmlTransient
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

    @XmlTransient
    public Collection<Pedidoexame> getPedidoexameCollection() {
        return pedidoexameCollection;
    }

    public void setPedidoexameCollection(Collection<Pedidoexame> pedidoexameCollection) {
        this.pedidoexameCollection = pedidoexameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConsulta != null ? idConsulta.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Consulta)) {
            return false;
        }
        Consulta other = (Consulta) object;
        if ((this.idConsulta == null && other.idConsulta != null) || (this.idConsulta != null && !this.idConsulta.equals(other.idConsulta))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Consulta[ idConsulta=" + idConsulta + " ]";
    }
    
}
