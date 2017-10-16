/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author PToledo
 */
@Entity
@Table(catalog = "db_asfecer", schema = "", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"cpf"})
    , @UniqueConstraint(columnNames = {"cartaoConvenio"})})
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p")
    , @NamedQuery(name = "Paciente.findByIdPaciente", query = "SELECT p FROM Paciente p WHERE p.idPaciente = :idPaciente")
    , @NamedQuery(name = "Paciente.findByNomePaciente", query = "SELECT p FROM Paciente p WHERE p.nomePaciente = :nomePaciente")
    , @NamedQuery(name = "Paciente.findByDataNascimento", query = "SELECT p FROM Paciente p WHERE p.dataNascimento = :dataNascimento")
    , @NamedQuery(name = "Paciente.findByNomeMae", query = "SELECT p FROM Paciente p WHERE p.nomeMae = :nomeMae")
    , @NamedQuery(name = "Paciente.findByCpf", query = "SELECT p FROM Paciente p WHERE p.cpf = :cpf")
    , @NamedQuery(name = "Paciente.findByCartaoConvenio", query = "SELECT p FROM Paciente p WHERE p.cartaoConvenio = :cartaoConvenio")
    , @NamedQuery(name = "Paciente.findByTipoSanguineo", query = "SELECT p FROM Paciente p WHERE p.tipoSanguineo = :tipoSanguineo")
    , @NamedQuery(name = "Paciente.findByFatorRH", query = "SELECT p FROM Paciente p WHERE p.fatorRH = :fatorRH")
    , @NamedQuery(name = "Paciente.findBySexo", query = "SELECT p FROM Paciente p WHERE p.sexo = :sexo")
    , @NamedQuery(name = "Paciente.findByEmail", query = "SELECT p FROM Paciente p WHERE p.email = :email")
    , @NamedQuery(name = "Paciente.findByTelefone", query = "SELECT p FROM Paciente p WHERE p.telefone = :telefone")
    , @NamedQuery(name = "Paciente.findByCelular", query = "SELECT p FROM Paciente p WHERE p.celular = :celular")
    , @NamedQuery(name = "Paciente.findByObs", query = "SELECT p FROM Paciente p WHERE p.obs = :obs")})
public class Paciente implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(nullable = false)
    private Integer idPaciente;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(nullable = false, length = 100)
    private String nomePaciente;
    @Temporal(TemporalType.DATE)
    private Date dataNascimento;
    @Size(max = 100)
    @Column(length = 100)
    private String nomeMae;
    @Size(max = 14)
    @Column(length = 14)
    private String cpf;
    @Size(max = 30)
    @Column(length = 30)
    private String cartaoConvenio;
    @Size(max = 2)
    @Column(length = 2)
    private String tipoSanguineo;
    private Character fatorRH;
    private Character sexo;
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
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private Collection<Agenda> agendaCollection;
    @JoinColumn(name = "Convenio", referencedColumnName = "idConvenio", nullable = false)
    @ManyToOne(optional = false)
    private Convenio convenio;
    @JoinColumn(name = "Endereco", referencedColumnName = "idEndereco")
    @ManyToOne
    private Endereco endereco;
    @JoinColumn(name = "NaturalidadeCidade", referencedColumnName = "idCidade")
    @ManyToOne
    private Cidade naturalidadeCidade;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private Collection<Consulta> consultaCollection;

    public Paciente() {
    }

    public Paciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public Paciente(Integer idPaciente, String nomePaciente) {
        this.idPaciente = idPaciente;
        this.nomePaciente = nomePaciente;
    }

    public Paciente(String nomePaciente, Date dataNascimento, String nomeMae, String cpf, String cartaoConvenio, String tipoSanguineo, Character fatorRH, Character sexo, String email, String telefone, String celular, String obs, Convenio convenio, Endereco endereco, Cidade naturalidadeCidade) {
        this.nomePaciente = nomePaciente;
        this.dataNascimento = dataNascimento;
        this.nomeMae = nomeMae;
        this.cpf = cpf;
        this.cartaoConvenio = cartaoConvenio;
        this.tipoSanguineo = tipoSanguineo;
        this.fatorRH = fatorRH;
        this.sexo = sexo;
        this.email = email;
        this.telefone = telefone;
        this.celular = celular;
        this.obs = obs;
        this.convenio = convenio;
        this.endereco = endereco;
        this.naturalidadeCidade = naturalidadeCidade;
    }

    public Paciente(Integer idPaciente, String nomePaciente, Date dataNascimento, String nomeMae, String cpf, String cartaoConvenio, String tipoSanguineo, Character fatorRH, Character sexo, String email, String telefone, String celular, String obs, Convenio convenio, Endereco endereco, Cidade naturalidadeCidade) {
        this.idPaciente = idPaciente;
        this.nomePaciente = nomePaciente;
        this.dataNascimento = dataNascimento;
        this.nomeMae = nomeMae;
        this.cpf = cpf;
        this.cartaoConvenio = cartaoConvenio;
        this.tipoSanguineo = tipoSanguineo;
        this.fatorRH = fatorRH;
        this.sexo = sexo;
        this.email = email;
        this.telefone = telefone;
        this.celular = celular;
        this.obs = obs;
        this.convenio = convenio;
        this.endereco = endereco;
        this.naturalidadeCidade = naturalidadeCidade;
    }
    
    
    
    public Integer getIdPaciente() {
        return idPaciente;
    }

    public void setIdPaciente(Integer idPaciente) {
        this.idPaciente = idPaciente;
    }

    public String getNomePaciente() {
        return nomePaciente;
    }

    public void setNomePaciente(String nomePaciente) {
        this.nomePaciente = nomePaciente;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getCartaoConvenio() {
        return cartaoConvenio;
    }

    public void setCartaoConvenio(String cartaoConvenio) {
        this.cartaoConvenio = cartaoConvenio;
    }

    public String getTipoSanguineo() {
        return tipoSanguineo;
    }

    public void setTipoSanguineo(String tipoSanguineo) {
        this.tipoSanguineo = tipoSanguineo;
    }

    public Character getFatorRH() {
        return fatorRH;
    }

    public void setFatorRH(Character fatorRH) {
        this.fatorRH = fatorRH;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
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

    @XmlTransient
    public Collection<Agenda> getAgendaCollection() {
        return agendaCollection;
    }

    public void setAgendaCollection(Collection<Agenda> agendaCollection) {
        this.agendaCollection = agendaCollection;
    }

    public Convenio getConvenio() {
        return convenio;
    }

    public void setConvenio(Convenio convenio) {
        this.convenio = convenio;
    }

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    public Cidade getNaturalidadeCidade() {
        return naturalidadeCidade;
    }

    public void setNaturalidadeCidade(Cidade naturalidadeCidade) {
        this.naturalidadeCidade = naturalidadeCidade;
    }

    @XmlTransient
    public Collection<Consulta> getConsultaCollection() {
        return consultaCollection;
    }

    public void setConsultaCollection(Collection<Consulta> consultaCollection) {
        this.consultaCollection = consultaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idPaciente != null ? idPaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.idPaciente == null && other.idPaciente != null) || (this.idPaciente != null && !this.idPaciente.equals(other.idPaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Paciente[ idPaciente=" + idPaciente + " ]";
    }
    
}
