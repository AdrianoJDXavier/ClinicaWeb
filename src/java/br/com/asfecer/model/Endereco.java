package br.com.asfecer.model;

import java.io.Serializable;
import java.util.Collection;
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
    @NamedQuery(name = "Endereco.findAll", query = "SELECT e FROM Endereco e")
    , @NamedQuery(name = "Endereco.findByIdEndereco", query = "SELECT e FROM Endereco e WHERE e.idEndereco = :idEndereco")
    , @NamedQuery(name = "Endereco.findByTipoLogradouro", query = "SELECT e FROM Endereco e WHERE e.tipoLogradouro = :tipoLogradouro")
    , @NamedQuery(name = "Endereco.findByNomeNogradouro", query = "SELECT e FROM Endereco e WHERE e.nomeNogradouro = :nomeNogradouro")
    , @NamedQuery(name = "Endereco.findByNumero", query = "SELECT e FROM Endereco e WHERE e.numero = :numero")
    , @NamedQuery(name = "Endereco.findByComplemento", query = "SELECT e FROM Endereco e WHERE e.complemento = :complemento")
    , @NamedQuery(name = "Endereco.findByBairro", query = "SELECT e FROM Endereco e WHERE e.bairro = :bairro")
    , @NamedQuery(name = "Endereco.findByCep", query = "SELECT e FROM Endereco e WHERE e.cep = :cep")})
public class Endereco implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idEndereco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipo_logradouro", nullable = false, length = 20)
    private String tipoLogradouro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome_logradouro", nullable = false, length = 100)
    private String nomelogradouro;
    private Integer numero;
    @Size(max = 15)
    @Column(length = 15)
    private String complemento;
    @Size(max = 45)
    @Column(length = 45)
    private String bairro;
    @Size(max = 10)
    @Column(length = 10)
    private String cep;
    @JoinColumn(name = "Cidade", referencedColumnName = "idCidade", nullable = false)
    @ManyToOne(optional = false)
    private Cidade cidade;
    @OneToMany(mappedBy = "endereco")
    private Collection<Paciente> pacienteCollection;
    @OneToMany(mappedBy = "endereco")
    private Collection<Funcionario> funcionarioCollection;

    public Endereco() {
    }

    public Endereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public Endereco(Integer idEndereco, String tipoLogradouro, String nomeNogradouro) {
        this.idEndereco = idEndereco;
        this.tipoLogradouro = tipoLogradouro;
        this.nomelogradouro = nomeNogradouro;
    }

    public Endereco(String tipoLogradouro, String nomeNogradouro, Integer numero, String complemento, String bairro, String cep, Cidade cidade) {
        this.tipoLogradouro = tipoLogradouro;
        this.nomelogradouro = nomeNogradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
    }

    public Endereco(Integer idEndereco, String tipoLogradouro, String nomeNogradouro, Integer numero, String complemento, String bairro, String cep, Cidade cidade) {
        this.idEndereco = idEndereco;
        this.tipoLogradouro = tipoLogradouro;
        this.nomelogradouro = nomeNogradouro;
        this.numero = numero;
        this.complemento = complemento;
        this.bairro = bairro;
        this.cep = cep;
        this.cidade = cidade;
    }

    public Integer getIdEndereco() {
        return idEndereco;
    }

    public void setIdEndereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public String getNomelogradouro() {
        return nomelogradouro;
    }

    public void setNomelogradouro(String nomelogradouro) {
        this.nomelogradouro = nomelogradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    @XmlTransient
    public Collection<Paciente> getPacienteCollection() {
        return pacienteCollection;
    }

    public void setPacienteCollection(Collection<Paciente> pacienteCollection) {
        this.pacienteCollection = pacienteCollection;
    }

    @XmlTransient
    public Collection<Funcionario> getFuncionarioCollection() {
        return funcionarioCollection;
    }

    public void setFuncionarioCollection(Collection<Funcionario> funcionarioCollection) {
        this.funcionarioCollection = funcionarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idEndereco != null ? idEndereco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Endereco)) {
            return false;
        }
        Endereco other = (Endereco) object;
        if ((this.idEndereco == null && other.idEndereco != null) || (this.idEndereco != null && !this.idEndereco.equals(other.idEndereco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Endereco[ idEndereco=" + idEndereco + " ]";
    }
    
}
