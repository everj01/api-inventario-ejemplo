package com.tienda.inventario.controller;

import com.tienda.inventario.dto.*;
import com.tienda.inventario.security.Sesion;
import com.tienda.inventario.service.ProductoService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** La empresa nunca viaja en la URL: sale del token. */
@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    private final ProductoService service;

    public ProductoController(ProductoService service) {
        this.service = service;
    }

    @GetMapping
    public List<ProductoResponse> listar(@RequestParam(name = "buscar", required = false) String buscar) {
        return service.listar(Sesion.empresaId(), buscar);
    }

    @GetMapping("/stock-bajo")
    public List<ProductoResponse> stockBajo() {
        return service.stockBajo(Sesion.empresaId());
    }

    /** El escáner manda aquí lo que leyó de la cámara: uuid del QR o identificador del código de barras. */
    @GetMapping("/codigo/{codigo}")
    public ProductoResponse porCodigo(@PathVariable("codigo") String codigo) {
        return service.porCodigo(Sesion.empresaId(), codigo);
    }

    @GetMapping("/{id}")
    public ProductoResponse obtener(@PathVariable("id") Long id) {
        return service.obtener(Sesion.empresaId(), id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProductoResponse crear(@Valid @RequestBody ProductoRequest req) {
        return service.crear(Sesion.empresaId(), req);
    }

    @PutMapping("/{id}")
    public ProductoResponse actualizar(@PathVariable("id") Long id, @Valid @RequestBody ProductoRequest req) {
        return service.actualizar(Sesion.empresaId(), id, req);
    }

    @PatchMapping("/{id}/stock")
    public ProductoResponse moverStock(@PathVariable("id") Long id, @Valid @RequestBody MovimientoStockRequest req) {
        return service.moverStock(Sesion.empresaId(), id, req.cantidad());
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        service.eliminar(Sesion.empresaId(), id);
    }
}
