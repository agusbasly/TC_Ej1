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
}
