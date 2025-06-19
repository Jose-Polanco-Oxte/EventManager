package jpolanco.springbootapp.user.infrastructure.components.interfaces;

import jpolanco.springbootapp.user.domain.model.User;

public interface JwtProvider {
    String generateAccessToken(User user);
    String generateRefreshToken(User user);
    String extractUsername(String token);
    boolean isTokenValid(String token, String email);
    boolean isTokenExpired(String token);
}
