package com.comafi.service;

import com.comafi.repository.MovimientoRepository;
import com.comafi.model.Movimiento;
import java.util.List;

public class MovimientoService {
    private MovimientoRepository repo = new MovimientoRepository();

    public List<Movimiento> obtenerMovimientosPorCuenta(Long accountId) {
        return repo.obtenerMovimientosPorCuenta(accountId);
    }
}
