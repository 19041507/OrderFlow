package com.orderflow.service;

import com.orderflow.model.HistoricoStatus;
import com.orderflow.model.Pagamento;
import com.orderflow.model.Pedido;
import com.orderflow.repository.HistoricoStatusRepository;
import com.orderflow.repository.PagamentoRepository;
import com.orderflow.repository.PedidoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PagamentoService {

    @Autowired
    private PagamentoRepository pagamentoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private HistoricoStatusRepository historicoStatusRepository;

    public Pagamento registrarPagamento(Pagamento pagamento) {
        if (pagamento.getIdPedido() == null) {
            throw new RuntimeException("Informe o idPedido");
        }

        Pedido pedido = pedidoRepository.findById(pagamento.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pagamento.setIdPagamento(null);
        pagamento.setStatusPagamento("APROVADO");
        pagamento.setDataPagamento(LocalDateTime.now());
        if (pagamento.getValor() == null) {
            pagamento.setValor(pedido.getValorTotal() == null ? 0.0 : pedido.getValorTotal());
        }

        pedido.setStatusAtual("EM_PREPARO");
        pedidoRepository.save(pedido);
        registrarHistorico(pagamento.getIdPedido(), "EM_PREPARO");

        return pagamentoRepository.save(pagamento);
    }

    public List<Pagamento> listarTodos() {
        return pagamentoRepository.findAll();
    }

    public Optional<Pagamento> buscarPorId(Integer id) {
        return pagamentoRepository.findById(id);
    }

    public Pagamento atualizar(Integer id, Pagamento pagamentoAtualizado) {
        Pagamento pagamento = pagamentoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pagamento não encontrado"));

        pagamento.setMetodoPagamento(pagamentoAtualizado.getMetodoPagamento());
        pagamento.setValor(pagamentoAtualizado.getValor());
        pagamento.setStatusPagamento(pagamentoAtualizado.getStatusPagamento());
        pagamento.setIdPedido(pagamentoAtualizado.getIdPedido());

        return pagamentoRepository.save(pagamento);
    }

    public void deletar(Integer id) {
        pagamentoRepository.deleteById(id);
    }

    private void registrarHistorico(Integer idPedido, String status) {
        HistoricoStatus historico = new HistoricoStatus();
        historico.setIdPedido(idPedido);
        historico.setStatus(status);
        historico.setDataHora(LocalDateTime.now());
        historicoStatusRepository.save(historico);
    }
}
