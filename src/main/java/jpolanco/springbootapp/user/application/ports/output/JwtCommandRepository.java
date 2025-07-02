package jpolanco.springbootapp.user.application.ports.output;

import jpolanco.springbootapp.shared.application.adapters.output.CUDRepository;
import jpolanco.springbootapp.user.application.utils.TokenStatus;
import jpolanco.springbootapp.user.application.utils.TokenE;

import java.util.List;

public interface JwtCommandRepository extends CUDRepository<TokenE, String> {
    void deleteAllByUserId(Long userId);
    void revokeByToken(String token);
    void saveAll(List<TokenE> tokens);
    void deleteAllByStatus(TokenStatus status);
    void revokeAllByUserId(Long userId);
}
