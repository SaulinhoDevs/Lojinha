package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.entities.UsuarioPF;
import com.lojinhasystem.system.services.UsuarioPFService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}