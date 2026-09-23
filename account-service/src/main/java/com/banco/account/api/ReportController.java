package com.banco.account.api;

import com.banco.account.service.ReportService;
import java.time.LocalDate;
import java.util.List;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reportes")
@CrossOrigin
public class ReportController {
    private final ReportService service;

    public ReportController(ReportService s) {
        service = s;
    }

    @GetMapping
    public List<ReportResponse> report(@RequestParam(name = "identificacion", required = false) String identificacion,
            @RequestParam(name = "fechaInicio", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam(name = "fechaFin", required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return service.report(identificacion, fechaInicio, fechaFin);
    }
}
