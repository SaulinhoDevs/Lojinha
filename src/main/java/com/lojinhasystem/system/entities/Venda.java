package com.lojinhasystem.system.entities;

import com.lojinhasystem.system.entities.enums.StatusVenda;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "tb_venda")
public class Venda implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate dataVenda;
    private Double frete;
    private Double desconto;

    private Integer status;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @OneToMany(mappedBy = "id.venda")
    private Set<ItemPedido> itens = new HashSet<>();

    public Venda() {
    }

    public Venda(Long id, LocalDate dataVenda, Double frete, Double desconto, StatusVenda status, Cliente cliente) {
        this.id = id;
        this.dataVenda = dataVenda;
        this.frete = frete;
        this.desconto = desconto;
        setStatus(status);
        this.cliente = cliente;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDataVenda() {
        return dataVenda;
    }

    public void setDataVenda(LocalDate dataVenda) {
        this.dataVenda = dataVenda;
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

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public StatusVenda getStatus() {
        return StatusVenda.valueOf(status);
    }

    public void setStatus(StatusVenda status) {
        if (status != null) {
            this.status = status.getCodigo();
        }
    }

    public Set<ItemPedido> getItens() {
        return itens;
    }

    public Double getTotal() {
        double soma = 0.0;
        for (ItemPedido x : itens) {
            soma += x.getSubTotal();
        }
        return soma + getFrete() - getDesconto();
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Venda venda = (Venda) o;
        return Objects.equals(id, venda.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}