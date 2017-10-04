/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Atestado;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Tipoatestado;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class AtestadoJpaController implements Serializable {

    public AtestadoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Atestado atestado) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Consulta consulta = atestado.getConsulta();
            if (consulta != null) {
                consulta = em.getReference(consulta.getClass(), consulta.getIdConsulta());
                atestado.setConsulta(consulta);
            }
            Tipoatestado tipoAtestado = atestado.getTipoAtestado();
            if (tipoAtestado != null) {
                tipoAtestado = em.getReference(tipoAtestado.getClass(), tipoAtestado.getIdTipoAtestado());
                atestado.setTipoAtestado(tipoAtestado);
            }
            em.persist(atestado);
            if (consulta != null) {
                consulta.getAtestadoCollection().add(atestado);
                consulta = em.merge(consulta);
            }
            if (tipoAtestado != null) {
                tipoAtestado.getAtestadoCollection().add(atestado);
                tipoAtestado = em.merge(tipoAtestado);
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

    public void edit(Atestado atestado) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Atestado persistentAtestado = em.find(Atestado.class, atestado.getIdAtestado());
            Consulta consultaOld = persistentAtestado.getConsulta();
            Consulta consultaNew = atestado.getConsulta();
            Tipoatestado tipoAtestadoOld = persistentAtestado.getTipoAtestado();
            Tipoatestado tipoAtestadoNew = atestado.getTipoAtestado();
            if (consultaNew != null) {
                consultaNew = em.getReference(consultaNew.getClass(), consultaNew.getIdConsulta());
                atestado.setConsulta(consultaNew);
            }
            if (tipoAtestadoNew != null) {
                tipoAtestadoNew = em.getReference(tipoAtestadoNew.getClass(), tipoAtestadoNew.getIdTipoAtestado());
                atestado.setTipoAtestado(tipoAtestadoNew);
            }
            atestado = em.merge(atestado);
            if (consultaOld != null && !consultaOld.equals(consultaNew)) {
                consultaOld.getAtestadoCollection().remove(atestado);
                consultaOld = em.merge(consultaOld);
            }
            if (consultaNew != null && !consultaNew.equals(consultaOld)) {
                consultaNew.getAtestadoCollection().add(atestado);
                consultaNew = em.merge(consultaNew);
            }
            if (tipoAtestadoOld != null && !tipoAtestadoOld.equals(tipoAtestadoNew)) {
                tipoAtestadoOld.getAtestadoCollection().remove(atestado);
                tipoAtestadoOld = em.merge(tipoAtestadoOld);
            }
            if (tipoAtestadoNew != null && !tipoAtestadoNew.equals(tipoAtestadoOld)) {
                tipoAtestadoNew.getAtestadoCollection().add(atestado);
                tipoAtestadoNew = em.merge(tipoAtestadoNew);
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
                Integer id = atestado.getIdAtestado();
                if (findAtestado(id) == null) {
                    throw new NonexistentEntityException("The atestado with id " + id + " no longer exists.");
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
            Atestado atestado;
            try {
                atestado = em.getReference(Atestado.class, id);
                atestado.getIdAtestado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The atestado with id " + id + " no longer exists.", enfe);
            }
            Consulta consulta = atestado.getConsulta();
            if (consulta != null) {
                consulta.getAtestadoCollection().remove(atestado);
                consulta = em.merge(consulta);
            }
            Tipoatestado tipoAtestado = atestado.getTipoAtestado();
            if (tipoAtestado != null) {
                tipoAtestado.getAtestadoCollection().remove(atestado);
                tipoAtestado = em.merge(tipoAtestado);
            }
            em.remove(atestado);
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

    public List<Atestado> findAtestadoEntities() {
        return findAtestadoEntities(true, -1, -1);
    }

    public List<Atestado> findAtestadoEntities(int maxResults, int firstResult) {
        return findAtestadoEntities(false, maxResults, firstResult);
    }

    private List<Atestado> findAtestadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Atestado.class));
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

    public Atestado findAtestado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Atestado.class, id);
        } finally {
            em.close();
        }
    }

    public int getAtestadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Atestado> rt = cq.from(Atestado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
