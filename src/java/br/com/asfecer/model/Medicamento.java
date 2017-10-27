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
@Table(name = "medicamento")
@NamedQueries({
    @NamedQuery(name = "Medicamento.findAll", query = "SELECT m FROM Medicamento m")})
public class Medicamento implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDMEDICAMENTO")
    private Integer idmedicamento;
    @Size(max = 200)
    @Column(name = "APRESENTACAO")
    private String apresentacao;
    @Size(max = 45)
    @Column(name = "LABORATORIO")
    private String laboratorio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "MEDICAMENTO")
    private String medicamento;
    @Size(max = 60)
    @Column(name = "PRINCIPIOATIVO")
    private String principioativo;
    @OneToMany(mappedBy = "medicamento")
    private Collection<Tipoatestado> tipoatestadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicamento")
    private Collection<Itensreceituario> itensreceituarioCollection;

    public Medicamento() {
    }

    public Medicamento(Integer idmedicamento) {
        this.idmedicamento = idmedicamento;
    }

    public Medicamento(Integer idmedicamento, String medicamento) {
        this.idmedicamento = idmedicamento;
        this.medicamento = medicamento;
    }

    public Integer getIdmedicamento() {
        return idmedicamento;
    }

    public void setIdmedicamento(Integer idmedicamento) {
        this.idmedicamento = idmedicamento;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getPrincipioativo() {
        return principioativo;
    }

    public void setPrincipioativo(String principioativo) {
        this.principioativo = principioativo;
    }

    public Collection<Tipoatestado> getTipoatestadoCollection() {
        return tipoatestadoCollection;
    }

    public void setTipoatestadoCollection(Collection<Tipoatestado> tipoatestadoCollection) {
        this.tipoatestadoCollection = tipoatestadoCollection;
    }

    public Collection<Itensreceituario> getItensreceituarioCollection() {
        return itensreceituarioCollection;
    }

    public void setItensreceituarioCollection(Collection<Itensreceituario> itensreceituarioCollection) {
        this.itensreceituarioCollection = itensreceituarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idmedicamento != null ? idmedicamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicamento)) {
            return false;
        }
        Medicamento other = (Medicamento) object;
        if ((this.idmedicamento == null && other.idmedicamento != null) || (this.idmedicamento != null && !this.idmedicamento.equals(other.idmedicamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Medicamento[ idmedicamento=" + idmedicamento + " ]";
    }
    
}
