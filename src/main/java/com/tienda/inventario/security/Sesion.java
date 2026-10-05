package com.tienda.inventario.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.server.ResponseStatusException;

/** Atajo para saber quien esta haciendo la peticion actual. */
public final class Sesion {

    private Sesion() {}

    public static UsuarioAutenticado actual() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof UsuarioAutenticado u)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Sesión no válida");
        }
        return u;
    }

    /** La empresa sobre la que trabaja la peticion. El superadmin debe indicarla. */
    public static Long empresaId() {
        UsuarioAutenticado u = actual();
        if (u.empresaId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El usuario de soporte debe indicar la empresa (usa el panel de soporte)");
        }
        return u.empresaId();
    }
}
