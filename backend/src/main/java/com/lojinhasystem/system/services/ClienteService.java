package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Cliente;
import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.repositories.ClienteRepository;
import com.lojinhasystem.system.services.exceptions.DatabaseException;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private VendaService vendaService;

    @Autowired
    private UsuarioAutenticadoService usuarioAutenticadoService;

    public List<Cliente> findAll() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        return clienteRepository.findByUsuarioId(usuarioLogado.getId());
    }

    public Cliente findById(Long id) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        return clienteRepository.findByIdAndUsuarioId(id, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Cliente update(Long id, Cliente obj) {
        Cliente entity = findById(id);
        updateData(entity, obj);
        return clienteRepository.save(entity);
    }

    private void updateData(Cliente entity, Cliente obj) {
        entity.setNome(obj.getNome());
        entity.setDivida(obj.getDivida());
        entity.setTelefone(obj.getTelefone());
        entity.setRua(obj.getRua());
        entity.setBairro(obj.getBairro());
        entity.setNumero(obj.getNumero());
    }

    public Cliente insert(Cliente cliente) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        cliente.setUsuario(usuarioLogado);
        return clienteRepository.save(cliente);
    }

    public void delete(Long id) {
        try {
            Cliente cliente = findById(id);
            clienteRepository.delete(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }
}