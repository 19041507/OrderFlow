package com.orderflow.controller;

import com.orderflow.model.Cliente;
import com.orderflow.service.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @PostMapping
    public Cliente salvar(@RequestBody Cliente cliente) {
        return clienteService.salvar(cliente);
    }

    @GetMapping
    public List<Cliente> listarTodos() {
        return clienteService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<Cliente> buscarPorId(@PathVariable Integer id) {
        return clienteService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public Cliente atualizar(@PathVariable Integer id, @RequestBody Cliente cliente) {
        return clienteService.atualizar(id, cliente);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        clienteService.deletar(id);
    }
}