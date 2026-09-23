package com.banco.client.service;

import com.banco.client.api.*;
import com.banco.client.domain.*;
import com.banco.client.messaging.*;
import java.util.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClientService {
    private final ClientRepository repo;
    private final ClientEventPublisher events;
    private final PasswordEncoder encoder;

    public ClientService(ClientRepository r, ClientEventPublisher e, PasswordEncoder p) {
        repo = r;
        events = e;
        encoder = p;
    }

    public List<ClientResponse> all() {
        return repo.findAll().stream().map(this::dto).toList();
    }

    public ClientResponse one(Long id) {
        return dto(get(id));
    }

    @Transactional
    public ClientResponse create(ClientRequest r) {
        if (repo.existsByIdentification(r.identificacion()))
            throw new ApiException(409, "La identificación ya existe");
        var c = new Client();
        apply(c, r);
        repo.save(c);
        events.publish(c, "CREATED");
        return dto(c);
    }

    @Transactional
    public ClientResponse update(Long id, ClientRequest r) {
        var c = get(id);
        if (!c.getIdentification().equals(r.identificacion()))
            throw new ApiException(400, "La identificación no se puede cambiar");
        apply(c, r);
        events.publish(c, "UPDATED");
        return dto(c);
    }

    @Transactional
    public void delete(Long id) {
        var c = get(id);
        repo.delete(c);
        events.publish(c, "DELETED");
    }

    private Client get(Long id) {
        return repo.findById(id).orElseThrow(() -> new ApiException(404, "Cliente no encontrado"));
    }

    private void apply(Client c, ClientRequest r) {
        c.setName(r.nombres());
        c.setIdentification(r.identificacion());
        c.setAddress(r.direccion());
        c.setPhone(r.telefono());
        c.setClientId(r.identificacion());
        c.setPasswordHash(encoder.encode(r.contrasena()));
        c.setActive(r.estado());
    }

    private ClientResponse dto(Client c) {
        return new ClientResponse(c.getId(), c.getName(), c.getAddress(), c.getPhone(), "********",
                c.getIdentification(), c.isActive());
    }
}
