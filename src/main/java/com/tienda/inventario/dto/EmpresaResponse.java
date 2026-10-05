package com.tienda.inventario.dto;

import com.tienda.inventario.model.Empresa;

public record EmpresaResponse(Long id, String uuid, String nombre, String ruc, boolean activo,
                              long usuarios, long productos) {

    public static EmpresaResponse from(Empresa e, long usuarios, long productos) {
        return new EmpresaResponse(e.getId(), e.getUuid(), e.getNombre(), e.getRuc(), e.isActivo(), usuarios, productos);
    }
}
