package com.tienda.inventario.controller;

import com.tienda.inventario.dto.UsuarioRequest;
import com.tienda.inventario.dto.UsuarioResponse;
import com.tienda.inventario.security.Sesion;
import com.tienda.inventario.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/** Solo ADMIN (ver SecurityConfig). */
@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService service;

    public UsuarioController(UsuarioService service) {
        this.service = service;
    }

    @GetMapping
    public List<UsuarioResponse> listar() {
        return service.listar(Sesion.empresaId());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public UsuarioResponse crear(@Valid @RequestBody UsuarioRequest req) {
        return service.crear(Sesion.empresaId(), req);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable("id") Long id) {
        service.eliminar(Sesion.empresaId(), id, Sesion.actual().id());
    }
}
