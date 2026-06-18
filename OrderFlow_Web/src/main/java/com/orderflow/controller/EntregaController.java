package com.orderflow.controller;

import com.orderflow.model.Entrega;
import com.orderflow.service.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @PostMapping
    public Entrega registrarEnvio(@RequestBody Entrega entrega) {
        return entregaService.registrarEnvio(entrega);
    }

    @GetMapping
    public List<Entrega> listarTodos() {
        return entregaService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Entrega> buscarPorId(@PathVariable Integer id) {
        return entregaService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Entrega atualizar(@PathVariable Integer id, @RequestBody Entrega entrega) {
        return entregaService.atualizar(id, entrega);
    }

    @PutMapping("/{id}/confirmar")
    public Entrega confirmarEntrega(@PathVariable Integer id) {
        return entregaService.confirmarEntrega(id);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        entregaService.deletar(id);
    }
}
