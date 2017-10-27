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

/**
 *
 * @author Adriano Xavier
 */
@Entity
@Table(name = "prontuario")
@NamedQueries({
    @NamedQuery(name = "Prontuario.findAll", query = "SELECT p FROM Prontuario p")})
public class Prontuario implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "IDPRONTUARIO")
    private Integer idprontuario;
    @Size(max = 1000)
    @Column(name = "ANAMNESE")
    private String anamnese;
    @Size(max = 1000)
    @Column(name = "DIAGNOSTICODEFINITIVO")
    private String diagnosticodefinitivo;
    @Size(max = 1000)
    @Column(name = "EVOLUCAO")
    private String evolucao;
    @Size(max = 1000)
    @Column(name = "EXAMESCOMPLEMENTARES")
    private String examescomplementares;
    @Size(max = 1000)
    @Column(name = "EXAMESFISICOS")
    private String examesfisicos;
    @Size(max = 1000)
    @Column(name = "HIPOTESESDIAGNOSTICAS")
    private String hipotesesdiagnosticas;
    @Size(max = 1000)
    @Column(name = "QUEIXAPRINCIPAL")
    private String queixaprincipal;
    @Size(max = 1000)
    @Column(name = "TRATAMENTO")
    private String tratamento;
    @JoinColumn(name = "Consulta", referencedColumnName = "IDCONSULTA")
    @ManyToOne(optional = false)
    private Consulta consulta;

    public Prontuario() {
    }

    public Prontuario(Integer idprontuario) {
        this.idprontuario = idprontuario;
    }

    public Integer getIdprontuario() {
        return idprontuario;
    }

    public void setIdprontuario(Integer idprontuario) {
        this.idprontuario = idprontuario;
    }

    public String getAnamnese() {
        return anamnese;
    }

    public void setAnamnese(String anamnese) {
        this.anamnese = anamnese;
    }

    public String getDiagnosticodefinitivo() {
        return diagnosticodefinitivo;
    }

    public void setDiagnosticodefinitivo(String diagnosticodefinitivo) {
        this.diagnosticodefinitivo = diagnosticodefinitivo;
    }

    public String getEvolucao() {
        return evolucao;
    }

    public void setEvolucao(String evolucao) {
        this.evolucao = evolucao;
    }

    public String getExamescomplementares() {
        return examescomplementares;
    }

    public void setExamescomplementares(String examescomplementares) {
        this.examescomplementares = examescomplementares;
    }

    public String getExamesfisicos() {
        return examesfisicos;
    }

    public void setExamesfisicos(String examesfisicos) {
        this.examesfisicos = examesfisicos;
    }

    public String getHipotesesdiagnosticas() {
        return hipotesesdiagnosticas;
    }

    public void setHipotesesdiagnosticas(String hipotesesdiagnosticas) {
        this.hipotesesdiagnosticas = hipotesesdiagnosticas;
    }

    public String getQueixaprincipal() {
        return queixaprincipal;
    }

    public void setQueixaprincipal(String queixaprincipal) {
        this.queixaprincipal = queixaprincipal;
    }

    public String getTratamento() {
        return tratamento;
    }

    public void setTratamento(String tratamento) {
        this.tratamento = tratamento;
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
        hash += (idprontuario != null ? idprontuario.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Prontuario)) {
            return false;
        }
        Prontuario other = (Prontuario) object;
        if ((this.idprontuario == null && other.idprontuario != null) || (this.idprontuario != null && !this.idprontuario.equals(other.idprontuario))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "br.com.asfecer.model.Prontuario[ idprontuario=" + idprontuario + " ]";
    }
    
}
