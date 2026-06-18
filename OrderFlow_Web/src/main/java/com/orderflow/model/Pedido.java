package com.orderflow.model;

import jakarta.persistence.*;

@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPedido;

    private Double valorTotal;
    private String statusAtual;
    private Integer idCliente;

    public Pedido() {
    }

    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
    public Double getValorTotal() { return valorTotal; }
    public void setValorTotal(Double valorTotal) { this.valorTotal = valorTotal; }
    public String getStatusAtual() { return statusAtual; }
    public void setStatusAtual(String statusAtual) { this.statusAtual = statusAtual; }
    public Integer getIdCliente() { return idCliente; }
    public void setIdCliente(Integer idCliente) { this.idCliente = idCliente; }
}
