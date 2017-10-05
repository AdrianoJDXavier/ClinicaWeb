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
import br.com.asfecer.model.Tipoatestado;
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

    public void create(Patologia patologia) throws RollbackFailureException, Exception {
        if (patologia.getTipoatestadoCollection() == null) {
            patologia.setTipoatestadoCollection(new ArrayList<Tipoatestado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Tipoatestado> attachedTipoatestadoCollection = new ArrayList<Tipoatestado>();
            for (Tipoatestado tipoatestadoCollectionTipoatestadoToAttach : patologia.getTipoatestadoCollection()) {
                tipoatestadoCollectionTipoatestadoToAttach = em.getReference(tipoatestadoCollectionTipoatestadoToAttach.getClass(), tipoatestadoCollectionTipoatestadoToAttach.getIdTipoAtestado());
                attachedTipoatestadoCollection.add(tipoatestadoCollectionTipoatestadoToAttach);
            }
            patologia.setTipoatestadoCollection(attachedTipoatestadoCollection);
            em.persist(patologia);
            for (Tipoatestado tipoatestadoCollectionTipoatestado : patologia.getTipoatestadoCollection()) {
                Patologia oldPatologiaOfTipoatestadoCollectionTipoatestado = tipoatestadoCollectionTipoatestado.getPatologia();
                tipoatestadoCollectionTipoatestado.setPatologia(patologia);
                tipoatestadoCollectionTipoatestado = em.merge(tipoatestadoCollectionTipoatestado);
                if (oldPatologiaOfTipoatestadoCollectionTipoatestado != null) {
                    oldPatologiaOfTipoatestadoCollectionTipoatestado.getTipoatestadoCollection().remove(tipoatestadoCollectionTipoatestado);
                    oldPatologiaOfTipoatestadoCollectionTipoatestado = em.merge(oldPatologiaOfTipoatestadoCollectionTipoatestado);
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

    public void edit(Patologia patologia) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Patologia persistentPatologia = em.find(Patologia.class, patologia.getIdPatologia());
            Collection<Tipoatestado> tipoatestadoCollectionOld = persistentPatologia.getTipoatestadoCollection();
            Collection<Tipoatestado> tipoatestadoCollectionNew = patologia.getTipoatestadoCollection();
            Collection<Tipoatestado> attachedTipoatestadoCollectionNew = new ArrayList<Tipoatestado>();
            for (Tipoatestado tipoatestadoCollectionNewTipoatestadoToAttach : tipoatestadoCollectionNew) {
                tipoatestadoCollectionNewTipoatestadoToAttach = em.getReference(tipoatestadoCollectionNewTipoatestadoToAttach.getClass(), tipoatestadoCollectionNewTipoatestadoToAttach.getIdTipoAtestado());
                attachedTipoatestadoCollectionNew.add(tipoatestadoCollectionNewTipoatestadoToAttach);
            }
            tipoatestadoCollectionNew = attachedTipoatestadoCollectionNew;
            patologia.setTipoatestadoCollection(tipoatestadoCollectionNew);
            patologia = em.merge(patologia);
            for (Tipoatestado tipoatestadoCollectionOldTipoatestado : tipoatestadoCollectionOld) {
                if (!tipoatestadoCollectionNew.contains(tipoatestadoCollectionOldTipoatestado)) {
                    tipoatestadoCollectionOldTipoatestado.setPatologia(null);
                    tipoatestadoCollectionOldTipoatestado = em.merge(tipoatestadoCollectionOldTipoatestado);
                }
            }
            for (Tipoatestado tipoatestadoCollectionNewTipoatestado : tipoatestadoCollectionNew) {
                if (!tipoatestadoCollectionOld.contains(tipoatestadoCollectionNewTipoatestado)) {
                    Patologia oldPatologiaOfTipoatestadoCollectionNewTipoatestado = tipoatestadoCollectionNewTipoatestado.getPatologia();
                    tipoatestadoCollectionNewTipoatestado.setPatologia(patologia);
                    tipoatestadoCollectionNewTipoatestado = em.merge(tipoatestadoCollectionNewTipoatestado);
                    if (oldPatologiaOfTipoatestadoCollectionNewTipoatestado != null && !oldPatologiaOfTipoatestadoCollectionNewTipoatestado.equals(patologia)) {
                        oldPatologiaOfTipoatestadoCollectionNewTipoatestado.getTipoatestadoCollection().remove(tipoatestadoCollectionNewTipoatestado);
                        oldPatologiaOfTipoatestadoCollectionNewTipoatestado = em.merge(oldPatologiaOfTipoatestadoCollectionNewTipoatestado);
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
            Patologia patologia;
            try {
                patologia = em.getReference(Patologia.class, id);
                patologia.getIdPatologia();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The patologia with id " + id + " no longer exists.", enfe);
            }
            Collection<Tipoatestado> tipoatestadoCollection = patologia.getTipoatestadoCollection();
            for (Tipoatestado tipoatestadoCollectionTipoatestado : tipoatestadoCollection) {
                tipoatestadoCollectionTipoatestado.setPatologia(null);
                tipoatestadoCollectionTipoatestado = em.merge(tipoatestadoCollectionTipoatestado);
            }
            em.remove(patologia);
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
