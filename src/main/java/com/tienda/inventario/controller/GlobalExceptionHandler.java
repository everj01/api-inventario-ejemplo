package com.tienda.inventario.controller;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ResponseStatusException;
import java.util.LinkedHashMap;
import java.util.Map;

/** Respuestas de error uniformes: { "mensaje": "...", "errores": { campo: mensaje } } */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> validacion(MethodArgumentNotValidException ex) {
        Map<String, String> errores = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(e -> {
            // "empresa.nombre" -> "nombre", para que el frontend lo encuentre por nombre de campo
            String campo = e.getField().substring(e.getField().lastIndexOf('.') + 1);
            errores.putIfAbsent(campo, e.getDefaultMessage());
        });
        return ResponseEntity.badRequest()
                .body(Map.of("mensaje", "Revisa los datos del formulario", "errores", errores));
    }

    @ExceptionHandler(ResponseStatusException.class)
    public ResponseEntity<Map<String, Object>> estado(ResponseStatusException ex) {
        String mensaje = ex.getReason() != null ? ex.getReason() : "Ocurrió un error";
        return ResponseEntity.status(ex.getStatusCode()).body(Map.of("mensaje", mensaje));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, Object>> integridad(DataIntegrityViolationException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of("mensaje", "Ese dato ya existe o viola una restricción de la base de datos"));
    }
}
