package com.tienda.inventario.controller;

import com.tienda.inventario.dto.*;
import com.tienda.inventario.service.EmpresaService;
import com.tienda.inventario.service.ProductoService;
import com.tienda.inventario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Solo SUPERADMIN (ver SecurityConfig). Ve y administra TODAS las empresas. */
@RestController
@RequestMapping("/api/soporte")
public class SoporteController {

    private final EmpresaService empresaService;
    private final ProductoService productoService;
    private final UsuarioService usuarioService;

    public SoporteController(EmpresaService empresaService, ProductoService productoService,
                             UsuarioService usuarioService) {
        this.empresaService = empresaService;
        this.productoService = productoService;
        this.usuarioService = usuarioService;
    }

    @GetMapping("/empresas")
    public List<EmpresaResponse> empresas() {
        return empresaService.listar();
    }

    @GetMapping("/empresas/{id}")
    public EmpresaResponse empresa(@PathVariable("id") Long id) {
        return empresaService.obtener(id);
    }

    @PostMapping("/empresas")
    @ResponseStatus(HttpStatus.CREATED)
    public EmpresaResponse crearEmpresa(@Valid @RequestBody EmpresaAltaRequest req) {
        return empresaService.crear(req);
    }

    @PutMapping("/empresas/{id}")
    public EmpresaResponse actualizarEmpresa(@PathVariable("id") Long id, @Valid @RequestBody EmpresaRequest req) {
        return empresaService.actualizar(id, req);
    }

    @GetMapping("/empresas/{id}/usuarios")
    public List<UsuarioResponse> usuarios(@PathVariable("id") Long id) {
        return empresaService.usuarios(id);
    }

    @PostMapping("/empresas/{id}/usuarios")
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crearUsuario(@PathVariable("id") Long id, @Valid @RequestBody UsuarioRequest req) {
        return usuarioService.crear(id, req);
    }

    @PatchMapping("/usuarios/{id}/estado")
    public UsuarioResponse cambiarEstado(@PathVariable("id") Long id,
                                         @RequestParam(name = "activo") boolean activo) {
        return usuarioService.cambiarEstado(id, activo);
    }

    /** Soporte revisando el inventario de una empresa concreta. */
    @GetMapping("/empresas/{id}/productos")
    public List<ProductoResponse> productos(@PathVariable("id") Long id,
                                            @RequestParam(name = "buscar", required = false) String buscar) {
        return productoService.listar(id, buscar);
    }
}
