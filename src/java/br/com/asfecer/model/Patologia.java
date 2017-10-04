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
    @NamedQuery(name = "Patologia.findAll", query = "SELECT p FROM Patologia p")
    , @NamedQuery(name = "Patologia.findByIdPatologia", query = "SELECT p FROM Patologia p WHERE p.idPatologia = :idPatologia")
    , @NamedQuery(name = "Patologia.findByPatologia", query = "SELECT p FROM Patologia p WHERE p.patologia = :patologia")})
public class Patologia implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idPatologia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String patologia;
    @OneToMany(mappedBy = "patologia")
    private Collection<Tipoatestado> tipoatestadoCollection;

    public Patologia() {
    }

    public Patologia(Integer idPatologia) {
        this.idPatologia = idPatologia;
    }

    public Patologia(Integer idPatologia, String patologia) {
        this.idPatologia = idPatologia;
        this.patologia = patologia;
    }

    public Integer getIdPatologia() {
        return idPatologia;
    }

    public void setIdPatologia(Integer idPatologia) {
        this.idPatologia = idPatologia;
    }

    public String getPatologia() {
        return patologia;
    }

    public void setPatologia(String patologia) {
        this.patologia = patologia;
    }

    @XmlTransient
    public Collection<Tipoatestado> getTipoatestadoCollection() {
        return tipoatestadoCollection;
    }

    public void setTipoatestadoCollection(Collection<Tipoatestado> tipoatestadoCollection) {
        this.tipoatestadoCollection = tipoatestadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPatologia != null ? idPatologia.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Patologia)) {
            return false;
        }
        Patologia other = (Patologia) object;
        if ((this.idPatologia == null && other.idPatologia != null) || (this.idPatologia != null && !this.idPatologia.equals(other.idPatologia))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Patologia[ idPatologia=" + idPatologia + " ]";
    }
    
}
