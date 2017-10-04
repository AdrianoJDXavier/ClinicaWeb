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
    @NamedQuery(name = "Itensreceituario.findAll", query = "SELECT i FROM Itensreceituario i")
    , @NamedQuery(name = "Itensreceituario.findByIdItensReceituario", query = "SELECT i FROM Itensreceituario i WHERE i.idItensReceituario = :idItensReceituario")
    , @NamedQuery(name = "Itensreceituario.findByOrdem", query = "SELECT i FROM Itensreceituario i WHERE i.ordem = :ordem")
    , @NamedQuery(name = "Itensreceituario.findByQuantidade", query = "SELECT i FROM Itensreceituario i WHERE i.quantidade = :quantidade")
    , @NamedQuery(name = "Itensreceituario.findByPosologia", query = "SELECT i FROM Itensreceituario i WHERE i.posologia = :posologia")
    , @NamedQuery(name = "Itensreceituario.findByDose", query = "SELECT i FROM Itensreceituario i WHERE i.dose = :dose")
    , @NamedQuery(name = "Itensreceituario.findByTipoUso", query = "SELECT i FROM Itensreceituario i WHERE i.tipoUso = :tipoUso")})
public class Itensreceituario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idItensReceituario;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int ordem;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private int quantidade;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 200)
    @Column(nullable = false, length = 200)
    private String posologia;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(nullable = false, length = 45)
    private String dose;
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private boolean tipoUso;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tipoReceituario")
    private Collection<Receituario> receituarioCollection;
    @JoinColumn(name = "Medicamento", referencedColumnName = "idMedicamento", nullable = false)
    @ManyToOne(optional = false)
    private Medicamento medicamento;

    public Itensreceituario() {
    }

    public Itensreceituario(Integer idItensReceituario) {
        this.idItensReceituario = idItensReceituario;
    }

    public Itensreceituario(Integer idItensReceituario, int ordem, int quantidade, String posologia, String dose, boolean tipoUso) {
        this.idItensReceituario = idItensReceituario;
        this.ordem = ordem;
        this.quantidade = quantidade;
        this.posologia = posologia;
        this.dose = dose;
        this.tipoUso = tipoUso;
    }

    public Integer getIdItensReceituario() {
        return idItensReceituario;
    }

    public void setIdItensReceituario(Integer idItensReceituario) {
        this.idItensReceituario = idItensReceituario;
    }

    public int getOrdem() {
        return ordem;
    }

    public void setOrdem(int ordem) {
        this.ordem = ordem;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getPosologia() {
        return posologia;
    }

    public void setPosologia(String posologia) {
        this.posologia = posologia;
    }

    public String getDose() {
        return dose;
    }

    public void setDose(String dose) {
        this.dose = dose;
    }

    public boolean getTipoUso() {
        return tipoUso;
    }

    public void setTipoUso(boolean tipoUso) {
        this.tipoUso = tipoUso;
    }

    @XmlTransient
    public Collection<Receituario> getReceituarioCollection() {
        return receituarioCollection;
    }

    public void setReceituarioCollection(Collection<Receituario> receituarioCollection) {
        this.receituarioCollection = receituarioCollection;
    }

    public Medicamento getMedicamento() {
        return medicamento;
    }

    public void setMedicamento(Medicamento medicamento) {
        this.medicamento = medicamento;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idItensReceituario != null ? idItensReceituario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Itensreceituario)) {
            return false;
        }
        Itensreceituario other = (Itensreceituario) object;
        if ((this.idItensReceituario == null && other.idItensReceituario != null) || (this.idItensReceituario != null && !this.idItensReceituario.equals(other.idItensReceituario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Itensreceituario[ idItensReceituario=" + idItensReceituario + " ]";
    }
    
}
