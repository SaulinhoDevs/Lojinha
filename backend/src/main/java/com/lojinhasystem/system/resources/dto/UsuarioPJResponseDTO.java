package com.lojinhasystem.system.resources.dto;

import com.lojinhasystem.system.entities.UsuarioPJ;

public class UsuarioPJResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String rua;
    private String bairro;
    private Integer numero;
    private String telefone;
    private String cnpj;

    public UsuarioPJResponseDTO() {
    }

    public UsuarioPJResponseDTO(UsuarioPJ usuarioPJ) {
        this.id = usuarioPJ.getId();
        this.nome = usuarioPJ.getNome();
        this.email = usuarioPJ.getEmail();
        this.rua = usuarioPJ.getRua();
        this.bairro = usuarioPJ.getBairro();
        this.numero = usuarioPJ.getNumero();
        this.telefone = usuarioPJ.getTelefone();
        this.cnpj = usuarioPJ.getCnpj();
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

    public String getCnpj() {
        return cnpj;
    }
}