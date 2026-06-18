package com.orderflow.controller;

import com.orderflow.model.Pedido;
import com.orderflow.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    @Autowired
    private PedidoService pedidoService;

    @PostMapping
    public Pedido criar(@RequestBody Pedido pedido) {
        return pedidoService.criarPedido(pedido);
    }

    @GetMapping
    public List<Pedido> listarTodos() {
        return pedidoService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Pedido> buscarPorId(@PathVariable Integer id) {
        return pedidoService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Pedido atualizarStatus(@PathVariable Integer id, @RequestParam String novoStatus) {
        return pedidoService.atualizarStatus(id, novoStatus);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        pedidoService.deletar(id);
    }
}
