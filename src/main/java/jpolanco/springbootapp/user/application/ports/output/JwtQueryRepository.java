package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.user.application.utils.TokenE;

import java.util.List;
import java.util.Optional;

public interface JwtQueryRepository {
    Optional<TokenE> findByToken(String token);
    List<TokenE> findAllByUserId(Long userId);
    int countSessionsByUserId(Long userId);
    List<TokenE> findAll();
}
