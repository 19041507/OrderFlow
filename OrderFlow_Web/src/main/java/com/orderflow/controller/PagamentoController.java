package com.orderflow.controller;

import com.orderflow.model.Pagamento;
import com.orderflow.service.PagamentoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pagamentos")
public class PagamentoController {

    @Autowired
    private PagamentoService pagamentoService;

    @PostMapping
    public Pagamento registrar(@RequestBody Pagamento pagamento) {
        return pagamentoService.registrarPagamento(pagamento);
    }

    @GetMapping
    public List<Pagamento> listarTodos() {
        return pagamentoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Pagamento> buscarPorId(@PathVariable Integer id) {
        return pagamentoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Pagamento atualizar(@PathVariable Integer id, @RequestBody Pagamento pagamento) {
        return pagamentoService.atualizar(id, pagamento);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        pagamentoService.deletar(id);
    }
}
