package jpolanco.springbootapp.user.infrastructure.adapters.output.mysql;

import jpolanco.springbootapp.shared.utils.TokenStatus;
import jpolanco.springbootapp.user.application.ports.output.JwtRepository;
import jpolanco.springbootapp.user.application.utils.TokenE;
import jpolanco.springbootapp.user.infrastructure.adapters.mappers.entity.TokenEntityMapper;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.UserEntity;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaTokenRepository;
import jpolanco.springbootapp.user.infrastructure.adapters.output.repository.JpaUserRepository;
import jpolanco.springbootapp.user.infrastructure.errors.UserIntegrity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class TokenRepositoryMySQL implements JwtRepository {
    private final JpaTokenRepository jpaTokenRepository;
    private final JpaUserRepository jpaUserRepository;
    private final TokenEntityMapper tokenEntityMapper;

    @Override
    public Optional<TokenE> findByToken(String token) {
        return jpaTokenRepository.findByToken(token)
                .map(tokenEntityMapper::toDomain);
    }

    @Override
    public void deleteAllByUserId(String userId) {
        jpaTokenRepository.deleteAllByUserId(UUID.fromString(userId));
    }

    @Override
    public void revokeByToken(String token) {
        var maybeToken = jpaTokenRepository.findByToken(token);
        if (maybeToken.isPresent()) {
            var tokenEntity = maybeToken.get();
            tokenEntity.setStatus(TokenStatus.REVOKED);
            jpaTokenRepository.save(tokenEntity);
        } else {
            throw new UserIntegrity("Token not found", 404);
        }
    }

    @Override
    public List<TokenE> findAllByUserId(String userId) {
        return jpaTokenRepository.findAllByUserId(UUID.fromString(userId))
                .stream()
                .map(tokenEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void saveAll(List<TokenE> tokens) {
        Set<UUID> userIds = tokens.stream()
                .map(TokenE::getToken)
                .map(UUID::fromString)
                .collect(Collectors.toSet());

        Map<UUID, UserEntity> userEntityMap = jpaUserRepository.findAllById(userIds).stream()
                .collect(Collectors.toMap(UserEntity::getId, Function.identity()));

        for (var tokenE : tokens) {
            var tokenEntity = new TokenEntity();
            tokenEntity.setToken(tokenE.getToken());
            var maybeUser = Optional.ofNullable(userEntityMap.get(UUID.fromString(tokenE.getUserId())));
            if (maybeUser.isEmpty()) {
                throw new UserIntegrity("User not found with ID: " + tokenE.getUserId(), 404);
            }
            userIds.add(maybeUser.get().getId());
            tokenEntity.setUser(maybeUser.get());
            tokenEntity.setStatus(tokenE.getStatus());
            jpaTokenRepository.save(tokenEntity);
        }
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
    public List<TokenE> findAll() {
        return jpaTokenRepository.findAll()
                .stream()
                .map(tokenEntityMapper::toDomain)
                .toList();
    }

    @Override
    public void revokeAllByUserId(String userId) {
        var userUUID = UUID.fromString(userId);
        int updatedCount = jpaTokenRepository.revokeAllByUserId(userUUID);
        if (updatedCount == 0) {
            throw new UserIntegrity("No tokens found for user ID: " + userId, 404);
        }
    }

    @Override
    public TokenE save(TokenE entity) {
        var tokenEntity = tokenEntityMapper.toEntity(entity);
        var savedEntity = jpaTokenRepository.save(tokenEntity);
        return tokenEntityMapper.toDomain(savedEntity);
    }

    @Override
    public Optional<TokenE> findById(Long s) {
        return jpaTokenRepository.findById(s)
                .map(tokenEntityMapper::toDomain);
    }

    @Override
    public void deleteById(Long s) {
        jpaTokenRepository.deleteById(s);
    }
}
