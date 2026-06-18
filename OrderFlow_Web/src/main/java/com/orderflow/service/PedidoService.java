package com.orderflow.service;

import com.orderflow.model.HistoricoStatus;
import com.orderflow.model.Pedido;
import com.orderflow.repository.ClienteRepository;
import com.orderflow.repository.HistoricoStatusRepository;
import com.orderflow.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    public Pedido criarPedido(Pedido pedido) {
        if (pedido.getIdCliente() == null) {
            throw new RuntimeException("Informe o idCliente para criar o pedido");
        }

        clienteRepository.findById(pedido.getIdCliente())
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        pedido.setIdPedido(null);
        pedido.setStatusAtual("PENDENTE");
        pedido.setValorTotal(0.0);

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        registrarHistorico(pedidoSalvo.getIdPedido(), "PENDENTE");

        return pedidoSalvo;
    }

    public List<Pedido> listarTodos() {
        return pedidoRepository.findAll();
    }

    public Optional<Pedido> buscarPorId(Integer id) {
        return pedidoRepository.findById(id);
    }

    public Pedido atualizarStatus(Integer id, String novoStatus) {
        if (novoStatus == null || novoStatus.isBlank()) {
            throw new RuntimeException("Informe o novoStatus");
        }

        Pedido pedido = pedidoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setStatusAtual(novoStatus.trim().toUpperCase());
        Pedido pedidoAtualizado = pedidoRepository.save(pedido);
        registrarHistorico(id, pedidoAtualizado.getStatusAtual());

        return pedidoAtualizado;
    }

    public void deletar(Integer id) {
        pedidoRepository.deleteById(id);
    }

    private void registrarHistorico(Integer idPedido, String status) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setIdPedido(idPedido);
        historico.setStatus(status);
        historico.setDataHora(LocalDateTime.now());
        historicoStatusRepository.save(historico);
    }
}
