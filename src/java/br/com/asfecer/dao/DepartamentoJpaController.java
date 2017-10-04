/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Cargo;
import br.com.asfecer.model.Departamento;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class DepartamentoJpaController implements Serializable {

    public DepartamentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Departamento departamento) throws RollbackFailureException, Exception {
        if (departamento.getCargoCollection() == null) {
            departamento.setCargoCollection(new ArrayList<Cargo>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Cargo> attachedCargoCollection = new ArrayList<Cargo>();
            for (Cargo cargoCollectionCargoToAttach : departamento.getCargoCollection()) {
                cargoCollectionCargoToAttach = em.getReference(cargoCollectionCargoToAttach.getClass(), cargoCollectionCargoToAttach.getIdCargo());
                attachedCargoCollection.add(cargoCollectionCargoToAttach);
            }
            departamento.setCargoCollection(attachedCargoCollection);
            em.persist(departamento);
            for (Cargo cargoCollectionCargo : departamento.getCargoCollection()) {
                Departamento oldDeptoOfCargoCollectionCargo = cargoCollectionCargo.getDepto();
                cargoCollectionCargo.setDepto(departamento);
                cargoCollectionCargo = em.merge(cargoCollectionCargo);
                if (oldDeptoOfCargoCollectionCargo != null) {
                    oldDeptoOfCargoCollectionCargo.getCargoCollection().remove(cargoCollectionCargo);
                    oldDeptoOfCargoCollectionCargo = em.merge(oldDeptoOfCargoCollectionCargo);
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

    public void edit(Departamento departamento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Departamento persistentDepartamento = em.find(Departamento.class, departamento.getIdDepto());
            Collection<Cargo> cargoCollectionOld = persistentDepartamento.getCargoCollection();
            Collection<Cargo> cargoCollectionNew = departamento.getCargoCollection();
            List<String> illegalOrphanMessages = null;
            for (Cargo cargoCollectionOldCargo : cargoCollectionOld) {
                if (!cargoCollectionNew.contains(cargoCollectionOldCargo)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Cargo " + cargoCollectionOldCargo + " since its depto field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Cargo> attachedCargoCollectionNew = new ArrayList<Cargo>();
            for (Cargo cargoCollectionNewCargoToAttach : cargoCollectionNew) {
                cargoCollectionNewCargoToAttach = em.getReference(cargoCollectionNewCargoToAttach.getClass(), cargoCollectionNewCargoToAttach.getIdCargo());
                attachedCargoCollectionNew.add(cargoCollectionNewCargoToAttach);
            }
            cargoCollectionNew = attachedCargoCollectionNew;
            departamento.setCargoCollection(cargoCollectionNew);
            departamento = em.merge(departamento);
            for (Cargo cargoCollectionNewCargo : cargoCollectionNew) {
                if (!cargoCollectionOld.contains(cargoCollectionNewCargo)) {
                    Departamento oldDeptoOfCargoCollectionNewCargo = cargoCollectionNewCargo.getDepto();
                    cargoCollectionNewCargo.setDepto(departamento);
                    cargoCollectionNewCargo = em.merge(cargoCollectionNewCargo);
                    if (oldDeptoOfCargoCollectionNewCargo != null && !oldDeptoOfCargoCollectionNewCargo.equals(departamento)) {
                        oldDeptoOfCargoCollectionNewCargo.getCargoCollection().remove(cargoCollectionNewCargo);
                        oldDeptoOfCargoCollectionNewCargo = em.merge(oldDeptoOfCargoCollectionNewCargo);
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
                Integer id = departamento.getIdDepto();
                if (findDepartamento(id) == null) {
                    throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.");
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
            Departamento departamento;
            try {
                departamento = em.getReference(Departamento.class, id);
                departamento.getIdDepto();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The departamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Cargo> cargoCollectionOrphanCheck = departamento.getCargoCollection();
            for (Cargo cargoCollectionOrphanCheckCargo : cargoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Departamento (" + departamento + ") cannot be destroyed since the Cargo " + cargoCollectionOrphanCheckCargo + " in its cargoCollection field has a non-nullable depto field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(departamento);
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

    public List<Departamento> findDepartamentoEntities() {
        return findDepartamentoEntities(true, -1, -1);
    }

    public List<Departamento> findDepartamentoEntities(int maxResults, int firstResult) {
        return findDepartamentoEntities(false, maxResults, firstResult);
    }

    private List<Departamento> findDepartamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Departamento.class));
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

    public Departamento findDepartamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Departamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getDepartamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Departamento> rt = cq.from(Departamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
