/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PToledo
 */
@Entity
@Table(catalog = "db_asfecer", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Atestado.findAll", query = "SELECT a FROM Atestado a")
    , @NamedQuery(name = "Atestado.findByIdAtestado", query = "SELECT a FROM Atestado a WHERE a.idAtestado = :idAtestado")
    , @NamedQuery(name = "Atestado.findByDataAtestado", query = "SELECT a FROM Atestado a WHERE a.dataAtestado = :dataAtestado")})
public class Atestado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idAtestado;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date dataAtestado;
    @JoinColumn(name = "Consulta", referencedColumnName = "idConsulta", nullable = false)
    @ManyToOne(optional = false)
    private Consulta consulta;
    @JoinColumn(name = "TipoAtestado", referencedColumnName = "idTipoAtestado", nullable = false)
    @ManyToOne(optional = false)
    private Tipoatestado tipoAtestado;

    public Atestado() {
    }

    public Atestado(Integer idAtestado) {
        this.idAtestado = idAtestado;
    }

    public Atestado(Integer idAtestado, Date dataAtestado) {
        this.idAtestado = idAtestado;
        this.dataAtestado = dataAtestado;
    }

    public Integer getIdAtestado() {
        return idAtestado;
    }

    public void setIdAtestado(Integer idAtestado) {
        this.idAtestado = idAtestado;
    }

    public Date getDataAtestado() {
        return dataAtestado;
    }

    public void setDataAtestado(Date dataAtestado) {
        this.dataAtestado = dataAtestado;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Tipoatestado getTipoAtestado() {
        return tipoAtestado;
    }

    public void setTipoAtestado(Tipoatestado tipoAtestado) {
        this.tipoAtestado = tipoAtestado;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idAtestado != null ? idAtestado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Atestado)) {
            return false;
        }
        Atestado other = (Atestado) object;
        if ((this.idAtestado == null && other.idAtestado != null) || (this.idAtestado != null && !this.idAtestado.equals(other.idAtestado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Atestado[ idAtestado=" + idAtestado + " ]";
    }
    
}
