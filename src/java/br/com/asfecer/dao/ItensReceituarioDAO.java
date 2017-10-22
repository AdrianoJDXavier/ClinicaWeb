/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.ItensReceituario;
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
public class ItensReceituarioDAO implements Serializable {

    public ItensReceituarioDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(ItensReceituario itensReceituario) throws RollbackFailureException, RuntimeException {
        if (itensReceituario.getReceituarioCollection() == null) {
            itensReceituario.setReceituarioCollection(new ArrayList<Receituario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento medicamento = itensReceituario.getMedicamento();
            if (medicamento != null) {
                medicamento = em.getReference(medicamento.getClass(), medicamento.getIdMedicamento());
                itensReceituario.setMedicamento(medicamento);
            }
            Collection<Receituario> attachedReceituarioCollection = new ArrayList<Receituario>();
            for (Receituario receituarioCollectionReceituarioToAttach : itensReceituario.getReceituarioCollection()) {
                receituarioCollectionReceituarioToAttach = em.getReference(receituarioCollectionReceituarioToAttach.getClass(), receituarioCollectionReceituarioToAttach.getIdReceituario());
                attachedReceituarioCollection.add(receituarioCollectionReceituarioToAttach);
            }
            itensReceituario.setReceituarioCollection(attachedReceituarioCollection);
            em.persist(itensReceituario);
            if (medicamento != null) {
                medicamento.getItensReceituarioCollection().add(itensReceituario);
                medicamento = em.merge(medicamento);
            }
            for (Receituario receituarioCollectionReceituario : itensReceituario.getReceituarioCollection()) {
                ItensReceituario oldTipoReceituarioOfReceituarioCollectionReceituario = receituarioCollectionReceituario.getTipoReceituario();
                receituarioCollectionReceituario.setTipoReceituario(itensReceituario);
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

    public void edit(ItensReceituario itensReceituario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ItensReceituario persistentItensReceituario = em.find(ItensReceituario.class, itensReceituario.getIdItensReceituario());
            Medicamento medicamentoOld = persistentItensReceituario.getMedicamento();
            Medicamento medicamentoNew = itensReceituario.getMedicamento();
            Collection<Receituario> receituarioCollectionOld = persistentItensReceituario.getReceituarioCollection();
            Collection<Receituario> receituarioCollectionNew = itensReceituario.getReceituarioCollection();
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
                itensReceituario.setMedicamento(medicamentoNew);
            }
            Collection<Receituario> attachedReceituarioCollectionNew = new ArrayList<Receituario>();
            for (Receituario receituarioCollectionNewReceituarioToAttach : receituarioCollectionNew) {
                receituarioCollectionNewReceituarioToAttach = em.getReference(receituarioCollectionNewReceituarioToAttach.getClass(), receituarioCollectionNewReceituarioToAttach.getIdReceituario());
                attachedReceituarioCollectionNew.add(receituarioCollectionNewReceituarioToAttach);
            }
            receituarioCollectionNew = attachedReceituarioCollectionNew;
            itensReceituario.setReceituarioCollection(receituarioCollectionNew);
            itensReceituario = em.merge(itensReceituario);
            if (medicamentoOld != null && !medicamentoOld.equals(medicamentoNew)) {
                medicamentoOld.getItensReceituarioCollection().remove(itensReceituario);
                medicamentoOld = em.merge(medicamentoOld);
            }
            if (medicamentoNew != null && !medicamentoNew.equals(medicamentoOld)) {
                medicamentoNew.getItensReceituarioCollection().add(itensReceituario);
                medicamentoNew = em.merge(medicamentoNew);
            }
            for (Receituario receituarioCollectionNewReceituario : receituarioCollectionNew) {
                if (!receituarioCollectionOld.contains(receituarioCollectionNewReceituario)) {
                    ItensReceituario oldTipoReceituarioOfReceituarioCollectionNewReceituario = receituarioCollectionNewReceituario.getTipoReceituario();
                    receituarioCollectionNewReceituario.setTipoReceituario(itensReceituario);
                    receituarioCollectionNewReceituario = em.merge(receituarioCollectionNewReceituario);
                    if (oldTipoReceituarioOfReceituarioCollectionNewReceituario != null && !oldTipoReceituarioOfReceituarioCollectionNewReceituario.equals(itensReceituario)) {
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
                Integer id = itensReceituario.getIdItensReceituario();
                if (findItensReceituario(id) == null) {
                    throw new NonexistentEntityException("The itensReceituario with id " + id + " no longer exists.");
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
            ItensReceituario itensReceituario;
            try {
                itensReceituario = em.getReference(ItensReceituario.class, id);
                itensReceituario.getIdItensReceituario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itensReceituario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Receituario> receituarioCollectionOrphanCheck = itensReceituario.getReceituarioCollection();
            for (Receituario receituarioCollectionOrphanCheckReceituario : receituarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This ItensReceituario (" + itensReceituario + ") cannot be destroyed since the Receituario " + receituarioCollectionOrphanCheckReceituario + " in its receituarioCollection field has a non-nullable tipoReceituario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medicamento medicamento = itensReceituario.getMedicamento();
            if (medicamento != null) {
                medicamento.getItensReceituarioCollection().remove(itensReceituario);
                medicamento = em.merge(medicamento);
            }
            em.remove(itensReceituario);
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

    public List<ItensReceituario> findItensReceituarioEntities() {
        return findItensReceituarioEntities(true, -1, -1);
    }

    public List<ItensReceituario> findItensReceituarioEntities(int maxResults, int firstResult) {
        return findItensReceituarioEntities(false, maxResults, firstResult);
    }

    private List<ItensReceituario> findItensReceituarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(ItensReceituario.class));
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

    public ItensReceituario findItensReceituario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(ItensReceituario.class, id);
        } finally {
            em.close();
        }
    }

    public int getItensReceituarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<ItensReceituario> rt = cq.from(ItensReceituario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
