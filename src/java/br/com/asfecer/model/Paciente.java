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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Adriano Xavier
 */
@Entity
@Table(name = "paciente")
@NamedQueries({
    @NamedQuery(name = "Paciente.findAll", query = "SELECT p FROM Paciente p")})
public class Paciente implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "IDPACIENTE")
    private Integer idpaciente;
    @Size(max = 30)
    @Column(name = "CARTAOCONVENIO")
    private String cartaoconvenio;
    @Size(max = 20)
    @Column(name = "CELULAR")
    private String celular;
    @Size(max = 14)
    @Column(name = "CPF")
    private String cpf;
    @Column(name = "DATANASCIMENTO")
    @Temporal(TemporalType.DATE)
    private Date datanascimento;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="E-mail inválido")//if the field contains email address consider using this annotation to enforce field validation
    @Size(max = 100)
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "FATORRH")
    private Character fatorrh;
    @Size(max = 100)
    @Column(name = "NOMEMAE")
    private String nomemae;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 100)
    @Column(name = "NOMEPACIENTE")
    private String nomepaciente;
    @Size(max = 300)
    @Column(name = "OBS")
    private String obs;
    @Column(name = "SEXO")
    private Character sexo;
    @Size(max = 20)
    @Column(name = "TELEFONE")
    private String telefone;
    @Size(max = 2)
    @Column(name = "TIPOSANGUINEO")
    private String tiposanguineo;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private Collection<Agenda> agendaCollection;
    @JoinColumn(name = "Convenio", referencedColumnName = "IDCONVENIO")
    @ManyToOne(optional = false)
    private Convenio convenio;
    @JoinColumn(name = "Endereco", referencedColumnName = "IDENDERECO")
    @ManyToOne
    private Endereco endereco;
    @JoinColumn(name = "NaturalidadeCidade", referencedColumnName = "IDCIDADE")
    @ManyToOne
    private Cidade naturalidadeCidade;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "paciente")
    private Collection<Consulta> consultaCollection;

    public Paciente() {
    }

    public Paciente(Integer idpaciente) {
        this.idpaciente = idpaciente;
    }

    public Paciente(Integer idpaciente, String nomepaciente) {
        this.idpaciente = idpaciente;
        this.nomepaciente = nomepaciente;
    }

    public Integer getIdpaciente() {
        return idpaciente;
    }

    public void setIdpaciente(Integer idpaciente) {
        this.idpaciente = idpaciente;
    }

    public String getCartaoconvenio() {
        return cartaoconvenio;
    }

    public void setCartaoconvenio(String cartaoconvenio) {
        this.cartaoconvenio = cartaoconvenio;
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

    public Character getFatorrh() {
        return fatorrh;
    }

    public void setFatorrh(Character fatorrh) {
        this.fatorrh = fatorrh;
    }

    public String getNomemae() {
        return nomemae;
    }

    public void setNomemae(String nomemae) {
        this.nomemae = nomemae;
    }

    public String getNomepaciente() {
        return nomepaciente;
    }

    public void setNomepaciente(String nomepaciente) {
        this.nomepaciente = nomepaciente;
    }

    public String getObs() {
        return obs;
    }

    public void setObs(String obs) {
        this.obs = obs;
    }

    public Character getSexo() {
        return sexo;
    }

    public void setSexo(Character sexo) {
        this.sexo = sexo;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getTiposanguineo() {
        return tiposanguineo;
    }

    public void setTiposanguineo(String tiposanguineo) {
        this.tiposanguineo = tiposanguineo;
    }

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

    public Collection<Consulta> getConsultaCollection() {
        return consultaCollection;
    }

    public void setConsultaCollection(Collection<Consulta> consultaCollection) {
        this.consultaCollection = consultaCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idpaciente != null ? idpaciente.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Paciente)) {
            return false;
        }
        Paciente other = (Paciente) object;
        if ((this.idpaciente == null && other.idpaciente != null) || (this.idpaciente != null && !this.idpaciente.equals(other.idpaciente))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Paciente[ idpaciente=" + idpaciente + " ]";
    }
    
}
