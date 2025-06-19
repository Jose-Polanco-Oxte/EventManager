package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.user.domain.model.Token;

import java.util.List;
import java.util.Optional;

public interface JwtRepository {
    Optional<Token> findByToken(String token);
    void deleteAllByUserId(String userId);
    List<Token> findAllByUserId(String userId);
    void save(Token token);
    void saveAll(List<Token> tokens);
    int countSessionsByUserId(String userId);
    void deleteAllByStatus(TokenStatus status);
    List<Token> findAll();
}
