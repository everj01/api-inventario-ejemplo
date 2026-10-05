package com.tienda.inventario.dto;

import jakarta.validation.constraints.NotNull;

/** cantidad positiva = entrada de mercaderia, negativa = salida/venta. */
public record MovimientoStockRequest(@NotNull(message = "Indica la cantidad") Integer cantidad) {
}
