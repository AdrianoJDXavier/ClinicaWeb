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
import br.com.asfecer.model.Convenio;
import br.com.asfecer.model.Endereco;
import br.com.asfecer.model.Cidade;
import br.com.asfecer.model.Agenda;
import java.util.ArrayList;
import java.util.Collection;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Paciente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class PacienteJpaController implements Serializable {

    public PacienteJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Paciente paciente) throws RollbackFailureException, Exception {
        if (paciente.getAgendaCollection() == null) {
            paciente.setAgendaCollection(new ArrayList<Agenda>());
        }
        if (paciente.getConsultaCollection() == null) {
            paciente.setConsultaCollection(new ArrayList<Consulta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Convenio convenio = paciente.getConvenio();
            if (convenio != null) {
                convenio = em.getReference(convenio.getClass(), convenio.getIdConvenio());
                paciente.setConvenio(convenio);
            }
            Endereco endereco = paciente.getEndereco();
            if (endereco != null) {
                endereco = em.getReference(endereco.getClass(), endereco.getIdEndereco());
                paciente.setEndereco(endereco);
            }
            Cidade naturalidadeCidade = paciente.getNaturalidadeCidade();
            if (naturalidadeCidade != null) {
                naturalidadeCidade = em.getReference(naturalidadeCidade.getClass(), naturalidadeCidade.getIdCidade());
                paciente.setNaturalidadeCidade(naturalidadeCidade);
            }
            Collection<Agenda> attachedAgendaCollection = new ArrayList<Agenda>();
            for (Agenda agendaCollectionAgendaToAttach : paciente.getAgendaCollection()) {
                agendaCollectionAgendaToAttach = em.getReference(agendaCollectionAgendaToAttach.getClass(), agendaCollectionAgendaToAttach.getRegistroAgenda());
                attachedAgendaCollection.add(agendaCollectionAgendaToAttach);
            }
            paciente.setAgendaCollection(attachedAgendaCollection);
            Collection<Consulta> attachedConsultaCollection = new ArrayList<Consulta>();
            for (Consulta consultaCollectionConsultaToAttach : paciente.getConsultaCollection()) {
                consultaCollectionConsultaToAttach = em.getReference(consultaCollectionConsultaToAttach.getClass(), consultaCollectionConsultaToAttach.getIdConsulta());
                attachedConsultaCollection.add(consultaCollectionConsultaToAttach);
            }
            paciente.setConsultaCollection(attachedConsultaCollection);
            em.persist(paciente);
            if (convenio != null) {
                convenio.getPacienteCollection().add(paciente);
                convenio = em.merge(convenio);
            }
            if (endereco != null) {
                endereco.getPacienteCollection().add(paciente);
                endereco = em.merge(endereco);
            }
            if (naturalidadeCidade != null) {
                naturalidadeCidade.getPacienteCollection().add(paciente);
                naturalidadeCidade = em.merge(naturalidadeCidade);
            }
            for (Agenda agendaCollectionAgenda : paciente.getAgendaCollection()) {
                Paciente oldPacienteOfAgendaCollectionAgenda = agendaCollectionAgenda.getPaciente();
                agendaCollectionAgenda.setPaciente(paciente);
                agendaCollectionAgenda = em.merge(agendaCollectionAgenda);
                if (oldPacienteOfAgendaCollectionAgenda != null) {
                    oldPacienteOfAgendaCollectionAgenda.getAgendaCollection().remove(agendaCollectionAgenda);
                    oldPacienteOfAgendaCollectionAgenda = em.merge(oldPacienteOfAgendaCollectionAgenda);
                }
            }
            for (Consulta consultaCollectionConsulta : paciente.getConsultaCollection()) {
                Paciente oldPacienteOfConsultaCollectionConsulta = consultaCollectionConsulta.getPaciente();
                consultaCollectionConsulta.setPaciente(paciente);
                consultaCollectionConsulta = em.merge(consultaCollectionConsulta);
                if (oldPacienteOfConsultaCollectionConsulta != null) {
                    oldPacienteOfConsultaCollectionConsulta.getConsultaCollection().remove(consultaCollectionConsulta);
                    oldPacienteOfConsultaCollectionConsulta = em.merge(oldPacienteOfConsultaCollectionConsulta);
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

    public void edit(Paciente paciente) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Paciente persistentPaciente = em.find(Paciente.class, paciente.getIdPaciente());
            Convenio convenioOld = persistentPaciente.getConvenio();
            Convenio convenioNew = paciente.getConvenio();
            Endereco enderecoOld = persistentPaciente.getEndereco();
            Endereco enderecoNew = paciente.getEndereco();
            Cidade naturalidadeCidadeOld = persistentPaciente.getNaturalidadeCidade();
            Cidade naturalidadeCidadeNew = paciente.getNaturalidadeCidade();
            Collection<Agenda> agendaCollectionOld = persistentPaciente.getAgendaCollection();
            Collection<Agenda> agendaCollectionNew = paciente.getAgendaCollection();
            Collection<Consulta> consultaCollectionOld = persistentPaciente.getConsultaCollection();
            Collection<Consulta> consultaCollectionNew = paciente.getConsultaCollection();
            List<String> illegalOrphanMessages = null;
            for (Agenda agendaCollectionOldAgenda : agendaCollectionOld) {
                if (!agendaCollectionNew.contains(agendaCollectionOldAgenda)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Agenda " + agendaCollectionOldAgenda + " since its paciente field is not nullable.");
                }
            }
            for (Consulta consultaCollectionOldConsulta : consultaCollectionOld) {
                if (!consultaCollectionNew.contains(consultaCollectionOldConsulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Consulta " + consultaCollectionOldConsulta + " since its paciente field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (convenioNew != null) {
                convenioNew = em.getReference(convenioNew.getClass(), convenioNew.getIdConvenio());
                paciente.setConvenio(convenioNew);
            }
            if (enderecoNew != null) {
                enderecoNew = em.getReference(enderecoNew.getClass(), enderecoNew.getIdEndereco());
                paciente.setEndereco(enderecoNew);
            }
            if (naturalidadeCidadeNew != null) {
                naturalidadeCidadeNew = em.getReference(naturalidadeCidadeNew.getClass(), naturalidadeCidadeNew.getIdCidade());
                paciente.setNaturalidadeCidade(naturalidadeCidadeNew);
            }
            Collection<Agenda> attachedAgendaCollectionNew = new ArrayList<Agenda>();
            for (Agenda agendaCollectionNewAgendaToAttach : agendaCollectionNew) {
                agendaCollectionNewAgendaToAttach = em.getReference(agendaCollectionNewAgendaToAttach.getClass(), agendaCollectionNewAgendaToAttach.getRegistroAgenda());
                attachedAgendaCollectionNew.add(agendaCollectionNewAgendaToAttach);
            }
            agendaCollectionNew = attachedAgendaCollectionNew;
            paciente.setAgendaCollection(agendaCollectionNew);
            Collection<Consulta> attachedConsultaCollectionNew = new ArrayList<Consulta>();
            for (Consulta consultaCollectionNewConsultaToAttach : consultaCollectionNew) {
                consultaCollectionNewConsultaToAttach = em.getReference(consultaCollectionNewConsultaToAttach.getClass(), consultaCollectionNewConsultaToAttach.getIdConsulta());
                attachedConsultaCollectionNew.add(consultaCollectionNewConsultaToAttach);
            }
            consultaCollectionNew = attachedConsultaCollectionNew;
            paciente.setConsultaCollection(consultaCollectionNew);
            paciente = em.merge(paciente);
            if (convenioOld != null && !convenioOld.equals(convenioNew)) {
                convenioOld.getPacienteCollection().remove(paciente);
                convenioOld = em.merge(convenioOld);
            }
            if (convenioNew != null && !convenioNew.equals(convenioOld)) {
                convenioNew.getPacienteCollection().add(paciente);
                convenioNew = em.merge(convenioNew);
            }
            if (enderecoOld != null && !enderecoOld.equals(enderecoNew)) {
                enderecoOld.getPacienteCollection().remove(paciente);
                enderecoOld = em.merge(enderecoOld);
            }
            if (enderecoNew != null && !enderecoNew.equals(enderecoOld)) {
                enderecoNew.getPacienteCollection().add(paciente);
                enderecoNew = em.merge(enderecoNew);
            }
            if (naturalidadeCidadeOld != null && !naturalidadeCidadeOld.equals(naturalidadeCidadeNew)) {
                naturalidadeCidadeOld.getPacienteCollection().remove(paciente);
                naturalidadeCidadeOld = em.merge(naturalidadeCidadeOld);
            }
            if (naturalidadeCidadeNew != null && !naturalidadeCidadeNew.equals(naturalidadeCidadeOld)) {
                naturalidadeCidadeNew.getPacienteCollection().add(paciente);
                naturalidadeCidadeNew = em.merge(naturalidadeCidadeNew);
            }
            for (Agenda agendaCollectionNewAgenda : agendaCollectionNew) {
                if (!agendaCollectionOld.contains(agendaCollectionNewAgenda)) {
                    Paciente oldPacienteOfAgendaCollectionNewAgenda = agendaCollectionNewAgenda.getPaciente();
                    agendaCollectionNewAgenda.setPaciente(paciente);
                    agendaCollectionNewAgenda = em.merge(agendaCollectionNewAgenda);
                    if (oldPacienteOfAgendaCollectionNewAgenda != null && !oldPacienteOfAgendaCollectionNewAgenda.equals(paciente)) {
                        oldPacienteOfAgendaCollectionNewAgenda.getAgendaCollection().remove(agendaCollectionNewAgenda);
                        oldPacienteOfAgendaCollectionNewAgenda = em.merge(oldPacienteOfAgendaCollectionNewAgenda);
                    }
                }
            }
            for (Consulta consultaCollectionNewConsulta : consultaCollectionNew) {
                if (!consultaCollectionOld.contains(consultaCollectionNewConsulta)) {
                    Paciente oldPacienteOfConsultaCollectionNewConsulta = consultaCollectionNewConsulta.getPaciente();
                    consultaCollectionNewConsulta.setPaciente(paciente);
                    consultaCollectionNewConsulta = em.merge(consultaCollectionNewConsulta);
                    if (oldPacienteOfConsultaCollectionNewConsulta != null && !oldPacienteOfConsultaCollectionNewConsulta.equals(paciente)) {
                        oldPacienteOfConsultaCollectionNewConsulta.getConsultaCollection().remove(consultaCollectionNewConsulta);
                        oldPacienteOfConsultaCollectionNewConsulta = em.merge(oldPacienteOfConsultaCollectionNewConsulta);
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
                Integer id = paciente.getIdPaciente();
                if (findPaciente(id) == null) {
                    throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.");
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
            Paciente paciente;
            try {
                paciente = em.getReference(Paciente.class, id);
                paciente.getIdPaciente();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The paciente with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Agenda> agendaCollectionOrphanCheck = paciente.getAgendaCollection();
            for (Agenda agendaCollectionOrphanCheckAgenda : agendaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Agenda " + agendaCollectionOrphanCheckAgenda + " in its agendaCollection field has a non-nullable paciente field.");
            }
            Collection<Consulta> consultaCollectionOrphanCheck = paciente.getConsultaCollection();
            for (Consulta consultaCollectionOrphanCheckConsulta : consultaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Paciente (" + paciente + ") cannot be destroyed since the Consulta " + consultaCollectionOrphanCheckConsulta + " in its consultaCollection field has a non-nullable paciente field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Convenio convenio = paciente.getConvenio();
            if (convenio != null) {
                convenio.getPacienteCollection().remove(paciente);
                convenio = em.merge(convenio);
            }
            Endereco endereco = paciente.getEndereco();
            if (endereco != null) {
                endereco.getPacienteCollection().remove(paciente);
                endereco = em.merge(endereco);
            }
            Cidade naturalidadeCidade = paciente.getNaturalidadeCidade();
            if (naturalidadeCidade != null) {
                naturalidadeCidade.getPacienteCollection().remove(paciente);
                naturalidadeCidade = em.merge(naturalidadeCidade);
            }
            em.remove(paciente);
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

    public List<Paciente> findPacienteEntities() {
        return findPacienteEntities(true, -1, -1);
    }

    public List<Paciente> findPacienteEntities(int maxResults, int firstResult) {
        return findPacienteEntities(false, maxResults, firstResult);
    }

    private List<Paciente> findPacienteEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Paciente.class));
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

    public Paciente findPaciente(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Paciente.class, id);
        } finally {
            em.close();
        }
    }

    public int getPacienteCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Paciente> rt = cq.from(Paciente.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
