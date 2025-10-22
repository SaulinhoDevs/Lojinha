package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.UsuarioPJ;
import com.lojinhasystem.system.repositories.UsuarioPJRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioPJService {

    @Autowired
    private UsuarioPJRepository usuarioPJRepository;

    public List<UsuarioPJ> findAll() {
        return usuarioPJRepository.findAll();
    }

    public UsuarioPJ findById(Long id) {
        Optional<UsuarioPJ> usuarioPJ = usuarioPJRepository.findById(id);
        return usuarioPJ.get();
    }

}