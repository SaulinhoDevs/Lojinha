package com.lojinhasystem.system.resources.dto;

import java.util.ArrayList;
import java.util.List;

public class VendaRequestDTO {

    private Double frete;
    private Double desconto;
    private Integer status;
    private Long clienteId;
    private List<ItemPedidoRequestDTO> itens = new ArrayList<>();

    public VendaRequestDTO() {
    }

    public Double getFrete() {
        return frete;
    }

    public void setFrete(Double frete) {
        this.frete = frete;
    }

    public Double getDesconto() {
        return desconto;
    }

    public void setDesconto(Double desconto) {
        this.desconto = desconto;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Long getClienteId() {
        return clienteId;
    }

    public void setClienteId(Long clienteId) {
        this.clienteId = clienteId;
    }

    public List<ItemPedidoRequestDTO> getItens() {
        return itens;
    }

    public void setItens(List<ItemPedidoRequestDTO> itens) {
        this.itens = itens;
    }
}