package jpolanco.applicationcore.user.infrastructure.components.providers;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jpolanco.applicationcore.user.application.ports.input.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtManager implements JwtProvider {

    private static final Logger logger = LoggerFactory.getLogger(JwtManager.class);

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration.access}")
    private long accessExpiration;

    @Value("${jwt.expiration.refresh}")
    private long refreshExpiration;

    private SecretKey signInKey;
    private JwtParser jwtParser;

    @PostConstruct
    public void init() {
        this.signInKey = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
        this.jwtParser = Jwts.parser().verifyWith(signInKey).build();
    }

    @Override
    public String generateAccessToken(UUID id, String subject, List<String> roles) {
        return buildToken(id.toString(), subject, roles, accessExpiration);
    }

    @Override
    public String generateRefreshToken(UUID id, String subject, List<String> roles) {
        return buildToken(id.toString(), subject, roles, refreshExpiration);
    }


    private String buildToken(final String id, final String subject, final List<String> roles, final long expirationTime) {
        return Jwts.builder()
                .id(id)
                .claim("roles", roles)
                .subject(subject)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(signInKey)
                .compact();
    }

    @Override
    public Optional<String> extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public Optional<UUID> extractId(String token) {
        Optional<String> id = extractClaim(token, Claims::getId);
        if (id.isEmpty()) return Optional.empty();
        try {
            return Optional.of(UUID.fromString(id.get()));
        } catch (IllegalArgumentException e) {
            logger.warn("ID extraído del JWT no es un UUID válido: {}", id.get());
            return Optional.empty();
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public Optional<List<String>> extractRoles(String token) {
        return extractClaim(token, claims -> claims.get("roles", List.class));
    }

    private <T> Optional<T> extractClaim(String token, java.util.function.Function<Claims, T> claimsResolver) {
        try {
            Claims claims = jwtParser.parseSignedClaims(token).getPayload();
            return Optional.ofNullable(claimsResolver.apply(claims));
        } catch (SignatureException e) {
            logger.warn("JWT signature inválida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.warn("JWT token malformado: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.warn("JWT token expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.warn("JWT token no soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.warn("JWT claims string vacío o nulo: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Error inesperado al extraer claim del JWT: {}", e.getMessage(), e);
        }
        return Optional.empty();
    }

    @Override
    public boolean isTokenValid(String token, String email) {
        Optional<String> extractedUsername = extractUsername(token);
        return extractedUsername.isPresent() && extractedUsername.get().equals(email) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        Optional<Date> expiration = extractClaim(token, Claims::getExpiration);
        return expiration.map(exp -> exp.before(new Date())).orElse(true);
    }

    public static JwtManager setConfig(String secret, long accessExpiration, long refreshExpiration) {
        JwtManager jwtManager = new JwtManager();
        jwtManager.secret = secret;
        jwtManager.accessExpiration = accessExpiration;
        jwtManager.refreshExpiration = refreshExpiration;
        jwtManager.init();
        return jwtManager;
    }
}