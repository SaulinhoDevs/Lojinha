package com.lojinhasystem.system.resources.dto;

import com.lojinhasystem.system.entities.Produto;

import java.util.ArrayList;
import java.util.List;

public class ProdutoResponseDTO {

    private Long id;
    private String nome;
    private Integer estoque;
    private Double precoVenda;
    private Double precoCompra;
    private List<CategoriaResponseDTO> categorias = new ArrayList<>();

    public ProdutoResponseDTO() {
    }

    public ProdutoResponseDTO(Produto produto) {
        this.id = produto.getId();
        this.nome = produto.getNome();
        this.estoque = produto.getEstoque();
        this.precoVenda = produto.getPrecoVenda();
        this.precoCompra = produto.getPrecoCompra();
        this.categorias = produto.getCategorias().stream()
                .map(CategoriaResponseDTO::new)
                .toList();
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public Integer getEstoque() {
        return estoque;
    }

    public Double getPrecoVenda() {
        return precoVenda;
    }

    public Double getPrecoCompra() {
        return precoCompra;
    }

    public List<CategoriaResponseDTO> getCategorias() {
        return categorias;
    }
}