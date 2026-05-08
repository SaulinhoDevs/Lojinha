package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.UsuarioPJ;
import com.lojinhasystem.system.repositories.UsuarioPJRepository;
import com.lojinhasystem.system.resources.dto.UsuarioPJRequestDTO;
import com.lojinhasystem.system.resources.dto.UsuarioPJResponseDTO;
import com.lojinhasystem.system.services.exceptions.DatabaseException;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UsuarioPJService {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UsuarioPJRepository usuarioPJRepository;

    public List<UsuarioPJResponseDTO> findAll() {
        return usuarioPJRepository.findAll()
                .stream()
                .map(UsuarioPJResponseDTO::new)
                .toList();
    }

    public UsuarioPJResponseDTO findById(Long id) {
        UsuarioPJ usuarioPJ = findEntityById(id);
        return new UsuarioPJResponseDTO(usuarioPJ);
    }

    public UsuarioPJResponseDTO insert(UsuarioPJRequestDTO dto) {
        UsuarioPJ usuarioPJ = new UsuarioPJ();
        copyDtoToEntity(dto, usuarioPJ);
        usuarioPJ = usuarioPJRepository.save(usuarioPJ);
        return new UsuarioPJResponseDTO(usuarioPJ);
    }

    public UsuarioPJResponseDTO update(Long id, UsuarioPJRequestDTO dto) {
        UsuarioPJ entity = findEntityById(id);
        updateData(entity, dto);
        entity = usuarioPJRepository.save(entity);
        return new UsuarioPJResponseDTO(entity);
    }

    public void delete(Long id) {
        try {
            UsuarioPJ usuarioPJ = findEntityById(id);
            usuarioPJRepository.delete(usuarioPJ);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException(e.getMessage());
        }
    }

    private UsuarioPJ findEntityById(Long id) {
        return usuarioPJRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    private void updateData(UsuarioPJ entity, UsuarioPJRequestDTO dto) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setRua(dto.getRua());
        entity.setBairro(dto.getBairro());
        entity.setNumero(dto.getNumero());
        entity.setTelefone(dto.getTelefone());
        entity.setCnpj(dto.getCnpj());

        if (dto.getSenha() != null && !dto.getSenha().isBlank()) {
            entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        }
    }

    private void copyDtoToEntity(UsuarioPJRequestDTO dto, UsuarioPJ entity) {
        entity.setNome(dto.getNome());
        entity.setEmail(dto.getEmail());
        entity.setSenha(passwordEncoder.encode(dto.getSenha()));
        entity.setRua(dto.getRua());
        entity.setBairro(dto.getBairro());
        entity.setNumero(dto.getNumero());
        entity.setTelefone(dto.getTelefone());
        entity.setCnpj(dto.getCnpj());
    }
}