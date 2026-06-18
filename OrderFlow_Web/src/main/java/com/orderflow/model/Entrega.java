package com.orderflow.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "entrega")
public class Entrega {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEntrega;

    private String codigoRastreio;
    private LocalDateTime dataEntrega;
    private String transporte;
    private LocalDateTime horaEntrega;
    private LocalDateTime horaEnvio;
    private LocalDateTime dataEnvio;
    private String statusEntrega;
    private Integer idPedido;

    public Entrega() {
    }

    public Integer getIdEntrega() { return idEntrega; }
    public void setIdEntrega(Integer idEntrega) { this.idEntrega = idEntrega; }
    public String getCodigoRastreio() { return codigoRastreio; }
    public void setCodigoRastreio(String codigoRastreio) { this.codigoRastreio = codigoRastreio; }
    public LocalDateTime getDataEntrega() { return dataEntrega; }
    public void setDataEntrega(LocalDateTime dataEntrega) { this.dataEntrega = dataEntrega; }
    public String getTransporte() { return transporte; }
    public void setTransporte(String transporte) { this.transporte = transporte; }
    public LocalDateTime getHoraEntrega() { return horaEntrega; }
    public void setHoraEntrega(LocalDateTime horaEntrega) { this.horaEntrega = horaEntrega; }
    public LocalDateTime getHoraEnvio() { return horaEnvio; }
    public void setHoraEnvio(LocalDateTime horaEnvio) { this.horaEnvio = horaEnvio; }
    public LocalDateTime getDataEnvio() { return dataEnvio; }
    public void setDataEnvio(LocalDateTime dataEnvio) { this.dataEnvio = dataEnvio; }
    public String getStatusEntrega() { return statusEntrega; }
    public void setStatusEntrega(String statusEntrega) { this.statusEntrega = statusEntrega; }
    public Integer getIdPedido() { return idPedido; }
    public void setIdPedido(Integer idPedido) { this.idPedido = idPedido; }
}
