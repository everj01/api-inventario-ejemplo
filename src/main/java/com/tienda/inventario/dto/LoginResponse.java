package com.tienda.inventario.dto;

import com.tienda.inventario.model.Usuario;

/** Lo que recibe el navegador al iniciar sesion: el token y los datos minimos del usuario. */
public record LoginResponse(
        String token,
        Long id,
        String uuid,
        String nombre,
        String username,
        Usuario.Rol rol,
        Long empresaId,
        String empresaNombre) {
}
