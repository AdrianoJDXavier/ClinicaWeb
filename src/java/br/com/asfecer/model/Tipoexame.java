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
@Table(name = "tipoexame")
@NamedQueries({
    @NamedQuery(name = "Tipoexame.findAll", query = "SELECT t FROM Tipoexame t")})
public class Tipoexame implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDTIPOEXAME")
    private Integer idtipoexame;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "TIPOEXAME")
    private String tipoexame;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoExame")
    private Collection<Exame> exameCollection;

    public Tipoexame() {
    }

    public Tipoexame(Integer idtipoexame) {
        this.idtipoexame = idtipoexame;
    }

    public Tipoexame(Integer idtipoexame, String tipoexame) {
        this.idtipoexame = idtipoexame;
        this.tipoexame = tipoexame;
    }

    public Integer getIdtipoexame() {
        return idtipoexame;
    }

    public void setIdtipoexame(Integer idtipoexame) {
        this.idtipoexame = idtipoexame;
    }

    public String getTipoexame() {
        return tipoexame;
    }

    public void setTipoexame(String tipoexame) {
        this.tipoexame = tipoexame;
    }

    public Collection<Exame> getExameCollection() {
        return exameCollection;
    }

    public void setExameCollection(Collection<Exame> exameCollection) {
        this.exameCollection = exameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoexame != null ? idtipoexame.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoexame)) {
            return false;
        }
        Tipoexame other = (Tipoexame) object;
        if ((this.idtipoexame == null && other.idtipoexame != null) || (this.idtipoexame != null && !this.idtipoexame.equals(other.idtipoexame))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Tipoexame[ idtipoexame=" + idtipoexame + " ]";
    }
    
}
