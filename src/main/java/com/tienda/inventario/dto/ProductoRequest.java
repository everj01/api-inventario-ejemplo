package com.tienda.inventario.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

/** El frontend no manda uuid ni empresa: los pone la entidad y el token. */
public record ProductoRequest(
        @NotBlank(message = "El identificador es obligatorio")
        @Size(max = 40, message = "Máximo 40 caracteres")
        @Pattern(regexp = "[A-Za-z0-9._-]+", message = "Solo letras, números, punto, guion y guion bajo")
        String identificador,

        @NotBlank(message = "El nombre es obligatorio") String nombre,
        String categoria,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.0", message = "El precio no puede ser negativo") BigDecimal precio,

        @NotNull(message = "El stock es obligatorio")
        @Min(value = 0, message = "El stock no puede ser negativo") Integer stock,

        @NotNull(message = "El stock mínimo es obligatorio")
        @Min(value = 0, message = "El stock mínimo no puede ser negativo") Integer stockMinimo) {
}
