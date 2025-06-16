package com.comafi.controller;

import com.comafi.service.MovimientoService;
import com.comafi.model.Movimiento;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import java.util.List;

@Path("accounts/{accountId}/movimientos")
@Produces(MediaType.APPLICATION_JSON)
public class MovimientoController {
    private MovimientoService service = new MovimientoService();

    @GET
    @Operation(
        summary = "Obtener movimientos por cuenta",
        description = "Devuelve una lista de movimientos asociados a una cuenta específica."
    )
    public List<Movimiento> obtenerMovimientosPorCuenta(
        @Parameter(description = "ID de la cuenta para la cual se desean obtener los movimientos.", required = true)
        @PathParam("accountId") Long accountId
    ) {
        return service.obtenerMovimientosPorCuenta(accountId);
    }
}
