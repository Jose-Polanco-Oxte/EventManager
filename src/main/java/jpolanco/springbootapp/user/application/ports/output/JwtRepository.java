package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.application.adapters.output.CRUDRepository;
import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.user.application.utils.TokenE;

import java.util.List;
import java.util.Optional;

public interface JwtRepository extends CRUDRepository<TokenE, Long> {
    Optional<TokenE> findByToken(String token);
    void deleteAllByUserId(String userId);
    void revokeByToken(String token);
    List<TokenE> findAllByUserId(String userId);
    void saveAll(List<TokenE> tokens);
    int countSessionsByUserId(String userId);
    void deleteAllByStatus(TokenStatus status);
    List<TokenE> findAll();
    void revokeAllByUserId(String userId);
}