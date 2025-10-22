package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.entities.UsuarioPJ;
import com.lojinhasystem.system.services.UsuarioPJService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

}