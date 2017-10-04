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
import br.com.asfecer.model.Cidade;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Estados;
import br.com.asfecer.model.Endereco;
import java.util.ArrayList;
import java.util.Collection;
import br.com.asfecer.model.Paciente;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class CidadeJpaController implements Serializable {

    public CidadeJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Cidade cidade) throws PreexistingEntityException, RollbackFailureException, Exception {
        if (cidade.getEnderecoCollection() == null) {
            cidade.setEnderecoCollection(new ArrayList<Endereco>());
        }
        if (cidade.getPacienteCollection() == null) {
            cidade.setPacienteCollection(new ArrayList<Paciente>());
        }
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Estados estado = cidade.getEstado();
            if (estado != null) {
                estado = em.getReference(estado.getClass(), estado.getSigla());
                cidade.setEstado(estado);
            }
            Collection<Endereco> attachedEnderecoCollection = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionEnderecoToAttach : cidade.getEnderecoCollection()) {
                enderecoCollectionEnderecoToAttach = em.getReference(enderecoCollectionEnderecoToAttach.getClass(), enderecoCollectionEnderecoToAttach.getIdEndereco());
                attachedEnderecoCollection.add(enderecoCollectionEnderecoToAttach);
            }
            cidade.setEnderecoCollection(attachedEnderecoCollection);
            Collection<Paciente> attachedPacienteCollection = new ArrayList<Paciente>();
            for (Paciente pacienteCollectionPacienteToAttach : cidade.getPacienteCollection()) {
                pacienteCollectionPacienteToAttach = em.getReference(pacienteCollectionPacienteToAttach.getClass(), pacienteCollectionPacienteToAttach.getIdPaciente());
                attachedPacienteCollection.add(pacienteCollectionPacienteToAttach);
            }
            cidade.setPacienteCollection(attachedPacienteCollection);
            em.persist(cidade);
            if (estado != null) {
                estado.getCidadeCollection().add(cidade);
                estado = em.merge(estado);
            }
            for (Endereco enderecoCollectionEndereco : cidade.getEnderecoCollection()) {
                Cidade oldCidadeOfEnderecoCollectionEndereco = enderecoCollectionEndereco.getCidade();
                enderecoCollectionEndereco.setCidade(cidade);
                enderecoCollectionEndereco = em.merge(enderecoCollectionEndereco);
                if (oldCidadeOfEnderecoCollectionEndereco != null) {
                    oldCidadeOfEnderecoCollectionEndereco.getEnderecoCollection().remove(enderecoCollectionEndereco);
                    oldCidadeOfEnderecoCollectionEndereco = em.merge(oldCidadeOfEnderecoCollectionEndereco);
                }
            }
            for (Paciente pacienteCollectionPaciente : cidade.getPacienteCollection()) {
                Cidade oldNaturalidadeCidadeOfPacienteCollectionPaciente = pacienteCollectionPaciente.getNaturalidadeCidade();
                pacienteCollectionPaciente.setNaturalidadeCidade(cidade);
                pacienteCollectionPaciente = em.merge(pacienteCollectionPaciente);
                if (oldNaturalidadeCidadeOfPacienteCollectionPaciente != null) {
                    oldNaturalidadeCidadeOfPacienteCollectionPaciente.getPacienteCollection().remove(pacienteCollectionPaciente);
                    oldNaturalidadeCidadeOfPacienteCollectionPaciente = em.merge(oldNaturalidadeCidadeOfPacienteCollectionPaciente);
                }
            }
            utx.commit();
        } catch (Exception ex) {
            try {
                utx.rollback();
            } catch (Exception re) {
                throw new RollbackFailureException("An error occurred attempting to roll back the transaction.", re);
            }
            if (findCidade(cidade.getIdCidade()) != null) {
                throw new PreexistingEntityException("Cidade " + cidade + " already exists.", ex);
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void edit(Cidade cidade) throws IllegalOrphanException, NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cidade persistentCidade = em.find(Cidade.class, cidade.getIdCidade());
            Estados estadoOld = persistentCidade.getEstado();
            Estados estadoNew = cidade.getEstado();
            Collection<Endereco> enderecoCollectionOld = persistentCidade.getEnderecoCollection();
            Collection<Endereco> enderecoCollectionNew = cidade.getEnderecoCollection();
            Collection<Paciente> pacienteCollectionOld = persistentCidade.getPacienteCollection();
            Collection<Paciente> pacienteCollectionNew = cidade.getPacienteCollection();
            List<String> illegalOrphanMessages = null;
            for (Endereco enderecoCollectionOldEndereco : enderecoCollectionOld) {
                if (!enderecoCollectionNew.contains(enderecoCollectionOldEndereco)) {
                    if (illegalOrphanMessages == null) {
                        illegalOrphanMessages = new ArrayList<String>();
                    }
                    illegalOrphanMessages.add("You must retain Endereco " + enderecoCollectionOldEndereco + " since its cidade field is not nullable.");
                }
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            if (estadoNew != null) {
                estadoNew = em.getReference(estadoNew.getClass(), estadoNew.getSigla());
                cidade.setEstado(estadoNew);
            }
            Collection<Endereco> attachedEnderecoCollectionNew = new ArrayList<Endereco>();
            for (Endereco enderecoCollectionNewEnderecoToAttach : enderecoCollectionNew) {
                enderecoCollectionNewEnderecoToAttach = em.getReference(enderecoCollectionNewEnderecoToAttach.getClass(), enderecoCollectionNewEnderecoToAttach.getIdEndereco());
                attachedEnderecoCollectionNew.add(enderecoCollectionNewEnderecoToAttach);
            }
            enderecoCollectionNew = attachedEnderecoCollectionNew;
            cidade.setEnderecoCollection(enderecoCollectionNew);
            Collection<Paciente> attachedPacienteCollectionNew = new ArrayList<Paciente>();
            for (Paciente pacienteCollectionNewPacienteToAttach : pacienteCollectionNew) {
                pacienteCollectionNewPacienteToAttach = em.getReference(pacienteCollectionNewPacienteToAttach.getClass(), pacienteCollectionNewPacienteToAttach.getIdPaciente());
                attachedPacienteCollectionNew.add(pacienteCollectionNewPacienteToAttach);
            }
            pacienteCollectionNew = attachedPacienteCollectionNew;
            cidade.setPacienteCollection(pacienteCollectionNew);
            cidade = em.merge(cidade);
            if (estadoOld != null && !estadoOld.equals(estadoNew)) {
                estadoOld.getCidadeCollection().remove(cidade);
                estadoOld = em.merge(estadoOld);
            }
            if (estadoNew != null && !estadoNew.equals(estadoOld)) {
                estadoNew.getCidadeCollection().add(cidade);
                estadoNew = em.merge(estadoNew);
            }
            for (Endereco enderecoCollectionNewEndereco : enderecoCollectionNew) {
                if (!enderecoCollectionOld.contains(enderecoCollectionNewEndereco)) {
                    Cidade oldCidadeOfEnderecoCollectionNewEndereco = enderecoCollectionNewEndereco.getCidade();
                    enderecoCollectionNewEndereco.setCidade(cidade);
                    enderecoCollectionNewEndereco = em.merge(enderecoCollectionNewEndereco);
                    if (oldCidadeOfEnderecoCollectionNewEndereco != null && !oldCidadeOfEnderecoCollectionNewEndereco.equals(cidade)) {
                        oldCidadeOfEnderecoCollectionNewEndereco.getEnderecoCollection().remove(enderecoCollectionNewEndereco);
                        oldCidadeOfEnderecoCollectionNewEndereco = em.merge(oldCidadeOfEnderecoCollectionNewEndereco);
                    }
                }
            }
            for (Paciente pacienteCollectionOldPaciente : pacienteCollectionOld) {
                if (!pacienteCollectionNew.contains(pacienteCollectionOldPaciente)) {
                    pacienteCollectionOldPaciente.setNaturalidadeCidade(null);
                    pacienteCollectionOldPaciente = em.merge(pacienteCollectionOldPaciente);
                }
            }
            for (Paciente pacienteCollectionNewPaciente : pacienteCollectionNew) {
                if (!pacienteCollectionOld.contains(pacienteCollectionNewPaciente)) {
                    Cidade oldNaturalidadeCidadeOfPacienteCollectionNewPaciente = pacienteCollectionNewPaciente.getNaturalidadeCidade();
                    pacienteCollectionNewPaciente.setNaturalidadeCidade(cidade);
                    pacienteCollectionNewPaciente = em.merge(pacienteCollectionNewPaciente);
                    if (oldNaturalidadeCidadeOfPacienteCollectionNewPaciente != null && !oldNaturalidadeCidadeOfPacienteCollectionNewPaciente.equals(cidade)) {
                        oldNaturalidadeCidadeOfPacienteCollectionNewPaciente.getPacienteCollection().remove(pacienteCollectionNewPaciente);
                        oldNaturalidadeCidadeOfPacienteCollectionNewPaciente = em.merge(oldNaturalidadeCidadeOfPacienteCollectionNewPaciente);
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
                Integer id = cidade.getIdCidade();
                if (findCidade(id) == null) {
                    throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.");
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
            Cidade cidade;
            try {
                cidade = em.getReference(Cidade.class, id);
                cidade.getIdCidade();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The cidade with id " + id + " no longer exists.", enfe);
            }
            List<String> illegalOrphanMessages = null;
            Collection<Endereco> enderecoCollectionOrphanCheck = cidade.getEnderecoCollection();
            for (Endereco enderecoCollectionOrphanCheckEndereco : enderecoCollectionOrphanCheck) {
                if (illegalOrphanMessages == null) {
                    illegalOrphanMessages = new ArrayList<String>();
                }
                illegalOrphanMessages.add("This Cidade (" + cidade + ") cannot be destroyed since the Endereco " + enderecoCollectionOrphanCheckEndereco + " in its enderecoCollection field has a non-nullable cidade field.");
            }
            if (illegalOrphanMessages != null) {
                throw new IllegalOrphanException(illegalOrphanMessages);
            }
            Estados estado = cidade.getEstado();
            if (estado != null) {
                estado.getCidadeCollection().remove(cidade);
                estado = em.merge(estado);
            }
            Collection<Paciente> pacienteCollection = cidade.getPacienteCollection();
            for (Paciente pacienteCollectionPaciente : pacienteCollection) {
                pacienteCollectionPaciente.setNaturalidadeCidade(null);
                pacienteCollectionPaciente = em.merge(pacienteCollectionPaciente);
            }
            em.remove(cidade);
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

    public List<Cidade> findCidadeEntities() {
        return findCidadeEntities(true, -1, -1);
    }

    public List<Cidade> findCidadeEntities(int maxResults, int firstResult) {
        return findCidadeEntities(false, maxResults, firstResult);
    }

    private List<Cidade> findCidadeEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Cidade.class));
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

    public Cidade findCidade(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Cidade.class, id);
        } finally {
            em.close();
        }
    }

    public int getCidadeCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Cidade> rt = cq.from(Cidade.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
