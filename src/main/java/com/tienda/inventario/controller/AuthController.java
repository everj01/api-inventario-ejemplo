package com.tienda.inventario.controller;

import com.tienda.inventario.dto.LoginRequest;
import com.tienda.inventario.dto.LoginResponse;
import com.tienda.inventario.security.Sesion;
import com.tienda.inventario.security.UsuarioAutenticado;
import com.tienda.inventario.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService service;

    public AuthController(AuthService service) {
        this.service = service;
    }

    /** Puerta de las empresas. */
    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest req) {
        return service.login(req);
    }

    /** Puerta de soporte técnico, separada a propósito. */
    @PostMapping("/soporte/login")
    public LoginResponse loginSoporte(@Valid @RequestBody LoginRequest req) {
        return service.loginSoporte(req);
    }

    /** Sirve al frontend para saber si el token sigue vivo al recargar la página. */
    @GetMapping("/yo")
    public UsuarioAutenticado yo() {
        return Sesion.actual();
    }
}
