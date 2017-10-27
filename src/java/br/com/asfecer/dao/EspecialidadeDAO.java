/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import br.com.asfecer.model.Especialidade;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Medico;
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
public class EspecialidadeDAO implements Serializable {

    public EspecialidadeDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Especialidade especialidade) throws RollbackFailureException, Exception {
        if (especialidade.getMedicoCollection() == null) {
            especialidade.setMedicoCollection(new ArrayList<Medico>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Medico> attachedMedicoCollection = new ArrayList<Medico>();
            for (Medico medicoCollectionMedicoToAttach : especialidade.getMedicoCollection()) {
                medicoCollectionMedicoToAttach = em.getReference(medicoCollectionMedicoToAttach.getClass(), medicoCollectionMedicoToAttach.getIdmedico());
                attachedMedicoCollection.add(medicoCollectionMedicoToAttach);
            }
            especialidade.setMedicoCollection(attachedMedicoCollection);
            em.persist(especialidade);
            for (Medico medicoCollectionMedico : especialidade.getMedicoCollection()) {
                Especialidade oldEspecialidadeOfMedicoCollectionMedico = medicoCollectionMedico.getEspecialidade();
                medicoCollectionMedico.setEspecialidade(especialidade);
                medicoCollectionMedico = em.merge(medicoCollectionMedico);
                if (oldEspecialidadeOfMedicoCollectionMedico != null) {
                    oldEspecialidadeOfMedicoCollectionMedico.getMedicoCollection().remove(medicoCollectionMedico);
                    oldEspecialidadeOfMedicoCollectionMedico = em.merge(oldEspecialidadeOfMedicoCollectionMedico);
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

    public void edit(Especialidade especialidade) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Especialidade persistentEspecialidade = em.find(Especialidade.class, especialidade.getIdespecialidade());
            Collection<Medico> medicoCollectionOld = persistentEspecialidade.getMedicoCollection();
            Collection<Medico> medicoCollectionNew = especialidade.getMedicoCollection();
            List<String> illegalOrphanMessages = null;
            for (Medico medicoCollectionOldMedico : medicoCollectionOld) {
                if (!medicoCollectionNew.contains(medicoCollectionOldMedico)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Medico " + medicoCollectionOldMedico + " since its especialidade field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Medico> attachedMedicoCollectionNew = new ArrayList<Medico>();
            for (Medico medicoCollectionNewMedicoToAttach : medicoCollectionNew) {
                medicoCollectionNewMedicoToAttach = em.getReference(medicoCollectionNewMedicoToAttach.getClass(), medicoCollectionNewMedicoToAttach.getIdmedico());
                attachedMedicoCollectionNew.add(medicoCollectionNewMedicoToAttach);
            }
            medicoCollectionNew = attachedMedicoCollectionNew;
            especialidade.setMedicoCollection(medicoCollectionNew);
            especialidade = em.merge(especialidade);
            for (Medico medicoCollectionNewMedico : medicoCollectionNew) {
                if (!medicoCollectionOld.contains(medicoCollectionNewMedico)) {
                    Especialidade oldEspecialidadeOfMedicoCollectionNewMedico = medicoCollectionNewMedico.getEspecialidade();
                    medicoCollectionNewMedico.setEspecialidade(especialidade);
                    medicoCollectionNewMedico = em.merge(medicoCollectionNewMedico);
                    if (oldEspecialidadeOfMedicoCollectionNewMedico != null && !oldEspecialidadeOfMedicoCollectionNewMedico.equals(especialidade)) {
                        oldEspecialidadeOfMedicoCollectionNewMedico.getMedicoCollection().remove(medicoCollectionNewMedico);
                        oldEspecialidadeOfMedicoCollectionNewMedico = em.merge(oldEspecialidadeOfMedicoCollectionNewMedico);
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
                Integer id = especialidade.getIdespecialidade();
                if (findEspecialidade(id) == null) {
                    throw new NonexistentEntityException("The especialidade with id " + id + " no longer exists.");
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
            Especialidade especialidade;
            try {
                especialidade = em.getReference(Especialidade.class, id);
                especialidade.getIdespecialidade();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The especialidade with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Medico> medicoCollectionOrphanCheck = especialidade.getMedicoCollection();
            for (Medico medicoCollectionOrphanCheckMedico : medicoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Especialidade (" + especialidade + ") cannot be destroyed since the Medico " + medicoCollectionOrphanCheckMedico + " in its medicoCollection field has a non-nullable especialidade field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            em.remove(especialidade);
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

    public List<Especialidade> findEspecialidadeEntities() {
        return findEspecialidadeEntities(true, -1, -1);
    }

    public List<Especialidade> findEspecialidadeEntities(int maxResults, int firstResult) {
        return findEspecialidadeEntities(false, maxResults, firstResult);
    }

    private List<Especialidade> findEspecialidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Especialidade.class));
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

    public Especialidade findEspecialidade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Especialidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getEspecialidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Especialidade> rt = cq.from(Especialidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
