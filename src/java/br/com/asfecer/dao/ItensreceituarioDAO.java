/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Itensreceituario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Medicamento;
import br.com.asfecer.model.Receituario;
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
public class ItensreceituarioDAO implements Serializable {

    public ItensreceituarioDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Itensreceituario itensreceituario) throws RollbackFailureException, RuntimeException {
        if (itensreceituario.getReceituarioCollection() == null) {
            itensreceituario.setReceituarioCollection(new ArrayList<Receituario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento medicamento = itensreceituario.getMedicamento();
            if (medicamento != null) {
                medicamento = em.getReference(medicamento.getClass(), medicamento.getIdMedicamento());
                itensreceituario.setMedicamento(medicamento);
            }
            Collection<Receituario> attachedReceituarioCollection = new ArrayList<Receituario>();
            for (Receituario receituarioCollectionReceituarioToAttach : itensreceituario.getReceituarioCollection()) {
                receituarioCollectionReceituarioToAttach = em.getReference(receituarioCollectionReceituarioToAttach.getClass(), receituarioCollectionReceituarioToAttach.getIdReceituario());
                attachedReceituarioCollection.add(receituarioCollectionReceituarioToAttach);
            }
            itensreceituario.setReceituarioCollection(attachedReceituarioCollection);
            em.persist(itensreceituario);
            if (medicamento != null) {
                medicamento.getItensreceituarioCollection().add(itensreceituario);
                medicamento = em.merge(medicamento);
            }
            for (Receituario receituarioCollectionReceituario : itensreceituario.getReceituarioCollection()) {
                Itensreceituario oldTipoReceituarioOfReceituarioCollectionReceituario = receituarioCollectionReceituario.getTipoReceituario();
                receituarioCollectionReceituario.setTipoReceituario(itensreceituario);
                receituarioCollectionReceituario = em.merge(receituarioCollectionReceituario);
                if (oldTipoReceituarioOfReceituarioCollectionReceituario != null) {
                    oldTipoReceituarioOfReceituarioCollectionReceituario.getReceituarioCollection().remove(receituarioCollectionReceituario);
                    oldTipoReceituarioOfReceituarioCollectionReceituario = em.merge(oldTipoReceituarioOfReceituarioCollectionReceituario);
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

    public void edit(Itensreceituario itensreceituario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Itensreceituario persistentItensreceituario = em.find(Itensreceituario.class, itensreceituario.getIdItensReceituario());
            Medicamento medicamentoOld = persistentItensreceituario.getMedicamento();
            Medicamento medicamentoNew = itensreceituario.getMedicamento();
            Collection<Receituario> receituarioCollectionOld = persistentItensreceituario.getReceituarioCollection();
            Collection<Receituario> receituarioCollectionNew = itensreceituario.getReceituarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Receituario receituarioCollectionOldReceituario : receituarioCollectionOld) {
                if (!receituarioCollectionNew.contains(receituarioCollectionOldReceituario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Receituario " + receituarioCollectionOldReceituario + " since its tipoReceituario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (medicamentoNew != null) {
                medicamentoNew = em.getReference(medicamentoNew.getClass(), medicamentoNew.getIdMedicamento());
                itensreceituario.setMedicamento(medicamentoNew);
            }
            Collection<Receituario> attachedReceituarioCollectionNew = new ArrayList<Receituario>();
            for (Receituario receituarioCollectionNewReceituarioToAttach : receituarioCollectionNew) {
                receituarioCollectionNewReceituarioToAttach = em.getReference(receituarioCollectionNewReceituarioToAttach.getClass(), receituarioCollectionNewReceituarioToAttach.getIdReceituario());
                attachedReceituarioCollectionNew.add(receituarioCollectionNewReceituarioToAttach);
            }
            receituarioCollectionNew = attachedReceituarioCollectionNew;
            itensreceituario.setReceituarioCollection(receituarioCollectionNew);
            itensreceituario = em.merge(itensreceituario);
            if (medicamentoOld != null && !medicamentoOld.equals(medicamentoNew)) {
                medicamentoOld.getItensreceituarioCollection().remove(itensreceituario);
                medicamentoOld = em.merge(medicamentoOld);
            }
            if (medicamentoNew != null && !medicamentoNew.equals(medicamentoOld)) {
                medicamentoNew.getItensreceituarioCollection().add(itensreceituario);
                medicamentoNew = em.merge(medicamentoNew);
            }
            for (Receituario receituarioCollectionNewReceituario : receituarioCollectionNew) {
                if (!receituarioCollectionOld.contains(receituarioCollectionNewReceituario)) {
                    Itensreceituario oldTipoReceituarioOfReceituarioCollectionNewReceituario = receituarioCollectionNewReceituario.getTipoReceituario();
                    receituarioCollectionNewReceituario.setTipoReceituario(itensreceituario);
                    receituarioCollectionNewReceituario = em.merge(receituarioCollectionNewReceituario);
                    if (oldTipoReceituarioOfReceituarioCollectionNewReceituario != null && !oldTipoReceituarioOfReceituarioCollectionNewReceituario.equals(itensreceituario)) {
                        oldTipoReceituarioOfReceituarioCollectionNewReceituario.getReceituarioCollection().remove(receituarioCollectionNewReceituario);
                        oldTipoReceituarioOfReceituarioCollectionNewReceituario = em.merge(oldTipoReceituarioOfReceituarioCollectionNewReceituario);
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
                Integer id = itensreceituario.getIdItensReceituario();
                if (findItensreceituario(id) == null) {
                    throw new NonexistentEntityException("The itensreceituario with id " + id + " no longer exists.");
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
            Itensreceituario itensreceituario;
            try {
                itensreceituario = em.getReference(Itensreceituario.class, id);
                itensreceituario.getIdItensReceituario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itensreceituario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Receituario> receituarioCollectionOrphanCheck = itensreceituario.getReceituarioCollection();
            for (Receituario receituarioCollectionOrphanCheckReceituario : receituarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Itensreceituario (" + itensreceituario + ") cannot be destroyed since the Receituario " + receituarioCollectionOrphanCheckReceituario + " in its receituarioCollection field has a non-nullable tipoReceituario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medicamento medicamento = itensreceituario.getMedicamento();
            if (medicamento != null) {
                medicamento.getItensreceituarioCollection().remove(itensreceituario);
                medicamento = em.merge(medicamento);
            }
            em.remove(itensreceituario);
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

    public List<Itensreceituario> findItensreceituarioEntities() {
        return findItensreceituarioEntities(true, -1, -1);
    }

    public List<Itensreceituario> findItensreceituarioEntities(int maxResults, int firstResult) {
        return findItensreceituarioEntities(false, maxResults, firstResult);
    }

    private List<Itensreceituario> findItensreceituarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Itensreceituario.class));
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

    public Itensreceituario findItensreceituario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Itensreceituario.class, id);
        } finally {
            em.close();
        }
    }

    public int getItensreceituarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Itensreceituario> rt = cq.from(Itensreceituario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
