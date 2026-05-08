package com.lojinhasystem.system.resources.dto;

import com.lojinhasystem.system.entities.UsuarioPF;

public class UsuarioPFResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String rua;
    private String bairro;
    private Integer numero;
    private String telefone;
    private String cpf;

    public UsuarioPFResponseDTO() {
    }

    public UsuarioPFResponseDTO(UsuarioPF usuarioPF) {
        this.id = usuarioPF.getId();
        this.nome = usuarioPF.getNome();
        this.email = usuarioPF.getEmail();
        this.rua = usuarioPF.getRua();
        this.bairro = usuarioPF.getBairro();
        this.numero = usuarioPF.getNumero();
        this.telefone = usuarioPF.getTelefone();
        this.cpf = usuarioPF.getCpf();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
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

    public String getTelefone() {
        return telefone;
    }

    public String getCpf() {
        return cpf;
    }
}