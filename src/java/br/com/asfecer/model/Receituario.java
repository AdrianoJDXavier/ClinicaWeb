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
@Table(name = "receituario")
@NamedQueries({
    @NamedQuery(name = "Receituario.findAll", query = "SELECT r FROM Receituario r")})
public class Receituario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDRECEITUARIO")
    private Integer idreceituario;
    @Basic(optional = false)
    @NotNull
    @Column(name = "DATA")
    @Temporal(TemporalType.DATE)
    private Date data;
    @JoinColumn(name = "Consulta", referencedColumnName = "IDCONSULTA")
    @ManyToOne(optional = false)
    private Consulta consulta;
    @JoinColumn(name = "TipoReceituario", referencedColumnName = "IDITENSRECEITUARIO")
    @ManyToOne(optional = false)
    private Itensreceituario tipoReceituario;

    public Receituario() {
    }

    public Receituario(Integer idreceituario) {
        this.idreceituario = idreceituario;
    }

    public Receituario(Integer idreceituario, Date data) {
        this.idreceituario = idreceituario;
        this.data = data;
    }

    public Integer getIdreceituario() {
        return idreceituario;
    }

    public void setIdreceituario(Integer idreceituario) {
        this.idreceituario = idreceituario;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    public Itensreceituario getTipoReceituario() {
        return tipoReceituario;
    }

    public void setTipoReceituario(Itensreceituario tipoReceituario) {
        this.tipoReceituario = tipoReceituario;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idreceituario != null ? idreceituario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Receituario)) {
            return false;
        }
        Receituario other = (Receituario) object;
        if ((this.idreceituario == null && other.idreceituario != null) || (this.idreceituario != null && !this.idreceituario.equals(other.idreceituario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Receituario[ idreceituario=" + idreceituario + " ]";
    }
    
}
