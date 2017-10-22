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
import br.com.asfecer.model.TipoAtestado;
import java.util.ArrayList;
import java.util.Collection;
import br.com.asfecer.model.ItensReceituario;
import br.com.asfecer.model.Medicamento;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class MedicamentoDAO implements Serializable {

    public MedicamentoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Medicamento medicamento) throws RollbackFailureException, RuntimeException {
        if (medicamento.getTipoAtestadoCollection() == null) {
            medicamento.setTipoAtestadoCollection(new ArrayList<TipoAtestado>());
        }
        if (medicamento.getItensReceituarioCollection() == null) {
            medicamento.setItensReceituarioCollection(new ArrayList<ItensReceituario>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Collection<TipoAtestado> attachedTipoAtestadoCollection = new ArrayList<TipoAtestado>();
            for (TipoAtestado tipoatestadoCollectionTipoAtestadoToAttach : medicamento.getTipoAtestadoCollection()) {
                tipoatestadoCollectionTipoAtestadoToAttach = em.getReference(tipoatestadoCollectionTipoAtestadoToAttach.getClass(), tipoatestadoCollectionTipoAtestadoToAttach.getIdTipoAtestado());
                attachedTipoAtestadoCollection.add(tipoatestadoCollectionTipoAtestadoToAttach);
            }
            medicamento.setTipoAtestadoCollection(attachedTipoAtestadoCollection);
            Collection<ItensReceituario> attachedItensReceituarioCollection = new ArrayList<ItensReceituario>();
            for (ItensReceituario itensReceituarioCollectionItensReceituarioToAttach : medicamento.getItensReceituarioCollection()) {
                itensReceituarioCollectionItensReceituarioToAttach = em.getReference(itensReceituarioCollectionItensReceituarioToAttach.getClass(), itensReceituarioCollectionItensReceituarioToAttach.getIdItensReceituario());
                attachedItensReceituarioCollection.add(itensReceituarioCollectionItensReceituarioToAttach);
            }
            medicamento.setItensReceituarioCollection(attachedItensReceituarioCollection);
            em.persist(medicamento);
            for (TipoAtestado tipoatestadoCollectionTipoAtestado : medicamento.getTipoAtestadoCollection()) {
                Medicamento oldMedicamentoOfTipoAtestadoCollectionTipoAtestado = tipoatestadoCollectionTipoAtestado.getMedicamento();
                tipoatestadoCollectionTipoAtestado.setMedicamento(medicamento);
                tipoatestadoCollectionTipoAtestado = em.merge(tipoatestadoCollectionTipoAtestado);
                if (oldMedicamentoOfTipoAtestadoCollectionTipoAtestado != null) {
                    oldMedicamentoOfTipoAtestadoCollectionTipoAtestado.getTipoAtestadoCollection().remove(tipoatestadoCollectionTipoAtestado);
                    oldMedicamentoOfTipoAtestadoCollectionTipoAtestado = em.merge(oldMedicamentoOfTipoAtestadoCollectionTipoAtestado);
                }
            }
            for (ItensReceituario itensReceituarioCollectionItensReceituario : medicamento.getItensReceituarioCollection()) {
                Medicamento oldMedicamentoOfItensReceituarioCollectionItensReceituario = itensReceituarioCollectionItensReceituario.getMedicamento();
                itensReceituarioCollectionItensReceituario.setMedicamento(medicamento);
                itensReceituarioCollectionItensReceituario = em.merge(itensReceituarioCollectionItensReceituario);
                if (oldMedicamentoOfItensReceituarioCollectionItensReceituario != null) {
                    oldMedicamentoOfItensReceituarioCollectionItensReceituario.getItensReceituarioCollection().remove(itensReceituarioCollectionItensReceituario);
                    oldMedicamentoOfItensReceituarioCollectionItensReceituario = em.merge(oldMedicamentoOfItensReceituarioCollectionItensReceituario);
                }
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

    public void edit(Medicamento medicamento) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento persistentMedicamento = em.find(Medicamento.class, medicamento.getIdMedicamento());
            Collection<TipoAtestado> tipoatestadoCollectionOld = persistentMedicamento.getTipoAtestadoCollection();
            Collection<TipoAtestado> tipoatestadoCollectionNew = medicamento.getTipoAtestadoCollection();
            Collection<ItensReceituario> itensReceituarioCollectionOld = persistentMedicamento.getItensReceituarioCollection();
            Collection<ItensReceituario> itensReceituarioCollectionNew = medicamento.getItensReceituarioCollection();
            List<String> illegalOrphanMessages = null;
            for (ItensReceituario itensReceituarioCollectionOldItensReceituario : itensReceituarioCollectionOld) {
                if (!itensReceituarioCollectionNew.contains(itensReceituarioCollectionOldItensReceituario)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain ItensReceituario " + itensReceituarioCollectionOldItensReceituario + " since its medicamento field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TipoAtestado> attachedTipoAtestadoCollectionNew = new ArrayList<TipoAtestado>();
            for (TipoAtestado tipoatestadoCollectionNewTipoAtestadoToAttach : tipoatestadoCollectionNew) {
                tipoatestadoCollectionNewTipoAtestadoToAttach = em.getReference(tipoatestadoCollectionNewTipoAtestadoToAttach.getClass(), tipoatestadoCollectionNewTipoAtestadoToAttach.getIdTipoAtestado());
                attachedTipoAtestadoCollectionNew.add(tipoatestadoCollectionNewTipoAtestadoToAttach);
            }
            tipoatestadoCollectionNew = attachedTipoAtestadoCollectionNew;
            medicamento.setTipoAtestadoCollection(tipoatestadoCollectionNew);
            Collection<ItensReceituario> attachedItensReceituarioCollectionNew = new ArrayList<ItensReceituario>();
            for (ItensReceituario itensReceituarioCollectionNewItensReceituarioToAttach : itensReceituarioCollectionNew) {
                itensReceituarioCollectionNewItensReceituarioToAttach = em.getReference(itensReceituarioCollectionNewItensReceituarioToAttach.getClass(), itensReceituarioCollectionNewItensReceituarioToAttach.getIdItensReceituario());
                attachedItensReceituarioCollectionNew.add(itensReceituarioCollectionNewItensReceituarioToAttach);
            }
            itensReceituarioCollectionNew = attachedItensReceituarioCollectionNew;
            medicamento.setItensReceituarioCollection(itensReceituarioCollectionNew);
            medicamento = em.merge(medicamento);
            for (TipoAtestado tipoatestadoCollectionOldTipoAtestado : tipoatestadoCollectionOld) {
                if (!tipoatestadoCollectionNew.contains(tipoatestadoCollectionOldTipoAtestado)) {
                    tipoatestadoCollectionOldTipoAtestado.setMedicamento(null);
                    tipoatestadoCollectionOldTipoAtestado = em.merge(tipoatestadoCollectionOldTipoAtestado);
                }
            }
            for (TipoAtestado tipoatestadoCollectionNewTipoAtestado : tipoatestadoCollectionNew) {
                if (!tipoatestadoCollectionOld.contains(tipoatestadoCollectionNewTipoAtestado)) {
                    Medicamento oldMedicamentoOfTipoAtestadoCollectionNewTipoAtestado = tipoatestadoCollectionNewTipoAtestado.getMedicamento();
                    tipoatestadoCollectionNewTipoAtestado.setMedicamento(medicamento);
                    tipoatestadoCollectionNewTipoAtestado = em.merge(tipoatestadoCollectionNewTipoAtestado);
                    if (oldMedicamentoOfTipoAtestadoCollectionNewTipoAtestado != null && !oldMedicamentoOfTipoAtestadoCollectionNewTipoAtestado.equals(medicamento)) {
                        oldMedicamentoOfTipoAtestadoCollectionNewTipoAtestado.getTipoAtestadoCollection().remove(tipoatestadoCollectionNewTipoAtestado);
                        oldMedicamentoOfTipoAtestadoCollectionNewTipoAtestado = em.merge(oldMedicamentoOfTipoAtestadoCollectionNewTipoAtestado);
                    }
                }
            }
            for (ItensReceituario itensReceituarioCollectionNewItensReceituario : itensReceituarioCollectionNew) {
                if (!itensReceituarioCollectionOld.contains(itensReceituarioCollectionNewItensReceituario)) {
                    Medicamento oldMedicamentoOfItensReceituarioCollectionNewItensReceituario = itensReceituarioCollectionNewItensReceituario.getMedicamento();
                    itensReceituarioCollectionNewItensReceituario.setMedicamento(medicamento);
                    itensReceituarioCollectionNewItensReceituario = em.merge(itensReceituarioCollectionNewItensReceituario);
                    if (oldMedicamentoOfItensReceituarioCollectionNewItensReceituario != null && !oldMedicamentoOfItensReceituarioCollectionNewItensReceituario.equals(medicamento)) {
                        oldMedicamentoOfItensReceituarioCollectionNewItensReceituario.getItensReceituarioCollection().remove(itensReceituarioCollectionNewItensReceituario);
                        oldMedicamentoOfItensReceituarioCollectionNewItensReceituario = em.merge(oldMedicamentoOfItensReceituarioCollectionNewItensReceituario);
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
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
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
            Collection<ItensReceituario> itensReceituarioCollectionOrphanCheck = medicamento.getItensReceituarioCollection();
            for (ItensReceituario itensReceituarioCollectionOrphanCheckItensReceituario : itensReceituarioCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Medicamento (" + medicamento + ") cannot be destroyed since the ItensReceituario " + itensReceituarioCollectionOrphanCheckItensReceituario + " in its itensReceituarioCollection field has a non-nullable medicamento field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Collection<TipoAtestado> tipoatestadoCollection = medicamento.getTipoAtestadoCollection();
            for (TipoAtestado tipoatestadoCollectionTipoAtestado : tipoatestadoCollection) {
                tipoatestadoCollectionTipoAtestado.setMedicamento(null);
                tipoatestadoCollectionTipoAtestado = em.merge(tipoatestadoCollectionTipoAtestado);
            }
            em.remove(medicamento);
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
