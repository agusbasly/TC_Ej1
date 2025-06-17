package com.comafi.repository;

import com.comafi.model.Account;
import com.comafi.model.Movimiento;
import jakarta.persistence.*;
import jakarta.persistence.criteria.*;

import java.time.LocalDateTime;
import java.util.List;

public class MovimientoRepository {
    private EntityManagerFactory emfFactory = Persistence.createEntityManagerFactory("em");
    private EntityManager emf = emfFactory.createEntityManager();

    public Movimiento crear(Movimiento movimiento) {
        EntityTransaction transaction = emf.getTransaction();
        try {
            transaction.begin();
            emf.persist(movimiento);
            transaction.commit();
            return movimiento;
        } catch (Exception e) {
            if (transaction.isActive()) transaction.rollback();
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
    
    public void crearTransferencia(double monto, Account origen, Account destino) {
    EntityTransaction transaction = emf.getTransaction();
    try {
        transaction.begin();

        Movimiento salida = new Movimiento();
        salida.setMonto(-monto);
        salida.setFecha(LocalDateTime.now());
        salida.setAccount(origen);

        Movimiento entrada = new Movimiento();
        entrada.setMonto(monto);
        entrada.setFecha(LocalDateTime.now());
        entrada.setAccount(destino);

        emf.persist(salida);
        emf.persist(entrada);

        transaction.commit();
    } catch (Exception e) {
        if (transaction.isActive()) transaction.rollback();
        throw e;
    }
}
}
