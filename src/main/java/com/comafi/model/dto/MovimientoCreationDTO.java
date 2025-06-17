package com.comafi.model.dto;

public class MovimientoCreationDTO {

    private Double monto;
    private String tipo; // va a retirar o depositar?

    public Double getMonto() {
        return monto;
    }

    public void setMonto(Double monto) {
        this.monto = monto;
    }
    
    public String getTipo() {
        return tipo;
    }
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }
}
