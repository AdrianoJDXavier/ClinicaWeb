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
import br.com.asfecer.model.Tipoatestado;
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
public class TipoatestadoDAO implements Serializable {

    public TipoatestadoDAO(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Tipoatestado tipoatestado) throws PreexistingEntityException, RollbackFailureException, RuntimeException {
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
                medicamento.getTipoatestadoCollection().add(tipoatestado);
                medicamento = em.merge(medicamento);
            }
            if (patologia != null) {
                patologia.getTipoatestadoCollection().add(tipoatestado);
                patologia = em.merge(patologia);
            }
            for (Atestado atestadoCollectionAtestado : tipoatestado.getAtestadoCollection()) {
                Tipoatestado oldTipoAtestadoOfAtestadoCollectionAtestado = atestadoCollectionAtestado.getTipoAtestado();
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
            if (findTipoatestado(tipoatestado.getIdTipoAtestado()) != null) {
                throw new PreexistingEntityException("Tipoatestado " + tipoatestado + " already exists.", ex);
            }
            throw new RuntimeException(ex);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Tipoatestado tipoatestado) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, RuntimeException {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Tipoatestado persistentTipoatestado = em.find(Tipoatestado.class, tipoatestado.getIdTipoAtestado());
            Medicamento medicamentoOld = persistentTipoatestado.getMedicamento();
            Medicamento medicamentoNew = tipoatestado.getMedicamento();
            Patologia patologiaOld = persistentTipoatestado.getPatologia();
            Patologia patologiaNew = tipoatestado.getPatologia();
            Collection<Atestado> atestadoCollectionOld = persistentTipoatestado.getAtestadoCollection();
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
                medicamentoOld.getTipoatestadoCollection().remove(tipoatestado);
                medicamentoOld = em.merge(medicamentoOld);
            }
            if (medicamentoNew != null && !medicamentoNew.equals(medicamentoOld)) {
                medicamentoNew.getTipoatestadoCollection().add(tipoatestado);
                medicamentoNew = em.merge(medicamentoNew);
            }
            if (patologiaOld != null && !patologiaOld.equals(patologiaNew)) {
                patologiaOld.getTipoatestadoCollection().remove(tipoatestado);
                patologiaOld = em.merge(patologiaOld);
            }
            if (patologiaNew != null && !patologiaNew.equals(patologiaOld)) {
                patologiaNew.getTipoatestadoCollection().add(tipoatestado);
                patologiaNew = em.merge(patologiaNew);
            }
            for (Atestado atestadoCollectionNewAtestado : atestadoCollectionNew) {
                if (!atestadoCollectionOld.contains(atestadoCollectionNewAtestado)) {
                    Tipoatestado oldTipoAtestadoOfAtestadoCollectionNewAtestado = atestadoCollectionNewAtestado.getTipoAtestado();
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
                if (findTipoatestado(id) == null) {
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
            Tipoatestado tipoatestado;
            try {
                tipoatestado = em.getReference(Tipoatestado.class, id);
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
                illegalOrphanMessages.add("This Tipoatestado (" + tipoatestado + ") cannot be destroyed since the Atestado " + atestadoCollectionOrphanCheckAtestado + " in its atestadoCollection field has a non-nullable tipoAtestado field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Medicamento medicamento = tipoatestado.getMedicamento();
            if (medicamento != null) {
                medicamento.getTipoatestadoCollection().remove(tipoatestado);
                medicamento = em.merge(medicamento);
            }
            Patologia patologia = tipoatestado.getPatologia();
            if (patologia != null) {
                patologia.getTipoatestadoCollection().remove(tipoatestado);
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

    public List<Tipoatestado> findTipoatestadoEntities() {
        return findTipoatestadoEntities(true, -1, -1);
    }

    public List<Tipoatestado> findTipoatestadoEntities(int maxResults, int firstResult) {
        return findTipoatestadoEntities(false, maxResults, firstResult);
    }

    private List<Tipoatestado> findTipoatestadoEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Tipoatestado.class));
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

    public Tipoatestado findTipoatestado(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Tipoatestado.class, id);
        } finally {
            em.close();
        }
    }

    public int getTipoatestadoCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Tipoatestado> rt = cq.from(Tipoatestado.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
