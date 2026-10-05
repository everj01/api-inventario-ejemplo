package com.tienda.inventario.security;

import com.tienda.inventario.model.Usuario;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

/** Crea y verifica los tokens. El token guarda quien eres y a que empresa perteneces. */
@Service
public class JwtService {

    private final SecretKey clave;
    private final long expiracionMs;

    public JwtService(@Value("${app.jwt.secret}") String secret,
                      @Value("${app.jwt.expiracion-minutos}") long minutos) {
        this.clave = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.expiracionMs = minutos * 60_000;
    }

    public String generar(Usuario u) {
        Date ahora = new Date();
        var builder = Jwts.builder()
                .subject(u.getUsername())
                .claim("uid", u.getId())
                .claim("nombre", u.getNombre())
                .claim("rol", u.getRol().name())
                .issuedAt(ahora)
                .expiration(new Date(ahora.getTime() + expiracionMs));
        if (u.getEmpresa() != null) {
            builder.claim("empresaId", u.getEmpresa().getId());
        }
        return builder.signWith(clave).compact();
    }

    /** Devuelve null si el token es invalido o expiro. */
    public UsuarioAutenticado leer(String token) {
        try {
            Claims c = Jwts.parser().verifyWith(clave).build()
                    .parseSignedClaims(token).getPayload();
            Number empresaId = c.get("empresaId", Number.class);
            return new UsuarioAutenticado(
                    c.get("uid", Number.class).longValue(),
                    c.getSubject(),
                    c.get("nombre", String.class),
                    Usuario.Rol.valueOf(c.get("rol", String.class)),
                    empresaId != null ? empresaId.longValue() : null);
        } catch (Exception e) {
            return null;
        }
    }
}
