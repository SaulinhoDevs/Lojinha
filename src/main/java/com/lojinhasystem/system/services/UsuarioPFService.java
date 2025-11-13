package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.UsuarioPF;
import com.lojinhasystem.system.repositories.UsuarioPFRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioPFService {

    @Autowired
    private UsuarioPFRepository usuarioPFRepository;

    public List<UsuarioPF> findAll() {
        return usuarioPFRepository.findAll();
    }

    public UsuarioPF findById(Long id) {
        Optional<UsuarioPF> usuarioPF = usuarioPFRepository.findById(id);
        return usuarioPF.get();
    }

    public UsuarioPF insert(UsuarioPF usuarioPF) {
        return usuarioPFRepository.save(usuarioPF);
    }

    public void delete(Long id) {
        usuarioPFRepository.deleteById(id);
    }

    public UsuarioPF update(Long id, UsuarioPF obj) {
        UsuarioPF entity = usuarioPFRepository.getReferenceById(id);
        updateData(entity, obj);
        return usuarioPFRepository.save(entity);
    }

    private void updateData(UsuarioPF entity, UsuarioPF obj) {
        entity.setNome(obj.getNome());
        entity.setTelefone(obj.getTelefone());
        entity.setCpf(obj.getCpf());
    }
}