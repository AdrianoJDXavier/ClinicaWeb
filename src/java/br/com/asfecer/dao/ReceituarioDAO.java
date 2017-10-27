/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Itensreceituario;
import br.com.asfecer.model.Receituario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Adriano Xavier
 */
public class ReceituarioDAO implements Serializable {

    public ReceituarioDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Receituario receituario) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Consulta consulta = receituario.getConsulta();
            if (consulta != null) {
                consulta = em.getReference(consulta.getClass(), consulta.getIdconsulta());
                receituario.setConsulta(consulta);
            }
            Itensreceituario tipoReceituario = receituario.getTipoReceituario();
            if (tipoReceituario != null) {
                tipoReceituario = em.getReference(tipoReceituario.getClass(), tipoReceituario.getIditensreceituario());
                receituario.setTipoReceituario(tipoReceituario);
            }
            em.persist(receituario);
            if (consulta != null) {
                consulta.getReceituarioCollection().add(receituario);
                consulta = em.merge(consulta);
            }
            if (tipoReceituario != null) {
                tipoReceituario.getReceituarioCollection().add(receituario);
                tipoReceituario = em.merge(tipoReceituario);
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

    public void edit(Receituario receituario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Receituario persistentReceituario = em.find(Receituario.class, receituario.getIdreceituario());
            Consulta consultaOld = persistentReceituario.getConsulta();
            Consulta consultaNew = receituario.getConsulta();
            Itensreceituario tipoReceituarioOld = persistentReceituario.getTipoReceituario();
            Itensreceituario tipoReceituarioNew = receituario.getTipoReceituario();
            if (consultaNew != null) {
                consultaNew = em.getReference(consultaNew.getClass(), consultaNew.getIdconsulta());
                receituario.setConsulta(consultaNew);
            }
            if (tipoReceituarioNew != null) {
                tipoReceituarioNew = em.getReference(tipoReceituarioNew.getClass(), tipoReceituarioNew.getIditensreceituario());
                receituario.setTipoReceituario(tipoReceituarioNew);
            }
            receituario = em.merge(receituario);
            if (consultaOld != null && !consultaOld.equals(consultaNew)) {
                consultaOld.getReceituarioCollection().remove(receituario);
                consultaOld = em.merge(consultaOld);
            }
            if (consultaNew != null && !consultaNew.equals(consultaOld)) {
                consultaNew.getReceituarioCollection().add(receituario);
                consultaNew = em.merge(consultaNew);
            }
            if (tipoReceituarioOld != null && !tipoReceituarioOld.equals(tipoReceituarioNew)) {
                tipoReceituarioOld.getReceituarioCollection().remove(receituario);
                tipoReceituarioOld = em.merge(tipoReceituarioOld);
            }
            if (tipoReceituarioNew != null && !tipoReceituarioNew.equals(tipoReceituarioOld)) {
                tipoReceituarioNew.getReceituarioCollection().add(receituario);
                tipoReceituarioNew = em.merge(tipoReceituarioNew);
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
                Integer id = receituario.getIdreceituario();
                if (findReceituario(id) == null) {
                    throw new NonexistentEntityException("The receituario with id " + id + " no longer exists.");
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
            Receituario receituario;
            try {
                receituario = em.getReference(Receituario.class, id);
                receituario.getIdreceituario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The receituario with id " + id + " no longer exists.", enfe);
            }
            Consulta consulta = receituario.getConsulta();
            if (consulta != null) {
                consulta.getReceituarioCollection().remove(receituario);
                consulta = em.merge(consulta);
            }
            Itensreceituario tipoReceituario = receituario.getTipoReceituario();
            if (tipoReceituario != null) {
                tipoReceituario.getReceituarioCollection().remove(receituario);
                tipoReceituario = em.merge(tipoReceituario);
            }
            em.remove(receituario);
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

    public List<Receituario> findReceituarioEntities() {
        return findReceituarioEntities(true, -1, -1);
    }

    public List<Receituario> findReceituarioEntities(int maxResults, int firstResult) {
        return findReceituarioEntities(false, maxResults, firstResult);
    }

    private List<Receituario> findReceituarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Receituario.class));
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

    public Receituario findReceituario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Receituario.class, id);
        } finally {
            em.close();
        }
    }

    public int getReceituarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Receituario> rt = cq.from(Receituario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
