package com.lojinhasystem.system.resources.dto;

public class ClienteRequestDTO {

    private String nome;
    private Double divida;
    private String telefone;
    private String rua;
    private String bairro;
    private Integer numero;

    public ClienteRequestDTO() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Double getDivida() {
        return divida;
    }

    public void setDivida(Double divida) {
        this.divida = divida;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public Integer getNumero() {
        return numero;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }
}