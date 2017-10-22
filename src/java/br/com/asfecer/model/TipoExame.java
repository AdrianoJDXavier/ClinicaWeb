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
    @NamedQuery(name = "Tipoexame.findAll", query = "SELECT t FROM Tipoexame t")
    , @NamedQuery(name = "Tipoexame.findByIdTipoExame", query = "SELECT t FROM Tipoexame t WHERE t.idTipoExame = :idTipoExame")
    , @NamedQuery(name = "Tipoexame.findByTipoExame", query = "SELECT t FROM Tipoexame t WHERE t.tipoExame = :tipoExame")})
public class TipoExame implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idTipoExame;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String tipoExame;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoExame")
    private Collection<Exame> exameCollection;

    public TipoExame() {
    }

    public TipoExame(Integer idTipoExame) {
        this.idTipoExame = idTipoExame;
    }

    public TipoExame(Integer idTipoExame, String tipoExame) {
        this.idTipoExame = idTipoExame;
        this.tipoExame = tipoExame;
    }

    public TipoExame(String tipoExame) {
        this.tipoExame = tipoExame;
    }

    public Integer getIdTipoExame() {
        return idTipoExame;
    }

    public void setIdTipoExame(Integer idTipoExame) {
        this.idTipoExame = idTipoExame;
    }

    public String getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(String tipoExame) {
        this.tipoExame = tipoExame;
    }

    @XmlTransient
    public Collection<Exame> getExameCollection() {
        return exameCollection;
    }

    public void setExameCollection(Collection<Exame> exameCollection) {
        this.exameCollection = exameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoExame != null ? idTipoExame.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoExame)) {
            return false;
        }
        TipoExame other = (TipoExame) object;
        if ((this.idTipoExame == null && other.idTipoExame != null) || (this.idTipoExame != null && !this.idTipoExame.equals(other.idTipoExame))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Tipoexame[ idTipoExame=" + idTipoExame + " ]";
    }
    
}
