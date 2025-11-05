package com.lojinhasystem.system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.lojinhasystem.system.entities.pk.ItemPedidoPK;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "tb_item_pedido")
public class ItemPedido implements Serializable {

    private static final long serialVersionUID = 1L;

    @EmbeddedId
    private ItemPedidoPK id = new ItemPedidoPK();

    private Integer quantidade;
    private Double precoUnitario;

    public ItemPedido() {
    }

    public ItemPedido(Venda venda, Produto produto, Integer quantidade, Double precoUnitario) {
        id.setVenda(venda);
        id.setProduto(produto);
        this.quantidade = quantidade;
        this.precoUnitario = precoUnitario;
    }

    @JsonIgnore
    public Venda getVenda() {
        return id.getVenda();
    }

    public void setVenda(Venda venda) {
        id.setVenda(venda);
    }

    public Produto getProduto() {
        return id.getProduto();
    }

    public void setProduto(Produto produto) {
        id.setProduto(produto);
    }

    public Integer getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(Integer quantidade) {
        this.quantidade = quantidade;
    }

    public Double getPrecoUnitario() {
        return precoUnitario;
    }

    public void setPrecoUnitario(Double precoUnitario) {
        this.precoUnitario = precoUnitario;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido that = (ItemPedido) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}