package com.lojinhasystem.system.services;

import com.lojinhasystem.system.entities.ItemPedido;
import com.lojinhasystem.system.entities.Venda;
import com.lojinhasystem.system.repositories.VendaRepository;
import com.lojinhasystem.system.services.exceptions.ResourceNotFoundException;
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
        return venda.orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Venda insert(Venda obj) {
        return vendaRepository.save(obj);
    }

    public void delete(Long id) {
        if (vendaRepository.existsById(id)) {
            vendaRepository.deleteById(id);
        } else {
            throw new ResourceNotFoundException(id);
        }
    }

    public Double calcularTotalVenda(Venda venda) {
        double soma = venda.getItens().stream()
                .mapToDouble(ItemPedido::getSubTotal)
                .sum();
        return soma + venda.getFrete() - venda.getDesconto();
    }

}