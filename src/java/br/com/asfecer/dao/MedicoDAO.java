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
import br.com.asfecer.model.Especialidade;
import br.com.asfecer.model.Estados;
import br.com.asfecer.model.Horario;
import java.util.ArrayList;
import java.util.Collection;
import br.com.asfecer.model.Consulta;
import br.com.asfecer.model.Medico;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author Adriano Xavier
 */
public class MedicoDAO implements Serializable {

    public MedicoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medico medico) throws RollbackFailureException, Exception {
        if (medico.getHorarioCollection() == null) {
            medico.setHorarioCollection(new ArrayList<Horario>());
        }
        if (medico.getConsultaCollection() == null) {
            medico.setConsultaCollection(new ArrayList<Consulta>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Especialidade especialidade = medico.getEspecialidade();
            if (especialidade != null) {
                especialidade = em.getReference(especialidade.getClass(), especialidade.getIdespecialidade());
                medico.setEspecialidade(especialidade);
            }
            Estados ufCrm = medico.getUfCrm();
            if (ufCrm != null) {
                ufCrm = em.getReference(ufCrm.getClass(), ufCrm.getSigla());
                medico.setUfCrm(ufCrm);
            }
            Collection<Horario> attachedHorarioCollection = new ArrayList<Horario>();
            for (Horario horarioCollectionHorarioToAttach : medico.getHorarioCollection()) {
                horarioCollectionHorarioToAttach = em.getReference(horarioCollectionHorarioToAttach.getClass(), horarioCollectionHorarioToAttach.getIdhorario());
                attachedHorarioCollection.add(horarioCollectionHorarioToAttach);
            }
            medico.setHorarioCollection(attachedHorarioCollection);
            Collection<Consulta> attachedConsultaCollection = new ArrayList<Consulta>();
            for (Consulta consultaCollectionConsultaToAttach : medico.getConsultaCollection()) {
                consultaCollectionConsultaToAttach = em.getReference(consultaCollectionConsultaToAttach.getClass(), consultaCollectionConsultaToAttach.getIdconsulta());
                attachedConsultaCollection.add(consultaCollectionConsultaToAttach);
            }
            medico.setConsultaCollection(attachedConsultaCollection);
            em.persist(medico);
            if (especialidade != null) {
                especialidade.getMedicoCollection().add(medico);
                especialidade = em.merge(especialidade);
            }
            if (ufCrm != null) {
                ufCrm.getMedicoCollection().add(medico);
                ufCrm = em.merge(ufCrm);
            }
            for (Horario horarioCollectionHorario : medico.getHorarioCollection()) {
                Medico oldMedicoOfHorarioCollectionHorario = horarioCollectionHorario.getMedico();
                horarioCollectionHorario.setMedico(medico);
                horarioCollectionHorario = em.merge(horarioCollectionHorario);
                if (oldMedicoOfHorarioCollectionHorario != null) {
                    oldMedicoOfHorarioCollectionHorario.getHorarioCollection().remove(horarioCollectionHorario);
                    oldMedicoOfHorarioCollectionHorario = em.merge(oldMedicoOfHorarioCollectionHorario);
                }
            }
            for (Consulta consultaCollectionConsulta : medico.getConsultaCollection()) {
                Medico oldMedicoOfConsultaCollectionConsulta = consultaCollectionConsulta.getMedico();
                consultaCollectionConsulta.setMedico(medico);
                consultaCollectionConsulta = em.merge(consultaCollectionConsulta);
                if (oldMedicoOfConsultaCollectionConsulta != null) {
                    oldMedicoOfConsultaCollectionConsulta.getConsultaCollection().remove(consultaCollectionConsulta);
                    oldMedicoOfConsultaCollectionConsulta = em.merge(oldMedicoOfConsultaCollectionConsulta);
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

    public void edit(Medico medico) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medico persistentMedico = em.find(Medico.class, medico.getIdmedico());
            Especialidade especialidadeOld = persistentMedico.getEspecialidade();
            Especialidade especialidadeNew = medico.getEspecialidade();
            Estados ufCrmOld = persistentMedico.getUfCrm();
            Estados ufCrmNew = medico.getUfCrm();
            Collection<Horario> horarioCollectionOld = persistentMedico.getHorarioCollection();
            Collection<Horario> horarioCollectionNew = medico.getHorarioCollection();
            Collection<Consulta> consultaCollectionOld = persistentMedico.getConsultaCollection();
            Collection<Consulta> consultaCollectionNew = medico.getConsultaCollection();
            List<String> illegalOrphanMessages = null;
            for (Consulta consultaCollectionOldConsulta : consultaCollectionOld) {
                if (!consultaCollectionNew.contains(consultaCollectionOldConsulta)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Consulta " + consultaCollectionOldConsulta + " since its medico field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (especialidadeNew != null) {
                especialidadeNew = em.getReference(especialidadeNew.getClass(), especialidadeNew.getIdespecialidade());
                medico.setEspecialidade(especialidadeNew);
            }
            if (ufCrmNew != null) {
                ufCrmNew = em.getReference(ufCrmNew.getClass(), ufCrmNew.getSigla());
                medico.setUfCrm(ufCrmNew);
            }
            Collection<Horario> attachedHorarioCollectionNew = new ArrayList<Horario>();
            for (Horario horarioCollectionNewHorarioToAttach : horarioCollectionNew) {
                horarioCollectionNewHorarioToAttach = em.getReference(horarioCollectionNewHorarioToAttach.getClass(), horarioCollectionNewHorarioToAttach.getIdhorario());
                attachedHorarioCollectionNew.add(horarioCollectionNewHorarioToAttach);
            }
            horarioCollectionNew = attachedHorarioCollectionNew;
            medico.setHorarioCollection(horarioCollectionNew);
            Collection<Consulta> attachedConsultaCollectionNew = new ArrayList<Consulta>();
            for (Consulta consultaCollectionNewConsultaToAttach : consultaCollectionNew) {
                consultaCollectionNewConsultaToAttach = em.getReference(consultaCollectionNewConsultaToAttach.getClass(), consultaCollectionNewConsultaToAttach.getIdconsulta());
                attachedConsultaCollectionNew.add(consultaCollectionNewConsultaToAttach);
            }
            consultaCollectionNew = attachedConsultaCollectionNew;
            medico.setConsultaCollection(consultaCollectionNew);
            medico = em.merge(medico);
            if (especialidadeOld != null && !especialidadeOld.equals(especialidadeNew)) {
                especialidadeOld.getMedicoCollection().remove(medico);
                especialidadeOld = em.merge(especialidadeOld);
            }
            if (especialidadeNew != null && !especialidadeNew.equals(especialidadeOld)) {
                especialidadeNew.getMedicoCollection().add(medico);
                especialidadeNew = em.merge(especialidadeNew);
            }
            if (ufCrmOld != null && !ufCrmOld.equals(ufCrmNew)) {
                ufCrmOld.getMedicoCollection().remove(medico);
                ufCrmOld = em.merge(ufCrmOld);
            }
            if (ufCrmNew != null && !ufCrmNew.equals(ufCrmOld)) {
                ufCrmNew.getMedicoCollection().add(medico);
                ufCrmNew = em.merge(ufCrmNew);
            }
            for (Horario horarioCollectionOldHorario : horarioCollectionOld) {
                if (!horarioCollectionNew.contains(horarioCollectionOldHorario)) {
                    horarioCollectionOldHorario.setMedico(null);
                    horarioCollectionOldHorario = em.merge(horarioCollectionOldHorario);
                }
            }
            for (Horario horarioCollectionNewHorario : horarioCollectionNew) {
                if (!horarioCollectionOld.contains(horarioCollectionNewHorario)) {
                    Medico oldMedicoOfHorarioCollectionNewHorario = horarioCollectionNewHorario.getMedico();
                    horarioCollectionNewHorario.setMedico(medico);
                    horarioCollectionNewHorario = em.merge(horarioCollectionNewHorario);
                    if (oldMedicoOfHorarioCollectionNewHorario != null && !oldMedicoOfHorarioCollectionNewHorario.equals(medico)) {
                        oldMedicoOfHorarioCollectionNewHorario.getHorarioCollection().remove(horarioCollectionNewHorario);
                        oldMedicoOfHorarioCollectionNewHorario = em.merge(oldMedicoOfHorarioCollectionNewHorario);
                    }
                }
            }
            for (Consulta consultaCollectionNewConsulta : consultaCollectionNew) {
                if (!consultaCollectionOld.contains(consultaCollectionNewConsulta)) {
                    Medico oldMedicoOfConsultaCollectionNewConsulta = consultaCollectionNewConsulta.getMedico();
                    consultaCollectionNewConsulta.setMedico(medico);
                    consultaCollectionNewConsulta = em.merge(consultaCollectionNewConsulta);
                    if (oldMedicoOfConsultaCollectionNewConsulta != null && !oldMedicoOfConsultaCollectionNewConsulta.equals(medico)) {
                        oldMedicoOfConsultaCollectionNewConsulta.getConsultaCollection().remove(consultaCollectionNewConsulta);
                        oldMedicoOfConsultaCollectionNewConsulta = em.merge(oldMedicoOfConsultaCollectionNewConsulta);
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
                Integer id = medico.getIdmedico();
                if (findMedico(id) == null) {
                    throw new NonexistentEntityException("The medico with id " + id + " no longer exists.");
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
            Medico medico;
            try {
                medico = em.getReference(Medico.class, id);
                medico.getIdmedico();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medico with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Consulta> consultaCollectionOrphanCheck = medico.getConsultaCollection();
            for (Consulta consultaCollectionOrphanCheckConsulta : consultaCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medico (" + medico + ") cannot be destroyed since the Consulta " + consultaCollectionOrphanCheckConsulta + " in its consultaCollection field has a non-nullable medico field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Especialidade especialidade = medico.getEspecialidade();
            if (especialidade != null) {
                especialidade.getMedicoCollection().remove(medico);
                especialidade = em.merge(especialidade);
            }
            Estados ufCrm = medico.getUfCrm();
            if (ufCrm != null) {
                ufCrm.getMedicoCollection().remove(medico);
                ufCrm = em.merge(ufCrm);
            }
            Collection<Horario> horarioCollection = medico.getHorarioCollection();
            for (Horario horarioCollectionHorario : horarioCollection) {
                horarioCollectionHorario.setMedico(null);
                horarioCollectionHorario = em.merge(horarioCollectionHorario);
            }
            em.remove(medico);
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

    public List<Medico> findMedicoEntities() {
        return findMedicoEntities(true, -1, -1);
    }

    public List<Medico> findMedicoEntities(int maxResults, int firstResult) {
        return findMedicoEntities(false, maxResults, firstResult);
    }

    private List<Medico> findMedicoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medico.class));
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

    public Medico findMedico(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medico.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medico> rt = cq.from(Medico.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
