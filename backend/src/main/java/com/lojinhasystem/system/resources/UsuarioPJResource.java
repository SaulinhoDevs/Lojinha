package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.resources.dto.UsuarioPJRequestDTO;
import com.lojinhasystem.system.resources.dto.UsuarioPJResponseDTO;
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
    public ResponseEntity<List<UsuarioPJResponseDTO>> findAll() {
        List<UsuarioPJResponseDTO> usuariosPJ = usuarioPJService.findAll();
        return ResponseEntity.ok().body(usuariosPJ);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<UsuarioPJResponseDTO> findById(@PathVariable Long id) {
        UsuarioPJResponseDTO usuarioPJ = usuarioPJService.findById(id);
        return ResponseEntity.ok().body(usuarioPJ);
    }

    @PostMapping
    public ResponseEntity<UsuarioPJResponseDTO> insert(@RequestBody UsuarioPJRequestDTO obj) {
        UsuarioPJResponseDTO usuarioPJ = usuarioPJService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(usuarioPJ.getId())
                .toUri();
        return ResponseEntity.created(uri).body(usuarioPJ);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<UsuarioPJResponseDTO> update(@PathVariable Long id, @RequestBody UsuarioPJRequestDTO obj) {
        UsuarioPJResponseDTO usuarioPJ = usuarioPJService.update(id, obj);
        return ResponseEntity.ok().body(usuarioPJ);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        usuarioPJService.delete(id);
        return ResponseEntity.noContent().build();
    }
}