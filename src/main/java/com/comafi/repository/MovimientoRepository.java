package com.comafi.repository;
import com.comafi.model.*;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import jakarta.persistence.criteria.*;


public class MovimientoRepository {
    private EntityManagerFactory emfFactory = Persistence.createEntityManagerFactory("em");
    private EntityManager emf = emfFactory.createEntityManager();

    public void guardar(String tipo, double monto, Account account) {
        EntityTransaction transaction = emf.getTransaction();
        try {
            transaction.begin();
            Movimiento movimiento = new Movimiento();
            movimiento.setMonto(monto);
            movimiento.setFecha(LocalDateTime.now());
            movimiento.setAccount(account);
            emf.persist(movimiento);
            transaction.commit();
        } catch (Exception e) {
            if (transaction.isActive()) {
                transaction.rollback();
            }
            throw e;
        }
    }

    public List<Movimiento> obtenerMovimientosPorCuenta(Long accountId) {
        CriteriaBuilder cb = emf.getCriteriaBuilder();
        CriteriaQuery<Movimiento> cq = cb.createQuery(Movimiento.class);
        Root<Movimiento> root = cq.from(Movimiento.class);
        cq.select(root).where(cb.equal(root.get("account").get("id"), accountId));
        return emf.createQuery(cq).getResultList();
    }
}
