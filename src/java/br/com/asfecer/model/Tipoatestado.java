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

/**
 *
 * @author Adriano Xavier
 */
@Entity
@Table(name = "tipoatestado")
@NamedQueries({
    @NamedQuery(name = "Tipoatestado.findAll", query = "SELECT t FROM Tipoatestado t")})
public class Tipoatestado implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDTIPOATESTADO")
    private Integer idtipoatestado;
    @Size(max = 45)
    @Column(name = "ATIVIDADE")
    private String atividade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "DESCRICAO")
    private String descricao;
    @Column(name = "DIAS")
    private Integer dias;
    @Size(max = 45)
    @Column(name = "LOCALAFASTAMENTO")
    private String localafastamento;
    @JoinColumn(name = "Medicamento", referencedColumnName = "IDMEDICAMENTO")
    @ManyToOne
    private Medicamento medicamento;
    @JoinColumn(name = "Patologia", referencedColumnName = "IDPATOLOGIA")
    @ManyToOne
    private Patologia patologia;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoAtestado")
    private Collection<Atestado> atestadoCollection;

    public Tipoatestado() {
    }

    public Tipoatestado(Integer idtipoatestado) {
        this.idtipoatestado = idtipoatestado;
    }

    public Tipoatestado(Integer idtipoatestado, String descricao) {
        this.idtipoatestado = idtipoatestado;
        this.descricao = descricao;
    }

    public Integer getIdtipoatestado() {
        return idtipoatestado;
    }

    public void setIdtipoatestado(Integer idtipoatestado) {
        this.idtipoatestado = idtipoatestado;
    }

    public String getAtividade() {
        return atividade;
    }

    public void setAtividade(String atividade) {
        this.atividade = atividade;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Integer getDias() {
        return dias;
    }

    public void setDias(Integer dias) {
        this.dias = dias;
    }

    public String getLocalafastamento() {
        return localafastamento;
    }

    public void setLocalafastamento(String localafastamento) {
        this.localafastamento = localafastamento;
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

    public Collection<Atestado> getAtestadoCollection() {
        return atestadoCollection;
    }

    public void setAtestadoCollection(Collection<Atestado> atestadoCollection) {
        this.atestadoCollection = atestadoCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idtipoatestado != null ? idtipoatestado.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Tipoatestado)) {
            return false;
        }
        Tipoatestado other = (Tipoatestado) object;
        if ((this.idtipoatestado == null && other.idtipoatestado != null) || (this.idtipoatestado != null && !this.idtipoatestado.equals(other.idtipoatestado))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Tipoatestado[ idtipoatestado=" + idtipoatestado + " ]";
    }
    
}
