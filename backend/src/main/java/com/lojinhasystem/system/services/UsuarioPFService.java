package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.UsuarioPF;
import com.lojinhasystem.system.repositories.UsuarioPFRepository;
import com.lojinhasystem.system.resources.dto.UsuarioPFRequestDTO;
import com.lojinhasystem.system.resources.dto.UsuarioPFResponseDTO;
import com.lojinhasystem.system.services.exceptions.DatabaseException;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioPFService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioPFRepository usuarioPFRepository;

    public List<UsuarioPFResponseDTO> findAll() {
        return usuarioPFRepository.findAll()
                .stream()
                .map(UsuarioPFResponseDTO::new)
                .toList();
    }

    public UsuarioPFResponseDTO findById(Long id) {
        UsuarioPF usuarioPF = findEntityById(id);
        return new UsuarioPFResponseDTO(usuarioPF);
    }

    public UsuarioPFResponseDTO insert(UsuarioPFRequestDTO dto) {
        UsuarioPF usuarioPF = new UsuarioPF();
        copyDtoToEntity(dto, usuarioPF);
        usuarioPF = usuarioPFRepository.save(usuarioPF);
        return new UsuarioPFResponseDTO(usuarioPF);
    }

    public UsuarioPFResponseDTO update(Long id, UsuarioPFRequestDTO dto) {
        UsuarioPF entity = findEntityById(id);
        updateData(entity, dto);
        entity = usuarioPFRepository.save(entity);
        return new UsuarioPFResponseDTO(entity);
    }

    public void delete(Long id) {
        try {
            UsuarioPF usuarioPF = findEntityById(id);
            usuarioPFRepository.delete(usuarioPF);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private UsuarioPF findEntityById(Long id) {
        return usuarioPFRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private void updateData(UsuarioPF entity, UsuarioPFRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setRua(dto.getRua());
        entity.setBairro(dto.getBairro());
        entity.setNumero(dto.getNumero());
        entity.setTelefone(dto.getTelefone());
        entity.setCpf(dto.getCpf());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
    }

    private void copyDtoToEntity(UsuarioPFRequestDTO dto, UsuarioPF entity) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        entity.setRua(dto.getRua());
        entity.setBairro(dto.getBairro());
        entity.setNumero(dto.getNumero());
        entity.setTelefone(dto.getTelefone());
        entity.setCpf(dto.getCpf());
    }
}