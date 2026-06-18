package com.orderflow.service;

import com.orderflow.model.Cliente;
import com.orderflow.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    // Salvar cliente
    public Cliente salvar(Cliente cliente) {
        cliente.setIdCliente(null);
        return clienteRepository.save(cliente);
    }

    // Listar todos
    public List<Cliente> listarTodos() {
        return clienteRepository.findAll();
    }

    // Buscar por ID
    public Optional<Cliente> buscarPorId(Integer id) {
        return clienteRepository.findById(id);
    }

    // Deletar
    public void deletar(Integer id) {
        clienteRepository.deleteById(id);
    }

    // Atualizar
    public Cliente atualizar(Integer id, Cliente clienteAtualizado) {

        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente não encontrado"));

        cliente.setNome(clienteAtualizado.getNome());
        cliente.setEmail(clienteAtualizado.getEmail());
        cliente.setTelefone(clienteAtualizado.getTelefone());
        cliente.setCep(clienteAtualizado.getCep());
        cliente.setLogradouro(clienteAtualizado.getLogradouro());
        cliente.setNumero(clienteAtualizado.getNumero());
        cliente.setComplemento(clienteAtualizado.getComplemento());
        cliente.setBairro(clienteAtualizado.getBairro());
        cliente.setCidade(clienteAtualizado.getCidade());
        cliente.setEstado(clienteAtualizado.getEstado());

        return clienteRepository.save(cliente);
    }
}