package com.banco.client.api;

/** La contraseña se muestra enmascarada: nunca se expone su valor ni hash. */
public record ClientResponse(Long id, String nombres, String direccion, String telefono, String contrasena,
        String identificacion, boolean estado) {
}
