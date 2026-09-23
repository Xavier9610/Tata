package com.banco.account.api;

import com.banco.account.service.AccountService;
import jakarta.validation.Valid;
import java.util.*;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cuentas")
@CrossOrigin
public class AccountController {
    private final AccountService service;

    public AccountController(AccountService s) {
        service = s;
    }

    @GetMapping
    public List<AccountResponse> all() {
        return service.all();
    }

    @GetMapping("/{number}")
    public AccountResponse one(@PathVariable("number") String number) {
        return service.one(number);
    }

    @PostMapping
    public ResponseEntity<AccountResponse> create(@Valid @RequestBody AccountRequest r) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(r));
    }

    @PutMapping("/{number}")
    public AccountResponse update(@PathVariable("number") String number, @Valid @RequestBody AccountRequest r) {
        return service.update(number, r);
    }

    @DeleteMapping("/{number}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("number") String number) {
        service.delete(number);
    }
}
