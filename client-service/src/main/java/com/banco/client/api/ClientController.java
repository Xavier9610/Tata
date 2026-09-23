package com.banco.client.api;

import com.banco.client.service.ClientService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/clientes")
@CrossOrigin
public class ClientController {
    private final ClientService service;

    public ClientController(ClientService s) {
        service = s;
    }

    @GetMapping
    public List<ClientResponse> all() {
        return service.all();
    }

    @GetMapping("/{id}")
    public ClientResponse one(@PathVariable("id") Long id) {
        return service.one(id);
    }

    @PostMapping
    public ResponseEntity<ClientResponse> create(@Valid @RequestBody ClientRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{id}")
    public ClientResponse update(@PathVariable("id") Long id, @Valid @RequestBody ClientRequest r) {
        return service.update(id, r);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }
}
