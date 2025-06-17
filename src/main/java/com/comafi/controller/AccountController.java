package com.comafi.controller;

import com.comafi.model.*;
import com.comafi.model.dto.AccountBalanceDTO;
import com.comafi.model.dto.AccountCreationDTO;
import com.comafi.model.dto.TransferenciaDTO;
import com.comafi.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import java.util.List;

@Path("/accounts")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@Tag(name = "Account", description = "Endpoints para gestión de cuentas")
public class AccountController {
    private AccountService service = new AccountService();

    @POST
    @Operation(summary = "Crear una nueva cuenta", description = "Crea una nueva cuenta bancaria")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cuenta creada exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos de cuenta inválidos")
    })
    public Response crearCuenta(@Parameter(description = "Datos de la cuenta a crear") AccountCreationDTO dto) {
        try {
            Account nuevaCuenta = new Account();
            nuevaCuenta.setDueno(dto.getNombre());
            nuevaCuenta.setBalance(dto.getBalance() != null ? dto.getBalance() : 0.0);
            Account cuentaCreada = service.crearCuenta(nuevaCuenta);
            return Response.status(Response.Status.CREATED).entity(cuentaCreada).build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error inesperado").build();
        }
    }

    @GET
    @Operation(summary = "Obtener todas las cuentas", description = "Retorna una lista de todas las cuentas existentes")
    public List<Account> obtenerTodasCuentas() {
        return service.obtenerTodasCuentas();
    }

    @GET
    @Path("/{id}")
    @Operation(summary = "Obtener cuenta por ID", description = "Retorna una cuenta específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cuenta encontrada"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public Response obtenerCuentaPorId(@Parameter(description = "ID de la cuenta") @PathParam("id") Long id) {
        Account cuenta = service.obtenerCuentaPorId(id);
        if (cuenta != null) {
            return Response.ok(cuenta).build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @DELETE
    @Path("/{id}")
    @Operation(summary = "Eliminar cuenta", description = "Elimina una cuenta específica por su ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cuenta eliminada exitosamente"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public Response eliminarCuenta(@Parameter(description = "ID de la cuenta a eliminar") @PathParam("id") Long id) {
        Account cuenta = service.obtenerCuentaPorId(id);
        if (cuenta != null) {
            service.eliminarCuenta(cuenta);
            return Response.noContent().build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("/{id}/balance")
    @Operation(summary = "Actualizar balance", description = "Actualiza el balance de una cuenta específica")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Balance actualizado exitosamente"),
        @ApiResponse(responseCode = "400", description = "Datos inválidos"),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor")
    })
    
    public Response update(
        @Parameter(description = "ID de la cuenta") @PathParam("id") Long id,
        @Parameter(description = "Nuevo balance de la cuenta") AccountBalanceDTO body) {
        try {
        service.actualizarBalance(id, body.getBalance());
        Account updated = service.obtenerCuentaPorId(id);
        return Response.ok(updated).build();
        } catch (IllegalArgumentException e) {
        return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity("Error inesperado").build();
        }
    }
    
    @POST
    @Path("{sourceId}/transferencia/{targetId}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Transferir saldo entre cuentas")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Transferencia exitosa"),
        @ApiResponse(responseCode = "400", description = "Error de validación o saldo insuficiente"),
        @ApiResponse(responseCode = "404", description = "Cuenta no encontrada")
    })
    public Response transferir(
        @PathParam("sourceId") Long sourceId,
        @PathParam("targetId") Long targetId,
        TransferenciaDTO dto
    ) {
        try {
            service.transferirSaldo(sourceId, targetId, dto.getMonto());
            return Response.ok("Transferencia realizada con éxito").build();
        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST).entity(e.getMessage()).build();
        } catch (Exception e) {
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity("Error inesperado").build();
    }
}

}
