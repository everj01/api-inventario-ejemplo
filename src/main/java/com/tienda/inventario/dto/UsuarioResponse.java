package com.tienda.inventario.dto;

import com.tienda.inventario.model.Usuario;

public record UsuarioResponse(Long id, String uuid, String nombre, String username,
                              Usuario.Rol rol, boolean activo, Long empresaId, String empresaNombre) {

    public static UsuarioResponse from(Usuario u) {
        return new UsuarioResponse(
                u.getId(), u.getUuid(), u.getNombre(), u.getUsername(), u.getRol(), u.isActivo(),
                u.getEmpresa() != null ? u.getEmpresa().getId() : null,
                u.getEmpresa() != null ? u.getEmpresa().getNombre() : null);
    }
}
