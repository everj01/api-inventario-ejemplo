package com.tienda.inventario.repository;

import com.tienda.inventario.model.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/** Todos los metodos reciben empresaId para no mezclar datos entre empresas. */
public interface ProductoRepository extends JpaRepository<Producto, Long> {

    List<Producto> findByEmpresaIdOrderByNombreAsc(Long empresaId);

    Optional<Producto> findByIdAndEmpresaId(Long id, Long empresaId);

    Optional<Producto> findByUuidAndEmpresaId(String uuid, Long empresaId);

    Optional<Producto> findByIdentificadorAndEmpresaId(String identificador, Long empresaId);

    boolean existsByIdentificadorAndEmpresaId(String identificador, Long empresaId);

    boolean existsByIdentificadorAndEmpresaIdAndIdNot(String identificador, Long empresaId, Long id);

    long countByEmpresaId(Long empresaId);

    @Query("""
           SELECT p FROM Producto p
           WHERE p.empresa.id = :empresaId
             AND (LOWER(p.nombre) LIKE LOWER(CONCAT('%', :q, '%'))
                  OR LOWER(p.identificador) LIKE LOWER(CONCAT('%', :q, '%')))
           ORDER BY p.nombre ASC
           """)
    List<Producto> buscar(@Param("empresaId") Long empresaId, @Param("q") String q);

    @Query("SELECT p FROM Producto p WHERE p.empresa.id = :empresaId AND p.stock <= p.stockMinimo ORDER BY p.stock ASC")
    List<Producto> findStockBajo(@Param("empresaId") Long empresaId);
}
