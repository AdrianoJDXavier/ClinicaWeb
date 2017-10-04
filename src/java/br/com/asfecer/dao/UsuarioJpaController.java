/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Agenda;
import br.com.asfecer.model.Usuario;
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
public class UsuarioJpaController implements Serializable {

    public UsuarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Usuario usuario) throws RollbackFailureException, Exception {
        if (usuario.getAgendaCollection() == null) {
            usuario.setAgendaCollection(new ArrayList<Agenda>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Agenda> attachedAgendaCollection = new ArrayList<Agenda>();
            for (Agenda agendaCollectionAgendaToAttach : usuario.getAgendaCollection()) {
                agendaCollectionAgendaToAttach = em.getReference(agendaCollectionAgendaToAttach.getClass(), agendaCollectionAgendaToAttach.getRegistroAgenda());
                attachedAgendaCollection.add(agendaCollectionAgendaToAttach);
            }
            usuario.setAgendaCollection(attachedAgendaCollection);
            em.persist(usuario);
            for (Agenda agendaCollectionAgenda : usuario.getAgendaCollection()) {
                Usuario oldUsuarioOfAgendaCollectionAgenda = agendaCollectionAgenda.getUsuario();
                agendaCollectionAgenda.setUsuario(usuario);
                agendaCollectionAgenda = em.merge(agendaCollectionAgenda);
                if (oldUsuarioOfAgendaCollectionAgenda != null) {
                    oldUsuarioOfAgendaCollectionAgenda.getAgendaCollection().remove(agendaCollectionAgenda);
                    oldUsuarioOfAgendaCollectionAgenda = em.merge(oldUsuarioOfAgendaCollectionAgenda);
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

    public void edit(Usuario usuario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Usuario persistentUsuario = em.find(Usuario.class, usuario.getIdUsuario());
            Collection<Agenda> agendaCollectionOld = persistentUsuario.getAgendaCollection();
            Collection<Agenda> agendaCollectionNew = usuario.getAgendaCollection();
            List<String> illegalOrphanMessages = null;
            for (Agenda agendaCollectionOldAgenda : agendaCollectionOld) {
                if (!agendaCollectionNew.contains(agendaCollectionOldAgenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agenda " + agendaCollectionOldAgenda + " since its usuario field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Agenda> attachedAgendaCollectionNew = new ArrayList<Agenda>();
            for (Agenda agendaCollectionNewAgendaToAttach : agendaCollectionNew) {
                agendaCollectionNewAgendaToAttach = em.getReference(agendaCollectionNewAgendaToAttach.getClass(), agendaCollectionNewAgendaToAttach.getRegistroAgenda());
                attachedAgendaCollectionNew.add(agendaCollectionNewAgendaToAttach);
            }
            agendaCollectionNew = attachedAgendaCollectionNew;
            usuario.setAgendaCollection(agendaCollectionNew);
            usuario = em.merge(usuario);
            for (Agenda agendaCollectionNewAgenda : agendaCollectionNew) {
                if (!agendaCollectionOld.contains(agendaCollectionNewAgenda)) {
                    Usuario oldUsuarioOfAgendaCollectionNewAgenda = agendaCollectionNewAgenda.getUsuario();
                    agendaCollectionNewAgenda.setUsuario(usuario);
                    agendaCollectionNewAgenda = em.merge(agendaCollectionNewAgenda);
                    if (oldUsuarioOfAgendaCollectionNewAgenda != null && !oldUsuarioOfAgendaCollectionNewAgenda.equals(usuario)) {
                        oldUsuarioOfAgendaCollectionNewAgenda.getAgendaCollection().remove(agendaCollectionNewAgenda);
                        oldUsuarioOfAgendaCollectionNewAgenda = em.merge(oldUsuarioOfAgendaCollectionNewAgenda);
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
                Integer id = usuario.getIdUsuario();
                if (findUsuario(id) == null) {
                    throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.");
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
            Usuario usuario;
            try {
                usuario = em.getReference(Usuario.class, id);
                usuario.getIdUsuario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The usuario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Agenda> agendaCollectionOrphanCheck = usuario.getAgendaCollection();
            for (Agenda agendaCollectionOrphanCheckAgenda : agendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Usuario (" + usuario + ") cannot be destroyed since the Agenda " + agendaCollectionOrphanCheckAgenda + " in its agendaCollection field has a non-nullable usuario field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(usuario);
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

    public List<Usuario> findUsuarioEntities() {
        return findUsuarioEntities(true, -1, -1);
    }

    public List<Usuario> findUsuarioEntities(int maxResults, int firstResult) {
        return findUsuarioEntities(false, maxResults, firstResult);
    }

    private List<Usuario> findUsuarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Usuario.class));
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

    public Usuario findUsuario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Usuario.class, id);
        } finally {
            em.close();
        }
    }

    public int getUsuarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Usuario> rt = cq.from(Usuario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
