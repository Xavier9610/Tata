package com.banco.client.api;

import jakarta.validation.constraints.*;

/** Contrato de negocio solicitado para alta y edición de clientes. */
public record ClientRequest(@NotBlank @Size(max = 120) String nombres, @NotBlank @Size(max = 200) String direccion,
        @NotBlank @Size(max = 30) String telefono, @NotBlank @Size(min = 4, max = 72) String contrasena,
        @NotBlank @Size(max = 40) String identificacion, @NotNull Boolean estado) {
}
