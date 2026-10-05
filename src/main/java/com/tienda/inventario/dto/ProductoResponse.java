package com.tienda.inventario.dto;

import com.tienda.inventario.model.Producto;
import java.math.BigDecimal;

public record ProductoResponse(Long id, String uuid, String identificador, String nombre,
                               String categoria, BigDecimal precio, Integer stock, Integer stockMinimo) {

    public static ProductoResponse from(Producto p) {
        return new ProductoResponse(p.getId(), p.getUuid(), p.getIdentificador(), p.getNombre(),
                p.getCategoria(), p.getPrecio(), p.getStock(), p.getStockMinimo());
    }
}
