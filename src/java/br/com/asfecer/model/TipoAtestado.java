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
    @NamedQuery(name = "Tipoatestado.findAll", query = "SELECT t FROM Tipoatestado t")
    , @NamedQuery(name = "Tipoatestado.findByIdTipoAtestado", query = "SELECT t FROM Tipoatestado t WHERE t.idTipoAtestado = :idTipoAtestado")
    , @NamedQuery(name = "Tipoatestado.findByDescricao", query = "SELECT t FROM Tipoatestado t WHERE t.descricao = :descricao")
    , @NamedQuery(name = "Tipoatestado.findByLocalAfastamento", query = "SELECT t FROM Tipoatestado t WHERE t.localAfastamento = :localAfastamento")
    , @NamedQuery(name = "Tipoatestado.findByDias", query = "SELECT t FROM Tipoatestado t WHERE t.dias = :dias")
    , @NamedQuery(name = "Tipoatestado.findByAtividade", query = "SELECT t FROM Tipoatestado t WHERE t.atividade = :atividade")})
public class TipoAtestado implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer idTipoAtestado;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String descricao;
    @Size(max = 45)
    @Column(length = 45)
    private String localAfastamento;
    private Integer dias;
    @Size(max = 45)
    @Column(length = 45)
    private String atividade;
    @JoinColumn(name = "Medicamento", referencedColumnName = "idMedicamento")
    @ManyToOne
    private Medicamento medicamento;
    @JoinColumn(name = "Patologia", referencedColumnName = "idPatologia")
    @ManyToOne
    private Patologia patologia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAtestado")
    private Collection<Atestado> atestadoCollection;

    public TipoAtestado() {
    }

    public TipoAtestado(Integer idTipoAtestado) {
        this.idTipoAtestado = idTipoAtestado;
    }

    public TipoAtestado(Integer idTipoAtestado, String descricao) {
        this.idTipoAtestado = idTipoAtestado;
        this.descricao = descricao;
    }

    public TipoAtestado(String descricao, String localAfastamento, Integer dias, String atividade, Medicamento medicamento, Patologia patologia) {
        this.descricao = descricao;
        this.localAfastamento = localAfastamento;
        this.dias = dias;
        this.atividade = atividade;
        this.medicamento = medicamento;
        this.patologia = patologia;
    }

    public TipoAtestado(Integer idTipoAtestado, String descricao, String localAfastamento, Integer dias, String atividade, Medicamento medicamento, Patologia patologia) {
        this.idTipoAtestado = idTipoAtestado;
        this.descricao = descricao;
        this.localAfastamento = localAfastamento;
        this.dias = dias;
        this.atividade = atividade;
        this.medicamento = medicamento;
        this.patologia = patologia;
    }

    public Integer getIdTipoAtestado() {
        return idTipoAtestado;
    }

    public void setIdTipoAtestado(Integer idTipoAtestado) {
        this.idTipoAtestado = idTipoAtestado;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getLocalAfastamento() {
        return localAfastamento;
    }

    public void setLocalAfastamento(String localAfastamento) {
        this.localAfastamento = localAfastamento;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    public Patologia getPatologia() {
        return patologia;
    }

    public void setPatologia(Patologia patologia) {
        this.patologia = patologia;
    }

    @XmlTransient
    public Collection<Atestado> getAtestadoCollection() {
        return atestadoCollection;
    }

    public void setAtestadoCollection(Collection<Atestado> atestadoCollection) {
        this.atestadoCollection = atestadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idTipoAtestado != null ? idTipoAtestado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof TipoAtestado)) {
            return false;
        }
        TipoAtestado other = (TipoAtestado) object;
        if ((this.idTipoAtestado == null && other.idTipoAtestado != null) || (this.idTipoAtestado != null && !this.idTipoAtestado.equals(other.idTipoAtestado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Tipoatestado[ idTipoAtestado=" + idTipoAtestado + " ]";
    }
    
}
