/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Convenio;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Paciente;
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
public class ConvenioJpaController implements Serializable {

    public ConvenioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Convenio convenio) throws RollbackFailureException, Exception {
        if (convenio.getPacienteCollection() == null) {
            convenio.setPacienteCollection(new ArrayList<Paciente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Paciente> attachedPacienteCollection = new ArrayList<Paciente>();
            for (Paciente pacienteCollectionPacienteToAttach : convenio.getPacienteCollection()) {
                pacienteCollectionPacienteToAttach = em.getReference(pacienteCollectionPacienteToAttach.getClass(), pacienteCollectionPacienteToAttach.getIdPaciente());
                attachedPacienteCollection.add(pacienteCollectionPacienteToAttach);
            }
            convenio.setPacienteCollection(attachedPacienteCollection);
            em.persist(convenio);
            for (Paciente pacienteCollectionPaciente : convenio.getPacienteCollection()) {
                Convenio oldConvenioOfPacienteCollectionPaciente = pacienteCollectionPaciente.getConvenio();
                pacienteCollectionPaciente.setConvenio(convenio);
                pacienteCollectionPaciente = em.merge(pacienteCollectionPaciente);
                if (oldConvenioOfPacienteCollectionPaciente != null) {
                    oldConvenioOfPacienteCollectionPaciente.getPacienteCollection().remove(pacienteCollectionPaciente);
                    oldConvenioOfPacienteCollectionPaciente = em.merge(oldConvenioOfPacienteCollectionPaciente);
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

    public void edit(Convenio convenio) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Convenio persistentConvenio = em.find(Convenio.class, convenio.getIdConvenio());
            Collection<Paciente> pacienteCollectionOld = persistentConvenio.getPacienteCollection();
            Collection<Paciente> pacienteCollectionNew = convenio.getPacienteCollection();
            List<String> illegalOrphanMessages = null;
            for (Paciente pacienteCollectionOldPaciente : pacienteCollectionOld) {
                if (!pacienteCollectionNew.contains(pacienteCollectionOldPaciente)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Paciente " + pacienteCollectionOldPaciente + " since its convenio field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Paciente> attachedPacienteCollectionNew = new ArrayList<Paciente>();
            for (Paciente pacienteCollectionNewPacienteToAttach : pacienteCollectionNew) {
                pacienteCollectionNewPacienteToAttach = em.getReference(pacienteCollectionNewPacienteToAttach.getClass(), pacienteCollectionNewPacienteToAttach.getIdPaciente());
                attachedPacienteCollectionNew.add(pacienteCollectionNewPacienteToAttach);
            }
            pacienteCollectionNew = attachedPacienteCollectionNew;
            convenio.setPacienteCollection(pacienteCollectionNew);
            convenio = em.merge(convenio);
            for (Paciente pacienteCollectionNewPaciente : pacienteCollectionNew) {
                if (!pacienteCollectionOld.contains(pacienteCollectionNewPaciente)) {
                    Convenio oldConvenioOfPacienteCollectionNewPaciente = pacienteCollectionNewPaciente.getConvenio();
                    pacienteCollectionNewPaciente.setConvenio(convenio);
                    pacienteCollectionNewPaciente = em.merge(pacienteCollectionNewPaciente);
                    if (oldConvenioOfPacienteCollectionNewPaciente != null && !oldConvenioOfPacienteCollectionNewPaciente.equals(convenio)) {
                        oldConvenioOfPacienteCollectionNewPaciente.getPacienteCollection().remove(pacienteCollectionNewPaciente);
                        oldConvenioOfPacienteCollectionNewPaciente = em.merge(oldConvenioOfPacienteCollectionNewPaciente);
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
                Integer id = convenio.getIdConvenio();
                if (findConvenio(id) == null) {
                    throw new NonexistentEntityException("The convenio with id " + id + " no longer exists.");
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
            Convenio convenio;
            try {
                convenio = em.getReference(Convenio.class, id);
                convenio.getIdConvenio();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The convenio with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Paciente> pacienteCollectionOrphanCheck = convenio.getPacienteCollection();
            for (Paciente pacienteCollectionOrphanCheckPaciente : pacienteCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Convenio (" + convenio + ") cannot be destroyed since the Paciente " + pacienteCollectionOrphanCheckPaciente + " in its pacienteCollection field has a non-nullable convenio field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(convenio);
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

    public List<Convenio> findConvenioEntities() {
        return findConvenioEntities(true, -1, -1);
    }

    public List<Convenio> findConvenioEntities(int maxResults, int firstResult) {
        return findConvenioEntities(false, maxResults, firstResult);
    }

    private List<Convenio> findConvenioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Convenio.class));
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

    public Convenio findConvenio(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Convenio.class, id);
        } finally {
            em.close();
        }
    }

    public int getConvenioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Convenio> rt = cq.from(Convenio.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
