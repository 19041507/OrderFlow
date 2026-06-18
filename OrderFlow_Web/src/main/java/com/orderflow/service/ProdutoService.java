package com.orderflow.service;

import com.orderflow.model.Produto;
import com.orderflow.repository.ProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {

    @Autowired
    private ProdutoRepository produtoRepository;

    // Salvar produto
    public Produto salvar(Produto produto) {
        produto.setIdProduto(null);
        if (produto.getEstoque() == null) {
            produto.setEstoque(0);
        }
        if (produto.getPreco() == null) {
            produto.setPreco(0.0);
        }
        return produtoRepository.save(produto);
    }

    // Listar todos
    public List<Produto> listarTodos() {
        return produtoRepository.findAll();
    }

    // Buscar por ID
    public Optional<Produto> buscarPorId(Integer id) {
        return produtoRepository.findById(id);
    }

    // Deletar
    public void deletar(Integer id) {
        produtoRepository.deleteById(id);
    }

    // Atualizar produto
    public Produto atualizar(Integer id, Produto produtoAtualizado) {

        Produto produto = produtoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        produto.setNome(produtoAtualizado.getNome());
        produto.setPreco(produtoAtualizado.getPreco());
        produto.setEstoque(produtoAtualizado.getEstoque());

        return produtoRepository.save(produto);
    }

    // Regra de negócio: verificar estoque
    public boolean possuiEstoque(Integer idProduto, Integer quantidade) {

        Produto produto = produtoRepository.findById(idProduto)
                .orElseThrow(() -> new RuntimeException("Produto não encontrado"));

        return produto.getEstoque() >= quantidade;
    }
}