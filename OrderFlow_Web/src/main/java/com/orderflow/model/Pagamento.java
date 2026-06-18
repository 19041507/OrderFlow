package com.orderflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "pagamento")
public class Pagamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPagamento;

    private String metodoPagamento;
    private LocalDateTime dataPagamento;
    private Double valor;
    private String statusPagamento;
    private Integer idPedido;

    public Pagamento() {
    }

    public Integer getIdPagamento() { return idPagamento; }
    public void setIdPagamento(Integer idPagamento) { this.idPagamento = idPagamento; }
    public String getMetodoPagamento() { return metodoPagamento; }
    public void setMetodoPagamento(String metodoPagamento) { this.metodoPagamento = metodoPagamento; }
    public LocalDateTime getDataPagamento() { return dataPagamento; }
    public void setDataPagamento(LocalDateTime dataPagamento) { this.dataPagamento = dataPagamento; }
    public Double getValor() { return valor; }
    public void setValor(Double valor) { this.valor = valor; }
    public String getStatusPagamento() { return statusPagamento; }
    public void setStatusPagamento(String statusPagamento) { this.statusPagamento = statusPagamento; }
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
}
