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
 * @author Paulo
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
    @Column(name = "idEndereco")
    private Integer idEndereco;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 20)
    @Column(name = "tipo_logradouro")
    private String tipoLogradouro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "nome_nogradouro")
    private String nomeNogradouro;
    @Column(name = "numero")
    private Integer numero;
    @Size(max = 15)
    @Column(name = "complemento")
    private String complemento;
    @Size(max = 45)
    @Column(name = "bairro")
    private String bairro;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 45)
    @Column(name = "cidade")
    private String cidade;
    @Size(max = 10)
    @Column(name = "cep")
    private String cep;
    @JoinColumn(name = "Uf", referencedColumnName = "sigla")
    @ManyToOne(optional = false)
    private Estados estados;
    @OneToMany(mappedBy = "endereco")
    private Collection<Paciente> pacienteCollection;

    public Endereco() {
    }

    public Endereco(Integer idEndereco) {
        this.idEndereco = idEndereco;
    }

    public Endereco(Integer idEndereco, String tipoLogradouro, String nomeNogradouro, String cidade) {
        this.idEndereco = idEndereco;
        this.tipoLogradouro = tipoLogradouro;
        this.nomeNogradouro = nomeNogradouro;
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

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public Estados getEstados() {
        return estados;
    }

    public void setEstados(Estados estados) {
        this.estados = estados;
    }

    public Collection<Paciente> getPacienteCollection() {
        return pacienteCollection;
    }

    public void setPacienteCollection(Collection<Paciente> pacienteCollection) {
        this.pacienteCollection = pacienteCollection;
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
