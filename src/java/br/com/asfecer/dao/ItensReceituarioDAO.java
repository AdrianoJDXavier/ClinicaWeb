/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.ItensReceituario;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Medicamento;
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
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento medicamento = itensReceituario.getMedicamento();
            if (medicamento != null) {
                medicamento = em.getReference(medicamento.getClass(), medicamento.getIdMedicamento());
                itensReceituario.setMedicamento(medicamento);
            }
            em.persist(itensReceituario);
            if (medicamento != null) {
                medicamento.getItensReceituarioCollection().add(itensReceituario);
                medicamento = em.merge(medicamento);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(ItensReceituario itensReceituario) throws NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            ItensReceituario persistentItensReceituario = em.find(ItensReceituario.class, itensReceituario.getIdItensReceituario());
            Medicamento medicamentoOld = persistentItensReceituario.getMedicamento();
            Medicamento medicamentoNew = itensReceituario.getMedicamento();
            if (medicamentoNew != null) {
                medicamentoNew = em.getReference(medicamentoNew.getClass(), medicamentoNew.getIdMedicamento());
                itensReceituario.setMedicamento(medicamentoNew);
            }
            itensReceituario = em.merge(itensReceituario);
            if (medicamentoOld != null && !medicamentoOld.equals(medicamentoNew)) {
                medicamentoOld.getItensReceituarioCollection().remove(itensReceituario);
                medicamentoOld = em.merge(medicamentoOld);
            }
            if (medicamentoNew != null && !medicamentoNew.equals(medicamentoOld)) {
                medicamentoNew.getItensReceituarioCollection().add(itensReceituario);
                medicamentoNew = em.merge(medicamentoNew);
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
            new RuntimeException(ex);
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
            ItensReceituario itensReceituario;
            try {
                itensReceituario = em.getReference(ItensReceituario.class, id);
                itensReceituario.getIdItensReceituario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The itensReceituario with id " + id + " no longer exists.", enfe);
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
            new RuntimeException(ex);
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
