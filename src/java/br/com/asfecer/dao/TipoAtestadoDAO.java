/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.IllegalOrphanException;
import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.PreexistingEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Medicamento;
import br.com.asfecer.model.Patologia;
import br.com.asfecer.model.Atestado;
import br.com.asfecer.model.TipoAtestado;
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
public class TipoAtestadoDAO implements Serializable {

    public TipoAtestadoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(TipoAtestado tipoatestado) throws PreexistingEntityException, RollbackFailureException, RuntimeException {
        if (tipoatestado.getAtestadoCollection() == null) {
            tipoatestado.setAtestadoCollection(new ArrayList<Atestado>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Medicamento medicamento = tipoatestado.getMedicamento();
            if (medicamento != null) {
                medicamento = em.getReference(medicamento.getClass(), medicamento.getIdMedicamento());
                tipoatestado.setMedicamento(medicamento);
            }
            Patologia patologia = tipoatestado.getPatologia();
            if (patologia != null) {
                patologia = em.getReference(patologia.getClass(), patologia.getIdPatologia());
                tipoatestado.setPatologia(patologia);
            }
            Collection<Atestado> attachedAtestadoCollection = new ArrayList<Atestado>();
            for (Atestado atestadoCollectionAtestadoToAttach : tipoatestado.getAtestadoCollection()) {
                atestadoCollectionAtestadoToAttach = em.getReference(atestadoCollectionAtestadoToAttach.getClass(), atestadoCollectionAtestadoToAttach.getIdAtestado());
                attachedAtestadoCollection.add(atestadoCollectionAtestadoToAttach);
            }
            tipoatestado.setAtestadoCollection(attachedAtestadoCollection);
            em.persist(tipoatestado);
            if (medicamento != null) {
                medicamento.getTipoAtestadoCollection().add(tipoatestado);
                medicamento = em.merge(medicamento);
            }
            if (patologia != null) {
                patologia.getTipoAtestadoCollection().add(tipoatestado);
                patologia = em.merge(patologia);
            }
            for (Atestado atestadoCollectionAtestado : tipoatestado.getAtestadoCollection()) {
                TipoAtestado oldTipoAtestadoOfAtestadoCollectionAtestado = atestadoCollectionAtestado.getTipoAtestado();
                atestadoCollectionAtestado.setTipoAtestado(tipoatestado);
                atestadoCollectionAtestado = em.merge(atestadoCollectionAtestado);
                if (oldTipoAtestadoOfAtestadoCollectionAtestado != null) {
                    oldTipoAtestadoOfAtestadoCollectionAtestado.getAtestadoCollection().remove(atestadoCollectionAtestado);
                    oldTipoAtestadoOfAtestadoCollectionAtestado = em.merge(oldTipoAtestadoOfAtestadoCollectionAtestado);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findTipoAtestado(tipoatestado.getIdTipoAtestado()) != null) {
                throw new PreexistingEntityException("TipoAtestado " + tipoatestado + " already exists.", ex);
            }
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(TipoAtestado tipoatestado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            TipoAtestado persistentTipoAtestado = em.find(TipoAtestado.class, tipoatestado.getIdTipoAtestado());
            Medicamento medicamentoOld = persistentTipoAtestado.getMedicamento();
            Medicamento medicamentoNew = tipoatestado.getMedicamento();
            Patologia patologiaOld = persistentTipoAtestado.getPatologia();
            Patologia patologiaNew = tipoatestado.getPatologia();
            Collection<Atestado> atestadoCollectionOld = persistentTipoAtestado.getAtestadoCollection();
            Collection<Atestado> atestadoCollectionNew = tipoatestado.getAtestadoCollection();
            List<String> illegalOrphanMessages = null;
            for (Atestado atestadoCollectionOldAtestado : atestadoCollectionOld) {
                if (!atestadoCollectionNew.contains(atestadoCollectionOldAtestado)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Atestado " + atestadoCollectionOldAtestado + " since its tipoAtestado field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (medicamentoNew != null) {
                medicamentoNew = em.getReference(medicamentoNew.getClass(), medicamentoNew.getIdMedicamento());
                tipoatestado.setMedicamento(medicamentoNew);
            }
            if (patologiaNew != null) {
                patologiaNew = em.getReference(patologiaNew.getClass(), patologiaNew.getIdPatologia());
                tipoatestado.setPatologia(patologiaNew);
            }
            Collection<Atestado> attachedAtestadoCollectionNew = new ArrayList<Atestado>();
            for (Atestado atestadoCollectionNewAtestadoToAttach : atestadoCollectionNew) {
                atestadoCollectionNewAtestadoToAttach = em.getReference(atestadoCollectionNewAtestadoToAttach.getClass(), atestadoCollectionNewAtestadoToAttach.getIdAtestado());
                attachedAtestadoCollectionNew.add(atestadoCollectionNewAtestadoToAttach);
            }
            atestadoCollectionNew = attachedAtestadoCollectionNew;
            tipoatestado.setAtestadoCollection(atestadoCollectionNew);
            tipoatestado = em.merge(tipoatestado);
            if (medicamentoOld != null && !medicamentoOld.equals(medicamentoNew)) {
                medicamentoOld.getTipoAtestadoCollection().remove(tipoatestado);
                medicamentoOld = em.merge(medicamentoOld);
            }
            if (medicamentoNew != null && !medicamentoNew.equals(medicamentoOld)) {
                medicamentoNew.getTipoAtestadoCollection().add(tipoatestado);
                medicamentoNew = em.merge(medicamentoNew);
            }
            if (patologiaOld != null && !patologiaOld.equals(patologiaNew)) {
                patologiaOld.getTipoAtestadoCollection().remove(tipoatestado);
                patologiaOld = em.merge(patologiaOld);
            }
            if (patologiaNew != null && !patologiaNew.equals(patologiaOld)) {
                patologiaNew.getTipoAtestadoCollection().add(tipoatestado);
                patologiaNew = em.merge(patologiaNew);
            }
            for (Atestado atestadoCollectionNewAtestado : atestadoCollectionNew) {
                if (!atestadoCollectionOld.contains(atestadoCollectionNewAtestado)) {
                    TipoAtestado oldTipoAtestadoOfAtestadoCollectionNewAtestado = atestadoCollectionNewAtestado.getTipoAtestado();
                    atestadoCollectionNewAtestado.setTipoAtestado(tipoatestado);
                    atestadoCollectionNewAtestado = em.merge(atestadoCollectionNewAtestado);
                    if (oldTipoAtestadoOfAtestadoCollectionNewAtestado != null && !oldTipoAtestadoOfAtestadoCollectionNewAtestado.equals(tipoatestado)) {
                        oldTipoAtestadoOfAtestadoCollectionNewAtestado.getAtestadoCollection().remove(atestadoCollectionNewAtestado);
                        oldTipoAtestadoOfAtestadoCollectionNewAtestado = em.merge(oldTipoAtestadoOfAtestadoCollectionNewAtestado);
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
                Integer id = tipoatestado.getIdTipoAtestado();
                if (findTipoAtestado(id) == null) {
                    throw new NonexistentEntityException("The tipoatestado with id " + id + " no longer exists.");
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
            TipoAtestado tipoatestado;
            try {
                tipoatestado = em.getReference(TipoAtestado.class, id);
                tipoatestado.getIdTipoAtestado();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The tipoatestado with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Atestado> atestadoCollectionOrphanCheck = tipoatestado.getAtestadoCollection();
            for (Atestado atestadoCollectionOrphanCheckAtestado : atestadoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This TipoAtestado (" + tipoatestado + ") cannot be destroyed since the Atestado " + atestadoCollectionOrphanCheckAtestado + " in its atestadoCollection field has a non-nullable tipoAtestado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medicamento medicamento = tipoatestado.getMedicamento();
            if (medicamento != null) {
                medicamento.getTipoAtestadoCollection().remove(tipoatestado);
                medicamento = em.merge(medicamento);
            }
            Patologia patologia = tipoatestado.getPatologia();
            if (patologia != null) {
                patologia.getTipoAtestadoCollection().remove(tipoatestado);
                patologia = em.merge(patologia);
            }
            em.remove(tipoatestado);
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

    public List<TipoAtestado> findTipoAtestadoEntities() {
        return findTipoAtestadoEntities(true, -1, -1);
    }

    public List<TipoAtestado> findTipoAtestadoEntities(int maxResults, int firstResult) {
        return findTipoAtestadoEntities(false, maxResults, firstResult);
    }

    private List<TipoAtestado> findTipoAtestadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(TipoAtestado.class));
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

    public TipoAtestado findTipoAtestado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(TipoAtestado.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoAtestadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<TipoAtestado> rt = cq.from(TipoAtestado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
