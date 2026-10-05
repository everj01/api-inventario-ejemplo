package com.tienda.inventario.config;

import com.tienda.inventario.model.Empresa;
import com.tienda.inventario.model.Producto;
import com.tienda.inventario.model.Usuario;
import com.tienda.inventario.repository.EmpresaRepository;
import com.tienda.inventario.repository.ProductoRepository;
import com.tienda.inventario.repository.UsuarioRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import java.math.BigDecimal;

@Configuration
public class AppConfig {

    /** Datos iniciales: un usuario de soporte, una empresa demo con su administrador y productos de ejemplo. */
    @Bean
    public CommandLineRunner datosIniciales(UsuarioRepository usuarios, EmpresaRepository empresas,
                                            ProductoRepository productos, PasswordEncoder encoder) {
        return args -> {
            if (usuarios.count() > 0) return;

            Usuario soporte = new Usuario();
            soporte.setNombre("Soporte Técnico");
            soporte.setUsername("soporte");
            soporte.setPassword(encoder.encode("soporte123"));
            soporte.setRol(Usuario.Rol.SUPERADMIN);
            usuarios.save(soporte);

            Empresa demo = new Empresa();
            demo.setNombre("Bodega Demo");
            demo.setRuc("20000000001");
            empresas.save(demo);

            Usuario admin = new Usuario();
            admin.setNombre("Administrador Demo");
            admin.setUsername("admin");
            admin.setPassword(encoder.encode("admin123"));
            admin.setRol(Usuario.Rol.ADMIN);
            admin.setEmpresa(demo);
            usuarios.save(admin);

            productos.save(producto(demo, "ABA-001", "Arroz Costeño 1kg", "Abarrotes", "5.50", 80, 10));
            productos.save(producto(demo, "ABA-002", "Azúcar Rubia 1kg", "Abarrotes", "4.20", 60, 10));
            productos.save(producto(demo, "ABA-003", "Aceite Primor 1L", "Abarrotes", "9.90", 40, 8));
            productos.save(producto(demo, "BEB-001", "Coca-Cola 500ml", "Bebidas", "3.50", 100, 15));
            productos.save(producto(demo, "BEB-002", "Agua San Luis 625ml", "Bebidas", "1.80", 120, 20));
            productos.save(producto(demo, "LAC-001", "Leche Gloria Evaporada", "Lácteos", "4.80", 50, 10));
            productos.save(producto(demo, "LIM-001", "Detergente Ace 1kg", "Limpieza", "12.50", 25, 5));
            productos.save(producto(demo, "LIM-002", "Papel Higiénico Suave x4", "Limpieza", "8.90", 5, 10));
            productos.save(producto(demo, "SNK-001", "Galletas Casino", "Snacks", "1.50", 150, 20));
            productos.save(producto(demo, "SNK-002", "Papas Lays 45g", "Snacks", "3.00", 2, 10));

            System.out.println(">>> Soporte: soporte / soporte123   |   Empresa demo: admin / admin123   |   10 productos de ejemplo creados");
        };
    }

    private Producto producto(Empresa empresa, String identificador, String nombre, String categoria,
                              String precio, int stock, int stockMinimo) {
        Producto p = new Producto();
        p.setEmpresa(empresa);
        p.setIdentificador(identificador);
        p.setNombre(nombre);
        p.setCategoria(categoria);
        p.setPrecio(new BigDecimal(precio));
        p.setStock(stock);
        p.setStockMinimo(stockMinimo);
        return p;
    }
}
