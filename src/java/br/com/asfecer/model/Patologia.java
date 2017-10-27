/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
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
@Table(name = "patologia")
@NamedQueries({
    @NamedQuery(name = "Patologia.findAll", query = "SELECT p FROM Patologia p")})
public class Patologia implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDPATOLOGIA")
    private Integer idpatologia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "PATOLOGIA")
    private String patologia;
    @OneToMany(mappedBy = "patologia")
    private Collection<Tipoatestado> tipoatestadoCollection;

    public Patologia() {
    }

    public Patologia(Integer idpatologia) {
        this.idpatologia = idpatologia;
    }

    public Patologia(Integer idpatologia, String patologia) {
        this.idpatologia = idpatologia;
        this.patologia = patologia;
    }

    public Integer getIdpatologia() {
        return idpatologia;
    }

    public void setIdpatologia(Integer idpatologia) {
        this.idpatologia = idpatologia;
    }

    public String getPatologia() {
        return patologia;
    }

    public void setPatologia(String patologia) {
        this.patologia = patologia;
    }

    public Collection<Tipoatestado> getTipoatestadoCollection() {
        return tipoatestadoCollection;
    }

    public void setTipoatestadoCollection(Collection<Tipoatestado> tipoatestadoCollection) {
        this.tipoatestadoCollection = tipoatestadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpatologia != null ? idpatologia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Patologia)) {
            return false;
        }
        Patologia other = (Patologia) object;
        if ((this.idpatologia == null && other.idpatologia != null) || (this.idpatologia != null && !this.idpatologia.equals(other.idpatologia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Patologia[ idpatologia=" + idpatologia + " ]";
    }
    
}
