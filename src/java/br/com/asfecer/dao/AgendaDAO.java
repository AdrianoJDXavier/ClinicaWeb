/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Agenda;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Horario;
import br.com.asfecer.model.Paciente;
import br.com.asfecer.model.Usuario;
import br.com.asfecer.model.Consulta;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Adriano Xavier
 */
public class AgendaDAO implements Serializable {

    public AgendaDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Agenda agenda) throws RollbackFailureException, Exception {
        if (agenda.getConsultaCollection() == null) {
            agenda.setConsultaCollection(new ArrayList<Consulta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Horario medico = agenda.getMedico();
            if (medico != null) {
                medico = em.getReference(medico.getClass(), medico.getIdhorario());
                agenda.setMedico(medico);
            }
            Paciente paciente = agenda.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getIdpaciente());
                agenda.setPaciente(paciente);
            }
            Usuario usuario = agenda.getUsuario();
            if (usuario != null) {
                usuario = em.getReference(usuario.getClass(), usuario.getIdusuario());
                agenda.setUsuario(usuario);
            }
            Collection<Consulta> attachedConsultaCollection = new ArrayList<Consulta>();
            for (Consulta consultaCollectionConsultaToAttach : agenda.getConsultaCollection()) {
                consultaCollectionConsultaToAttach = em.getReference(consultaCollectionConsultaToAttach.getClass(), consultaCollectionConsultaToAttach.getIdconsulta());
                attachedConsultaCollection.add(consultaCollectionConsultaToAttach);
            }
            agenda.setConsultaCollection(attachedConsultaCollection);
            em.persist(agenda);
            if (medico != null) {
                medico.getAgendaCollection().add(agenda);
                medico = em.merge(medico);
            }
            if (paciente != null) {
                paciente.getAgendaCollection().add(agenda);
                paciente = em.merge(paciente);
            }
            if (usuario != null) {
                usuario.getAgendaCollection().add(agenda);
                usuario = em.merge(usuario);
            }
            for (Consulta consultaCollectionConsulta : agenda.getConsultaCollection()) {
                Agenda oldAgendaOfConsultaCollectionConsulta = consultaCollectionConsulta.getAgenda();
                consultaCollectionConsulta.setAgenda(agenda);
                consultaCollectionConsulta = em.merge(consultaCollectionConsulta);
                if (oldAgendaOfConsultaCollectionConsulta != null) {
                    oldAgendaOfConsultaCollectionConsulta.getConsultaCollection().remove(consultaCollectionConsulta);
                    oldAgendaOfConsultaCollectionConsulta = em.merge(oldAgendaOfConsultaCollectionConsulta);
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

    public void edit(Agenda agenda) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Agenda persistentAgenda = em.find(Agenda.class, agenda.getRegistroagenda());
            Horario medicoOld = persistentAgenda.getMedico();
            Horario medicoNew = agenda.getMedico();
            Paciente pacienteOld = persistentAgenda.getPaciente();
            Paciente pacienteNew = agenda.getPaciente();
            Usuario usuarioOld = persistentAgenda.getUsuario();
            Usuario usuarioNew = agenda.getUsuario();
            Collection<Consulta> consultaCollectionOld = persistentAgenda.getConsultaCollection();
            Collection<Consulta> consultaCollectionNew = agenda.getConsultaCollection();
            if (medicoNew != null) {
                medicoNew = em.getReference(medicoNew.getClass(), medicoNew.getIdhorario());
                agenda.setMedico(medicoNew);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getIdpaciente());
                agenda.setPaciente(pacienteNew);
            }
            if (usuarioNew != null) {
                usuarioNew = em.getReference(usuarioNew.getClass(), usuarioNew.getIdusuario());
                agenda.setUsuario(usuarioNew);
            }
            Collection<Consulta> attachedConsultaCollectionNew = new ArrayList<Consulta>();
            for (Consulta consultaCollectionNewConsultaToAttach : consultaCollectionNew) {
                consultaCollectionNewConsultaToAttach = em.getReference(consultaCollectionNewConsultaToAttach.getClass(), consultaCollectionNewConsultaToAttach.getIdconsulta());
                attachedConsultaCollectionNew.add(consultaCollectionNewConsultaToAttach);
            }
            consultaCollectionNew = attachedConsultaCollectionNew;
            agenda.setConsultaCollection(consultaCollectionNew);
            agenda = em.merge(agenda);
            if (medicoOld != null && !medicoOld.equals(medicoNew)) {
                medicoOld.getAgendaCollection().remove(agenda);
                medicoOld = em.merge(medicoOld);
            }
            if (medicoNew != null && !medicoNew.equals(medicoOld)) {
                medicoNew.getAgendaCollection().add(agenda);
                medicoNew = em.merge(medicoNew);
            }
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.getAgendaCollection().remove(agenda);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.getAgendaCollection().add(agenda);
                pacienteNew = em.merge(pacienteNew);
            }
            if (usuarioOld != null && !usuarioOld.equals(usuarioNew)) {
                usuarioOld.getAgendaCollection().remove(agenda);
                usuarioOld = em.merge(usuarioOld);
            }
            if (usuarioNew != null && !usuarioNew.equals(usuarioOld)) {
                usuarioNew.getAgendaCollection().add(agenda);
                usuarioNew = em.merge(usuarioNew);
            }
            for (Consulta consultaCollectionOldConsulta : consultaCollectionOld) {
                if (!consultaCollectionNew.contains(consultaCollectionOldConsulta)) {
                    consultaCollectionOldConsulta.setAgenda(null);
                    consultaCollectionOldConsulta = em.merge(consultaCollectionOldConsulta);
                }
            }
            for (Consulta consultaCollectionNewConsulta : consultaCollectionNew) {
                if (!consultaCollectionOld.contains(consultaCollectionNewConsulta)) {
                    Agenda oldAgendaOfConsultaCollectionNewConsulta = consultaCollectionNewConsulta.getAgenda();
                    consultaCollectionNewConsulta.setAgenda(agenda);
                    consultaCollectionNewConsulta = em.merge(consultaCollectionNewConsulta);
                    if (oldAgendaOfConsultaCollectionNewConsulta != null && !oldAgendaOfConsultaCollectionNewConsulta.equals(agenda)) {
                        oldAgendaOfConsultaCollectionNewConsulta.getConsultaCollection().remove(consultaCollectionNewConsulta);
                        oldAgendaOfConsultaCollectionNewConsulta = em.merge(oldAgendaOfConsultaCollectionNewConsulta);
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
                Integer id = agenda.getRegistroagenda();
                if (findAgenda(id) == null) {
                    throw new NonexistentEntityException("The agenda with id " + id + " no longer exists.");
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
            Agenda agenda;
            try {
                agenda = em.getReference(Agenda.class, id);
                agenda.getRegistroagenda();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The agenda with id " + id + " no longer exists.", enfe);
            }
            Horario medico = agenda.getMedico();
            if (medico != null) {
                medico.getAgendaCollection().remove(agenda);
                medico = em.merge(medico);
            }
            Paciente paciente = agenda.getPaciente();
            if (paciente != null) {
                paciente.getAgendaCollection().remove(agenda);
                paciente = em.merge(paciente);
            }
            Usuario usuario = agenda.getUsuario();
            if (usuario != null) {
                usuario.getAgendaCollection().remove(agenda);
                usuario = em.merge(usuario);
            }
            Collection<Consulta> consultaCollection = agenda.getConsultaCollection();
            for (Consulta consultaCollectionConsulta : consultaCollection) {
                consultaCollectionConsulta.setAgenda(null);
                consultaCollectionConsulta = em.merge(consultaCollectionConsulta);
            }
            em.remove(agenda);
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

    public List<Agenda> findAgendaEntities() {
        return findAgendaEntities(true, -1, -1);
    }

    public List<Agenda> findAgendaEntities(int maxResults, int firstResult) {
        return findAgendaEntities(false, maxResults, firstResult);
    }

    private List<Agenda> findAgendaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Agenda.class));
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

    public Agenda findAgenda(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Agenda.class, id);
        } finally {
            em.close();
        }
    }

    public int getAgendaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Agenda> rt = cq.from(Agenda.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
