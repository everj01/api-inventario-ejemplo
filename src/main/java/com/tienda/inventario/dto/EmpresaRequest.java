package com.tienda.inventario.dto;

import jakarta.validation.constraints.*;

public record EmpresaRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @Size(max = 20, message = "RUC demasiado largo") String ruc,
        boolean activo) {
}
