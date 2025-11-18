package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.entities.UsuarioPF;
import com.lojinhasystem.system.entities.Venda;
import com.lojinhasystem.system.services.VendaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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

    @PostMapping
    public ResponseEntity<Venda> insert(@RequestBody Venda obj) {
        Venda venda = vendaService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(venda.getId()).toUri();
        return ResponseEntity.created(uri).body(venda);
    }
}