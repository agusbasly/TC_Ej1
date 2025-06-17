package com.comafi.service;

import com.comafi.model.*;
import com.comafi.repository.*;
import java.util.List;

public class AccountService {
    private AccountRepository repo = new AccountRepository();

    public Account crearCuenta(Account account) {
        if (account.getBalance() == null) {
            account.setBalance(0.0);
        }
        return repo.guardar(account);
    }

    public Account obtenerCuentaPorId(Long id) {
        return repo.buscarPorId(id);
    }

    public List<Account> obtenerTodasCuentas() {
        return repo.buscarTodos();
    }

    public void eliminarCuenta(Account account) {
        repo.eliminar(account);
    }

    public void actualizarBalance(Long id, Double nuevoBalance) {
        Account account = repo.buscarPorId(id);
        if (account != null) {
            account.setBalance(nuevoBalance);
            repo.actualizar(account);
        } else {
            throw new RuntimeException("Cuenta no encontrada");
        }
    }
    public void transferirSaldo(Long sourceId, Long targetId, double monto) {
    if (monto <= 0) {
        throw new IllegalArgumentException("El monto debe ser positivo.");
    }

    Account origen = obtenerCuentaPorId(sourceId);
    Account destino = obtenerCuentaPorId(targetId);

    if (origen == null || destino == null) {
        throw new IllegalArgumentException("Una de las cuentas no existe.");
    }

    if (origen.getBalance() < monto) {
        throw new IllegalArgumentException("Saldo insuficiente en la cuenta origen.");
    }

    origen.setBalance(origen.getBalance() - monto);
    destino.setBalance(destino.getBalance() + monto);

    repo.actualizar(origen);
    repo.actualizar(destino);

    MovimientoRepository movimientoRepo = new MovimientoRepository();
    movimientoRepo.crearTransferencia(monto, origen, destino);
}

}
