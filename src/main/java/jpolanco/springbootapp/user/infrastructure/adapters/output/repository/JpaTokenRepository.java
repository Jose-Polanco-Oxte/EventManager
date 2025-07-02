package jpolanco.springbootapp.user.infrastructure.adapters.output.repository;


import jpolanco.springbootapp.user.application.utils.TokenStatus;
import jpolanco.springbootapp.user.infrastructure.adapters.output.persistence.TokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface JpaTokenRepository extends JpaRepository<TokenEntity, Long> {
    Optional<TokenEntity> findByToken(String token);

    void deleteAllByUserId(Long userId);

    List<TokenEntity> findAllByUserId(Long userId);

    @Query("SELECT COUNT(t) FROM tokens t WHERE t.user.id = ?1 AND t.status = 'ACTIVE'")
    int countByUserIdAndStatusIsActive(Long userId);

    void deleteAllByStatus(TokenStatus status);

    @Modifying
    @Query("""
              UPDATE tokens t SET t.status = 'REVOKED' WHERE t.user.id = ?1
    """)
    int revokeAllByUserId(Long userId);

    void deleteByToken(String token);
}
