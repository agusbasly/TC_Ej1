package com.comafi.model.dto;

public class AccountCreationDTO {
    private String nombre;
    private Double balance;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }  
}
