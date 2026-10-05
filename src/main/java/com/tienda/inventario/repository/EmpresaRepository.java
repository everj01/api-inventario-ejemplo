package com.tienda.inventario.repository;

import com.tienda.inventario.model.Empresa;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface EmpresaRepository extends JpaRepository<Empresa, Long> {
    List<Empresa> findAllByOrderByNombreAsc();
    boolean existsByRuc(String ruc);
}
