package com.lojinhasystem.system.resources.dto;

import com.lojinhasystem.system.entities.Categoria;

public class CategoriaResponseDTO {

    private Long id;
    private String nome;
    private String descricao;

    public CategoriaResponseDTO() {
    }

    public CategoriaResponseDTO(Long id, String nome, String descricao) {
        this.id = id;
        this.nome = nome;
        this.descricao = descricao;
    }

    public CategoriaResponseDTO(Categoria categoria) {
        this.id = categoria.getId();
        this.nome = categoria.getNome();
        this.descricao = categoria.getDescricao();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getDescricao() {
        return descricao;
    }
}