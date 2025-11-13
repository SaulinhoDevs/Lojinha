package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.entities.UsuarioPF;
import com.lojinhasystem.system.services.UsuarioPFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/usuarios/pf")
public class UsuarioPFResource {

    @Autowired
    private UsuarioPFService usuarioPFService;

    @GetMapping
    public ResponseEntity<List<UsuarioPF>> findAll() {
        List<UsuarioPF> usuarioPF = usuarioPFService.findAll();
        return ResponseEntity.ok().body(usuarioPF);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioPF> findById(@PathVariable Long id) {
        UsuarioPF usuarioPF = usuarioPFService.findById(id);
        return ResponseEntity.ok().body(usuarioPF);
    }

    @PostMapping
    public ResponseEntity<UsuarioPF> insert(@RequestBody UsuarioPF obj) {
        UsuarioPF usuarioPF = usuarioPFService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioPFService.delete(id);
        return ResponseEntity.noContent().build();
    }
}