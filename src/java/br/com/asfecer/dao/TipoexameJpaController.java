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
import br.com.asfecer.model.Exame;
import br.com.asfecer.model.Tipoexame;
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
public class TipoexameJpaController implements Serializable {

    public TipoexameJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoexame tipoexame) throws RollbackFailureException, Exception {
        if (tipoexame.getExameCollection() == null) {
            tipoexame.setExameCollection(new ArrayList<Exame>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Exame> attachedExameCollection = new ArrayList<Exame>();
            for (Exame exameCollectionExameToAttach : tipoexame.getExameCollection()) {
                exameCollectionExameToAttach = em.getReference(exameCollectionExameToAttach.getClass(), exameCollectionExameToAttach.getIdExame());
                attachedExameCollection.add(exameCollectionExameToAttach);
            }
            tipoexame.setExameCollection(attachedExameCollection);
            em.persist(tipoexame);
            for (Exame exameCollectionExame : tipoexame.getExameCollection()) {
                Tipoexame oldTipoExameOfExameCollectionExame = exameCollectionExame.getTipoExame();
                exameCollectionExame.setTipoExame(tipoexame);
                exameCollectionExame = em.merge(exameCollectionExame);
                if (oldTipoExameOfExameCollectionExame != null) {
                    oldTipoExameOfExameCollectionExame.getExameCollection().remove(exameCollectionExame);
                    oldTipoExameOfExameCollectionExame = em.merge(oldTipoExameOfExameCollectionExame);
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

    public void edit(Tipoexame tipoexame) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipoexame persistentTipoexame = em.find(Tipoexame.class, tipoexame.getIdTipoExame());
            Collection<Exame> exameCollectionOld = persistentTipoexame.getExameCollection();
            Collection<Exame> exameCollectionNew = tipoexame.getExameCollection();
            List<String> illegalOrphanMessages = null;
            for (Exame exameCollectionOldExame : exameCollectionOld) {
                if (!exameCollectionNew.contains(exameCollectionOldExame)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Exame " + exameCollectionOldExame + " since its tipoExame field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Exame> attachedExameCollectionNew = new ArrayList<Exame>();
            for (Exame exameCollectionNewExameToAttach : exameCollectionNew) {
                exameCollectionNewExameToAttach = em.getReference(exameCollectionNewExameToAttach.getClass(), exameCollectionNewExameToAttach.getIdExame());
                attachedExameCollectionNew.add(exameCollectionNewExameToAttach);
            }
            exameCollectionNew = attachedExameCollectionNew;
            tipoexame.setExameCollection(exameCollectionNew);
            tipoexame = em.merge(tipoexame);
            for (Exame exameCollectionNewExame : exameCollectionNew) {
                if (!exameCollectionOld.contains(exameCollectionNewExame)) {
                    Tipoexame oldTipoExameOfExameCollectionNewExame = exameCollectionNewExame.getTipoExame();
                    exameCollectionNewExame.setTipoExame(tipoexame);
                    exameCollectionNewExame = em.merge(exameCollectionNewExame);
                    if (oldTipoExameOfExameCollectionNewExame != null && !oldTipoExameOfExameCollectionNewExame.equals(tipoexame)) {
                        oldTipoExameOfExameCollectionNewExame.getExameCollection().remove(exameCollectionNewExame);
                        oldTipoExameOfExameCollectionNewExame = em.merge(oldTipoExameOfExameCollectionNewExame);
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
                Integer id = tipoexame.getIdTipoExame();
                if (findTipoexame(id) == null) {
                    throw new NonexistentEntityException("The tipoexame with id " + id + " no longer exists.");
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
            Tipoexame tipoexame;
            try {
                tipoexame = em.getReference(Tipoexame.class, id);
                tipoexame.getIdTipoExame();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoexame with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Exame> exameCollectionOrphanCheck = tipoexame.getExameCollection();
            for (Exame exameCollectionOrphanCheckExame : exameCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Tipoexame (" + tipoexame + ") cannot be destroyed since the Exame " + exameCollectionOrphanCheckExame + " in its exameCollection field has a non-nullable tipoExame field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(tipoexame);
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

    public List<Tipoexame> findTipoexameEntities() {
        return findTipoexameEntities(true, -1, -1);
    }

    public List<Tipoexame> findTipoexameEntities(int maxResults, int firstResult) {
        return findTipoexameEntities(false, maxResults, firstResult);
    }

    private List<Tipoexame> findTipoexameEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoexame.class));
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

    public Tipoexame findTipoexame(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoexame.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoexameCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoexame> rt = cq.from(Tipoexame.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
