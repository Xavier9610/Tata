package com.banco.account.service;

import com.banco.account.api.*;
import com.banco.account.domain.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AccountService {
    private final AccountRepository accounts;
    private final ClientProjectionRepository clients;

    public AccountService(AccountRepository a, ClientProjectionRepository c) {
        accounts = a;
        clients = c;
    }

    public List<AccountResponse> all() {
        return accounts.findAll().stream().map(this::dto).toList();
    }

    public AccountResponse one(String n) {
        return dto(get(n));
    }

    @Transactional
    public AccountResponse create(AccountRequest r) {
        if (accounts.existsById(r.numeroCuenta()))
            throw new ApiException(409, "La cuenta ya existe");
        var client = clients.findById(r.clienteIdentificacion())
                .orElseThrow(() -> new ApiException(409, "Cliente aún no sincronizado; reintente en unos segundos"));
        if (!client.isActive())
            throw new ApiException(422, "El cliente está inactivo");
        var a = new Account();
        a.setNumber(r.numeroCuenta());
        a.setType(r.tipo());
        a.setOpeningBalance(r.saldoInicial());
        a.setCurrentBalance(r.saldoInicial());
        a.setActive(r.estado());
        a.setClientId(r.clienteIdentificacion());
        return dto(accounts.save(a));
    }

    @Transactional
    public AccountResponse update(String n, AccountRequest r) {
        var a = get(n);
        if (!n.equals(r.numeroCuenta()))
            throw new ApiException(400, "El número de cuenta no se puede cambiar");
        a.setType(r.tipo());
        a.setActive(r.estado());
        return dto(a);
    }

    @Transactional
    public void delete(String n) {
        var a = get(n);
        if (a.getCurrentBalance().compareTo(a.getOpeningBalance()) != 0)
            throw new ApiException(409, "No se puede eliminar una cuenta con movimientos");
        accounts.delete(a);
    }

    public Account get(String n) {
        return accounts.findById(n).orElseThrow(() -> new ApiException(404, "Cuenta no encontrada"));
    }

    private AccountResponse dto(Account a) {
        var nombre = clients.findById(a.getClientId()).map(ClientProjection::getName).orElse(a.getClientId());
        return new AccountResponse(a.getNumber(), a.getType(), a.getOpeningBalance(), a.isActive(), nombre);
    }
}
