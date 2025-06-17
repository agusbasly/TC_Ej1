package com.comafi.controller;

import com.comafi.model.Account;
import com.comafi.model.Movimiento;
import com.comafi.model.dto.MovimientoCreationDTO;
import com.comafi.service.AccountService;
import com.comafi.service.MovimientoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;

@Path("accounts/{accountId}/movimientos")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class MovimientoController {

    private final MovimientoService movimientoService = new MovimientoService();
    private final AccountService accountService = new AccountService();

    @GET
    @Operation(
        summary = "Obtener movimientos por cuenta",
        description = "Devuelve una lista de movimientos asociados a una cuenta específica."
    )
    public List<Movimiento> obtenerMovimientosPorCuenta(
        @Parameter(description = "ID de la cuenta", required = true)
        @PathParam("accountId") Long accountId
    ) {
        return movimientoService.obtenerMovimientosPorCuenta(accountId);
    }

    @POST
    @Operation(
        summary = "Registrar un nuevo movimiento",
        description = "Registra un nuevo movimiento (depósito o retiro) en una cuenta específica."
    )
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Movimiento creado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de movimiento inválidos"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public Response crearMovimiento(
        @PathParam("accountId") Long accountId,
        MovimientoCreationDTO dto
    ) {
        try {
            Account cuenta = accountService.obtenerCuentaPorId(accountId);
            if (cuenta == null) {
                return Response.status(Response.Status.NOT_FOUND)
                               .entity("Cuenta no encontrada").build();
            }

            Movimiento nuevoMovimiento = movimientoService.crearMovimiento(dto, cuenta);
            return Response.status(Response.Status.CREATED).entity(nuevoMovimiento).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                           .entity("Error inesperado").build();
        }
    }
}
