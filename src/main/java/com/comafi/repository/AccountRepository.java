package com.comafi.repository;

import com.comafi.model.Account;
import jakarta.persistence.*;
import java.util.List;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;

public class AccountRepository {
    private static final EntityManagerFactory emf = Persistence.createEntityManagerFactory("em");
    private EntityManager em = emf.createEntityManager();

    public Account guardar(Account account) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.persist(account);
            transaction.commit();
            return account;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Account buscarPorId(Long id) {
        return em.find(Account.class, id);
    }

    public List<Account> buscarTodos() {
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Account> cq = cb.createQuery(Account.class);
        Root<Account> root = cq.from(Account.class);
        cq.select(root);
        return em.createQuery(cq).getResultList();
    }

    public void eliminar(Account account) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            em.remove(em.contains(account) ? account : em.merge(account));
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public Account actualizar(Account account) {
        EntityTransaction transaction = em.getTransaction();
        try {
            transaction.begin();
            Account mergedAccount = em.merge(account);
            transaction.commit();
            return mergedAccount;
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }
}
