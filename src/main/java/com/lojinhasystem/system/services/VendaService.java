package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.Cliente;
import com.lojinhasystem.system.entities.Venda;
import com.lojinhasystem.system.entities.enums.StatusVenda;
import com.lojinhasystem.system.repositories.ClienteRepository;
import com.lojinhasystem.system.repositories.VendaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VendaService {

    @Autowired
    private VendaRepository vendaRepository;

    public List<Venda> findAll() {
        return vendaRepository.findAll();
    }

    public Venda findById(Long id) {
        Optional<Venda> venda = vendaRepository.findById(id);
        return venda.get();
    }
}