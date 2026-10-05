package com.tienda.inventario.service;

import com.tienda.inventario.dto.UsuarioRequest;
import com.tienda.inventario.dto.UsuarioResponse;
import com.tienda.inventario.model.Empresa;
import com.tienda.inventario.model.Usuario;
import com.tienda.inventario.repository.EmpresaRepository;
import com.tienda.inventario.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class UsuarioService {

    private final UsuarioRepository repo;
    private final EmpresaRepository empresaRepo;
    private final PasswordEncoder encoder;

    public UsuarioService(UsuarioRepository repo, EmpresaRepository empresaRepo, PasswordEncoder encoder) {
        this.repo = repo;
        this.empresaRepo = empresaRepo;
        this.encoder = encoder;
    }

    public List<UsuarioResponse> listar(Long empresaId) {
        return repo.findByEmpresaIdOrderByNombreAsc(empresaId).stream().map(UsuarioResponse::from).toList();
    }

    public UsuarioResponse crear(Long empresaId, UsuarioRequest req) {
        if (req.rol() == Usuario.Rol.SUPERADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "El rol de soporte no se crea desde aquí");
        }
        return guardar(empresaId, req);
    }

    /** Usado tambien por soporte al dar de alta una empresa con su primer administrador. */
    public UsuarioResponse guardar(Long empresaId, UsuarioRequest req) {
        if (repo.existsByUsername(req.username())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ese nombre de usuario ya está en uso");
        }
        Empresa empresa = empresaRepo.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));

        Usuario u = new Usuario();
        u.setNombre(req.nombre());
        u.setUsername(req.username());
        u.setPassword(encoder.encode(req.password()));
        u.setRol(req.rol());
        u.setEmpresa(empresa);
        return UsuarioResponse.from(repo.save(u));
    }

    public void eliminar(Long empresaId, Long id, Long idSolicitante) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        if (u.getEmpresa() == null || !u.getEmpresa().getId().equals(empresaId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado");
        }
        if (u.getId().equals(idSolicitante)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No puedes eliminar tu propio usuario");
        }
        repo.delete(u);
    }

    public UsuarioResponse cambiarEstado(Long id, boolean activo) {
        Usuario u = repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        u.setActivo(activo);
        return UsuarioResponse.from(repo.save(u));
    }
}
