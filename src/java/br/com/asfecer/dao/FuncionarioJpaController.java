/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.asfecer.dao;

import br.com.asfecer.dao.exceptions.NonexistentEntityException;
import br.com.asfecer.dao.exceptions.RollbackFailureException;
import java.io.Serializable;
import javax.persistence.Query;
import javax.persistence.EntityNotFoundException;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import br.com.asfecer.model.Cargo;
import br.com.asfecer.model.Endereco;
import br.com.asfecer.model.Estados;
import br.com.asfecer.model.Funcionario;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.transaction.UserTransaction;

/**
 *
 * @author PToledo
 */
public class FuncionarioJpaController implements Serializable {

    public FuncionarioJpaController(UserTransaction utx, EntityManagerFactory emf) {
        this.utx = utx;
        this.emf = emf;
    }
    private UserTransaction utx = null;
    private EntityManagerFactory emf = null;

    public EntityManager getEntityManager() {
        return emf.createEntityManager();
    }

    public void create(Funcionario funcionario) throws RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Cargo cargo = funcionario.getCargo();
            if (cargo != null) {
                cargo = em.getReference(cargo.getClass(), cargo.getIdCargo());
                funcionario.setCargo(cargo);
            }
            Endereco endereco = funcionario.getEndereco();
            if (endereco != null) {
                endereco = em.getReference(endereco.getClass(), endereco.getIdEndereco());
                funcionario.setEndereco(endereco);
            }
            Estados ufEmissor = funcionario.getUfEmissor();
            if (ufEmissor != null) {
                ufEmissor = em.getReference(ufEmissor.getClass(), ufEmissor.getSigla());
                funcionario.setUfEmissor(ufEmissor);
            }
            em.persist(funcionario);
            if (cargo != null) {
                cargo.getFuncionarioCollection().add(funcionario);
                cargo = em.merge(cargo);
            }
            if (endereco != null) {
                endereco.getFuncionarioCollection().add(funcionario);
                endereco = em.merge(endereco);
            }
            if (ufEmissor != null) {
                ufEmissor.getFuncionarioCollection().add(funcionario);
                ufEmissor = em.merge(ufEmissor);
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

    public void edit(Funcionario funcionario) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Funcionario persistentFuncionario = em.find(Funcionario.class, funcionario.getIdFuncionario());
            Cargo cargoOld = persistentFuncionario.getCargo();
            Cargo cargoNew = funcionario.getCargo();
            Endereco enderecoOld = persistentFuncionario.getEndereco();
            Endereco enderecoNew = funcionario.getEndereco();
            Estados ufEmissorOld = persistentFuncionario.getUfEmissor();
            Estados ufEmissorNew = funcionario.getUfEmissor();
            if (cargoNew != null) {
                cargoNew = em.getReference(cargoNew.getClass(), cargoNew.getIdCargo());
                funcionario.setCargo(cargoNew);
            }
            if (enderecoNew != null) {
                enderecoNew = em.getReference(enderecoNew.getClass(), enderecoNew.getIdEndereco());
                funcionario.setEndereco(enderecoNew);
            }
            if (ufEmissorNew != null) {
                ufEmissorNew = em.getReference(ufEmissorNew.getClass(), ufEmissorNew.getSigla());
                funcionario.setUfEmissor(ufEmissorNew);
            }
            funcionario = em.merge(funcionario);
            if (cargoOld != null && !cargoOld.equals(cargoNew)) {
                cargoOld.getFuncionarioCollection().remove(funcionario);
                cargoOld = em.merge(cargoOld);
            }
            if (cargoNew != null && !cargoNew.equals(cargoOld)) {
                cargoNew.getFuncionarioCollection().add(funcionario);
                cargoNew = em.merge(cargoNew);
            }
            if (enderecoOld != null && !enderecoOld.equals(enderecoNew)) {
                enderecoOld.getFuncionarioCollection().remove(funcionario);
                enderecoOld = em.merge(enderecoOld);
            }
            if (enderecoNew != null && !enderecoNew.equals(enderecoOld)) {
                enderecoNew.getFuncionarioCollection().add(funcionario);
                enderecoNew = em.merge(enderecoNew);
            }
            if (ufEmissorOld != null && !ufEmissorOld.equals(ufEmissorNew)) {
                ufEmissorOld.getFuncionarioCollection().remove(funcionario);
                ufEmissorOld = em.merge(ufEmissorOld);
            }
            if (ufEmissorNew != null && !ufEmissorNew.equals(ufEmissorOld)) {
                ufEmissorNew.getFuncionarioCollection().add(funcionario);
                ufEmissorNew = em.merge(ufEmissorNew);
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
                Integer id = funcionario.getIdFuncionario();
                if (findFuncionario(id) == null) {
                    throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.");
                }
            }
            throw ex;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }

    public void destroy(Integer id) throws NonexistentEntityException, RollbackFailureException, Exception {
        EntityManager em = null;
        try {
            utx.begin();
            em = getEntityManager();
            Funcionario funcionario;
            try {
                funcionario = em.getReference(Funcionario.class, id);
                funcionario.getIdFuncionario();
            } catch (EntityNotFoundException enfe) {
                throw new NonexistentEntityException("The funcionario with id " + id + " no longer exists.", enfe);
            }
            Cargo cargo = funcionario.getCargo();
            if (cargo != null) {
                cargo.getFuncionarioCollection().remove(funcionario);
                cargo = em.merge(cargo);
            }
            Endereco endereco = funcionario.getEndereco();
            if (endereco != null) {
                endereco.getFuncionarioCollection().remove(funcionario);
                endereco = em.merge(endereco);
            }
            Estados ufEmissor = funcionario.getUfEmissor();
            if (ufEmissor != null) {
                ufEmissor.getFuncionarioCollection().remove(funcionario);
                ufEmissor = em.merge(ufEmissor);
            }
            em.remove(funcionario);
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

    public List<Funcionario> findFuncionarioEntities() {
        return findFuncionarioEntities(true, -1, -1);
    }

    public List<Funcionario> findFuncionarioEntities(int maxResults, int firstResult) {
        return findFuncionarioEntities(false, maxResults, firstResult);
    }

    private List<Funcionario> findFuncionarioEntities(boolean all, int maxResults, int firstResult) {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            cq.select(cq.from(Funcionario.class));
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

    public Funcionario findFuncionario(Integer id) {
        EntityManager em = getEntityManager();
        try {
            return em.find(Funcionario.class, id);
        } finally {
            em.close();
        }
    }

    public int getFuncionarioCount() {
        EntityManager em = getEntityManager();
        try {
            CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
            Root<Funcionario> rt = cq.from(Funcionario.class);
            cq.select(em.getCriteriaBuilder().count(rt));
            Query q = em.createQuery(cq);
            return ((Long) q.getSingleResult()).intValue();
        } finally {
            em.close();
        }
    }
    
}
