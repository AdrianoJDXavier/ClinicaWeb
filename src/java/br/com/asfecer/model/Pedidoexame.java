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
    @NamedQuery(name = "Pedidoexame.findAll", query = "SELECT p FROM Pedidoexame p")
    , @NamedQuery(name = "Pedidoexame.findByIdPedidoExame", query = "SELECT p FROM Pedidoexame p WHERE p.idPedidoExame = :idPedidoExame")
    , @NamedQuery(name = "Pedidoexame.findByData", query = "SELECT p FROM Pedidoexame p WHERE p.data = :data")})
public class Pedidoexame implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idPedidoExame;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date data;
    @JoinColumn(name = "Consulta", referencedColumnName = "idConsulta", nullable = false)
    @ManyToOne(optional = false)
    private Consulta consulta;
    @JoinColumn(name = "Exame", referencedColumnName = "idExame", nullable = false)
    @ManyToOne(optional = false)
    private Exame exame;

    public Pedidoexame() {
    }

    public Pedidoexame(Integer idPedidoExame) {
        this.idPedidoExame = idPedidoExame;
    }

    public Pedidoexame(Integer idPedidoExame, Date data) {
        this.idPedidoExame = idPedidoExame;
        this.data = data;
    }

    public Integer getIdPedidoExame() {
        return idPedidoExame;
    }

    public void setIdPedidoExame(Integer idPedidoExame) {
        this.idPedidoExame = idPedidoExame;
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

    public Exame getExame() {
        return exame;
    }

    public void setExame(Exame exame) {
        this.exame = exame;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPedidoExame != null ? idPedidoExame.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Pedidoexame)) {
            return false;
        }
        Pedidoexame other = (Pedidoexame) object;
        if ((this.idPedidoExame == null && other.idPedidoExame != null) || (this.idPedidoExame != null && !this.idPedidoExame.equals(other.idPedidoExame))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Pedidoexame[ idPedidoExame=" + idPedidoExame + " ]";
    }
    
}
