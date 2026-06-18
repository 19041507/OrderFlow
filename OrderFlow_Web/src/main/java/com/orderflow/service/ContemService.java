package com.orderflow.service;

import com.orderflow.model.Contem;
import com.orderflow.model.Pedido;
import com.orderflow.model.Produto;
import com.orderflow.repository.ContemRepository;
import com.orderflow.repository.PedidoRepository;
import com.orderflow.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContemService {

    @Autowired
    private ContemRepository contemRepository;

    @Autowired
    private ProdutoRepository produtoRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    public Contem salvar(Contem contem) {
        if (contem.getIdPedido() == null) {
            throw new RuntimeException("Informe o idPedido");
        }
        if (contem.getIdProduto() == null) {
            throw new RuntimeException("Informe o idProduto");
        }
        if (contem.getQuantidade() == null || contem.getQuantidade() <= 0) {
            throw new RuntimeException("A quantidade deve ser maior que zero");
        }

        pedidoRepository.findById(contem.getIdPedido())
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        Produto produto = produtoRepository.findById(contem.getIdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        Contem itemAnterior = null;
        if (contem.getIdContem() != null) {
            itemAnterior = contemRepository.findById(contem.getIdContem()).orElse(null);
        }

        int estoqueDisponivel = produto.getEstoque() == null ? 0 : produto.getEstoque();
        if (itemAnterior != null && itemAnterior.getIdProduto().equals(contem.getIdProduto())) {
            estoqueDisponivel += itemAnterior.getQuantidade();
        }

        if (estoqueDisponivel < contem.getQuantidade()) {
            throw new RuntimeException("Estoque insuficiente. Disponível: " + estoqueDisponivel);
        }

        contem.setSubtotal((produto.getPreco() == null ? 0.0 : produto.getPreco()) * contem.getQuantidade());
        produto.setEstoque(estoqueDisponivel - contem.getQuantidade());
        produtoRepository.save(produto);

        Contem contemSalvo = contemRepository.save(contem);
        recalcularValorTotal(contemSalvo.getIdPedido());

        return contemSalvo;
    }

    public List<Contem> listarTodos() {
        return contemRepository.findAll();
    }

    public Contem buscarPorId(Integer id) {
        return contemRepository.findById(id).orElse(null);
    }

    public List<Contem> listarPorPedido(Integer idPedido) {
        return contemRepository.findByIdPedido(idPedido);
    }

    public void excluir(Integer id) {
        Contem contem = contemRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Item não encontrado"));

        Produto produto = produtoRepository.findById(contem.getIdProduto())
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setEstoque((produto.getEstoque() == null ? 0 : produto.getEstoque()) + contem.getQuantidade());
        produtoRepository.save(produto);
        contemRepository.deleteById(id);
        recalcularValorTotal(contem.getIdPedido());
    }

    private void recalcularValorTotal(Integer idPedido) {
        List<Contem> itens = contemRepository.findByIdPedido(idPedido);

        double total = itens.stream()
                .mapToDouble(item -> item.getSubtotal() != null ? item.getSubtotal() : 0.0)
                .sum();

        Pedido pedido = pedidoRepository.findById(idPedido)
                .orElseThrow(() -> new RuntimeException("Pedido não encontrado"));

        pedido.setValorTotal(total);
        pedidoRepository.save(pedido);
    }
}
