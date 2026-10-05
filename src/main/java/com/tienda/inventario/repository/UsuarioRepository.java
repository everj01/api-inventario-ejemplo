package com.tienda.inventario.repository;

import com.tienda.inventario.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);
    boolean existsByUsername(String username);
    List<Usuario> findByEmpresaIdOrderByNombreAsc(Long empresaId);
    List<Usuario> findByRol(Usuario.Rol rol);
    long countByEmpresaId(Long empresaId);
}
