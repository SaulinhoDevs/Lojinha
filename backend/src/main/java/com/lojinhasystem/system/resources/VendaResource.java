package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.resources.dto.VendaRequestDTO;
import com.lojinhasystem.system.resources.dto.VendaResponseDTO;
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
    public ResponseEntity<List<VendaResponseDTO>> findAll() {
        List<VendaResponseDTO> vendas = vendaService.findAll();
        return ResponseEntity.ok().body(vendas);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<VendaResponseDTO> findById(@PathVariable Long id) {
        VendaResponseDTO venda = vendaService.findById(id);
        return ResponseEntity.ok().body(venda);
    }

    @PostMapping
    public ResponseEntity<VendaResponseDTO> insert(@RequestBody VendaRequestDTO obj) {
        VendaResponseDTO venda = vendaService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(venda.getId())
                .toUri();
        return ResponseEntity.created(uri).body(venda);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<VendaResponseDTO> update(@PathVariable Long id, @RequestBody VendaRequestDTO obj) {
        VendaResponseDTO venda = vendaService.update(id, obj);
        return ResponseEntity.ok().body(venda);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        vendaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}