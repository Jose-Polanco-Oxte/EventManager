package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.shared.domain.TokenStatus;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import jpolanco.springbootapp.user.domain.model.Token;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.TokenEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RequiredArgsConstructor
@Repository
public class TokenRepositoryMySQL implements JwtRepository {
    private final JpaTokenRepository jpaTokenRepository;
    private final TokenEntityMapper tokenEntityMapper;

    @Override
    public Optional<Token> findByToken(String token) {
        return jpaTokenRepository.findByToken(token)
                .map(tokenEntityMapper::toDomain);
    }

    @Override
    public void deleteAllByUserId(String userId) {
        jpaTokenRepository.deleteAllByUserId(UUID.fromString(userId));
    }

    @Override
    public List<Token> findAllByUserId(String userId) {
        return jpaTokenRepository.findAllByUserId(UUID.fromString(userId))
                .stream()
                .map(tokenEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void save(Token token) {
        var tokenEntity = tokenEntityMapper.toEntity(token);
        jpaTokenRepository.save(tokenEntity);
    }

    @Override
    public void saveAll(List<Token> tokens) {
        var tokenEntities = tokens.stream()
                .map(tokenEntityMapper::toEntity)
                .toList();
        jpaTokenRepository.saveAll(tokenEntities);
    }

    @Override
    public int countSessionsByUserId(String userId) {
        return jpaTokenRepository.countByUserIdAndStatusIsActive(UUID.fromString(userId));
    }

    @Override
    public void deleteAllByStatus(TokenStatus status) {
        jpaTokenRepository.deleteAllByStatus(status);
    }

    @Override
    public List<Token> findAll() {
        return jpaTokenRepository.findAll()
                .stream()
                .map(tokenEntityMapper::toDomain)
                .toList();
    }
}
