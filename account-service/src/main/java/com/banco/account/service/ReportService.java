package com.banco.account.service;

import com.banco.account.api.*;
import com.banco.account.domain.*;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
public class ReportService {
    private final AccountRepository accounts;
    private final MovementRepository movements;
    private final ClientProjectionRepository clients;

    public ReportService(AccountRepository a, MovementRepository m, ClientProjectionRepository c) {
        accounts = a;
        movements = m;
        clients = c;
    }

    public List<ReportResponse> report(String identificacion, LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio != null && fechaFin != null && fechaFin.isBefore(fechaInicio))
            throw new ApiException(400, "La fecha final debe ser posterior o igual a la inicial");
        var titulares = StringUtils.hasText(identificacion)
                ? clients.findById(identificacion.trim()).map(List::of).orElseGet(List::of)
                : clients.findAllByOrderByNameAsc();
        return titulares.stream().map(c -> build(c, fechaInicio, fechaFin)).toList();
    }

    private ReportResponse build(ClientProjection client, LocalDate fechaInicio, LocalDate fechaFin) {
        var list = accounts.findByClientIdOrderByNumber(client.getClientId());
        var byAccount = new HashMap<String, List<Movement>>();
        if (!list.isEmpty()) {
            Instant from = fechaInicio == null ? null : fechaInicio.atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant to = fechaFin == null ? null : fechaFin.plusDays(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            var accountNumbers = list.stream().map(Account::getNumber).collect(java.util.stream.Collectors.toSet());
            for (var m : movements.findAllByOrderByOccurredAtAscIdAsc()) {
                if (accountNumbers.contains(m.getAccountNumber()) && (from == null || !m.getOccurredAt().isBefore(from))
                        && (to == null || m.getOccurredAt().isBefore(to))) {
                    byAccount.computeIfAbsent(m.getAccountNumber(), x -> new ArrayList<>()).add(m);
                }
            }
        }
        return new ReportResponse(client.getClientId(), client.getName(), fechaInicio, fechaFin, list
                .stream().map(
                        a -> new ReportResponse.AccountReport(a.getNumber(), a.getType(), a.getOpeningBalance(),
                                a.getCurrentBalance(), a.isActive(),
                                byAccount.getOrDefault(a.getNumber(), List.of()).stream()
                                        .map(m -> new ReportResponse.MovementItem(m.getId(), m.getOccurredAt(),
                                                m.getType().name(), m.getAmount(), m.getBalanceAfter()))
                                        .toList()))
                .toList());
    }
}
