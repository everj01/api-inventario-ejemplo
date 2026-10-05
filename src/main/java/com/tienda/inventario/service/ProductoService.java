package com.tienda.inventario.service;

import com.tienda.inventario.dto.ProductoRequest;
import com.tienda.inventario.dto.ProductoResponse;
import com.tienda.inventario.model.Empresa;
import com.tienda.inventario.model.Producto;
import com.tienda.inventario.repository.EmpresaRepository;
import com.tienda.inventario.repository.ProductoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository repo;
    private final EmpresaRepository empresaRepo;

    public ProductoService(ProductoRepository repo, EmpresaRepository empresaRepo) {
        this.repo = repo;
        this.empresaRepo = empresaRepo;
    }

    public List<ProductoResponse> listar(Long empresaId, String buscar) {
        List<Producto> lista = (buscar == null || buscar.isBlank())
                ? repo.findByEmpresaIdOrderByNombreAsc(empresaId)
                : repo.buscar(empresaId, buscar.trim());
        return lista.stream().map(ProductoResponse::from).toList();
    }

    public List<ProductoResponse> stockBajo(Long empresaId) {
        return repo.findStockBajo(empresaId).stream().map(ProductoResponse::from).toList();
    }

    /** Busca por uuid (QR) o por identificador (código de barras). Lo usa el escáner. */
    public ProductoResponse porCodigo(Long empresaId, String codigo) {
        return repo.findByUuidAndEmpresaId(codigo, empresaId)
                .or(() -> repo.findByIdentificadorAndEmpresaId(codigo, empresaId))
                .map(ProductoResponse::from)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Ningún producto coincide con el código " + codigo));
    }

    public ProductoResponse obtener(Long empresaId, Long id) {
        return ProductoResponse.from(entidad(empresaId, id));
    }

    private Producto entidad(Long empresaId, Long id) {
        return repo.findByIdAndEmpresaId(id, empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Producto no encontrado"));
    }

    public ProductoResponse crear(Long empresaId, ProductoRequest req) {
        if (repo.existsByIdentificadorAndEmpresaId(req.identificador(), empresaId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese identificador");
        }
        Empresa empresa = empresaRepo.findById(empresaId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Empresa no encontrada"));

        Producto p = new Producto();
        p.setEmpresa(empresa);
        copiar(req, p);
        return ProductoResponse.from(repo.save(p));
    }

    public ProductoResponse actualizar(Long empresaId, Long id, ProductoRequest req) {
        Producto p = entidad(empresaId, id);
        if (repo.existsByIdentificadorAndEmpresaIdAndIdNot(req.identificador(), empresaId, id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ya existe un producto con ese identificador");
        }
        copiar(req, p);
        return ProductoResponse.from(repo.save(p));
    }

    private void copiar(ProductoRequest req, Producto p) {
        p.setIdentificador(req.identificador());
        p.setNombre(req.nombre());
        p.setCategoria(req.categoria());
        p.setPrecio(req.precio());
        p.setStock(req.stock());
        p.setStockMinimo(req.stockMinimo());
    }

    @Transactional
    public ProductoResponse moverStock(Long empresaId, Long id, int cantidad) {
        Producto p = entidad(empresaId, id);
        int nuevo = p.getStock() + cantidad;
        if (nuevo < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "No hay stock suficiente (quedan " + p.getStock() + ")");
        }
        p.setStock(nuevo);
        return ProductoResponse.from(p);
    }

    public void eliminar(Long empresaId, Long id) {
        repo.delete(entidad(empresaId, id));
    }
}
