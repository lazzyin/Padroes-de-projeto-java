package com.dio.padraoprojeto.service.impl;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.dio.padraoprojeto.model.*;
import com.dio.padraoprojeto.service.*;

public class ClienteServiceImpl implements ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    EnderecoRepository enderecoRepository;
    @Autowired
       private ViaCepService viaCepService;

    @Override
    public Iterable<Cliente> buscarTodos() {
        // TODO Auto-generated method stub
        return clienteRepository.findAll();
    }

    @Override
    public Cliente buscarPorId(Long id) {
       Optional<Cliente>cliente=clienteRepository.findById(id);
        return cliente.get();
    }

    @Override
    public void inserir(Cliente cliente) {
        salvarClienteComCep(cliente);
    }
    
    private void salvarClienteComCep(Cliente cliente) {
        String cep=cliente.getEndereco().getCep();
        Endereco endereco=enderecoRepository.findById(cep).orElseGet(() -> {
            Endereco novoEndereco = viaCepService.consultarCep(cep);
            enderecoRepository.save(novoEndereco);
            return novoEndereco;
        });
        cliente.setEndereco(endereco);
        clienteRepository.save(cliente);
    }

    @Override
    public void atualizar(Long id, Cliente cliente) {
        Optional<Cliente>clienteDb=clienteRepository.findById(id);
        if (clienteDb.isPresent()) {
            salvarClienteComCep(cliente);
        }
    }

    @Override
    public void deletar(Long id) {
    clienteRepository.deleteById(id);        
    }
    
}
