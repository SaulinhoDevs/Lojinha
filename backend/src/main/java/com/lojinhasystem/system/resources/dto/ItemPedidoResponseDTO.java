package com.lojinhasystem.system.resources.dto;

public class ItemPedidoResponseDTO {

    private Long produtoId;
    private String produtoNome;
    private Integer quantidade;
    private Double precoUnitario;
    private Double subTotal;

    public ItemPedidoResponseDTO() {
    }

    public ItemPedidoResponseDTO(Long produtoId, String produtoNome, Integer quantidade, Double precoUnitario, Double subTotal) {
        this.produtoId = produtoId;
        this.produtoNome = produtoNome;
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
        this.subTotal = subTotal;
    }

    public Long getProdutoId() {
        return produtoId;
    }

    public String getProdutoNome() {
        return produtoNome;
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public Double getSubTotal() {
        return subTotal;
    }
}