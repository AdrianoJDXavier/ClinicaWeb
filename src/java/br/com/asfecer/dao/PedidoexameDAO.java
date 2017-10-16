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
import br.com.asfecer.model.Exame;
import br.com.asfecer.model.Pedidoexame;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class PedidoexameDAO implements Serializable {

    public PedidoexameDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Pedidoexame pedidoexame) throws RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Consulta consulta = pedidoexame.getConsulta();
            if (consulta != null) {
                consulta = em.getReference(consulta.getClass(), consulta.getIdConsulta());
                pedidoexame.setConsulta(consulta);
            }
            Exame exame = pedidoexame.getExame();
            if (exame != null) {
                exame = em.getReference(exame.getClass(), exame.getIdExame());
                pedidoexame.setExame(exame);
            }
            em.persist(pedidoexame);
            if (consulta != null) {
                consulta.getPedidoexameCollection().add(pedidoexame);
                consulta = em.merge(consulta);
            }
            if (exame != null) {
                exame.getPedidoexameCollection().add(pedidoexame);
                exame = em.merge(exame);
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

    public void edit(Pedidoexame pedidoexame) throws NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Pedidoexame persistentPedidoexame = em.find(Pedidoexame.class, pedidoexame.getIdPedidoExame());
            Consulta consultaOld = persistentPedidoexame.getConsulta();
            Consulta consultaNew = pedidoexame.getConsulta();
            Exame exameOld = persistentPedidoexame.getExame();
            Exame exameNew = pedidoexame.getExame();
            if (consultaNew != null) {
                consultaNew = em.getReference(consultaNew.getClass(), consultaNew.getIdConsulta());
                pedidoexame.setConsulta(consultaNew);
            }
            if (exameNew != null) {
                exameNew = em.getReference(exameNew.getClass(), exameNew.getIdExame());
                pedidoexame.setExame(exameNew);
            }
            pedidoexame = em.merge(pedidoexame);
            if (consultaOld != null && !consultaOld.equals(consultaNew)) {
                consultaOld.getPedidoexameCollection().remove(pedidoexame);
                consultaOld = em.merge(consultaOld);
            }
            if (consultaNew != null && !consultaNew.equals(consultaOld)) {
                consultaNew.getPedidoexameCollection().add(pedidoexame);
                consultaNew = em.merge(consultaNew);
            }
            if (exameOld != null && !exameOld.equals(exameNew)) {
                exameOld.getPedidoexameCollection().remove(pedidoexame);
                exameOld = em.merge(exameOld);
            }
            if (exameNew != null && !exameNew.equals(exameOld)) {
                exameNew.getPedidoexameCollection().add(pedidoexame);
                exameNew = em.merge(exameNew);
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
                Integer id = pedidoexame.getIdPedidoExame();
                if (findPedidoexame(id) == null) {
                    throw new NonexistentEntityException("The pedidoexame with id " + id + " no longer exists.");
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
            Pedidoexame pedidoexame;
            try {
                pedidoexame = em.getReference(Pedidoexame.class, id);
                pedidoexame.getIdPedidoExame();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The pedidoexame with id " + id + " no longer exists.", enfe);
            }
            Consulta consulta = pedidoexame.getConsulta();
            if (consulta != null) {
                consulta.getPedidoexameCollection().remove(pedidoexame);
                consulta = em.merge(consulta);
            }
            Exame exame = pedidoexame.getExame();
            if (exame != null) {
                exame.getPedidoexameCollection().remove(pedidoexame);
                exame = em.merge(exame);
            }
            em.remove(pedidoexame);
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

    public List<Pedidoexame> findPedidoexameEntities() {
        return findPedidoexameEntities(true, -1, -1);
    }

    public List<Pedidoexame> findPedidoexameEntities(int maxResults, int firstResult) {
        return findPedidoexameEntities(false, maxResults, firstResult);
    }

    private List<Pedidoexame> findPedidoexameEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Pedidoexame.class));
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

    public Pedidoexame findPedidoexame(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Pedidoexame.class, id);
        } finally {
            em.close();
        }
    }

    public int getPedidoexameCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Pedidoexame> rt = cq.from(Pedidoexame.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
