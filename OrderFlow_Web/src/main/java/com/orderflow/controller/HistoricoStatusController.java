package com.orderflow.controller;

import com.orderflow.model.HistoricoStatus;
import com.orderflow.service.HistoricoStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/historico-status")
public class HistoricoStatusController {

    @Autowired
    private HistoricoStatusService historicoStatusService;

    @PostMapping
    public HistoricoStatus registrar(@RequestBody HistoricoStatus historico) {
        return historicoStatusService.registrarHistorico(historico);
    }

    @GetMapping
    public List<HistoricoStatus> listarTodos() {
        return historicoStatusService.listarTodos();
    }

    @GetMapping("/{id}")
    public Optional<HistoricoStatus> buscarPorId(@PathVariable Integer id) {
        return historicoStatusService.buscarPorId(id);
    }

    @PutMapping("/{id}")
    public HistoricoStatus atualizar(@PathVariable Integer id, @RequestBody HistoricoStatus historico) {
        return historicoStatusService.atualizar(id, historico);
    }

    @DeleteMapping("/{id}")
    public void deletar(@PathVariable Integer id) {
        historicoStatusService.deletar(id);
    }
}
