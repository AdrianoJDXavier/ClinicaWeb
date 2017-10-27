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
@Table(name = "exame")
@NamedQueries({
    @NamedQuery(name = "Exame.findAll", query = "SELECT e FROM Exame e")})
public class Exame implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDEXAME")
    private Integer idexame;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "EXAME")
    private String exame;
    @JoinColumn(name = "TipoExame", referencedColumnName = "IDTIPOEXAME")
    @ManyToOne(optional = false)
    private Tipoexame tipoExame;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "exame")
    private Collection<Pedidoexame> pedidoexameCollection;

    public Exame() {
    }

    public Exame(Integer idexame) {
        this.idexame = idexame;
    }

    public Exame(Integer idexame, String exame) {
        this.idexame = idexame;
        this.exame = exame;
    }

    public Integer getIdexame() {
        return idexame;
    }

    public void setIdexame(Integer idexame) {
        this.idexame = idexame;
    }

    public String getExame() {
        return exame;
    }

    public void setExame(String exame) {
        this.exame = exame;
    }

    public Tipoexame getTipoExame() {
        return tipoExame;
    }

    public void setTipoExame(Tipoexame tipoExame) {
        this.tipoExame = tipoExame;
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
        hash += (idexame != null ? idexame.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Exame)) {
            return false;
        }
        Exame other = (Exame) object;
        if ((this.idexame == null && other.idexame != null) || (this.idexame != null && !this.idexame.equals(other.idexame))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Exame[ idexame=" + idexame + " ]";
    }
    
}
