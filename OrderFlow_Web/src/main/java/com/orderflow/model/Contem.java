package com.orderflow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "contem")
public class Contem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idContem;

    private Integer idPedido;
    private Integer idProduto;
    private Integer quantidade;
    private Double subtotal;
    private String obs;

    public Contem() {
    }

    public Integer getIdContem() { return idContem; }
    public void setIdContem(Integer idContem) { this.idContem = idContem; }
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
    public Integer getIdProduto() { return idProduto; }
    public void setIdProduto(Integer idProduto) { this.idProduto = idProduto; }
    public Integer getQuantidade() { return quantidade; }
    public void setQuantidade(Integer quantidade) { this.quantidade = quantidade; }
    public Double getSubtotal() { return subtotal; }
    public void setSubtotal(Double subtotal) { this.subtotal = subtotal; }
    public String getObs() { return obs; }
    public void setObs(String obs) { this.obs = obs; }
}
