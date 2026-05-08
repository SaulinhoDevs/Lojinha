package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Cliente;
import com.lojinhasystem.system.entities.Usuario;
import com.lojinhasystem.system.repositories.ClienteRepository;
import com.lojinhasystem.system.resources.dto.ClienteRequestDTO;
import com.lojinhasystem.system.resources.dto.ClienteResponseDTO;
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
    private UsuarioAutenticadoService usuarioAutenticadoService;

    public List<ClienteResponseDTO> findAll() {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();
        return clienteRepository.findByUsuarioId(usuarioLogado.getId())
                .stream()
                .map(ClienteResponseDTO::new)
                .toList();
    }

    public ClienteResponseDTO findById(Long id) {
        Cliente cliente = findEntityById(id);
        return new ClienteResponseDTO(cliente);
    }

    public ClienteResponseDTO insert(ClienteRequestDTO dto) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        Cliente cliente = new Cliente();
        cliente.setNome(dto.getNome());
        cliente.setDivida(dto.getDivida());
        cliente.setTelefone(dto.getTelefone());
        cliente.setRua(dto.getRua());
        cliente.setBairro(dto.getBairro());
        cliente.setNumero(dto.getNumero());
        cliente.setUsuario(usuarioLogado);

        cliente = clienteRepository.save(cliente);
        return new ClienteResponseDTO(cliente);
    }

    public ClienteResponseDTO update(Long id, ClienteRequestDTO dto) {
        Cliente entity = findEntityById(id);
        updateData(entity, dto);
        entity = clienteRepository.save(entity);
        return new ClienteResponseDTO(entity);
    }

    public void delete(Long id) {
        try {
            Cliente cliente = findEntityById(id);
            clienteRepository.delete(cliente);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private void updateData(Cliente entity, ClienteRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setDivida(dto.getDivida());
        entity.setTelefone(dto.getTelefone());
        entity.setRua(dto.getRua());
        entity.setBairro(dto.getBairro());
        entity.setNumero(dto.getNumero());
    }

    private Cliente findEntityById(Long id) {
        Usuario usuarioLogado = usuarioAutenticadoService.getUsuarioLogado();

        return clienteRepository.findByIdAndUsuarioId(id, usuarioLogado.getId())
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}