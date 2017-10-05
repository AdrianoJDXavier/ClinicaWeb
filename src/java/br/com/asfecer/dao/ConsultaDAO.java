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
import br.com.asfecer.model.Medico;
import br.com.asfecer.model.Paciente;
import br.com.asfecer.model.Prontuario;
import java.util.ArrayList;
import java.util.Collection;
import br.com.asfecer.model.Atestado;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Receituario;
import br.com.asfecer.model.Pedidoexame;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class ConsultaDAO implements Serializable {

    public ConsultaDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Consulta consulta) throws RollbackFailureException, Exception {
        if (consulta.getProntuarioCollection() == null) {
            consulta.setProntuarioCollection(new ArrayList<Prontuario>());
        }
        if (consulta.getAtestadoCollection() == null) {
            consulta.setAtestadoCollection(new ArrayList<Atestado>());
        }
        if (consulta.getReceituarioCollection() == null) {
            consulta.setReceituarioCollection(new ArrayList<Receituario>());
        }
        if (consulta.getPedidoexameCollection() == null) {
            consulta.setPedidoexameCollection(new ArrayList<Pedidoexame>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Agenda agenda = consulta.getAgenda();
            if (agenda != null) {
                agenda = em.getReference(agenda.getClass(), agenda.getRegistroAgenda());
                consulta.setAgenda(agenda);
            }
            Medico medico = consulta.getMedico();
            if (medico != null) {
                medico = em.getReference(medico.getClass(), medico.getIdMedico());
                consulta.setMedico(medico);
            }
            Paciente paciente = consulta.getPaciente();
            if (paciente != null) {
                paciente = em.getReference(paciente.getClass(), paciente.getIdPaciente());
                consulta.setPaciente(paciente);
            }
            Collection<Prontuario> attachedProntuarioCollection = new ArrayList<Prontuario>();
            for (Prontuario prontuarioCollectionProntuarioToAttach : consulta.getProntuarioCollection()) {
                prontuarioCollectionProntuarioToAttach = em.getReference(prontuarioCollectionProntuarioToAttach.getClass(), prontuarioCollectionProntuarioToAttach.getIdProntuario());
                attachedProntuarioCollection.add(prontuarioCollectionProntuarioToAttach);
            }
            consulta.setProntuarioCollection(attachedProntuarioCollection);
            Collection<Atestado> attachedAtestadoCollection = new ArrayList<Atestado>();
            for (Atestado atestadoCollectionAtestadoToAttach : consulta.getAtestadoCollection()) {
                atestadoCollectionAtestadoToAttach = em.getReference(atestadoCollectionAtestadoToAttach.getClass(), atestadoCollectionAtestadoToAttach.getIdAtestado());
                attachedAtestadoCollection.add(atestadoCollectionAtestadoToAttach);
            }
            consulta.setAtestadoCollection(attachedAtestadoCollection);
            Collection<Receituario> attachedReceituarioCollection = new ArrayList<Receituario>();
            for (Receituario receituarioCollectionReceituarioToAttach : consulta.getReceituarioCollection()) {
                receituarioCollectionReceituarioToAttach = em.getReference(receituarioCollectionReceituarioToAttach.getClass(), receituarioCollectionReceituarioToAttach.getIdReceituario());
                attachedReceituarioCollection.add(receituarioCollectionReceituarioToAttach);
            }
            consulta.setReceituarioCollection(attachedReceituarioCollection);
            Collection<Pedidoexame> attachedPedidoexameCollection = new ArrayList<Pedidoexame>();
            for (Pedidoexame pedidoexameCollectionPedidoexameToAttach : consulta.getPedidoexameCollection()) {
                pedidoexameCollectionPedidoexameToAttach = em.getReference(pedidoexameCollectionPedidoexameToAttach.getClass(), pedidoexameCollectionPedidoexameToAttach.getIdPedidoExame());
                attachedPedidoexameCollection.add(pedidoexameCollectionPedidoexameToAttach);
            }
            consulta.setPedidoexameCollection(attachedPedidoexameCollection);
            em.persist(consulta);
            if (agenda != null) {
                agenda.getConsultaCollection().add(consulta);
                agenda = em.merge(agenda);
            }
            if (medico != null) {
                medico.getConsultaCollection().add(consulta);
                medico = em.merge(medico);
            }
            if (paciente != null) {
                paciente.getConsultaCollection().add(consulta);
                paciente = em.merge(paciente);
            }
            for (Prontuario prontuarioCollectionProntuario : consulta.getProntuarioCollection()) {
                Consulta oldConsultaOfProntuarioCollectionProntuario = prontuarioCollectionProntuario.getConsulta();
                prontuarioCollectionProntuario.setConsulta(consulta);
                prontuarioCollectionProntuario = em.merge(prontuarioCollectionProntuario);
                if (oldConsultaOfProntuarioCollectionProntuario != null) {
                    oldConsultaOfProntuarioCollectionProntuario.getProntuarioCollection().remove(prontuarioCollectionProntuario);
                    oldConsultaOfProntuarioCollectionProntuario = em.merge(oldConsultaOfProntuarioCollectionProntuario);
                }
            }
            for (Atestado atestadoCollectionAtestado : consulta.getAtestadoCollection()) {
                Consulta oldConsultaOfAtestadoCollectionAtestado = atestadoCollectionAtestado.getConsulta();
                atestadoCollectionAtestado.setConsulta(consulta);
                atestadoCollectionAtestado = em.merge(atestadoCollectionAtestado);
                if (oldConsultaOfAtestadoCollectionAtestado != null) {
                    oldConsultaOfAtestadoCollectionAtestado.getAtestadoCollection().remove(atestadoCollectionAtestado);
                    oldConsultaOfAtestadoCollectionAtestado = em.merge(oldConsultaOfAtestadoCollectionAtestado);
                }
            }
            for (Receituario receituarioCollectionReceituario : consulta.getReceituarioCollection()) {
                Consulta oldConsultaOfReceituarioCollectionReceituario = receituarioCollectionReceituario.getConsulta();
                receituarioCollectionReceituario.setConsulta(consulta);
                receituarioCollectionReceituario = em.merge(receituarioCollectionReceituario);
                if (oldConsultaOfReceituarioCollectionReceituario != null) {
                    oldConsultaOfReceituarioCollectionReceituario.getReceituarioCollection().remove(receituarioCollectionReceituario);
                    oldConsultaOfReceituarioCollectionReceituario = em.merge(oldConsultaOfReceituarioCollectionReceituario);
                }
            }
            for (Pedidoexame pedidoexameCollectionPedidoexame : consulta.getPedidoexameCollection()) {
                Consulta oldConsultaOfPedidoexameCollectionPedidoexame = pedidoexameCollectionPedidoexame.getConsulta();
                pedidoexameCollectionPedidoexame.setConsulta(consulta);
                pedidoexameCollectionPedidoexame = em.merge(pedidoexameCollectionPedidoexame);
                if (oldConsultaOfPedidoexameCollectionPedidoexame != null) {
                    oldConsultaOfPedidoexameCollectionPedidoexame.getPedidoexameCollection().remove(pedidoexameCollectionPedidoexame);
                    oldConsultaOfPedidoexameCollectionPedidoexame = em.merge(oldConsultaOfPedidoexameCollectionPedidoexame);
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

    public void edit(Consulta consulta) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Consulta persistentConsulta = em.find(Consulta.class, consulta.getIdConsulta());
            Agenda agendaOld = persistentConsulta.getAgenda();
            Agenda agendaNew = consulta.getAgenda();
            Medico medicoOld = persistentConsulta.getMedico();
            Medico medicoNew = consulta.getMedico();
            Paciente pacienteOld = persistentConsulta.getPaciente();
            Paciente pacienteNew = consulta.getPaciente();
            Collection<Prontuario> prontuarioCollectionOld = persistentConsulta.getProntuarioCollection();
            Collection<Prontuario> prontuarioCollectionNew = consulta.getProntuarioCollection();
            Collection<Atestado> atestadoCollectionOld = persistentConsulta.getAtestadoCollection();
            Collection<Atestado> atestadoCollectionNew = consulta.getAtestadoCollection();
            Collection<Receituario> receituarioCollectionOld = persistentConsulta.getReceituarioCollection();
            Collection<Receituario> receituarioCollectionNew = consulta.getReceituarioCollection();
            Collection<Pedidoexame> pedidoexameCollectionOld = persistentConsulta.getPedidoexameCollection();
            Collection<Pedidoexame> pedidoexameCollectionNew = consulta.getPedidoexameCollection();
            List<String> illegalOrphanMessages = null;
            for (Prontuario prontuarioCollectionOldProntuario : prontuarioCollectionOld) {
                if (!prontuarioCollectionNew.contains(prontuarioCollectionOldProntuario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Prontuario " + prontuarioCollectionOldProntuario + " since its consulta field is not nullable.");
                }
            }
            for (Atestado atestadoCollectionOldAtestado : atestadoCollectionOld) {
                if (!atestadoCollectionNew.contains(atestadoCollectionOldAtestado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Atestado " + atestadoCollectionOldAtestado + " since its consulta field is not nullable.");
                }
            }
            for (Receituario receituarioCollectionOldReceituario : receituarioCollectionOld) {
                if (!receituarioCollectionNew.contains(receituarioCollectionOldReceituario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Receituario " + receituarioCollectionOldReceituario + " since its consulta field is not nullable.");
                }
            }
            for (Pedidoexame pedidoexameCollectionOldPedidoexame : pedidoexameCollectionOld) {
                if (!pedidoexameCollectionNew.contains(pedidoexameCollectionOldPedidoexame)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Pedidoexame " + pedidoexameCollectionOldPedidoexame + " since its consulta field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (agendaNew != null) {
                agendaNew = em.getReference(agendaNew.getClass(), agendaNew.getRegistroAgenda());
                consulta.setAgenda(agendaNew);
            }
            if (medicoNew != null) {
                medicoNew = em.getReference(medicoNew.getClass(), medicoNew.getIdMedico());
                consulta.setMedico(medicoNew);
            }
            if (pacienteNew != null) {
                pacienteNew = em.getReference(pacienteNew.getClass(), pacienteNew.getIdPaciente());
                consulta.setPaciente(pacienteNew);
            }
            Collection<Prontuario> attachedProntuarioCollectionNew = new ArrayList<Prontuario>();
            for (Prontuario prontuarioCollectionNewProntuarioToAttach : prontuarioCollectionNew) {
                prontuarioCollectionNewProntuarioToAttach = em.getReference(prontuarioCollectionNewProntuarioToAttach.getClass(), prontuarioCollectionNewProntuarioToAttach.getIdProntuario());
                attachedProntuarioCollectionNew.add(prontuarioCollectionNewProntuarioToAttach);
            }
            prontuarioCollectionNew = attachedProntuarioCollectionNew;
            consulta.setProntuarioCollection(prontuarioCollectionNew);
            Collection<Atestado> attachedAtestadoCollectionNew = new ArrayList<Atestado>();
            for (Atestado atestadoCollectionNewAtestadoToAttach : atestadoCollectionNew) {
                atestadoCollectionNewAtestadoToAttach = em.getReference(atestadoCollectionNewAtestadoToAttach.getClass(), atestadoCollectionNewAtestadoToAttach.getIdAtestado());
                attachedAtestadoCollectionNew.add(atestadoCollectionNewAtestadoToAttach);
            }
            atestadoCollectionNew = attachedAtestadoCollectionNew;
            consulta.setAtestadoCollection(atestadoCollectionNew);
            Collection<Receituario> attachedReceituarioCollectionNew = new ArrayList<Receituario>();
            for (Receituario receituarioCollectionNewReceituarioToAttach : receituarioCollectionNew) {
                receituarioCollectionNewReceituarioToAttach = em.getReference(receituarioCollectionNewReceituarioToAttach.getClass(), receituarioCollectionNewReceituarioToAttach.getIdReceituario());
                attachedReceituarioCollectionNew.add(receituarioCollectionNewReceituarioToAttach);
            }
            receituarioCollectionNew = attachedReceituarioCollectionNew;
            consulta.setReceituarioCollection(receituarioCollectionNew);
            Collection<Pedidoexame> attachedPedidoexameCollectionNew = new ArrayList<Pedidoexame>();
            for (Pedidoexame pedidoexameCollectionNewPedidoexameToAttach : pedidoexameCollectionNew) {
                pedidoexameCollectionNewPedidoexameToAttach = em.getReference(pedidoexameCollectionNewPedidoexameToAttach.getClass(), pedidoexameCollectionNewPedidoexameToAttach.getIdPedidoExame());
                attachedPedidoexameCollectionNew.add(pedidoexameCollectionNewPedidoexameToAttach);
            }
            pedidoexameCollectionNew = attachedPedidoexameCollectionNew;
            consulta.setPedidoexameCollection(pedidoexameCollectionNew);
            consulta = em.merge(consulta);
            if (agendaOld != null && !agendaOld.equals(agendaNew)) {
                agendaOld.getConsultaCollection().remove(consulta);
                agendaOld = em.merge(agendaOld);
            }
            if (agendaNew != null && !agendaNew.equals(agendaOld)) {
                agendaNew.getConsultaCollection().add(consulta);
                agendaNew = em.merge(agendaNew);
            }
            if (medicoOld != null && !medicoOld.equals(medicoNew)) {
                medicoOld.getConsultaCollection().remove(consulta);
                medicoOld = em.merge(medicoOld);
            }
            if (medicoNew != null && !medicoNew.equals(medicoOld)) {
                medicoNew.getConsultaCollection().add(consulta);
                medicoNew = em.merge(medicoNew);
            }
            if (pacienteOld != null && !pacienteOld.equals(pacienteNew)) {
                pacienteOld.getConsultaCollection().remove(consulta);
                pacienteOld = em.merge(pacienteOld);
            }
            if (pacienteNew != null && !pacienteNew.equals(pacienteOld)) {
                pacienteNew.getConsultaCollection().add(consulta);
                pacienteNew = em.merge(pacienteNew);
            }
            for (Prontuario prontuarioCollectionNewProntuario : prontuarioCollectionNew) {
                if (!prontuarioCollectionOld.contains(prontuarioCollectionNewProntuario)) {
                    Consulta oldConsultaOfProntuarioCollectionNewProntuario = prontuarioCollectionNewProntuario.getConsulta();
                    prontuarioCollectionNewProntuario.setConsulta(consulta);
                    prontuarioCollectionNewProntuario = em.merge(prontuarioCollectionNewProntuario);
                    if (oldConsultaOfProntuarioCollectionNewProntuario != null && !oldConsultaOfProntuarioCollectionNewProntuario.equals(consulta)) {
                        oldConsultaOfProntuarioCollectionNewProntuario.getProntuarioCollection().remove(prontuarioCollectionNewProntuario);
                        oldConsultaOfProntuarioCollectionNewProntuario = em.merge(oldConsultaOfProntuarioCollectionNewProntuario);
                    }
                }
            }
            for (Atestado atestadoCollectionNewAtestado : atestadoCollectionNew) {
                if (!atestadoCollectionOld.contains(atestadoCollectionNewAtestado)) {
                    Consulta oldConsultaOfAtestadoCollectionNewAtestado = atestadoCollectionNewAtestado.getConsulta();
                    atestadoCollectionNewAtestado.setConsulta(consulta);
                    atestadoCollectionNewAtestado = em.merge(atestadoCollectionNewAtestado);
                    if (oldConsultaOfAtestadoCollectionNewAtestado != null && !oldConsultaOfAtestadoCollectionNewAtestado.equals(consulta)) {
                        oldConsultaOfAtestadoCollectionNewAtestado.getAtestadoCollection().remove(atestadoCollectionNewAtestado);
                        oldConsultaOfAtestadoCollectionNewAtestado = em.merge(oldConsultaOfAtestadoCollectionNewAtestado);
                    }
                }
            }
            for (Receituario receituarioCollectionNewReceituario : receituarioCollectionNew) {
                if (!receituarioCollectionOld.contains(receituarioCollectionNewReceituario)) {
                    Consulta oldConsultaOfReceituarioCollectionNewReceituario = receituarioCollectionNewReceituario.getConsulta();
                    receituarioCollectionNewReceituario.setConsulta(consulta);
                    receituarioCollectionNewReceituario = em.merge(receituarioCollectionNewReceituario);
                    if (oldConsultaOfReceituarioCollectionNewReceituario != null && !oldConsultaOfReceituarioCollectionNewReceituario.equals(consulta)) {
                        oldConsultaOfReceituarioCollectionNewReceituario.getReceituarioCollection().remove(receituarioCollectionNewReceituario);
                        oldConsultaOfReceituarioCollectionNewReceituario = em.merge(oldConsultaOfReceituarioCollectionNewReceituario);
                    }
                }
            }
            for (Pedidoexame pedidoexameCollectionNewPedidoexame : pedidoexameCollectionNew) {
                if (!pedidoexameCollectionOld.contains(pedidoexameCollectionNewPedidoexame)) {
                    Consulta oldConsultaOfPedidoexameCollectionNewPedidoexame = pedidoexameCollectionNewPedidoexame.getConsulta();
                    pedidoexameCollectionNewPedidoexame.setConsulta(consulta);
                    pedidoexameCollectionNewPedidoexame = em.merge(pedidoexameCollectionNewPedidoexame);
                    if (oldConsultaOfPedidoexameCollectionNewPedidoexame != null && !oldConsultaOfPedidoexameCollectionNewPedidoexame.equals(consulta)) {
                        oldConsultaOfPedidoexameCollectionNewPedidoexame.getPedidoexameCollection().remove(pedidoexameCollectionNewPedidoexame);
                        oldConsultaOfPedidoexameCollectionNewPedidoexame = em.merge(oldConsultaOfPedidoexameCollectionNewPedidoexame);
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
                Integer id = consulta.getIdConsulta();
                if (findConsulta(id) == null) {
                    throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.");
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
            Consulta consulta;
            try {
                consulta = em.getReference(Consulta.class, id);
                consulta.getIdConsulta();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The consulta with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Prontuario> prontuarioCollectionOrphanCheck = consulta.getProntuarioCollection();
            for (Prontuario prontuarioCollectionOrphanCheckProntuario : prontuarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Consulta (" + consulta + ") cannot be destroyed since the Prontuario " + prontuarioCollectionOrphanCheckProntuario + " in its prontuarioCollection field has a non-nullable consulta field.");
            }
            Collection<Atestado> atestadoCollectionOrphanCheck = consulta.getAtestadoCollection();
            for (Atestado atestadoCollectionOrphanCheckAtestado : atestadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Consulta (" + consulta + ") cannot be destroyed since the Atestado " + atestadoCollectionOrphanCheckAtestado + " in its atestadoCollection field has a non-nullable consulta field.");
            }
            Collection<Receituario> receituarioCollectionOrphanCheck = consulta.getReceituarioCollection();
            for (Receituario receituarioCollectionOrphanCheckReceituario : receituarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Consulta (" + consulta + ") cannot be destroyed since the Receituario " + receituarioCollectionOrphanCheckReceituario + " in its receituarioCollection field has a non-nullable consulta field.");
            }
            Collection<Pedidoexame> pedidoexameCollectionOrphanCheck = consulta.getPedidoexameCollection();
            for (Pedidoexame pedidoexameCollectionOrphanCheckPedidoexame : pedidoexameCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Consulta (" + consulta + ") cannot be destroyed since the Pedidoexame " + pedidoexameCollectionOrphanCheckPedidoexame + " in its pedidoexameCollection field has a non-nullable consulta field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Agenda agenda = consulta.getAgenda();
            if (agenda != null) {
                agenda.getConsultaCollection().remove(consulta);
                agenda = em.merge(agenda);
            }
            Medico medico = consulta.getMedico();
            if (medico != null) {
                medico.getConsultaCollection().remove(consulta);
                medico = em.merge(medico);
            }
            Paciente paciente = consulta.getPaciente();
            if (paciente != null) {
                paciente.getConsultaCollection().remove(consulta);
                paciente = em.merge(paciente);
            }
            em.remove(consulta);
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

    public List<Consulta> findConsultaEntities() {
        return findConsultaEntities(true, -1, -1);
    }

    public List<Consulta> findConsultaEntities(int maxResults, int firstResult) {
        return findConsultaEntities(false, maxResults, firstResult);
    }

    private List<Consulta> findConsultaEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Consulta.class));
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

    public Consulta findConsulta(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Consulta.class, id);
        } finally {
            em.close();
        }
    }

    public int getConsultaCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Consulta> rt = cq.from(Consulta.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
