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
import br.com.asfecer.model.TipoExame;
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
public class TipoExameDAO implements Serializable {

    public TipoExameDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoExame tipoexame) throws RollbackFailureException, RuntimeException {
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
                TipoExame oldTipoExameOfExameCollectionExame = exameCollectionExame.getTipoExame();
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
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoExame tipoexame) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoExame persistentTipoExame = em.find(TipoExame.class, tipoexame.getIdTipoExame());
            Collection<Exame> exameCollectionOld = persistentTipoExame.getExameCollection();
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
                    TipoExame oldTipoExameOfExameCollectionNewExame = exameCollectionNewExame.getTipoExame();
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
                if (findTipoExame(id) == null) {
                    throw new NonexistentEntityException("The tipoexame with id " + id + " no longer exists.");
                }
            }
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoExame tipoexame;
            try {
                tipoexame = em.getReference(TipoExame.class, id);
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
                illegalOrphanMessages.add("This TipoExame (" + tipoexame + ") cannot be destroyed since the Exame " + exameCollectionOrphanCheckExame + " in its exameCollection field has a non-nullable tipoExame field.");
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
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public List<TipoExame> findTipoExameEntities() {
        return findTipoExameEntities(true, -1, -1);
    }

    public List<TipoExame> findTipoExameEntities(int maxResults, int firstResult) {
        return findTipoExameEntities(false, maxResults, firstResult);
    }

    private List<TipoExame> findTipoExameEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoExame.class));
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

    public TipoExame findTipoExame(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoExame.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoExameCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoExame> rt = cq.from(TipoExame.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
