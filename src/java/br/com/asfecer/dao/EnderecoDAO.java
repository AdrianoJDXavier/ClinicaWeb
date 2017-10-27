/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Cidade;
import br.com.asfecer.model.Endereco;
import br.com.asfecer.model.Paciente;
import java.util.ArrayList;
import java.util.Collection;
import br.com.asfecer.model.Funcionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Adriano Xavier
 */
public class EnderecoDAO implements Serializable {

    public EnderecoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Endereco endereco) throws RollbackFailureException, Exception {
        if (endereco.getPacienteCollection() == null) {
            endereco.setPacienteCollection(new ArrayList<Paciente>());
        }
        if (endereco.getFuncionarioCollection() == null) {
            endereco.setFuncionarioCollection(new ArrayList<Funcionario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cidade cidade = endereco.getCidade();
            if (cidade != null) {
                cidade = em.getReference(cidade.getClass(), cidade.getIdcidade());
                endereco.setCidade(cidade);
            }
            Collection<Paciente> attachedPacienteCollection = new ArrayList<Paciente>();
            for (Paciente pacienteCollectionPacienteToAttach : endereco.getPacienteCollection()) {
                pacienteCollectionPacienteToAttach = em.getReference(pacienteCollectionPacienteToAttach.getClass(), pacienteCollectionPacienteToAttach.getIdpaciente());
                attachedPacienteCollection.add(pacienteCollectionPacienteToAttach);
            }
            endereco.setPacienteCollection(attachedPacienteCollection);
            Collection<Funcionario> attachedFuncionarioCollection = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionFuncionarioToAttach : endereco.getFuncionarioCollection()) {
                funcionarioCollectionFuncionarioToAttach = em.getReference(funcionarioCollectionFuncionarioToAttach.getClass(), funcionarioCollectionFuncionarioToAttach.getIdfuncionario());
                attachedFuncionarioCollection.add(funcionarioCollectionFuncionarioToAttach);
            }
            endereco.setFuncionarioCollection(attachedFuncionarioCollection);
            em.persist(endereco);
            if (cidade != null) {
                cidade.getEnderecoCollection().add(endereco);
                cidade = em.merge(cidade);
            }
            for (Paciente pacienteCollectionPaciente : endereco.getPacienteCollection()) {
                Endereco oldEnderecoOfPacienteCollectionPaciente = pacienteCollectionPaciente.getEndereco();
                pacienteCollectionPaciente.setEndereco(endereco);
                pacienteCollectionPaciente = em.merge(pacienteCollectionPaciente);
                if (oldEnderecoOfPacienteCollectionPaciente != null) {
                    oldEnderecoOfPacienteCollectionPaciente.getPacienteCollection().remove(pacienteCollectionPaciente);
                    oldEnderecoOfPacienteCollectionPaciente = em.merge(oldEnderecoOfPacienteCollectionPaciente);
                }
            }
            for (Funcionario funcionarioCollectionFuncionario : endereco.getFuncionarioCollection()) {
                Endereco oldEnderecoOfFuncionarioCollectionFuncionario = funcionarioCollectionFuncionario.getEndereco();
                funcionarioCollectionFuncionario.setEndereco(endereco);
                funcionarioCollectionFuncionario = em.merge(funcionarioCollectionFuncionario);
                if (oldEnderecoOfFuncionarioCollectionFuncionario != null) {
                    oldEnderecoOfFuncionarioCollectionFuncionario.getFuncionarioCollection().remove(funcionarioCollectionFuncionario);
                    oldEnderecoOfFuncionarioCollectionFuncionario = em.merge(oldEnderecoOfFuncionarioCollectionFuncionario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Endereco endereco) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Endereco persistentEndereco = em.find(Endereco.class, endereco.getIdendereco());
            Cidade cidadeOld = persistentEndereco.getCidade();
            Cidade cidadeNew = endereco.getCidade();
            Collection<Paciente> pacienteCollectionOld = persistentEndereco.getPacienteCollection();
            Collection<Paciente> pacienteCollectionNew = endereco.getPacienteCollection();
            Collection<Funcionario> funcionarioCollectionOld = persistentEndereco.getFuncionarioCollection();
            Collection<Funcionario> funcionarioCollectionNew = endereco.getFuncionarioCollection();
            if (cidadeNew != null) {
                cidadeNew = em.getReference(cidadeNew.getClass(), cidadeNew.getIdcidade());
                endereco.setCidade(cidadeNew);
            }
            Collection<Paciente> attachedPacienteCollectionNew = new ArrayList<Paciente>();
            for (Paciente pacienteCollectionNewPacienteToAttach : pacienteCollectionNew) {
                pacienteCollectionNewPacienteToAttach = em.getReference(pacienteCollectionNewPacienteToAttach.getClass(), pacienteCollectionNewPacienteToAttach.getIdpaciente());
                attachedPacienteCollectionNew.add(pacienteCollectionNewPacienteToAttach);
            }
            pacienteCollectionNew = attachedPacienteCollectionNew;
            endereco.setPacienteCollection(pacienteCollectionNew);
            Collection<Funcionario> attachedFuncionarioCollectionNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionNewFuncionarioToAttach : funcionarioCollectionNew) {
                funcionarioCollectionNewFuncionarioToAttach = em.getReference(funcionarioCollectionNewFuncionarioToAttach.getClass(), funcionarioCollectionNewFuncionarioToAttach.getIdfuncionario());
                attachedFuncionarioCollectionNew.add(funcionarioCollectionNewFuncionarioToAttach);
            }
            funcionarioCollectionNew = attachedFuncionarioCollectionNew;
            endereco.setFuncionarioCollection(funcionarioCollectionNew);
            endereco = em.merge(endereco);
            if (cidadeOld != null && !cidadeOld.equals(cidadeNew)) {
                cidadeOld.getEnderecoCollection().remove(endereco);
                cidadeOld = em.merge(cidadeOld);
            }
            if (cidadeNew != null && !cidadeNew.equals(cidadeOld)) {
                cidadeNew.getEnderecoCollection().add(endereco);
                cidadeNew = em.merge(cidadeNew);
            }
            for (Paciente pacienteCollectionOldPaciente : pacienteCollectionOld) {
                if (!pacienteCollectionNew.contains(pacienteCollectionOldPaciente)) {
                    pacienteCollectionOldPaciente.setEndereco(null);
                    pacienteCollectionOldPaciente = em.merge(pacienteCollectionOldPaciente);
                }
            }
            for (Paciente pacienteCollectionNewPaciente : pacienteCollectionNew) {
                if (!pacienteCollectionOld.contains(pacienteCollectionNewPaciente)) {
                    Endereco oldEnderecoOfPacienteCollectionNewPaciente = pacienteCollectionNewPaciente.getEndereco();
                    pacienteCollectionNewPaciente.setEndereco(endereco);
                    pacienteCollectionNewPaciente = em.merge(pacienteCollectionNewPaciente);
                    if (oldEnderecoOfPacienteCollectionNewPaciente != null && !oldEnderecoOfPacienteCollectionNewPaciente.equals(endereco)) {
                        oldEnderecoOfPacienteCollectionNewPaciente.getPacienteCollection().remove(pacienteCollectionNewPaciente);
                        oldEnderecoOfPacienteCollectionNewPaciente = em.merge(oldEnderecoOfPacienteCollectionNewPaciente);
                    }
                }
            }
            for (Funcionario funcionarioCollectionOldFuncionario : funcionarioCollectionOld) {
                if (!funcionarioCollectionNew.contains(funcionarioCollectionOldFuncionario)) {
                    funcionarioCollectionOldFuncionario.setEndereco(null);
                    funcionarioCollectionOldFuncionario = em.merge(funcionarioCollectionOldFuncionario);
                }
            }
            for (Funcionario funcionarioCollectionNewFuncionario : funcionarioCollectionNew) {
                if (!funcionarioCollectionOld.contains(funcionarioCollectionNewFuncionario)) {
                    Endereco oldEnderecoOfFuncionarioCollectionNewFuncionario = funcionarioCollectionNewFuncionario.getEndereco();
                    funcionarioCollectionNewFuncionario.setEndereco(endereco);
                    funcionarioCollectionNewFuncionario = em.merge(funcionarioCollectionNewFuncionario);
                    if (oldEnderecoOfFuncionarioCollectionNewFuncionario != null && !oldEnderecoOfFuncionarioCollectionNewFuncionario.equals(endereco)) {
                        oldEnderecoOfFuncionarioCollectionNewFuncionario.getFuncionarioCollection().remove(funcionarioCollectionNewFuncionario);
                        oldEnderecoOfFuncionarioCollectionNewFuncionario = em.merge(oldEnderecoOfFuncionarioCollectionNewFuncionario);
                    }
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            String msg = ex.getLocalizedMessage();
            if (msg == null || msg.length() == 0) {
                Integer id = endereco.getIdendereco();
                if (findEndereco(id) == null) {
                    throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Endereco endereco;
            try {
                endereco = em.getReference(Endereco.class, id);
                endereco.getIdendereco();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The endereco with id " + id + " no longer exists.", enfe);
            }
            Cidade cidade = endereco.getCidade();
            if (cidade != null) {
                cidade.getEnderecoCollection().remove(endereco);
                cidade = em.merge(cidade);
            }
            Collection<Paciente> pacienteCollection = endereco.getPacienteCollection();
            for (Paciente pacienteCollectionPaciente : pacienteCollection) {
                pacienteCollectionPaciente.setEndereco(null);
                pacienteCollectionPaciente = em.merge(pacienteCollectionPaciente);
            }
            Collection<Funcionario> funcionarioCollection = endereco.getFuncionarioCollection();
            for (Funcionario funcionarioCollectionFuncionario : funcionarioCollection) {
                funcionarioCollectionFuncionario.setEndereco(null);
                funcionarioCollectionFuncionario = em.merge(funcionarioCollectionFuncionario);
            }
            em.remove(endereco);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Endereco> findEnderecoEntities() {
        return findEnderecoEntities(true, -1, -1);
    }

    public List<Endereco> findEnderecoEntities(int maxResults, int firstResult) {
        return findEnderecoEntities(false, maxResults, firstResult);
    }

    private List<Endereco> findEnderecoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Endereco.class));
            Query q = em.createQuery(cq);
            if (!all) {
                q.setMaxResults(maxResults);
                q.setFirstResult(firstResult);
            }
            return q.getResultList();
        } finally {
            em.close();
        }
    }

    public Endereco findEndereco(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Endereco.class, id);
        } finally {
            em.close();
        }
    }

    public int getEnderecoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Endereco> rt = cq.from(Endereco.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
