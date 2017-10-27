/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.PreexistingEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Prontuario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Adriano Xavier
 */
public class ProntuarioDAO implements Serializable {

    public ProntuarioDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Prontuario prontuario) throws PreexistingEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Consulta consulta = prontuario.getConsulta();
            if (consulta != null) {
                consulta = em.getReference(consulta.getClass(), consulta.getIdconsulta());
                prontuario.setConsulta(consulta);
            }
            em.persist(prontuario);
            if (consulta != null) {
                consulta.getProntuarioCollection().add(prontuario);
                consulta = em.merge(consulta);
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findProntuario(prontuario.getIdprontuario()) != null) {
                throw new PreexistingEntityException("Prontuario " + prontuario + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Prontuario prontuario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Prontuario persistentProntuario = em.find(Prontuario.class, prontuario.getIdprontuario());
            Consulta consultaOld = persistentProntuario.getConsulta();
            Consulta consultaNew = prontuario.getConsulta();
            if (consultaNew != null) {
                consultaNew = em.getReference(consultaNew.getClass(), consultaNew.getIdconsulta());
                prontuario.setConsulta(consultaNew);
            }
            prontuario = em.merge(prontuario);
            if (consultaOld != null && !consultaOld.equals(consultaNew)) {
                consultaOld.getProntuarioCollection().remove(prontuario);
                consultaOld = em.merge(consultaOld);
            }
            if (consultaNew != null && !consultaNew.equals(consultaOld)) {
                consultaNew.getProntuarioCollection().add(prontuario);
                consultaNew = em.merge(consultaNew);
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
                Integer id = prontuario.getIdprontuario();
                if (findProntuario(id) == null) {
                    throw new NonexistentEntityException("The prontuario with id " + id + " no longer exists.");
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
            Prontuario prontuario;
            try {
                prontuario = em.getReference(Prontuario.class, id);
                prontuario.getIdprontuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The prontuario with id " + id + " no longer exists.", enfe);
            }
            Consulta consulta = prontuario.getConsulta();
            if (consulta != null) {
                consulta.getProntuarioCollection().remove(prontuario);
                consulta = em.merge(consulta);
            }
            em.remove(prontuario);
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

    public List<Prontuario> findProntuarioEntities() {
        return findProntuarioEntities(true, -1, -1);
    }

    public List<Prontuario> findProntuarioEntities(int maxResults, int firstResult) {
        return findProntuarioEntities(false, maxResults, firstResult);
    }

    private List<Prontuario> findProntuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Prontuario.class));
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

    public Prontuario findProntuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Prontuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getProntuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Prontuario> rt = cq.from(Prontuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
