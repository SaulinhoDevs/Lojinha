package com.lojinhasystem.system.resources;

import com.lojinhasystem.system.resources.dto.ClienteRequestDTO;
import com.lojinhasystem.system.resources.dto.ClienteResponseDTO;
import com.lojinhasystem.system.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

    @Autowired
    private ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteResponseDTO>> findAll() {
        List<ClienteResponseDTO> clientes = clienteService.findAll();
        return ResponseEntity.ok().body(clientes);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<ClienteResponseDTO> findById(@PathVariable Long id) {
        ClienteResponseDTO cliente = clienteService.findById(id);
        return ResponseEntity.ok().body(cliente);
    }

    @PostMapping
    public ResponseEntity<ClienteResponseDTO> insert(@RequestBody ClienteRequestDTO obj) {
        ClienteResponseDTO cliente = clienteService.insert(obj);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(cliente.getId())
                .toUri();
        return ResponseEntity.created(uri).body(cliente);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<ClienteResponseDTO> update(@PathVariable Long id, @RequestBody ClienteRequestDTO obj) {
        ClienteResponseDTO cliente = clienteService.update(id, obj);
        return ResponseEntity.ok().body(cliente);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        clienteService.delete(id);
        return ResponseEntity.noContent().build();
    }
}