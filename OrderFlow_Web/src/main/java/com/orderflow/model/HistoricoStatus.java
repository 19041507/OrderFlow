package com.orderflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "historico_status")
public class HistoricoStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idHistorico;

    private String status;
    private LocalDateTime dataHora;
    private Integer idPedido;

    public HistoricoStatus() {
    }

    public Integer getIdHistorico() { return idHistorico; }
    public void setIdHistorico(Integer idHistorico) { this.idHistorico = idHistorico; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
}
