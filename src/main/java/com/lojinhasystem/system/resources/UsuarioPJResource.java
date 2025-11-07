package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.entities.UsuarioPF;
import com.lojinhasystem.system.entities.UsuarioPJ;
import com.lojinhasystem.system.services.UsuarioPJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/usuarios/pj")
public class UsuarioPJResource {

    @Autowired
    private UsuarioPJService usuarioPJService;

    @GetMapping
    public ResponseEntity<List<UsuarioPJ>> findALl() {
        List<UsuarioPJ> usuarioPJ = usuarioPJService.findAll();
        return ResponseEntity.ok().body(usuarioPJ);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioPJ> findById(@PathVariable Long id) {
        UsuarioPJ usuarioPJ = usuarioPJService.findById(id);
        return ResponseEntity.ok().body(usuarioPJ);
    }

    @PostMapping
    public ResponseEntity<UsuarioPJ> insert(@RequestBody UsuarioPJ obj) {
        UsuarioPJ usuarioPJ = usuarioPJService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(obj.getId()).toUri();
        return ResponseEntity.created(uri).body(obj);
    }
}