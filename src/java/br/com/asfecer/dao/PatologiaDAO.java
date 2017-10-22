/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Patologia;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.TipoAtestado;
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
public class PatologiaDAO implements Serializable {

    public PatologiaDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Patologia patologia) throws RollbackFailureException, RuntimeException {
        if (patologia.getTipoAtestadoCollection() == null) {
            patologia.setTipoAtestadoCollection(new ArrayList<TipoAtestado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<TipoAtestado> attachedTipoAtestadoCollection = new ArrayList<TipoAtestado>();
            for (TipoAtestado tipoatestadoCollectionTipoAtestadoToAttach : patologia.getTipoAtestadoCollection()) {
                tipoatestadoCollectionTipoAtestadoToAttach = em.getReference(tipoatestadoCollectionTipoAtestadoToAttach.getClass(), tipoatestadoCollectionTipoAtestadoToAttach.getIdTipoAtestado());
                attachedTipoAtestadoCollection.add(tipoatestadoCollectionTipoAtestadoToAttach);
            }
            patologia.setTipoAtestadoCollection(attachedTipoAtestadoCollection);
            em.persist(patologia);
            for (TipoAtestado tipoatestadoCollectionTipoAtestado : patologia.getTipoAtestadoCollection()) {
                Patologia oldPatologiaOfTipoAtestadoCollectionTipoAtestado = tipoatestadoCollectionTipoAtestado.getPatologia();
                tipoatestadoCollectionTipoAtestado.setPatologia(patologia);
                tipoatestadoCollectionTipoAtestado = em.merge(tipoatestadoCollectionTipoAtestado);
                if (oldPatologiaOfTipoAtestadoCollectionTipoAtestado != null) {
                    oldPatologiaOfTipoAtestadoCollectionTipoAtestado.getTipoAtestadoCollection().remove(tipoatestadoCollectionTipoAtestado);
                    oldPatologiaOfTipoAtestadoCollectionTipoAtestado = em.merge(oldPatologiaOfTipoAtestadoCollectionTipoAtestado);
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

    public void edit(Patologia patologia) throws NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Patologia persistentPatologia = em.find(Patologia.class, patologia.getIdPatologia());
            Collection<TipoAtestado> tipoatestadoCollectionOld = persistentPatologia.getTipoAtestadoCollection();
            Collection<TipoAtestado> tipoatestadoCollectionNew = patologia.getTipoAtestadoCollection();
            Collection<TipoAtestado> attachedTipoAtestadoCollectionNew = new ArrayList<TipoAtestado>();
            for (TipoAtestado tipoatestadoCollectionNewTipoAtestadoToAttach : tipoatestadoCollectionNew) {
                tipoatestadoCollectionNewTipoAtestadoToAttach = em.getReference(tipoatestadoCollectionNewTipoAtestadoToAttach.getClass(), tipoatestadoCollectionNewTipoAtestadoToAttach.getIdTipoAtestado());
                attachedTipoAtestadoCollectionNew.add(tipoatestadoCollectionNewTipoAtestadoToAttach);
            }
            tipoatestadoCollectionNew = attachedTipoAtestadoCollectionNew;
            patologia.setTipoAtestadoCollection(tipoatestadoCollectionNew);
            patologia = em.merge(patologia);
            for (TipoAtestado tipoatestadoCollectionOldTipoAtestado : tipoatestadoCollectionOld) {
                if (!tipoatestadoCollectionNew.contains(tipoatestadoCollectionOldTipoAtestado)) {
                    tipoatestadoCollectionOldTipoAtestado.setPatologia(null);
                    tipoatestadoCollectionOldTipoAtestado = em.merge(tipoatestadoCollectionOldTipoAtestado);
                }
            }
            for (TipoAtestado tipoatestadoCollectionNewTipoAtestado : tipoatestadoCollectionNew) {
                if (!tipoatestadoCollectionOld.contains(tipoatestadoCollectionNewTipoAtestado)) {
                    Patologia oldPatologiaOfTipoAtestadoCollectionNewTipoAtestado = tipoatestadoCollectionNewTipoAtestado.getPatologia();
                    tipoatestadoCollectionNewTipoAtestado.setPatologia(patologia);
                    tipoatestadoCollectionNewTipoAtestado = em.merge(tipoatestadoCollectionNewTipoAtestado);
                    if (oldPatologiaOfTipoAtestadoCollectionNewTipoAtestado != null && !oldPatologiaOfTipoAtestadoCollectionNewTipoAtestado.equals(patologia)) {
                        oldPatologiaOfTipoAtestadoCollectionNewTipoAtestado.getTipoAtestadoCollection().remove(tipoatestadoCollectionNewTipoAtestado);
                        oldPatologiaOfTipoAtestadoCollectionNewTipoAtestado = em.merge(oldPatologiaOfTipoAtestadoCollectionNewTipoAtestado);
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
                Integer id = patologia.getIdPatologia();
                if (findPatologia(id) == null) {
                    throw new NonexistentEntityException("The patologia with id " + id + " no longer exists.");
                }
            }
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Patologia patologia;
            try {
                patologia = em.getReference(Patologia.class, id);
                patologia.getIdPatologia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The patologia with id " + id + " no longer exists.", enfe);
            }
            Collection<TipoAtestado> tipoatestadoCollection = patologia.getTipoAtestadoCollection();
            for (TipoAtestado tipoatestadoCollectionTipoAtestado : tipoatestadoCollection) {
                tipoatestadoCollectionTipoAtestado.setPatologia(null);
                tipoatestadoCollectionTipoAtestado = em.merge(tipoatestadoCollectionTipoAtestado);
            }
            em.remove(patologia);
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

    public List<Patologia> findPatologiaEntities() {
        return findPatologiaEntities(true, -1, -1);
    }

    public List<Patologia> findPatologiaEntities(int maxResults, int firstResult) {
        return findPatologiaEntities(false, maxResults, firstResult);
    }

    private List<Patologia> findPatologiaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Patologia.class));
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

    public Patologia findPatologia(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Patologia.class, id);
        } finally {
            em.close();
        }
    }

    public int getPatologiaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Patologia> rt = cq.from(Patologia.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
