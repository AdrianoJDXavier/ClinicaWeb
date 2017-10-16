/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.PreexistingEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Cidade;
import br.com.asfecer.model.Estados;
import java.util.ArrayList;
import java.util.Collection;
import br.com.asfecer.model.Medico;
import br.com.asfecer.model.Funcionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class EstadosDAO implements Serializable {

    public EstadosDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Estados estados) throws PreexistingEntityException, RollbackFailureException, RuntimeException {
        if (estados.getCidadeCollection() == null) {
            estados.setCidadeCollection(new ArrayList<Cidade>());
        }
        if (estados.getMedicoCollection() == null) {
            estados.setMedicoCollection(new ArrayList<Medico>());
        }
        if (estados.getFuncionarioCollection() == null) {
            estados.setFuncionarioCollection(new ArrayList<Funcionario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Cidade> attachedCidadeCollection = new ArrayList<Cidade>();
            for (Cidade cidadeCollectionCidadeToAttach : estados.getCidadeCollection()) {
                cidadeCollectionCidadeToAttach = em.getReference(cidadeCollectionCidadeToAttach.getClass(), cidadeCollectionCidadeToAttach.getIdCidade());
                attachedCidadeCollection.add(cidadeCollectionCidadeToAttach);
            }
            estados.setCidadeCollection(attachedCidadeCollection);
            Collection<Medico> attachedMedicoCollection = new ArrayList<Medico>();
            for (Medico medicoCollectionMedicoToAttach : estados.getMedicoCollection()) {
                medicoCollectionMedicoToAttach = em.getReference(medicoCollectionMedicoToAttach.getClass(), medicoCollectionMedicoToAttach.getIdMedico());
                attachedMedicoCollection.add(medicoCollectionMedicoToAttach);
            }
            estados.setMedicoCollection(attachedMedicoCollection);
            Collection<Funcionario> attachedFuncionarioCollection = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionFuncionarioToAttach : estados.getFuncionarioCollection()) {
                funcionarioCollectionFuncionarioToAttach = em.getReference(funcionarioCollectionFuncionarioToAttach.getClass(), funcionarioCollectionFuncionarioToAttach.getIdFuncionario());
                attachedFuncionarioCollection.add(funcionarioCollectionFuncionarioToAttach);
            }
            estados.setFuncionarioCollection(attachedFuncionarioCollection);
            em.persist(estados);
            for (Cidade cidadeCollectionCidade : estados.getCidadeCollection()) {
                Estados oldEstadoOfCidadeCollectionCidade = cidadeCollectionCidade.getEstado();
                cidadeCollectionCidade.setEstado(estados);
                cidadeCollectionCidade = em.merge(cidadeCollectionCidade);
                if (oldEstadoOfCidadeCollectionCidade != null) {
                    oldEstadoOfCidadeCollectionCidade.getCidadeCollection().remove(cidadeCollectionCidade);
                    oldEstadoOfCidadeCollectionCidade = em.merge(oldEstadoOfCidadeCollectionCidade);
                }
            }
            for (Medico medicoCollectionMedico : estados.getMedicoCollection()) {
                Estados oldUfCrmOfMedicoCollectionMedico = medicoCollectionMedico.getUfCrm();
                medicoCollectionMedico.setUfCrm(estados);
                medicoCollectionMedico = em.merge(medicoCollectionMedico);
                if (oldUfCrmOfMedicoCollectionMedico != null) {
                    oldUfCrmOfMedicoCollectionMedico.getMedicoCollection().remove(medicoCollectionMedico);
                    oldUfCrmOfMedicoCollectionMedico = em.merge(oldUfCrmOfMedicoCollectionMedico);
                }
            }
            for (Funcionario funcionarioCollectionFuncionario : estados.getFuncionarioCollection()) {
                Estados oldUfEmissorOfFuncionarioCollectionFuncionario = funcionarioCollectionFuncionario.getUfEmissor();
                funcionarioCollectionFuncionario.setUfEmissor(estados);
                funcionarioCollectionFuncionario = em.merge(funcionarioCollectionFuncionario);
                if (oldUfEmissorOfFuncionarioCollectionFuncionario != null) {
                    oldUfEmissorOfFuncionarioCollectionFuncionario.getFuncionarioCollection().remove(funcionarioCollectionFuncionario);
                    oldUfEmissorOfFuncionarioCollectionFuncionario = em.merge(oldUfEmissorOfFuncionarioCollectionFuncionario);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findEstados(estados.getSigla()) != null) {
                throw new PreexistingEntityException("Estados " + estados + " already exists.", ex);
            }
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Estados estados) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estados persistentEstados = em.find(Estados.class, estados.getSigla());
            Collection<Cidade> cidadeCollectionOld = persistentEstados.getCidadeCollection();
            Collection<Cidade> cidadeCollectionNew = estados.getCidadeCollection();
            Collection<Medico> medicoCollectionOld = persistentEstados.getMedicoCollection();
            Collection<Medico> medicoCollectionNew = estados.getMedicoCollection();
            Collection<Funcionario> funcionarioCollectionOld = persistentEstados.getFuncionarioCollection();
            Collection<Funcionario> funcionarioCollectionNew = estados.getFuncionarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Cidade cidadeCollectionOldCidade : cidadeCollectionOld) {
                if (!cidadeCollectionNew.contains(cidadeCollectionOldCidade)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cidade " + cidadeCollectionOldCidade + " since its estado field is not nullable.");
                }
            }
            for (Medico medicoCollectionOldMedico : medicoCollectionOld) {
                if (!medicoCollectionNew.contains(medicoCollectionOldMedico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Medico " + medicoCollectionOldMedico + " since its ufCrm field is not nullable.");
                }
            }
            for (Funcionario funcionarioCollectionOldFuncionario : funcionarioCollectionOld) {
                if (!funcionarioCollectionNew.contains(funcionarioCollectionOldFuncionario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionario " + funcionarioCollectionOldFuncionario + " since its ufEmissor field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Cidade> attachedCidadeCollectionNew = new ArrayList<Cidade>();
            for (Cidade cidadeCollectionNewCidadeToAttach : cidadeCollectionNew) {
                cidadeCollectionNewCidadeToAttach = em.getReference(cidadeCollectionNewCidadeToAttach.getClass(), cidadeCollectionNewCidadeToAttach.getIdCidade());
                attachedCidadeCollectionNew.add(cidadeCollectionNewCidadeToAttach);
            }
            cidadeCollectionNew = attachedCidadeCollectionNew;
            estados.setCidadeCollection(cidadeCollectionNew);
            Collection<Medico> attachedMedicoCollectionNew = new ArrayList<Medico>();
            for (Medico medicoCollectionNewMedicoToAttach : medicoCollectionNew) {
                medicoCollectionNewMedicoToAttach = em.getReference(medicoCollectionNewMedicoToAttach.getClass(), medicoCollectionNewMedicoToAttach.getIdMedico());
                attachedMedicoCollectionNew.add(medicoCollectionNewMedicoToAttach);
            }
            medicoCollectionNew = attachedMedicoCollectionNew;
            estados.setMedicoCollection(medicoCollectionNew);
            Collection<Funcionario> attachedFuncionarioCollectionNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionNewFuncionarioToAttach : funcionarioCollectionNew) {
                funcionarioCollectionNewFuncionarioToAttach = em.getReference(funcionarioCollectionNewFuncionarioToAttach.getClass(), funcionarioCollectionNewFuncionarioToAttach.getIdFuncionario());
                attachedFuncionarioCollectionNew.add(funcionarioCollectionNewFuncionarioToAttach);
            }
            funcionarioCollectionNew = attachedFuncionarioCollectionNew;
            estados.setFuncionarioCollection(funcionarioCollectionNew);
            estados = em.merge(estados);
            for (Cidade cidadeCollectionNewCidade : cidadeCollectionNew) {
                if (!cidadeCollectionOld.contains(cidadeCollectionNewCidade)) {
                    Estados oldEstadoOfCidadeCollectionNewCidade = cidadeCollectionNewCidade.getEstado();
                    cidadeCollectionNewCidade.setEstado(estados);
                    cidadeCollectionNewCidade = em.merge(cidadeCollectionNewCidade);
                    if (oldEstadoOfCidadeCollectionNewCidade != null && !oldEstadoOfCidadeCollectionNewCidade.equals(estados)) {
                        oldEstadoOfCidadeCollectionNewCidade.getCidadeCollection().remove(cidadeCollectionNewCidade);
                        oldEstadoOfCidadeCollectionNewCidade = em.merge(oldEstadoOfCidadeCollectionNewCidade);
                    }
                }
            }
            for (Medico medicoCollectionNewMedico : medicoCollectionNew) {
                if (!medicoCollectionOld.contains(medicoCollectionNewMedico)) {
                    Estados oldUfCrmOfMedicoCollectionNewMedico = medicoCollectionNewMedico.getUfCrm();
                    medicoCollectionNewMedico.setUfCrm(estados);
                    medicoCollectionNewMedico = em.merge(medicoCollectionNewMedico);
                    if (oldUfCrmOfMedicoCollectionNewMedico != null && !oldUfCrmOfMedicoCollectionNewMedico.equals(estados)) {
                        oldUfCrmOfMedicoCollectionNewMedico.getMedicoCollection().remove(medicoCollectionNewMedico);
                        oldUfCrmOfMedicoCollectionNewMedico = em.merge(oldUfCrmOfMedicoCollectionNewMedico);
                    }
                }
            }
            for (Funcionario funcionarioCollectionNewFuncionario : funcionarioCollectionNew) {
                if (!funcionarioCollectionOld.contains(funcionarioCollectionNewFuncionario)) {
                    Estados oldUfEmissorOfFuncionarioCollectionNewFuncionario = funcionarioCollectionNewFuncionario.getUfEmissor();
                    funcionarioCollectionNewFuncionario.setUfEmissor(estados);
                    funcionarioCollectionNewFuncionario = em.merge(funcionarioCollectionNewFuncionario);
                    if (oldUfEmissorOfFuncionarioCollectionNewFuncionario != null && !oldUfEmissorOfFuncionarioCollectionNewFuncionario.equals(estados)) {
                        oldUfEmissorOfFuncionarioCollectionNewFuncionario.getFuncionarioCollection().remove(funcionarioCollectionNewFuncionario);
                        oldUfEmissorOfFuncionarioCollectionNewFuncionario = em.merge(oldUfEmissorOfFuncionarioCollectionNewFuncionario);
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
                String id = estados.getSigla();
                if (findEstados(id) == null) {
                    throw new NonexistentEntityException("The estados with id " + id + " no longer exists.");
                }
            }
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(String id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estados estados;
            try {
                estados = em.getReference(Estados.class, id);
                estados.getSigla();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The estados with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Cidade> cidadeCollectionOrphanCheck = estados.getCidadeCollection();
            for (Cidade cidadeCollectionOrphanCheckCidade : cidadeCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estados (" + estados + ") cannot be destroyed since the Cidade " + cidadeCollectionOrphanCheckCidade + " in its cidadeCollection field has a non-nullable estado field.");
            }
            Collection<Medico> medicoCollectionOrphanCheck = estados.getMedicoCollection();
            for (Medico medicoCollectionOrphanCheckMedico : medicoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estados (" + estados + ") cannot be destroyed since the Medico " + medicoCollectionOrphanCheckMedico + " in its medicoCollection field has a non-nullable ufCrm field.");
            }
            Collection<Funcionario> funcionarioCollectionOrphanCheck = estados.getFuncionarioCollection();
            for (Funcionario funcionarioCollectionOrphanCheckFuncionario : funcionarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Estados (" + estados + ") cannot be destroyed since the Funcionario " + funcionarioCollectionOrphanCheckFuncionario + " in its funcionarioCollection field has a non-nullable ufEmissor field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(estados);
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<Estados> findEstadosEntities() {
        return findEstadosEntities(true, -1, -1);
    }

    public List<Estados> findEstadosEntities(int maxResults, int firstResult) {
        return findEstadosEntities(false, maxResults, firstResult);
    }

    private List<Estados> findEstadosEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Estados.class));
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

    public Estados findEstados(String id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Estados.class, id);
        } finally {
            em.close();
        }
    }

    public int getEstadosCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Estados> rt = cq.from(Estados.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
