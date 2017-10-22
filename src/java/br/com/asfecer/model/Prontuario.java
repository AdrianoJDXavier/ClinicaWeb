/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.model;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
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
    @NamedQuery(name = "Prontuario.findAll", query = "SELECT p FROM Prontuario p")
    , @NamedQuery(name = "Prontuario.findByIdProntuario", query = "SELECT p FROM Prontuario p WHERE p.idProntuario = :idProntuario")
    , @NamedQuery(name = "Prontuario.findByQueixaPrincipal", query = "SELECT p FROM Prontuario p WHERE p.queixaPrincipal = :queixaPrincipal")
    , @NamedQuery(name = "Prontuario.findByAnamnese", query = "SELECT p FROM Prontuario p WHERE p.anamnese = :anamnese")
    , @NamedQuery(name = "Prontuario.findByExamesFisicos", query = "SELECT p FROM Prontuario p WHERE p.examesFisicos = :examesFisicos")
    , @NamedQuery(name = "Prontuario.findByExamesComplementares", query = "SELECT p FROM Prontuario p WHERE p.examesComplementares = :examesComplementares")
    , @NamedQuery(name = "Prontuario.findByHipotesesDiagnosticas", query = "SELECT p FROM Prontuario p WHERE p.hipotesesDiagnosticas = :hipotesesDiagnosticas")
    , @NamedQuery(name = "Prontuario.findByDiagnosticoDefinitivo", query = "SELECT p FROM Prontuario p WHERE p.diagnosticoDefinitivo = :diagnosticoDefinitivo")
    , @NamedQuery(name = "Prontuario.findByTratamento", query = "SELECT p FROM Prontuario p WHERE p.tratamento = :tratamento")
    , @NamedQuery(name = "Prontuario.findByEvolucao", query = "SELECT p FROM Prontuario p WHERE p.evolucao = :evolucao")})
public class Prontuario implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(nullable = false)
    private Integer idProntuario;
    @Size(max = 1000)
    @Column(length = 1000)
    private String queixaPrincipal;
    @Size(max = 1000)
    @Column(length = 1000)
    private String anamnese;
    @Size(max = 1000)
    @Column(length = 1000)
    private String examesFisicos;
    @Size(max = 1000)
    @Column(length = 1000)
    private String examesComplementares;
    @Size(max = 1000)
    @Column(length = 1000)
    private String hipotesesDiagnosticas;
    @Size(max = 1000)
    @Column(length = 1000)
    private String diagnosticoDefinitivo;
    @Size(max = 1000)
    @Column(length = 1000)
    private String tratamento;
    @Size(max = 1000)
    @Column(length = 1000)
    private String evolucao;
    @JoinColumn(name = "Consulta", referencedColumnName = "idConsulta", nullable = false)
    @ManyToOne(optional = false)
    private Consulta consulta;

    public Prontuario() {
    }

    public Prontuario(Integer idProntuario) {
        this.idProntuario = idProntuario;
    }

    public Prontuario(String queixaPrincipal, String anamnese, String examesFisicos, String examesComplementares, String hipotesesDiagnosticas, String diagnosticoDefinitivo, String tratamento, String evolucao, Consulta consulta) {
        this.queixaPrincipal = queixaPrincipal;
        this.anamnese = anamnese;
        this.examesFisicos = examesFisicos;
        this.examesComplementares = examesComplementares;
        this.hipotesesDiagnosticas = hipotesesDiagnosticas;
        this.diagnosticoDefinitivo = diagnosticoDefinitivo;
        this.tratamento = tratamento;
        this.evolucao = evolucao;
        this.consulta = consulta;
    }

    public Prontuario(Integer idProntuario, String queixaPrincipal, String anamnese, String examesFisicos, String examesComplementares, String hipotesesDiagnosticas, String diagnosticoDefinitivo, String tratamento, String evolucao, Consulta consulta) {
        this.idProntuario = idProntuario;
        this.queixaPrincipal = queixaPrincipal;
        this.anamnese = anamnese;
        this.examesFisicos = examesFisicos;
        this.examesComplementares = examesComplementares;
        this.hipotesesDiagnosticas = hipotesesDiagnosticas;
        this.diagnosticoDefinitivo = diagnosticoDefinitivo;
        this.tratamento = tratamento;
        this.evolucao = evolucao;
        this.consulta = consulta;
    }

    public Integer getIdProntuario() {
        return idProntuario;
    }

    public void setIdProntuario(Integer idProntuario) {
        this.idProntuario = idProntuario;
    }

    public String getQueixaPrincipal() {
        return queixaPrincipal;
    }

    public void setQueixaPrincipal(String queixaPrincipal) {
        this.queixaPrincipal = queixaPrincipal;
    }

    public String getAnamnese() {
        return anamnese;
    }

    public void setAnamnese(String anamnese) {
        this.anamnese = anamnese;
    }

    public String getExamesFisicos() {
        return examesFisicos;
    }

    public void setExamesFisicos(String examesFisicos) {
        this.examesFisicos = examesFisicos;
    }

    public String getExamesComplementares() {
        return examesComplementares;
    }

    public void setExamesComplementares(String examesComplementares) {
        this.examesComplementares = examesComplementares;
    }

    public String getHipotesesDiagnosticas() {
        return hipotesesDiagnosticas;
    }

    public void setHipotesesDiagnosticas(String hipotesesDiagnosticas) {
        this.hipotesesDiagnosticas = hipotesesDiagnosticas;
    }

    public String getDiagnosticoDefinitivo() {
        return diagnosticoDefinitivo;
    }

    public void setDiagnosticoDefinitivo(String diagnosticoDefinitivo) {
        this.diagnosticoDefinitivo = diagnosticoDefinitivo;
    }

    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
    }

    public String getEvolucao() {
        return evolucao;
    }

    public void setEvolucao(String evolucao) {
        this.evolucao = evolucao;
    }

    public Consulta getConsulta() {
        return consulta;
    }

    public void setConsulta(Consulta consulta) {
        this.consulta = consulta;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (idProntuario != null ? idProntuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prontuario)) {
            return false;
        }
        Prontuario other = (Prontuario) object;
        if ((this.idProntuario == null && other.idProntuario != null) || (this.idProntuario != null && !this.idProntuario.equals(other.idProntuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Prontuario[ idProntuario=" + idProntuario + " ]";
    }
    
}
