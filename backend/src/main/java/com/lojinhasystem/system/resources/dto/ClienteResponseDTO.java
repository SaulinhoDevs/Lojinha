package com.lojinhasystem.system.resources.dto;

import com.lojinhasystem.system.entities.Cliente;

public class ClienteResponseDTO {

    private Long id;
    private String nome;
    private Double divida;
    private String telefone;
    private String rua;
    private String bairro;
    private Integer numero;

    public ClienteResponseDTO() {
    }

    public ClienteResponseDTO(Cliente cliente) {
        this.id = cliente.getId();
        this.nome = cliente.getNome();
        this.divida = cliente.getDivida();
        this.telefone = cliente.getTelefone();
        this.rua = cliente.getRua();
        this.bairro = cliente.getBairro();
        this.numero = cliente.getNumero();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Double getDivida() {
        return divida;
    }

    public String getTelefone() {
        return telefone;
    }

    public String getRua() {
        return rua;
    }

    public String getBairro() {
        return bairro;
    }

    public Integer getNumero() {
        return numero;
    }
}