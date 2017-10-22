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
import br.com.asfecer.model.TipoExame;
import br.com.asfecer.model.PedidoExame;
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

    public void create(Exame exame) throws RollbackFailureException, RuntimeException {
        if (exame.getPedidoExameCollection() == null) {
            exame.setPedidoExameCollection(new ArrayList<PedidoExame>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoExame tipoExame = exame.getTipoExame();
            if (tipoExame != null) {
                tipoExame = em.getReference(tipoExame.getClass(), tipoExame.getIdTipoExame());
                exame.setTipoExame(tipoExame);
            }
            Collection<PedidoExame> attachedPedidoExameCollection = new ArrayList<PedidoExame>();
            for (PedidoExame pedidoexameCollectionPedidoExameToAttach : exame.getPedidoExameCollection()) {
                pedidoexameCollectionPedidoExameToAttach = em.getReference(pedidoexameCollectionPedidoExameToAttach.getClass(), pedidoexameCollectionPedidoExameToAttach.getIdPedidoExame());
                attachedPedidoExameCollection.add(pedidoexameCollectionPedidoExameToAttach);
            }
            exame.setPedidoExameCollection(attachedPedidoExameCollection);
            em.persist(exame);
            if (tipoExame != null) {
                tipoExame.getExameCollection().add(exame);
                tipoExame = em.merge(tipoExame);
            }
            for (PedidoExame pedidoexameCollectionPedidoExame : exame.getPedidoExameCollection()) {
                Exame oldExameOfPedidoExameCollectionPedidoExame = pedidoexameCollectionPedidoExame.getExame();
                pedidoexameCollectionPedidoExame.setExame(exame);
                pedidoexameCollectionPedidoExame = em.merge(pedidoexameCollectionPedidoExame);
                if (oldExameOfPedidoExameCollectionPedidoExame != null) {
                    oldExameOfPedidoExameCollectionPedidoExame.getPedidoExameCollection().remove(pedidoexameCollectionPedidoExame);
                    oldExameOfPedidoExameCollectionPedidoExame = em.merge(oldExameOfPedidoExameCollectionPedidoExame);
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

    public void edit(Exame exame) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Exame persistentExame = em.find(Exame.class, exame.getIdExame());
            TipoExame tipoExameOld = persistentExame.getTipoExame();
            TipoExame tipoExameNew = exame.getTipoExame();
            Collection<PedidoExame> pedidoexameCollectionOld = persistentExame.getPedidoExameCollection();
            Collection<PedidoExame> pedidoexameCollectionNew = exame.getPedidoExameCollection();
            List<String> illegalOrphanMessages = null;
            for (PedidoExame pedidoexameCollectionOldPedidoExame : pedidoexameCollectionOld) {
                if (!pedidoexameCollectionNew.contains(pedidoexameCollectionOldPedidoExame)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain PedidoExame " + pedidoexameCollectionOldPedidoExame + " since its exame field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (tipoExameNew != null) {
                tipoExameNew = em.getReference(tipoExameNew.getClass(), tipoExameNew.getIdTipoExame());
                exame.setTipoExame(tipoExameNew);
            }
            Collection<PedidoExame> attachedPedidoExameCollectionNew = new ArrayList<PedidoExame>();
            for (PedidoExame pedidoexameCollectionNewPedidoExameToAttach : pedidoexameCollectionNew) {
                pedidoexameCollectionNewPedidoExameToAttach = em.getReference(pedidoexameCollectionNewPedidoExameToAttach.getClass(), pedidoexameCollectionNewPedidoExameToAttach.getIdPedidoExame());
                attachedPedidoExameCollectionNew.add(pedidoexameCollectionNewPedidoExameToAttach);
            }
            pedidoexameCollectionNew = attachedPedidoExameCollectionNew;
            exame.setPedidoExameCollection(pedidoexameCollectionNew);
            exame = em.merge(exame);
            if (tipoExameOld != null && !tipoExameOld.equals(tipoExameNew)) {
                tipoExameOld.getExameCollection().remove(exame);
                tipoExameOld = em.merge(tipoExameOld);
            }
            if (tipoExameNew != null && !tipoExameNew.equals(tipoExameOld)) {
                tipoExameNew.getExameCollection().add(exame);
                tipoExameNew = em.merge(tipoExameNew);
            }
            for (PedidoExame pedidoexameCollectionNewPedidoExame : pedidoexameCollectionNew) {
                if (!pedidoexameCollectionOld.contains(pedidoexameCollectionNewPedidoExame)) {
                    Exame oldExameOfPedidoExameCollectionNewPedidoExame = pedidoexameCollectionNewPedidoExame.getExame();
                    pedidoexameCollectionNewPedidoExame.setExame(exame);
                    pedidoexameCollectionNewPedidoExame = em.merge(pedidoexameCollectionNewPedidoExame);
                    if (oldExameOfPedidoExameCollectionNewPedidoExame != null && !oldExameOfPedidoExameCollectionNewPedidoExame.equals(exame)) {
                        oldExameOfPedidoExameCollectionNewPedidoExame.getPedidoExameCollection().remove(pedidoexameCollectionNewPedidoExame);
                        oldExameOfPedidoExameCollectionNewPedidoExame = em.merge(oldExameOfPedidoExameCollectionNewPedidoExame);
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
            Exame exame;
            try {
                exame = em.getReference(Exame.class, id);
                exame.getIdExame();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The exame with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<PedidoExame> pedidoexameCollectionOrphanCheck = exame.getPedidoExameCollection();
            for (PedidoExame pedidoexameCollectionOrphanCheckPedidoExame : pedidoexameCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Exame (" + exame + ") cannot be destroyed since the PedidoExame " + pedidoexameCollectionOrphanCheckPedidoExame + " in its pedidoexameCollection field has a non-nullable exame field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            TipoExame tipoExame = exame.getTipoExame();
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
            throw new RuntimeException(ex);
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
