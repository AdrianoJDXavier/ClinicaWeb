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
import br.com.asfecer.model.Tipoatestado;
import java.util.ArrayList;
import java.util.Collection;
import br.com.asfecer.model.Itensreceituario;
import br.com.asfecer.model.Medicamento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class MedicamentoJpaController implements Serializable {

    public MedicamentoJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicamento medicamento) throws RollbackFailureException, Exception {
        if (medicamento.getTipoatestadoCollection() == null) {
            medicamento.setTipoatestadoCollection(new ArrayList<Tipoatestado>());
        }
        if (medicamento.getItensreceituarioCollection() == null) {
            medicamento.setItensreceituarioCollection(new ArrayList<Itensreceituario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<Tipoatestado> attachedTipoatestadoCollection = new ArrayList<Tipoatestado>();
            for (Tipoatestado tipoatestadoCollectionTipoatestadoToAttach : medicamento.getTipoatestadoCollection()) {
                tipoatestadoCollectionTipoatestadoToAttach = em.getReference(tipoatestadoCollectionTipoatestadoToAttach.getClass(), tipoatestadoCollectionTipoatestadoToAttach.getIdTipoAtestado());
                attachedTipoatestadoCollection.add(tipoatestadoCollectionTipoatestadoToAttach);
            }
            medicamento.setTipoatestadoCollection(attachedTipoatestadoCollection);
            Collection<Itensreceituario> attachedItensreceituarioCollection = new ArrayList<Itensreceituario>();
            for (Itensreceituario itensreceituarioCollectionItensreceituarioToAttach : medicamento.getItensreceituarioCollection()) {
                itensreceituarioCollectionItensreceituarioToAttach = em.getReference(itensreceituarioCollectionItensreceituarioToAttach.getClass(), itensreceituarioCollectionItensreceituarioToAttach.getIdItensReceituario());
                attachedItensreceituarioCollection.add(itensreceituarioCollectionItensreceituarioToAttach);
            }
            medicamento.setItensreceituarioCollection(attachedItensreceituarioCollection);
            em.persist(medicamento);
            for (Tipoatestado tipoatestadoCollectionTipoatestado : medicamento.getTipoatestadoCollection()) {
                Medicamento oldMedicamentoOfTipoatestadoCollectionTipoatestado = tipoatestadoCollectionTipoatestado.getMedicamento();
                tipoatestadoCollectionTipoatestado.setMedicamento(medicamento);
                tipoatestadoCollectionTipoatestado = em.merge(tipoatestadoCollectionTipoatestado);
                if (oldMedicamentoOfTipoatestadoCollectionTipoatestado != null) {
                    oldMedicamentoOfTipoatestadoCollectionTipoatestado.getTipoatestadoCollection().remove(tipoatestadoCollectionTipoatestado);
                    oldMedicamentoOfTipoatestadoCollectionTipoatestado = em.merge(oldMedicamentoOfTipoatestadoCollectionTipoatestado);
                }
            }
            for (Itensreceituario itensreceituarioCollectionItensreceituario : medicamento.getItensreceituarioCollection()) {
                Medicamento oldMedicamentoOfItensreceituarioCollectionItensreceituario = itensreceituarioCollectionItensreceituario.getMedicamento();
                itensreceituarioCollectionItensreceituario.setMedicamento(medicamento);
                itensreceituarioCollectionItensreceituario = em.merge(itensreceituarioCollectionItensreceituario);
                if (oldMedicamentoOfItensreceituarioCollectionItensreceituario != null) {
                    oldMedicamentoOfItensreceituarioCollectionItensreceituario.getItensreceituarioCollection().remove(itensreceituarioCollectionItensreceituario);
                    oldMedicamentoOfItensreceituarioCollectionItensreceituario = em.merge(oldMedicamentoOfItensreceituarioCollectionItensreceituario);
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

    public void edit(Medicamento medicamento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento persistentMedicamento = em.find(Medicamento.class, medicamento.getIdMedicamento());
            Collection<Tipoatestado> tipoatestadoCollectionOld = persistentMedicamento.getTipoatestadoCollection();
            Collection<Tipoatestado> tipoatestadoCollectionNew = medicamento.getTipoatestadoCollection();
            Collection<Itensreceituario> itensreceituarioCollectionOld = persistentMedicamento.getItensreceituarioCollection();
            Collection<Itensreceituario> itensreceituarioCollectionNew = medicamento.getItensreceituarioCollection();
            List<String> illegalOrphanMessages = null;
            for (Itensreceituario itensreceituarioCollectionOldItensreceituario : itensreceituarioCollectionOld) {
                if (!itensreceituarioCollectionNew.contains(itensreceituarioCollectionOldItensreceituario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Itensreceituario " + itensreceituarioCollectionOldItensreceituario + " since its medicamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Tipoatestado> attachedTipoatestadoCollectionNew = new ArrayList<Tipoatestado>();
            for (Tipoatestado tipoatestadoCollectionNewTipoatestadoToAttach : tipoatestadoCollectionNew) {
                tipoatestadoCollectionNewTipoatestadoToAttach = em.getReference(tipoatestadoCollectionNewTipoatestadoToAttach.getClass(), tipoatestadoCollectionNewTipoatestadoToAttach.getIdTipoAtestado());
                attachedTipoatestadoCollectionNew.add(tipoatestadoCollectionNewTipoatestadoToAttach);
            }
            tipoatestadoCollectionNew = attachedTipoatestadoCollectionNew;
            medicamento.setTipoatestadoCollection(tipoatestadoCollectionNew);
            Collection<Itensreceituario> attachedItensreceituarioCollectionNew = new ArrayList<Itensreceituario>();
            for (Itensreceituario itensreceituarioCollectionNewItensreceituarioToAttach : itensreceituarioCollectionNew) {
                itensreceituarioCollectionNewItensreceituarioToAttach = em.getReference(itensreceituarioCollectionNewItensreceituarioToAttach.getClass(), itensreceituarioCollectionNewItensreceituarioToAttach.getIdItensReceituario());
                attachedItensreceituarioCollectionNew.add(itensreceituarioCollectionNewItensreceituarioToAttach);
            }
            itensreceituarioCollectionNew = attachedItensreceituarioCollectionNew;
            medicamento.setItensreceituarioCollection(itensreceituarioCollectionNew);
            medicamento = em.merge(medicamento);
            for (Tipoatestado tipoatestadoCollectionOldTipoatestado : tipoatestadoCollectionOld) {
                if (!tipoatestadoCollectionNew.contains(tipoatestadoCollectionOldTipoatestado)) {
                    tipoatestadoCollectionOldTipoatestado.setMedicamento(null);
                    tipoatestadoCollectionOldTipoatestado = em.merge(tipoatestadoCollectionOldTipoatestado);
                }
            }
            for (Tipoatestado tipoatestadoCollectionNewTipoatestado : tipoatestadoCollectionNew) {
                if (!tipoatestadoCollectionOld.contains(tipoatestadoCollectionNewTipoatestado)) {
                    Medicamento oldMedicamentoOfTipoatestadoCollectionNewTipoatestado = tipoatestadoCollectionNewTipoatestado.getMedicamento();
                    tipoatestadoCollectionNewTipoatestado.setMedicamento(medicamento);
                    tipoatestadoCollectionNewTipoatestado = em.merge(tipoatestadoCollectionNewTipoatestado);
                    if (oldMedicamentoOfTipoatestadoCollectionNewTipoatestado != null && !oldMedicamentoOfTipoatestadoCollectionNewTipoatestado.equals(medicamento)) {
                        oldMedicamentoOfTipoatestadoCollectionNewTipoatestado.getTipoatestadoCollection().remove(tipoatestadoCollectionNewTipoatestado);
                        oldMedicamentoOfTipoatestadoCollectionNewTipoatestado = em.merge(oldMedicamentoOfTipoatestadoCollectionNewTipoatestado);
                    }
                }
            }
            for (Itensreceituario itensreceituarioCollectionNewItensreceituario : itensreceituarioCollectionNew) {
                if (!itensreceituarioCollectionOld.contains(itensreceituarioCollectionNewItensreceituario)) {
                    Medicamento oldMedicamentoOfItensreceituarioCollectionNewItensreceituario = itensreceituarioCollectionNewItensreceituario.getMedicamento();
                    itensreceituarioCollectionNewItensreceituario.setMedicamento(medicamento);
                    itensreceituarioCollectionNewItensreceituario = em.merge(itensreceituarioCollectionNewItensreceituario);
                    if (oldMedicamentoOfItensreceituarioCollectionNewItensreceituario != null && !oldMedicamentoOfItensreceituarioCollectionNewItensreceituario.equals(medicamento)) {
                        oldMedicamentoOfItensreceituarioCollectionNewItensreceituario.getItensreceituarioCollection().remove(itensreceituarioCollectionNewItensreceituario);
                        oldMedicamentoOfItensreceituarioCollectionNewItensreceituario = em.merge(oldMedicamentoOfItensreceituarioCollectionNewItensreceituario);
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
                Integer id = medicamento.getIdMedicamento();
                if (findMedicamento(id) == null) {
                    throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.");
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
            Medicamento medicamento;
            try {
                medicamento = em.getReference(Medicamento.class, id);
                medicamento.getIdMedicamento();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The medicamento with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Itensreceituario> itensreceituarioCollectionOrphanCheck = medicamento.getItensreceituarioCollection();
            for (Itensreceituario itensreceituarioCollectionOrphanCheckItensreceituario : itensreceituarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicamento (" + medicamento + ") cannot be destroyed since the Itensreceituario " + itensreceituarioCollectionOrphanCheckItensreceituario + " in its itensreceituarioCollection field has a non-nullable medicamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<Tipoatestado> tipoatestadoCollection = medicamento.getTipoatestadoCollection();
            for (Tipoatestado tipoatestadoCollectionTipoatestado : tipoatestadoCollection) {
                tipoatestadoCollectionTipoatestado.setMedicamento(null);
                tipoatestadoCollectionTipoatestado = em.merge(tipoatestadoCollectionTipoatestado);
            }
            em.remove(medicamento);
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

    public List<Medicamento> findMedicamentoEntities() {
        return findMedicamentoEntities(true, -1, -1);
    }

    public List<Medicamento> findMedicamentoEntities(int maxResults, int firstResult) {
        return findMedicamentoEntities(false, maxResults, firstResult);
    }

    private List<Medicamento> findMedicamentoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Medicamento.class));
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

    public Medicamento findMedicamento(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Medicamento.class, id);
        } finally {
            em.close();
        }
    }

    public int getMedicamentoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Medicamento> rt = cq.from(Medicamento.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
