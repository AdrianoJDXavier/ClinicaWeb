/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
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

/**
 *
 * @author Adriano Xavier
 */
@Entity
@Table(name = "endereco")
@NamedQueries({
    @NamedQuery(name = "Endereco.findAll", query = "SELECT e FROM Endereco e")})
public class Endereco implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDENDERECO")
    private Integer idendereco;
    @Size(max = 45)
    @Column(name = "BAIRRO")
    private String bairro;
    @Size(max = 10)
    @Column(name = "CEP")
    private String cep;
    @Size(max = 15)
    @Column(name = "COMPLEMENTO")
    private String complemento;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome_nogradouro")
    private String nomeNogradouro;
    @Column(name = "NUMERO")
    private Integer numero;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipo_logradouro")
    private String tipoLogradouro;
    @JoinColumn(name = "Cidade", referencedColumnName = "IDCIDADE")
    @ManyToOne(optional = false)
    private Cidade cidade;
    @OneToMany(mappedBy = "endereco")
    private Collection<Paciente> pacienteCollection;
    @OneToMany(mappedBy = "endereco")
    private Collection<Funcionario> funcionarioCollection;

    public Endereco() {
    }

    public Endereco(Integer idendereco) {
        this.idendereco = idendereco;
    }

    public Endereco(Integer idendereco, String nomeNogradouro, String tipoLogradouro) {
        this.idendereco = idendereco;
        this.nomeNogradouro = nomeNogradouro;
        this.tipoLogradouro = tipoLogradouro;
    }

    public Integer getIdendereco() {
        return idendereco;
    }

    public void setIdendereco(Integer idendereco) {
        this.idendereco = idendereco;
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

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getNomeNogradouro() {
        return nomeNogradouro;
    }

    public void setNomeNogradouro(String nomeNogradouro) {
        this.nomeNogradouro = nomeNogradouro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public String getTipoLogradouro() {
        return tipoLogradouro;
    }

    public void setTipoLogradouro(String tipoLogradouro) {
        this.tipoLogradouro = tipoLogradouro;
    }

    public Cidade getCidade() {
        return cidade;
    }

    public void setCidade(Cidade cidade) {
        this.cidade = cidade;
    }

    public Collection<Paciente> getPacienteCollection() {
        return pacienteCollection;
    }

    public void setPacienteCollection(Collection<Paciente> pacienteCollection) {
        this.pacienteCollection = pacienteCollection;
    }

    public Collection<Funcionario> getFuncionarioCollection() {
        return funcionarioCollection;
    }

    public void setFuncionarioCollection(Collection<Funcionario> funcionarioCollection) {
        this.funcionarioCollection = funcionarioCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idendereco != null ? idendereco.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Endereco)) {
            return false;
        }
        Endereco other = (Endereco) object;
        if ((this.idendereco == null && other.idendereco != null) || (this.idendereco != null && !this.idendereco.equals(other.idendereco))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Endereco[ idendereco=" + idendereco + " ]";
    }
    
}
