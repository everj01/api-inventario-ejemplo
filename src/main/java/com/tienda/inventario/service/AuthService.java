package com.tienda.inventario.service;

import com.tienda.inventario.dto.LoginRequest;
import com.tienda.inventario.dto.LoginResponse;
import com.tienda.inventario.model.Usuario;
import com.tienda.inventario.repository.UsuarioRepository;
import com.tienda.inventario.security.JwtService;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private final UsuarioRepository repo;
    private final PasswordEncoder encoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository repo, PasswordEncoder encoder, JwtService jwtService) {
        this.repo = repo;
        this.encoder = encoder;
        this.jwtService = jwtService;
    }

    /** Login de empresa: ADMIN o EMPLEADO. El soporte no entra por aquí. */
    public LoginResponse login(LoginRequest req) {
        Usuario u = verificar(req);
        if (u.getRol() == Usuario.Rol.SUPERADMIN) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
        }
        if (u.getEmpresa() == null || !u.getEmpresa().isActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La empresa está desactivada. Contacta a soporte.");
        }
        return respuesta(u);
    }

    /** Login de soporte técnico: solo SUPERADMIN, en una ruta aparte. */
    public LoginResponse loginSoporte(LoginRequest req) {
        Usuario u = verificar(req);
        if (u.getRol() != Usuario.Rol.SUPERADMIN) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos");
        }
        return respuesta(u);
    }

    private Usuario verificar(LoginRequest req) {
        Usuario u = repo.findByUsername(req.username())
                .filter(x -> encoder.matches(req.password(), x.getPassword()))
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.UNAUTHORIZED, "Usuario o contraseña incorrectos"));
        if (!u.isActivo()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Tu usuario está desactivado");
        }
        return u;
    }

    private LoginResponse respuesta(Usuario u) {
        return new LoginResponse(
                jwtService.generar(u), u.getId(), u.getUuid(), u.getNombre(), u.getUsername(), u.getRol(),
                u.getEmpresa() != null ? u.getEmpresa().getId() : null,
                u.getEmpresa() != null ? u.getEmpresa().getNombre() : null);
    }
}
