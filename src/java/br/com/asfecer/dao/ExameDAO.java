/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Exame;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Tipoexame;
import br.com.asfecer.model.Pedidoexame;
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
public class ExameDAO implements Serializable {

    public ExameDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Exame exame) throws RollbackFailureException, Exception {
        if (exame.getPedidoexameCollection() == null) {
            exame.setPedidoexameCollection(new ArrayList<Pedidoexame>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipoexame tipoExame = exame.getTipoExame();
            if (tipoExame != null) {
                tipoExame = em.getReference(tipoExame.getClass(), tipoExame.getIdTipoExame());
                exame.setTipoExame(tipoExame);
            }
            Collection<Pedidoexame> attachedPedidoexameCollection = new ArrayList<Pedidoexame>();
            for (Pedidoexame pedidoexameCollectionPedidoexameToAttach : exame.getPedidoexameCollection()) {
                pedidoexameCollectionPedidoexameToAttach = em.getReference(pedidoexameCollectionPedidoexameToAttach.getClass(), pedidoexameCollectionPedidoexameToAttach.getIdPedidoExame());
                attachedPedidoexameCollection.add(pedidoexameCollectionPedidoexameToAttach);
            }
            exame.setPedidoexameCollection(attachedPedidoexameCollection);
            em.persist(exame);
            if (tipoExame != null) {
                tipoExame.getExameCollection().add(exame);
                tipoExame = em.merge(tipoExame);
            }
            for (Pedidoexame pedidoexameCollectionPedidoexame : exame.getPedidoexameCollection()) {
                Exame oldExameOfPedidoexameCollectionPedidoexame = pedidoexameCollectionPedidoexame.getExame();
                pedidoexameCollectionPedidoexame.setExame(exame);
                pedidoexameCollectionPedidoexame = em.merge(pedidoexameCollectionPedidoexame);
                if (oldExameOfPedidoexameCollectionPedidoexame != null) {
                    oldExameOfPedidoexameCollectionPedidoexame.getPedidoexameCollection().remove(pedidoexameCollectionPedidoexame);
                    oldExameOfPedidoexameCollectionPedidoexame = em.merge(oldExameOfPedidoexameCollectionPedidoexame);
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

    public void edit(Exame exame) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Exame persistentExame = em.find(Exame.class, exame.getIdExame());
            Tipoexame tipoExameOld = persistentExame.getTipoExame();
            Tipoexame tipoExameNew = exame.getTipoExame();
            Collection<Pedidoexame> pedidoexameCollectionOld = persistentExame.getPedidoexameCollection();
            Collection<Pedidoexame> pedidoexameCollectionNew = exame.getPedidoexameCollection();
            List<String> illegalOrphanMessages = null;
            for (Pedidoexame pedidoexameCollectionOldPedidoexame : pedidoexameCollectionOld) {
                if (!pedidoexameCollectionNew.contains(pedidoexameCollectionOldPedidoexame)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedidoexame " + pedidoexameCollectionOldPedidoexame + " since its exame field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoExameNew != null) {
                tipoExameNew = em.getReference(tipoExameNew.getClass(), tipoExameNew.getIdTipoExame());
                exame.setTipoExame(tipoExameNew);
            }
            Collection<Pedidoexame> attachedPedidoexameCollectionNew = new ArrayList<Pedidoexame>();
            for (Pedidoexame pedidoexameCollectionNewPedidoexameToAttach : pedidoexameCollectionNew) {
                pedidoexameCollectionNewPedidoexameToAttach = em.getReference(pedidoexameCollectionNewPedidoexameToAttach.getClass(), pedidoexameCollectionNewPedidoexameToAttach.getIdPedidoExame());
                attachedPedidoexameCollectionNew.add(pedidoexameCollectionNewPedidoexameToAttach);
            }
            pedidoexameCollectionNew = attachedPedidoexameCollectionNew;
            exame.setPedidoexameCollection(pedidoexameCollectionNew);
            exame = em.merge(exame);
            if (tipoExameOld != null && !tipoExameOld.equals(tipoExameNew)) {
                tipoExameOld.getExameCollection().remove(exame);
                tipoExameOld = em.merge(tipoExameOld);
            }
            if (tipoExameNew != null && !tipoExameNew.equals(tipoExameOld)) {
                tipoExameNew.getExameCollection().add(exame);
                tipoExameNew = em.merge(tipoExameNew);
            }
            for (Pedidoexame pedidoexameCollectionNewPedidoexame : pedidoexameCollectionNew) {
                if (!pedidoexameCollectionOld.contains(pedidoexameCollectionNewPedidoexame)) {
                    Exame oldExameOfPedidoexameCollectionNewPedidoexame = pedidoexameCollectionNewPedidoexame.getExame();
                    pedidoexameCollectionNewPedidoexame.setExame(exame);
                    pedidoexameCollectionNewPedidoexame = em.merge(pedidoexameCollectionNewPedidoexame);
                    if (oldExameOfPedidoexameCollectionNewPedidoexame != null && !oldExameOfPedidoexameCollectionNewPedidoexame.equals(exame)) {
                        oldExameOfPedidoexameCollectionNewPedidoexame.getPedidoexameCollection().remove(pedidoexameCollectionNewPedidoexame);
                        oldExameOfPedidoexameCollectionNewPedidoexame = em.merge(oldExameOfPedidoexameCollectionNewPedidoexame);
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
                Integer id = exame.getIdExame();
                if (findExame(id) == null) {
                    throw new NonexistentEntityException("The exame with id " + id + " no longer exists.");
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
            Exame exame;
            try {
                exame = em.getReference(Exame.class, id);
                exame.getIdExame();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The exame with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Pedidoexame> pedidoexameCollectionOrphanCheck = exame.getPedidoexameCollection();
            for (Pedidoexame pedidoexameCollectionOrphanCheckPedidoexame : pedidoexameCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Exame (" + exame + ") cannot be destroyed since the Pedidoexame " + pedidoexameCollectionOrphanCheckPedidoexame + " in its pedidoexameCollection field has a non-nullable exame field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Tipoexame tipoExame = exame.getTipoExame();
            if (tipoExame != null) {
                tipoExame.getExameCollection().remove(exame);
                tipoExame = em.merge(tipoExame);
            }
            em.remove(exame);
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

    public List<Exame> findExameEntities() {
        return findExameEntities(true, -1, -1);
    }

    public List<Exame> findExameEntities(int maxResults, int firstResult) {
        return findExameEntities(false, maxResults, firstResult);
    }

    private List<Exame> findExameEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Exame.class));
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

    public Exame findExame(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Exame.class, id);
        } finally {
            em.close();
        }
    }

    public int getExameCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Exame> rt = cq.from(Exame.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
