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
    @NamedQuery(name = "Especialidade.findAll", query = "SELECT e FROM Especialidade e")
    , @NamedQuery(name = "Especialidade.findByIdEspecialidade", query = "SELECT e FROM Especialidade e WHERE e.idEspecialidade = :idEspecialidade")
    , @NamedQuery(name = "Especialidade.findByDescricao", query = "SELECT e FROM Especialidade e WHERE e.descricao = :descricao")})
public class Especialidade implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idEspecialidade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String descricao;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "especialidade")
    private Collection<Medico> medicoCollection;

    public Especialidade() {
    }

    public Especialidade(Integer idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    public Especialidade(String descricao) {
        this.descricao = descricao;
    }

    public Especialidade(Integer idEspecialidade, String descricao) {
        this.idEspecialidade = idEspecialidade;
        this.descricao = descricao;
    }

    public Integer getIdEspecialidade() {
        return idEspecialidade;
    }

    public void setIdEspecialidade(Integer idEspecialidade) {
        this.idEspecialidade = idEspecialidade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    @XmlTransient
    public Collection<Medico> getMedicoCollection() {
        return medicoCollection;
    }

    public void setMedicoCollection(Collection<Medico> medicoCollection) {
        this.medicoCollection = medicoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEspecialidade != null ? idEspecialidade.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Especialidade)) {
            return false;
        }
        Especialidade other = (Especialidade) object;
        if ((this.idEspecialidade == null && other.idEspecialidade != null) || (this.idEspecialidade != null && !this.idEspecialidade.equals(other.idEspecialidade))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Especialidade[ idEspecialidade=" + idEspecialidade + " ]";
    }
    
}
