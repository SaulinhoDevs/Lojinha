package com.lojinhasystem.system.resources.dto;

import java.util.List;

public class ResumoLojaDTO {

    private String nomeLoja;
    private Double totalVendasMes;
    private Long quantidadeVendasMes;
    private Double totalDividasClientes;
    private List<ProdutoEstoqueBaixoDTO> produtosEstoqueBaixo;
    private List<ProdutoMaisVendidoDTO> topProdutosVendidos;

    public ResumoLojaDTO() {
    }

    public ResumoLojaDTO(String nomeLoja, Double totalVendasMes, Long quantidadeVendasMes,
                         Double totalDividasClientes, List<ProdutoEstoqueBaixoDTO> produtosEstoqueBaixo,
                         List<ProdutoMaisVendidoDTO> topProdutosVendidos) {
        this.nomeLoja = nomeLoja;
        this.totalVendasMes = totalVendasMes;
        this.quantidadeVendasMes = quantidadeVendasMes;
        this.totalDividasClientes = totalDividasClientes;
        this.produtosEstoqueBaixo = produtosEstoqueBaixo;
        this.topProdutosVendidos = topProdutosVendidos;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public Double getTotalVendasMes() {
        return totalVendasMes;
    }

    public Long getQuantidadeVendasMes() {
        return quantidadeVendasMes;
    }

    public Double getTotalDividasClientes() {
        return totalDividasClientes;
    }

    public List<ProdutoEstoqueBaixoDTO> getProdutosEstoqueBaixo() {
        return produtosEstoqueBaixo;
    }

    public List<ProdutoMaisVendidoDTO> getTopProdutosVendidos() {
        return topProdutosVendidos;
    }

    public record ProdutoEstoqueBaixoDTO(String nome, Integer estoque) {
    }

    public record ProdutoMaisVendidoDTO(String nome, Long quantidadeVendida) {
    }
}