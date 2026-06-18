package com.orderflow.controller;

import com.orderflow.model.Produto;
import com.orderflow.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    @Autowired
    private ProdutoService produtoService;

    @PostMapping
    public Produto salvar(@RequestBody Produto produto) {
        return produtoService.salvar(produto);
    }

    @GetMapping
    public List<Produto> listarTodos() {
        return produtoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Produto> buscarPorId(@PathVariable Integer id) {
        return produtoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Produto atualizar(@PathVariable Integer id, @RequestBody Produto produto) {
        return produtoService.atualizar(id, produto);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        produtoService.deletar(id);
    }
}