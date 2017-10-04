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
import br.com.asfecer.model.Medico;
import br.com.asfecer.model.Agenda;
import br.com.asfecer.model.Horario;
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
public class HorarioJpaController implements Serializable {

    public HorarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Horario horario) throws RollbackFailureException, Exception {
        if (horario.getAgendaCollection() == null) {
            horario.setAgendaCollection(new ArrayList<Agenda>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medico medico = horario.getMedico();
            if (medico != null) {
                medico = em.getReference(medico.getClass(), medico.getIdMedico());
                horario.setMedico(medico);
            }
            Collection<Agenda> attachedAgendaCollection = new ArrayList<Agenda>();
            for (Agenda agendaCollectionAgendaToAttach : horario.getAgendaCollection()) {
                agendaCollectionAgendaToAttach = em.getReference(agendaCollectionAgendaToAttach.getClass(), agendaCollectionAgendaToAttach.getRegistroAgenda());
                attachedAgendaCollection.add(agendaCollectionAgendaToAttach);
            }
            horario.setAgendaCollection(attachedAgendaCollection);
            em.persist(horario);
            if (medico != null) {
                medico.getHorarioCollection().add(horario);
                medico = em.merge(medico);
            }
            for (Agenda agendaCollectionAgenda : horario.getAgendaCollection()) {
                Horario oldMedicoOfAgendaCollectionAgenda = agendaCollectionAgenda.getMedico();
                agendaCollectionAgenda.setMedico(horario);
                agendaCollectionAgenda = em.merge(agendaCollectionAgenda);
                if (oldMedicoOfAgendaCollectionAgenda != null) {
                    oldMedicoOfAgendaCollectionAgenda.getAgendaCollection().remove(agendaCollectionAgenda);
                    oldMedicoOfAgendaCollectionAgenda = em.merge(oldMedicoOfAgendaCollectionAgenda);
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

    public void edit(Horario horario) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Horario persistentHorario = em.find(Horario.class, horario.getIdHorario());
            Medico medicoOld = persistentHorario.getMedico();
            Medico medicoNew = horario.getMedico();
            Collection<Agenda> agendaCollectionOld = persistentHorario.getAgendaCollection();
            Collection<Agenda> agendaCollectionNew = horario.getAgendaCollection();
            List<String> illegalOrphanMessages = null;
            for (Agenda agendaCollectionOldAgenda : agendaCollectionOld) {
                if (!agendaCollectionNew.contains(agendaCollectionOldAgenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agenda " + agendaCollectionOldAgenda + " since its medico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (medicoNew != null) {
                medicoNew = em.getReference(medicoNew.getClass(), medicoNew.getIdMedico());
                horario.setMedico(medicoNew);
            }
            Collection<Agenda> attachedAgendaCollectionNew = new ArrayList<Agenda>();
            for (Agenda agendaCollectionNewAgendaToAttach : agendaCollectionNew) {
                agendaCollectionNewAgendaToAttach = em.getReference(agendaCollectionNewAgendaToAttach.getClass(), agendaCollectionNewAgendaToAttach.getRegistroAgenda());
                attachedAgendaCollectionNew.add(agendaCollectionNewAgendaToAttach);
            }
            agendaCollectionNew = attachedAgendaCollectionNew;
            horario.setAgendaCollection(agendaCollectionNew);
            horario = em.merge(horario);
            if (medicoOld != null && !medicoOld.equals(medicoNew)) {
                medicoOld.getHorarioCollection().remove(horario);
                medicoOld = em.merge(medicoOld);
            }
            if (medicoNew != null && !medicoNew.equals(medicoOld)) {
                medicoNew.getHorarioCollection().add(horario);
                medicoNew = em.merge(medicoNew);
            }
            for (Agenda agendaCollectionNewAgenda : agendaCollectionNew) {
                if (!agendaCollectionOld.contains(agendaCollectionNewAgenda)) {
                    Horario oldMedicoOfAgendaCollectionNewAgenda = agendaCollectionNewAgenda.getMedico();
                    agendaCollectionNewAgenda.setMedico(horario);
                    agendaCollectionNewAgenda = em.merge(agendaCollectionNewAgenda);
                    if (oldMedicoOfAgendaCollectionNewAgenda != null && !oldMedicoOfAgendaCollectionNewAgenda.equals(horario)) {
                        oldMedicoOfAgendaCollectionNewAgenda.getAgendaCollection().remove(agendaCollectionNewAgenda);
                        oldMedicoOfAgendaCollectionNewAgenda = em.merge(oldMedicoOfAgendaCollectionNewAgenda);
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
                Integer id = horario.getIdHorario();
                if (findHorario(id) == null) {
                    throw new NonexistentEntityException("The horario with id " + id + " no longer exists.");
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
            Horario horario;
            try {
                horario = em.getReference(Horario.class, id);
                horario.getIdHorario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The horario with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Agenda> agendaCollectionOrphanCheck = horario.getAgendaCollection();
            for (Agenda agendaCollectionOrphanCheckAgenda : agendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Horario (" + horario + ") cannot be destroyed since the Agenda " + agendaCollectionOrphanCheckAgenda + " in its agendaCollection field has a non-nullable medico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medico medico = horario.getMedico();
            if (medico != null) {
                medico.getHorarioCollection().remove(horario);
                medico = em.merge(medico);
            }
            em.remove(horario);
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

    public List<Horario> findHorarioEntities() {
        return findHorarioEntities(true, -1, -1);
    }

    public List<Horario> findHorarioEntities(int maxResults, int firstResult) {
        return findHorarioEntities(false, maxResults, firstResult);
    }

    private List<Horario> findHorarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Horario.class));
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

    public Horario findHorario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Horario.class, id);
        } finally {
            em.close();
        }
    }

    public int getHorarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Horario> rt = cq.from(Horario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
