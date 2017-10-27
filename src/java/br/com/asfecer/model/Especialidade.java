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
@Table(name = "especialidade")
@NamedQueries({
    @NamedQuery(name = "Especialidade.findAll", query = "SELECT e FROM Especialidade e")})
public class Especialidade implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDESPECIALIDADE")
    private Integer idespecialidade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DESCRICAO")
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "especialidade")
    private Collection<Medico> medicoCollection;

    public Especialidade() {
    }

    public Especialidade(Integer idespecialidade) {
        this.idespecialidade = idespecialidade;
    }

    public Especialidade(Integer idespecialidade, String descricao) {
        this.idespecialidade = idespecialidade;
        this.descricao = descricao;
    }

    public Integer getIdespecialidade() {
        return idespecialidade;
    }

    public void setIdespecialidade(Integer idespecialidade) {
        this.idespecialidade = idespecialidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Collection<Medico> getMedicoCollection() {
        return medicoCollection;
    }

    public void setMedicoCollection(Collection<Medico> medicoCollection) {
        this.medicoCollection = medicoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idespecialidade != null ? idespecialidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Especialidade)) {
            return false;
        }
        Especialidade other = (Especialidade) object;
        if ((this.idespecialidade == null && other.idespecialidade != null) || (this.idespecialidade != null && !this.idespecialidade.equals(other.idespecialidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Especialidade[ idespecialidade=" + idespecialidade + " ]";
    }
    
}
