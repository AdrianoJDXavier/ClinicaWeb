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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "medico")
@NamedQueries({
    @NamedQuery(name = "Medico.findAll", query = "SELECT m FROM Medico m")})
public class Medico implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDMEDICO")
    private Integer idmedico;
    @Basic(optional = false)
    @NotNull
    @Column(name = "CRM")
    private int crm;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMEMEDICO")
    private String nomemedico;
    @OneToMany(mappedBy = "medico")
    private Collection<Horario> horarioCollection;
    @JoinColumn(name = "Especialidade", referencedColumnName = "IDESPECIALIDADE")
    @ManyToOne(optional = false)
    private Especialidade especialidade;
    @JoinColumn(name = "UfCrm", referencedColumnName = "SIGLA")
    @ManyToOne(optional = false)
    private Estados ufCrm;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medico")
    private Collection<Consulta> consultaCollection;

    public Medico() {
    }

    public Medico(Integer idmedico) {
        this.idmedico = idmedico;
    }

    public Medico(Integer idmedico, int crm, String nomemedico) {
        this.idmedico = idmedico;
        this.crm = crm;
        this.nomemedico = nomemedico;
    }

    public Integer getIdmedico() {
        return idmedico;
    }

    public void setIdmedico(Integer idmedico) {
        this.idmedico = idmedico;
    }

    public int getCrm() {
        return crm;
    }

    public void setCrm(int crm) {
        this.crm = crm;
    }

    public String getNomemedico() {
        return nomemedico;
    }

    public void setNomemedico(String nomemedico) {
        this.nomemedico = nomemedico;
    }

    public Collection<Horario> getHorarioCollection() {
        return horarioCollection;
    }

    public void setHorarioCollection(Collection<Horario> horarioCollection) {
        this.horarioCollection = horarioCollection;
    }

    public Especialidade getEspecialidade() {
        return especialidade;
    }

    public void setEspecialidade(Especialidade especialidade) {
        this.especialidade = especialidade;
    }

    public Estados getUfCrm() {
        return ufCrm;
    }

    public void setUfCrm(Estados ufCrm) {
        this.ufCrm = ufCrm;
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
        hash += (idmedico != null ? idmedico.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medico)) {
            return false;
        }
        Medico other = (Medico) object;
        if ((this.idmedico == null && other.idmedico != null) || (this.idmedico != null && !this.idmedico.equals(other.idmedico))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Medico[ idmedico=" + idmedico + " ]";
    }
    
}
