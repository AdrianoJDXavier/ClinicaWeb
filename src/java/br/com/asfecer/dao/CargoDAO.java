/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Cargo;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Departamento;
import br.com.asfecer.model.Funcionario;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Adriano Xavier
 */
public class CargoDAO implements Serializable {

    public CargoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cargo cargo) throws RollbackFailureException, Exception {
        if (cargo.getFuncionarioCollection() == null) {
            cargo.setFuncionarioCollection(new ArrayList<Funcionario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departamento depto = cargo.getDepto();
            if (depto != null) {
                depto = em.getReference(depto.getClass(), depto.getIddepto());
                cargo.setDepto(depto);
            }
            Collection<Funcionario> attachedFuncionarioCollection = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionFuncionarioToAttach : cargo.getFuncionarioCollection()) {
                funcionarioCollectionFuncionarioToAttach = em.getReference(funcionarioCollectionFuncionarioToAttach.getClass(), funcionarioCollectionFuncionarioToAttach.getIdfuncionario());
                attachedFuncionarioCollection.add(funcionarioCollectionFuncionarioToAttach);
            }
            cargo.setFuncionarioCollection(attachedFuncionarioCollection);
            em.persist(cargo);
            if (depto != null) {
                depto.getCargoCollection().add(cargo);
                depto = em.merge(depto);
            }
            for (Funcionario funcionarioCollectionFuncionario : cargo.getFuncionarioCollection()) {
                Cargo oldCargoOfFuncionarioCollectionFuncionario = funcionarioCollectionFuncionario.getCargo();
                funcionarioCollectionFuncionario.setCargo(cargo);
                funcionarioCollectionFuncionario = em.merge(funcionarioCollectionFuncionario);
                if (oldCargoOfFuncionarioCollectionFuncionario != null) {
                    oldCargoOfFuncionarioCollectionFuncionario.getFuncionarioCollection().remove(funcionarioCollectionFuncionario);
                    oldCargoOfFuncionarioCollectionFuncionario = em.merge(oldCargoOfFuncionarioCollectionFuncionario);
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

    public void edit(Cargo cargo) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cargo persistentCargo = em.find(Cargo.class, cargo.getIdcargo());
            Departamento deptoOld = persistentCargo.getDepto();
            Departamento deptoNew = cargo.getDepto();
            Collection<Funcionario> funcionarioCollectionOld = persistentCargo.getFuncionarioCollection();
            Collection<Funcionario> funcionarioCollectionNew = cargo.getFuncionarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Funcionario funcionarioCollectionOldFuncionario : funcionarioCollectionOld) {
                if (!funcionarioCollectionNew.contains(funcionarioCollectionOldFuncionario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Funcionario " + funcionarioCollectionOldFuncionario + " since its cargo field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (deptoNew != null) {
                deptoNew = em.getReference(deptoNew.getClass(), deptoNew.getIddepto());
                cargo.setDepto(deptoNew);
            }
            Collection<Funcionario> attachedFuncionarioCollectionNew = new ArrayList<Funcionario>();
            for (Funcionario funcionarioCollectionNewFuncionarioToAttach : funcionarioCollectionNew) {
                funcionarioCollectionNewFuncionarioToAttach = em.getReference(funcionarioCollectionNewFuncionarioToAttach.getClass(), funcionarioCollectionNewFuncionarioToAttach.getIdfuncionario());
                attachedFuncionarioCollectionNew.add(funcionarioCollectionNewFuncionarioToAttach);
            }
            funcionarioCollectionNew = attachedFuncionarioCollectionNew;
            cargo.setFuncionarioCollection(funcionarioCollectionNew);
            cargo = em.merge(cargo);
            if (deptoOld != null && !deptoOld.equals(deptoNew)) {
                deptoOld.getCargoCollection().remove(cargo);
                deptoOld = em.merge(deptoOld);
            }
            if (deptoNew != null && !deptoNew.equals(deptoOld)) {
                deptoNew.getCargoCollection().add(cargo);
                deptoNew = em.merge(deptoNew);
            }
            for (Funcionario funcionarioCollectionNewFuncionario : funcionarioCollectionNew) {
                if (!funcionarioCollectionOld.contains(funcionarioCollectionNewFuncionario)) {
                    Cargo oldCargoOfFuncionarioCollectionNewFuncionario = funcionarioCollectionNewFuncionario.getCargo();
                    funcionarioCollectionNewFuncionario.setCargo(cargo);
                    funcionarioCollectionNewFuncionario = em.merge(funcionarioCollectionNewFuncionario);
                    if (oldCargoOfFuncionarioCollectionNewFuncionario != null && !oldCargoOfFuncionarioCollectionNewFuncionario.equals(cargo)) {
                        oldCargoOfFuncionarioCollectionNewFuncionario.getFuncionarioCollection().remove(funcionarioCollectionNewFuncionario);
                        oldCargoOfFuncionarioCollectionNewFuncionario = em.merge(oldCargoOfFuncionarioCollectionNewFuncionario);
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
                Integer id = cargo.getIdcargo();
                if (findCargo(id) == null) {
                    throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cargo cargo;
            try {
                cargo = em.getReference(Cargo.class, id);
                cargo.getIdcargo();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cargo with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Funcionario> funcionarioCollectionOrphanCheck = cargo.getFuncionarioCollection();
            for (Funcionario funcionarioCollectionOrphanCheckFuncionario : funcionarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cargo (" + cargo + ") cannot be destroyed since the Funcionario " + funcionarioCollectionOrphanCheckFuncionario + " in its funcionarioCollection field has a non-nullable cargo field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Departamento depto = cargo.getDepto();
            if (depto != null) {
                depto.getCargoCollection().remove(cargo);
                depto = em.merge(depto);
            }
            em.remove(cargo);
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

    public List<Cargo> findCargoEntities() {
        return findCargoEntities(true, -1, -1);
    }

    public List<Cargo> findCargoEntities(int maxResults, int firstResult) {
        return findCargoEntities(false, maxResults, firstResult);
    }

    private List<Cargo> findCargoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cargo.class));
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

    public Cargo findCargo(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cargo.class, id);
        } finally {
            em.close();
        }
    }

    public int getCargoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cargo> rt = cq.from(Cargo.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
