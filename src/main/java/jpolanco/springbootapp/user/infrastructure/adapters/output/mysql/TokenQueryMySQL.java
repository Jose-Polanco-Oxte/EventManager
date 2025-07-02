package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.user.application.ports.output.JwtQueryRepository;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.TokenEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class TokenQueryMySQL implements JwtQueryRepository {
    private final JpaTokenRepository jpaTokenRepository;
    private final TokenEntityMapper tokenEntityMapper;

    @Override
    public Optional<TokenE> findByToken(String token) {
        return jpaTokenRepository.findByToken(token)
                .map(tokenEntityMapper::fromPersistence);
    }

    @Override
    public List<TokenE> findAllByUserId(Long userId) {
        return jpaTokenRepository.findAllByUserId(userId)
                .stream()
                .map(tokenEntityMapper::fromPersistence)
                .toList();
    }

    @Override
    public int countSessionsByUserId(Long userId) {
        return jpaTokenRepository.countByUserIdAndStatusIsActive(userId);
    }

    @Override
    public List<TokenE> findAll() {
        return jpaTokenRepository.findAll()
                .stream()
                .map(tokenEntityMapper::fromPersistence)
                .toList();
    }
}
