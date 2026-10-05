package com.tienda.inventario.dto;

import com.tienda.inventario.model.Usuario;
import jakarta.validation.constraints.*;

public record UsuarioRequest(
        @NotBlank(message = "El nombre es obligatorio") String nombre,
        @NotBlank(message = "El usuario es obligatorio") String username,
        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres") String password,
        @NotNull(message = "Elige un rol") Usuario.Rol rol) {
}
