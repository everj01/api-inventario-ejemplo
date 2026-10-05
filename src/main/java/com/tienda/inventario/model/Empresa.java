package com.tienda.inventario.model;

import jakarta.persistence.*;
import java.util.UUID;

/** Cada empresa es un "inquilino" (tenant): sus productos y usuarios no se cruzan con los de otra. */
@Entity
@Table(name = "empresas")
public class Empresa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Identificador publico, para integraciones de terceros. */
    @Column(nullable = false, unique = true, length = 36, updatable = false)
    private String uuid;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 20)
    private String ruc;

    @Column(nullable = false)
    private boolean activo = true;

    @PrePersist
    void alCrear() {
        if (uuid == null) uuid = UUID.randomUUID().toString();
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getUuid() { return uuid; }
    public void setUuid(String uuid) { this.uuid = uuid; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getRuc() { return ruc; }
    public void setRuc(String ruc) { this.ruc = ruc; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
