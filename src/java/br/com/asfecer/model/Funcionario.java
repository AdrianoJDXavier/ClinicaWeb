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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author PToledo
 */
@Entity
@Table(catalog = "db_asfecer", schema = "")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Funcionario.findAll", query = "SELECT f FROM Funcionario f")
    , @NamedQuery(name = "Funcionario.findByIdFuncionario", query = "SELECT f FROM Funcionario f WHERE f.idFuncionario = :idFuncionario")
    , @NamedQuery(name = "Funcionario.findByNomeFuncionario", query = "SELECT f FROM Funcionario f WHERE f.nomeFuncionario = :nomeFuncionario")
    , @NamedQuery(name = "Funcionario.findByDataNascimento", query = "SELECT f FROM Funcionario f WHERE f.dataNascimento = :dataNascimento")
    , @NamedQuery(name = "Funcionario.findByCpf", query = "SELECT f FROM Funcionario f WHERE f.cpf = :cpf")
    , @NamedQuery(name = "Funcionario.findByRg", query = "SELECT f FROM Funcionario f WHERE f.rg = :rg")
    , @NamedQuery(name = "Funcionario.findByOrgaoEmissor", query = "SELECT f FROM Funcionario f WHERE f.orgaoEmissor = :orgaoEmissor")
    , @NamedQuery(name = "Funcionario.findByCtps", query = "SELECT f FROM Funcionario f WHERE f.ctps = :ctps")
    , @NamedQuery(name = "Funcionario.findByPis", query = "SELECT f FROM Funcionario f WHERE f.pis = :pis")
    , @NamedQuery(name = "Funcionario.findByEmail", query = "SELECT f FROM Funcionario f WHERE f.email = :email")
    , @NamedQuery(name = "Funcionario.findByTelefone", query = "SELECT f FROM Funcionario f WHERE f.telefone = :telefone")
    , @NamedQuery(name = "Funcionario.findByCelular", query = "SELECT f FROM Funcionario f WHERE f.celular = :celular")
    , @NamedQuery(name = "Funcionario.findByObs", query = "SELECT f FROM Funcionario f WHERE f.obs = :obs")})
public class Funcionario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idFuncionario;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String nomeFuncionario;
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @Size(max = 14)
    @Column(length = 14)
    private String cpf;
    @Size(max = 15)
    @Column(length = 15)
    private String rg;
    @Size(max = 10)
    @Column(length = 10)
    private String orgaoEmissor;
    @Size(max = 20)
    @Column(length = 20)
    private String ctps;
    @Size(max = 20)
    @Column(length = 20)
    private String pis;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(length = 100)
    private String email;
    @Size(max = 20)
    @Column(length = 20)
    private String telefone;
    @Size(max = 20)
    @Column(length = 20)
    private String celular;
    @Size(max = 300)
    @Column(length = 300)
    private String obs;
    @JoinColumn(name = "Cargo", referencedColumnName = "idCargo", nullable = false)
    @ManyToOne(optional = false)
    private Cargo cargo;
    @JoinColumn(name = "Endereco", referencedColumnName = "idEndereco")
    @ManyToOne
    private Endereco endereco;
    @JoinColumn(name = "UfEmissor", referencedColumnName = "sigla", nullable = false)
    @ManyToOne(optional = false)
    private Estados ufEmissor;

    public Funcionario() {
    }

    public Funcionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public Funcionario(Integer idFuncionario, String nomeFuncionario) {
        this.idFuncionario = idFuncionario;
        this.nomeFuncionario = nomeFuncionario;
    }

    public Integer getIdFuncionario() {
        return idFuncionario;
    }

    public void setIdFuncionario(Integer idFuncionario) {
        this.idFuncionario = idFuncionario;
    }

    public String getNomeFuncionario() {
        return nomeFuncionario;
    }

    public void setNomeFuncionario(String nomeFuncionario) {
        this.nomeFuncionario = nomeFuncionario;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getRg() {
        return rg;
    }

    public void setRg(String rg) {
        this.rg = rg;
    }

    public String getOrgaoEmissor() {
        return orgaoEmissor;
    }

    public void setOrgaoEmissor(String orgaoEmissor) {
        this.orgaoEmissor = orgaoEmissor;
    }

    public String getCtps() {
        return ctps;
    }

    public void setCtps(String ctps) {
        this.ctps = ctps;
    }

    public String getPis() {
        return pis;
    }

    public void setPis(String pis) {
        this.pis = pis;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCelular() {
        return celular;
    }

    public void setCelular(String celular) {
        this.celular = celular;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
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
        hash += (idFuncionario != null ? idFuncionario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Funcionario)) {
            return false;
        }
        Funcionario other = (Funcionario) object;
        if ((this.idFuncionario == null && other.idFuncionario != null) || (this.idFuncionario != null && !this.idFuncionario.equals(other.idFuncionario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Funcionario[ idFuncionario=" + idFuncionario + " ]";
    }
    
}
