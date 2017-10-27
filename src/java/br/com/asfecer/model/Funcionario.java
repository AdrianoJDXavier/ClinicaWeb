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
import javax.validation.constraints.Size;

/**
 *
 * @author Adriano Xavier
 */
@Entity
@Table(name = "funcionario")
@NamedQueries({
    @NamedQuery(name = "Funcionario.findAll", query = "SELECT f FROM Funcionario f")})
public class Funcionario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDFUNCIONARIO")
    private Integer idfuncionario;
    @Size(max = 20)
    @Column(name = "CELULAR")
    private String celular;
    @Size(max = 14)
    @Column(name = "CPF")
    private String cpf;
    @Size(max = 20)
    @Column(name = "CTPS")
    private String ctps;
    @Column(name = "DATANASCIMENTO")
    @Temporal(TemporalType.DATE)
    private Date datanascimento;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMEFUNCIONARIO")
    private String nomefuncionario;
    @Size(max = 300)
    @Column(name = "OBS")
    private String obs;
    @Size(max = 10)
    @Column(name = "ORGAOEMISSOR")
    private String orgaoemissor;
    @Size(max = 20)
    @Column(name = "PIS")
    private String pis;
    @Size(max = 15)
    @Column(name = "RG")
    private String rg;
    @Size(max = 20)
    @Column(name = "TELEFONE")
    private String telefone;
    @JoinColumn(name = "Cargo", referencedColumnName = "IDCARGO")
    @ManyToOne(optional = false)
    private Cargo cargo;
    @JoinColumn(name = "Endereco", referencedColumnName = "IDENDERECO")
    @ManyToOne
    private Endereco endereco;
    @JoinColumn(name = "UfEmissor", referencedColumnName = "SIGLA")
    @ManyToOne(optional = false)
    private Estados ufEmissor;

    public Funcionario() {
    }

    public Funcionario(Integer idfuncionario) {
        this.idfuncionario = idfuncionario;
    }

    public Funcionario(Integer idfuncionario, String nomefuncionario) {
        this.idfuncionario = idfuncionario;
        this.nomefuncionario = nomefuncionario;
    }

    public Integer getIdfuncionario() {
        return idfuncionario;
    }

    public void setIdfuncionario(Integer idfuncionario) {
        this.idfuncionario = idfuncionario;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCtps() {
        return ctps;
    }

    public void setCtps(String ctps) {
        this.ctps = ctps;
    }

    public Date getDatanascimento() {
        return datanascimento;
    }

    public void setDatanascimento(Date datanascimento) {
        this.datanascimento = datanascimento;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNomefuncionario() {
        return nomefuncionario;
    }

    public void setNomefuncionario(String nomefuncionario) {
        this.nomefuncionario = nomefuncionario;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public String getOrgaoemissor() {
        return orgaoemissor;
    }

    public void setOrgaoemissor(String orgaoemissor) {
        this.orgaoemissor = orgaoemissor;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public Cargo getCargo() {
        return cargo;
    }

    public void setCargo(Cargo cargo) {
        this.cargo = cargo;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Estados getUfEmissor() {
        return ufEmissor;
    }

    public void setUfEmissor(Estados ufEmissor) {
        this.ufEmissor = ufEmissor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idfuncionario != null ? idfuncionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.idfuncionario == null && other.idfuncionario != null) || (this.idfuncionario != null && !this.idfuncionario.equals(other.idfuncionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Funcionario[ idfuncionario=" + idfuncionario + " ]";
    }
    
}
