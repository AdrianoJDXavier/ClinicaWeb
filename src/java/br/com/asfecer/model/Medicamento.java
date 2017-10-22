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
    @NamedQuery(name = "Medicamento.findAll", query = "SELECT m FROM Medicamento m")
    , @NamedQuery(name = "Medicamento.findByIdMedicamento", query = "SELECT m FROM Medicamento m WHERE m.idMedicamento = :idMedicamento")
    , @NamedQuery(name = "Medicamento.findByPrincipioAtivo", query = "SELECT m FROM Medicamento m WHERE m.principioAtivo = :principioAtivo")
    , @NamedQuery(name = "Medicamento.findByMedicamento", query = "SELECT m FROM Medicamento m WHERE m.medicamento = :medicamento")
    , @NamedQuery(name = "Medicamento.findByLaboratorio", query = "SELECT m FROM Medicamento m WHERE m.laboratorio = :laboratorio")
    , @NamedQuery(name = "Medicamento.findByApresentacao", query = "SELECT m FROM Medicamento m WHERE m.apresentacao = :apresentacao")})
public class Medicamento implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idMedicamento;
    @Size(max = 60)
    @Column(length = 60)
    private String principioAtivo;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String medicamento;
    @Size(max = 45)
    @Column(length = 45)
    private String laboratorio;
    @Size(max = 200)
    @Column(length = 200)
    private String apresentacao;
    @OneToMany(mappedBy = "medicamento")
    private Collection<TipoAtestado> tipoatestadoCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "medicamento")
    private Collection<ItensReceituario> itensReceituarioCollection;

    public Medicamento() {
    }

    public Medicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public Medicamento(Integer idMedicamento, String medicamento) {
        this.idMedicamento = idMedicamento;
        this.medicamento = medicamento;
    }

    public Medicamento(String principioAtivo, String medicamento, String laboratorio, String apresentacao) {
        this.principioAtivo = principioAtivo;
        this.medicamento = medicamento;
        this.laboratorio = laboratorio;
        this.apresentacao = apresentacao;
    }

    public Medicamento(Integer idMedicamento, String principioAtivo, String medicamento, String laboratorio, String apresentacao) {
        this.idMedicamento = idMedicamento;
        this.principioAtivo = principioAtivo;
        this.medicamento = medicamento;
        this.laboratorio = laboratorio;
        this.apresentacao = apresentacao;
    }

    public Integer getIdMedicamento() {
        return idMedicamento;
    }

    public void setIdMedicamento(Integer idMedicamento) {
        this.idMedicamento = idMedicamento;
    }

    public String getPrincipioAtivo() {
        return principioAtivo;
    }

    public void setPrincipioAtivo(String principioAtivo) {
        this.principioAtivo = principioAtivo;
    }

    public String getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(String medicamento) {
        this.medicamento = medicamento;
    }

    public String getLaboratorio() {
        return laboratorio;
    }

    public void setLaboratorio(String laboratorio) {
        this.laboratorio = laboratorio;
    }

    public String getApresentacao() {
        return apresentacao;
    }

    public void setApresentacao(String apresentacao) {
        this.apresentacao = apresentacao;
    }

    @XmlTransient
    public Collection<TipoAtestado> getTipoAtestadoCollection() {
        return tipoatestadoCollection;
    }

    public void setTipoAtestadoCollection(Collection<TipoAtestado> tipoatestadoCollection) {
        this.tipoatestadoCollection = tipoatestadoCollection;
    }

    @XmlTransient
    public Collection<ItensReceituario> getItensReceituarioCollection() {
        return itensReceituarioCollection;
    }

    public void setItensReceituarioCollection(Collection<ItensReceituario> itensReceituarioCollection) {
        this.itensReceituarioCollection = itensReceituarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idMedicamento != null ? idMedicamento.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Medicamento)) {
            return false;
        }
        Medicamento other = (Medicamento) object;
        if ((this.idMedicamento == null && other.idMedicamento != null) || (this.idMedicamento != null && !this.idMedicamento.equals(other.idMedicamento))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Medicamento[ idMedicamento=" + idMedicamento + " ]";
    }
    
}
