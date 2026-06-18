package com.orderflow.service;

import com.orderflow.model.Entrega;
import com.orderflow.model.HistoricoStatus;
import com.orderflow.model.Pedido;
import com.orderflow.repository.EntregaRepository;
import com.orderflow.repository.HistoricoStatusRepository;
import com.orderflow.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    public Entrega registrarEnvio(Entrega entrega) {
        if (entrega.getIdPedido() == null) {
            throw new RuntimeException("Informe o idPedido");
        }

        Pedido pedido = pedidoRepository.findById(entrega.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        entrega.setIdEntrega(null);
        entrega.setStatusEntrega("EM_TRANSITO");
        entrega.setDataEnvio(LocalDateTime.now());
        entrega.setHoraEnvio(LocalDateTime.now());

        pedido.setStatusAtual("ENVIADO");
        pedidoRepository.save(pedido);
        registrarHistorico(entrega.getIdPedido(), "ENVIADO");

        return entregaRepository.save(entrega);
    }

    public Entrega confirmarEntrega(Integer idEntrega) {
        Entrega entrega = entregaRepository.findById(idEntrega)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        entrega.setStatusEntrega("ENTREGUE");
        entrega.setDataEntrega(LocalDateTime.now());
        entrega.setHoraEntrega(LocalDateTime.now());

        Pedido pedido = pedidoRepository.findById(entrega.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatusAtual("ENTREGUE");
        pedidoRepository.save(pedido);
        registrarHistorico(entrega.getIdPedido(), "ENTREGUE");

        return entregaRepository.save(entrega);
    }

    public List<Entrega> listarTodos() {
        return entregaRepository.findAll();
    }

    public Optional<Entrega> buscarPorId(Integer id) {
        return entregaRepository.findById(id);
    }

    public Entrega atualizar(Integer id, Entrega entregaAtualizada) {
        Entrega entrega = entregaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Entrega não encontrada"));

        entrega.setCodigoRastreio(entregaAtualizada.getCodigoRastreio());
        entrega.setTransporte(entregaAtualizada.getTransporte());
        entrega.setStatusEntrega(entregaAtualizada.getStatusEntrega());
        entrega.setIdPedido(entregaAtualizada.getIdPedido());

        return entregaRepository.save(entrega);
    }

    public void deletar(Integer id) {
        entregaRepository.deleteById(id);
    }

    private void registrarHistorico(Integer idPedido, String status) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setIdPedido(idPedido);
        historico.setStatus(status);
        historico.setDataHora(LocalDateTime.now());
        historicoStatusRepository.save(historico);
    }
}
