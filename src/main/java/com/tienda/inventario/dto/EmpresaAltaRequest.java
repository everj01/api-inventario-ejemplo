package com.tienda.inventario.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

/** Alta completa desde soporte: la empresa y su primer administrador, en un solo paso. */
public record EmpresaAltaRequest(
        @NotNull @Valid EmpresaRequest empresa,
        @NotNull @Valid UsuarioRequest admin) {
}
