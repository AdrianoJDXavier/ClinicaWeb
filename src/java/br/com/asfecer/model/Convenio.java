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
    @NamedQuery(name = "Convenio.findAll", query = "SELECT c FROM Convenio c")
    , @NamedQuery(name = "Convenio.findByIdConvenio", query = "SELECT c FROM Convenio c WHERE c.idConvenio = :idConvenio")
    , @NamedQuery(name = "Convenio.findByEmpresaConvenio", query = "SELECT c FROM Convenio c WHERE c.empresaConvenio = :empresaConvenio")
    , @NamedQuery(name = "Convenio.findByTipoConvenio", query = "SELECT c FROM Convenio c WHERE c.tipoConvenio = :tipoConvenio")
    , @NamedQuery(name = "Convenio.findByTelefone", query = "SELECT c FROM Convenio c WHERE c.telefone = :telefone")
    , @NamedQuery(name = "Convenio.findByStatus", query = "SELECT c FROM Convenio c WHERE c.status = :status")
    , @NamedQuery(name = "Convenio.findByObs", query = "SELECT c FROM Convenio c WHERE c.obs = :obs")})
public class Convenio implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idConvenio;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 60)
    @Column(name = "empresa_convenio", nullable = false, length = 60)
    private String empresaConvenio;
    @Size(max = 30)
    @Column(name = "tipo_convenio", length = 30)
    private String tipoConvenio;
    @Size(max = 20)
    @Column(length = 20)
    private String telefone;
    private Boolean status;
    @Size(max = 300)
    @Column(length = 300)
    private String obs;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "convenio")
    private Collection<Paciente> pacienteCollection;

    public Convenio() {
    }

    public Convenio(Integer idConvenio) {
        this.idConvenio = idConvenio;
    }

    public Convenio(Integer idConvenio, String empresaConvenio) {
        this.idConvenio = idConvenio;
        this.empresaConvenio = empresaConvenio;
    }

    public Integer getIdConvenio() {
        return idConvenio;
    }

    public void setIdConvenio(Integer idConvenio) {
        this.idConvenio = idConvenio;
    }

    public String getEmpresaConvenio() {
        return empresaConvenio;
    }

    public void setEmpresaConvenio(String empresaConvenio) {
        this.empresaConvenio = empresaConvenio;
    }

    public String getTipoConvenio() {
        return tipoConvenio;
    }

    public void setTipoConvenio(String tipoConvenio) {
        this.tipoConvenio = tipoConvenio;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    @XmlTransient
    public Collection<Paciente> getPacienteCollection() {
        return pacienteCollection;
    }

    public void setPacienteCollection(Collection<Paciente> pacienteCollection) {
        this.pacienteCollection = pacienteCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idConvenio != null ? idConvenio.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Convenio)) {
            return false;
        }
        Convenio other = (Convenio) object;
        if ((this.idConvenio == null && other.idConvenio != null) || (this.idConvenio != null && !this.idConvenio.equals(other.idConvenio))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Convenio[ idConvenio=" + idConvenio + " ]";
    }
    
}
