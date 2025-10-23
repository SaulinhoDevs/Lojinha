package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.entities.Venda;
import com.lojinhasystem.system.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "/vendas")
public class VendaResource {
    
    @Autowired
    private VendaService vendaService;

    @GetMapping
    public ResponseEntity<List<Venda>> findAll() {
        List<Venda> vendas = vendaService.findAll();
        return ResponseEntity.ok().body(vendas);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Venda> findById(@PathVariable Long id) {
        Venda venda = vendaService.findById(id);
        return ResponseEntity.ok().body(venda);
    }
    
}