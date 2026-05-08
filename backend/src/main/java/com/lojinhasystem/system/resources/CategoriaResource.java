package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.resources.dto.CategoriaRequestDTO;
import com.lojinhasystem.system.resources.dto.CategoriaResponseDTO;
import com.lojinhasystem.system.services.CategoriaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping
    public ResponseEntity<List<CategoriaResponseDTO>> findAll() {
        List<CategoriaResponseDTO> categorias = categoriaService.findAll();
        return ResponseEntity.ok().body(categorias);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CategoriaResponseDTO> findById(@PathVariable Long id) {
        CategoriaResponseDTO categoria = categoriaService.findById(id);
        return ResponseEntity.ok().body(categoria);
    }

    @PostMapping
    public ResponseEntity<CategoriaResponseDTO> insert(@RequestBody CategoriaRequestDTO obj) {
        CategoriaResponseDTO categoria = categoriaService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(categoria.getId())
                .toUri();
        return ResponseEntity.created(uri).body(categoria);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<CategoriaResponseDTO> update(@PathVariable Long id, @RequestBody CategoriaRequestDTO obj) {
        CategoriaResponseDTO categoria = categoriaService.update(id, obj);
        return ResponseEntity.ok().body(categoria);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        categoriaService.delete(id);
        return ResponseEntity.noContent().build();
    }
}