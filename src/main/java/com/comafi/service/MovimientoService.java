package com.comafi.service;

import com.comafi.model.Account;
import com.comafi.model.Movimiento;
import com.comafi.model.dto.MovimientoCreationDTO;
import com.comafi.repository.MovimientoRepository;

import java.time.LocalDateTime;
import java.util.List;

public class MovimientoService {

    private MovimientoRepository repo = new MovimientoRepository();
    private AccountService accountService = new AccountService();

    public List<Movimiento> obtenerMovimientosPorCuenta(Long accountId) {
        return repo.obtenerMovimientosPorCuenta(accountId);
    }

    public Movimiento crearMovimiento(MovimientoCreationDTO dto, Account account) {
        if (dto.getMonto() == null || dto.getMonto() <= 0) {
            throw new IllegalArgumentException("El monto debe ser un valor positivo.");
        }

        if (!dto.getTipo().equalsIgnoreCase("DEPOSITO") && !dto.getTipo().equalsIgnoreCase("RETIRO")) {
            throw new IllegalArgumentException("Tipo de movimiento inválido. Debe ser 'DEPOSITO' o 'RETIRO'.");
        }

        Double nuevoBalance = account.getBalance();
        if (dto.getTipo().equalsIgnoreCase("DEPOSITO")) {
            nuevoBalance += dto.getMonto();
        } else {
            if (dto.getMonto() > nuevoBalance) {
                throw new IllegalArgumentException("Fondos insuficientes para realizar el retiro.");
            }
            nuevoBalance -= dto.getMonto();
        }

        // Actualizar cuenta
        accountService.actualizarBalance(account.getId(), nuevoBalance);

        // Crear y persistir el movimiento
        Movimiento movimiento = new Movimiento();
        movimiento.setMonto(dto.getMonto() * (dto.getTipo().equalsIgnoreCase("RETIRO") ? -1 : 1)); // guardar retiro como negativo
        movimiento.setFecha(LocalDateTime.now());
        movimiento.setAccount(account);

        return repo.crear(movimiento);
    }
}
