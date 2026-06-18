package com.orderflow.controller;

import com.orderflow.model.Contem;
import com.orderflow.service.ContemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contem")
public class ContemController {

    @Autowired
    private ContemService contemService;

    @GetMapping
    public List<Contem> listarTodos() {
        return contemService.listarTodos();
    }

    @PostMapping
    public Contem salvar(@RequestBody Contem contem) {
        return contemService.salvar(contem);
    }
    @GetMapping("/{id}")
    public Contem buscarPorId(@PathVariable Integer id) {
        return contemService.buscarPorId(id);
    }

    @GetMapping("/pedido/{idPedido}")
    public List<Contem> listarPorPedido(@PathVariable Integer idPedido) {
        return contemService.listarPorPedido(idPedido);
    }

    @PutMapping("/{id}")
    public Contem atualizar(@PathVariable Integer id, @RequestBody Contem contem) {
        contem.setIdContem(id);
        return contemService.salvar(contem);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Integer id) {
        contemService.excluir(id);
    }
}