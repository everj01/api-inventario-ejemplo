package com.tienda.inventario.security;

import com.tienda.inventario.model.Usuario;

/** Los datos del usuario que viajan dentro del token y que quedan disponibles en cada request. */
public record UsuarioAutenticado(Long id, String username, String nombre, Usuario.Rol rol, Long empresaId) {

    public boolean esSuperadmin() {
        return rol == Usuario.Rol.SUPERADMIN;
    }
}
