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

/**
 *
 * @author Adriano Xavier
 */
@Entity
@Table(name = "atestado")
@NamedQueries({
    @NamedQuery(name = "Atestado.findAll", query = "SELECT a FROM Atestado a")})
public class Atestado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDATESTADO")
    private Integer idatestado;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATAATESTADO")
    @Temporal(TemporalType.DATE)
    private Date dataatestado;
    @JoinColumn(name = "Consulta", referencedColumnName = "IDCONSULTA")
    @ManyToOne(optional = false)
    private Consulta consulta;
    @JoinColumn(name = "TipoAtestado", referencedColumnName = "IDTIPOATESTADO")
    @ManyToOne(optional = false)
    private Tipoatestado tipoAtestado;

    public Atestado() {
    }

    public Atestado(Integer idatestado) {
        this.idatestado = idatestado;
    }

    public Atestado(Integer idatestado, Date dataatestado) {
        this.idatestado = idatestado;
        this.dataatestado = dataatestado;
    }

    public Integer getIdatestado() {
        return idatestado;
    }

    public void setIdatestado(Integer idatestado) {
        this.idatestado = idatestado;
    }

    public Date getDataatestado() {
        return dataatestado;
    }

    public void setDataatestado(Date dataatestado) {
        this.dataatestado = dataatestado;
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
        hash += (idatestado != null ? idatestado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Atestado)) {
            return false;
        }
        Atestado other = (Atestado) object;
        if ((this.idatestado == null && other.idatestado != null) || (this.idatestado != null && !this.idatestado.equals(other.idatestado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Atestado[ idatestado=" + idatestado + " ]";
    }
    
}
