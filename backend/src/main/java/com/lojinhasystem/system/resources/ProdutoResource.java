package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.resources.dto.ProdutoRequestDTO;
import com.lojinhasystem.system.resources.dto.ProdutoResponseDTO;
import com.lojinhasystem.system.services.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<ProdutoResponseDTO>> findAll() {
        List<ProdutoResponseDTO> produtos = produtoService.findAll();
        return ResponseEntity.ok().body(produtos);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> findById(@PathVariable Long id) {
        ProdutoResponseDTO produto = produtoService.findById(id);
        return ResponseEntity.ok().body(produto);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponseDTO> insert(@RequestBody ProdutoRequestDTO obj) {
        ProdutoResponseDTO produto = produtoService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(produto.getId())
                .toUri();
        return ResponseEntity.created(uri).body(produto);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ProdutoResponseDTO> update(@PathVariable Long id, @RequestBody ProdutoRequestDTO obj) {
        ProdutoResponseDTO produto = produtoService.update(id, obj);
        return ResponseEntity.ok().body(produto);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        produtoService.delete(id);
        return ResponseEntity.noContent().build();
    }
}