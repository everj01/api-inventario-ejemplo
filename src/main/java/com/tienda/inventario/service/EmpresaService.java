package com.tienda.inventario.service;

import com.tienda.inventario.dto.*;
import com.tienda.inventario.model.Empresa;
import com.tienda.inventario.model.Usuario;
import com.tienda.inventario.repository.EmpresaRepository;
import com.tienda.inventario.repository.ProductoRepository;
import com.tienda.inventario.repository.UsuarioRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

/** Todo lo que hace el usuario de soporte (SUPERADMIN). */
@Service
public class EmpresaService {

    private final EmpresaRepository repo;
    private final UsuarioRepository usuarioRepo;
    private final ProductoRepository productoRepo;
    private final UsuarioService usuarioService;

    public EmpresaService(EmpresaRepository repo, UsuarioRepository usuarioRepo,
                          ProductoRepository productoRepo, UsuarioService usuarioService) {
        this.repo = repo;
        this.usuarioRepo = usuarioRepo;
        this.productoRepo = productoRepo;
        this.usuarioService = usuarioService;
    }

    public List<EmpresaResponse> listar() {
        return repo.findAllByOrderByNombreAsc().stream().map(this::conConteos).toList();
    }

    public EmpresaResponse obtener(Long id) {
        return conConteos(entidad(id));
    }

    private Empresa entidad(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));
    }

    private EmpresaResponse conConteos(Empresa e) {
        return EmpresaResponse.from(e, usuarioRepo.countByEmpresaId(e.getId()), productoRepo.countByEmpresaId(e.getId()));
    }

    /** Crea la empresa y su primer administrador en una sola operación. */
    public EmpresaResponse crear(EmpresaAltaRequest req) {
        if (req.admin().rol() != Usuario.Rol.ADMIN) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El primer usuario de una empresa debe ser administrador");
        }
        Empresa e = new Empresa();
        e.setNombre(req.empresa().nombre());
        e.setRuc(req.empresa().ruc());
        e.setActivo(true);
        Empresa guardada = repo.save(e);

        usuarioService.guardar(guardada.getId(), req.admin());
        return conConteos(guardada);
    }

    public EmpresaResponse actualizar(Long id, EmpresaRequest req) {
        Empresa e = entidad(id);
        e.setNombre(req.nombre());
        e.setRuc(req.ruc());
        e.setActivo(req.activo());
        return conConteos(repo.save(e));
    }

    public List<UsuarioResponse> usuarios(Long empresaId) {
        entidad(empresaId);
        return usuarioService.listar(empresaId);
    }

    public List<UsuarioResponse> soporte() {
        return usuarioRepo.findByRol(Usuario.Rol.SUPERADMIN).stream().map(UsuarioResponse::from).toList();
    }
}
