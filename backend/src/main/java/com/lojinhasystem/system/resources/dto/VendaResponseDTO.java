package com.lojinhasystem.system.resources.dto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class VendaResponseDTO {

    private Long id;
    private LocalDate dataVenda;
    private Double frete;
    private Double desconto;
    private Integer status;
    private Long clienteId;
    private String clienteNome;
    private Double total;
    private List<ItemPedidoResponseDTO> itens = new ArrayList<>();

    public VendaResponseDTO() {
    }

    public VendaResponseDTO(
            Long id,
            LocalDate dataVenda,
            Double frete,
            Double desconto,
            Integer status,
            Long clienteId,
            String clienteNome,
            Double total,
            List<ItemPedidoResponseDTO> itens
    ) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.frete = frete;
        this.desconto = desconto;
        this.status = status;
        this.clienteId = clienteId;
        this.clienteNome = clienteNome;
        this.total = total;
        this.itens = itens;
    }

    public Long getId() {
        return id;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public Double getFrete() {
        return frete;
    }

    public Double getDesconto() {
        return desconto;
    }

    public Integer getStatus() {
        return status;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public String getClienteNome() {
        return clienteNome;
    }

    public Double getTotal() {
        return total;
    }

    public List<ItemPedidoResponseDTO> getItens() {
        return itens;
    }
}