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
    @NamedQuery(name = "Exame.findAll", query = "SELECT e FROM Exame e")
    , @NamedQuery(name = "Exame.findByIdExame", query = "SELECT e FROM Exame e WHERE e.idExame = :idExame")
    , @NamedQuery(name = "Exame.findByExame", query = "SELECT e FROM Exame e WHERE e.exame = :exame")})
public class Exame implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idExame;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String exame;
    @JoinColumn(name = "TipoExame", referencedColumnName = "idTipoExame", nullable = false)
    @ManyToOne(optional = false)
    private TipoExame tipoExame;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exame")
    private Collection<PedidoExame> pedidoexameCollection;

    public Exame() {
    }

    public Exame(Integer idExame) {
        this.idExame = idExame;
    }

    public Exame(String exame) {
        this.exame = exame;
    }
    
    public Exame(Integer idExame, String exame) {
        this.idExame = idExame;
        this.exame = exame;
    }

    public Integer getIdExame() {
        return idExame;
    }

    public void setIdExame(Integer idExame) {
        this.idExame = idExame;
    }

    public String getExame() {
        return exame;
    }

    public void setExame(String exame) {
        this.exame = exame;
    }

    public TipoExame getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(TipoExame tipoExame) {
        this.tipoExame = tipoExame;
    }

    @XmlTransient
    public Collection<PedidoExame> getPedidoExameCollection() {
        return pedidoexameCollection;
    }

    public void setPedidoExameCollection(Collection<PedidoExame> pedidoexameCollection) {
        this.pedidoexameCollection = pedidoexameCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idExame != null ? idExame.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Exame)) {
            return false;
        }
        Exame other = (Exame) object;
        if ((this.idExame == null && other.idExame != null) || (this.idExame != null && !this.idExame.equals(other.idExame))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Exame[ idExame=" + idExame + " ]";
    }
    
}
